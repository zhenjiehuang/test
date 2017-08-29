package huimei.data.recognize;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.hm.apollo.module.cdss.enums.RecognizePojoTypeEnum;
import com.hm.apollo.module.cdss.enums.WordTypeEnum;
import com.hm.apollo.module.cdss.model.CallbackWordPojo;
import com.hm.apollo.module.cdss.model.response.RecognizeWordsPojo;
import com.hm.apollo.module.cdss.pojo.WordsAndCallbackWordsPojo;
import com.hm.apollo.module.cdss.service.SymptomService;

/**
 * Description:
 * Copyright (C) 2017 HuiMei All Right Reserved.
 * createDate：2017年7月18日
 * author：huangzhenjie
 * @version 1.0
 */
public class Recoginze {

    // 单独可召回的类型
    static List<Integer> indexOnly1Types = Arrays.asList(WordTypeEnum.症状词.getType(),
            WordTypeEnum.疾病词.getType(), WordTypeEnum.项目词.getType());
    // 出现在召回词第一位的类型
    static List<Integer> index1Types = Arrays.asList(WordTypeEnum.症状词.getType(), WordTypeEnum.查体词.getType(),
            WordTypeEnum.项目词.getType(), WordTypeEnum.条件词.getType(), WordTypeEnum.检查名称.getType());
    // 出现在召回词第二位的类型
    static List<Integer> index2Types = Arrays.asList(WordTypeEnum.症状词.getType(), WordTypeEnum.表现词.getType(),
            WordTypeEnum.项目值.getType(), WordTypeEnum.查体词.getType(), WordTypeEnum.项目词.getType());
    // 出现在召回词第二位的类型
    static List<Integer> index3Types = Arrays.asList(WordTypeEnum.表现词.getType(), WordTypeEnum.项目值.getType(),
            WordTypeEnum.查体词.getType());

    // 全部召回词
    static Set<String> allCallbackWords = new HashSet<>();

    static Set<String> allSymptomPhrases = new HashSet<>();

    static Set<String> allClassificationWords = new HashSet<>();

    static Map<String, List<String>> classificationWordMap = new ConcurrentHashMap<>();

    static Map<Integer, List<String>> preTypesListMap2 = new HashMap<>();

    static Map<Integer, List<Integer>> preTypesListMap = new HashMap<>();

    static Map<Integer, List<Integer>> nexTypesListMap = new HashMap<>();

    SymptomService symptomService;

