# apr-backend-assignment
에이피알 백엔드 과제
> (Spring Boot 3.5.6) Back-End 를 기반으로 구성된 prj-friends 프로젝트

### 프로젝트 정보
- **프로젝트명**: prj-friends
- **그룹**: com.friends
- **버전**: 1.0.0
- **빌드 도구**: Gradle
- **Java 버전**: 21
- **Framework**: Spring Boot 3.5.6
- **Database**: : H2 (in-memory)
- **API Documentation**: Swagger/OpenAPI

### 프로젝트 구조
각 API 모듈 내 도메인별 표준 구조:

com.friends/
├── Controller/       # REST API 요청을 처리하는 컨트롤러 클래스
├── Entity/           # 데이터 전송 객체(DTO) 및 JPA 엔티티 클래스
├── Exception/        # 공통 예외 처리 및 커스텀 예외 클래스
├── Repository/       # 데이터베이스 접근을 위한 레포지토리 인터페이스
├── Service/          # 비즈니스 로직을 처리하는 서비스 클래스
├── Util/             # 공통 유틸리티 및 상수 정의

> ## Rest API 목록 (Swagger)

| 구분          | URL                                          | 비고    |
|---------------|----------------------------------------------|---------|
| API           | [http://localhost:9010/swagger-ui/index.html | 친구 API |


# DB 테이블 정보

## users
| 컬럼명     | 타입        | 설명           |
|------------|------------|----------------|
| user_id    | NUMBER(10) | 사용자 ID       |
| username   | VARCHAR(50)| 사용자 이름     |
| email      | VARCHAR(100)| 이메일        |
| created_at | TIMESTAMP  | 생성일자        |

**PK**: user_id  
**설명**: 시스템 사용자 정보를 관리

---

## frnd_lst_mst
| 컬럼명        | 타입        | 설명           |
|---------------|------------|----------------|
| from_user_id  | NUMBER(10) | 보낸 사용자 ID |
| to_user_id    | NUMBER(10) | 받은 사용자 ID |
| approvedat    | DATE       | 승인일자       |

**PK**: from_user_id + to_user_id  
**설명**: 친구 관계가 승인된 사용자들의 정보를 저장하는 테이블

---

## frnd_request
| 컬럼명         | 타입         | 설명                            |
|----------------|-------------|--------------------------------|
| request_id     | VARCHAR(36) | 요청 식별자 (UUID)             |
| request_user_id| NUMBER(10)  | 친구 요청을 보낸 사용자 ID      |
| target_user_id | NUMBER(10)  | 친구 요청을 받은 사용자 ID      |
| proc_yn        | CHAR(1)     | 처리 여부 (Y: 수락, N: 대기중) |
| requested_at   | TIMESTAMP   | 요청 시각                        |
| responded_at   | TIMESTAMP   | 응답 시각                        |

**PK**: request_id  
**설명**: 친구 요청 정보를 저장하며, 요청 상태와 시각을 관리

