package huimei.segment;

public class Segment {

    private String match(String str) {
        if ("abcde".contains(str)) {
            return "abcde";
        }

        return null;
    }

    private void segment(String se) {
        char[] c = se.toCharArray();

        for (int i = 0; i < c.length - 1; i++) {
            String str = new String(new char[] { c[i], c[i + 1] });
            String rs = match(str);
            if (rs != null) {
                int index = rs.indexOf(str);
                System.out.println(str + "---------" + index);
                int start = i - index;
                for (int cIndex = 0; start < i; start++, cIndex++) {
                    if (c[start] != rs.charAt(cIndex)) {
                        return;
                    }
                    System.out.print(c[start]);
                }
                for (start += str.length(), index += str.length(); index < rs.length(); start++, index++) {
                    if (c[start] != rs.charAt(index)) {
                        return;
                    }
                    System.out.print(c[start]);
                }
                System.out.println();
            }
        }
    }

    public static void main(String[] args) {
        try {
            String str = "abcdefghijklmnopqrstuvwxyz";

            new Segment().segment(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
