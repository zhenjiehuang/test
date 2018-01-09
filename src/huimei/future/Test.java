package huimei.future;

import java.util.concurrent.CompletableFuture;

public class Test {
    public static void main(String[] args) {
        try {
            CompletableFuture.runAsync(new Runnable() {
                @Override
                public void run() {
                    System.out.println("111111");
                    try {

                        Thread.sleep(3000);
                    } catch (Exception e) {
                    }

                }
            });
            CompletableFuture<Void> f = CompletableFuture.runAsync(new Runnable() {
                @Override
                public void run() {
                    System.out.println("111111");
                    try {

                        Thread.sleep(3000);
                    } catch (Exception e) {
                    }

                }
            });
            CompletableFuture<Void> f2 = f.runAsync(new Runnable() {
                @Override
                public void run() {
                    System.out.println("111111");
                    try {

                        Thread.sleep(3000);
                    } catch (Exception e) {
                    }

                }
            });

            CompletableFuture.allOf(f, f2).get();

            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
