# Harpocrates

## Introduction

According to Wikipedia, Harpocrates is the Greek god of silence, secrets, and confidentiality. [Wikipedia](https://en.wikipedia.org/wiki/Harpocrates)

Cryptography, specifically encryption, is the process of making text unintelligible, or "making it secret" [Fruhlinger](https://www.csoonline.com/article/569921/what-is-cryptography-how-algorithms-keep-information-secret-and-safe.html).

`harpocrates-web-backend` is a Spring Boot application that provides functionality to create encrypted secrets, with a set amount of views, linked to a single ID. This ID is the key - if you have the ID, you can fetch and view the secret contents. You can also burn any secret by its ID as well.

## The "Why"

I wanted to create a secret-share site like https://onetimesecret.com. Harpocrates is a personal side project that allowed me to migrate away from Spring 2.7 and experiment with Spring 3.

The goal is to make it simple, so that it can be easily ran locally, via a container, or using `docker-compose`. The goal is to allow someone to pull down the repo, then run it easily.

## Getting Started

Harpocrates is available to be run locally, as a Docker container, or via `docker-compose`.

### Configuration

Harpocrates relies on the following configuration to work correctly:

- `SERVER_PORT` - defaults to 8080 if not provided
- `HARPOCRATES_SECRET_KEY` - highly recommended that a custom, 16 character string is used here
- `HARPOCRATES_INIT_VECTOR` - highly recommended that a custom, 16 character string is used here
- `REDIS_DATABASE` - defaults to `0` if not provided
- `REDIS_HOST` - defaults to `0.0.0.0` if not provided
- `REDIS_PORT` - defaults to `6379` if not provided
- `REDIS_PASSWORD` - highly recommended that a custom password is provided
- `REDIS_TIMEOUT` - defaults to 60000 if not provided

### Local

To run locally, first you'll need to ensure you have a Redis connection available.

Set the relevant environment variables to work with Harpocrates configuration, then run the app.

### Docker Container

To run as a Docker container, you will first need to build the project with `gradle`. 

A `Makefile` command `make app/build` is provided to simplify this. 

You can use the provided `Dockerfile` to build the image, then run the container with the relevant environment variables to work with Harpocrates configuration.

### Docker Compose

To run with `docker-compose`, you can utilize the provided `docker-compose.yml` file.

First, you'll need to build the project with `gradle`.

A `Makefile` command `make app/build` is provided to simplify this.

Then, you can use the `docker-compose.yml` file and provide it your configuration to work with Harpocrates.

Alternatively, you can create a `.env` file and fill in the relevant configuration properties there, then use the `Makefile` command `make docker/compose-with-env` if your `.env` file is in the root. 

Otherwise, you can use `docker-compose` and point it at your `.env` file if it's in a different location: `docker compose --env-file <your .env file> up --build`.

An example of an `.env` file can be based off the `.env.example` file.

## Encryption Information

A codec is a device or program that encodes or decodes data.

The encryption codec used in Harpocrates is configured to use the `AES` algorithm, specifically the `AES/CBC/PKCS5Padding` transformation.

The codec relies on two pieces of configuration:
* A secret key
* An init vector

Both the secret key and the init vector must be 16 byte strings, otherwise `InvalidKeyException` or `InvalidAlgorithmParameterException` exceptions can be encountered.

The secret key and init vector are tied to the following application properties:

- `harpocrates.secret.key`
- `harpocrates.init.vector`

## API

The Swagger UI interface can be found at `/api/swagger-ui/index.html`
The OpenAPI specification can be found at `/api/docs`

## Roadmap

- Allow for other persistence sources to be used, like Postgres or MySQL
- Improve READMe
- Add vulnerability monitoring
- Add code quality monitoring
- Add lifetime to secrets, automatically burning them once the lifetime has been reached

## References

Harpocrates. (n.d.). In Wikipedia. Retrieved August 6, 2023, from https://en.wikipedia.org/wiki/Harpocrates

Fruhlinger, J. (2022, May 22). What is cryptography? How algorithms keep information secret and safe. https://www.csoonline.com/article/569921/what-is-cryptography-how-algorithms-keep-information-secret-and-safe.html
