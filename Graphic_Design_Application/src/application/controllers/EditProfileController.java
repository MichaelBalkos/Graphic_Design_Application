package application.controllers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.SQLException;

import application.model.Model;
import application.model.User;
import application.utility.Picture;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class EditProfileController implements Controller {
	
	// Obtain Model singleton.
	Model model = Model.getInstance();
	
	// Obtain User singleton.
	User user = User.getInstance();
	
	private Stage editProfileStage;
	private Stage parentStage;
	
	// Graphical node elements and their fx:ids
	@FXML private ImageView profilePic_IMG;
	@FXML private Hyperlink profilePic_HL;
    @FXML private Button cancel_BTN;
    @FXML private Button ok_BTN;
    @FXML private TextField username_TF;
    @FXML private TextField firstname_TF;
    @FXML private TextField lastname_TF;
    @FXML private Label message_LBL;
    @FXML private ToggleButton subscribe_TB;

    
	/**
	 * Logic and handlers for editing user's profile information.
	 */
	@FXML
	public void initialize() {

		// Turn off username textbox functionality to act like a label.
		username_TF.setEditable(false);
		username_TF.setMouseTransparent(true);
		username_TF.setFocusTraversable(false);

		// Pre-fill user information.
		username_TF.setText(user.getUsername());
		firstname_TF.setText(user.getFirstName());
		lastname_TF.setText(user.getLastName());

		if (user.getProfilePic() != null && user.getProfilePic().length > 0) {
			// Set the profile picture box as the user's selected image.
			Image img = new Image(new ByteArrayInputStream(user.getProfilePic()));
			profilePic_IMG.setImage(img);
		}

		// Initialize setting user's premium status.
		int premium = model.getIsPremium();
		if (premium == 1) {
			subscribe_TB.setSelected(true);
		} else {
			subscribe_TB.setSelected(false);
		}
		
		// Take user back to canvas editor.
		cancel_BTN.setOnAction(event -> {
			editProfileStage.close();
			parentStage.show();
		});

		// Allow a user to upload an image as their profile picture.
		profilePic_HL.setOnAction(event -> {
			// Open file chooser, convert selected image to byte array, set byte array in user singleton.
			try {
				user.setProfilePic(Picture.convertPicture(editProfileStage));

				// Set the profile picture box as the user's selected image.
				Image img = new Image(new ByteArrayInputStream(user.getProfilePic()));
				profilePic_IMG.setImage(img);

			} catch (IOException e) {
				message_LBL.setText("Error: Unable to convert selected image to byte array.");
				message_LBL.setTextFill(Color.RED);
			} catch (NullPointerException e) {
				message_LBL.setText("Error: Please select an image before closing the dialog box.");
				message_LBL.setTextFill(Color.RED);
			}
		});

		// Update user details then go back to canvas editor.
		ok_BTN.setOnAction(event -> {
			// Check if the first or last name fields are populated.
			if (!firstname_TF.getText().trim().equals("") && !lastname_TF.getText().trim().equals("")) {
				try {
					// Update the user's details database.
					model.getUserDao().updateUser(user.getUsername(), firstname_TF.getText(), lastname_TF.getText(),
							user.getProfilePic());

					// Update the local user's details.
					user.setFirstName(firstname_TF.getText());
					user.setLastName(lastname_TF.getText());

					// Take user back to canvas editor.
					editProfileStage.close();
					parentStage.show();

				} catch (SQLException e) {
					message_LBL.setText(e.getMessage());
					message_LBL.setTextFill(Color.RED);
				}
			} else {
				message_LBL.setText("First or last name cannot be blank.");
				message_LBL.setTextFill(Color.RED);
			}
		});

		
		// Update user if they are a premium subscriber or not.
		subscribe_TB.setOnAction(event -> {
			if (subscribe_TB.isSelected()) {
				model.setIsPremium(1);
				try {
					model.getUserDao().updateMembership(user.getUsername(), 1);
				} catch (SQLException e) {
					message_LBL.setText(e.getMessage());
					message_LBL.setTextFill(Color.RED);
				}
			} else if (!subscribe_TB.isSelected()) {
				model.setIsPremium(0);
				try {
					model.getUserDao().updateMembership(user.getUsername(), 0);
				} catch (SQLException e) {
					message_LBL.setText(e.getMessage());
					message_LBL.setTextFill(Color.RED);
				}
			}
		});
		
	}

	/**
	 * Obtain the previous stage and model from other classes.
	 */
	public EditProfileController(Stage parentStage) {
		this.editProfileStage = new Stage();
		this.parentStage = parentStage;
	}
	
	/**
	 * Create a new login scene and set stage settings
	 */
	@Override
	public void showStage(Pane root) {
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/resources/stylesheets/Board.css").toExternalForm());
		editProfileStage.setScene(scene);
		editProfileStage.setResizable(false);
		editProfileStage.setTitle("Edit profile");
		editProfileStage.show();
	}
}