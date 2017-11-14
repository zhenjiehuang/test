package huimei.split.model;

import java.util.List;

import com.hm.mayson.module.customer.template.Replace;

public class Punctuation {

    private String id;

    private String name;

    private TextWord start;

    private TextWord end;

    private List<Replace> replaces;

    private List<Punctuation> punctuations;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TextWord getStart() {
        return start;
    }

    public void setStart(TextWord start) {
        this.start = start;
    }

    public TextWord getEnd() {
        return end;
    }

    public void setEnd(TextWord end) {
        this.end = end;
    }

    public List<Replace> getReplaces() {
        return replaces;
    }

    public void setReplaces(List<Replace> replaces) {
        this.replaces = replaces;
    }

    public List<Punctuation> getPunctuations() {
        return punctuations;
    }

    public void setPunctuations(List<Punctuation> punctuations) {
        this.punctuations = punctuations;
    }

}
