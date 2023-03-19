# 블로그 검색 서비스


## 프로젝트 정보
- Java 11
- Spring Boot 2.7.9
- H2 Database
- Spring Data JPA
- Gradle

## 가정
- DAU 10만
- 유저당 평균 10개 검색어
- 분당 약 700개 검색어 수집


## 검색 서비스 및 검색어 수집 시스템

![검색 서비스 및 검색어 수집 시스템](./diagram1.png)

- 요청은 검색어(query), 정렬 기준(sort), 페이지(page), 페이지 크기(size)를 받음
- 서비스의 핵심 기능은 블로그 컨텐츠 제공자로부터 조회한 컨텐츠 제공
- 검색어 수집은 부가 기능으로 판단하여 Aspect에서 처리
- Kafka를 통한 DB 저장 우회
  - 목적 1: DB 처리 이슈로 검색 서비스 오류나 성능 저하 방지
  - 목적 2: 트래픽이 몰릴 경우 부하 분산
  - 참고: 실제 구현은 DB 직접 INSERT
- 다양한 컨텐츠 제공자의 추가 고려
  - 외부 통신용 Feign Client는 공통 인터페이스 External Client를 구현
  - 컨텐츠 제공자 API별 요청 데이터 형식이 다른 문제를 External Adapter로 해결
  - Service는 Client에만 의존
  - Client 내부적으로 컨텐츠 제공자 우선순위대로 호출
  - 성공시 결과를 Service로 반환하고 실패시 다음 컨텐츠 제공자 호출
- DB 설계
  - 각 검색어별로 1개의 행을 가지고 검색횟수 컬럼을 갖는 것은 비효율적이라고 판단
  - 매 검색별로 QUERY 테이블에 인서트
  - 보다 효율적인 검색어 관리를 위해 QUERY 데이터는 배치로 일정 주기마다 QUERY_META 업데이트
  - 실시간 인기검색어는 QUERY에서 특정 시간 범위의 COUNT로 체크
  - 전체 검색순위는 QUERY_META의 SEARCH_COUNT 순위로 체크


## 문제 해결 전략

### 1. 블로그 검색
- 키워드를 통한 블로그 검색
  - 요청 파라미터: query
- 검색 결과에서 Sorting(정확도순, 최신순) 기능을 지원
  - 요청 파라미터: sort
  - accuracy(정확도순), recency(최신순)의 값을 가짐
  - 각각 Adapter에서 카카오, 네이버의 정확도순/최신순 파라미터 값으로 매핑
- 검색 결과는 Pagination 형태로 제공해야 합니다.
  - 요청 파라미터: page, size
  - 응답 Body 필드: page, size, totalCount, totalPageCount
- 검색 소스는 카카오 API(검색 소스 추가 고려)
  - External Client를 구현하는 검색 소스별 Feign Client
  - External Adapter로 검색 소스별 요청 데이터 세팅

### 2. 인기 검색어 목록
- 사용자들이 많이 검색한 순서대로, 최대 10개의 검색 키워드 제공
- 검색어 별로 검색된 횟수 함께 표기
  - 응답 Body 필드: searchCount

### 3. 추가 요건
- 모듈 간 의존성 제약
  - 상위 패키지에서 하위 패키지에 의존하지 않도록 주의
- 트래픽이 많고, 저장되어 있는 데이터가 많음을 염두에 둔 구현
  - 배치로 일정 주기마다 QUERY -> QUERY_META로 업데이트
  - 실시간 인기검색어는 QUERY 테이블에서 일정 시간 범위의 COUNT 함수로 조회
  - 전체 검색순위는 QUERY_META의 SEARCH_COUNT 내림차순으로 조회 
- 동시성 이슈가 발생할 수 있는 부분을 염두에 둔 구현 (예시. 키워드 별로 검색된 횟수의 정확도)
  - QUERY(검색어) 테이블에서 SEARCH_COUNT(검색횟수) 컬럼을 갖지 않음
  - 검색횟수는 일정 시간 범위 내에서 COUNT 함수로 조회
- 카카오 블로그 검색 API에 장애가 발생한 경우, 네이버 블로그 검색 API를 통해 데이터 제공
  - Search Client에서 우선순위대로 External Client 호출
  - 성공하면 결과 반환, 실패하면 다음 External Client 호출하도록 구현

### 4. 예외 처리
- 비즈니스상 문제는 SearchBusinessException으로 구현
- SearchBuisnessException은 ApiResult Enum 포함
- 각 예외 케이스는 ApiResult에서 관리
- 핸들링되지 못한 예외는 Controller Advice에서 일괄 기타 예외 케이스로 매핑
- 
### 5. 테스트 케이스
- 통합 테스트
  - 성공
  - 필수 요청 파라미터 누락
  - 요청 파라미터 검증 실패
    - 컨텐츠 제공자의 파라미터 범위에 따라 설정
- 단위 테스트
  - 1차 컨텐츠 제공자 통신 실패
