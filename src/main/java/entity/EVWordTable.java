package entity;

public class EVWordTable {
	private String txtVocabulary;
	private String txtStarred;
	private int idxVoca;

	public EVWordTable(String strVocabulary, boolean blStarred, int idx) {
		this.txtVocabulary = strVocabulary;
		
		if (blStarred)
			this.txtStarred = "Starred";
		else
			this.txtStarred = "";
		
		idxVoca = idx;
	}

	public String getTxtVocabulary() {
		return txtVocabulary;
	}

	public void setTxtVocabulary(String txtVocabulary) {
		this.txtVocabulary = txtVocabulary;
	}

	public String getTxtStarred() {
		return txtStarred;
	}

	public void setTxtStarred(String txtStarred) {
		this.txtStarred = txtStarred;
	}
	
	public int getIdxVoca() {
		return idxVoca;
	}
	
	public void setIdxVoca(int idxVoca) {
		this.idxVoca = idxVoca;
	}
}
