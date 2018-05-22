package huimei.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.hm.apollo.framework.utils.Utils;

public class Test {

    public static void main(String[] args) throws IOException {
        Integer aId = 1;
        Integer bId = null;
        Integer cId = 1;
        Integer dId = null;
        System.out.println(aId + (bId == null ? ""
                : ("_" + bId) + (cId == null ? "" : ("_" + cId)) + (dId == null ? "" : "_" + dId)));

        List<List<String>> list = new ArrayList<>();
        list.add(Arrays.asList("a", "b", "c"));
        list.add(Arrays.asList("A", "B", "C"));
        list.add(Arrays.asList("1", "2", "3"));

        System.out.println(JSON.toJSONString(Utils.cartesianProduct(list)));
        System.out.println(JSON
                .toJSONString(Utils.cartesianProduct(list).stream().map(l -> StringUtils.join(l.toArray()))
                        .collect(Collectors.toList())));

        // System.out.println("gethry\\pP[P|p]".endsWith("[P|p]"));

        // System.out.println("gethry\\pP[P|p]".endsWith("[P|p]"));

        System.out.println("µmol/L".matches("^[A-Za-z]+$"));
        System.out.println(System.currentTimeMillis());

        List<String> s = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            s.add(i + "");

        }

        for (int i = 0; i < s.size(); i += 10) {
            if (s.size() < i + 10) {
                System.out.println(s.subList(i, s.size()));
            } else {
                System.out.println(s.subList(i, i + 10));
            }
        }
    }

}
