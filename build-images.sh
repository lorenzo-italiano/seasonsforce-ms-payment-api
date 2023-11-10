mvn clean install

mv target/seasonsforce-ms-payment-api-1.0-SNAPSHOT.jar api-image/seasonsforce-ms-payment-api-1.0-SNAPSHOT.jar

cd api-image

docker build -t payment-api .

cd ../postgres-image

docker build -t payment-db .
