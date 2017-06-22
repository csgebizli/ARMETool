package maps;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.Console;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.TreeMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
public class main {

	synchronized public static void main(String[] args) throws BiffException, IOException, InterruptedException, ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException  {

		final TreeMap<String, String> mapping = new TreeMap<String, String>();
		final ArrayList<String> statusarray = new ArrayList();
		final ArrayList<String> logarray = new ArrayList();
		//File inputWorkbook = new File("C:/Users/cerens/Desktop/New folder/5/Test Dosyaları/Map.xls");

		final JTextField textField1;        
		final JTextField textField2;

		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		JFrame ok=new JFrame("Event Mapping Matrix Creator");
		JPanel h=new JPanel();

		textField1 = new JTextField();
		textField1.setBounds(192, 35, 183, 20);
		ok.getContentPane().add(textField1);
		textField1.setColumns(10);

		textField2 = new JTextField();
		textField2.setBounds(192, 85, 183, 20);
		ok.getContentPane().add(textField2);
		textField2.setColumns(10);

		final ArrayList<Integer> lstn = new ArrayList(); 


		h.setSize(300,300);
		ok.setBounds(100, 100, 450, 300);

		ok.add(h);
		ok.pack();
		ok.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ok.setVisible(true);
		ok.setBounds(100, 100, 450, 300);
		ok.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ok.getContentPane().setLayout(null);







		try {
			JButton btnNewButton_1 = new JButton("Select Log File");
			btnNewButton_1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {


					JFileChooser fileChooser = new JFileChooser();
					if (fileChooser.showOpenDialog(fileChooser) == JFileChooser.APPROVE_OPTION) {
						Scanner input = new Scanner(System.in);
						File file = fileChooser.getSelectedFile();
						String logyolu=file.getAbsolutePath();
						textField1.setText(logyolu);
						try {
							input = new Scanner(file);
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						while (input.hasNextLine()){
							logarray.add(input.nextLine());
						}
						input.close();
					}

				}
			});

			btnNewButton_1.setBounds(21, 34, 150, 23);
			ok.getContentPane().add(btnNewButton_1);
		}
		catch (Exception e)
		{
			System.out.println("ERROR: unable to read file " + "C:/Users/cerens/Desktop/New folder/5/Test Dosyaları/logfile.log");
			e.printStackTrace(); 
		}


