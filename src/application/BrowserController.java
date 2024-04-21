package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class BrowserController implements Initializable {

    @FXML
    private TextField tfURL;

    @FXML
    private WebView webView; //web page ကိုပဲပြပေးနိုင်တာ / loading လုပ်မပေးနိုင်ဘူး။ 
 
    @FXML
    private ProgressBar loadingbar;
    
    //loading လုပ်ဖို့အတွက် web engine လိုတယ်။ 
    private WebEngine webEngine;
    
    //section history တွေဖတ်ဖို့ / webEngine က load လုပ်တဲ့ site ရဲ့ info တွေသိမ်းထားတာ 
    private WebHistory webHistory;

    @FXML
    void processBack(MouseEvent event) {
    	webHistory = webView.getEngine().getHistory();
    	webHistory.go(-1); //ရှေ့ကို ပြန်သွားဖို့ 
    	ObservableList<WebHistory.Entry> entries = webHistory.getEntries();
		int currentIndex = webHistory.getCurrentIndex();
		tfURL.setText(entries.get(currentIndex).getUrl());
		
		Stage stage = (Stage) ((Node)(event.getSource())).getScene().getWindow();
		stage.setTitle(entries.get(currentIndex).getTitle());
    }

    @FXML
    void processForward(MouseEvent event) {
    	webHistory = webView.getEngine().getHistory();
    	webHistory.go(1); 
    	ObservableList<WebHistory.Entry> entries = webHistory.getEntries();
		int currentIndex = webHistory.getCurrentIndex();
		tfURL.setText(entries.get(currentIndex).getUrl());
		
		Stage stage = (Stage) ((Node)(event.getSource())).getScene().getWindow();
		stage.setTitle(entries.get(currentIndex).getTitle());
    }

    @FXML
    void processReload(MouseEvent event) {
    	webEngine.reload(); 
    }

    private void loadWebPage(String url, KeyEvent event) {
    	webEngine = webView.getEngine();
		
		loadingbar.setVisible(true);
		loadingbar.progressProperty().bind(webEngine.getLoadWorker().progressProperty());
		
		webEngine.load("https://" + url);
		
		webEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
			if(newValue.equals(Worker.State.SUCCEEDED)) {
				loadingbar.setVisible(false);
				
				webHistory = webView.getEngine().getHistory();
	    		ObservableList<WebHistory.Entry> entries = webHistory.getEntries();
	    		int currentIndex = webHistory.getCurrentIndex();
	    		tfURL.setText(entries.get(currentIndex).getUrl());
	    		
				if (event != null) {
					Stage stage = (Stage) ((Node) (event.getSource())).getScene().getWindow();
					stage.setTitle(entries.get(currentIndex).getTitle());
				}
			}
		});
    }
    
    @FXML
    void processURL(KeyEvent event) {
    	if(event.getCode() == KeyCode.ENTER) {
    		String url = tfURL.getText().trim();
    		loadWebPage(url,event); 		
    	}
    }

    @FXML
    void processZoomIn(MouseEvent event) {
    	webView.setZoom(webView.getZoom() * 2.0); //2x, 4x, 8x
    }

    @FXML
    void processZoomOut(MouseEvent event) {
    	webView.setZoom(webView.getZoom() / 2.0);
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		loadingbar.setVisible(false);
		loadWebPage("www.google.com", null);
		
		
	}

}
