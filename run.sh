#!/bin/bash

echo "===================================="
echo "   Spring Boot 애플리케이션 시작"
echo "===================================="
echo

echo "[1/4] Git 저장소 업데이트 중..."

# master 브랜치에서 최신 변경사항 가져오기
git pull origin master
if [ $? -ne 0 ]; then
    echo "Git pull 실패!"
    exit 1
fi

echo
echo "[2/4] 파일 권한 설정 중..."

# gradlew 실행 권한 부여
chmod +x gradlew
if [ $? -eq 0 ]; then
    echo "gradlew 실행 권한 부여 완료"
else
    echo "gradlew 권한 설정 실패"
fi

# run.sh 실행 권한 부여
chmod +x run.sh
if [ $? -eq 0 ]; then
    echo "run.sh 실행 권한 부여 완료"
else
    echo "run.sh 권한 설정 실패"
fi

echo
echo "[3/4] 프로젝트 빌드 중..."
./gradlew clean build -x test
if [ $? -ne 0 ]; then
    echo "빌드 실패!"
    exit 1
fi

echo
echo "[4/4] 애플리케이션 실행 중..."
echo "서버가 시작되면 http://localhost:8080 으로 접속하세요"
echo "종료하려면 Ctrl+C를 누르세요"
echo

java -jar build/libs/jeja-0.0.1-SNAPSHOT.jar

echo
echo "애플리케이션이 종료되었습니다."
