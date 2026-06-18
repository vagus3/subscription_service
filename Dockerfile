FROM node:20 AS frontend

WORKDIR /app

COPY package*.json ./
RUN npm ci

COPY vite.config.js ./
COPY src/main/frontend ./src/main/frontend

RUN npm run build


FROM eclipse-temurin:17-jdk AS build

WORKDIR /app

COPY . .

COPY --from=frontend \
/app/src/main/resources/static \
./src/main/resources/static

RUN chmod +x ./gradlew

RUN ./gradlew bootJar --no-daemon


FROM eclipse-temurin:17-jre

WORKDIR /app

ENV SPRING_PROFILES_ACTIVE=cloud

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]