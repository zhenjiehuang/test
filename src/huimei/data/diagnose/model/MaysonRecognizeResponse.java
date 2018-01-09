package huimei.data.diagnose.model;

import java.util.List;

/**
 * @author lipeng
 * @date 2017/7/7
 */
public class MaysonRecognizeResponse {
    // 识别结果
    List<MaysonRecognizeResultPojo> recognizeResult;
    // 新版识别
    List<RecognizeResultPojo> recognizeResultList;
    //当前诊断的 诊断识别结果
    List<DiseaseAndAlias> recognizeDiagnosisResults;
    // 评估表相关项目结果
    List<ExamRecognizePojo> illnessAssessItemResults;

    public boolean concains(MaysonRecognizeResponse target) {
        if (recognizeResultList.isEmpty() && target.getRecognizeResultList().isEmpty()
                && recognizeDiagnosisResults.isEmpty() && target.getRecognizeDiagnosisResults().isEmpty()) {
            return true;
        }
        if (!recognizeResultList.isEmpty()) {
            // if()
        }

        return recognizeResultList.containsAll(target.getRecognizeResultList())
                && recognizeDiagnosisResults.containsAll(target.getRecognizeDiagnosisResults());
    }

    public List<MaysonRecognizeResultPojo> getRecognizeResult() {
        return recognizeResult;
    }

    public void setRecognizeResult(List<MaysonRecognizeResultPojo> recognizeResult) {
        this.recognizeResult = recognizeResult;
    }

    public List<RecognizeResultPojo> getRecognizeResultList() {
        return recognizeResultList;
    }

    public void setRecognizeResultList(List<RecognizeResultPojo> recognizeResultList) {
        this.recognizeResultList = recognizeResultList;
    }

    public List<DiseaseAndAlias> getRecognizeDiagnosisResults() {
        return recognizeDiagnosisResults;
    }

    public void setRecognizeDiagnosisResults(List<DiseaseAndAlias> recognizeDiagnosisResults) {
        this.recognizeDiagnosisResults = recognizeDiagnosisResults;
    }

    public List<ExamRecognizePojo> getIllnessAssessItemResults() {
        return illnessAssessItemResults;
    }

    public void setIllnessAssessItemResults(List<ExamRecognizePojo> illnessAssessItemResults) {
        this.illnessAssessItemResults = illnessAssessItemResults;
    }
}
