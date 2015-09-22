/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tutorui;

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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

/**
 *
 * @author flow
 */
public class FXMLDocumentController implements Initializable {
    
    File selectedFile = null;
    FileReader fr = null;
    Automarker marker;
    Student student;
    
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
            if ( (selectedFile!= null) && (selectedFile.getPath().endsWith(edtUpload.getText().trim())))
            {
                marker = new Automarker(selectedFile.getPath());
            }
            else
            {
                selectedFile = null;
               marker =  new Automarker(edtUpload.getText());
            }
            student = marker.result();
            if(student != null)
            {
                updateTable(student);
            } else {lblError.setText("error on file");}
            selectedFile = null;
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
            selectedFile = fileChooser.showOpenDialog(anchor.getScene().getWindow());
        if (selectedFile != null)
        {
            edtUpload.setText(selectedFile.getName());
        }
    }
    
    private void updateTable(Student stud)
    {
        students.add(stud);
        tblStudent.setItems(students);
    }
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
