FROM java:8

MAINTAINER 573047307@qq.com

VOLUME /tmp

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

RUN mkdir -p /pig-auth

WORKDIR /feiqu-front

EXPOSE 8080

ADD feiqu-front.jar app.jar

CMD java -jar feiqu-front.jar
