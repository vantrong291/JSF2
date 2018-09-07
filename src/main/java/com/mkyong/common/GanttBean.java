/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mkyong.common;

import org.apache.commons.io.FileUtils;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import java.io.IOException;
import java.util.Scanner;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.Part;
import javax.xml.bind.ValidationException;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;


/**
 *
 * @author nkt-pvt
 */

@ManagedBean(name = "ganttBean")
@SessionScoped
public class GanttBean extends AbstractBean{

    private UploadedFile excelFile;

    public void upload(FileUploadEvent e) {
        this.excelFile = e.getFile();
//        File newFile = (File) this.file;
//        System.out.println(newFile.toString());
        System.out.println("Uploaded File Name Is :: " +
                this.excelFile.getFileName() +
                " :: Uploaded File Size :: " + this.excelFile.getSize());
        if(this.excelFile != null) {
            FacesMessage message = new FacesMessage("Succesful", this.excelFile.getFileName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }



    private Part file;
    private static final String FILENAME = "/home/vantrong291/Documents/updatedData.txt";

    @Override
    void processData(GanttEntity gantt){
        // save data to FILENAME
        String dat = gantt.toString();
        try {
            FileUtils.writeStringToFile(new File(FILENAME), dat);
            System.out.println("Save");
            System.out.println(this.data);
        }
        catch (IOException ex) {
            Logger.getLogger(GanttEntity.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            System.out.println(ex);
        }
    }

    @Override
    GanttEntity retrieveData(){
        //      do something to retieve data
        //      Ex: load data from db

        GanttEntity gantt = new GanttEntity();
        return gantt;
    }

    public String uploadExcel(FileUploadEvent e){
        // get fileContent
        try {
            this.excelFile = e.getFile();
            Scanner scanner = new Scanner(excelFile.getInputstream());
            String fileData = scanner.useDelimiter("\\A").next();
            scanner.close();
            GanttEntity ganttEntity = new GanttEntity(fileData);
            updateData(ganttEntity);
            System.out.print(fileData);
            return "success";
        }
        catch (IOException ex) {
            Logger.getLogger(GanttEntity.class.getName()).log(Level.SEVERE, null, ex);
            return "failed";
        }
        catch (NullPointerException ex) {
            Logger.getLogger(GanttEntity.class.getName()).log(Level.SEVERE, null, ex);
            return "failed";
        }

    }
    @Override
    public void pushData(String dat) {
        GanttEntity ganttEntity = GanttEntity.parseToJson(dat);
        processData(ganttEntity);
    }

    public void validate(FacesContext context, UIComponent component, Object value) {
        Part file = (Part) value;
        if (file.getSize() == 0) {
            throw new ValidatorException(new FacesMessage("This file is empty! Please choose another file!"));
        }
        else if (!file.getContentType().equals("text/plain")) {
            throw new ValidatorException(new FacesMessage("File is not a text file"));
        }

    }

    public Part getFile() {
        return file;
    }
//
    public void setFile(Part file) {
        this.file = file;
    }

}