# 술렁술렁

음주 기록을 관리해주는 '술렁술렁' 애플리케이션의 서버입니다.

![1.png](images%2F1.png)![2.png](images%2F2.png)

![3.png](images%2F3.png)
![4.png](images%2F4.png)
![5.png](images%2F5.png)
![6.png](images%2F6.png)

![7.png](images%2F7.png)
![8.png](images%2F8.png)

## 목차
1. [개요](#개요)
2. [기능](#기능)
3. [기술 스택](#기술-스택)
4. [아키텍처](#아키텍처)
5. [팀원 및 역할](#팀원-및-역할)

## 기능
   - 음주 기록 관리
   - 음주 통계
   - 사용자 인증
   - 친구 기능(테스트 중)
   - 공유 기록(테스트 중)
## 기술 스택
   - 백엔드
     - Java 17
     - Spring Boot 2.7
     - JPA(Hibernate), QueryDSL
   - 데이터베이스
     - MySQL 8.0
   - 캐시
     - Redis 
   - 인프라
     - Docker
     - GitHub Action
     - AWS EC2, AWS ELB
     - AWS RDS, AWS ElastiCache, AWS S3
     - AWS CloudWatch
     - AWS Cognito
     - AWS Network Services (VPC, Subnet, Routing Table, Internet Gateway, NAT Gateway)
     - AWS Route53, AWS Certificate Manager
## 아키텍처
   - presentation, application, domain, infrastructure로 패키지 분류 후 패키지 내에서 도메인 별로 패키지를 분리해 의존성을 낮췄습니다.
   - 아키텍처 다이어그램
     ![infra.png](images%2Finfra.png)
## 팀원 및 역할
   - 김건우: iOS 개발
   - 박단비: AOS 개발
   - 정태영: 백엔드 개발
   - 최다예: 디자인, QA(디자인, 기능)
   - 허정민: 백엔드 개발, 운영