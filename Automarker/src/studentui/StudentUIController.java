/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package studentui;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import automarker.Student;
//import java.awt.Color;
import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author flow
 */
public class StudentUIController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    Button login;
    @FXML
    Button btnUpload;
    @FXML
    TextField edtName;
    @FXML
    TextField edtStdNo;
    @FXML
    Hyperlink hStart;
    
    @FXML
    TabPane tabPane;
    VBox Vcontainer;
    @FXML
    AnchorPane anchor;
    @FXML
    SplitPane splitter;
    
    File selectedFile;
    Student student ;
    
   @FXML
   Label lblPath;
    
   @FXML
    private void uploadAction(ActionEvent event)
    {
        //System.out.println(btnUpload.getScene().toString());
        lblPath.setText("");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose File");
        fileChooser.getExtensionFilters().addAll( new FileChooser.ExtensionFilter("Lambda files", "*.lam"));
            selectedFile = fileChooser.showOpenDialog(btnUpload.getScene().getWindow());
        if (selectedFile != null)
        {
            lblPath.setText(selectedFile.getName());
        }
    }
    @FXML
    private void SetTabs(ActionEvent event){
           tabPane = new TabPane(); tabPane.prefHeight(372);tabPane.prefWidth(621);
                for (int i=0; i<4; i++)
                {
                    tabPane.getTabs().add(new QueTab(i));
                }
                anchor.getChildren().add(tabPane); //splitter.getItems().add(anchor);
                hStart.setVisible(false);
    }
    
    @FXML
    private void loginAction(ActionEvent event) {
        if (selectedFile!= null && !"".equals(edtStdNo.getText()) && !"".equals(edtName.getText())) 
        {
            student = new Student(edtStdNo.getText(), edtName.getText());
            Stage stage = (Stage)login.getScene().getWindow();
            try {
                Parent root = FXMLLoader.load(getClass().getResource("StudentUI.fxml"));
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException ex) {
                lblPath.setText("Error switching: "+ ex);
            }
            
            
        }
        else
        {
           lblPath.setText("Follow the instructs");
        }
            

        }

    @FXML
    private void addLine()
    {
        
       Vcontainer.getChildren().add(new vbExpr());
       System.out.println(Vcontainer.getChildren().size());
    }
    private class vbExpr extends VBox{
        HBox hbox ;
        
        Label lblError = new Label("Error Label");
        
        ObservableList<String> expr = FXCollections.observableArrayList("β", "α", "η", "→","=");
        ComboBox cbExpr;
        final TextField edtExpr, edtReason;

        private vbExpr() {
            this.cbExpr = new ComboBox(expr);
            cbExpr.setPrefSize(71, 24);
            this.edtExpr = new TextField();
            hbox = new HBox();
            hbox.setSpacing(10);
            edtExpr.setPromptText("Expression");
            edtExpr.setPrefSize( 364,22);
            edtReason = new TextField();
            edtReason.setPromptText("Reason");
            edtReason.setPrefWidth(131);
            lblError.setTextFill(Color.web("#cc3100"));
            hbox.getChildren().addAll(cbExpr,edtExpr,edtReason);
            this.getChildren().addAll(hbox,lblError);
            //this.getChildren().add(lblError);
            
       }
        
    }
    
    private class QueTab extends Tab{
        
        SplitPane split = new SplitPane();
        Pane pane;
        //orientation="VERTICAL" prefHeight="291.0" prefWidth="634.0"
    
 
        QueTab(int i){
            super("Question" +i );
            split.prefHeight(634);
            split.prefWidth(291);
            split.setOrientation(Orientation.VERTICAL);
            ScrollPane scroll = new ScrollPane();
            Vcontainer = new VBox();
            scroll.setContent(Vcontainer);
            split.getItems().addAll(init(),scroll);
            //split.getItems().add(scroll);
            this.setContent(split);
            //this.setContent(split);
            System.out.println(scroll.getChildrenUnmodifiable().contains(Vcontainer));
            
        }
            private Pane init(){
            pane = new Pane();
            pane.prefHeight(291); pane.prefWidth(600);
            pane.setLayoutX(-1);pane.setLayoutY(-6);
            
            TextArea edtInfo = new TextArea();
            edtInfo.setLayoutX(12); edtInfo.setLayoutY(18);
            edtInfo.setPrefHeight(44); edtInfo.setPrefWidth(576); edtInfo.setWrapText(true);
            Button btnAdd = new Button("Add Line");
            btnAdd.setOnAction(new EventHandler<ActionEvent>(){

                @Override
                public void handle(ActionEvent event) {
                    addLine(); //To change body of generated methods, choose Tools | Templates.
                }
            });
            btnAdd.setLayoutX(242); btnAdd.setLayoutY(64);
            Label lblExpr = new Label("EXpr"); lblExpr.setLayoutX(23); lblExpr.setLayoutY(66);
            pane.getChildren().addAll(edtInfo,lblExpr,btnAdd);
            return pane;
        }
       
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
