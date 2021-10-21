# all-knu-fcm

## maven run
프로파일과 실행 포트 지정
```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=local -Dspring-boot.run.jvmArguments='-Dserver.port=8081'
```

## maven build
```bash
./mvnw clean package -P local
```

## docker build
이미지 이름을 지정
```bash
./mvnw spring-boot:build-image -Dspring-boot.build-image.imageName=all-knu-fcm
```

## docker run
프로파일을 지정하여 docker 컨테이너 생성
```bash
docker run -e "SPRING_PROFILES_ACTIVE=local" -d -p 8080:8080 -t all-knu-fcm
```