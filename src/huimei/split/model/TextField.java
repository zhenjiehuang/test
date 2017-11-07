package huimei.split.model;

import java.util.List;

public class TextField {

    private String name;

    private String attr;

    private List<Punctuation> punctuations;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAttr() {
        return attr;
    }

    public void setAttr(String attr) {
        this.attr = attr;
    }

    public List<Punctuation> getPunctuations() {
        return punctuations;
    }

    public void setPunctuations(List<Punctuation> punctuations) {
        this.punctuations = punctuations;
    }

}
