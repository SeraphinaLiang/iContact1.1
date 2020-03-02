package screen;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.FadeTransition;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import staticStuff.SceneControl;

public class homepageControl {

	// homepage左侧四个按钮
	@FXML
	private Button btmyPage;
	@FXML
	private Button btcontact;
	@FXML
	private Button btsetting;
	@FXML
	private Button btWebsite;
	// 主容器
	@FXML
	private Pane home;// 置于空面板的初始界面
	@FXML
	private Pane workPage;// 根据 home左侧 bt而更换的页面
	@FXML
	private AnchorPane root; // 最底部的面板，先显示welcome，login，最后home

	public static Stage primaryStage;


	@FXML
	public void initialize() throws IOException {

		SceneControl.mainController = this;
		primaryStage = app.App.getPrimaryStage();

		if (!staticStuff.SceneControl.isWelcomePageLoad) {
			try {
				loadWelcomePage();
				staticStuff.SceneControl.isWelcomePageLoad = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	@FXML
	void enterMyPage(ActionEvent event) {

		// button 变色
		this.btmyPage.setStyle("-fx-background-color: #F08080; -fx-text-fill: #F5F5F5;");
		this.btcontact.setStyle("-fx-background-color:#F5F5DC; -fx-text-fill: #808080;");
		this.btsetting.setStyle("-fx-background-color:#F5F5DC; -fx-text-fill: #808080;");
		this.btWebsite.setStyle("-fx-background-color:#F5F5DC; -fx-text-fill: #808080;");

		// 进入界面
		try {
			Pane pane = FXMLLoader.load(getClass().getResource("myPage.fxml"));
			this.workPage.getChildren().setAll(pane);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@FXML
	void enterContactPage(ActionEvent event) {
		// button 变色
		this.btcontact.setStyle("-fx-background-color: #F08080; -fx-text-fill: #F5F5F5;");
		this.btmyPage.setStyle("-fx-background-color:#F5F5DC; -fx-text-fill: #808080;");
		this.btsetting.setStyle("-fx-background-color:#F5F5DC; -fx-text-fill: #808080;");
		this.btWebsite.setStyle("-fx-background-color:#F5F5DC; -fx-text-fill: #808080;");

		try {
			Pane pane = FXMLLoader.load(getClass().getResource("contactPage.fxml"));
			workPage.getChildren().setAll(pane);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML
	void enterSettingPage(ActionEvent event) {
		// button 变色
		this.btsetting.setStyle("-fx-background-color: #F08080; -fx-text-fill: #F5F5F5;");
		this.btmyPage.setStyle("-fx-background-color:#F5F5DC; -fx-text-fill: #808080;");
		this.btcontact.setStyle("-fx-background-color:#F5F5DC; -fx-text-fill: #808080;");
		this.btWebsite.setStyle("-fx-background-color:#F5F5DC; -fx-text-fill: #808080;");
		try {
			Pane pane = FXMLLoader.load(getClass().getResource("settingPage.fxml"));
			workPage.getChildren().setAll(pane);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML
	void enterWebsitePage(ActionEvent event) {
		// button 变色
		this.btWebsite.setStyle("-fx-background-color: #F08080; -fx-text-fill: #F5F5F5;");
		this.btmyPage.setStyle("-fx-background-color: #F5F5DC; -fx-text-fill: #808080;");
		this.btcontact.setStyle("-fx-background-color:#F5F5DC; -fx-text-fill: #808080;");
		this.btsetting.setStyle("-fx-background-color:#F5F5DC; -fx-text-fill: #808080;");

		try {
			Pane pane = FXMLLoader.load(getClass().getResource("website.fxml"));
			workPage.getChildren().setAll(pane);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void loadWelcomePage() throws IOException {
		Pane pane = FXMLLoader.load(getClass().getResource("welcome.fxml"));
		root.getChildren().setAll(pane);

		FadeTransition fadeIn = new FadeTransition(Duration.seconds(2.5), pane);
		fadeIn.setFromValue(0);
		fadeIn.setToValue(1);
		fadeIn.setCycleCount(1);

		FadeTransition fadeOut = new FadeTransition(Duration.seconds(2.5), pane);
		fadeOut.setFromValue(1);
		fadeOut.setToValue(0);
		fadeOut.setCycleCount(1);

		Pane paneLogin = FXMLLoader.load(getClass().getResource("login.fxml"));

		fadeIn.play();
		fadeIn.setOnFinished(e -> {
			fadeOut.play();
			fadeOut.setOnFinished(e1 -> {
				// 调试完改回这句
				root.getChildren().setAll(paneLogin);

				// 可以直接用换pane里面node的方法，更改局部界面
				// root.getChildren().setAll(this.home);

			});
		});

	}
}
