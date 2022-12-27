FROM adoptopenjdk/openjdk11@sha256:5454cc66dfdb61e8292051d6d9f4ef2c220b468171dc7808c9689acceba3f2f9

RUN mkdir /app
WORKDIR /app

COPY ./build/libs/*.jar /app/app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
