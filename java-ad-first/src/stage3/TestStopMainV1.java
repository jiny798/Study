package stage3;

public class TestStopMainV1 {

    public static void main(String[] args) throws InterruptedException {
        MyTask myTask = new MyTask();
        Thread thread = new Thread(myTask, "work");
        thread.start();

        Thread.sleep(4000);
        System.out.println("작업 중단 지시 stop is true");
        myTask.stop = true;
    }

    static class MyTask implements Runnable {
        volatile boolean stop = false;

        public void run() {
            while (!stop) {
                System.out.println("작업중");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }
            System.out.println("자원 정리");
            System.out.println("작업 종료");
        }

    }
}
