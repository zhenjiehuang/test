package huimei.split;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.dom4j.tree.DefaultElement;

import com.alibaba.druid.util.StringUtils;
import com.hm.mayson.module.customer.template.Replace;

import huimei.split.model.KeyProgress;
import huimei.split.model.KeySegment;
import huimei.split.model.Punctuation;
import huimei.split.model.TextField;
import huimei.split.model.TextTemplate;
import huimei.split.model.TextWord;

public class SpliteTest2 {

    private Map<Integer, TextTemplate> templates = new HashMap<Integer, TextTemplate>();

    public static void main(String[] args) {
        SpliteTest2 split = new SpliteTest2();
        split.init();
        split.parser(SpliteTest.progress, 2);
    }

    public Map<String, KeySegment> parser(String text, int progressType) {
        TextTemplate textTemplate = templates.get(progressType);

        if (textTemplate == null) {
            return null;
        }

        Map<String, KeySegment> keyMap = new HashMap<String, KeySegment>();
        putKeySegment(text, textTemplate.getPunctuations(), keyMap);

        // for (String key : keyMap.keySet()) {
        // System.out.println(key + "" +
        // JSONObject.toJSONString(keyMap.get(key)));
        // }

        return keyMap;
    }

    private void putKeySegment(String text, List<Punctuation> punctuations, Map<String, KeySegment> keyMap) {
        if (CollectionUtils.isEmpty(punctuations)) {
            return;
        }

        // TextWord覆盖了equals方法，让index和关键词相同的视为同一个对象
        Map<TextWord, KeyProgress> words = new HashMap<TextWord, KeyProgress>();
        List<KeySegment> segments = new ArrayList<KeySegment>();
        for (Punctuation punctuation : punctuations) {
            KeySegment segment = new KeySegment();
            segment.setStart(getProgress(text, punctuation.getStart(), words));
            segment.setEnd(getProgress(text, punctuation.getEnd(), words));
            segment.setId(punctuation.getId());
            segment.setPunctuation(punctuation);
            segments.add(segment);
            // id不为空，需要存起来，后面需要使用
            if (!StringUtils.isEmpty(segment.getId())) {
                keyMap.put(segment.getId(), segment);
            }
        }

        List<KeyProgress> keyList = words.values().stream().filter(key -> key.getIndex() != -1)
                .collect(Collectors.toList());
        KeyProgress[] keys = new KeyProgress[keyList.size()];
        keys = keyList.toArray(keys);
        // 对关键词索引进行排序，从小到大
        Arrays.sort(keys);
        segmentProgress(text, segments, Arrays.asList(keys), keyMap);
    }

    // 截取内容
    private void segmentProgress(String text, List<KeySegment> segments, List<KeyProgress> keys,
            Map<String, KeySegment> keyMap) {
        for (KeySegment segment : segments) {
            KeyProgress start = segment.getStart();
            KeyProgress end = segment.getEnd();

            String value = "";
            // -1的表示开始关键词不存在，直接忽略
            if (start.getIndex() != -1) {
                if (end == null) {
                    // keys经过index从小到大排序，获取到start的index后，index+1个就是内容结束的地方
                    int index = keys.indexOf(start);
                    if (index == -1) {
                        value = text.substring(start.getIndex() + start.getKeyWord().length());
                    } else {
                        // TODO 截取字符串的时候是否要去掉关键词，待定
                        if (index + 1 == keys.size()) {
                            value = text.substring(start.getIndex() + start.getKeyWord().length());
                        } else {
                            value = text.substring(start.getIndex() + start.getKeyWord().length(),
                                    keys.get(index + 1).getIndex());
                        }
                    }
                } else if (end.getIndex() != -1) {
                    value = text.substring(start.getIndex() + start.getKeyWord().length(), end.getIndex());
                }
                // 替换字符
                List<Replace> replaces = segment.getPunctuation().getReplaces();
                if (CollectionUtils.isNotEmpty(replaces)) {
                    for (Replace replace : replaces) {
                        value = value.replaceAll(replace.getSource(), replace.getTarget());
                    }
                }
            }
            segment.setText(value);
            putKeySegment(value, segment.getPunctuation().getPunctuations(), keyMap);
        }
    }

