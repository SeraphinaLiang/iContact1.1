package screen;

import basic.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import staticStuff.Data;

public class MyPageControl {

	//右下角的
	@FXML
	private TabPane tabPane;
	@FXML
	private Tab tabBasic;
	@FXML
	private Tab tabSecurity;

	@FXML
	private Label labelPassword;//修改密码错误提示信息
	@FXML
	private TextField tfNewpassword;

	@FXML
	private RadioButton rbFemale;

	@FXML
	private ToggleGroup gender;

	@FXML
	private TextField tfBirthday;

	@FXML
	private TextField tfFirstname;

	@FXML
	private TextField tfLastname;

	@FXML
	private TextField tfAddress;

	@FXML
	private Button btAddress;

	@FXML
	private ImageView clientPhoto;

	@FXML
	private TextArea taMyJob;

	@FXML
	private TextArea taMySkill;

	@FXML
	private TextField tfEmail;

	@FXML
	private TextField tfOldpassword;

	@FXML
	private RadioButton rbMale;

	@FXML
	private Button btBirthday;

	@FXML
	private Button btPasswordConfirm;

	@FXML
	private Button btFirstname;

	@FXML
	private Button btUpLoadPhoto;

	@FXML
	private TextField tfpasswordRepeat;

	@FXML
	private TextArea taMyEB;

	@FXML
	private Button btLastname;

	@FXML
	private Text TextName;//Jon Snow

	@FXML
	private Button btEmail;

	@FXML
	void initialize() {
		setOriginalInformation();
		textAreaListener();
		genderListener();
	}

	// 根据当前用户，设定界面初始信息
	private void setOriginalInformation() {
		Client c = Data.currentClient;
		//大标题-客户名字
		this.TextName.setText(c.getFirstName()+" "+c.getLastName());
		// 左边三个textArea
		this.taMyJob.setText(c.getMyJob());
		this.taMySkill.setText(c.getMySkill());
		this.taMyEB.setText(c.getEduBackground());
		// 基本信息
		this.tfFirstname.setText(c.getFirstName());
		this.tfLastname.setText(c.getLastName());
		this.tfAddress.setText(c.getAddress());
		this.tfEmail.setText(c.getEmail());
		this.tfBirthday.setText(c.getBirthday());
		
		//从数据库调入用户照片--没写完
		//app.App.getSQLDemo().readClientImageFromDB(c.getAccount());

		int gen = c.getGender();
		if (gen == 1) {
			this.rbMale.setSelected(true);
		} else {
			this.rbFemale.setSelected(true);
		}
        //修改密码提示信息置空
		this.labelPassword.setText(" ");
	}

//--------------------------------------------
	// 修改个人信息
	@FXML
	void changeFirstname(ActionEvent event) {
		Data.currentClient.setFirstName(this.tfFirstname.getText());
		this.tfFirstname.setText(this.tfFirstname.getText());
	}

	@FXML
	void changeLastname(ActionEvent event) {
		Data.currentClient.setLastName(this.tfLastname.getText());
		this.tfLastname.setText(this.tfLastname.getText());
	}

	@FXML
	void changeEmail(ActionEvent event) {
		Data.currentClient.setEmail(this.tfEmail.getText());
		this.tfEmail.setText(this.tfEmail.getText());
	}

	@FXML
	void changeBirthday(ActionEvent event) {
		Data.currentClient.setBirthday(this.tfBirthday.getText());
		this.tfBirthday.setText(this.tfBirthday.getText());
	}

	@FXML
	void changeAddress(ActionEvent event) {
		Data.currentClient.setAddress(this.tfAddress.getText());
		this.tfAddress.setText(this.tfAddress.getText());
	}

//-------------------------------------------
	// 修改密码
	@FXML
	void confirmPasswordChange(ActionEvent event) {
		String old = this.tfOldpassword.getText();
		String new1 = this.tfNewpassword.getText();
		String new2 = this.tfpasswordRepeat.getText();

		// 旧密码输入错误/旧密码不为空
		if ((!old.equals(Data.currentClient.getPassword())) && (!old.equals(""))) {
			this.labelPassword.setText("Your old Password is wrong.");
		} else {
			// 新密码为空
			if (new1.equals("") || new2.equals("")) {
				this.labelPassword.setText("");
				return;
			}
			// 两次新密码输入不一致
			else if ((!new1.equals(new2))) {
				this.labelPassword.setText("New passwords are not identical.");
			} else {// 旧密码正确，新密码一致
				if (new1.equals(old)) {// 新旧密码一致
					this.labelPassword.setText("Old password and new password are the same.");
				} else {
					this.labelPassword.setText("Modified successfully.");
					Data.currentClient.setPassword(new1);
					// 清空用户输入
					this.tfOldpassword.clear();
					this.tfNewpassword.clear();
					this.tfpasswordRepeat.clear();
				}
			}
		}
	}

	@FXML
	void clickOnTabpane(MouseEvent event) {
		//如果离开了改密码界面，提示消息置空
		if (this.tabPane.getSelectionModel().getSelectedItem().getId().equals("tabBasic")) {
			this.labelPassword.setText("");
		}
	}

//-------------listener-----------------------
	//左边三个框
	void textAreaListener() {
		this.taMySkill.textProperty().addListener(o -> {
			Data.currentClient.setMySkill(this.taMySkill.getText());
		});
		this.taMyJob.textProperty().addListener(o -> {
			Data.currentClient.setMyJob(this.taMyJob.getText());
		});
		this.taMyEB.textProperty().addListener(o -> {
			Data.currentClient.setEduBackground(this.taMyEB.getText());
		});
	}
    //radio buttun
	void genderListener() {
		this.gender.selectedToggleProperty().addListener(o -> {
			if (this.rbMale.isSelected()) {
				Data.currentClient.setGender(1);
			} else {
				Data.currentClient.setGender(2);
			}
		});
	}

}
