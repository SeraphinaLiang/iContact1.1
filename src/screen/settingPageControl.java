package screen;

import java.io.File;
import java.io.IOException;

import fileUpDownLoad.VCardHandle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import staticStuff.Data;

public class settingPageControl {
	// 删除账号
	@FXML
	private TextField tfPassword;
	@FXML
	private Button btConfirm;

	@FXML
	private Button btConnectPhone;// connect to my phone
	@FXML
	private Button btSave; // 导出用户联系人信息至本机
	@FXML
	private Button btUpload; // 从本机文件导入用户联系人

	// ---------------------------------------------------------------------------------

	@FXML
	void connectToPhone(ActionEvent event) {

	}

	@FXML
	void upload(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("选择需要上传的文件");
		// 初始打开的位置
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		File file = fileChooser.showOpenDialog(app.App.getPrimaryStage());

		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("VCF", "*.vcf"),
				new FileChooser.ExtensionFilter("CSV", "*.csv"));

		// 输入所选择文件的路径
		if(file.getPath().contains(".vcf")) {
			VCardHandle.readVCard(file.getAbsolutePath());
		}
		
	}

	@FXML
	void save(ActionEvent event) {

		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setTitle("保存联系人信息至...");
		File directory = directoryChooser.showDialog(app.App.getPrimaryStage());
		if (directory != null) {
			VCardHandle.exportVCardFile(directory.getAbsolutePath());
		}
	}

	@FXML
	void confirmDelete(ActionEvent event) {
		if (!tfPassword.getText().isEmpty()) {
			boolean right = Data.currentClient.getPassword().equals(tfPassword.getText());
			if (right) {
				Data.currentClient.setDelete(true);

				// 清空缓存
				Data.connectList.clear();
				Data.contactList.clear();
				Data.groupsList.clear();
				// 更新sql
				app.App.getSQLDemo().saveClientToDB();
				Data.clientList.remove(Data.currentClient.getAccount());

				// 切换至登录界面
				try {
					Pane pane = FXMLLoader.load(getClass().getResource("login.fxml"));
					Scene scene = new Scene(pane, 1000, 650);
					app.App.getPrimaryStage().setScene(scene);

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
