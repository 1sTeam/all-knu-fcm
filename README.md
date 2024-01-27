# all-knu-fcm

## 기본 secret 환경 구성
### resources/secrets/firebase/all-knu-firebase-adminsdk.json
```json
...
```
### resources/secrets/test-secrets.properties
```properties
fcm.token=..
```

## gradle build
```bash
./gradlew bootJar -x test
```

## docker build
```bash
gradle build 후 진행
docker build -t all-knu-fcm .
```

## docker run
프로파일을 지정하여 docker 컨테이너 생성
```bash
docker run -e "SPRING_PROFILES_ACTIVE=local" -d -p 8080:8080 -t all-knu-fcm
```
### option
- 컨테이너 이름 지정 `--name`
- docker network 지정 `--net`
