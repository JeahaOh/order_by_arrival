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
