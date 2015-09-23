/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package studentui;

import automarker.Question;
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
import automarker.TutorialInterface;
import automarker.Parser;
import automarker.Line;
import automarker.Solution;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Node;
import javafx.scene.control.Alert.AlertType;

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
    Button btnSubmit;
    @FXML
    TextField edtName;
    @FXML
    TextField edtStdNo;
    @FXML
    Hyperlink hStart;
    static List<Question> questions;
    @FXML
    TabPane tabPane;
    VBox Vcontainer;
    @FXML
    AnchorPane anchor;
    @FXML
    SplitPane splitter;
    TutorialInterface ti;
    File selectedFile;
    Student student ;
    Solution[] solutions;
    
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
           tabPane = new TabPane(); //tabPane.prefHeight(372);tabPane.prefWidth(621);
                for (Question question: questions)
                {
                    tabPane.getTabs().add(new QueTab(question));
                }
                anchor.getChildren().add(tabPane); //splitter.getItems().add(anchor);
                hStart.setVisible(false);
                btnSubmit.setVisible(true);
    }
    
    @FXML
    private void loginAction(ActionEvent event) {
        if (selectedFile!= null && !"".equals(edtStdNo.getText()) && !"".equals(edtName.getText())) 
        {
            student = new Student(edtStdNo.getText(), edtName.getText());
            ti = new TutorialInterface(student);
             questions = ti.loadQuestions(selectedFile.getPath());
            
            setup("StudentUI.fxml");
            
        }
        else
        {
           lblPath.setText("Follow the instructions");
        }
            

        }
 
   
    private class vbExpr extends VBox{
        HBox hbox ;
        
        Label lblError = new Label();
        
        ObservableList<String> expr = FXCollections.observableArrayList("β", "α", "η", "→","=");
        ComboBox cbExpr;
        final TextField edtExpr, edtReason;

        private vbExpr() {
            this.cbExpr = new ComboBox(expr);
            cbExpr.setValue(expr.get(0));
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
            edtExpr.setOnAction(new EventHandler<ActionEvent>(){

                @Override
                public void handle(ActionEvent event) { 
                    lblError.setText("");
                    String str = edtExpr.getText();
                    try {
                        Parser.parse(str);
                    } catch (Parser.ParseException ex) {
                        lblError.setText(ex.getMessage());
                    }
                }
            });
            //this.getChildren().add(lblError);
       }
       public Line getLine(){
            String str = (String)cbExpr.getValue();
            char reductionChar = str.charAt(0);
            Line line = new Line(edtExpr.getText(),reductionChar,edtReason.getText());
            return line;
        }
        
    }
    
    private class QueTab extends Tab{
        
        SplitPane split = new SplitPane();
        VBox pane;
        Question question;
        VBox mycontainer;
        Line[] answers;
        //orientation="VERTICAL" prefHeight="291.0" prefWidth="634.0"
    
 
        QueTab(Question question){
            super("Question " +question.getId() );
            System.out.println("Question: "+question.getId());
            this.question= question;
            //split.prefHeight(634);
            //split.prefWidth(391);
            split.setOrientation(Orientation.VERTICAL);
            ScrollPane scroll = new ScrollPane();
            mycontainer = new VBox();
            mycontainer.getChildren().add(new vbExpr());
            scroll.setContent(mycontainer);
            scroll.prefHeight(390); scroll.prefWidth(700);
            split.getItems().addAll(init(),scroll);
            this.setContent(split);
        }
        
            private Pane init(){
            pane = new VBox();
            pane.prefHeight(391); pane.prefWidth(800);
            //pane.setLayoutX(-1);pane.setLayoutY(-6);
            
            
            TextArea edtInfo = new TextArea();
            edtInfo.setText("Description: "+question.getDescription()+"\nRequirements: "+"\n" + question.getMaxMark()+" mark(s)");
            edtInfo.setLayoutX(12); edtInfo.setLayoutY(18);
            //edtInfo.setPrefHeight(100); edtInfo.setPrefWidth(576); 
            edtInfo.setWrapText(true);
            Button btnAdd = new Button("Add Line");
            btnAdd.setOnAction(new EventHandler<ActionEvent>(){

                @Override
                public void handle(ActionEvent event) {
                    mycontainer.getChildren().add(new vbExpr()); //To change body of generated methods, choose Tools | Templates.
                }
            });
            btnAdd.setLayoutX(242); btnAdd.setLayoutY(64);
            Label lblExpr = new Label(question.getStart()); lblExpr.setLayoutX(23); lblExpr.setLayoutY(66);
            HBox h = new HBox();
            h.getChildren().addAll(lblExpr,btnAdd);
            h.setSpacing(30);
            pane.getChildren().addAll(edtInfo,h);
            return pane;
        }
        public Line[] Answer()
        {
            int j= mycontainer.getChildren().size();
            answers = new Line[j];
            for (int i=0; i<j;i++)
            {
                 answers[i]= ((vbExpr)(mycontainer.getChildren().get(i))).getLine();
            }
            return answers;
        }
       
    }
    
    private void setup(String fxml){
    Stage stage = (Stage)login.getScene().getWindow();
            try {
                Parent root = FXMLLoader.load(getClass().getResource(fxml));
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException ex) {
                lblPath.setText("Error switching: "+ ex);
            }}
    
    private Boolean confirm()
    {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirm Submission");
        alert.setHeaderText("Are you sure you want to submit?");
        alert.setContentText("Make sure you have checked your work before you submit.\nClick OK to Submit");
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
        return true;}else{return false;}
                
        //return true;
    }
    
    @FXML
    private void submit(ActionEvent event){
        if (confirm()){
        int size = tabPane.getTabs().size();
        solutions = new Solution[size];
        for(int j =0; j<size;j++)
        {
            QueTab q = (QueTab)(tabPane.getTabs().get(j));
            solutions[j] = new Solution(q.question,q.Answer());
        }
        
        ti.solutions = solutions;
        ti.saveSolutions();
        //setup("Login.fxml");
        
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