    public void smartCallbackWord(List<RecognizeWordsPojo> words,
            List<Integer> commaIndexList, List<Integer> negativeIndexList, List<Integer> behindNegativeList,
            List<String> callbackWords, List<WordsAndCallbackWordsPojo> wordsAndCallbackWordsPojos,
            Map<String, CallbackWordPojo> allCallbackWordPojoMap) {

        List<CallbackWordPojo> re = new ArrayList<>();
        //
        List<Integer> reverseCommaIndexList = commaIndexList.stream().sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
        List<Integer> reverseNegativeIndexList = negativeIndexList.stream().sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
        Map<Integer, List<RecognizeWordsPojo>> typeWordPojosMap = new HashMap<>();
        for (RecognizeWordsPojo pojo : words) {
            Integer type = pojo.getType();
            List<RecognizeWordsPojo> typeWordPojos = typeWordPojosMap.get(type);
            if (typeWordPojos == null) {
                typeWordPojos = new ArrayList<>();
                typeWordPojosMap.put(type, typeWordPojos);
            }
            typeWordPojos.add(pojo);
        }

        // 先识别查体表现和症状
        // 前一个指定类型的词
        Map<Integer, RecognizeWordsPojo> preMap = new HashMap<>();
        Map<String, String> preMap2 = new HashMap<>();
        for (RecognizeWordsPojo pojo : words) {
            Integer type = pojo.getType();
            String word = pojo.getWord();
            Integer index = pojo.getTypeIndex();
            if (index1Types.contains(type) && index2Types.contains(type)) {
                RecognizeWordsPojo preWordPojo = preMap.get(type);
                if (preWordPojo != null) {
                    String preTypeWord = preWordPojo.getWord();
                    String callbackWord = preTypeWord + word;
                    preMap2.put(type + "_" + type, callbackWord);
                }
            }
            if (index1Types.contains(type) || indexOnly1Types.contains(type)) {
                preMap.put(type, pojo);
                if (indexOnly1Types.contains(type)) {
                    // 如果这个词是单独可以召回的类型
                    // 如果不是阴性特征
                    String callbackWord = this.getOneCallbackWord(word);
                    if (StringUtils.isNotEmpty(callbackWord) && pojo.isOriginal()) {// 原始词汇才加入返回结果中
                        CallbackWordPojo callbackWordPojo = allCallbackWordPojoMap.get(callbackWord);
                        if (callbackWordPojo == null) {
                            callbackWordPojo = new CallbackWordPojo();
                            allCallbackWordPojoMap.put(callbackWord, callbackWordPojo);
                            re.add(callbackWordPojo);
                        }
                        callbackWordPojo.setCallbackWordSource(word);
                        callbackWordPojo.setCallbackWord(callbackWord);
                        if (WordTypeEnum.疾病词.getType().equals(type)) {
                            callbackWordPojo.setType(RecognizePojoTypeEnum.诊断.getType());
                        } else {
                            callbackWordPojo.setType(RecognizePojoTypeEnum.临床表现.getType());
                        }
                        String bodyPart = this.getCallbackWordInfo(word, index,
                                typeWordPojosMap.get(WordTypeEnum.方位词.getType()), commaIndexList,
                                reverseCommaIndexList);
                        if (StringUtils.isNotEmpty(bodyPart)) {
                            callbackWordPojo.setPart(bodyPart);
                        }
                        String time = this.getCallbackWordInfo(word, index,
                                typeWordPojosMap.get(WordTypeEnum.时间词.getType()), commaIndexList,
                                reverseCommaIndexList);
                        if (StringUtils.isNotEmpty(time)) {
                            callbackWordPojo.setTime(time);
                        }

                        List<String> attributeList = this.getCallbackWordInfoByTypes(word, index,
                                typeWordPojosMap, commaIndexList, reverseCommaIndexList,
                                WordTypeEnum.性状词.getType());
                        if (CollectionUtils.isNotEmpty(attributeList)) {
                            callbackWordPojo.setAttribute(attributeList);
                        }
                        if (!this.isNegative(index, reverseCommaIndexList, reverseNegativeIndexList,
                                behindNegativeList)) {
                            WordsAndCallbackWordsPojo wordsAndCallbackWordsPojo = this
                                    .getWordsAndCallbackWordsPojo(word, word);
                            wordsAndCallbackWordsPojos.add(wordsAndCallbackWordsPojo);
                            callbackWords.addAll(this.callbacks(word));
                            callbackWordPojo.setOccur(1);
                        } else {
                            callbackWordPojo.setOccur(0);
                        }

                    }

                }
            }
            if (index2Types.contains(type)) {
                // 出现在召回词第2位的类型
                // 找到对应第一位的最近一个词，组成召回词
                List<Integer> preTypes = preTypesListMap.get(type);
                for (Integer preType : preTypes) {
                    RecognizeWordsPojo preWordPojo = preMap.get(preType);
                    if (preWordPojo != null) {
                        String preTypeWord = preWordPojo.getWord();
                        if (StringUtils.isNotEmpty(preTypeWord)) {
                            if (!StringUtils.equalsIgnoreCase(preTypeWord, word)) {
                                String callbackWord = preTypeWord + word;
                                preMap2.put(preType + "_" + type, callbackWord);
                                List<String> callbacks = this.callbacks(callbackWord);
                                if (CollectionUtils.isNotEmpty(callbacks)) {
                                    if (!this.isNegative(preWordPojo.getTypeIndex(), index,
                                            reverseCommaIndexList, reverseNegativeIndexList,
                                            behindNegativeList)) {
                                        WordsAndCallbackWordsPojo wordsAndCallbackWordsPojo = this
                                                .getWordsAndCallbackWordsPojo(callbackWord, preTypeWord,
                                                        word);
                                        wordsAndCallbackWordsPojos.add(wordsAndCallbackWordsPojo);
                                        callbackWords.addAll(callbacks);
                                    }
                                    if (callbackWords.contains(word)
                                            && allCallbackWords.contains(callbackWord)) {
                                        // 如果召回词中有关节发热，发热，去掉发热。
                                        callbackWords.remove(word);
                                    }
                                }

                                //////////////////////////////////////////

                                String callWord = this.getOneCallbackWord(callbackWord);
                                CallbackWordPojo callbackWordPojo = null;
                                if (StringUtils.isNotEmpty(callWord) && pojo.isOriginal()) {

                                    if (WordTypeEnum.项目词.getType().equals(preWordPojo.getType())) {
                                        callbackWordPojo = allCallbackWordPojoMap.get(preTypeWord);
                                        if (callbackWordPojo == null) {
                                            callbackWordPojo = new CallbackWordPojo();
                                            allCallbackWordPojoMap.put(preTypeWord, callbackWordPojo);
                                            re.add(callbackWordPojo);
                                        }
                                        callbackWordPojo.setType(RecognizePojoTypeEnum.辅助检查.getType());
                                        callbackWordPojo.setItemName(preTypeWord);
                                        callbackWordPojo.setItemValue(word);
                                    } else {
                                        callbackWordPojo = allCallbackWordPojoMap.get(callWord);
                                        if (callbackWordPojo == null) {
                                            callbackWordPojo = new CallbackWordPojo();
                                            allCallbackWordPojoMap.put(callWord, callbackWordPojo);
                                            re.add(callbackWordPojo);
                                        }
                                        callbackWordPojo.setCallbackWordSource(callbackWord);
                                        callbackWordPojo.setCallbackWord(callWord);
                                        callbackWordPojo.setType(RecognizePojoTypeEnum.临床表现.getType());
                                        String bodyPart = this.getCallbackWordInfo(word, index,
                                                typeWordPojosMap.get(WordTypeEnum.方位词.getType()),
                                                commaIndexList, reverseCommaIndexList);
                                        if (StringUtils.isNotEmpty(bodyPart)) {
                                            callbackWordPojo.setPart(bodyPart);
                                        }

                                        String time = this.getCallbackWordInfo(word, index,
                                                typeWordPojosMap.get(WordTypeEnum.时间词.getType()),
                                                commaIndexList, reverseCommaIndexList);
                                        if (StringUtils.isNotEmpty(time)) {
                                            callbackWordPojo.setTime(time);
                                        }
                                        List<String> attributeList = this.getCallbackWordInfoByTypes(word,
                                                index, typeWordPojosMap, commaIndexList,
                                                reverseCommaIndexList, WordTypeEnum.性状词.getType());
                                        if (CollectionUtils.isNotEmpty(attributeList)) {
                                            callbackWordPojo.setAttribute(attributeList);
                                        }
                                    }
                                    if (!this.isNegative(index, reverseCommaIndexList,
                                            reverseNegativeIndexList, behindNegativeList)) {
                                        callbackWordPojo.setOccur(1);
                                    } else {
                                        callbackWordPojo.setOccur(0);
                                    }
                                }
                                ///////////////////////////////////////////////////////
                            }
                        }
                    }
                }
            }
            if (index3Types.contains(type)) {
                // 出现在召回词第2位的类型
                // 找到对应第一位的最近一个词，组成召回词
                List<String> preTypes = preTypesListMap2.get(type);
                for (String preType : preTypes) {
                    String preTypeWord = preMap2.get(preType);
                    if (StringUtils.isNotEmpty(preTypeWord)) {
                        String callbackWord = preTypeWord + word;
                        WordsAndCallbackWordsPojo wordsAndCallbackWordsPojo = this
                                .getWordsAndCallbackWordsPojo(callbackWord, preTypeWord, word);
                        wordsAndCallbackWordsPojos.add(wordsAndCallbackWordsPojo);
                        callbackWords.addAll(this.callbacks(callbackWord));
                    }
                }
            }
        }

        Map<Integer, RecognizeWordsPojo> nextMap = new HashMap<>();
        for (int i = words.size() - 1; i >= 0; i--) {
            RecognizeWordsPojo pojo = words.get(i);
            Integer type = pojo.getType();
            String word = pojo.getWord();
            Integer index = pojo.getTypeIndex();
            if (index1Types.contains(type)) {
                List<Integer> nextTypes = nexTypesListMap.get(type);
                if (CollectionUtils.isNotEmpty(nextTypes)) {
                    for (Integer nextType : nextTypes) {
                        RecognizeWordsPojo nextWordPojo = nextMap.get(nextType);
                        if (nextWordPojo != null) {
                            String nextWord = nextWordPojo.getWord();
                            if (StringUtils.isNotEmpty(nextWord)) {
                                String callbackWord = word + nextWord;
                                List<String> callbacks = this.callbacks(callbackWord);
                                if (CollectionUtils.isNotEmpty(callbacks)) {
                                    if (!this.isNegative(index, nextWordPojo.getTypeIndex(),
                                            reverseCommaIndexList, reverseNegativeIndexList,
                                            behindNegativeList)) {
                                        callbackWords.addAll(callbacks);
                                        WordsAndCallbackWordsPojo wordsAndCallbackWordsPojo = this
                                                .getWordsAndCallbackWordsPojo(callbackWord, word, nextWord);
                                        wordsAndCallbackWordsPojos.add(wordsAndCallbackWordsPojo);
                                        if (callbackWords.contains(word)) {
                                            // 如果召回词中有关节发热，发热，去掉发热。
                                            callbackWords.removeIf(w -> w.equals(word));
                                        }
                                    }
                                }

                            }

                        }
                    }
                }
            } else if (index2Types.contains(type)) {
                nextMap.put(type, pojo);
            }
        }
        // return re;
    }

