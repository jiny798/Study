### 목차
- 자바 메모리 구조
- 메모리 구조 흐름 분석
- start() 메서드
- 데몬 스레드와 사용자 스레드
- 스레드 생성 방식

<br>

### 1. 자바 메모리 구조 


####  메서드 영역 
-  메서드 영역은 프로그램 실행에 필요한 데이터를 관리한다
- 프로그램에서 모든 영역은 해당 메서드 영역을 공유하고 있다

####  스택 영역 
- 자바 실행 시, 하나의 실행 스택이 생성된다 
- 스택 프레임에는 지역 변수, 메서드 호출 정보 등을 가진다
  - 스택 프레임 : 메소드 호출 시, 하나의 스택프레임이 쌓이고, 메서드가 종료되면 해당 스택 프레임이 제거 된다

####  힙 영역 
- 객체와 배열이 생성되는 영역이다. 
- GC(가비지 컬렉션)이 진행되는 영역이며, 더 이상 참조되지 않는 객체는 GC에 의해 제거된다 

<br>

### 2. 메모리 구조 흐름 분석

```java
public static void main(String[] args) {
  System.out.println(Thread.currentThread().getName() + ": main() start");
  TestThread testThread = new TestThread();
  System.out.println(Thread.currentThread().getName() + ": start() 호출 전");
  testThread.start();
  System.out.println(Thread.currentThread().getName() + ": start() 호출 후");
  System.out.println(Thread.currentThread().getName() + ": main() end");
}

```

> 1. main 스택 프레임 생성 > [로그 출력] main: main() start
> 2. TestThread 인스턴스 생성
> 3. [로그 출력] main: start() 호출 전
> 4. [로그 출력] main: start() 호출 후
> 5. [로그 출력] Thread-0: run()
> 6. [로그 출력] main: main() end



메인 스레드는 start() 메서드를 통해 Thread-0 스레드에게 실행을 지시만 한다

메인스레드가 직접 testThread 의 run 을 호출하는 것이 아님 

메인 스레드가 더 빨리 실행될 경우 위와 같지만 Thread-0 이 더 빨리 실행될 경우는 다음과 같을 수 있다 

> 1. main 스택 프레임 생성 > [로그 출력] main: main() start
> 2. TestThread 인스턴스 생성
> 3. [로그 출력] main: start() 호출 전
> 4. [로그 출력] Thread-0: run()
> 5. [로그 출력] main: start() 호출 후
> 6. [로그 출력] main: main() end

<br>

### 3. start() 메서드 

스레드의 start() 메서드는 스택 공간을 할당하면서 스레드를 시작하는 메서드이다.

main 스레드가 아닌 별도의 스레드에서 재정의한 run() 메서드를 실행하려면, 반드시 start() 메서드를 호출해야 한다 


<br>

### 4. 데몬 스레드와 사용자 스레드 


#### 사용자 스레드
- 프로그램의 주요 작업 수행
- 작업이 완료될 때까지 실행 (자바에서 메인스레드가 죽어도, 생성된 사용자 스레드는 실행됨)
- 모든 사용자 스레드가 죽어야 JVM 이 종료된다 

#### 데몬 스레드 
- 백그라운드에서 보조 작업 수행
- 모든 사용자 스레드가 종료되면 데몬 스레드는 자동으로 종료된다 


```java
    public static void main(String[] args) {
        System.out.println(Thread.currentThread().getName() + ": main() start");

        DaemonThread daemonThread = new DaemonThread();
        daemonThread.setDaemon(true); // 데몬 스레드 여부, 기본은 false
        daemonThread.start();

        System.out.println(Thread.currentThread().getName() + ": main() end");
    }

    // start출력 -> 10초 -> end 출력
    static class DaemonThread extends Thread {
        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + ": run() start");
            try {
                Thread.sleep(10000); // 10초간 실행
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(Thread.currentThread().getName() + ": run() end");
        }
    }


```

- DaemonThread의 end 가 출력되기 전에 프로그램이 종료된다 
- setDaemon(false) 로 설정하면 사용자 스레드가 되어, main 스레드가 종료되어도 DaemonThread 는 실행되므로,
  end 를 출력한다
<br>
<br>

### 5. 스레드 생성 방식

1. Thread 클래스를 상속 받아 Thread 의 start() 메서드를 실행하는 방법 

2. Runnable 인터페이스를 구현하고, Thread 생성자에 Runnable 을 전달한 뒤
   Thread 의 start() 메서드를 실행하는 방법 

```java
 public class HelloRunnable implements Runnable {
    @Override
    public void run() {
    System.out.println(Thread.currentThread().getName() + ": run()");
    }
 }

```
```java
 public class HelloRunnableMain {
    public static void main(String[] args) {
        System.out.println(Thread.currentThread().getName() + ": main() start");
        HelloRunnable runnable = new HelloRunnable();
        Thread thread = new Thread(runnable);
        thread.start();
        System.out.println(Thread.currentThread().getName() + ": main() end");
    }
 }

```

```java
main: main() start
main: main() end
Thread-0: run()
```

- 스레드와 스레드가 실행할 작업이 분리되어 관리된다 
- Runnable 인터페이스를 구현하는 것이 편리하다
- Thread 를 사용하는 방식의 경우 상속의 제한
  -  자바는 단일 상속만 허용함 > 이미 다른 클래스를 사용한다면 Thread 클래스를 상속받지 못함
- 반면 Runnable 인터페이스는 코드가 분리되고, 여러 스레드가 동일한 Runnable 객체를 공유할 수 있어서 효율적으로 자원 관리 가능 