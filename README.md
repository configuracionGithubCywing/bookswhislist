# Books Whislit API
This is API using Spring Boot, Postgresql running in docker containers defined by docker-compose.yml file.

# Requirements
Java 1.7

# Build and Run
Copy archive folder to Docker enabled server
Go to the containing folder and execute 
	> docker compose up

# Docker
The docker file defines a container based on Java with the jar created by spring package.

# Docker-compose
These containers have the 8081 port exposed for the API and 5432 port for postgresql

# Hola Mundo Test
{server}:8081/access/
