package popup;


import app.AppContent;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class AdderDialog extends Dialog<Boolean> {

	public AdderDialog() {
		ButtonType loginButtonType = new ButtonType("OK", ButtonData.OK_DONE);
		TextField tfOriginal = new TextField();
		TextField tfPhonetic = new TextField();
		TextField tfVietnamese = new TextField();
		TextArea taExample = new TextArea();
		
		taExample.setMaxSize(320, 80);

		getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);
		setTitle("Add a new word.");

		setResultConverter(dialogButton -> {
			if (dialogButton == loginButtonType) {
				if (!tfOriginal.getText().isEmpty() && !tfPhonetic.getText().isEmpty()
						&& !tfVietnamese.getText().isEmpty()) {

					if (AppContent.GetInstance().AddNewVocabulary(
							tfOriginal.getText(), 
							tfPhonetic.getText(), 
							tfVietnamese.getText(),
							taExample.getText()))
							return true;
				}
			}

			return false;
		});

		GridPane grid = new GridPane();
		grid.setPadding(new Insets(10, 30, 10, 30));
		grid.setHgap(20);
		grid.setVgap(5);

		grid.add(new Label("Original:"), 0, 0);
		grid.add(new Label("Phonetic:"), 0, 1);
		grid.add(new Label("Vietnamese:"), 0, 2);
		grid.add(new Label("Example:"), 0, 3);

		grid.add(tfOriginal, 1, 0);
		grid.add(tfPhonetic, 1, 1);
		grid.add(tfVietnamese, 1, 2);
		grid.add(taExample, 1, 3);

		getDialogPane().setContent(grid);
	}
}
