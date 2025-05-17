package stage4;

import util.MyLogger;
import util.ThreadUtils;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class VolatileMain {
    public static void main(String[] args) {
        MyTask task = new MyTask();
        Thread t = new Thread(task, "work");
        log("runFlag = " + task.runFlag); // true

        t.start();
        sleep(1000);
        task.runFlag = false;

        log("runFlag = " + task.runFlag);
        log("main 종료");
    }

    // runFlag 가 false가 될떄까지 종료되지 않는 테스크
    static class MyTask implements Runnable {
        boolean runFlag = true;
        //volatile boolean runFlag = true;

        @Override
        public void run() {
            log("task 시작");
            while (runFlag) {}
            log("task 종료");
        }
    }
}
