package popup;

import java.util.HashMap;
import java.util.LinkedList;

import app.AppContent;
import entity.EVWord;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import storage.XmlHelper;

public class EditorDialog extends Dialog<Boolean> {
	private ButtonType btRemove;
	private ButtonType btOK;
	private TextField tfOriginal;
	private TextField tfPhonetic;
	private TextField tfVietnamese;
	private TextArea taExample;
	private CheckBox cbStarred;

	public EditorDialog(HashMap<String, String> hmapVocaContent) {
		btRemove = new ButtonType("Remove", ButtonData.OTHER);
		btOK = new ButtonType("OK", ButtonData.OK_DONE);
		tfOriginal = new TextField(hmapVocaContent.get(EVWord.DF_ORIGINAL));
		tfPhonetic = new TextField(hmapVocaContent.get(EVWord.DF_PHONETIC));
		tfVietnamese = new TextField(hmapVocaContent.get(EVWord.DF_VIETNAMESE));
		taExample = new TextArea(hmapVocaContent.get(EVWord.DF_EXAMPLE));
		
		taExample.setMaxSize(320, 80);

		cbStarred = new CheckBox();
		cbStarred.setTooltip(new Tooltip("Is marked."));

		if (hmapVocaContent.get(EVWord.DF_FLAG).equals("1"))
			cbStarred.setSelected(true);
		else
			cbStarred.setSelected(false);

		getDialogPane().getButtonTypes().addAll(btRemove, btOK, ButtonType.CANCEL);
		setTitle("Edit the vocabulary.");

		setResultConverter(dialogButton -> {
			if (dialogButton == btOK) {
				if (!tfOriginal.getText().isEmpty() && !tfPhonetic.getText().isEmpty()
						&& !tfVietnamese.getText().isEmpty()) {

					EditSelectedVocabulary(tfOriginal.getText(), tfPhonetic.getText(), tfVietnamese.getText(), taExample.getText());
					return true;
				}
			} else if (dialogButton == btRemove) {
				if (!tfOriginal.getText().isEmpty() && !tfPhonetic.getText().isEmpty()
						&& !tfVietnamese.getText().isEmpty()) {

					RemoveSelectedVocabulary(tfOriginal.getText());
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
		grid.add(new Label("Marked:"), 0, 4);

		grid.add(tfOriginal, 1, 0);
		grid.add(tfPhonetic, 1, 1);
		grid.add(tfVietnamese, 1, 2);
		grid.add(taExample, 1, 3);
		grid.add(cbStarred, 1, 4);

		getDialogPane().setContent(grid);
	}

	private boolean EditSelectedVocabulary(String original, String phonetic, String vietnamese, String example) {
		LinkedList<EVWord> vocaList = AppContent.GetInstance().GetVocaList();

		for (int i = 0; i < vocaList.size(); i++) {
			if (vocaList.get(i).getOriginal().equals(original)
					|| vocaList.get(i).getPhonetic().equals(phonetic)
					|| vocaList.get(i).getVietnamese().equals(vietnamese)) {

				vocaList.get(i).setOriginal(original);
				vocaList.get(i).setPhonetic(phonetic);
				vocaList.get(i).setVietnamese(vietnamese);
				vocaList.get(i).setExample(example);

				if (cbStarred.isSelected())
					vocaList.get(i).setFlag(true);
				else
					vocaList.get(i).setFlag(false);

				XmlHelper.saveVocabulary_v2(vocaList);
				return false;
			}
		}

		return true;
	}

	private boolean RemoveSelectedVocabulary(String original) {
		LinkedList<EVWord> vocaList = AppContent.GetInstance().GetVocaList();

		for (int i = 0; i < vocaList.size(); i++) {
			if (vocaList.get(i).getOriginal().equals(original)) {
				vocaList.remove(i);

				XmlHelper.saveVocabulary_v2(vocaList);
				return false;
			}
		}

		return true;
	}
}
