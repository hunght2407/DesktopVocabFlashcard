package entity;

import javafx.scene.text.Text;

public class EVWordTable {
	private Text txtVocabulary;
	private Text txtStarred;
	private int idxVoca;

	public EVWordTable(String strVocabulary, boolean blStarred, int idx) {
		this.txtVocabulary = new Text();
		this.txtVocabulary.setText(strVocabulary);

		this.txtStarred = new Text();
		
		if (blStarred)
			this.txtStarred.setText("Starred");
		else
			this.txtStarred.setText("");
		
		idxVoca = idx;
	}

	public Text getTxtVocabulary() {
		return txtVocabulary;
	}

	public void setTxtVocabulary(Text txtVocabulary) {
		this.txtVocabulary = txtVocabulary;
	}

	public Text getTxtStarred() {
		return txtStarred;
	}

	public void setTxtStarred(Text txtStarred) {
		this.txtStarred = txtStarred;
	}
	
	public int getIdxVoca() {
		return idxVoca;
	}
	
	public void setIdxVoca(int idxVoca) {
		this.idxVoca = idxVoca;
	}
}
