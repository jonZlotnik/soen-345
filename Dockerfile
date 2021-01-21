FROM maven:3.6.3-openjdk-15

# Experiments - Part A

RUN mkdir -p ${USER_HOME_DIR}/ass1 ${USER_HOME_DIR}/ass1/part-a

COPY ass1-part-a/ ${USER_HOME_DIR}/ass1/part-a

RUN cd ${USER_HOME_DIR}/ass1/part-a \
  && mvn clean \
  && mvn compile \
  && mvn test


# Experiments Part B


# ENTRYPOINT ["/usr/local/bin/mvn-entrypoint.sh"]
# CMD ["ls"]
