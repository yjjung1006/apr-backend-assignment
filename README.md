# apr-backend-assignment
에이피알 백엔드 과제
> (Spring Boot 3.X) Back-End 를 기반으로 구성된 prj-friends 프로젝트

### 프로젝트 정보
- **프로젝트명**: prj-friends
- **그룹**: com.friends
- **버전**: 1.0.0
- **빌드 도구**: Gradle
- **Java 버전**: 21
- **Framework**: Spring Boot 3.x
- **Database**: : H2 (in-memory)
- **API Documentation**: Swagger/OpenAPI

### 프로젝트 구조
각 API 모듈 내 도메인별 표준 구조:

com.friends/
├── Controller/       # REST 컨트롤러
├── Service/          # 비즈니스 로직
├── Repository/       # 데이터 전송 객체 및 엔티티 클래스

> ## Rest API 목록 (Swagger)

| 구분          | URL                                          | 비고    |
|---------------|----------------------------------------------|---------|
| API           | [http://localhost:9010/swagger-ui/index.html | 친구 API |

