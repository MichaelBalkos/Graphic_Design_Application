package application.model;

import java.sql.SQLException;
import java.util.ArrayList;

import application.dao.UserDao;
import application.dao.UserDaoImpl;
import javafx.scene.Node;
import javafx.stage.Stage;


public class Model {
	private UserDao userDao;
	private Double canvasWidth;
	private Double canvasHeight;
	private Boolean canvasChange;
	private Node selectedNode;
	private Stage currentStage;
	// SQLite doesn't support boolean data types, so 'int' is used instead.
	private int isPremium;
	
	// Singleton implementation - Restrict instantiation of class from other classes.
	private Model() {
		userDao = new UserDaoImpl();
	}

	/**
	 * Singleton Pattern - Only a single instance of the Model class should exist.
	 * Inner helper class for Bill Pugh type implementation.
	 * Does not require synchronization (better performance), yet multithreading safe.
	 */
	private static class InnerSingletonModelClass {
		private static final Model model = new Model();
	}
	
	/**
	 * Singleton implementation - Public method for other classes to obtain the class instance.
	 */
	public static Model getInstance() {
		return InnerSingletonModelClass.model;
	}
	
	
	// Store nodes on the canvas
	//private ArrayList<Object> node = new ArrayList<Object>();
	private ArrayList<Node> node = new ArrayList<Node>();
	
	public void initDao() {
	}
	
	public void setup() throws SQLException {
		userDao.setup();
	}
	public UserDao getUserDao() {
		return userDao;
	}
	
	// Node ArrayList
	public ArrayList<Node> getNode() {
		return node;
	}
	
	// Canvas
	public Double getCanvasWidth() {
		return canvasWidth;
	}
	public void setCanvasWidth(Double canvasWidth) {
		this.canvasWidth = canvasWidth;
	}
	public Double getCanvasHeight() {
		return canvasHeight;
	}
	public void setCanvasHeight(Double canvasHeight) {
		this.canvasHeight = canvasHeight;
	}
	public Boolean getCanvasChange() {
		return canvasChange;
	}
	public void setCanvasChange(Boolean canvasChange) {
		this.canvasChange = canvasChange;
	}
	
	// Node
	public Node getSelectedNode() {
		return selectedNode;
	}
	public void setSelectedNode(Node selectedNode) {
		this.selectedNode = selectedNode;
	}
	
	// Stage
	public Stage getCurrentStage() {
		return currentStage;
	}
	public void setCurrentStage(Stage currentStage) {
		this.currentStage = currentStage;
	}

	// Premium membership
	public int getIsPremium() {
		return isPremium;
	}
	public void setIsPremium(int isPremium) {
		this.isPremium = isPremium;
	}
}
