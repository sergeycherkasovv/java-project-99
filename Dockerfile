FROM gradle:8.7-jdk21

WORKDIR /

COPY / .

RUN gradle installDist

CMD java -jar build/libs/HexletSpringBlog-1.0-SNAPSHOT.jar