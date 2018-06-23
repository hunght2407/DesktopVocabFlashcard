package popup;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

import com.google.api.services.drive.Drive;

import app.AppContent;
import app.MainApp;
import entity.EVWord;
import entity.EVWordTable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import storage.GoogleDrive;

public class WordListTable extends Dialog<Boolean> {
	private TableView<EVWordTable> tvVocaTable;
	private ObservableList<EVWordTable> obsVocaList;

	@SuppressWarnings("unchecked")
	public WordListTable() {
		ButtonType btnGDrive = new ButtonType("Update to Drive", ButtonData.OK_DONE);
		obsVocaList = FXCollections.observableArrayList(new EVWordTable("Vocabulary", false, 0));
		obsVocaList.clear();
		AddVocabularyToObsList(obsVocaList);
		
		TableColumn<EVWordTable, Integer> index = new TableColumn<EVWordTable, Integer>("index");
		index.setCellValueFactory(new PropertyValueFactory<EVWordTable, Integer>("idxVoca"));
		index.setPrefWidth(36);

		TableColumn<EVWordTable, String> vocabulary = new TableColumn<EVWordTable, String>("Vocabulary");
		vocabulary.setCellValueFactory(new PropertyValueFactory<EVWordTable, String>("txtVocabulary"));
		vocabulary.setPrefWidth(154);

		TableColumn<EVWordTable, String> starred = new TableColumn<EVWordTable, String>("Starred");
		starred.setCellValueFactory(new PropertyValueFactory<EVWordTable, String>("txtStarred"));
		starred.setPrefWidth(54);
		starred.setSortable(false);

		tvVocaTable = new TableView<EVWordTable>();
		tvVocaTable.setItems(obsVocaList);
		tvVocaTable.getColumns().addAll(index, vocabulary, starred);

		tvVocaTable.setRowFactory(tv -> {
			TableRow<EVWordTable> row = new TableRow<EVWordTable>();
			row.setOnMouseClicked(event -> {
				if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
					EVWordTable clickedRow = row.getItem();
					
					ShowEditVocaPopup(clickedRow.getIdxVoca() - 1);
				}
			});
			return row;
		});

		getDialogPane().getButtonTypes().addAll(btnGDrive,ButtonType.CANCEL);

		setResultConverter(dialogButton -> {
			if (dialogButton == ButtonType.CANCEL) {
				
			} else if (dialogButton == btnGDrive) {
				UpdateVocabularyToDrive();
			}
			return false;
		});

		getDialogPane().setContent(tvVocaTable);
		getDialogPane().setMinWidth(280);
	}

	private void UpdateVocabularyToDrive() {
        // Build a new authorized API client service.
        Drive service = null;
		try {
			service = GoogleDrive.getDriveService();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        //insert new file ----------------------------
//        private static File insertFile(Drive service, String title, String description,
//                String parentId, String mimeType, String filename)
//        File retf = GoogleDrive.insertFile(service, "Vocabulary_v2.xml", null, null, null, "Vocabulary_v2.xml" );
        //--------------------------------------------
        
        //update file --------------------------------
        GoogleDrive.updateFile(service, "1-dSnhPd7pEHhv4MwRiXoxrZX8wZuAqxr", null, null, null, "Vocabulary_v2.xml", false);
        //--------------------------------------------
        
        //get file list ------------------------------
//        List<File> result = null;
//		try {
//			result = GoogleDrive.retrieveAllFiles(service);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//        
//        for (int i = 0; i < result.size(); i++) {
//        	System.out.println(result.get(i).getTitle() + "-" + result.get(i).getId());
//        }
        //--------------------------------------------
	}
	
	private void ShowEditVocaPopup(int idx) {
		HashMap<String, String> hmapVocaContent = new HashMap<String, String>();
		LinkedList<EVWord> vocaList = AppContent.GetInstance().GetVocaList();
		
		hmapVocaContent.put(EVWord.DF_ORIGINAL, vocaList.get(idx).getOriginal());
		hmapVocaContent.put(EVWord.DF_PHONETIC, vocaList.get(idx).getPhonetic());
		hmapVocaContent.put(EVWord.DF_VIETNAMESE, vocaList.get(idx).getVietnamese());
		hmapVocaContent.put(EVWord.DF_EXAMPLE, vocaList.get(idx).getExample());
		
		if (vocaList.get(idx).getFlag())
			hmapVocaContent.put(EVWord.DF_FLAG, "1");
		else
			hmapVocaContent.put(EVWord.DF_FLAG, "0");
		

		MainApp.GetPrimaryStage().setAlwaysOnTop(false);
		EditorDialog puEditVoca = new EditorDialog(hmapVocaContent);
		
		if (puEditVoca.showAndWait().get() == true) {
			obsVocaList.clear();
			AddVocabularyToObsList(obsVocaList);
		}
		MainApp.GetPrimaryStage().setAlwaysOnTop(true);
	}

	private void AddVocabularyToObsList(ObservableList<EVWordTable> _obsVocaList) {
		LinkedList<EVWord> vocaList = AppContent.GetInstance().GetVocaList();

		for (int i = 0; i < vocaList.size(); i++) {
			_obsVocaList.add(new EVWordTable(vocaList.get(i).getOriginal(), vocaList.get(i).getFlag(), i + 1));
		}
	}
}
