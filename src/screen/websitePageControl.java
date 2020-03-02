package screen;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;

import javafx.concurrent.Worker.State;

public class websitePageControl {

	@FXML
	private ResourceBundle resources;
	@FXML
	private URL location;

	@FXML
	private Button btBackward;

	@FXML
	private Button btForward;

	@FXML
	private WebView webView;

	@FXML
	private TextField tfSearch;

	@FXML
	private Button btHome;

	@FXML
	private Button btSearch;

	@FXML
	private ImageView imgHome;

	WebEngine webEngine;

	// 记录网站历史
	private static ArrayList<String> history = new ArrayList<>();
	private static int length = -1; // 记录当前网站个数（一直累计，不后退）
	private static int lengthVar = 0;// 相对位置差值（相对于length的位置差值）

	@FXML
	void initialize() {
		// load html page
		webEngine = webView.getEngine();

		// 监听界面网页变化
		webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<State>() {
			public void changed(ObservableValue ov, State oldState, State newState) {
				if (newState == State.SUCCEEDED) {
					// 改代码 ---here--------------------------
					String currentweb = webEngine.getLocation();
					history.add(currentweb);
					length++;
					tfSearch.setText(currentweb);
					// ----------------------------------------
				}
			}
		});

		webEngine.load("https://www.bing.com");
		// webEngine.getLocation();//get url of current website
	}

/**
 * gobackward and goforward have some bugs
 * @param event
 */
	@FXML
	void goBackward(ActionEvent event) {
		// 可以往回跳
		if ((length - lengthVar) >= 1) { //Var后面网站个数大于等于一
			int sit = 0;
			lengthVar--;
			sit = length + lengthVar;
			String s = history.get(sit);		
		//	System.out.println("后"+s);			
			webEngine = webView.getEngine();
			webEngine.load(s);
		}
	}

	@FXML
	void goForward(ActionEvent event) {
           //可以往前跳
		if(lengthVar<0) {
			lengthVar++;
			int sit = length + lengthVar;
			String s = history.get(sit);		
		//	System.out.println("前"+s);		
			webEngine = webView.getEngine();
			webEngine.load(s);
		}
		else if(lengthVar==0&&length>=1) {
			lengthVar=1-length;
			int sit = length + lengthVar;
			String s = history.get(sit);			
		//	System.out.println("前"+s);
			webEngine = webView.getEngine();
			webEngine.load(s);
		}
	}

	@FXML
	void searchWebsite(ActionEvent event) {
		String web = this.tfSearch.getText();
		webEngine = webView.getEngine();
		webEngine.load(web);
	}

	@FXML
	void returnHome(ActionEvent event) {
		webEngine = webView.getEngine();
		webEngine.load("https://www.bing.com");
	}

}
