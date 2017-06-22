
import java.util.ArrayList;

public class Diagram {

    private ArrayList<String> diagramPath = new ArrayList<String>();
    private String dId;
    private boolean t_sExist=false;
    public String getdId() {
        return dId;
    }

    public boolean isT_sExist() {
        return t_sExist;
    }

    public void setT_sExist(boolean t_sExist) {
        this.t_sExist = t_sExist;
    }

    public void setdId(String dId) {
        this.dId = dId;
    }
    private int diagramLvl;
    private ArrayList<State> dStates = new ArrayList<State>();
    private ArrayList<Transition> dTransitions = new ArrayList<Transition>();

    public ArrayList<Transition> getdTransitions() {
        return dTransitions;
    }

    public ArrayList<String> getDiagramPath() {
        return diagramPath;
    }

    public void setDiagramPath(ArrayList<String> diagramPath) {
        this.diagramPath = diagramPath;
    }

    public void setdTransitions(ArrayList<Transition> dTransitions) {
        this.dTransitions = dTransitions;
    }

    public int getDiagramLvl() {
        return diagramLvl;
    }

    public void setDiagramLvl(int diagramLvl) {
        this.diagramLvl = diagramLvl;
    }

    public ArrayList<State> getdStates() {
        return dStates;
    }

    public void setdStates(ArrayList<State> dStates) {
        this.dStates = dStates;
    }

}
