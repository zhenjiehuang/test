package huimei.data.diagnose.model;

import java.util.List;

/**
 * @author lipeng
 * @date 2017/7/7
 */
public class MaysonRecognizeResultPojo {
    // 类型：1：诊断，2：临床表现，3：检查，4：评分结果
    Integer type;
    // 内容
    List<String> content;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public List<String> getContent() {
        return content;
    }

    public void setContent(List<String> content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MaysonRecognizeResultPojo) {
            MaysonRecognizeResultPojo o = (MaysonRecognizeResultPojo) obj;
            return content.containsAll(o.getContent());
        }
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return "MaysonRecognizeResultPojo{" +
                "type=" + type +
                ", content=" + content +
                '}';
    }
}
