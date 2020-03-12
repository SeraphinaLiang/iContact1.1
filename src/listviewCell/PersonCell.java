package listviewCell;

import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class PersonCell extends ListCell<String>{

	private Label id=new Label();//标识，不显示
	
	private HBox hbox = new HBox();
	private Label name=new Label();
	private Label email=new Label();
	private Label phone=new Label();
	private Label address=new Label();
	private Label currentGroup=new Label();
	
	private Image img = new Image("file:resources/img/pin.png");
	private ImageView imgV=new ImageView(img); 

	public PersonCell(String name,String email,String phone,String address,String currentGroup) {
		this.name.setText(String.format("%-25s", " "+name));
		this.email.setText(String.format("%-25s", email));
		this.phone.setText(String.format("%-25s", phone));
		this.address.setText(String.format("%-20s", address));
		this.currentGroup.setText(currentGroup);
	
		imgV.setFitHeight(15);
		imgV.setFitWidth(15);
		hbox.getChildren().addAll(imgV,this.name,this.email,this.phone,this.address,this.currentGroup);
	}

	public HBox getHbox() {
		return hbox;
	}

	public String getTheId() {
		return this.id.getText();
	}

	public void setTheId(String id) {
		this.id.setText(id);
	}

	
}