    private String getOneCallbackWord(String word) {
        if (StringUtils.isBlank(word)) {
            return null;
        }
        word = word.toLowerCase();
        if (allCallbackWords.contains(word) || allSymptomPhrases.contains(word)) {
            return word;
        } else if (allClassificationWords.contains(word)) {
            List<String> list = classificationWordMap.get(word);
            if (CollectionUtils.isNotEmpty(list)) {
                return list.get(0);
            }
        }
        return null;
    }

    private List<String> callbacks(String word) {
        List<String> re = new ArrayList<>();
        if (StringUtils.isEmpty(word)) {
            return re;
        }

        word = word.toLowerCase();
        if (allCallbackWords.contains(word) || allSymptomPhrases.contains(word)
                || symptomService.queryByName(word) != null) {
            re.add(word);
        }
        if (allClassificationWords.contains(word)) {
            List<String> list = classificationWordMap.get(word);
            if (CollectionUtils.isNotEmpty(list)) {
                re.addAll(list);
            }
        }
        return re;
    }

    /**
     * 是否是阴性特征
     *
     * @param word
     * @param indexMap
     * @param commaIndexList
     * @param negativeIndexList
     * @return
     */
    private boolean isNegative(Integer tmpIndex, List<Integer> commaIndexList,
            List<Integer> negativeIndexList, List<Integer> behindNegativeList) {
        // 最近的逗号位置
        Integer preCommaIndex = this.preIndex(tmpIndex, commaIndexList);
        // 最近的否定词位置
        Integer preNegativeIndex = this.preIndex(tmpIndex, negativeIndexList);
        // 最近的出现在后面的否定词
        Integer nextBehindNegativeIndex = this.nextIndex(tmpIndex, behindNegativeList);
        // 出现在后面的否定词的前一个逗号
        Integer nextBehindNegativePreCommaIndex = null;
        if (nextBehindNegativeIndex != null) {
            nextBehindNegativePreCommaIndex = this.preIndex(nextBehindNegativeIndex, commaIndexList);
        }
        // 否定词在当前词的前面并且，他们之间没有逗号
        if (tmpIndex != null && preNegativeIndex != null && preNegativeIndex < tmpIndex
                && (preCommaIndex == null
                        || (preCommaIndex < preNegativeIndex || preCommaIndex > tmpIndex))) {
            return true;
        } else if (tmpIndex != null && nextBehindNegativeIndex != null && nextBehindNegativeIndex > tmpIndex
                && (nextBehindNegativePreCommaIndex == null || (nextBehindNegativePreCommaIndex < tmpIndex
                        || nextBehindNegativePreCommaIndex > nextBehindNegativeIndex))) {
            return true;
        }
        return false;
    }

