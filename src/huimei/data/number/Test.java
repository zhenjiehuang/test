package huimei.data.number;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.hm.apollo.module.recognition.service.impl.UnitRecognitionServiceImpl;

public class Test {

    public static void main(String[] args) {
        UnitRecognitionServiceImpl re = new UnitRecognitionServiceImpl();
        String str = "前列腺癌 髋部或股骨颈T值-1.0";


        List<String> items = re.parseItemWords("前列腺癌 髋部或股骨颈T值-1.0", null);

        System.out.println(JSON.toJSONString(items));
    }
}
