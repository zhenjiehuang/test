package huimei.data.node;

import com.poi.excel.base.ExcelColumn;
import com.poi.excel.base.Type;

public class NodeExcel {

    @ExcelColumn(columnNum = 0, columnName = "别名", columnType = Type.String, length = 10000)
    private String word = "";

    @ExcelColumn(columnNum = 1, columnName = "父节点", columnType = Type.String, length = 10000)
    private String parent = "";

    @ExcelColumn(columnNum = 2, columnName = "节点名称", columnType = Type.String, length = 10000)
    private String node = "";

    @ExcelColumn(columnNum = 3, columnName = "是否有节点", columnType = Type.String, length = 10000)
    private String hasNode = "";

    @ExcelColumn(columnNum = 4, columnName = "是否有父节点", columnType = Type.String, length = 10000)
    private String hasParent = "";

    @ExcelColumn(columnNum = 5, columnName = "是否有同义词", columnType = Type.String, length = 10000)
    private String hasSynonym = "";

    @ExcelColumn(columnNum = 6, columnName = "是否审核通过", columnType = Type.String, length = 10000)
    private String hasPass = "";

    public NodeExcel() {
    }

    public NodeExcel(String word, String parent, String node, String hasNode, String hasParent,
            String hasSynonym) {
        super();
        this.word = word;
        this.parent = parent;
        this.node = node;
        this.hasNode = hasNode;
        this.hasParent = hasParent;
        this.hasSynonym = hasSynonym;
    }

    public NodeExcel(String word, String hasPass) {
        super();
        this.word = word;
        this.hasNode = "是";
        this.hasPass = hasPass;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public String getHasNode() {
        return hasNode;
    }

    public void setHasNode(String hasNode) {
        this.hasNode = hasNode;
    }

    public String getHasParent() {
        return hasParent;
    }

    public void setHasParent(String hasParent) {
        this.hasParent = hasParent;
    }

    public String getHasSynonym() {
        return hasSynonym;
    }

    public void setHasSynonym(String hasSynonym) {
        this.hasSynonym = hasSynonym;
    }

    public String getHasPass() {
        return hasPass;
    }

    public void setHasPass(String hasPass) {
        this.hasPass = hasPass;
    }

}
