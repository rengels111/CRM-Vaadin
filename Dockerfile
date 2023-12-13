FROM eclipse-temurin:17-jre
WORKDIR /
ADD target/flowcrmtutorial-1.0-SNAPSHOT.jar app.jar
RUN useradd -m myuser
USER myuser
EXPOSE 8090
CMD java -jar -Dspring.profiles.active=prod app.jar

#VOLUME /tmp
#ARG JAR_FILE
#EXPOSE 8090
#COPY target/*.jar app.jar
#ENTRYPOINT ["java", "-jar", "/app.jar"]