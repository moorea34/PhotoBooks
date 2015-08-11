package photobooks.application;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/*import photobooks.presentation.MainWindow;

public class PhotoBooks 
{
	public static void main(String[] args)
	{
		Globals.loadSettings();
		
		new MainWindow();
		
		//Clean when we're using DB
		Globals.getDao().dispose();
		
		System.out.println("Exiting PhotoBooks...");
	}
}*/

public class PhotoBooks extends Application
{
	public static void main(String[] args)
	{
		Globals.loadSettings();
		
		launch(args);
		
		//Clean when we're using DB
		Globals.getDao().dispose();
		
		System.out.println("Exiting PhotoBooks...");
	}

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Hello World!");
        Button btn = new Button();
        btn.setText("Say 'Hello World'");
        btn.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hello World!");
            }
        });
        
        StackPane root = new StackPane();
        root.getChildren().add(btn);
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
    }
}
