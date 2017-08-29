package huimei.segment;

/**
 * Description:
 * Copyright (C) 2017 HuiMei All Right Reserved.
 * createDate：2017年7月28日
 * author：huangzhenjie
 * @version 1.0
 */
public class SegmentNode {

    private SegmentNode pre;

    private SegmentNode next;

    private Object data;

    public SegmentNode getPre() {
        return pre;
    }

    public void setPre(SegmentNode pre) {
        this.pre = pre;
    }

    public SegmentNode getNext() {
        return next;
    }

    public void setNext(SegmentNode next) {
        this.next = next;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
