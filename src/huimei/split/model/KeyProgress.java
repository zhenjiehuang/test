package huimei.split.model;

public class KeyProgress implements Comparable<KeyProgress> {

    /**
     * 索引
     */
    private Integer index;

    /**
     * 第index个匹配项
     */
    private Integer wordIndex;

    private String id;

    private String keyWord;

    private boolean endWord = false;

    private String progress;

    public Integer getWordIndex() {
        return wordIndex;
    }

    public void setWordIndex(Integer wordIndex) {
        this.wordIndex = wordIndex;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public String getProgress() {
        return progress;
    }

    public boolean isEndWord() {
        return endWord;
    }

    public void setEndWord(boolean endWord) {
        this.endWord = endWord;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    @Override
    public int compareTo(KeyProgress o) {
        int i = index - o.index;
        if (i == 0) {
            return 0;
        } else if (i > 0) {
            return 1;
        } else {
            return -1;
        }
    }

    // @Override
    // public boolean equals(Object obj) {
    // if (obj instanceof KeyProgress) {
    // KeyProgress key = (KeyProgress) obj;
    // return key.wordIndex == wordIndex && key.keyWord.equals(keyWord);
    // }
    // return false;
    // }
}
