# 1. 빌드 스테이지 (멀티 스테이지 빌드)
FROM gradle:8.4-jdk17 AS builder
WORKDIR /app

# 전체 프로젝트 복사
COPY . .

# bootJar 이름 고정 (build.gradle.kts 에서 설정 필요)
# bootJar { archiveFileName = "api.jar" }

# API 모듈 빌드
RUN ./gradlew :api:bootJar --no-daemon

# 2. 실행 스테이지
FROM openjdk:17-jdk-slim
WORKDIR /app

# 빌드 결과 복사
COPY --from=builder /app/api/build/libs/api.jar /app/api.jar

# ✅ Oracle Wallet 복사 (로컬의 wallet 디렉토리 → 이미지 내부로)
COPY ./wallet /app/wallet

# ✅ 환경 변수 설정
ENV TNS_ADMIN=/app/wallet

# 필요 시 포트 개방
EXPOSE 800

# ✅ Oracle 설정 포함한 실행 명령
ENTRYPOINT ["java", "-Doracle.net.tns_admin=/app/wallet", "-jar", "/app/api.jar"]
