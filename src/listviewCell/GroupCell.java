package listviewCell;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class GroupCell extends ListCell<String> {

	/**
	 * 界面的listView 每一个联系组放到listview里面的内容
	 */

	private HBox hbox = new HBox();
	private Label groupName = new Label("groupName");
	private Label left = new Label("(");
	private Label right = new Label(")");
	private Label number = new Label("0");

	private Image img = new Image("parade.png");
	//private Image imgStar=new Image("star.png");
	
	private ImageView imgV=new ImageView(img); 

	public GroupCell(String name, int n,boolean ifStar) {
		super();
		this.setGroupName(name);
		this.setNumber(n);
		initLayout();
  
	//	System.out.println(img.getUrl());
	}

	private void initLayout() {
		imgV.setFitHeight(16);
		imgV.setFitWidth(16);
		
		groupName.setPadding(new Insets(5));
		left.setPadding(new Insets(5));
		number.setPadding(new Insets(5));
		right.setPadding(new Insets(5));
		hbox.getChildren().addAll(imgV, groupName, left, number, right);
	}

//------------------------------------------------------------------------------------------------

	public void setGroupName(String gn) {
		this.groupName.setText(gn);
	}

	public void setNumber(int n) {
		this.number.setText(String.valueOf(n));
	}

	public String getGroupName() {
		return this.groupName.getText();
	}

	public HBox getHbox() {
		return hbox;
	}

	public ImageView getImgV() {
		return imgV;
	}
    
	

}
