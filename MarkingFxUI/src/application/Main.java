package application;
	
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;


public class Main extends Application {
	File selectedFile = null;
	FileReader fr = null;
	@Override
	public void start(Stage primaryStage) {
		try {
			//BorderPane root = new BorderPane();
			
			GridPane grid = new GridPane();
			grid.setAlignment(Pos.CENTER);
			grid.setHgap(10); grid.setVgap(10);
			grid.setPadding(new Insets(25,25,20,1));
			//grid.setGridLinesVisible(true);
			
			//System.out.println(Main.class.getResource("~/lambda(1).png"));
			//primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("lambda(1).png")));
			
			//Adding Widgets
			Text titleLabel = new Text("AutoLam");
			titleLabel.setFont(Font.font(50));
			grid.add(titleLabel, 0, 0, 2, 1);
			Label lblUpload = new Label("Upload File");
			grid.add(lblUpload, 0, 1);
			TextField edtUpload = new TextField();
			grid.add(edtUpload, 1 , 1, 2,1);
			Label lblError = new Label("");
			grid.add(lblError, 1 , 2);
			Button btnChooser = new Button("Choose File");
			Button btnSubmit = new Button("Submit");
			HBox hbBtn = new HBox(10);
			hbBtn.getChildren().add(btnChooser);
			hbBtn.getChildren().add(btnSubmit);
			grid.add(hbBtn, 1, 3);
			
			 
			//Action Handlers
			btnSubmit.setOnAction(new EventHandler<ActionEvent>() {
				 
			    @Override
			    public void handle(ActionEvent e) {
			    	if (edtUpload.getText().trim().isEmpty()){
			    		lblError.setTextFill(Color.FIREBRICK);
				        lblError.setText("No File chosen");
			    	}
			    	else if (!edtUpload.getText().endsWith(".txt")){
			    		
			        lblError.setTextFill(Color.FIREBRICK);
			        lblError.setText("Unsupported file format");
			        }
			    	else
			    	{
			    		
			    		
				        try {
				        	if ( (selectedFile!= null) && (selectedFile.getPath().endsWith(edtUpload.getText().trim())))
				        	{
				        		fr =  new FileReader(selectedFile.getPath().toString());
				        	}
				        	else
				        	{
				        		selectedFile = null;
				        		fr =  new FileReader(edtUpload.getText());
				        	}
				        BufferedReader reader =  new BufferedReader(fr);
				        lblError.setTextFill(Color.FORESTGREEN);
				        lblError.setText("Ready for next script...");
				        BufferedWriter writer;
				        try{
				        	String StdNo =reader.readLine();
				            writer = new BufferedWriter(new FileWriter(new File("Report"+StdNo+".txt"), false));	//new writer
				            writer.close();
				            
				        } catch (Exception ex) {
				            System.err.print("ReadError: " + ex.toString());
				        } 
				        }
				        /**
				         *   Send stuff to the Calculator line by line and produce a 
				         * report as output that takes the student number
				         * given and adds .txt to it
				         * */
				       
				        catch (Exception ee ){
				        	lblError.setTextFill(Color.FIREBRICK);
					        lblError.setText("Error Reading File!");
				        }
			    	}
			    	
			    }
			});
			
			btnChooser.setOnAction(new EventHandler<ActionEvent>()
					{

						@Override
						public void handle(ActionEvent arg0) {
							lblError.setText("");
							FileChooser fileChooser = new FileChooser();
							 fileChooser.setTitle("Choose iFile");
							 fileChooser.getExtensionFilters().addAll(
							         new ExtensionFilter("Text Files", "*.txt"));
							 selectedFile = fileChooser.showOpenDialog(primaryStage);
							 if (selectedFile != null)
							 {
								 
								 edtUpload.setText(selectedFile.getName().toString());
								 lblError.setTextFill(Color.GRAY);
								 lblError.setText(selectedFile.getPath());
							 }
							
						}}
			);
			Scene scene = new Scene(grid,600,400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Tutor View");
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
 		launch(args);
	}
}
