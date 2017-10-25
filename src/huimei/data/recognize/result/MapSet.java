package huimei.data.recognize.result;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.hm.apollo.controller.PushService;
import com.hm.apollo.module.recognition.dao.NodeMapper;
import com.hm.apollo.module.recognition.dao.NodeRelationMapper;
import com.hm.apollo.module.recognition.dao.NodeSynonymMapper;
import com.hm.apollo.module.recognition.model.Node;
import com.hm.apollo.module.recognition.model.NodeRelation;
import com.hm.apollo.module.recognition.model.NodeSynonym;

public class MapSet {

    static Map<String, List<String>> map = new HashMap<String, List<String>>() {
        private static final long serialVersionUID = -1708856738489381658L;
        {
            put("绞痛", new ArrayList<String>(1000));
            put("压痛", new ArrayList<String>(1000));
            put("触痛", new ArrayList<String>(1000));
            put("疼痛", new ArrayList<String>(1000));
            put("隐痛", new ArrayList<String>(1000));
            put("肿痛", new ArrayList<String>(1000));
            put("胀痛", new ArrayList<String>(1000));
            put("反跳痛", new ArrayList<String>(1000));
            put("闷痛", new ArrayList<String>(1000));
            put("钝痛", new ArrayList<String>(1000));
            put("锐痛", new ArrayList<String>(1000));
            put("叩击痛", new ArrayList<String>(1000));
        }
    };

    @Autowired
    private NodeMapper nodeMapper;

    @Autowired
    private NodeSynonymMapper nodeSynonymMapper;

    @Autowired
    private NodeRelationMapper nodeRelationMapper;

    private Long nodeId = 1L;

    private Long modifyId = 77L;

    private Long insertNode() {
        Node node = new Node();
        node.setCreateDate(new Date());
        node.setFlag(1);
        node.setModifyDate(new Date());
        node.setModifyId(modifyId);
        node.setSysNode(1);

        nodeMapper.insert(node);

        return nodeId++;
    }


    private void insertRelation(Long parentId, Long childId) {
        NodeRelation record = new NodeRelation();
        record.setCreateDate(new Date());
        record.setFlag(1);
        record.setModifyDate(new Date());
        record.setModifyId(modifyId);
        record.setNodeId(parentId);
        record.setSubNodeId(childId);
        record.setRelationType(1);
        record.setStatus(9999);

        nodeRelationMapper.insert(record);
    }

    private void insertSynonym(String word, Long nodeId) {
        NodeSynonym record = new NodeSynonym();
        record.setCreateDate(new Date());
        record.setFlag(1);
        record.setIsNode(1);
        record.setModifyDate(new Date());
        record.setModifyId(modifyId);
        record.setNodeId(nodeId);
        record.setStatus(9999);
        record.setSynonymWord(word);

        nodeSynonymMapper.insertSelective(record);
    }

    @Test
    public void test() {
        try {
            String path = "D://";
            String fileName = "分型词.txt";
            try {
                BufferedReader br = new BufferedReader(new FileReader(new File(path, fileName)));

                String line = br.readLine();
                while ((line = br.readLine()) != null) {
                    boolean notMatch = true;
                    for (Entry<String, List<String>> entry : map.entrySet()) {
                        if (line.endsWith(entry.getKey())) {
                            entry.getValue().add(line);
                            notMatch = false;
                            break;
                        }
                    }
                    if (notMatch) {
                        if (line.endsWith("叩痛")) {
                            map.get("叩击痛").add(line);
                        } else if (line.endsWith("痛") || line.endsWith("疼")) {
                            map.get("疼痛").add(line);
                        }
                    }
                }

                for (Entry<String, List<String>> entry : map.entrySet()) {
                    Set<String> children = getChildren(entry.getKey());
                    NodeSynonym parent = getNode(entry.getKey());
                    for (String value : entry.getValue()) {
                        if (!children.contains(value)) {
                            NodeSynonym node = getNode(value);
                            if (node == null) {
                                Long nodeId = insertNode();
                                insertSynonym(value, nodeId);
                                insertRelation(parent.getNodeId(), nodeId);
                            } else {
                                insertRelation(parent.getNodeId(), node.getNodeId());
                            }
                            children.add(value);
                        }
                    }
                }

                br.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    static PushService service = new PushService();

    private static NodeSynonym getNode(String word) {
        NodeSynonym info = new NodeSynonym();
        info.setSynonymWord(word);

        String result = service.push("http://127.0.0.1:8080/apollo", "/getNodeSynonym/getWords", info);

        return JSONObject.parseObject(result).getObject("body", NodeSynonym.class);
    }

    private static Set<String> getChildren(String word) {
        NodeSynonym info = new NodeSynonym();
        info.setSynonymWord(word);

        String result = service.push("http://127.0.0.1:8080/apollo", "/getChildrenByWord/getWords", info);

        List<NodeSynonym> nodes = JSONObject.parseArray(JSONObject.parseObject(result).getString("body"),
                NodeSynonym.class);

        Set<String> ns = new HashSet<String>();
        if (nodes != null) {
            nodes.forEach(node -> {
                ns.add(node.getSynonymWord());
            });
        }

        return ns;
    }
}