    /**
     * 是否是阴性特征
     *
     * @param word
     * @param indexMap
     * @param commaIndexList
     * @param negativeIndexList
     * @return
     */
    private boolean isNegative(Integer index1, Integer index2,

            List<Integer> commaIndexList, List<Integer> negativeIndexList, List<Integer> behindNegativeList) {
        // index1最近的逗号位置
        Integer preCommaIndex1 = this.preIndex(index1, commaIndexList);
        // index1最近的否定词位置
        Integer preNegativeIndex1 = this.preIndex(index1, negativeIndexList);

        // index2最近的否定词位置
        Integer preNegativeIndex2 = this.preIndex(index2, negativeIndexList);

        // 最近的出现在后面的否定词
        Integer nextBehindNegativeIndex = this.nextIndex(index2, behindNegativeList);
        // 出现在后面的否定词的前一个逗号
        Integer nextBehindNegativePreCommaIndex = null;
        if (nextBehindNegativeIndex != null) {
            nextBehindNegativePreCommaIndex = this.preIndex(nextBehindNegativeIndex, commaIndexList);
        }

        // 否定词在当前词的前面并且，他们之间没有逗号
        // 无腹部疼痛
        // 腹部无疼痛
        //
        // 无关节红肿，发热，
        // 关节无红肿，发热
        //
        // 无关节红肿，腹部疼痛
        // 无关节红肿、腹部疼痛
        //
        // 咳嗽，无腹部疼痛，关节红肿

        // if (index1 != null && index1 != null
        // && preNegativeIndex < tmpIndex
        // && (preCommaIndex == null || (preCommaIndex < preNegativeIndex ||
        // preCommaIndex > tmpIndex))) {
        // return true;
        // }
        if (index1 != null && index2 != null) {
            if (((preNegativeIndex1 != null && preNegativeIndex1 < index1
                    && (preCommaIndex1 == null
                            || (preCommaIndex1 < preNegativeIndex1 || preCommaIndex1 > index1))))
                    || ((preNegativeIndex2 != null && preNegativeIndex2 > index1
                            && preNegativeIndex2 < index2))) {
                // || (preNegativeIndex1 == null && (preCommaIndex1 == null ||
                // preNegativeIndex1 < preCommaIndex1) && (preNegativeIndex2 !=
                // null && preNegativeIndex2 < index2))) {
                // 宫颈无举痛，子宫前位，两侧附件未及包块，无压痛
                // 无腹部压痛，关节红肿，胀痛
                return true;
            } else if (nextBehindNegativeIndex != null && (nextBehindNegativePreCommaIndex == null
                    || nextBehindNegativePreCommaIndex < index1)) {
                return true;
            }
        }
        return false;
    }

