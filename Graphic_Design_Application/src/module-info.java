module Test1 {
	requires java.sql;
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.graphics;
	requires junit;
	requires org.xerial.sqlitejdbc;
	requires java.desktop;
	
	opens application to javafx.graphics, javafx.fxml, java.sql;
	opens application.controllers to javafx.graphics, javafx.fxml, javafx.controls, java.sql;
	opens application.model to javafx.graphics, javafx.fxml, javafx.controls, java.sql;
	exports application.controllers;
}
