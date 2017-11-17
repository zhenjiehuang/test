package huimei.report;

import java.util.HashSet;
import java.util.Set;

public class Report {

    private String opr;

    private Set<String> hospital = new HashSet<String>();

    private Set<String> doctor = new HashSet<String>();

    private Set<String> patient = new HashSet<String>();

    private Set<String> cases = new HashSet<String>();

    public String getOpr() {
        return opr;
    }

    public void setOpr(String opr) {
        this.opr = opr;
    }

    public Set<String> getHospital() {
        return hospital;
    }

    public void setHospital(Set<String> hospital) {
        this.hospital = hospital;
    }

    public Set<String> getDoctor() {
        return doctor;
    }

    public void setDoctor(Set<String> doctor) {
        this.doctor = doctor;
    }

    public Set<String> getPatient() {
        return patient;
    }

    public void setPatient(Set<String> patient) {
        this.patient = patient;
    }

    public Set<String> getCases() {
        return cases;
    }

    public void setCases(Set<String> cases) {
        this.cases = cases;
    }

}