    private Integer preIndex(Integer index, List<Integer> list) {
        if (index == null || list == null || list.size() == 0) {
            return null;
        }
        for (Integer i : list) {
            if (index > i) {
                return i;
            }
        }
        return null;
    }

    private Integer nextIndex(Integer index, List<Integer> list) {
        if (index == null || list == null || list.size() == 0) {
            return null;
        }
        for (Integer i : list) {
            if (i > index) {
                return i;
            }
        }
        return null;
    }

    private String getCallbackWordInfo(String word, Integer index,
            List<RecognizeWordsPojo> pojos, List<Integer> commaIndexList,
            List<Integer> reverseCommaIndexList) {
        if (StringUtils.isEmpty(word)) {
            return null;
        }
        if (CollectionUtils.isNotEmpty(pojos)) {
            if (CollectionUtils.isEmpty(reverseCommaIndexList)) {
                return pojos.get(0).getWord();
            } else {
                if (index != null) {
                    Integer preCommaIndex = this.preIndex(index, reverseCommaIndexList);
                    Integer nextCommaIndex = this.nextIndex(index, commaIndexList);
                    for (RecognizeWordsPojo pojo : pojos) {
                        Integer pojoIndex = pojo.getTypeIndex();
                        if (pojoIndex != null && pojoIndex < index
                                && (preCommaIndex == null || preCommaIndex < pojoIndex)) {
                            return pojo.getWord();
                        }
                        if (pojoIndex != null && pojoIndex > index
                                && (nextCommaIndex == null || nextCommaIndex > pojoIndex)) {
                            return pojo.getWord();
                        }
                    }
                }
            }

        }
        return null;
    }
    private List<String> getCallbackWordInfoByTypes(String word, Integer index,
            Map<Integer, List<RecognizeWordsPojo>> typeWordPojosMap, List<Integer> commaIndexList,
            List<Integer> reverseCommaIndexList, Integer... types) {
        if (StringUtils.isEmpty(word)) {
            return null;
        }
        List<String> re = new ArrayList<>();
        for (Integer type : types) {
            List<RecognizeWordsPojo> list = typeWordPojosMap.get(type);
            if (CollectionUtils.isNotEmpty(list)) {
                list.stream().forEach(pojo -> {
                    String result = getCallbackWordInfo(pojo.getWord(), index, typeWordPojosMap.get(type),
                            commaIndexList, reverseCommaIndexList);
                    if (StringUtils.isNotEmpty(result)) {
                        re.add(result);
                    }
                });
            }
        }
        return re;
    }

    private WordsAndCallbackWordsPojo getWordsAndCallbackWordsPojo(String callbackWord, String... words) {
        WordsAndCallbackWordsPojo pojo = new WordsAndCallbackWordsPojo();
        pojo.setCallbackWord(callbackWord);
        List<String> wordList = Arrays.asList(words);
        pojo.setWords(wordList);
        return pojo;
    }
}
