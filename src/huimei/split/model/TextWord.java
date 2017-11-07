package huimei.split.model;

public class TextWord {

    private int index;

    private String text;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TextWord) {
            TextWord word = (TextWord) obj;
            return word.index == index && word.text.equals(text);
        }
        return false;
    }

}
