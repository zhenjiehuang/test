package huimei.split.model;

public class KeySegment {

    private String id;

    private KeyProgress start;

    private KeyProgress end;

    private Punctuation punctuation;

    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public KeyProgress getStart() {
        return start;
    }

    public void setStart(KeyProgress start) {
        this.start = start;
    }

    public KeyProgress getEnd() {
        return end;
    }

    public void setEnd(KeyProgress end) {
        this.end = end;
    }

    public Punctuation getPunctuation() {
        return punctuation;
    }

    public void setPunctuation(Punctuation punctuation) {
        this.punctuation = punctuation;
    }

}
