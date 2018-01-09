package huimei.data.diagnose.model;

/**
 * 文字型识别结果
 *
 * @author lipeng
 * @date 2017/8/16
 */
public class RecognizeResultPojo {
    String word;
    int occur = 1;
    // 类型：目前只有诊断：1
    int conceptType;
    int conceptOrder=0;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getOccur() {
        return occur;
    }

    public void setOccur(int occur) {
        this.occur = occur;
    }

    public int getConceptType() {
        return conceptType;
    }

    public void setConceptType(int conceptType) {
        this.conceptType = conceptType;
    }

    public int getConceptOrder() {
        return conceptOrder;
    }

    public void setConceptOrder(int conceptOrder) {
        this.conceptOrder = conceptOrder;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RecognizeResultPojo) {
            RecognizeResultPojo o = (RecognizeResultPojo) obj;
            return word.equals(o.getWord());
        }
        return super.equals(obj);
    }
}