		try {
			JButton btnNewButton1 = new JButton("Select Map File");
			btnNewButton1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					JFileChooser fileChooser = new JFileChooser();
					if (fileChooser.showOpenDialog(fileChooser) == JFileChooser.APPROVE_OPTION) {


						File file = fileChooser.getSelectedFile();	
						String logyolu=file.getAbsolutePath();
						textField2.setText(logyolu);
						Workbook workbook;
						try {
							workbook = Workbook.getWorkbook(file);
							Sheet mapsSheet       = workbook.getSheet(0);
							int rows = mapsSheet.getRows();	
							for (int row = 1; row < rows; row++) 
							{
								mapping.put(
										(mapsSheet.getCell(0,row).getContents().replaceAll("<", "").replaceAll(">", "")),
										(mapsSheet.getCell(1,row).getContents())
										);
							}
						} catch (BiffException | IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}



					}}});


			btnNewButton1.setBounds(21, 84, 150, 23);
			ok.getContentPane().add(btnNewButton1);
		}

		catch(Exception e)
		{
			e.printStackTrace(); 
		}

		try
		{
			JButton btnNewButton_2 = new JButton("Create");
			btnNewButton_2.setFocusable(false);

			btnNewButton_2.addActionListener(new ActionListener() {


				synchronized public void actionPerformed(ActionEvent e) {

					System.out.println(mapping);

					String temp = "";
					String newly = "";
					for (int i = 0; i < logarray.size(); i++){

						if(isStarted(mapping.keySet(),  logarray.get(i)) || temp.length()>0 ){ // yeni bir eleman basliyorsa
							if(newly.length() > 0 ){//yeni bir eleman flag'ini sifirlamak
								mapping.put(newly,"newly added"+i);
								statusarray.add( mapping.get(newly));
								System.out.println("newly : "+ newly); 

								newly="";
							}

							temp+=logarray.get(i);
							if (mapping.keySet().contains(temp)){//contains'i appendi ediyor

								statusarray.add( mapping.get(temp));
								System.out.println("var : " + mapping.get(temp));
								temp="";

							}else{

							}
						}else{	//yeni bir elemanla baslamiyorsa
							newly+=logarray.get(i);
							if(i==logarray.size()-1 && newly.length()>0 ) {
								mapping.put(newly,"newly added"+i);

								statusarray.add( mapping.get(newly));
								System.out.println("newly : "+ newly);

							}
						}
					}
					System.out.println(mapping);
					System.out.println("Status : "+statusarray);
				
					writeToExcel(mapping, statusarray, logarray);
					
					JOptionPane.showMessageDialog(null, "Matrix and Probability Files are created", "Done", JOptionPane.INFORMATION_MESSAGE);

				}
			});
			btnNewButton_2.setBounds(21, 180, 150, 54);
			ok.getContentPane().add(btnNewButton_2);
		}
		catch(Exception e)
		{
			e.printStackTrace(); 
		}


		//		do {
		//			Thread.sleep(500);
		//		} while(lstn.size()==0);
	}


	private static void writeToExcel(TreeMap<String, String> mapping, ArrayList<String> statusArray , ArrayList<String> logArray){

		try{

			String userHomeFolder = System.getProperty("user.home");
			File filename = new File(userHomeFolder, "Matrix.xls");
			HSSFWorkbook workbook1 = new HSSFWorkbook();
			HSSFSheet sheet = workbook1.createSheet("FirstSheet");  
			HSSFRow rowhead = sheet.createRow((short)0);  


			File filename2 = new File(userHomeFolder, "Probability.xls");
			HSSFWorkbook workbook2 = new HSSFWorkbook();
			HSSFSheet sheet2 = workbook2.createSheet("Sheet");  
			HSSFRow rowhead2 = sheet2.createRow((short)0);  
			HSSFRow rowhead4 = sheet2.createRow((short)1);
			rowhead4.createCell(0).setCellValue("Default Prob.");
			HSSFRow rowhead3 = sheet2.createRow((short)2);
			rowhead3.createCell(0).setCellValue("Calculated Prob.");
			HSSFRow rowhead5 = sheet2.createRow((short)3);
			rowhead5.createCell(0).setCellValue("Updated Prob.");
			TreeMap<String, Integer> locator = new TreeMap<>();

			int j = 1;
			Iterator<String> it = mapping.values().iterator();
			while(it.hasNext()){
				HSSFRow cell = sheet.createRow(j+1);
				HSSFRow cell2 = sheet.createRow(1);
				cell2.createCell(0).setCellValue("invoke");
				String key = (String) it.next();	
				rowhead.createCell(j).setCellValue(key);
				cell.createCell(0).setCellValue(key);
				locator.put(key, j);

				j++;
			}
			int index =1;
			Iterator<String> locociterator = locator.keySet().iterator();
			while (locociterator.hasNext()){
			String item =	locociterator.next();
			int occurances = Collections.frequency(statusArray, item);
			rowhead2.createCell(index).setCellValue(item);
			rowhead4.createCell(index).setCellValue((float)1/(locator.values().size()));
			rowhead3.createCell(index).setCellValue( (float) occurances/statusArray.size());
			rowhead5.createCell(index).setCellValue((float)((float)occurances/statusArray.size() + (float)1/locator.values().size()) / 2);
			index++;
			}
		
			
			System.out.println(locator);
			System.out.println((float)1/(locator.values().size()));

			for(int i=1; i<statusArray.size(); i++){

				System.out.println(locator.get(statusArray.get(i)));

				int prev = locator.get(statusArray.get(i-1));
				int next = locator.get(statusArray.get(i));
				sheet.getRow(prev+1).createCell(next).setCellValue("X");
				
			}
			sheet.getRow(1).createCell(1).setCellValue("X");
			String lastStatus = statusArray.get(statusArray.size()-1);
			int lastElemLocation=locator.get(lastStatus);
			sheet.getRow(lastElemLocation+1).createCell(rowhead.getLastCellNum()).setCellValue("X");


			rowhead.createCell(rowhead.getLastCellNum()).setCellValue("terminate");
			FileOutputStream fileOut = new FileOutputStream(filename);
			workbook1.write(fileOut);
			fileOut.close();

			FileOutputStream fileOut2 = new FileOutputStream(filename2);
			workbook2.write(fileOut2);
			fileOut2.close();


		}catch(Exception ex){
			ex.printStackTrace();
		}

	}

	private static boolean isStarted(Collection<String> collection, String string) {
		boolean result = false;

		Iterator<String> it = collection.iterator();
		while(it.hasNext()){
			String value = it.next();
			if(value.startsWith(string))

				result = true;
		}

		return result;

	}

}