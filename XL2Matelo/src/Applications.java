import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Applications {

	private String pname, pId, rootChain, instancechain;
	String instChanUid = "_chainreaction";
	int instChanUidCnt = 0;

	public FileApplications getF() {
		return f;
	}

	private static int idSeed = 100000;

	public ArrayList<Transition> getTransitionList() {
		return transitionList;
	}

	private ArrayList<MacroState> macroList = new ArrayList<MacroState>();
	private ArrayList<Transition> exordenaryTransitions = new ArrayList<Transition>();

	private FileApplications f = new FileApplications();
	private ArrayList<Diagram> diagrams = new ArrayList<Diagram>();

	private ArrayList<Transition> transitionList = new ArrayList<Transition>();

	private ArrayList<State> stateList = new ArrayList<State>();

	private ArrayList<ArrayList<String>> pathAL = new ArrayList<ArrayList<String>>();
	private ArrayList<String> generalPathLists = new ArrayList<String>();
	private static int startPositionX = 240;
	private static int startPositionY = 80;
	private static final int POSITION_RATIO = 50;

	public ArrayList<Diagram> getDiagrams() {
		return diagrams;
	}

	public void setDiagrams(ArrayList<Diagram> diagrams) {
		this.diagrams = diagrams;
	}

	/**
	 * Excel dosyasindan state isimlerini alir
	 */
	public void getAllStates() {
		for (int rows = 1; rows < f.getSheet().getRows(); rows++) {
			if (stateList.isEmpty()) {
				stateList.add(new State(f.getSheet().getCell(0, rows).getContents()));

			} else {

				if (!f.getSheet().getCell(0, rows).toString().equals("")) {
					stateList.add(new State(f.getSheet().getCell(0, rows).getContents()));

				}
			}
		}
		for(State state : stateList){
			System.out.println(state.getName());
		}
	}

	/**
	 * State isimlerinden pathleri uretir
	 */
	public void setStateDetails() {
		ArrayList<Integer> n = new ArrayList<Integer>();

		for (int i = 0; i < stateList.size(); i++) {
			String c = stateList.get(i).getName();
			// c.substring(0, c.lastIndexOf("-")+1);
			for (int j = 0; j < c.length(); j++) {
				if (c.charAt(j) == '-') {
					n.add(j);
				}
			}
			if (n.size() != 0) {
				stateList.get(i).getStatePath().add(c.substring(0, n.get(0)));
				if (n.size() >= 1) {
					for (int k = 1; k < n.size(); k++) {
						stateList.get(i).getStatePath().add(c.substring(n.get(k - 1) + 1, (n.get(k))));
					}
				}
				stateList.get(i).setName(c.substring(n.get(n.size() - 1) + 1, c.length()));
				stateList.get(i).setStateLevel(n.size());
			} else {
				stateList.get(i).getStatePath().add("Root");
				stateList.get(i).setStateLevel(0);
			}
			n.clear();

		}
	}

	// compare string arrays
	public boolean aLEq(ArrayList<String> a, ArrayList<String> b) {
		int cnt = 0;
		if (a.size() == b.size()) {
			for (int i = 0; i < a.size(); i++) {
				if (a.get(i).equalsIgnoreCase(b.get(i))) {
					cnt++;
				}
			}
		}
		if (cnt == a.size()) {
			return true;

		} else {
			return false;
		}
	}

	public void generatePathList() {

		// generalPathLists.add("Root");
		String temp = "";
		for (int i = 0; i < stateList.size(); i++) {
			if (!stateList.get(i).getStatePath().isEmpty()) {
				for (int j = 0; j < stateList.get(i).getStatePath().size(); j++) {
					temp = temp.concat(stateList.get(i).getStatePath().get(j) + "-");

				}
				temp = temp.substring(0, temp.lastIndexOf("-"));
				if (!generalPathLists.contains(temp)) {
					generalPathLists.add(temp);

				}
				temp = "";
			} else {
				generalPathLists.add("Root");
			}
		}
	}

	public boolean alComp(ArrayList<String> a, ArrayList<String> b) {
		if (a.size() != b.size()) {
			return false;
		} else {
			int cond = 0;
			for (int i = 0; i < a.size(); i++) {
				if (a.get(i).equalsIgnoreCase(b.get(i))) {
					cond++;
				}
			}
			if (cond == a.size()) {
				return true;
			}
		}
		return false;
	}

	public void separateStates() {
		// Adds Root States
		Diagram d = new Diagram();
		d.setDiagramLvl(0);
		d.getDiagramPath().add("Root");
		for (int i = 0; i < stateList.size(); i++) {
			if (stateList.get(i).getStatePath().get(0).equalsIgnoreCase("Root")) {
				d.getdStates().add(stateList.get(i));
				stateList.remove(i);
				i--;
				if (stateList.size() == 0) {
					break;
				}
			}
		}
		diagrams.add(d);
		if (!stateList.isEmpty()) {
			Diagram dfirst = new Diagram();
			dfirst.setDiagramLvl(1);
			dfirst.getDiagramPath().add(stateList.get(0).getStatePath().get(0));
			dfirst.getdStates().add(stateList.get(0));
			diagrams.add(dfirst);
			stateList.remove(0);
		}
		int[] assign = new int[2];
		assign[0] = 0;
		assign[1] = 0;
		int comptLvl2 = 0;
		for (int i = 0; i < stateList.size(); i++) {
			for (int j = 0; j < diagrams.size(); j++) {
				if (!alComp(stateList.get(i).getStatePath(), diagrams.get(j).getDiagramPath())) {
					comptLvl2++;
				} else {
					assign[0] = i;
					assign[1] = j;
				}
			}
			if (comptLvl2 == diagrams.size()) {
				Diagram dnew = new Diagram();
				dnew.setDiagramLvl(stateList.get(i).getStateLevel());
				for (int j = 0; j < stateList.get(i).getStatePath().size(); j++) {
					dnew.getDiagramPath().add(stateList.get(i).getStatePath().get(j));
				}
				dnew.getdStates().add(stateList.get(i));
				diagrams.add(dnew);
				stateList.remove(i);
				i--;
				if (stateList.size() == 0) {
					break;
				}
			} else {
				diagrams.get(assign[1]).getdStates().add(stateList.get(assign[0]));
				stateList.remove(i);
				i--;
			}

			assign[0] = 0;
			assign[1] = 0;
			comptLvl2 = 0;
		}
	}

	public void addTerminateState() {
		for (int i = 0; i < diagrams.size(); i++) {
			State s = new State("Terminate");
			for (int j = 0; j < diagrams.get(i).getDiagramPath().size(); j++) {
				s.getStatePath().add(diagrams.get(i).getDiagramPath().get(j));
			}
			diagrams.get(i).getdStates().add(s);
		}
	}

	public void getAllTransitions() {
		for (int i = 1; i < f.getSheet().getColumns(); i++) {
			for (int j = 1; j < f.getSheet().getColumns(); j++) {
				if (!f.getSheet().getCell(i, j).getContents().isEmpty()) {
					// transitionList.add(new Transition(f.getSheet().getCell(i,
					// j).getContents(), f.getSheet().getCell(i, 0)
					// .getContents(), f.getSheet().getCell(0,
					// j).getContents()));
					transitionList.add(new Transition(f.getSheet().getCell(i, j).getContents(), f.getSheet().getCell(i, 0)
							.getContents(), f.getSheet().getCell(0, j).getContents()));
				}
			}
		}
	}

	/*
	 * public void setTransitionDetails() { ArrayList<Integer> n = new
	 * ArrayList<Integer>(); for (int i = 0; i < transitionList.size(); i++) {
	 * // if(transitionList.get(i).) } }
	 */
	/**
	 * pre ve post dominator treelerin olup olmadigini kontrol eder eger varsa
	 * baglantilarini kurar, yolsa yeni stateleri olusturup gerekli baglantilari
	 * ayarlar
	 */
	public void processTransitions() {
		String iId = null, sId = null, fId = null, tId = null;
		int exordinary;
		for (int i = 0; i < diagrams.size(); i++) {
			exordinary = 0;
			boolean sExist = false;
			boolean fExist = false;
			// her bir transition için duzgun veya duzensiz olma durumuna gore
			// ayirir
			for (int j = 0; j < diagrams.get(i).getdTransitions().size(); j++) {
				if (!diagrams.get(i).getdTransitions().get(j).isType()) {
					exordinary++;
					break;
				}
			}
			// duzensiz transitionlarda dosyada tanimli pre / post dominator
			// belirleyici statelerin varligini kontrol eder
			if (exordinary > 0) {
				for (int j = 0; j < diagrams.get(i).getdTransitions().size(); j++) {
					if (diagrams.get(i).getdTransitions().get(j).getSourceState().equalsIgnoreCase("S")
							&& diagrams.get(i).getdTransitions().get(j).getTargetState().equalsIgnoreCase("S")) {
						sExist = true;
					} else if (diagrams.get(i).getdTransitions().get(j).getSourceState().equalsIgnoreCase("F")
							&& diagrams.get(i).getdTransitions().get(j).getTargetState().equalsIgnoreCase("F")) {
						fExist = true;
					}
				}
				// eger pre/post dominator tree yok ise state içindeki kosullara
				// gore bunu revize eder,
				// ayni sekilde bu statelere bagli transitionlari da yeni haline
				// uyarlar.
				if (!sExist && !fExist) {
					for (int j = 0; j < diagrams.get(i).getdStates().size(); j++) {
						if (diagrams.get(i).getdStates().get(j).getName().equalsIgnoreCase("invoke")) {
							iId = diagrams.get(i).getdStates().get(j).getName();
						} else if (diagrams.get(i).getdStates().get(j).getName().equalsIgnoreCase("S")) {
							sId = diagrams.get(i).getdStates().get(j).getName();

						} else if (diagrams.get(i).getdStates().get(j).getName().equalsIgnoreCase("F")) {
							fId = diagrams.get(i).getdStates().get(j).getName();

						} else if (diagrams.get(i).getdStates().get(j).getName().equalsIgnoreCase("Terminate")) {
							tId = diagrams.get(i).getdStates().get(j).getName();
						}
					}
					for (int j = 0; j < diagrams.get(i).getdTransitions().size(); j++) {
						if (diagrams.get(i).getdTransitions().get(j).getSourceState().equalsIgnoreCase("invoke")) {
							diagrams.get(i).getdTransitions().get(j).setOrgSoId(sId);
							diagrams.get(i).getdTransitions().get(j).setOrgSource("S");
							diagrams.get(i).getdTransitions().get(j).setSourceState("S");
						} else if (diagrams.get(i).getdTransitions().get(j).getTargetState().equalsIgnoreCase("terminate")) {
							diagrams.get(i).getdTransitions().get(j).setOrgTaId(fId);
							diagrams.get(i).getdTransitions().get(j).setOrgTarget("F");
							diagrams.get(i).getdTransitions().get(j).setTargetState("F");

						}
					}
					Transition t = new Transition("x", "invoke", "S");
					t.setSourceState("invoke");
					t.setTargetState("S");
					t.setType(true);
					t.setOrgSoId(iId);
					t.setOrgTaId(sId);
					for (int j = 0; j < diagrams.get(i).getdTransitions().get(0).getTransitionPath().size(); j++) {
						t.getTransitionPath().add(diagrams.get(i).getdTransitions().get(0).getTransitionPath().get(j));
					}
					t.settId("_d0t" + String.valueOf(100000 + diagrams.get(i).getdTransitions().size()));
					diagrams.get(i).getdTransitions().add(0, t);
					Transition t1 = new Transition("x", "F", "Terminate");
					t1.setSourceState("F");
					t1.setTargetState("Terminate");
					t1.setType(true);
					t1.setOrgSoId(fId);
					t1.setOrgTaId(tId);
					for (int j = 0; j < diagrams.get(i).getdTransitions().get(0).getTransitionPath().size(); j++) {
						t1.getTransitionPath().add(diagrams.get(i).getdTransitions().get(0).getTransitionPath().get(j));
					}
					t1.settId("_d0t" + String.valueOf(100000 + diagrams.get(i).getdTransitions().size()));
					diagrams.get(i).getdTransitions().add(t1);
				}
			}
			// eger düzensiz bir transition varsa dogrulama amacli post
			// dominator stateden pre dominator state e yeni bir transition
			// ekler
			if (exordinary > 0) {
				Transition tx = new Transition("x", "F", "S");
				tx.setSourceState("F");
				tx.setTargetState("S");
				tx.setType(true);
				tx.setOrgSoId(fId);
				tx.setOrgTaId(sId);
				for (int j = 0; j < diagrams.get(i).getdTransitions().get(0).getTransitionPath().size(); j++) {
					tx.getTransitionPath().add(diagrams.get(i).getdTransitions().get(0).getTransitionPath().get(j));
				}
				tx.settId("_d0t" + String.valueOf(100000 + diagrams.get(i).getdTransitions().size()));
				diagrams.get(i).getdTransitions().add(tx);
			}
		}
		for (int j = 0; j < diagrams.size(); j++) {
			for (int j2 = 0; j2 < diagrams.get(j).getdTransitions().size(); j2++) {
				if ((diagrams.get(j).getdTransitions().get(j2).getSourceState().equalsIgnoreCase("S")
						&&diagrams.get(j).getdTransitions().get(j2).getTargetState().equalsIgnoreCase("S"))
						|| (diagrams.get(j).getdTransitions().get(j2).getSourceState().equalsIgnoreCase("F")
						&&diagrams.get(j).getdTransitions().get(j2).getTargetState().equalsIgnoreCase("F"))) {
					if (diagrams.get(j).getdTransitions().get(j2).getSourceState()
							.equalsIgnoreCase(diagrams.get(j).getdTransitions().get(j2).getTargetState())) {
						diagrams.get(j).getdTransitions().remove(j2);
					}
				}
			}
		}
	}

	/**
	 * 
	 * @param t
	 *            Transition türünde ArrayList alir
	 * 
	 */
	public void tAnalyze(ArrayList<Transition> t) {
		ArrayList<String> source = new ArrayList<String>();
		ArrayList<String> target = new ArrayList<String>();
		ArrayList<Integer> pointsS = new ArrayList<Integer>();
		ArrayList<Integer> pointsT = new ArrayList<Integer>();

		for (int i = 0; i < t.size(); i++) {
			String a = t.get(i).getOrgSource();
			String b = t.get(i).getOrgTarget();
			// source için path uzunlugu cikarir
			for (int j = 0; j < a.length(); j++) {
				if (a.charAt(j) == '-') {
					pointsS.add(j);
				}
			}
			// target için path uzunlugu
			for (int j = 0; j < b.length(); j++) {
				if (b.charAt(j) == '-') {
					pointsT.add(j);
				}
			}
			// source pathini alir
			if (pointsS.size() != 0) {
				source.add(a.substring(0, pointsS.get(0)));
				if (pointsS.size() >= 1) {
					for (int j = 1; j < pointsS.size(); j++) {
						source.add(a.substring(pointsS.get(j - 1) + 1, pointsS.get(j)));
					}
				}
			} else {
				source.add("Root");
			}
			// target pathini alir
			if (pointsT.size() != 0) {
				target.add(b.substring(0, pointsT.get(0)));
				if (pointsT.size() >= 1) {
					for (int j = 1; j < pointsT.size(); j++) {
						target.add(b.substring(pointsT.get(j - 1) + 1, pointsT.get(j)));
					}
				}
			} else {
				target.add("Root");
			}
			// pathleri karsilastirinca kisa path kadar esitlik varsa veya path
			// uzunluklari esitse duzgun bir transitiondur
			int comp = 0;
			if (source.size() == target.size()) {
				comp = source.size();
			} else if (source.size() > target.size()) {
				comp = target.size();
				t.get(i).setType(false);
			} else if (source.size() < target.size()) {
				comp = source.size();
				t.get(i).setType(false);
			}
			int breakpoint = 0;
			// pathlerin biribirinden farkli olan kirilma noktasini cikarir ve
			// ortak path cikarmak için gerekirse ust modellere geciþ yapar
			for (int j = 0; j < comp; j++) {

				if (source.get(j).equalsIgnoreCase(target.get(j))) {
					t.get(i).getTransitionPath().add(source.get(j));

				} else {

					t.get(i).setSourceState(source.get(j));
					t.get(i).setTargetState(target.get(j));
					t.get(i).setType(false);
					if (j != 0) {
						breakpoint = t.get(i).getTransitionPath().size();
					}
					if (j == 0) {
						t.get(i).getTransitionPath().add("Root");
					}
					break;
				}
			}
			// transitionun type ini kontrol ederek duzenli veya duzensiz bir
			// transition olma durumuna gore transition ýn bilgilerini düzenler
			if (t.get(i).isType()) {
				t.get(i).setSourceState(a.substring(a.lastIndexOf('-') + 1, a.length()));
				t.get(i).setTargetState(b.substring(b.lastIndexOf('-') + 1, b.length()));
			}

			if (t.get(i).getSourceState() == null && t.get(i).getTargetState() == null && source.size() != target.size()
					&& !t.get(i).isType()) {
				if (source.size() < target.size()) {
					t.get(i).setSourceState(a.substring(pointsS.get(breakpoint) + 1, a.length()));
					t.get(i).setTargetState(b.substring(pointsT.get(breakpoint) + 1, pointsT.get(breakpoint + 1)));
				} else {
					t.get(i).setSourceState(a.substring(pointsS.get(breakpoint) + 1, pointsS.get(breakpoint + 1)));
					t.get(i).setTargetState(b.substring(pointsT.get(breakpoint) + 1, b.length()));

				}

			}

			source.clear();
			target.clear();
			pointsS.clear();
			pointsT.clear();
		}

	}

	/**
	 * transitionlari ilgili modele ekler
	 */
	public void mergeTransDiag() {
		for (int i = 0; i < diagrams.size(); i++) {
			for (int j = 0; j < transitionList.size(); j++) {
				if (aLEq(diagrams.get(i).getDiagramPath(), transitionList.get(j).getTransitionPath())) {
					diagrams.get(i).getdTransitions().add(transitionList.get(j));
					transitionList.remove(j);
					j--;
				}
			}
		}
		if (transitionList.size() != 0) {
			System.out.println("------------------------------------------------");
			for (int i = 0; i < transitionList.size(); i++) {
				System.out.println(transitionList.get(i).getOrgSource().toString() + " "
						+ transitionList.get(i).getOrgTarget().toString() + " cannot be setted ");
			}
			System.out.println("------------------------------------------------");
		}
	}

	/**
	 * her model için, içerisinde bulunan stateler ve statelerden cikan
	 * transitionlarýn sayisini hesaplar
	 */
	public void calculateOutgoings() {
		for (int i = 0; i < diagrams.size(); i++) {
			for (int j = 0; j < diagrams.get(i).getdStates().size(); j++) {
				for (int k = 0; k < diagrams.get(i).getdTransitions().size(); k++) {
					if (diagrams.get(i).getdTransitions().get(k).getOrgSoId()
							.equalsIgnoreCase(diagrams.get(i).getdStates().get(j).getsId())) {
						diagrams.get(i).getdStates().get(j).setOutCount(diagrams.get(i).getdStates().get(j).getOutCount() + 1);
						if (diagrams.get(i).getdStates().get(j).getOutgoingTransitions().isEmpty()) {
							diagrams.get(i).getdStates().get(j)
									.setOutgoingTransitions(diagrams.get(i).getdTransitions().get(k).gettId());
						} else {
							diagrams.get(i)
									.getdStates()
									.get(j)
									.setOutgoingTransitions(
											diagrams.get(i).getdStates().get(j).getOutgoingTransitions() + " "
													+ diagrams.get(i).getdTransitions().get(k).gettId());
						}
					}
					if (diagrams.get(i).getdTransitions().get(k).getOrgTaId()
							.equalsIgnoreCase(diagrams.get(i).getdStates().get(j).getsId())) {
						if (diagrams.get(i).getdStates().get(j).getIncomingTransitions().isEmpty()) {
							diagrams.get(i).getdStates().get(j)
									.setIncomingTransitions(diagrams.get(i).getdTransitions().get(k).gettId());
						} else {
							diagrams.get(i)
									.getdStates()
									.get(j)
									.setIncomingTransitions(
											diagrams.get(i).getdStates().get(j).getIncomingTransitions() + " "
													+ diagrams.get(i).getdTransitions().get(k).gettId());
						}
					}
				}
			}
		}
	}

	/**
	 * cizim sirasi için statelerin ekran konumlarini hesaplar
	 */
	public void recalculateStatePositions() {
		for (int i = 0; i < diagrams.size(); i++) {
			for (int j = 0; j < diagrams.get(i).getdStates().size(); j++) {
				diagrams.get(i).getdStates().get(j).setxPosition(String.valueOf(startPositionX + (j * POSITION_RATIO)));
				diagrams.get(i).getdStates().get(j).setyPosition(String.valueOf(startPositionY + (j * POSITION_RATIO)));
			}
		}
	}

	/**
	 * statlerden cikan transitionlarin gerceklesme yüzdelerini aritmetik
	 * ortalama olarak hesaplar
	 */
	public void calculateOutgoingValues() {
		NumberFormat formatter = new DecimalFormat("#,###");
		for (int i = 0; i < diagrams.size(); i++) {
			for (int j = 0; j < diagrams.get(i).getdStates().size(); j++) {
				if (!diagrams.get(i).getdStates().get(j).getName().equalsIgnoreCase("terminate")) {
					if (diagrams.get(i).getdStates().get(j).getOutCount() == 1) {
						diagrams.get(i).getdStates().get(j).setFirstTransitionValues(Double.valueOf("1.000"));
					} else {
						int n = diagrams.get(i).getdStates().get(j).getOutCount();

						double d = 1.000;
						d = Double.valueOf(formatter.format(d / 1));
						diagrams.get(i).getdStates().get(j).setOutgoingTransitionValues(d);// Double.valueOf(formatter.format(d)));
						diagrams.get(i).getdStates().get(j)
								.setFirstTransitionValues(Float.valueOf(formatter.format(1 - (n - 1) * d)));
					}
				}
			}
		}
	}

	/**
	 * hesaplanmis olan transitionlarin gerceklesme yüzdelerini iþler
	 */
	public void assignValues() {
		for (int i = 0; i < diagrams.size(); i++) {
			for (int j = 0; j < diagrams.get(i).getdStates().size(); j++) {
				for (int k = 0; k < diagrams.get(i).getdTransitions().size(); k++) {
					if (diagrams.get(i).getdStates().get(j).getName()
							.equalsIgnoreCase(diagrams.get(i).getdTransitions().get(k).getSourceState())) {
						if (diagrams.get(i).getdStates().get(j).getOutCount() == 1) {
							diagrams.get(i).getdTransitions().get(k)
									.setValue(String.valueOf(diagrams.get(i).getdStates().get(j).getFirstTransitionValues()));
						} else if (diagrams.get(i).getdStates().get(j).getOutCount() > 1) {
							diagrams.get(i).getdTransitions().get(k)
									.setValue(String.valueOf(diagrams.get(i).getdStates().get(j).getOutgoingTransitionValues()));
							diagrams.get(i).getdStates().get(j)
									.setOutCount(diagrams.get(i).getdStates().get(j).getOutCount() - 1);
						}
					}
				}
			}
		}
	}

	/**
	 * pre / post dominator state gerekli iste bunlari ekler
	 */
	public void assignDominatorTrees() {
		int dPre = 0;
		int dPost = 0;
		boolean exord;
		for (int i = 0; i < diagrams.size(); i++) {
			exord = true;
			for (int j = 0; j < diagrams.get(i).getdTransitions().size(); j++) {
				if (!diagrams.get(i).getdTransitions().get(j).isType()) {
					exord = false;
				}
			}
			if (!exord) {
				for (int j = 0; j < diagrams.get(i).getdStates().size(); j++) {
					if (diagrams.get(i).getdStates().get(j).getName().equalsIgnoreCase("S")) {
						dPre++;
					} else if (diagrams.get(i).getdStates().get(j).getName().equalsIgnoreCase("F")) {
						dPost++;
					}
				}
				if (dPre == 0 && dPost == 0) {
					State s = new State("S");
					s.setsId("_d" + i + "s" + (100000 + diagrams.get(i).getdStates().size()));
					s.setStateLevel(diagrams.get(i).getdStates().get(0).getStateLevel());
					for (int j = 0; j < diagrams.get(i).getdStates().get(0).getStatePath().size(); j++) {
						s.getStatePath().add(diagrams.get(i).getdStates().get(0).getStatePath().get(j));

					}
					State f = new State("F");
					f.setsId("_d" + i + "s" + (100000 + diagrams.get(i).getdStates().size() + 1));
					f.setStateLevel(diagrams.get(i).getdStates().get(0).getStateLevel());
					for (int j = 0; j < diagrams.get(i).getdStates().get(0).getStatePath().size(); j++) {
						f.getStatePath().add(diagrams.get(i).getdStates().get(0).getStatePath().get(j));

					}
					diagrams.get(i).getdStates().add(0, s);
					diagrams.get(i).getdStates().add(diagrams.get(i).getdStates().size() - 1, f);
				}
			}
		}
	}

	public void generateOrgIds() {
		for (int i = 0; i < diagrams.size(); i++) {
			for (int j = 0; j < diagrams.get(i).getdTransitions().size(); j++) {
				int cnt = 0;
				for (int k = 0; k < diagrams.get(i).getdStates().size(); k++) {
					if (cnt >= 2) {
						break;
					}
					if (diagrams.get(i).getdStates().get(k).getName()
							.equalsIgnoreCase(diagrams.get(i).getdTransitions().get(j).getSourceState())) {
						diagrams.get(i).getdTransitions().get(j).setOrgSoId(diagrams.get(i).getdStates().get(k).getsId());
						cnt++;
					}
					if (diagrams.get(i).getdStates().get(k).getName()
							.equalsIgnoreCase(diagrams.get(i).getdTransitions().get(j).getTargetState())) {
						diagrams.get(i).getdTransitions().get(j).setOrgTaId(diagrams.get(i).getdStates().get(k).getsId());
						cnt++;
					}
				}
			}
		}
	}

	public void createDiagramFiles(String a) {
		String path = "";
		for (int i = 0; i < diagrams.size(); i++) {
			path = diagrams.get(i).getDiagramPath().get(0);

			for (int j = 1; j < diagrams.get(i).getDiagramPath().size(); j++) {
				path = path + "-" + diagrams.get(i).getDiagramPath().get(j).toString();
			}
			File newf = new File(a + "\\model\\chain\\" + path + ".mcm");
			try {
				newf.createNewFile();

			} catch (IOException ex) {
				Logger.getLogger(Applications.class.getName()).log(Level.SEVERE, null, ex);
			}
			path = "";
		}
	}

	public void output() {
		String xx;
		for (int i = 0; i < diagrams.size(); i++) {
			PrintWriter writer = null;
			try {
				writer = new PrintWriter(new File(f.getProjectPath() + "\\model\\chain\\" + generalPathLists.get(i) + ".mcm"));

			} catch (FileNotFoundException ex) {
				Logger.getLogger(Applications.class.getName()).log(Level.SEVERE, null, ex);
			}

			for (int j = 0; j < diagrams.get(i).getdStates().size() - 1; j++) {

				writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
				writer.println("<diagram:Diagram xmlns:diagram=\"http://all4tec.net/matelo/diagram/v5.4.1\">");

				writer.println("  <invokeState uUID=\"" + diagrams.get(i).getdStates().get(0).getsId()
						+ "\" name=\"invoke\" xPosition=\"" + diagrams.get(i).getdStates().get(0).getxPosition()
						+ "\" yPosition=\"" + diagrams.get(i).getdStates().get(0).getyPosition() + "\" outgoingTransitions=\""
						+ diagrams.get(i).getdStates().get(0).getOutgoingTransitions() + "\"/>");
				writer.println("  <terminateState uUID=\""
						+ diagrams.get(i).getdStates().get(diagrams.get(i).getdStates().size() - 1).getsId()
						+ "\" name=\"terminate\" xPosition=\""
						+ diagrams.get(i).getdStates().get(diagrams.get(i).getdStates().size() - 1).getxPosition()
						+ "\" yPosition=\""
						+ diagrams.get(i).getdStates().get(diagrams.get(i).getdStates().size() - 1).getyPosition()
						+ "\" incomingTransitions=\""
						+ diagrams.get(i).getdStates().get(diagrams.get(i).getdStates().size() - 1).getIncomingTransitions()
						+ "\"/>");

				for (int k = 1; k < diagrams.get(i).getdStates().size() - 1; k++) {
					writer.println("  <states uUID=\"" + diagrams.get(i).getdStates().get(k).getsId() + "\" name=\""
							+ diagrams.get(i).getdStates().get(k).getName() + "\" xPosition=\""
							+ diagrams.get(i).getdStates().get(k).getxPosition() + "\" yPosition=\""
							+ diagrams.get(i).getdStates().get(k).getyPosition() + "\" incomingTransitions=\""
							+ diagrams.get(i).getdStates().get(k).getIncomingTransitions() + "\" outgoingTransitions=\""
							+ diagrams.get(i).getdStates().get(k).getOutgoingTransitions() + "\"/>");
				}

				for (int l = 0; l < diagrams.get(i).getdTransitions().size(); l++) {
					writer.println("  <transitions uUID=\"" + diagrams.get(i).getdTransitions().get(l).gettId()
							+ "\" sourceState=\"" + diagrams.get(i).getdTransitions().get(l).getOrgSoId() + "\" targetState=\""
							+ diagrams.get(i).getdTransitions().get(l).getOrgTaId() + "\">");
					writer.println("    <probabilities>");
					writer.println("      <key href=\"/model/data/library/.profileslibrary#" + f.getProfileLibrary() + "\"/>");
					writer.println("      <value frequency=\"Normal\" value=\""
							+ diagrams.get(i).getdTransitions().get(l).getValue() + "\"/>");
					writer.println("    </probabilities>");
					writer.println("  </transitions>");
				}

				writer.println("</diagram:Diagram>");
				writer.close();
			}
		}
	}

	public void generateIds() {
		for (int i = 0; i < diagrams.size(); i++) {
			diagrams.get(i).setdId("_d" + String.valueOf(i + idSeed));
			for (int j = 0; j < diagrams.get(i).getdStates().size(); j++) {
				diagrams.get(i).getdStates().get(j).setsId("_d" + String.valueOf(i) + "s" + String.valueOf(j + idSeed));
			}
			for (int j = 0; j < diagrams.get(i).getdTransitions().size(); j++) {
				diagrams.get(i).getdTransitions().get(j).settId("_d" + String.valueOf(i) + "t" + String.valueOf(j + idSeed));
			}
		}
	}

	public void processPRT() {
		pname = f.getProjectPath().substring(f.getProjectPath().lastIndexOf("\\") + 1, f.getProjectPath().length());
		pId = null;
		rootChain = null;
		int pDetails = 0;
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(f.getProjectPath() + "\\" + pname + ".prt"));

		} catch (FileNotFoundException ex) {
			Logger.getLogger(FileApplications.class.getName()).log(Level.SEVERE, null, ex);
		}
		String line = null;
		do {
			try {
				line = reader.readLine();

			} catch (IOException ex) {
				Logger.getLogger(FileApplications.class.getName()).log(Level.SEVERE, null, ex);
			}
			if (line.startsWith("<project:")) {
				line = line.trim();
				pId = line;
				rootChain = line;

				pId = pId.substring(pId.indexOf("uUID=\"") + 6, pId.length());
				pId = pId.substring(0, pId.indexOf('\"'));

				rootChain = rootChain.substring(rootChain.indexOf("rootChain=\"") + 11, rootChain.length());
				rootChain = rootChain.substring(0, rootChain.indexOf('\"'));
				break;
			}
		} while (line != null);
		try {
			reader.close();

		} catch (IOException ex) {
			Logger.getLogger(FileApplications.class.getName()).log(Level.SEVERE, null, ex);
		}

		PrintWriter writer = null;
		try {
			writer = new PrintWriter(new File(f.getProjectPath() + "\\" + pname + ".prt"));

		} catch (FileNotFoundException ex) {
			Logger.getLogger(Applications.class.getName()).log(Level.SEVERE, null, ex);
		}

		writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		writer.println("<project:Project xmlns:project=\"http://all4tec.net/matelo/project/v5.4.1\" uUID=\"" + pId
				+ "\" rootChain=\"" + rootChain + "\">");
		writer.println("  <chains uUID=\"" + rootChain + "\" name=\"" + generalPathLists.get(0) + "\" path=\"\\model\\chain\\"
				+ generalPathLists.get(0) + ".mcm\"/>");
		for (int i = 1; i < diagrams.size(); i++) {
			writer.println("  <chains uUID=\"" + diagrams.get(i).getdId() + "\" name=\"" + generalPathLists.get(i)
					+ "\" path=\"\\model\\chain\\" + generalPathLists.get(i) + ".mcm\"/>");
		}
		writer.println("</project:Project>");
		writer.close();
	}

	public void sortDiagrams() {
		Diagram d = new Diagram();

		for (int i = 0; i < diagrams.size() - 1; i++) {
			for (int j = 1; j < diagrams.size(); j++) {
				if (diagrams.get(i).getDiagramLvl() > diagrams.get(j).getDiagramLvl()) {
					d = diagrams.get(j);
					diagrams.remove(j);
					diagrams.add(i, d);
				}
			}
		}

	}

	/**
	 * eger düzensiz bir transition var ise veya daha ust modellerde gecis
	 * gerekliyse bunu analiz eder ve o modelde pre/post dominator stateleri
	 * olusturup dogrulama için post dan pre dominator state e bir transition
	 * ekler
	 * 
	 */
	public void checkMacroStates() {
		for (int i = 0; i < diagrams.size() - 1; i++) {
			if (diagrams.get(i).getDiagramLvl() == 0) {
				for (int j = i + 1; j < diagrams.size(); j++) {
					if (diagrams.get(j).getDiagramPath().size() == 1) {
						for (int k = 0; k < diagrams.get(i).getdStates().size(); k++) {
							if (diagrams.get(j).getDiagramPath().get(0).toString()
									.equalsIgnoreCase(diagrams.get(i).getdStates().get(k).getName())) {
								diagrams.get(i).getdStates().get(k).setMacroState(true);
								// diagrams.get(i).getdStates().get(k).setMacroStateDestination(diagrams.get(j).getdId());
								diagrams.get(i).getdStates().get(k).setDiagramId(diagrams.get(j).getdId());
								stateList.add(diagrams.get(i).getdStates().get(k));

							}
						}
					}
				}
			} else {

				for (int j = i + 1; j < diagrams.size(); j++) {
					int cnt = 0;
					if (diagrams.get(j).getDiagramLvl() - 1 == diagrams.get(i).getDiagramLvl()) {
						for (int k = 0; k < diagrams.get(i).getDiagramPath().size(); k++) {
							if (diagrams.get(i).getDiagramPath().get(k).equalsIgnoreCase(diagrams.get(j).getDiagramPath().get(k))) {
								cnt++;
							}
						}
						if (cnt == diagrams.get(i).getDiagramPath().size()) {
							for (int k = 0; k < diagrams.get(i).getdStates().size(); k++) {
								if (diagrams.get(j).getDiagramPath().get(diagrams.get(j).getDiagramPath().size() - 1)
										.equalsIgnoreCase(diagrams.get(i).getdStates().get(k).getName())) {
									diagrams.get(i).getdStates().get(k).setMacroState(true);
									// diagrams.get(i).getdStates().get(k).setMacroStateDestination(diagrams.get(j).getdId());
									diagrams.get(i).getdStates().get(k).setDiagramId(diagrams.get(j).getdId());
									stateList.add(diagrams.get(i).getdStates().get(k));
								}
							}
						}
					}
				}
			}
		}
		for (int i = 0; i < stateList.size(); i++) {
			if (stateList.get(i).getStateLevel() > 0) {
				stateList.get(i).getStatePath().add(0, "Root");
			}
		}
	}

	public String as(String z) {
		int q = 0;
		int w = 0;
		for (int i = 0; i < z.length(); i++) {
			if (z.charAt(i) == 'd') {
				q = i + 1;
			} else if (z.charAt(i) == 's') {
				w = i;
			}
		}
		return z.substring(q, w);
	}

	public void processIchains() {

		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(f.getProjectPath() + "\\model\\data\\library\\.strategieslibrary"));

		} catch (FileNotFoundException ex) {
			Logger.getLogger(Applications.class.getName()).log(Level.SEVERE, null, ex);
		}
		String line = null;

		do {
			try {
				line = reader.readLine();

			} catch (IOException ex) {
				Logger.getLogger(FileApplications.class.getName()).log(Level.SEVERE, null, ex);
			}
			if (line.contains(".instancechains#")) {
				instancechain = line.substring(line.indexOf("#") + 1, line.lastIndexOf("\"/>"));
				break;
			}
		} while (line != null);
		try {
			reader.close();

		} catch (IOException ex) {
			Logger.getLogger(FileApplications.class.getName()).log(Level.SEVERE, null, ex);
		}
		for (int i = 0; i < stateList.size(); i++) {
			MacroState m = new MacroState(stateList.get(i));
			macroList.add(m);
			// m.setMacroStateId('_'+instChanUid+String.valueOf(i));
			m.setMacroStateId(stateList.get(i).getDiagramId());
		}
		for (int i = 0; i < macroList.size(); i++) {

			if (macroList.get(i).getS().getStateLevel() == 0) {
				macroList.get(i).setParrent(instancechain);
				macroList.get(i).setPosNumber(i);
				for (int j = 0; j < macroList.size(); j++) {
					int a = 0;
					if (macroList.get(j).getS().getStateLevel() - 1 == macroList.get(i).getS().getStateLevel()) {
						for (int k = 0; k < macroList.get(i).getS().getStatePath().size(); k++) {
							if (macroList.get(i).getS().getStatePath().get(k)
									.equalsIgnoreCase(macroList.get(j).getS().getStatePath().get(k))) {
								a++;
							}
						}
						if ((a - 1 == macroList.get(i).getS().getStateLevel())
								&& (macroList.get(j).getS().getStatePath().get(a).equalsIgnoreCase(macroList.get(i).getS()
										.getName()))) {
							macroList.get(i).setChildrenId(macroList.get(j).getS().getDiagramId());
							macroList.get(i).setChieldIndex(j);
							macroList.get(j).setParrent(macroList.get(i).getMacroStateId());

						}
					}

				}
			} else {
				boolean condition = false;
				for (int j = 0; j < macroList.size(); j++) {
					int a = 0;
					if (macroList.get(j).getS().getStateLevel() - 1 == macroList.get(i).getS().getStateLevel()) {
						for (int k = 0; k < macroList.get(i).getS().getStatePath().size(); k++) {
							if (macroList.get(i).getS().getStatePath().get(k)
									.equalsIgnoreCase(macroList.get(j).getS().getStatePath().get(k))) {
								a++;
							}
						}
						if ((a == macroList.get(j).getS().getStateLevel())
								&& (macroList.get(j).getS().getStatePath().get(a + 1).equalsIgnoreCase(macroList.get(j).getS()
										.getName()))) {
							macroList.get(i).setChildrenId(macroList.get(j).getS().getDiagramId());
							macroList.get(i).setChieldIndex(j);
							macroList.get(i).setPosNumber(i);
							macroList.get(j).setParrent(macroList.get(i).getMacroStateId());
						}
					}

				}

			}
		}
		for (int i = 0; i < macroList.size() - 1; i++) {
			for (int j = 1; j < macroList.size(); j++) {
				if (macroList.get(i).getChildrenId() != null) {
					if (macroList.get(j).getParrent().equalsIgnoreCase(macroList.get(i).getMacroStateId())) {
						macroList.get(i).setHasChield(true);
					}
				}
			}
		}

		// ----------------------------------------------------------------------------------------------------------
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(new File(f.getProjectPath() + "\\model\\data\\utils\\a.instancechains"));

		} catch (FileNotFoundException ex) {
			Logger.getLogger(Applications.class.getName()).log(Level.SEVERE, null, ex);
		}

		writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
				+ "<instances:RootInstanceChain xmlns:instances=\"http://all4tec.net/matelo/instances/v5.4.1\" uUID=\""
				+ instancechain + "\">");

		for (int i = 0; i < macroList.size(); i++) {
			if (macroList.get(i).usable()) {
				if (!macroList.get(i).isHasChield()) {
					writer.print("  <children uUID=\"" + macroList.get(i).getMacroStateId() + "\" macroStateId=\""
							+ macroList.get(i).getS().getsId() + "\" parent=\"" + macroList.get(i).getParrent() + "\">\n");
					writer.println("    <chain href=\"/.prt#" + macroList.get(i).getS().getDiagramId() + "\"/>");
					writer.println("  </children>");
					macroList.get(i).setUsable(false);
				} else {
					generateMacroStates(macroList.get(i), writer);
				}
			}
		}

		writer.println("  <chain href=\"/.prt#" + rootChain + "\"/>");

		writer.println("</instances:RootInstanceChain>");
		writer.close();

	}

	public void generateMacroStates(MacroState m, PrintWriter p) {
		String z = "";
		for (int i = 0; i < m.getS().getStateLevel() + 1; i++) {
			z = z.concat("  ");
		}
		p.print(z + "<children uUID=\"" + m.getMacroStateId() + "\" macroStateId=\"" + m.getS().getsId() + "\" parent=\""
				+ m.getParrent() + "\">\n");
		if (m.isHasChield()) {
			generateMacroStates(macroList.get(m.getChieldIndex()), p);
		}

		p.println(z + "  <chain href=\"/.prt#" + m.getS().getDiagramId() + "\"/>");
		p.println(z + "</children>");
		m.setUsable(false);
	}
}

// }