    private KeyProgress getProgress(String text, TextWord word, Map<TextWord, KeyProgress> map) {
        if (word == null) {
            return null;
        }

        // 相同关键词的指向同一个对象，有利于后面的截取字符串
        KeyProgress key = map.get(word);
        if (key != null) {
            return key;
        }

        key = new KeyProgress();
        key.setKeyWord(word.getText());
        key.setWordIndex(word.getIndex());

        String keyWord = key.getKeyWord();
        int keyIndex = -1;
        int loop = 0;
        int index = -1;
        while ((keyIndex = text.indexOf(keyWord, keyIndex)) != -1) {
            index = keyIndex;
            keyIndex += keyWord.length();
            if (loop++ == key.getWordIndex()) {
                break;
            }
        }

        key.setIndex(index);
        map.put(word, key);

        return key;
    }

    public void init() {
        try {
            File file = new File("Z:/workspace/huimei/mayson/mayson/src/main/resources/text/xuanwu.xml");
            SAXReader xmlReader = new SAXReader();
            Document document = xmlReader.read(file);
            DefaultElement root = (DefaultElement) document.selectSingleNode("//templates");
            List<?> templates = root.elements("template");

            for (Object template : templates) {
                TextTemplate textTemplate = getTemplate((DefaultElement) template);
                this.templates.put(textTemplate.getProgressType(), textTemplate);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private TextTemplate getTemplate(DefaultElement template) {
        TextTemplate textTemplate = new TextTemplate();
        textTemplate.setProgressType(Integer.valueOf(template.attributeValue("progressType")));
        DefaultElement punctuations = (DefaultElement) template.element("punctuations");
        textTemplate.setPunctuations(getPunctuations(punctuations.elements("punctuation")));
        Set<String> ids = new HashSet<String>();
        textTemplate.setFields(getField(template.elements("field"), ids));
        textTemplate.setIds(ids);

        return textTemplate;
    }

    private List<TextField> getField(List<?> nodes, Set<String> ids) {
        if (CollectionUtils.isEmpty(nodes)) {
            return null;
        }

        List<TextField> fields = new ArrayList<TextField>();
        for (Object obj : nodes) {
            DefaultElement node = (DefaultElement) obj;
            TextField field = new TextField();
            field.setAttr(node.attributeValue("attr"));
            field.setName(node.attributeValue("attrName"));
            fields.add(field);
            List<?> punctuations = node.elements("punctuation");
            if (CollectionUtils.isNotEmpty(punctuations)) {
                field.setPunctuations(new ArrayList<Punctuation>());
                for (Object o : punctuations) {
                    DefaultElement element = (DefaultElement) o;
                    Punctuation punctuation = new Punctuation();
                    punctuation.setId(element.attributeValue("id"));
                    punctuation.setName(element.attributeValue("name"));
                    field.getPunctuations().add(punctuation);
                    ids.add(punctuation.getId());
                }
            }
        }

        return fields;
    }

    private List<Punctuation> getPunctuations(List<?> nodes) {
        if (CollectionUtils.isEmpty(nodes)) {
            return null;
        }

        List<Punctuation> punctuations = new ArrayList<Punctuation>();
        for (Object obj : nodes) {
            DefaultElement node = (DefaultElement) obj;

            Punctuation punctuation = new Punctuation();
            punctuation.setId(node.attributeValue("id"));
            punctuation.setName(node.attributeValue("name"));
            punctuation.setStart(getTextWord(node, "start"));
            punctuation.setEnd(getTextWord(node, "end"));
            punctuation.setReplaces(getReplace(node.selectNodes("replace")));
            punctuation.setPunctuations(getPunctuations(node.elements("punctuation")));
            punctuations.add(punctuation);
        }

        return punctuations;
    }

    private TextWord getTextWord(DefaultElement node, String nodeName) {
        DefaultElement text = (DefaultElement) node.element(nodeName);
        if (text == null) {
            return null;
        }

        TextWord word = new TextWord();
        word.setText(text.getTextTrim());
        String index = text.attributeValue("index");
        if (StringUtils.isNumber(index)) {
            word.setIndex(Integer.valueOf(index));
        }

        return word;
    }

    private List<Replace> getReplace(List<?> nodes) {
        if (CollectionUtils.isEmpty(nodes)) {
            return null;
        }

        List<Replace> replaces = new ArrayList<Replace>();
        for (Object obj : nodes) {
            DefaultElement node = (DefaultElement) obj;
            String source = node.attributeValue("source");
            String target = node.getText();
            if (!StringUtils.isEmpty(source)) {
                Replace replace = new Replace();
                replace.setSource(source);
                replace.setTarget(target);
                replaces.add(replace);
            }
        }

        return replaces;
    }
}
