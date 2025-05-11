# ✈️ Traveldige


> **Travelidge**는 소상공인 및 여행업체를 위한 **온라인 예약/결제 통합 관리 시스템**입니다.  
> 자체 플랫폼과 **네이버 스마트스토어**를 연동하여 상품 등록부터 예약, 결제, 주문 관리까지 한 번에 처리할 수 있도록 지원합니다.

- 소상공인 및 여행업체는 **쉽고 빠르게 상품을 등록**할 수 있습니다.
- 고객은 **간편하게 예약 및 결제**를 통해 원활한 사용자 경험을 제공합니다.
---

## 👨‍💻 개발 기간 및 인원

- **기간**: 2025.02 ~ 2025.05
- **인원**: 백엔드: 1명, 프론트엔드: 1명

---

## 🌐 배포 링크

- [사용자 페이지](https://travelidge.shop)  
- [관리자 페이지](https://admin.travelidge.shop)
- [API 명세](https://api.travelidge.shop/swagger-ui)
---

## 🗂 ERD 구조

- [ERD Cloud에서 보기](https://www.erdcloud.com/d/mYpMAqACf4JSA5JHM )

<br>

![travelidge (5)](https://github.com/user-attachments/assets/b95190c4-046e-4f08-809c-e1ff264c6810)

---

## 🏗 시스템 아키텍처

![시스템 아키텍쳐-페이지-1의 복사본 drawio (3)](https://github.com/user-attachments/assets/34de2a30-2678-474e-98a8-873e7454424a)


---

## 🛠 기술 스택

### 🔙 Back-end
- Java 17, Spring Boot 3.4.1
- Spring Security, JWT, OAuth2
- JPA (Hibernate), QueryDSL
- Spring Batch
- Toss Payments API
- Swagger

### ☁️ Cloud & Storage
- Oracle Cloud Infrastructure (OCI)
- Oracle Object Storage

### 🛢 Database & Caching
- Oracle Database
- Redis

### ⚙ DevOps & Infra
- Jenkins
- Docker
- Nginx
---
## 🚀 주요 기능

### ✅ **공통**
-  신고 처리  
-  상품 목록 및 상세 조회  
-  상품 문의, 1:1 문의  

---

### 👤 **사용자 기능**
-  소셜 회원가입 및 로그인
-  회원 정보 수정
-  장바구니 관리
-  상품 구매 및 결제 (Toss API 연동)
-  주문 관리 (취소, 반품)  
-  리뷰 작성 및 신고  
-  관심상품 등록  
-  후기 작성
-  상품 검색 및 인기 검색어 출력  
-  상품 문의 및 1:1 문의 작성  

---

### 🔧 **관리자 기능**
-  관리자 로그인
-  관리자 계정 생성 및 삭제
-  상품 및 카테고리 관리 
     - 자체 시스템  
     - 네이버 스마트스토어 연동  
-  주문 상태 관리
     - 확인, 취소, 반품  
     - 자체 시스템 + 네이버 스마트스토어 연동  
-  티켓 발급 및 사용 처리
-  리뷰 신고 관리
-  상품 문의 및 1:1 문의 응답
-  추천상품 등록 및 삭제
  
### ⚙ 시스템 기능
- **네이버 스마트스토어 API 연동**
   - 상품 및 주문 데이터 자동 동기화
   - **Spring Batch**를 활용한 주문 수집, 수정 자동 처리
- **예약 및 결제 시스템 구현**
   - 실시간 예약 가능 여부 확인 로직
   - **토스 결제 API** 연동을 통한 간편 결제 처리
- **사용자 인증 및 보안 처리**
   - **JWT** 기반 사용자 인증 시스템
   - **OAuth2**를 통한 소셜 로그인 연동
- **Redis**를 활용한 키워드 기반 인기 검색어 기능 구현
- **Oracle Object Storage**를 활용하여 이미지 및 기타 정적 파일 업로드 구현
- **멀티 도메인 구성**
     - 사용자용 클라이언트/관리자용 어드민 페이지 구분
- **Nginx**를 활용한 리버스 프록시 설정으로 클라이언트와 관리자 페이지 트래픽 분기 처리
- **Let's Encrypt SSL 인증서 적용**
  - **Certbot**을 통한 무료 SSL 인증서 발급 및 자동 갱신
  - Nginx와 연동하여 HTTPS 보안 통신 지원
- **CI/CD 자동화 구축**
   - Jenkins + Docker + Nginx + Oracle Cloud를 활용한 자동 배포 파이프라인 구성 및 운영
- **Swagger**를 통한 API 문서 자동화


