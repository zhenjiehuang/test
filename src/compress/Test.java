package compress;

import java.util.HashMap;
import java.util.Map;

public class Test {

    static Map<Integer, CompressIndex> index = new HashMap<Integer, CompressIndex>();

    public static void main(String[] args) {
        // >0 BYTE 7f
        // <0 BYTE 80
        byte[] bs = new byte[] { 0, 11, -11, -67, 121 };
        StringBuffer str = new StringBuffer();

        for (int i = 0; i < bs.length; i++) {
            byte b = bs[i];
            char[] cs = Integer.toHexString(b).toCharArray();
            if (cs.length == 1) {
                str.append('0');
                index.get('0').addIndex((long) (i << 1));
                str.append(cs[0]);
            } else {
                str.append(cs[cs.length - 2]);
                str.append(cs[cs.length - 1]);
            }
        }

        for (byte b : bs) {
        }

        System.out.println(str);

        byte[] sources = new byte[str.length() / 2];
        for (int i = 0; i < sources.length; i++) {
            int start = i << 1;
            String s = str.substring(start, start + 2);
            sources[i] = (byte) Integer.parseInt(s, 16);
        }

        for (byte source : sources) {
            System.out.println(source);
        }
    }
}
