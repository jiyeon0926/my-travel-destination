<p align="center">
  <img width="250" height="250" alt="my-travel-destination" src="https://github.com/user-attachments/assets/b147dc01-3d40-4237-9c74-79b0f84dd488" />
</p>

# My Travel Destination
- 사용자들이 작성한 여행 블로그를 한 곳에서 확인하고, 나의 다음 여행지를 발견하는 서비스

## 📅 프로젝트 기간
- 총 기간 : 2025-08-16 ~ 
- 설계 : 2025-08-16 ~ 2025-08-23
- BE 1차 기능 개발 : 2025-08-23 ~ 2025-09-30
- BE 2차 기능 개발 : 미정

## 🛠️ 기술 스택
- Java 21
- Spring Boot 3.5.4
- Spring Security
- Querydsl
- Kakaopay Open API
- Redis
- MySQL
- AWS S3

## 🖌️ 와이어프레임
- 1️⃣[사용자](https://www.figma.com/design/pz5HnNQ6CbxfQP8pICnl6K/my-travel-service?node-id=2-153&p=f&t=nUCVQhWyv5rZqjOm-0)
- 2️⃣[관리자](https://www.figma.com/design/pz5HnNQ6CbxfQP8pICnl6K/my-travel-service?node-id=94-839&p=f&t=nUCVQhWyv5rZqjOm-0)
- 3️⃣[업체](https://www.figma.com/design/pz5HnNQ6CbxfQP8pICnl6K/my-travel-service?node-id=94-838&p=f&t=nUCVQhWyv5rZqjOm-0)

## 📍 핵심 기능
- 여행지 검색
- 블로그

## 🏷️ 도메인별 기능
- 사용자 관리
- 업체 관리
- 티켓 관리
- 블로그
- 예약 및 결제

## 🗂️ 역할별 기능

### 0️⃣ 공통
- 로그인
- 로그아웃

### 1️⃣ 사용자
- 회원가입
- 회원탈퇴
- 프로필 변경
- 비밀번호 변경
- 여행지 검색
- 여행 티켓 검색
- 여행 티켓 예약 및 결제
- 블로그
- 예약 내역 확인

### 2️⃣ 관리자
- 업체 관리

### 3️⃣ 업체
- 비밀번호 변경
- 티켓 관리
- 예약 내역 확인

## 🧩 [ERD](https://dbdiagram.io/d/My-Travel-Destination-686676f8f413ba3508206ce6)

## 📝 [API 명세서](https://www.notion.so/My-Travel-Destination-2562f0d54a3480bcadeaed91521358fe?source=copy_link)

## 🔒 로그인 및 로그아웃
이 프로젝트의 핵심은 인증 기능이 아니므로, 로그인 연장 기능은 구현하지 않았습니다. <br/>
로그인 연장을 구현하려면 Refresh Token이 필요하지만, 현재 프로젝트에서는 해당 기능이 없으므로 Access Token만 발급하도록 설계하였습니다. <br/>
로그아웃 시에는 Access Token을 Redis에 저장하여, 이후 해당 토큰이 사용될 경우 유효하지 않은 것으로 판별되도록 처리하였습니다. <br/>
만료되기 전 토큰이어도 로그아웃 직후라면 인증이 불가능합니다.

## 🔍✏️ CQRS 패턴을 적용한 이유
보통 읽기 작업보다 쓰기 작업의 비즈니스 로직이 복잡합니다. </br>
복잡한 쓰기 작업의 로직을 Command 서비스에 집중시키면 읽기와 쓰기 작업의 로직이 분리되어 각 작업의 관심사가 명확해지고, 가독성과 유지보수성이 향상됩니다. </br>

## 🧾 문서
프로젝트 진행 과정에서 학습한 내용과 문제 해결 과정을 기록한 문서입니다.
- [AWS S3 이미지 업로드](https://blog.naver.com/yeondata/223989927527)
- [Querydsl을 적용한 Fetch Join](https://velog.io/@yeoni9094/%EB%AC%B8%EC%A0%9C%ED%95%B4%EA%B2%B0-Querydsl%EC%9D%84-%EC%A0%81%EC%9A%A9%ED%95%9C-Fetch-Join)
- [동시성 제어](https://blog.naver.com/yeondata/224021426407)

## 🔗 참고
- [카카오페이 단건 결제 공식 문서](https://developers.kakaopay.com/docs/payment/online/single-payment)
- [Java 8 Consumer accept() 공식 문서](https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html)
- [CQRS 패턴 By Azure Architecture](https://learn.microsoft.com/ko-kr/azure/architecture/patterns/cqrs)