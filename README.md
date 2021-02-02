# soen-345

## Assignment 1 Instructions

All commands to be run from root of this repository.

1. Build the docker image and run it with docker-compose so as to add the volumes specified in `docker-compose.yml` in the root dir of this repo.

``` bash
docker build -t soen345ass1-image -f Dockerfile .

docker-compose up
```

1. Review coverage reports by opening the following files in your browser:

   - `ass1_out_part_a/part_a/target/jacoco-ut/index.html`
   - `ass1_out_part_c/part_c/target/jacoco-ut/index.html`
   - `ass1_out_part_d/part_d/commons-io-2.8.0-src/target/site/clover/index.html`
