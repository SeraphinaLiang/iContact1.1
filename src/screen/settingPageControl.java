package screen;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import staticStuff.Data;

public class settingPageControl {
	//删除账号
	@FXML
    private TextField tfPassword;
    @FXML
    private Button btConfirm;


    @FXML
    void confirmDelete(ActionEvent event) {
    	if(!tfPassword.getText().isEmpty()) {
    		boolean right=Data.currentClient.getPassword().equals(tfPassword.getText());
    		if(right) {
    			Data.currentClient.setDelete(true);
    			
    			//清空缓存
    			Data.connectList.clear();
    			Data.contactList.clear();
    			Data.groupsList.clear();
    			//更新sql
    			app.App.getSQLDemo().saveClientToDB();
    			Data.clientList.remove(Data.currentClient.getAccount());
    			
    			//切换至登录界面
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
