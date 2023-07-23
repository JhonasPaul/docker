FROM eclipse-temurin:17-jdk-jammy as builder

#ruta de trabajo
WORKDIR /app/msvc-cursos


COPY .mvn/ .mvn
COPY mvnw pom.xml ./

#RUN ./mvnw clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot.repackage.skip && rm -r ./target/
RUN ./mvnw dependency:go-offline
#copia el src del proyecto al src de la iamgen que tambien se encuentra en msvc-cursos
COPY src ./src
RUN ./mvnw clean package -DskipTests



FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
COPY --from=builder /app/msvc-cursos/target/msvc-cursos-0.0.1-SNAPSHOT.jar .
EXPOSE 8001
#comando a ejecutar, punto de entrada ejecuta este comando, una capa de ejecucion que le pertenece alcontenedor
#por ahora se pone target porque la ruta que tenia la ruta completa con el target se borro
CMD ["java", "-jar", "msvc-cursos-0.0.1-SNAPSHOT.jar"]
