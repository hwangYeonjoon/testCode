# 기본 이미지로 OpenJDK를 사용합니다. ARM64 아키텍처를 지원하는 이미지를 선택하세요.
FROM openjdk:17-alpine

# 작업 디렉토리를 설정합니다.
WORKDIR /app

# JAR 파일을 컨테이너로 복사합니다.
COPY build/libs/pushtest-0.0.1-SNAPSHOT.jar /app/app.jar

# 애플리케이션을 실행합니다.
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
