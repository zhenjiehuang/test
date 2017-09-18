package huimei.data.node;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hm.apollo.framework.utils.BCConvertUtils;
import com.hm.apollo.module.cdss.dao.ClassificationWordMapper;
import com.hm.apollo.module.cdss.dao.DiseaseMapper;
import com.hm.apollo.module.cdss.dao.SymptomMapper;
import com.hm.apollo.module.cdss.model.ClassificationWord;
import com.hm.apollo.module.cdss.model.Disease;
import com.hm.apollo.module.knowledge.dao.DiseaseAliasMapper;
import com.hm.apollo.module.knowledge.model.DiseaseAlias;
import com.hm.apollo.module.recognition.dao.NodeSynonymMapper;
import com.hm.apollo.module.recognition.model.NodeSynonym;
import com.hm.apollo.module.recognition.service.NodeSynonymService;
import com.poi.excel.parse.ExportExcel;

/**
 * @author lipeng
 * @date 2017/8/30
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class ClassificationWordDataTask {
    @Autowired
    ClassificationWordMapper classificationWordMapper;

    @Autowired
    NodeSynonymService nodeSynonymService;

    @Autowired
    DiseaseAliasMapper diseaseAliasMapper;

    @Autowired
    DiseaseMapper diseaseMapper;

    @Autowired
    NodeSynonymMapper nodeSynonymMapper;

    @Autowired
    SymptomMapper symptomMapper;

    List<NodeSynonym> synonyms = null;

    private NodeSynonym getNodeSynonym(String word) {
        if (synonyms == null) {
            synonyms = nodeSynonymMapper.selectAll();
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

    private NodeSynonym getNode(NodeSynonym node) {
        if (node.getIsNode() == 1) {
            return node;
        }
        for (NodeSynonym synonym : synonyms) {
            if (synonym.getNodeId().equals(node.getNodeId()) && synonym.getIsNode() == 1) {
                return synonym;
            }
        }

        return null;
    }

    private boolean hasParent(String source, String parent, int level) {
        if(level>=10){
            return false;
        }
        List<NodeSynonym> parents = nodeSynonymService.getParentsByWord(source);
        if (parents == null) {
            return false;
        }
        for (NodeSynonym parentNode : parents) {
            if (BCConvertUtils.toBanjiaoLowercase(parentNode.getSynonymWord()).equalsIgnoreCase(parent)) {
                return true;
            } else {
                if (hasParent(parentNode.getSynonymWord(), parent, level + 1)) {
                    return true;
                }
            }
        }

        return false;
    }

    @Test
    public void classification() throws Exception {
        List<ClassificationWord> classificationWords = classificationWordMapper.selectAll();
        List<NodeExcel> findNodes = new ArrayList<NodeExcel>();
        List<NodeExcel> unConfirm = new ArrayList<>();
        List<NodeExcel> findParents = new ArrayList<NodeExcel>();

        for (ClassificationWord classificationWord : classificationWords) {
            String words = classificationWord.getWords();
            String call = BCConvertUtils.toBanjiaoLowercase(classificationWord.getCallbackWord());
            NodeSynonym nodeSynonym = getNodeSynonym(call);
            if (nodeSynonym == null) {
                findNodes.add(new NodeExcel(call, "", call, "否", "", ""));
            } else {
                if (nodeSynonym.getStatus() == 1) {
                    unConfirm.add(new NodeExcel(call, "否"));
                }
                nodeSynonym = getNode(nodeSynonym);
                if (StringUtils.isNotEmpty(words)) {
                    String[] wordArr = BCConvertUtils.toBanjiaoLowercase(words).split(",");
                    for (String s : wordArr) {
                        NodeSynonym nodeSynonym1 = getNodeSynonym(s);
                        if (nodeSynonym1 == null) {
                            findNodes.add(new NodeExcel(s, "", "", "否", "", ""));
                        } else {
                            nodeSynonym1 = getNode(nodeSynonym1);
                            Long callNodeId = nodeSynonym.getNodeId();
                            Long wordNodeId = nodeSynonym1.getNodeId();
                            if (!callNodeId.equals(wordNodeId)) {
                                // 不是同义词
                                if (!hasParent(s, call, 0)) {
                                    // 父子关系错误
                                    findParents.add(new NodeExcel(s, call, "", "", "否", ""));
                                }
                                // List<NodeSynonym> parents =
                                // nodeSynonymService.getParentsByWord(s);
                                // if (CollectionUtils.isNotEmpty(parents)) {
                                // List<String> parentList = parents.stream()
                                // .map(NodeSynonym::getSynonymWord).collect(Collectors.toList());
                                // if
                                // (!parentList.contains(BCConvertUtils.quanjiao2banjiao(call)))
                                // {
                                // // 父子关系错误
                                // findParents.add(new NodeExcel(s, call, "",
                                // "", "否", ""));
                                // }
                                // } else {
                                // // 父子关系错误
                                // findParents.add(new NodeExcel(s, call, "",
                                // "", "否", ""));
                                // }
                            }
                        }
                    }
                }
            }
        }

        ExportExcel<NodeExcel> p = new ExportExcel<>(findParents, NodeExcel.class);
        p.saveFile(new File("D://", "父节点-分型.xls"));

        ExportExcel<NodeExcel> u = new ExportExcel<>(unConfirm, NodeExcel.class);
        u.saveFile(new File("D://", "未审核-分型.xls"));


        ExportExcel<NodeExcel> n = new ExportExcel<>(findNodes, NodeExcel.class);
        n.saveFile(new File("D://", "不存在节点-分型.xls"));
    }

    // @Test
    public void diseaseAlias() {
        List<NodeExcel> unConfirm = new ArrayList<>();
        List<DiseaseAlias> diseases = diseaseAliasMapper.findAll();
        for (DiseaseAlias disease : diseases) {
            NodeSynonym node = getNodeSynonym(disease.getAlias());
            if (node != null && node.getStatus() == 1) {
                unConfirm.add(new NodeExcel(disease.getAlias(), "否"));
            } else {
                unConfirm.add(new NodeExcel(disease.getAlias(), "", "", "否", "", ""));
            }
        }

        ExportExcel<NodeExcel> n = new ExportExcel<>(unConfirm, NodeExcel.class);
        n.saveFile(new File("D://", "诊断别名.xls"));
    }

    // @Test
    public void disease() {
        List<NodeExcel> unConfirm = new ArrayList<>();
        List<Disease> diseases = diseaseMapper.selectAll();
        for (Disease disease : diseases) {
            NodeSynonym node = getNodeSynonym(disease.getDiseaseName());
            if (node != null && node.getStatus() == 1) {
                unConfirm.add(new NodeExcel(disease.getDiseaseName(), "否"));
            } else {
                unConfirm.add(new NodeExcel(disease.getDiseaseName(), "", "", "否", "", ""));
            }
        }

        ExportExcel<NodeExcel> n = new ExportExcel<>(unConfirm, NodeExcel.class);
        n.saveFile(new File("D://", "诊断.xls"));
    }

    // @Test
    public void symptom() {
        List<NodeExcel> unConfirm = new ArrayList<>();
        List<String> symptoms = symptomMapper.getAllName();
        for (String symptom : symptoms) {
            NodeSynonym node = getNodeSynonym(symptom);
            if (node != null && node.getStatus() == 1) {
                unConfirm.add(new NodeExcel(symptom, "否"));
            } else {
                unConfirm.add(new NodeExcel(symptom, "", "", "否", "", ""));
            }
        }

        ExportExcel<NodeExcel> n = new ExportExcel<>(unConfirm, NodeExcel.class);
        n.saveFile(new File("D://", "诊断因子.xls"));
    }
}
