
### 자바 메모리 구조 
<br>


#### 1. 메서드 영역 
-  메서드 영역은 프로그램 실행에 필요한 데이터를 관리한다
- 프로그램에서 모든 영역은 해당 메서드 영역을 공유하고 있다

#### 2. 스택 영역 
- 자바 실행 시, 하나의 실행 스택이 생성된다 
- 스택 프레임에는 지역 변수, 메서드 호출 정보 등을 가진다
  - 스택 프레임 : 메소드 호출 시, 하나의 스택프레임이 쌓이고, 메서드가 종료되면 해당 스택 프레임이 제거 된다

#### 3. 힙 영역 
- 객체와 배열이 생성되는 영역이다. 
- GC(가비지 컬렉션)이 진행되는 영역이며, 더 이상 참조되지 않는 객체는 GC에 의해 제거된다 

<br>

### 메모리 구조 흐름 분석

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

### start() 메서드 

스레드의 start() 메서드는 스택 공간을 할당하면서 스레드를 시작하는 메서드이다.
main 스레드가 아닌 별도의 스레드에서 재정의한 run() 메서드를 실행하려면, 반드시 start() 메서드를 호출해야 한다 