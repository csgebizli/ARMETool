
import java.util.ArrayList;

public class Transition {

    private String name;
    private String tId;
    private String sourceState, targetState;
    private String value;
    private ArrayList<String> transitionPath = new ArrayList<String>();
    private String orgSource, orgTarget;
    private String orgSoId, orgTaId;
    private boolean type;

    public String getOrgSoId() {
        return orgSoId;
    }

    public void setOrgSoId(String orgSoId) {
        this.orgSoId = orgSoId;
    }

    public String getOrgTaId() {
        return orgTaId;
    }

    public void setOrgTaId(String orgTaId) {
        this.orgTaId = orgTaId;
    }

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public String gettId() {
        return tId;
    }

    public void settId(String tId) {
        this.tId = tId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ArrayList<String> getTransitionPath() {
        return transitionPath;
    }

    public void setTransitionPath(ArrayList<String> transitionPath) {
        this.transitionPath = transitionPath;
    }

    public String getOrgSource() {
        return orgSource;
    }

    public void setOrgSource(String orgSource) {
        this.orgSource = orgSource;
    }

    public String getOrgTarget() {
        return orgTarget;
    }

    public void setOrgTarget(String orgTarget) {
        this.orgTarget = orgTarget;
    }

    public Transition(String name, String orgTarget, String orgSource) {
        super();
        this.name = name;
        this.orgSource = orgSource;
        this.orgTarget = orgTarget;
        this.type = true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSourceState() {
        return sourceState;
    }

    public void setSourceState(String sourceState) {
        this.sourceState = sourceState;
    }

    public String getTargetState() {
        return targetState;
    }

    public void setTargetState(String targetState) {
        this.targetState = targetState;
    }

}
