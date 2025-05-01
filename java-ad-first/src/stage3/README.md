
### 스레드 객체 정보

스레드 정보를 출력하기 위한 다양한 메소드가 지원된다 

```java
    public static void main(String[] args) {
        // main 스레드
        Thread mainThread = Thread.currentThread();
        log("mainThread = " + mainThread);
        log("mainThread.threadId() = " + mainThread.threadId());
        log("mainThread.getName() = " + mainThread.getName());
        log("mainThread.getPriority() = " + mainThread.getPriority()); // 1~10 (기본값 5)
        log("mainThread.getThreadGroup() = " + mainThread.getThreadGroup());
        log("mainThread.getState() = " + mainThread.getState());

        // myThread 스레드
        Thread myThread = new Thread(new TestThread(), "myThread");
        log("myThread = " + myThread);
        log("myThread.threadId() = " + myThread.threadId());
        log("myThread.getName() = " + myThread.getName());
        log("myThread.getPriority() = " + myThread.getPriority());
        log("myThread.getThreadGroup() = " + myThread.getThreadGroup());
        log("myThread.getState() = " + myThread.getState());
    }
```
<br>

#### 스레드 객체 정보
>  log(myThread);
> 
> 출력 :  Thread[#21,myThread,5,main]
>  
> Thread 클래스의 toString() 은 스레드 ID, 스레드 이름, 우선순위, 스레드 그룹을 출력한다

<br>

#### 스레드 ID
>  log(myThread.threadId());
>
> 스레드의 고유 식별자를 반환한다. 이 ID는 JVM 내에서 유일한 값으로 사용

<br>

#### 스레드 이름
>  log(myThread.getName());
>
> 스레드의 이름이 반환된다. 여기서는 Thread 생성자에 "myThread" 로 이름을 부여했다

<br>

#### 스레드 우선순위
>  log(myThread.getPriority());
>
> 스레드의 우선순위를 반환하는 메서드이다. (setPriority() 로 변경가능)
> 
>  우선순위는 1 (가장 낮음)에서 10 (가장 높음)까지 설정할 수 있으며, 기본값은 5이다
> 
> 우선순위는 스레드 스케줄러가 어떤 스레드를 우선 실행할지 결정하는 데 사용된다
> 
>  하지만 실제로 실행되는 순서는 JVM 구현과 운영체제에 따라 달라질 수 있다

<br>


#### 스레드 상태
>  log(myThread.getPriority());
>
> 스레드의 현재 상태를 반환하는 메서드
> 
> Thread.State 열거형에 정의된 상수를 하나 반환한다

스레드 상태의 값 설명은 다음과 같다
- NEW : 스레드가 아직 시작되지 않은 초기 상태
- RUNNABLE : 스레드가 실행 중이거나 실행될 준비가 된 상태
- BLOCKED : 스레드가 동기화 락을 기다리는 상태
- WAITING : 스레드가 다른 스레드의 특정 작업이 완료되기를 기다리는 상태
- TIMED_WAITING :  일정 시간 동안 기다리는 상태
- TERMINATED : 스레드가 실행을 마친 상태

<br>

### 스레드 상태값의 시점을 자세히 보자

1. New 
   - 스레드가 생성되고 아직 시작되지 않은 상태 
   - Thread thread = new Thread(runnable);
   - 말그대로 생성만 하고, start() 메서드를 호출하지 않은 상태 
   
<br>

2. Runnable (실행가능한 상태)
    - 스레드가 실행될 준비가 된 상태이다. 이 상태에서 스레드는 실제로 CPU에서 실행될 수 있다
    - thread.start(); 호출 시, 스레드는 Runnable 상태로 변경된다 
    - Runnable 상태에 있는 모든 스레드가 동시에 실행되지는 않는다.  운영체제의 스케줄러가 각 스레드
      에 CPU 시간을 할당하여 실행하기 때문에, Runnable 상태에 있는 스레드는 스케줄러의 실행 대기열에 포
      함되어 있다가 차례로 CPU에서 실행된다
    - 운영체제 스케줄러의 대기열이든 CPU에 의해 실행되고 있는 상태이든 모두 RUNNABLE 이다

<br>

3. Blocked (차단상태)
    - 스레드가 다른 스레드에 의해 동기화 락을 얻기 위해 기다리는 상태
    - synchronized(lock) { ... 코드 블록}
    - 위와 같이 다른 스레드가 이미 코드블록을 실행중일 때, lock 을 얻기 위해 대기하는 상태를 말한다

<br>

4. Waiting (대기상태)
    - 스레드가 다른 스레드의 특정 작업이 완료되기를 무기한 기다리는 상태
    - wait(), join() 메서드가 호출될 때, 해당 상태가 된다
    - wait() 라면 notify(),notifyAll() 메서드를 호출하거나, join() 이 완료될 때 까지 기다린다

<br>

5. Timed Waiting (시간 제한 대기 상태)
    -  sleep(long millis), wait(long timeout), join(long millis) 메서드가 호출될 때 해당 상태가 된다 
    - 주어진 시간이 경과하거나 다른 스레드가 해당 스레드를 깨우면 이 상태에서 벗어난다

<br> 

6. Terminated (종료)
   - 스레드의 실행이 완료된 상태 

<br>
<br>

### 인터럽트 

반복되는 작업을 새로운 스레드에서 실행하고
메인에서 interrupt() 를 호춣하는 예제

```java

public static void main(String[] args) {
   MyTask task = new MyTask();
   Thread thread = new Thread(task, "work");
   thread.start();

   sleep(1000);

   log("작업 중단 지시 - thread.interrupt()");
   thread.interrupt();
   log("work 스레드 인터럽트 상태1 = " + thread.isInterrupted());
}

    static class MyTask implements Runnable {
        @Override
        public void run() {
            
            // 1. 인터럽트 상태 체크 
            while (!Thread.interrupted()) { 
                log("작업 중");
            }
            log("work 스레드 인터럽트 상태2 = " + Thread.currentThread().isInterrupted());

            try {
                log("자원 정리 시도");
                Thread.sleep(1000);
                log("자원 정리 완료");
            } catch (InterruptedException e) {
                log("자원 정리 실패 - 자원 정리 중 인터럽트 발생");
                log("work 스레드 인터럽트 상태3 = " +
                        Thread.currentThread().isInterrupted());
            }
            log("작업 종료");
        }
    }

```

- 메인에서 interrupt() 를 호출하면, MyTask 내부의 Thread.interrupted() 는 true 가 된다
- 그리고 다시 인터럽트 상태를 false 로 변경한다 
- Thread.isInterrupted() 를 사용하면 interrupt() 호출 시, true 를 반환하지만 인터럽트를 변경하지 않는다
  - 주의해서 사용 필요 