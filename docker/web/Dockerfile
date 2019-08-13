FROM openjdk:11
# xmlstarlet is useful when modifying attributes/elements
# saxon can be used to execute configuration transformation using XSLT
# augeas is a great tool to edit any configuration files (XML too)
# bsdtar can be used to unpack zip files using pipes
#ENV JAVA_HOME /usr
#ENV MVNW_VERBOSE true
RUN mkdir -p /usr/src/app
RUN chmod 777 /usr/src/app
ADD . /usr/src/app/
WORKDIR /usr/src/app
RUN ./mvnw clean package
ENTRYPOINT ["java","-Xmx4096m","-jar","/usr/src/app/target/amazing-api-0.0.1-SNAPSHOT.jar"]
# Expose the ports we're interested in
EXPOSE 8080 