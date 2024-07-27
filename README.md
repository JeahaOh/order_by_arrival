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

---

## 쿠폰 프로젝트 

### 프로젝트 셋팅

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

### 요구사항 정의

선착순 100명에게 할인쿠폰을 제공하는 이벤트를 진행하고자 한다.  
  
이 이벤트는 아래와 같은 조건을 만족하여야 한다.  
  
- 선착순 100명에게만 지급되어야한다.
- 101개 이상이 지급되면 안된다.
- 순간적으로 몰리는 트래픽을 버틸 수 있어야합니다.

### 문제점

- 쿠폰이 100개만 발급되어야 하지만 그 이상 발급 됨.
- race condition; 경쟁상태  
  둘 이상의 입력 또는 조작의 타이밍이나 순서 등이 결과값에 영향을 줄 수 있는 상태.  
  입력 변화의 타이밍이나 순서가 예상과 다르게 작동하면 정상적인 결과가 나오지 않게 될 위험이 있음, 이를 경쟁 위험이라 함.

---

## Redis를 활용하여 문제 해결

### Redis 셋팅

```shell
  docker pull redis
  docker run -d -p 6379:6379 redis
```

```shell
  docker ps

  docker exec -it 44d7f23b3034 redis-cli
  127.0.0.1:6379> incr coupon_count
  (integer) 1
  127.0.0.1:6379> incr coupon_count
  (integer) 2
  127.0.0.1:6379> incr coupon_count
  (integer) 3
```

### redis 사용의 문제점

- 단기간에 많은 요청이 들어와 트래픽이 몰릴 경우 부하 발생과 서비스 오류의 위험가능
- nGrinder로 부하테스트시 검증 가능 

---

## Apache Kafka를 이용하여 문제 해결

### Docker Compose

```shell
  docker-compose -v
  brew install cask docker-compose
  docker-compose -v

  cd kafka
  # 카프카 실행
  docker-compose -up -d

  # 카프카 종료
  docker-compose down
```

### Apacke Kafka

- 분산 이벤트 스트리밍 플랫폼
- 이벤트 스트리밍 : 소스에서 목적지까지 이벤트를 실시간으로 스트리밍 하는 것
- Topic :  
  Queue
- Producer :  
  Topic에 데이터 삽입
- Consumer :  
  Topic에 데이터를 꺼내감

- 토픽 생성  
  ```shell
    docker exec -it kafka kafka-topics.sh --bootstrap-server localhost:9092 --create --topic coupon_create
  ```
- 프로듀서 실행  
  ```shell
    docker exec -it kafka kafka-console-producer.sh --topic coupon_create --broker-list 0.0.0.0:9092
  ```
- 컨슈머 실행  
  ```shell
    docker exec -it kafka kafka-console-consumer.sh --topic coupon_create --bootstrap-server localhost:9092
  ```

### Producer, Consumer를 이용, 쿠폰 발급하기