package app;

import java.util.HashMap;

import entity.EVWord;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import popup.AdderDialog;
import popup.EditorDialog;
import popup.LongmanDialog;
import popup.WordListTable;
import storage.GoogleDrive;

public class MainApp extends Application {
	DragContext mapDragContext = new DragContext();
	static Stage primaryStage;

	public void start(Stage stage) {
		DoubleProperty doubleProperty = new SimpleDoubleProperty(0);
		primaryStage = stage;

		BorderPane root = new BorderPane();
		root.setPadding(new Insets(12));
		root.setStyle("-fx-background-color: null;");

		root.styleProperty()
				.bind(Bindings.concat("-fx-background-color: rgba(0, 0, 0, ").concat(doubleProperty).concat(");"));
		root.setEffect(new DropShadow(10, Color.GREY));

		// ----------------------------------------------------------
		Button btnLongman = new Button("Lm");
		Button btnSetting = new Button("St");
		Button btnAddNew = new Button("Ad");
		Button btnEdit = new Button("Ed");
		Button btnVocabularyList = new Button("Vl");

		HBox hb = new HBox();
		hb.getChildren().addAll(btnLongman, btnAddNew, btnEdit, btnVocabularyList, btnSetting);
		hb.setSpacing(2);
		hb.setAlignment(Pos.BASELINE_RIGHT);

		btnLongman.styleProperty().bind(Bindings.concat(
				"-fx-text-fill: red;-fx-font-size: 12;-fx-font-weight: bold;-fx-background-color: rgba(0, 255, 0, ")
				.concat(doubleProperty).concat(");"));
		btnLongman.setTooltip(new Tooltip("Go to Longman webside."));
		btnLongman.setOnAction(e -> FuncLongman());
		
		btnSetting.styleProperty().bind(Bindings.concat(
				"-fx-text-fill: red;-fx-font-size: 12;-fx-font-weight: bold;-fx-background-color: rgba(0, 255, 0, ")
				.concat(doubleProperty).concat(");"));
		btnSetting.setTooltip(new Tooltip("Setting."));
		
		btnAddNew.styleProperty().bind(Bindings.concat(
				"-fx-text-fill: red;-fx-font-size: 12;-fx-font-weight: bold;-fx-background-color: rgba(0, 255, 0, ")
				.concat(doubleProperty).concat(");"));
		btnAddNew.setTooltip(new Tooltip("Add new vocabulary."));
		btnAddNew.setOnAction(e -> FuncAddNew());
		
		btnEdit.styleProperty().bind(Bindings.concat(
				"-fx-text-fill: red;-fx-font-size: 12;-fx-font-weight: bold;-fx-background-color: rgba(0, 255, 0, ")
				.concat(doubleProperty).concat(");"));
		btnEdit.setTooltip(new Tooltip("Edit the vocabulary."));
		btnEdit.setOnAction(e -> FuncEditVocabulary());
		
		btnVocabularyList.styleProperty().bind(Bindings.concat(
				"-fx-text-fill: red;-fx-font-size: 12;-fx-font-weight: bold;-fx-background-color: rgba(0, 255, 0, ")
				.concat(doubleProperty).concat(");"));
		btnVocabularyList.setTooltip(new Tooltip("Show the vocabulary list."));
		btnVocabularyList.setOnAction(e -> FuncVocaList());

		// ---------------------------------------------------------
		Slider slider = new Slider(0, 1, .0);
		doubleProperty.bind(slider.valueProperty());
		slider.setTranslateY(0);

		root.setTop(hb);
		root.setCenter(AppContent.GetInstance().GetAppContent());
		root.setBottom(slider);

		primaryStage.initStyle(StageStyle.TRANSPARENT);
		primaryStage.setX(1140);
		primaryStage.setY(615);
		Scene scene = new Scene(root, 240, 170);
		scene.setFill(Color.TRANSPARENT);

		primaryStage.setTitle("English Popup");
		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.setAlwaysOnTop(true);

		primaryStage.getScene().setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				movingMainScreen(event);
			}
		});
		
		primaryStage.setOnCloseRequest(event -> {
	        Platform.exit();
	        System.exit(0);
		});
		
		stage.getIcons().add(new Image(GoogleDrive.class.getResourceAsStream("/iconapp.png")));
	}
	
	private void FuncLongman() {
		primaryStage.setAlwaysOnTop(false);
		HashMap<String, String> hmapVocaContent = AppContent.GetInstance().GetVocaContent();
		
		LongmanDialog puLongman = new LongmanDialog(hmapVocaContent.get(EVWord.DF_ORIGINAL));
		puLongman.showAndWait();
		puLongman = null;
		primaryStage.setAlwaysOnTop(true);
	}
	
	private void FuncAddNew() {
		primaryStage.setAlwaysOnTop(false);
		AdderDialog puAddVoca = new AdderDialog();
		puAddVoca.showAndWait();
		puAddVoca = null;
		primaryStage.setAlwaysOnTop(true);
	}
	
	private void FuncEditVocabulary() {
		primaryStage.setAlwaysOnTop(false);
		EditorDialog puEditVoca = new EditorDialog(AppContent.GetInstance().GetVocaContent());
		puEditVoca.showAndWait();
		puEditVoca = null;
		primaryStage.setAlwaysOnTop(true);
	}
	
	private void FuncVocaList() {
		primaryStage.setAlwaysOnTop(false);
		WordListTable puVocaList = new WordListTable();
		puVocaList.showAndWait();
		puVocaList = null;
		primaryStage.setAlwaysOnTop(true);
		
	}
	
	public static Stage GetPrimaryStage() {
		return primaryStage;
	}

	public static void main(String[] args) {
		launch(args);
	}

	class DragContext {
		double mouseAnchorX;
		double mouseAnchorY;

		double translateAnchorX;
		double translateAnchorY;
	}

	public void movingMainScreen(MouseEvent event) {
		if (event.isStillSincePress()) {
			mapDragContext.mouseAnchorX = event.getScreenX();
			mapDragContext.mouseAnchorY = event.getScreenY();

			mapDragContext.translateAnchorX = primaryStage.getX();
			mapDragContext.translateAnchorY = primaryStage.getY();
		} else {
			primaryStage.setX(mapDragContext.translateAnchorX + event.getScreenX() - mapDragContext.mouseAnchorX);
			primaryStage.setY(mapDragContext.translateAnchorY + event.getScreenY() - mapDragContext.mouseAnchorY);
		}
	}
}