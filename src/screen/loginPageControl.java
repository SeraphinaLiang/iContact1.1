package screen;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;

import basic.Client;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import staticStuff.Data;
import staticStuff.SceneControl;
import staticStuff.utility;

public class loginPageControl {
	/**
	 * 没有检查是否有账号相同的用户
	 */

	// -----临时变量------------------
	static Stage currentStage;
	// 组件.getScene().getWindow().hide(); 关闭组件所在的stage

	// login page--------------------------------------------------------------
	@FXML
	private ResourceBundle resources;
	@FXML
	private URL location;
	@FXML
	private Button btcreate;
	@FXML
	private TextField tfaccount;
	@FXML
	private PasswordField tfpassword;
	@FXML
	private Button btlogin;
	@FXML
	private Button btexit;

	// pop up------login fail pop up--------------------------------------------
	@FXML
	private Button btShunDownPopUp;
	@FXML
	private Button btReWrite;
	// pop up---------create account pop up--------------------------------------
	@FXML
	private Button btReturnToLogin;
	// pop up---------repeat account pop up--------------------------------------
	@FXML
	private Button btRepeat;

	// create an account---------------------------------------------------------
	@FXML
	private DatePicker pickBirthday;
	@FXML
	private TextField tfLastname;
	@FXML
	private RadioButton rbFemale;
	@FXML
	private RadioButton rbMale;
	@FXML
	private TextField tfAccountSignUp;
	@FXML
	private ToggleGroup gender;
	@FXML
	private TextField tfFirstname;
	@FXML
	private TextField tfPasswordSignUp;
	@FXML
	private TextField tfEmail;
	@FXML
	private Button btSignUp;

//**********************************全局函数*****************************************************

	@FXML
	void initialize() throws IOException { // login page init
		SceneControl.loginController = this;
	}

	// change current stage
	void setCurrentStage(Stage s) {
		loginPageControl.currentStage = s;
	}

	// get current stage
	Stage getCurrentStage() {
		return loginPageControl.currentStage;
	}

//***********************************界面函数***************************************************

	@FXML
	void shutDownRepeatPopUp(ActionEvent event) {
		btRepeat.getScene().getWindow().hide();
	}

	// ------FINISH-------FINISH---------FINISH-----------
	@FXML
	void menberSignUp(ActionEvent event) throws IOException { // button btsignup--- create account page
		Pane pane;

		// 查看改账号是否已存在
		if (!tfAccountSignUp.getText().isEmpty()) {
			if (staticStuff.Data.clientList.containsKey(tfAccountSignUp.getText())) {
				Pane pane1 = FXMLLoader.load(getClass().getResource("repeatAccountPopUp.fxml"));	
				Scene s1=new Scene(pane1);
				Stage sta=new Stage();
				sta.setScene(s1);
				sta.show();
				return;
			}
		}

		// 每次load新界面controller就变了，要在变之前获取数据
		if (!tfLastname.getText().isEmpty()) {
			if (!tfAccountSignUp.getText().isEmpty()) {
				if (!tfFirstname.getText().isEmpty()) {
					if (!tfPasswordSignUp.getText().isEmpty()) {
						if (!tfEmail.getText().isEmpty()) {
							String firstName;
							String lastName;
							String account;
							String password;
							String email;
							firstName = tfFirstname.getText();
							lastName = tfLastname.getText();
							account = tfAccountSignUp.getText();
							password = tfPasswordSignUp.getText();
							email = tfEmail.getText();	
							
							String birthday;
							try {
								birthday = pickBirthday.getValue().toString();// 2020-01-29
							} catch (Exception e) {
								birthday="0000-00-00";
							}

							int sex = 0;
							if (gender.getSelectedToggle() != null) {
								if (rbMale.isSelected()) {
									sex = 1; // male
								} else
									sex = 2; // female
							}

							// 创建Client对象，加入Data
							Client c = new Client(firstName, lastName, account, password, email, birthday, sex,false);
							staticStuff.Data.clientList.put(account, c);

							// 清空用户输入数据
							tfFirstname.clear();
							tfLastname.clear();
							tfAccountSignUp.clear();
							tfPasswordSignUp.clear();
							tfEmail.clear();
							rbMale.setSelected(false);
							rbFemale.setSelected(false);
							pickBirthday.setValue(null);

							// 弹窗
							pane = FXMLLoader.load(getClass().getResource("createAccountPopUp.fxml"));
							Stage ss = new Stage();
							this.setCurrentStage(ss);
							Scene s = new Scene(pane);
							ss.setScene(s);
							ss.setResizable(false);
							ss.show();
//							
//							//test
//							Client t=staticStuff.Data.clientList.get(account);
//							t.print();
						}
					}
				}
			}
		}
	}

	// ------FINISH-------FINISH---------FINISH-----------
	// create account pop up page创建成功，返回登录
	@FXML
	void goBackToLoginPage(ActionEvent event) throws IOException {
		// 转跳界面
		Pane pane = FXMLLoader.load(getClass().getResource("login.fxml"));
		Stage s = app.App.getPrimaryStage();
		Scene scene = new Scene(pane, 1000, 650);
		s.setScene(scene);
		this.getCurrentStage().close();
		this.setCurrentStage(null);

	}

	// login page 三个button 对应动作

	@SuppressWarnings("unused")
	@FXML
	void toLogin(ActionEvent event) throws IOException {

		if (utility.isAccountMatch(tfaccount.getText(), tfpassword.getText())) {// 匹配账号和密码
			//设定当前用户
			utility.setCurrentClient(tfaccount.getText());
			//清空输入数据
			tfaccount.clear();
			tfpassword.clear();
			// 转跳至主页homepage
			Pane home = FXMLLoader.load(getClass().getResource("homepage.fxml"));
			Scene s = new Scene(home);
			Stage stage = app.App.getPrimaryStage();
			stage.setScene(s);

		} else {
			// 弹窗 账号或密码输入错误
			try {
				Pane pane = FXMLLoader.load(getClass().getResource("loginFailPopUp.fxml"));
				Stage popUpStage = new Stage();
				Scene s = new Scene(pane);
				popUpStage.setScene(s);
				popUpStage.setResizable(false);
				popUpStage.show();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	@FXML
	void toCreateAccount(ActionEvent event) {

		try {
			Pane pane = FXMLLoader.load(getClass().getResource("createAccountPage.fxml"));
			Stage s = app.App.getPrimaryStage();
			Scene scene = new Scene(pane, 1000, 650);
			s.setScene(scene);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// 不登陆，关闭系统
	@FXML
	void toExit(ActionEvent event) {
		Stage s = app.App.getPrimaryStage();
		s.close();
	}

	// loginfail pop up page ---重新输入账号密码
	@FXML
	void reEnterAccount(ActionEvent event) {
		btShunDownPopUp.getScene().getWindow().hide();
	}

	// loginfail pop up page 退出系统
	@FXML
	void shutDownPopUp(ActionEvent event) {
		btShunDownPopUp.getScene().getWindow().hide();
		Stage s = app.App.getPrimaryStage();
		s.close();
	}

}
