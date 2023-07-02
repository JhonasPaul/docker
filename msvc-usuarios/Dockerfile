FROM tomee:jre17-webprofile

#ruta de trabajo
WORKDIR /app

#copiamos la ruta del jar en la carpeta app
COPY ./target/msvc-usuarios-0.0.1-SNAPSHOT.jar .

EXPOSE 8001

#comando a ejecutar
ENTRYPOINT ["java", "-jar", "msvc-usuarios-0.0.1-SNAPSHOT.jar"]
