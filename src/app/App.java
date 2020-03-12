package app;

import java.io.IOException;

import basic.Client;
import basic.Group;
import database.SQLDemo;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import screen.homepageControl;
import staticStuff.Data;

public class App extends Application {

	public static Stage stage = new Stage();
	public static SQLDemo sql = new SQLDemo();

	@Override
	public void start(Stage primaryStage) throws IOException {
		Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("screen/homepage.fxml"));
		Scene mainScene = new Scene(root, 1000, 650);
		stage.setHeight(650);
		stage.setWidth(1000);
		stage.setResizable(false);
		stage.setTitle("Mes amis et Moi");
		stage.initStyle(StageStyle.UTILITY);
		stage.setScene(mainScene);
		stage.show();

		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				exitSystem();
			}
		});
	}

	public static void main(String[] args) {
		/**
		 * 连接数据库，导入所有用户信息，再进入登陆界面， 登录界面：根据登录信息确定当前用户。
		 * 用户相关信息在login处导入---调用utility.setCurrentClient
		 */
		sql.initDatabase();
		sql.getDataFromDBClient();

//----------------------test------------------------------------------------		

//-------------------------------------------------------------------------------------------------------

		launch(args);
	}

	public static Stage getPrimaryStage() {
		return stage;
	}

	public static SQLDemo getSQLDemo() {
		return sql;
	}

	public static void exitSystem() {
		sql.saveClientToDB();
		try {
		//没登陆关闭不了界面
			sql.saveGroupToDB(Data.currentClient.getAccount());
			sql.saveLinkmanToDB(Data.currentClient.getAccount());
			sql.saveConnectToDB();
		}catch(Exception e) {
			
		}
		sql.closeDatabase();
	}
}