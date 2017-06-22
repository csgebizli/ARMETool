
/**
 *
 * @author vestatest
 */
public class MacroState {
    private String childrenId,macroStateId, parrent;
    private State s;
    private boolean usable=true;
    private boolean hasChield;
    private int chieldIndex;
    private int posNumber;

    public int getPosNumber() {
        return posNumber;
    }

    public void setPosNumber(int posNumber) {
        this.posNumber = posNumber;
    }

    public int getChieldIndex() {
        return chieldIndex;
    }

    public void setChieldIndex(int chieldIndex) {
        this.chieldIndex = chieldIndex;
    }

    public MacroState() {
        usable=true;
        hasChield=false;
        
    }

    public boolean usable() {
        return usable;
    }

    public void setUsable(boolean used) {
        this.usable = used;
    }

    public boolean isHasChield() {
        return hasChield;
    }

    public void setHasChield(boolean hasChield) {
        this.hasChield = hasChield;
    }

    public MacroState(State s) {
        this.s = s;
        
    }

    public State getS() {
        return s;
    }

    public void setS(State s) {
        this.s = s;
    }

    public String getChildrenId() {
        return childrenId;
    }

    public void setChildrenId(String childrenId) {
        this.childrenId = childrenId;
    }

    public String getMacroStateId() {
        return macroStateId;
    }

    public void setMacroStateId(String macroStateId) {
        this.macroStateId = macroStateId;
    }

    public String getParrent() {
        return parrent;
    }

    public void setParrent(String parrent) {
        this.parrent = parrent;
    }
    
}
