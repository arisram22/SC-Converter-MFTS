/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.danamon.converter;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author aris
 */
class CONVERT {
      private static DecimalFormat df2 = new DecimalFormat("###.###");
  private static Long startTime;

  public void deleteFileOut(File file) {
    for (File del: Objects.requireNonNull(file.listFiles())) {
      del.delete();
    }
  }


     private static boolean checkingFileStructure(List<String[]> records, String nameFile) {
    int rowCount = 0;
    String[] rowError = new String[records.size()];
    String[] delimiterError = new String[records.size()];
    String[] amountError = new String[records.size()];
    int count = 0;
    int delimiterCount = 0;
    int amountCount = 0;
    boolean status = true;
    boolean statusRow = true;
    boolean statusDelimiter = true;
    boolean statusAmmount = true;
    for (String[] record : records) {
      rowCount++;
      int length = record.length - 1;
      if (length < 3 || length > 6) {
        statusRow = false;
        rowError[count] = String.valueOf(rowCount);
        count++;
      } 
      if (Arrays.toString((Object[])record).contains(";")) {
        statusDelimiter = false;
        delimiterError[delimiterCount] = String.valueOf(rowCount);
        delimiterCount++;
      } 
      try {
        String[] amount = record[1].split("\\.");
//        System.out.println(amount[1].length());
        if (amount[1].length() > 2) {
          statusAmmount = false;
          amountError[amountCount] = String.valueOf(rowCount);
          amountCount++;
        } 
      } catch (Exception exception) {}
    } 
    if (!statusRow) {
      String[] errorCount = new String[count];
      System.arraycopy(rowError, 0, errorCount, 0, count);
      JOptionPane.showMessageDialog(null, "Error file " + nameFile + " row " + Arrays.toString((Object[])errorCount)
          .replace("[", "").replace("]", "")
          .replaceAll("(.15)", "$0\n"));
      status = false;
    } 
    if (!statusAmmount) {
      String[] errorCount = new String[amountCount];
      System.arraycopy(amountError, 0, errorCount, 0, amountCount);
      JOptionPane.showMessageDialog(null, "Check amount \nError file " + nameFile + " row " + Arrays.toString((Object[])errorCount)
          .replace("[", "").replace("]", "")
          .replaceAll("(.15)", "$0\n"));
      status = false;
    } 
    if (!statusDelimiter) {
      String[] errorCount = new String[delimiterCount];
      System.arraycopy(delimiterError, 0, errorCount, 0, delimiterCount);
      JOptionPane.showMessageDialog(null, "Delimiter should be ','\nCheck delimiter file " + nameFile + " row " + Arrays.toString((Object[])errorCount)
          .replace("[", "").replace("]", "")
          .replaceAll("(.15)", "$0\n"));
      status = false;
    } 
    return status;
  }

        
    
    public void convertAutoCredit(File in,File Out,String debitAccount,String descriptionAccount,String chargeTo,String chargeType,String productCode,String instructionDate,String instructionAt) {

        deleteFileOut(Out);


    String[] fileList = in.list();
    if (fileList.length == 0)
      JOptionPane.showMessageDialog(null, "File conversion error!! Please check the input file."); 
    for (String nameFile : fileList) {
      int recordCount = 0;
      startTime = null;
      startTime = Long.valueOf(System.nanoTime());
      try {
           String fileIn = in .toString() + File.separator + nameFile;
                FileReader fileReader = new FileReader(fileIn);
       
        CSVReader csvReader = new CSVReader(fileReader);
        List<String[]> records = csvReader.readAll();
        if (checkingFileStructure(records, nameFile)) {
          String[] out = nameFile.split("\\.");
//          File file = new File(prop.getProperty("app.folder_out") + out[0] + ".csv");
           File file = new File(Out.toString()+File.separator+ out[0]+ ".csv");
          FileWriter outputfile = new FileWriter(file);
                
          CSVWriter writer = new CSVWriter(outputfile, ',', '\u0000', '\u0000', "\n");
//          CSVWriter writer = new CSVWriter(outputfile, ',', false, false, "\n");
          writer.writeNext(new String[] { "H", debitAccount, descriptionAccount, "S", "Y", "", instructionDate, instructionAt, "" });
          for (String[] record : records) {
            recordCount++;
            int length = record.length - 1;
            String email = "";
            if (length == 6) {
              email = record[4] + ";" + record[5] + ";" + record[6];
            } else if (length == 5) {
              email = record[4] + ";" + record[5];
            } else if (length == 4) {
              email = record[4];
            } 
             String record2=record[2];
            writer.writeNext(new String[] { 
                  "D", productCode, "", "", "", "", "", "", "", record[0], 
                  "", "", "", "", "", email, "", record2, record[3], "", 
                  "", record[1], "", "", chargeTo, chargeType, "", "", "", "", 
                  "Y", "", "", "", "", "", "", "", "" });
          } 
          long endTime = System.nanoTime();
          long totalTime = endTime - startTime.longValue();
          double sec = totalTime / 1.0E9D;
          int seconds = (int)sec;
          JOptionPane.showMessageDialog(null, "Successfully converting " + recordCount + " record(s) \n in " + df2.format(sec) + " seconds");
          writer.close();
        } 
      } catch (IOException|ArrayIndexOutOfBoundsException e) {
        JOptionPane.showMessageDialog(null, "File conversion error!! Please check the input file.");
      } catch (CsvException ex) { 
            Logger.getLogger(CONVERT.class.getName()).log(Level.SEVERE, null, ex);
        } 
    } 
  }
  
    
}
