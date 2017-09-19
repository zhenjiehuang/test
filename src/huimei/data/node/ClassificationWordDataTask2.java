package huimei.data.node;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hm.apollo.framework.utils.BCConvertUtils;
import com.hm.apollo.module.recognition.dao.NodeSynonymMapper;
import com.hm.apollo.module.recognition.model.NodeSynonym;
import com.mysql.jdbc.StringUtils;
import com.poi.excel.parse.ExportExcel;
import com.poi.excel.parse.ImportExcel;

/**
 * @author lipeng
 * @date 2017/8/30
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class ClassificationWordDataTask2 {
    @Autowired
    NodeSynonymMapper nodeSynonymMapper;

    List<NodeSynonym> synonyms = null;

    private NodeSynonym getNodeSynonym(String word) {
        if (synonyms == null) {
            synonyms = nodeSynonymMapper.selectAll();
            // synonyms = new ArrayList<NodeSynonym>();
        }

        word = BCConvertUtils.toBanjiaoLowercase(word);
        NodeSynonym result = null;
        for (NodeSynonym synonym : synonyms) {
            if (synonym.getFlag() == 1 && synonym.getSynonymWord().equalsIgnoreCase(word)) {
                if (synonym.getStatus() == 2) {
                    return synonym;
                } else if (synonym.getStatus() == 1) {
                    result = synonym;
                }
            }
        }

        return result;
    }

    @Test
    public void test() {
        ImportExcel<Excel> im = new ImportExcel<>(new File("C:\\Users\\Administrator\\Desktop", "Book1.xlsx"),
                Excel.class);

        Set<Excel2> set = new HashSet<Excel2>();

        int i = 0;
        List<Excel> datas = im.getRowDatas();
        for (Excel data : datas) {
            if ("是".equals(data.getData7())) {
                check("第一列", data.getData1(), set);
            }

            check("项目", data.getData3(), set);
            check("单位", data.getData6(), set);
            // System.out.println(++i);
        }

        ExportExcel<Excel2> ex = new ExportExcel<>(new ArrayList<>(set), Excel2.class);
        ex.saveFile(new File("D://", "11111.xls"));
    }

    private void check(String sheet, String data, Set<Excel2> list) {
        if (StringUtils.isNullOrEmpty(data)) {
            return;
        }
        NodeSynonym node = getNodeSynonym(data);
        if (node == null) {
            // 不存在
            list.add(new Excel2(sheet, 0L, data, "否", "否"));
        } else if (node.getStatus() == 1) {
            // 审核未通过
            list.add(new Excel2(sheet, node.getId(), data, "是", "否"));
        } else {
            // list.add(new Excel2(sheet, node.getId(), data, "是", "否"));
        }
    }

}
