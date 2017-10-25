package huimei.data.recognize.result;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hm.apollo.module.recognition.dao.NodeMapper;
import com.hm.apollo.module.recognition.dao.NodeRelationMapper;
import com.hm.apollo.module.recognition.dao.NodeSynonymMapper;
import com.hm.apollo.module.recognition.model.Node;
import com.hm.apollo.module.recognition.model.NodeRelation;
import com.hm.apollo.module.recognition.model.NodeSynonym;

/**
 * Description: Copyright (C) 2017 HuiMei All Right Reserved.
 * createDate：2017年7月24日 author：huangzhenjie
 *
 * @version 1.0
 */
@Service
public class UpdateNodeSynonymService {

    @Autowired
    private NodeRelationMapper nodeRelationMapper;

    @Autowired
    private NodeSynonymMapper nodeSynonymMapper;

    @Autowired
    private NodeMapper nodeMapper;

    private Long modifyId = 77L;

    public NodeSynonym getNode(String word) {
        List<NodeSynonym> nodes = nodeSynonymMapper.selectByWord(word);
        for (NodeSynonym node : nodes) {
            if (node.getIsNode() == 1L) {
                return node;
            }
        }

        return nodes.isEmpty() ? null : nodes.get(0);
    }

    public List<NodeSynonym> getChildren(String word) {
        List<NodeSynonym> nodes = nodeSynonymMapper.selectSubByWord(word);
        return nodes;
    }

    public void addSynonym(String node, String synonym) {
        NodeSynonym n = getNode(node);
        if (n == null) {
            System.out.println("同义词关系不存在：" + node + "/" + synonym);
            return;
        }

        NodeSynonym s = getNode(synonym);
        if (s != null) {
            List<NodeSynonym> children = getChildren(node);
            if (contains(children, synonym)) {
                System.out.println("同义词关系存在父子关系：" + node + "/" + synonym);
                return;
            }
            children = getChildren(synonym);
            if (contains(children, node)) {
                System.out.println("同义词关系存在父子关系：" + synonym + "/" + node);
                return;
            }
        }

        if (s == null || (s != null && s.getNodeId() != n.getNodeId())) {
            insertSynonym(synonym, n.getNodeId(), 0);
        }
    }

    public void addParent(String parent, String child) {
        NodeSynonym parentNode = getNode(parent);
        if (parentNode == null) {
            // 不存在
            System.out.println("父节点不存在：" + parent + "/" + child);
            return;
        }

        List<NodeSynonym> children = getChildren(parent);

        if (!contains(children, child)) {
            NodeSynonym childNode = getNode(child);
            if (childNode != null) {
                children = getChildren(child);
                if (contains(children, parent)) {
                    // 不存在
                    System.out.println("父子关系混乱：" + parent + "/" + child);
                    return;
                }
                insertRelation(parentNode.getNodeId(), childNode.getNodeId());
            } else {
                Long nodeId = insertNode();
                insertSynonym(child, nodeId, 1);
                insertRelation(parentNode.getNodeId(), nodeId);
            }
        }
    }

    private boolean contains(List<NodeSynonym> nodes, String word) {
        for (NodeSynonym node : nodes) {
            if (node.getSynonymWord().equals(word)) {
                return true;
            }
        }

        return false;
    }

    private Long insertNode() {
        Node node = new Node();
        node.setCreateDate(new Date());
        node.setFlag(1);
        node.setModifyDate(new Date());
        node.setModifyId(modifyId);
        node.setSysNode(0);

        nodeMapper.insert(node);

        return node.getId();
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
        record.setStatus(2);

        nodeRelationMapper.insert(record);
    }

    private void insertSynonym(String word, Long nodeId, int isNode) {
        NodeSynonym record = new NodeSynonym();
        record.setCreateDate(new Date());
        record.setFlag(1);
        record.setIsNode(isNode);
        record.setModifyDate(new Date());
        record.setModifyId(modifyId);
        record.setNodeId(nodeId);
        record.setStatus(2);
        record.setSynonymWord(word);

        nodeSynonymMapper.insertSelective(record);
    }

}
