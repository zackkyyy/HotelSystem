

/**
 * Created by zack on 2018-03-27.
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource(String.valueOf("/Untitled.fxml")));
		Scene scene = new Scene(root, 1024, 720);
		Stage stage= new Stage();
		stage.setTitle("Hotel Management");
		stage.setScene(scene);
		stage.setMinWidth(1024);
		stage.setMinHeight(740);
		//  primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("")));

		stage.show();
	}
}
