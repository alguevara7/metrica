# Find out random unused TCP port
findRandomTcpPort(){
        port=$(( 1000+( $(od -An -N2 -i /dev/random) )%(1023+1) ))
        while :
        do
          (echo >/dev/tcp/localhost/$port) &>/dev/null &&  port=$(( 1000+( $(od -An -N2 -i /dev/random) )%(1023+1) )) || break
        done
        echo "$port"
}

SERVICE_NAME=${SERVICE_NAME-"metrica"}
SERVICE_PORT_HTTP=${1-$(findRandomTcpPort)}

# Expose to Consul and then set a tear-down trap to remove it.
curl -X POST -d '{"Address":"127.0.0.1", "Port": '${SERVICE_PORT_HTTP}', "Tags": ["http"], "Name": "'${SERVICE_NAME}'"}' "http://vm.dev.kjdev.ca:8500/v1/agent/service/register"
trap 'curl -X POST  "http://vm.dev.kjdev.ca:8500/v1/agent/service/deregister/${SERVICE_NAME}"' EXIT

export APPENDER=console
export CONSUL_HOST=vm.dev.kjdev.ca
export CONSUL_PORT=8600
export GRAPHITE_HOST=vm.dev.kjdev.ca
export GRAPHITE_PORT=2003

lein run-dev ${SERVICE_PORT_HTTP}
