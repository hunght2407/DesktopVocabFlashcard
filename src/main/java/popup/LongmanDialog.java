package popup;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class LongmanDialog extends Dialog<Boolean> {
	WebView browser;
	WebEngine webEngine;
	
	public LongmanDialog(String vocabulary) {
		browser = new WebView();
		webEngine = browser.getEngine();
		java.net.CookieManager manager = new java.net.CookieManager();
		java.net.CookieHandler.setDefault(manager);
		
		getDialogPane().getButtonTypes().add(ButtonType.OK);
		setTitle("Longman Dictionary.");

		setResultConverter(dialogButton -> {
			webEngine.load("about:blank");
			manager.getCookieStore().removeAll();
			return true;
		});

		browser.setPrefSize(500, 400);
		browser.setZoom(0.8);
		webEngine.load("http://www.ldoceonline.com/dictionary/"+vocabulary);

		getDialogPane().setContent(browser);
	}
}
