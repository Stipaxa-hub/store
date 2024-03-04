FROM openjdk:17-jdk-alpine as builder
WORKDIR jv-book-store
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} book-store.jar
RUN java -Djarmode=layertools -jar book-store.jar extract

FROM openjdk:17-jdk-alpine
WORKDIR jv-book-store
COPY --from=builder jv-book-store/dependencies/ ./
COPY --from=builder jv-book-store/spring-boot-loader/ ./
COPY --from=builder jv-book-store/snapshot-dependencies/ ./
COPY --from=builder jv-book-store/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
EXPOSE 8080
