app/build:
	./gradlew clean build

app/start:
	docker container run --name harpocrates-web-backend -p 8080:8080 harpocrates-web-backend

docker/build:
	docker build -t harpocrates-web-backend .

docker/compose-with-env:
	docker compose --env-file .env up --build

redis/up:
	docker compose --env-file .env up --build harpocrates-redis-db