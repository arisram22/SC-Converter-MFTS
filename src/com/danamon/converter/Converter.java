/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.danamon.converter;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

/**
 *
 * @author Aris
 */
public class Converter {

  private static DecimalFormat df2 = new DecimalFormat("###.###");
  private static Long startTime;

    public void deleteFileOut(File file) {
    for (File del: Objects.requireNonNull(file.listFiles())) {
      del.delete();
    }
  }


    private static String findBankCode(String bank) throws CsvException{
        String fileBank= "Bank Table.txt";
        try {
            FileReader fileReader = new FileReader(fileBank);;
            CSVReader csvReader = new CSVReader(fileReader);
            List<String[]> records = csvReader.readAll();
            for (String[] record:records){
                if(bank.toUpperCase().equals(record[0].toUpperCase())){
                    return record[1];
                }
            }
        } catch (RuntimeException | IOException e) {
            JOptionPane.showMessageDialog(null, "Bank Table.txt not found");
            System.exit(0);
            e.printStackTrace();
        }
        return "";
    }
    private static boolean patternMatches(String emailAddress) {
//      String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
//                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        String regexPattern ="^[A-Za-z0-9+_.-]+@(.+)$";
        return Pattern.compile(regexPattern)
                .matcher(emailAddress)
                .matches();
    }
    private static boolean checkingFileStructure(List<String[]> records, String nameFile) {
        int rowCount=0;
        String[] rowError = new String[records.size()];
        String[] delimiterError = new String[records.size()];
        String[] amountError = new String[records.size()];
        int count=0;
        int delimiterCount= 0;
        int amountCount= 0;

        boolean status= true;

        boolean statusRow= true;
        boolean statusDelimiter= true;
        boolean statusAmmount= true;

        for (String[] record:records){
            rowCount+=1;
            int length= record.length-1;
            if (length<9 || length>12){
                statusRow= false;
                rowError[count]= String.valueOf(rowCount);
                count+=1;
            }

            if(Arrays.toString(record).contains(";")){
                statusDelimiter= false;
                delimiterError[delimiterCount]= String.valueOf(rowCount);
                delimiterCount+=1;
            }

            try{
                String[] amount= record[1].split("\\.");
//                System.out.println(amount[1].length());
                if (amount[1].length()>2){
                    statusAmmount= false;
                    amountError[amountCount]= String.valueOf(rowCount);
                    amountCount+=1;
                }
            }catch (Exception e){
//                statusAmmount= false;
//                amountError[amountCount]= String.valueOf(rowCount);
//                System.out.println("error");
//                amountCount+=1;
            }
        }
        if (!statusRow){
            String[] errorCount= new String[count];
            System.arraycopy(rowError, 0, errorCount, 0, count);
//            JOptionPane.showMessageDialog(parent, "Error file "+ nameFile+" row "+ Arrays.toString(errorCount).replace("[","").replace("]", ""));
            JOptionPane.showMessageDialog(null, "Error file "+ nameFile+" row "+ Arrays.toString(errorCount)
                    .replace("[","").replace("]", "")
                    .replaceAll("(.15)", "$0\n"));
            status= false;
        }

        if (!statusAmmount){
            String[] errorCount= new String[amountCount];
            System.arraycopy(amountError, 0, errorCount, 0, amountCount);
//            JOptionPane.showMessageDialog(parent, "Error file "+ nameFile+" row "+ Arrays.toString(errorCount).replace("[","").replace("]", ""));
            JOptionPane.showMessageDialog(null, "Check amount \nError file "+ nameFile+" row "+ Arrays.toString(errorCount)
                    .replace("[","").replace("]", "")
                    .replaceAll("(.15)", "$0\n"));
            status= false;
        }

        if (!statusDelimiter){
            String[] errorCount= new String[delimiterCount];
            System.arraycopy(delimiterError, 0, errorCount, 0, delimiterCount);
//            JOptionPane.showMessageDialog(parent, "Error file "+ nameFile+" row "+ Arrays.toString(errorCount).replace("[","").replace("]", ""));
            JOptionPane.showMessageDialog(null, "Delimiter should be ','\nCheck delimiter file "+ nameFile+" row "+ Arrays.toString(errorCount)
                    .replace("[","").replace("]", "")
                    .replaceAll("(.15)", "$0\n"));
            status= false;
        }

        return status;

    }
    
//     public void convertSKNRTGS(File in,File Out,String debitAccount,String descriptionAccount,String chargeTo,String chargeType,String productCode,String instructionDate,String instructionAt){
//           String[] fileList = in .list();
//    List < KeyValue > listNew = new ArrayList < > ();
////        File dir= new File(prop.getProperty("app.folder_in"));
////        String[] fileList= dir.list();
//        String emailAddress = "username@domai";
//                          ç
//
//        if (fileList.length==0){
//            JOptionPane.showMessageDialog(null, "File conversion error!! Please check the input file.");
//        }
//        int recordCount;
//        for (String nameFile: fileList){
//            recordCount=0;
//            startTime= null;
//            startTime = System.nanoTime();
//            try {
//                String fileIn = in .toString() + File.separator + nameFile;
//                FileReader fileReader = new FileReader(fileIn);
//                CSVReader csvReader = new CSVReader(fileReader);
//                List<String[]> records = csvReader.readAll();
//                if (!checkingFileStructure(records, nameFile)){
//                    continue;
//                }
//
//                String[] out= nameFile.split(File.separator );
//                File file = new File(Out.toString()+File.separator+ out[0]+ ".csv");
//                //menghasilkan output di folder OUT sesuai nama folder IN
//                FileWriter outputfile = new FileWriter(file);
//                CSVWriter writer = new CSVWriter(outputfile, ',', '\u0000', '\u0000', "\n");
//
//                writer.writeNext(new String[]{"H", debitAccount, descriptionAccount, "S", "Y", "", instructionDate, instructionAt, ""});
//
//                for (String[] record:records){
//                    recordCount+=1;
//                    int length= record.length-1;
//                    String email="";
//                    String name=record[4];
//                    String bank=record[6];
//                    String code8=record[8];
//                    String code9=record[9];
//
//                    if(length == 12){
//                        email= record[10]+";"+record[11]+";"+record[12];
//                    }else if(length==11){
//                        email= record[10]+";"+record[11];
//                    }else if(length==10){
//                        if (patternMatches(record[10])){
//                            email= record[10];
//                        }
//                        else {
//                        name=record[4]+" "+record[5];
//                        bank=record[7];
//                        code8=record[9];
//                        code9=record[10];
//
//                        }
//                    }
//
//                    if(name.contains(",")){
//                        name='"'+name+'"';
//                    }
////                    System.out.println("sss"+length);
////                    System.out.println("sss"+record[8]);
//                    String bankCode= findBankCode(bank);
//                    writer.writeNext(new String[]{"D", productCode, code8, "", record[2], "", "", bankCode, "", record[0],name, "IDR",
//                            "X", "", "",email, "", "", record[3], "", "", record[1], "", "", chargeTo, chargeType, "", "", "", "",
//                            code9, "", "", "", "", "", "", "", ""});
//                }
//
//                long endTime   = System.nanoTime();
//                long totalTime = endTime - startTime;
//                double sec= (double) totalTime/1000000000;
//                int seconds= (int)sec;
//                JOptionPane.showMessageDialog(null, "Successfully converting "+recordCount+" record(s) \n in "+df2.format(sec)+" seconds");
//
//                writer.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//                JOptionPane.showMessageDialog(null, "File conversion error!! Please check the input file.");
//            }  catch (CsvException ex) {
//                   Logger.getLogger(Converter.class.getName()).log(Level.SEVERE, null, ex);
//               }
//
//        }
//    }
// 
     public void convertSKNRTGSupdate(File in,File Out,String debitAccount,String descriptionAccount,String chargeTo,String chargeType,String productCode,String instructionDate,String instructionAt){
        deleteFileOut(Out);
         String[] fileList = in .list();
        if (fileList.length==0){
            JOptionPane.showMessageDialog(null, "File conversion error!! Please check the input file.");
        }
        int recordCount;
        for (String nameFile: fileList){
            recordCount=0;
            startTime= null;
            startTime = System.nanoTime();
            try {
                String fileIn = in .toString() + File.separator + nameFile;
                FileReader fileReader = new FileReader(fileIn);
                CSVReader csvReader = new CSVReader(fileReader);
                List<String[]> records = csvReader.readAll();
                if (!checkingFileStructure(records, nameFile)){
                    continue;
                }

//                String[] out= nameFile.split(File.separator );
                String[] out= nameFile.split("\\.");
                File file = new File(Out.toString()+File.separator+ out[0]+ ".csv");
                //menghasilkan output di folder OUT sesuai nama folder IN
                FileWriter outputfile = new FileWriter(file);
                CSVWriter writer = new CSVWriter(outputfile, ',', '\u0000', '\u0000', "\n");

                writer.writeNext(new String[]{"H", debitAccount, descriptionAccount, "S", "Y", "", instructionDate, instructionAt, ""});

                for (String[] record:records){
                    recordCount+=1;
                    int length= record.length-1;
                    String email="";
                    String name=record[4];
                    String bank=record[6];
                    String code8=record[8];
                    String code9=record[9];                   
                    String desc=record[2];                   
                    String record3=record[3];

                    if(length == 12){
                        email= record[10]+";"+record[11]+";"+record[12];
                    }else if(length==11){
                        email= record[10]+";"+record[11];
                    }else if(length==10){
                        if (patternMatches(record[10])){
                            email= record[10];
                        }
                        else {
                        name=record[4]+" "+record[5];
                        bank=record[7];
                        code8=record[9];
                        code9=record[10];

                        }
                    }

                    if(name.contains(",")){
                        name='"'+name+'"';
                    }
                    String bankCode= findBankCode(bank);
                    writer.writeNext(new String[]{"D", productCode, code8, "", "", "", "", bankCode, "", record[0],name, "IDR",
                            "PUSAT", "", "",email, "", desc, "", "", "", record[1], "", "", chargeTo, chargeType, "", "", "", "",
                            code9, "", "", "", "", "", "", "", ""});
                }

                long endTime   = System.nanoTime();
                long totalTime = endTime - startTime;
                double sec= (double) totalTime/1000000000;
                int seconds= (int)sec;
                JOptionPane.showMessageDialog(null, "Successfully converting "+recordCount+" record(s) \n in "+df2.format(sec)+" seconds");

                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "File conversion error!! Please check the input file.");
            }  catch (CsvException ex) {
                   Logger.getLogger(Converter.class.getName()).log(Level.SEVERE, null, ex);
               }

        }
    }

}