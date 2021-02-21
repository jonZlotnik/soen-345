FROM maven:3.6.1-openjdk-8

ENV USER_HOME_DIR=/root



# # Assignment 1
# RUN mkdir -p ${USER_HOME_DIR}/ass1


# # Experiments - Part A

# RUN mkdir -p ${USER_HOME_DIR}/ass1/part_a

# COPY ass1-part-a/ ${USER_HOME_DIR}/ass1/part_a

# RUN cd ${USER_HOME_DIR}/ass1/part_a \
#   && mvn clean \
#   && mvn compile \
#   && mvn test


# # Experiments - Part B

# RUN mkdir -p ${USER_HOME_DIR}/ass1/part_b

# RUN cd ${USER_HOME_DIR}/ass1/part_b \
#   && curl https://httpd-mirror.sergal.org/apache//commons/io/source/commons-io-2.8.0-src.tar.gz --output apache-commons.tar.gz \
#   && tar -xf apache-commons.tar.gz \
#   && rm ${USER_HOME_DIR}/ass1/part_b/commons-io-2.8.0-src/pom.xml

# COPY ass1-part-b/pom.xml ${USER_HOME_DIR}/ass1/part_b/commons-io-2.8.0-src/pom.xml

# RUN cd ${USER_HOME_DIR}/ass1/part_b/commons-io-2.8.0-src/ \
#   && mvn clean \
#   && mvn compile \
#   && mvn test ; echo "done part b"


# # Experiments - Part C

# RUN cp -R ${USER_HOME_DIR}/ass1/part_b ${USER_HOME_DIR}/ass1/part_c \
#   && rm ${USER_HOME_DIR}/ass1/part_c/commons-io-2.8.0-src/pom.xml

# COPY ass1-part-c/pom.xml ${USER_HOME_DIR}/ass1/part_c/commons-io-2.8.0-src/pom.xml

# RUN cd ${USER_HOME_DIR}/ass1/part_c/commons-io-2.8.0-src/ \
#   && mvn clean \
#   && mvn compile \
#   && mvn test ; echo "done part c"


# # Experiments - Part D

# RUN cp -R ${USER_HOME_DIR}/ass1/part_c ${USER_HOME_DIR}/ass1/part_d

# COPY ass1-part-d/settings.xml ${USER_HOME_DIR}/.m2/settings.xml

# RUN cd ${USER_HOME_DIR}/ass1/part_d/commons-io-2.8.0-src/ \
#   && mvn clean clover:setup test clover:aggregate clover:clover ; echo "done part d"


# # Experiments - Part E

# RUN cp -R ${USER_HOME_DIR}/ass1/part_d ${USER_HOME_DIR}/ass1/part_e


# COPY ./ass1-entrypoint.sh /root/entrypoint.sh
# ENTRYPOINT ["/root/entrypoint.sh"]

# CMD ["/bin/bash"]
