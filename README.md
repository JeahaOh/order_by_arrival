# order_by_arrival

redis와 kafka를 이용한 선착순 이벤트 시스템 예제

## 환경 셋팅

## Docker

https://www.docker.com/

### docker.app으로 설치후 zsh에서 docker가 안 잡힐 때

```shell
  export PATH=$PATH:/Applications/Docker.app/Contents/Resources/bin
```

### Docker MySQL 실행 명령어

```shell
  docker pull mysql
  docker run -d -p 3306:3306 -e MYSQL_ROOT_PASSWORD=1020 --name mysql mysql
  docker ps
  docker exec -it mysql bash
```

### MySql database 생성

```shell
  mysql -u root -p
  password
  create database coupon_example;
  use coupon_example;
```

## 프로젝트 셋팅

application.yml

```yaml
  spring:
    jpa:
      hibernate:
        ddl-auto: create
      show-sql: true
    datasource:
      driver-class-name: com.mysql.cj.jdbc.Driver
      url: jdbc:mysql://127.0.0.1:3306/coupon_example
      username: root
      password: 1234
```

## 요구사항 정의

선착순 100명에게 할인쿠폰을 제공하는 이벤트를 진행하고자 한다.  
  
이 이벤트는 아래와 같은 조건을 만족하여야 한다.  
  
- 선착순 100명에게만 지급되어야한다.
- 101개 이상이 지급되면 안된다.
- 순간적으로 몰리는 트래픽을 버틸 수 있어야합니다.


