package application.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Window;

public class Picture {

	/**
	 * Open file chooser window, then convert selected image to byte array.
	 * Pass exception to caller so they can display error messages to the user.
	 * @param stage - The currently viewed javafx stage.
	 * @return - Image in byte array format
	 */
	public static byte[] convertPicture(Window stage) throws IOException, NullPointerException {

		FileChooser fileChooser = new FileChooser();

		// Set extension filter.
		ExtensionFilter extFilter = new FileChooser.ExtensionFilter("*.png", "*.jpg", "*.jpeg");
		fileChooser.getExtensionFilters().add(extFilter);

		FileInputStream inputStream = null;
		byte[] imageByte = null;

		try {
			// Show a file open dialog.
			File selectedFile = fileChooser.showOpenDialog(stage);

			// Initialize a byte array of size of the image.
			imageByte = new byte[(int) selectedFile.length()];

			// Create a new input stream directed to the file.
			inputStream = new FileInputStream(selectedFile);

			// Read the contents of file to byte array
			inputStream.read(imageByte);
		} catch (IOException | NullPointerException e) {
			// Notify caller of the exception for them to handle it.
			throw e;
		} finally {
			// Close the input stream.
			if (inputStream != null) {
				inputStream.close();
			}
		}
		return imageByte;
	}
}
