package redis;

public class Test2 {

    private static void add(int i) {
        System.out.println(i);
    }

    public static void main(String[] args) {
        try {
            int i = 0;
            while (true) {
                add(i++);
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
