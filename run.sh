#!/bin/bash

set -e
set -u
set -o pipefail

# 환경 변수 설정
PROFILE=${SPRING_PROFILES_ACTIVE:-prod}
JVM_OPTS=${JVM_OPTS:-"-Xms512m -Xmx1024m"}

echo "===================================="
echo "   Spring Boot 애플리케이션 시작"
echo "===================================="
echo "실행 환경: $PROFILE"
echo

echo "[0/5] 기존 프로세스 확인 및 종료..."
# 기존 프로세스 종료 로직 (위에서 제시한 코드)

echo "[1/5] Git 저장소 업데이트 중..."
git pull origin master

echo "[2/5] 파일 권한 설정 중..."
chmod +x gradlew run.sh

echo "[3/5] 프로젝트 빌드 중..."
./gradlew clean build -x test

echo "[4/5] 애플리케이션 실행 중..."
JAR_FILE=$(find build/libs -name "*.jar" -not -name "*-plain.jar" | head -1)

if [ -z "$JAR_FILE" ]; then
    echo "실행 가능한 JAR 파일을 찾을 수 없습니다!"
    exit 1
fi

mkdir -p logs

echo "실행할 JAR 파일: $JAR_FILE"
echo "서버가 시작되면 http://localhost:8080 으로 접속하세요"
echo "종료하려면 Ctrl+C를 누르세요"
echo

java $JVM_OPTS -Dspring.profiles.active=$PROFILE -jar "$JAR_FILE" 2>&1 | tee logs/application.log
