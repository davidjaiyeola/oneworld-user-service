FROM adoptopenjdk/openjdk12
LABEL maintainer="David Jaiyeola<david.jaiyeola@gmail.com>"
# Add the service itself
ARG JAR_FILE
ADD target/oneworld-user-service-1.0-SNAPSHOT.jar /usr/share/user-service/oneworld-user-service-1.0-SNAPSHOT.jar
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /usr/share/user-service/oneworld-user-service-1.0-SNAPSHOT.jar" ]
