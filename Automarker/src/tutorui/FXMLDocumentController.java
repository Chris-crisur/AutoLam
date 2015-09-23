/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tutorui;

import automarker.AnalyticsReport;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import automarker.Automarker;
import automarker.Student;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

/**
 *
 * @author flow
 */
public class FXMLDocumentController implements Initializable {
    
    List <File> selectedFiles = null;
    FileReader fr = null;
    Automarker marker;
    Student student;
    AnalyticsReport ar;
    
    @FXML
    private Label lblError;
    @FXML
    private TextField edtUpload;
    @FXML
    private AnchorPane anchor;
    @FXML
    private TableView <Student> tblStudent;
    
    ObservableList<Student> students = FXCollections.observableArrayList();
   
    
    //tblStudent.getItems().add()
    @FXML
    private void submitAction(ActionEvent event) {
        if (edtUpload.getText().trim().isEmpty()){
                lblError.setText("No File chosen");
        }
        else if (!edtUpload.getText().endsWith(".lam")){
        lblError.setText("Unsupported file format");
        }
        else
        {
            if (selectedFiles!= null) 
            {
                for (File selected : selectedFiles){
                marker = new Automarker(selected.getPath());
                student = marker.result();
                if(student != null)
                {
                    updateTable(student);
                } else {lblError.setText("error on file");}
                
                }
            } 
            edtUpload.clear();

        }

    }
    
    @FXML
    private void uploadAction(ActionEvent event)
    {
        lblError.setText("");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose File");
        fileChooser.getExtensionFilters().addAll( new ExtensionFilter("Lambda files", "*.lam"));
            selectedFiles = fileChooser.showOpenMultipleDialog(anchor.getScene().getWindow());
        for (File selected : selectedFiles){
            edtUpload.setText(selected.getName());}
    }
    
    private void updateTable(Student stud)
    {
        students.add(stud);
        tblStudent.setItems(students);
        ar.addStudent(stud.getStudentNum(),stud.getMark());
    }
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
	String date = sdf.format(new Date());
        ar = new AnalyticsReport(date);
    }    
    
}
