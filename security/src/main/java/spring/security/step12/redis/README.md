### 이중화 - Redis 세션 서버 

- 이중화는 시스템의 부하를 분산하면서, 서비스를 지속적으로 제공하는 것을 목표로 하는데,
- 스프링 시큐리티는 이러한 이중화 환경에서 인증, 권한 부여, 세션 관리 등의 보안 기능을 제공한다
- 시큐리티에서는 레디스 같은 분산 캐시를 사용하여 세션 정보를 여러 서버 간에 공유하고 안전하게 관리할 수 있다


![alt text](./redis.PNG)

- 위와 같이 서버 하나가 죽어도, 같은 세션 서버를 사용하기 때문에 지속적인 운영이 가능하다 

### 레디스 세션 서버

- 리눅스
    - sudo apt-get install redis-server 를 통해 설치
    - sudo service redis-server start 명령어로 레디스 서버 시작 가능
    
    
- Docker 설치 후
    - "docker run --name redis -p 6379:6379 -d redis" : 레디스 컨테이너 실행
   
<br>


#### 윈도우 도커 설치 방법

- 제어판 - 프로그램 및 기능 - Windows 기능 켜키/끄기 
    - Linux용 Windows 하위 시스템 체크
    - 가상 머신 플랫폼 체크 
    - 재시작
- https://wslstorestorage.blob.core.windows.net/wslblob/wsl_update_x64.msi
    - 받은 파일로 리눅스 커널 업데이트 
- 도커 공식 페이지에서 도커 데스크톱 설치

   
   
### 스프링부트 서버

```java
// gradle
implementation 'org.springframework.session:spring-session-data-redis'
implementation 'org.springframework.boot:spring-boot-starter-data-redis'
```

```java
// properties
spring.data.redis.host=localhost
spring.data.redis.port= 6379
```

```java
@Configuration
@EnableRedisHttpSession
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(host, port);
    }
}

```




