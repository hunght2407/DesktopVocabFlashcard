package app;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

import entity.EVWord;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import storage.XmlHelper;

public class AppContent extends VBox {
	private TextField tfLine1 = new TextField();
	private TextField tfLine2 = new TextField();
	private TextField tfLine3 = new TextField();
	private TextField tfLine4 = new TextField();
	
	private boolean blCurrentStarred;
	private LinkedList<EVWord> vocaList = new LinkedList<EVWord>();

	private static AppContent instance = null;

	public AppContent() {
		this.getChildren().addAll(tfLine1, tfLine2, tfLine3);

		tfLine1.styleProperty().bind(Bindings
				.concat("-fx-text-fill: yellow;-fx-font-size: 16;-fx-font-weight: bold;-fx-background-color: null;"));
		tfLine1.setEditable(false);

		tfLine2.styleProperty().bind(Bindings
				.concat("-fx-text-fill: blue;-fx-font-size: 14;-fx-font-weight: bold;-fx-background-color: null;"));
		tfLine2.setEditable(false);

		tfLine3.styleProperty().bind(Bindings.concat("-fx-font-size: 14;-fx-background-color: null;"));
		tfLine3.setEditable(false);

		XmlHelper.LoadVocabulary_v2(vocaList);

		Thread thrShowVocabulary = new Thread(rnShowVocabulary);
		thrShowVocabulary.start();
	}

	public VBox GetAppContent() {
		return this;
	}

	public LinkedList<EVWord> GetVocaList() {
		return vocaList;
	}

	public boolean AddNewVocabulary(String original, String phonetic, String vietnamese, String example) {

		for (int i = 0; i < vocaList.size(); i++) {
			if (vocaList.get(i).getOriginal().equals(original))
				return false;
		}

		EVWord vocaTmp = new EVWord(original, phonetic, vietnamese, example);
		vocaList.add(vocaTmp);
		XmlHelper.saveVocabulary_v2(vocaList);

		return true;
	}

	public boolean RemoveVocabulary(String word) {

		for (int i = 0; i < vocaList.size(); i++) {
			if (vocaList.get(i).getOriginal().equals(word)) {
				vocaList.remove(i);
				XmlHelper.saveVocabulary_v2(vocaList);
				return true;
			}
		}
		return false;
	}

	public HashMap<String, String> GetVocaContent() {
		HashMap<String, String> hmapVocaContent = new HashMap<String, String>();

		hmapVocaContent.put(EVWord.DF_ORIGINAL, tfLine1.getText());
		hmapVocaContent.put(EVWord.DF_PHONETIC, tfLine2.getText());
		hmapVocaContent.put(EVWord.DF_VIETNAMESE, tfLine3.getText());
		hmapVocaContent.put(EVWord.DF_EXAMPLE, tfLine4.getText());
		
		if (blCurrentStarred)
			hmapVocaContent.put(EVWord.DF_FLAG, "1");
		else
			hmapVocaContent.put(EVWord.DF_FLAG, "0");

		return hmapVocaContent;
	}

	public static AppContent GetInstance() {
		if (instance == null) {
			instance = new AppContent();
		}

		return instance;
	}

	// Runnable Tasks...
	Runnable rnShowVocabulary = new Runnable() {
		Random rand = new Random();
		int i;

		public void run() {
			while (true) {
				i = rand.nextInt(vocaList.size());

				if (!vocaList.get(i).getFlag()) {
					for (; i < vocaList.size(); i++) {
						if (vocaList.get(i).getFlag()) {
							break;
						}
					}

					if (i >= vocaList.size()) {
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
						}
						continue;
					}
				}

				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						tfLine1.setText(vocaList.get(i).getOriginal());
						tfLine2.setText(vocaList.get(i).getPhonetic());
						tfLine3.setText(vocaList.get(i).getVietnamese());
						tfLine4.setText(vocaList.get(i).getExample());
						blCurrentStarred = vocaList.get(i).getFlag();
					}
				});

				try {
					Thread.sleep(15000);
				} catch (InterruptedException e) {
				}
			}
		}
	};
}
