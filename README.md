# Diarity 백엔드

## 소개

[Diarity](https://diarity.me)는 사용자가 자기가 읽은 책과 간단한 독후감을 기록하고 관리할 수 있는 웹 애플리케이션입니다.

이 레포지토리는 Diarity의 백엔드 서버를 Spring Boot로 구현한 것입니다.

자세한 개발기는 [altair의 프로젝트 일기](https://altair823.tistory.com/)를 참고해주세요.

## 백엔드 기술 스택

- Java 21
- Spring Boot 3.4.2
- Spring Data JPA
- jasypt 3.0.5
- flyway-mysql
- MySQL 8.0.41

## 설치 및 실행 방법

### Bare Metal

```bash
./gradlew build
java -jar build/libs/diarity-{version}.jar
```

### Docker

도커 이미지를 통한 실행은 Private Registry인 Harbor를 통해 이루어집니다.
이 Harbor 저장소에 대한 접근 권한이 필요한 경우 이슈에 남겨주세요.

```bash
docker login harbor.altair823.xyz -u ${HARBOR_USERNAME} -p ${HARBOR_PASSWORD}
docker pull harbor.altair823.xyz/diarity-be/diarity-be-springboot:{TAG}
docker run -d \
    --name diarity-be-springboot \
    --restart always -p 8080:8080 \
    -e JASYPT_ENCRYPTOR_PASSWORD={JASYPT_ENCRYPTOR_PASSWORD} \
    -e SPRING_PROFILES_ACTIVE=development \
    harbor.altair823.xyz/diarity-be/diarity-be-springboot:{TAG}
```
