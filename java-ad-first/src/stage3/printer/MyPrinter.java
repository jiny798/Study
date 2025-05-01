package stage3.printer;

import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;

import static util.MyLogger.log;

public class MyPrinter {

    public static void main(String[] args) throws InterruptedException {
        Printer printer = new Printer();
        Thread printerThread = new Thread(printer, "printer");
        printerThread.start();

        Scanner userInput = new Scanner(System.in);
        while (true) {
            log("프린터할 문서를 입력하세요. 종료 (q): ");

            // 프린터 입력
            String input = userInput.nextLine();

            // 프린터 종료
            if (input.equals("q")) {
//                printer.work = false;
                printerThread.interrupt();
                break;
            }

            // 프린터 잡 추가
            printer.addJob(input);
        }
    }

    static class Printer implements Runnable {
        //        volatile boolean work = true;
        Queue<String> jobQueue = new ConcurrentLinkedQueue<>();

        @Override
        public void run() {
//            while (work) {
            // 인터럽트 걸리면 빠져나올 수 있다
            while (!Thread.interrupted()) {
                if (jobQueue.isEmpty()) {
                    Thread.yield(); // 큐가 없으면 그냥 다른 스레드에게 조금이라도 양보
                    continue;
                }
                try {
                    // 작업있으면 하나 가져와서 출력
                    String job = jobQueue.poll();
                    log("출력 시작: " + job + ", 대기 문서: " + jobQueue);
                    Thread.sleep(3000); // 출력 시간
                    log("출력 완료: " + job);
                } catch (InterruptedException e) {
                    // 메인에서 인터럽트 걸면 바로 빠져나옴
                    log("인터럽트!");
                    break;
                }
            }
            log("프린터 종료");
        }

        public void addJob(String input) {
            jobQueue.offer(input);
        }
    }
}