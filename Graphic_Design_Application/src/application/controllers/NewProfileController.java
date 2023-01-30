package application.controllers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.SQLException;

import application.model.Model;
import application.model.User;
import application.utility.Authentication;
import application.utility.Picture;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class NewProfileController implements Controller {
	
	private Stage newProfileStage;
	private Stage parentStage;

	// Obtain Model singleton.
	Model model = Model.getInstance();
	
	// Obtain User singleton.
	User user = User.getInstance();

	// Graphical node elements and their fx:ids
    @FXML private ImageView profilePic_IMG;
	@FXML private Hyperlink profilePic_HL;
    @FXML private Button close_BTN;
    @FXML private Button createUser_BTN;
    @FXML private TextField username_TF;
    @FXML private TextField firstname_TF;
    @FXML private TextField lastname_TF;
    @FXML private PasswordField password_PF;
    @FXML private Label newUserMessage_LBL;

  
	/**
	 * TODO change documentation title - Logic for creating/authentication/validation of user accounts.
	 */
	@FXML
	public void initialize() {
		close_BTN.setOnAction(event -> {
			newProfileStage.close();
			parentStage.show();
		});
		
		
		// Allow a user to upload an image as their profile picture.
		profilePic_HL.setOnAction(event -> {
			// Open file chooser, convert selected image to byte array, set byte array in user singleton.
			try {
				user.setProfilePic(Picture.convertPicture(newProfileStage));
				
				// Set the profile picture box as the user's selected image.
				Image img = new Image(new ByteArrayInputStream(user.getProfilePic()));
				profilePic_IMG.setImage(img);
			
			} catch (IOException e) {
				newUserMessage_LBL.setText("Error: Unable to convert selected image to byte array.");
				newUserMessage_LBL.setTextFill(Color.RED);
				// Temporary console print - TODO REMOVE
				//e.printStackTrace();
			} catch (NullPointerException e) {
				newUserMessage_LBL.setText("Error: Please select an image before closing the dialog box.");
				newUserMessage_LBL.setTextFill(Color.RED);
				// Temporary console print - TODO REMOVE
				//e.printStackTrace();
			}
		});

		
		createUser_BTN.setOnAction(event -> {
			
			// Check if sign up fields are populated.
			if (!username_TF.getText().isEmpty() && !password_PF.getText().isEmpty() && !firstname_TF.getText().isEmpty() && !lastname_TF.getText().isEmpty()) {
				try {
					boolean userExists = model.getUserDao().checkUserExists(username_TF.getText());
					// Check if a username already exists in the database.
					if (!userExists) {
						
						// Hash password
						Authentication auth = new Authentication();
						String hash = auth.hashPassword(password_PF.getText());
						
						user = model.getUserDao().createUser(username_TF.getText(), hash, firstname_TF.getText(), lastname_TF.getText(), user.getProfilePic());
						if (user != null) {
							newUserMessage_LBL.setText("Successfully created new user: " + user.getUsername());
							newUserMessage_LBL.setTextFill(Color.GREEN);
						} else {
							newUserMessage_LBL.setText("Cannot create user");
							newUserMessage_LBL.setTextFill(Color.RED);
						}
					} else {
						newUserMessage_LBL.setText("Username " + username_TF.getText() + " is already taken.\nPlease enter a different username.");
						newUserMessage_LBL.setTextFill(Color.RED);
					}
				} catch (SQLException e) {
					newUserMessage_LBL.setText(e.getMessage());
					newUserMessage_LBL.setTextFill(Color.RED);
					// Temporary console print - TODO REMOVE
					e.printStackTrace();
				}
				
			} else {
				newUserMessage_LBL.setText("Please fill in all fields");
				newUserMessage_LBL.setTextFill(Color.RED);
			}
			
		});
		
	}
	
	
	/**
	 * Obtain the previous stage and model from other classes.
	 */
	public NewProfileController(Stage parentStage) {
		this.newProfileStage = new Stage();
		this.parentStage = parentStage;
	}
	
	
	/**
	 * Create a new 'NewProfile' scene and set stage settings
	 */
	@Override
	public void showStage(Pane root) {
		Scene scene = new Scene(root);
		newProfileStage.setScene(scene);
		newProfileStage.setResizable(false);
		newProfileStage.setTitle("Create a new user");
		newProfileStage.show();
	}
	

}
