package huimei.split.model;

import java.util.List;
import java.util.Set;

public class TextTemplate {

    private int progressType;

    private List<Punctuation> punctuations;

    private List<TextField> fields;

    private Set<String> ids;

    public int getProgressType() {
        return progressType;
    }

    public void setProgressType(int progressType) {
        this.progressType = progressType;
    }

    public List<Punctuation> getPunctuations() {
        return punctuations;
    }

    public void setPunctuations(List<Punctuation> punctuations) {
        this.punctuations = punctuations;
    }

    public List<TextField> getFields() {
        return fields;
    }

    public void setFields(List<TextField> fields) {
        this.fields = fields;
    }

    public Set<String> getIds() {
        return ids;
    }

    public void setIds(Set<String> ids) {
        this.ids = ids;
    }

}
