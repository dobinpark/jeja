#!/bin/bash

echo "===================================="
echo "   Spring Boot 애플리케이션 시작"
echo "===================================="
echo

echo "[1/3] 이전 빌드 정리 중..."
./gradlew clean
if [ $? -ne 0 ]; then
    echo "정리 실패!"
    exit 1
fi

echo
echo "[2/3] 프로젝트 빌드 중..."
./gradlew build -x test
if [ $? -ne 0 ]; then
    echo "빌드 실패!"
    exit 1
fi

echo
echo "[3/3] 애플리케이션 실행 중..."
echo "서버가 시작되면 http://localhost:8080 으로 접속하세요"
echo "종료하려면 Ctrl+C를 누르세요"
echo

java -jar build/libs/jeja-0.0.1-SNAPSHOT.jar

echo
echo "애플리케이션이 종료되었습니다."
