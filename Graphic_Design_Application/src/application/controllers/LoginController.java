package application.controllers;

import java.io.IOException;
import java.sql.SQLException;

import application.model.Model;
import application.model.User;
import application.utility.Authentication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;

public class LoginController implements Controller {

	// Obtain Model singleton.
	Model model = Model.getInstance();
	
	// Obtain User singleton.
	User user = User.getInstance();
	
	private Stage loginStage;
	
	// Graphical node elements and their fx:ids
	@FXML private Label loginMessage_LBL;
	@FXML private TextField username_TF;
	@FXML private PasswordField password_PF;
	@FXML private Button signin_BTN;
	@FXML private Button close_BTN;
	@FXML private Hyperlink newUserLink_HL;
	
	
	/**
	 * Contains event handlers for login scene nodes
	 */
	@FXML
	public void initialize() {

		// Logic containing authentication and validation of login details.
		signin_BTN.setOnAction(event -> {

			if (!username_TF.getText().isEmpty() && !password_PF.getText().isEmpty()) {
				try {

					// Hash password
					Authentication auth = new Authentication();
					String hash = auth.hashPassword(password_PF.getText());

					// Check if the users login details match the database.
					user = model.getUserDao().loginUser(username_TF.getText(), hash);
					if (user != null) {
						try {
							FXMLLoader loader = new FXMLLoader(
									getClass().getResource("/resources/views/BoardView.fxml"));

							// Pass the current stage and model to a new BoardController class
							Callback<Class<?>, Object> controllerFactory = param -> {
								return new BoardController(loginStage);
							};

							loader.setControllerFactory(controllerFactory);
							VBox pane = loader.load();

							// Return the controller associated with the pane object.
							BoardController boardController = loader.getController();
							boardController.showStage(pane);

							loginStage.close();
						} catch (IOException e) {
							// Display the exception
							loginMessage_LBL.setText(e.getMessage());
						}
					} else {
						loginMessage_LBL.setText("Incorrect username or password");
						loginMessage_LBL.setTextFill(Color.RED);
					}
				} catch (SQLException e) {
					loginMessage_LBL.setText(e.getMessage());
					loginMessage_LBL.setTextFill(Color.RED);
				}
			} else {
				loginMessage_LBL.setText("Please enter both a username and password");
				loginMessage_LBL.setTextFill(Color.RED);
			}
			username_TF.clear();
			password_PF.clear();
		});

		// Opens the new profile scene and closes the current scene
		newUserLink_HL.setOnAction(event -> {

			try {
				// Create a new FXMLLoader instance.
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/views/NewProfileView.fxml"));

				// Pass the current stage and model a new NewProfileController class
				Callback<Class<?>, Object> controllerFactory = param -> {
					return new NewProfileController(loginStage);
				};
				loader.setControllerFactory(controllerFactory);
				AnchorPane pane = loader.load();

				// Return the controller associated with the pane object.
				NewProfileController newProfileController = loader.getController();
				newProfileController.showStage(pane);

				loginStage.close();
			} catch (IOException e) {
				// Display the error message.
				loginMessage_LBL.setText(e.getMessage());
			}
		});

		// Terminates the program if the user clicks the close button.
		close_BTN.setOnAction(event -> {
			loginStage.close();
		});

		// Reset the login message upon showing the login stage.
		loginStage.setOnShowing(event -> {
			loginMessage_LBL.setText("");
		});
	}

	
	/**
	 * Obtain the stage and model from other classes.
	 */
	public LoginController(Stage stage) {
		this.loginStage = stage;
	}

	
	/**
	 * Create a new login scene and set stage settings
	 */
	@Override
	public void showStage(Pane root) {
		Scene scene = new Scene(root, 500, 300);
		scene.getStylesheets().add(getClass().getResource("/resources/stylesheets/Login.css").toExternalForm());
		loginStage.setScene(scene);
		loginStage.setResizable(false);
		loginStage.setTitle("Welcome to SmartCanvas!");
		loginStage.show();
	}
}
