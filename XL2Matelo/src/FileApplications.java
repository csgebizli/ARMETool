
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/**
 *
 * @author VESTATEST
 */
public class FileApplications {

    public String getProjectPath() {
        return projectPath;
    }

    public void setProjectPath(String projectPath) {
        this.projectPath = projectPath;
    }

    private String profileLibrary;
    private FileNameExtensionFilter filter;
    private JFileChooser chooser = new JFileChooser();
    private JFileChooser dirChooser = new JFileChooser();
    private boolean dirCntrol = false;
    private File inputFile;
    private Workbook workbook;
    private Sheet sheet;
    private String projectPath;

    public Workbook getWorkbook() {
        return workbook;
    }

    public void setWorkbook(Workbook workbook) {
        this.workbook = workbook;
    }

    public Sheet getSheet() {
        return sheet;
    }

    public void setSheet(Sheet sheet) {
        this.sheet = sheet;
    }

    public String dirPath() {
        String s = dirChooser.getSelectedFile().getPath();
        return s;
    }

    public JFileChooser getChooser() {
        return chooser;
    }

    public void setChooser(JFileChooser chooser) {
        this.chooser = chooser;
    }

    public String getProfileLibrary() {
        return profileLibrary;
    }

    public void setProfileLibrary(String profileLibrary) {
        this.profileLibrary = profileLibrary;
    }

    public File getInputFile() {
        return inputFile;
    }

    public void setInputFile(File inputFile) {
        this.inputFile = inputFile;
    }

    public String directoryPath() {
        dirChooser = new JFileChooser("C:/");
        dirChooser.setDialogTitle("Select Project Folder");
        dirChooser.setMultiSelectionEnabled(false);
        dirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        dirChooser.setAcceptAllFileFilterUsed(false);

        if (dirChooser.showOpenDialog(dirChooser) == JFileChooser.APPROVE_OPTION) {
            dirCntrol = true;
            dirCtrl();
            projectPath = dirChooser.getSelectedFile().getPath();
            return dirChooser.getSelectedFile().getPath();
        } else {
            return "Kill process";
        }
    }

    public void dirCtrl() {
        if (!dirCntrol) {
            JOptionPane.showConfirmDialog(null, "Restart & Select directory");
            System.exit(1);
        }
    }

    public void findProfileLib() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(projectPath+"\\model\\data\\library\\.profileslibrary"));
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
            line = line.trim();
            if (line.substring(1, 9).equalsIgnoreCase("contents")) {
                line = line.substring(line.indexOf('_'),
                        line.indexOf('_')+23);
                profileLibrary = line;
                break;
            }

        } while (line != null);
        try {
            reader.close();
        } catch (IOException ex) {
            Logger.getLogger(FileApplications.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public String selectXlFile() throws BiffException, IOException {
        chooser.setDialogTitle("Select Excel file");
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        filter = new FileNameExtensionFilter("EXCEL (.xls )", "xls");
        chooser.addChoosableFileFilter(filter);
        chooser.setFileFilter(filter);

        int option = chooser.showOpenDialog(chooser);
        switch (option) {
            case JFileChooser.CANCEL_OPTION:

            case JFileChooser.ERROR_OPTION:
                JOptionPane.showMessageDialog(null, "Could not OPEN file", "ERROR",
                        JOptionPane.ERROR_MESSAGE);

        }
        inputFile = chooser.getSelectedFile();
        if (!inputFile.exists()) {
            JOptionPane.showMessageDialog(null, "Could not FOUND file",
                    "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        workbook = Workbook.getWorkbook(inputFile);
        setSheet(workbook.getSheet(0));
        return chooser.getSelectedFile().getPath();
    }

}
