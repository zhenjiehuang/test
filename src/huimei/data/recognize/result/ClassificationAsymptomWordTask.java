package huimei.data.recognize.result;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hm.apollo.framework.utils.BCConvertUtils;
import com.hm.apollo.module.cdss.dao.ClassificationWordMapper;
import com.hm.apollo.module.cdss.dao.SymptomCallbackMapper;
import com.hm.apollo.module.cdss.dao.SymptomMapper;
import com.hm.apollo.module.cdss.model.ClassificationWord;
import com.hm.apollo.module.cdss.model.Symptom;
import com.hm.apollo.module.cdss.model.SymptomCallback;
import com.poi.excel.parse.ImportExcel;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class ClassificationAsymptomWordTask {

    @Autowired
    private ClassificationWordMapper classificationWordMapper;

    @Autowired
    private SymptomCallbackMapper symptomCallbackMapper;

    @Autowired
    private SymptomMapper symptomMapper;

    private Map<String, Symptom> map;

    private Map<String, List<SymptomCallback>> symptomCallback;

    private Symptom getSymptom(String name) {
        if (map == null) {
            List<Symptom> symptoms = symptomMapper.getAllBriefSymptom();
            Map<String, Symptom> map = new HashMap<String, Symptom>();
            for (Symptom symptom : symptoms) {
                map.put(symptom.getName(), symptom);
            }
            this.map = map;
        }

        return map.get(name);
    }

    private SymptomCallback getSymptomCallback(String name, long id) {
        if (symptomCallback == null) {
            List<SymptomCallback> words = symptomCallbackMapper.selectAllUsedSymptomCallbacks();
            Map<String, List<SymptomCallback>> map = words.stream()
                    .collect(Collectors.groupingBy(SymptomCallback::getCallbackWord));
            symptomCallback = map;
        }

        List<SymptomCallback> words = symptomCallback.get(name);
        if (words == null) {
            return null;
        }

        for (SymptomCallback word : words) {
            if (word.getSymptomId().equals(id)) {
                return word;
            }
        }

        return null;
    }

    @Test
    public void test() {
        try {
            ImportExcel<ClassificationAsymptomWord> im = new ImportExcel<>(
                    new File("D://分组id_诊断因子_核心词_分型词.xlsx"),
                    ClassificationAsymptomWord.class);

            for (ClassificationAsymptomWord word : im.getRowDatas()) {
                if ("1".equals(word.getData5())) {
                    ClassificationWord classWord = classificationWordMapper.selectByWord(word.getData2(), "");
                    if (classWord == null) {
                        System.out.println("分型词不存在:" + word.getData2());
                    } else {
                        classWord.setWords(BCConvertUtils.quanjiao2banjiao(word.getData3()));
                        classificationWordMapper.updateByPrimaryKey(classWord);
                    }
                }

                Symptom symptom = getSymptom(word.getData1());
                if (symptom == null) {
                    System.out.println("诊断因子不存在:" + word.getData2());
                }

                SymptomCallback callback = getSymptomCallback(word.getData2(), symptom.getId());

                if ("新增".equals(word.getData4())) {
                    if (callback == null) {
                        callback = new SymptomCallback();
                        callback.setCallbackWord(word.getData2());
                        callback.setStatus(10);
                        callback.setSymptomId(symptom.getId());
                        callback.setModifyDate(new Date());
                        symptomCallbackMapper.insert(callback);
                    } else {
                        System.out.println("关系存在:" + word.getData1() + "" + word.getData2());
                    }
                } else if ("删除".equals(word.getData4())) {
                    if (callback == null) {
                        // System.out.println("关系不存在:" + word.getData2());
                    } else {
                        callback.setStatus(0);
                        // symptomCallbackMapper.updateByPrimaryKeySelective(callback);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
