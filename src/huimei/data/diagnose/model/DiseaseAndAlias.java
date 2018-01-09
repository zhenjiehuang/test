package huimei.data.diagnose.model;

import java.util.List;

/**
 * @author lipeng
 * @date 2017/9/18
 */
public class DiseaseAndAlias {
    String diseaseName;
    List<String> alias;

    public String getDiseaseName() {
        return diseaseName;
    }

    public void setDiseaseName(String diseaseName) {
        this.diseaseName = diseaseName;
    }

    public List<String> getAlias() {
        return alias;
    }

    public void setAlias(List<String> alias) {
        this.alias = alias;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DiseaseAndAlias) {
            DiseaseAndAlias o = (DiseaseAndAlias) obj;
            return diseaseName.equals(o.getDiseaseName()) && alias.containsAll(o.getAlias());
        }
        return super.equals(obj);
    }
}
