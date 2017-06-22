
import java.util.ArrayList;

public class State {

    private String name;

    public String getMacroStateDestination() {
        return macroStateDestination;
    }

    public void setMacroStateDestination(String macroStateDestination) {
        this.macroStateDestination = macroStateDestination;
    }
    private String sId;
    private String xPosition, yPosition;
    private String incomingTransitions, outgoingTransitions;
    private String macroStateDestination;
    private int outCount;
    private ArrayList<String> statePath = new ArrayList<String>();
    private int stateLevel;
    private double outgoingTransitionValues;
    private double firstTransitionValues;
    private boolean macroState;
    private String diagramId;

    public String getDiagramId() {
        return diagramId;
    }

    public void setDiagramId(String diagramId) {
        this.diagramId = diagramId;
    }

    public double getFirstTransitionValues() {
        return firstTransitionValues;
    }

    public String getsId() {
        return sId;
    }

    public void setsId(String sId) {
        this.sId = sId;
    }

    public void setFirstTransitionValues(double firstTransitionValues) {
        this.firstTransitionValues = firstTransitionValues;
    }

    public ArrayList<String> getStatePath() {
        return statePath;
    }

    public double getOutgoingTransitionValues() {
        return outgoingTransitionValues;
    }

    public void setOutgoingTransitionValues(double outgoingTransitionValues) {
        this.outgoingTransitionValues = outgoingTransitionValues;
    }

    public void setStatePath(ArrayList<String> statePath) {
        this.statePath = statePath;
    }

    public int getStateLevel() {
        return stateLevel;
    }

    public void setStateLevel(int stateLevel) {
        this.stateLevel = stateLevel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getxPosition() {
        return xPosition;
    }

    public void setxPosition(String xPosition) {
        this.xPosition = xPosition;
    }

    public String getyPosition() {
        return yPosition;
    }

    public void setyPosition(String yPosition) {
        this.yPosition = yPosition;
    }

    public String getIncomingTransitions() {
        return incomingTransitions;
    }

    public void setIncomingTransitions(String incomingTransitions) {
        this.incomingTransitions = incomingTransitions;
    }

    public String getOutgoingTransitions() {
        return outgoingTransitions;
    }

    public void setOutgoingTransitions(String outgoingTransitions) {
        this.outgoingTransitions = outgoingTransitions;
    }

    public int getOutCount() {
        return outCount;
    }

    public void setOutCount(int outCount) {
        this.outCount = outCount;
    }
    /*
     public static String getStartPositionX() {
     return startPositionX;
     }

     public static void setStartPositionX(String startPositionX) {
     State.startPositionX = startPositionX;
     }

     public static String getStartPositionY() {
     return startPositionY;
     }

     public static void setStartPositionY(String startPositionY) {
     State.startPositionY = startPositionY;
     }

     public static int getPositionRatio() {
     return POSITION_RATIO;
     }

     /*private static String startPositionX = "240";
     private static String startPositionY = "80";
     private static final int POSITION_RATIO = 50;

     public static int getPOSITION_RATIO() {
     return POSITION_RATIO;
     }*/

    State(String _name) {
        name = _name;
        statePath = new ArrayList<String>();
        incomingTransitions = "";
        outgoingTransitions = "";
//		xPosition = startPositionX;
//		yPosition = startPositionY;
//		startPositionX = Integer.toString(Integer.parseInt(startPositionX)
//				+ POSITION_RATIO);
//		startPositionY = Integer.toString(Integer.parseInt(startPositionY)
//				+ POSITION_RATIO);
        outCount = 0;
        macroState=false;
    }

    public boolean isMacroState() {
        return macroState;
    }

    public void setMacroState(boolean macroState) {
        this.macroState = macroState;
    }

    public void addIncoming(String _incoming) {
        incomingTransitions = incomingTransitions.isEmpty() ? _incoming
                : incomingTransitions + " " + _incoming;
    }

    public void addOutgoing(String _outgoing) {
        outgoingTransitions = outgoingTransitions.isEmpty() ? _outgoing
                : outgoingTransitions + " " + _outgoing;
        outCount++;
    }


}
