/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.danamon.converter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javax.swing.JOptionPane;

/**
 *
 * @author Administrator
 */
public class FileAndFolder {
    private File  folderIN, folderOUT;
     private String debit_account_no, account_description,instruction_at,bulk_upload,charge_to,charge_type;
    private static Properties prop;
   
    //chek app config
     void chekConfigFile() {
          prop = new Properties();
        String properties= "app.config";

        InputStream config = null;
        try {
            config = new FileInputStream(properties);
            prop.load(config);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Config File not found");
            e.printStackTrace();
        }
        folderIN= new File(prop.getProperty("app.folder_in"));
        folderOUT= new File(prop.getProperty("app.folder_out"));
        debit_account_no= prop.getProperty("app.debit_account_no");
        account_description=prop.getProperty("app.account_description");
        instruction_at= prop.getProperty("app.instruction_at");
        bulk_upload=prop.getProperty("app.bulk_upload");
        charge_to=prop.getProperty("app.charge_to");
        charge_type = prop.getProperty("app.charge_type");
                
        
        checkFolder(folderIN,folderOUT,debit_account_no,account_description,instruction_at);
    } 
     
     
  
     
    public File getFolderIn() {
        return folderIN;
    }

    public File getFolderOut() {
        return folderOUT;
    }
    
    public String getDebitAccount() {
        return debit_account_no;
    }
    public String getAccountDesc() {
        return account_description;
    }
    public String getIntructionAt() {
        return instruction_at;
    }
    public String getBulkUpload() {
        return bulk_upload;
    }
    public String getChargeTo() {
        return charge_to;
    }
    public String getChargeType() {
        return charge_type;
    }



     public boolean checkFolder( 
            File folderIn,
            File folderOut,
            String debitAccount,
            String accountDesc,
            String intructionAt
     ){
         
        

        boolean status= true;

        if(!folderIn.exists()){
            JOptionPane.showMessageDialog(null, "Folder IN not found.");
            status= false;
        }

        if(!folderOut.exists()){
            JOptionPane.showMessageDialog(null, "Folder Out not found");
            status= false;
        }
          if(debitAccount.length()>12){
            JOptionPane.showMessageDialog(null, "Check config file\nCredit Account No couldn't be more than 34");
            status= false;
        }

        if(accountDesc.length()>60){
            JOptionPane.showMessageDialog(null, "Check config file\nCredit Account No couldn't be more than 50");
            status= false;
        }

        if(intructionAt.length()>4 || intructionAt.length()<4){
            JOptionPane.showMessageDialog(null, "Check config file\nInstruction at couldn't be more or less than 4");
            status= false;
        }
        return status;
    }

    private static boolean checkConfigFile() {
    File folderIN = new File(prop.getProperty("app.folder_in"));
    File folderOUT = new File(prop.getProperty("app.folder_out"));
    boolean status = true;
    if (!folderIN.exists()) {
      JOptionPane.showMessageDialog(null, "Check config file\nfolder in not found ");
      status = false;
    } 
    if (!folderOUT.exists()) {
      JOptionPane.showMessageDialog(null, "Check config file\nfolder out not found");
      status = false;
    } 
    if (prop.getProperty("app.debit_account_no").length() > 12) {
      JOptionPane.showMessageDialog(null, "Check config file\nCredit Account No couldn't be more than 34");
      status = false;
    } 
    if (prop.getProperty("app.account_description").length() > 60) {
      JOptionPane.showMessageDialog(null, "Check config file\nCredit Account No couldn't be more than 50");
      status = false;
    } 
    if (prop.getProperty("app.credit_account_currency_code").length() > 3 || prop
      .getProperty("app.credit_account_currency_code").length() < 3) {
      JOptionPane.showMessageDialog(null, "Check config file\nCredit Account No couldn't be more or less than 3");
      status = false;
    } 
    if (prop.getProperty("app.instruction_at").length() > 4 || prop
      .getProperty("app.instruction_at").length() < 4) {
      JOptionPane.showMessageDialog(null, "Check config file\nInstruction at couldn't be more or less than 4");
      status = false;
    } 
    return status;
  }
}
