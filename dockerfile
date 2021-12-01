FROM maven:3.8.2-jdk-8

WORKDIR /starbux
COPY . .
RUN mvn package -Dmaven.test.skip

CMD mvn spring-boot:run