<p align="center">
  <img width="250" height="250" alt="my-travel-destination" src="https://github.com/user-attachments/assets/b147dc01-3d40-4237-9c74-79b0f84dd488" />
</p>

# My Travel Destination
- 기존 Trouble Shooting 프로젝트를 기반으로 핵심 기능 위주의 MVP를 구현하여 보완한 여행 서비스
- 사용자들이 작성한 여행 블로그를 한 곳에서 확인하고, 나의 다음 여행지를 발견하는 서비스

## 📅 프로젝트 기간
- 총 기간 : 2025-08-16 ~ 2025-10-09
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
- 블로그
  - 티켓 사용 내역을 블로그에 첨부 가능
  - 티켓 상세 화면에서 관련 블로그 확인 가능
- 여행지 검색
  - 블로그 데이터를 기반으로 검색
  - 검색 조건 : 블로그 제목, 총 경비, 여행 기간

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

## 📝 API 명세서
- [Swagger]() (링크 첨부 예정)
- [Notion](https://www.notion.so/My-Travel-Destination-2562f0d54a3480bcadeaed91521358fe?source=copy_link)

## 🔒 로그인 및 로그아웃
이 프로젝트의 핵심은 인증 기능이 아니므로, 로그인 연장 기능은 구현하지 않았습니다. <br/>
로그인 연장을 구현하려면 Refresh Token이 필요하지만, 현재 프로젝트에서는 해당 기능이 없으므로 Access Token만 발급하도록 설계하였습니다. <br/>
로그아웃 시에는 Access Token을 Redis에 저장하여, 이후 해당 토큰이 사용될 경우 유효하지 않은 것으로 판별되도록 처리하였습니다. <br/>
만료되기 전 토큰이어도 로그아웃 직후라면 인증이 불가능합니다.

## 🔍✏️ CQRS 패턴을 적용한 이유
보통 읽기 작업보다 쓰기 작업의 비즈니스 로직이 복잡합니다. </br>
복잡한 쓰기 작업의 로직을 Command 서비스에 집중시키면 읽기와 쓰기 작업의 로직이 분리되어 각 작업의 관심사가 명확해지고, 가독성과 유지보수성이 향상됩니다. </br>

## 🎫 티켓 예약 프로세스
1. 분산 락 적용 후 예약 신청 및 재고 차감
2. 락 해제
3. 결제 진행
4. 결제 완료 시 예약 확정

### Redisson 분산 락을 사용한 이유
동일한 티켓 일정에 대해 여러 사용자가 동시에 예약을 시도할 경우, 동시성 문제가 발생할 수 있습니다.

동시성 제어 방법에는 **비관적 락**, **낙관적 락**, **분산 락**이 있습니다.

- **비관적 락** </br>
현재 트랜잭션이 접근한 데이터에 행 단위로 락을 걸어, 다른 트랜잭션이 읽기/쓰기 접근을 하지 못하게 하기 때문에 데이터의 일관성을 보장합니다. </br>
하지만, 동시 접속자가 많을 경우에는 락 대기 시간으로 인해 성능 저하가 발생할 수 있습니다.

- **낙관적 락** </br>
버전 컬럼을 사용하여 변경 사항이 생겼을 경우에만 버전이 올라가고, 버전이 다르면 업데이트가 실패합니다. </br>
재고 차감과 복구 과정으로 인해 데이터 변경이 빈번한 현재 프로젝트에서는 버전 충돌이 발생할 수 있고, 이를 해결하기 위한 복구 및 재시도 과정에서 지연이 발생할 수 있습니다.

- **분산 락** </br>
락을 획득한 프로세스나 스레드만 공유 자원에 접근할 수 있도록 제어합니다. </br>
Redis는 메모리에 데이터를 저장하므로 관계형 DB보다 빠른 조회 및 처리가 가능하며, 이를 활용해 분산 락을 구현할 수 있습니다. </br>
Lettuce 기반의 분산 락은 락에 대한 타임아웃 설정이 없어서 락을 해제하지 못하면 데드락을 유발할 수 있습니다. </br>
반면, Redisson은 락 유지 시간을 설정할 수 있기 때문에 자동으로 락의 소유권을 회수하여 데드락을 방지할 수 있습니다. </br> </br>

따라서, 현재 프로젝트 환경과 성능, 확장성을 모두 고려하여 Redisson 분산 락을 선택하였습니다. </br>

## 🧾 문서
프로젝트 진행 과정에서 학습한 내용과 문제 해결 과정을 기록한 문서입니다.
- [AWS S3 이미지 업로드](https://blog.naver.com/yeondata/223989927527)
- [Querydsl을 적용한 Fetch Join](https://velog.io/@yeoni9094/%EB%AC%B8%EC%A0%9C%ED%95%B4%EA%B2%B0-Querydsl%EC%9D%84-%EC%A0%81%EC%9A%A9%ED%95%9C-Fetch-Join)
- [동시성 제어](https://blog.naver.com/yeondata/224021426407)

## 🔗 참고
- [카카오페이 단건 결제 공식 문서](https://developers.kakaopay.com/docs/payment/online/single-payment)
- [Java 8 Consumer accept() 공식 문서](https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html)
- [CQRS 패턴 By Azure Architecture](https://learn.microsoft.com/ko-kr/azure/architecture/patterns/cqrs)
- [Swagger-UI 참고 블로그](https://velog.io/@gmlstjq123/SpringBoot-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8%EC%97%90-Swagger-UI-%EC%A0%81%EC%9A%A9%ED%95%98%EA%B8%B0)
