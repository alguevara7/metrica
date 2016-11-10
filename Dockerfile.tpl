FROM registry.ecg.so/ca.ubuntu-mesos-jdk8:14.04-42

ADD target/metrica-${VERSION}-standalone.jar /
RUN ln -s /metrica-${VERSION}-standalone.jar metrica.jar

USER www-data
