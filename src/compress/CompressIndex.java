package compress;

import java.util.LinkedList;
import java.util.List;

public class CompressIndex {

    private char code;

    private List<Long> index = new LinkedList<Long>();

    public char getCode() {
        return code;
    }

    public void setCode(char code) {
        this.code = code;
    }

    public List<Long> getIndex() {
        return index;
    }

    public void setIndex(List<Long> index) {
        this.index = index;
    }

    public void addIndex(Long index) {
        this.index.add(index);
    }

}
