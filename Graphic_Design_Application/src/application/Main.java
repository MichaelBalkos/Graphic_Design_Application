package application;

import java.io.IOException;
import java.sql.SQLException;

import application.controllers.LoginController;
import application.model.Model;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;

public class Main extends Application {

	// Obtain Model singleton.
	Model model = Model.getInstance();

	@Override
	public void init() {
	}

	@Override
	public void start(Stage primaryStage) {
		try {
			model.setup();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/views/LoginView.fxml"));

			// Obtain a new LoginController instance
			Callback<Class<?>, Object> controllerFactory = param -> {
				return new LoginController(primaryStage);
			};

			loader.setControllerFactory(controllerFactory);
			AnchorPane root = loader.load();

			LoginController loginController = loader.getController();
			loginController.showStage(root);

		} catch (IOException | SQLException | RuntimeException e) {
			// Display error in a new window, as login screen isnt displayed yet.
			Scene scene = new Scene(new Label(e.getMessage()), 200, 100);
			primaryStage.setTitle("Error");
			primaryStage.setScene(scene);
			primaryStage.show();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
