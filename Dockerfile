FROM openjdk

EXPOSE 8080

ADD target/assignment-platform.jar assignment-platform.jar

CMD ["java", "-Dfile.encoding=UTF-8", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/assignment-platform.jar"]