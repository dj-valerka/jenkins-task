FROM openjdk:17
EXPOSE 8080
ADD target/spring-petclinic-*.jar petclinic.jar
ENTRYPOINT ["java","-jar","/petclinic.jar"]
