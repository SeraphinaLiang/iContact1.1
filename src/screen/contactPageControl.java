package screen;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.Observable;
import java.util.ResourceBundle;

import javax.swing.text.Position;

import basic.Connect;
import basic.ContactPerson;
import basic.Group;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import listviewCell.GroupCell;
import listviewCell.PersonCell;
import listviewCell.cellList;
import staticStuff.Data;
import staticStuff.SceneControl;
import staticStuff.utility;

public class contactPageControl implements Initializable {

	@FXML
	private ResourceBundle resources;
	@FXML
	private URL location;

	@FXML
	private ListView<GroupCell> ContactGroup;
	// ContactGroup.setItems(cellList.refreshGroupList()); 每次刷新界面加入此句
	@FXML
	private ListView<PersonCell> ContactPerson;
	// ----------------------------
	@FXML
	private ComboBox cbSearch;// 搜索联系人
	@FXML
	private Button btSearch;
	// -------------------------
	@FXML
	private Button btAllContact;
	@FXML
	private Button btUngroup;
	// --------------------------
	@FXML
	private Button btDeletePerson;
	@FXML
	private Button btDeleteGroup;

	@FXML
	private Button btAddPerson;
	@FXML
	private Button btAddGroup;
	// ------------------------
	@FXML
	private TextField tfptelephone;
	@FXML
	private TextField tfpbirthday;
	@FXML
	private TextField tfphomepage;
	@FXML
	private TextField tfpphone;
	@FXML
	private TextField tfpname;
	@FXML
	private TextField tfpemail;
	@FXML
	private TextField tfpcompany;
	@FXML
	private TextField tfpaddress;
	@FXML
	private TextField tfppostcode;
	@FXML
	private TextField tfpnotes;
	@FXML
	private TextField tfemergency;
	@FXML
	private TextField tfemerNumber;

	// ---------------------------
	@FXML
	private Button btclear;// 清空当前联系人信息
	@FXML
	private Button btmodifie;// 修改当前联系人信息
	// -----------------------
	@FXML
	private TextField tfgroupName;// 增加的组名
	@FXML
	private Text taddGroupNotice;// 增加新组时的提示信息
	// -------------------------
	@FXML
	private Button btAddToGroup; // 联系人增加到分组
	@FXML
	private Text addToGroupNotice;// 联系人增加到分组提示信息
	@FXML
	private TextField tfAddToGroup;
	// --------------------------
	@FXML
	private ImageView photo;
	@FXML
	private Button btUpLoadPhoto;
	// -------------------------

	private String currentGroup = null; // 当前显示联系人的分组 name
	private String currentPerson = null;// 当前显示详细信息的联系人(id)
	String currentClient = null;

//*********************************************************************************************************

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// 解决不在DB里面的联系人无法插入图片的问题
		app.App.getSQLDemo().saveLinkmanToDB(Data.currentClient.getAccount());
		// --------------------
		init();
		this.initGroupCell();
		this.initPersonCell();// 全部联系人
		searchInputListener();
		selectGroupListener();
		selectPersonListener();
	}

	// 上传照片
	@FXML
	void uploadPhoto(ActionEvent event) {
		// 选择文件
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("上传联系人的照片");
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		File file = fileChooser.showOpenDialog(app.App.getPrimaryStage());

		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Images", "*.*"),
				new FileChooser.ExtensionFilter("JPG", "*.jpg"), new FileChooser.ExtensionFilter("PNG", "*.png"));

		// 更新界面
		Image img = new Image(file.toURI().toString());
		photo.setImage(img);
		// 导入数据库
		app.App.getSQLDemo().putLinkmanPhotoWithPath(file.getAbsolutePath(), this.currentPerson);
		System.out.println("上传成功");
	}

	// 界面初始化
	void init() {
		this.btAllContact.setText("All Contacts (" + utility.getContactListActualSize() + ")");
		this.btUngroup.setText("Ungrouped (" + utility.searchUngroupedContacts().size() + ")");
		this.currentGroup = "AllContacts";
		this.currentClient = Data.currentClient.getAccount();
		this.tfgroupName.setEditable(false);
		this.tfgroupName.setPromptText("click ADD ");
	}

	// 刷新界面
	void repaint() {
		this.btAllContact.setText("All Contacts (" + utility.getContactListActualSize() + ")");
		this.btUngroup.setText("Ungrouped (" + utility.searchUngroupedContacts().size() + ")");
		this.initGroupCell();// 刷新group listview
		// 根据当前group刷新person listview
		try {
			if (this.currentGroup.equals("AllContacts")) {
				this.initPersonCell();
			} else if (this.currentGroup.equals("Ungroup")) {
				this.toShowUngroup();
			} else {
				this.showPersonWithGroup(this.currentGroup);
			}
		} catch (NullPointerException e) {
		}
	}

	// 新增联系人到分组
	@FXML
	void addPersonToGroup(ActionEvent event) {
		if (this.currentPerson != null) {
			String alreadyIn = utility.getGroupnameWithLinkmanID(this.currentPerson);
			if (!this.tfAddToGroup.getText().isEmpty()) {
				String newGroup = this.tfAddToGroup.getText();
				// 该组不存在
				if (!Data.groupsList.containsKey(newGroup)) {
					this.addToGroupNotice.setText("The group do not exist.");
				}
				// 已存在与该组
				else if (alreadyIn.contains(newGroup)) {
					this.addToGroupNotice.setText("Already in this group.");
				} else {
					// new connect
					Connect con = new Connect(this.currentPerson, newGroup, false);
					Data.connectList.add(con);

					this.addToGroupNotice.setText("");
					this.tfAddToGroup.clear();
					repaint();
				}
			}
		}
	}

	// 清空currentPerson
	@FXML
	void clearCurrentPerson(ActionEvent event) {
		this.currentPerson = null;
		this.toClearTextfield();
	}

	void toClearTextfield() {
		this.tfpname.clear();
		this.tfpphone.clear();
		this.tfptelephone.clear();
		this.tfpbirthday.clear();
		this.tfpemail.clear();
		this.tfpaddress.clear();
		this.tfpcompany.clear();
		this.tfpnotes.clear();
		this.tfemergency.clear();
		this.tfemerNumber.clear();
		this.tfppostcode.clear();
		this.tfphomepage.clear();
		Image defaultImg = new Image("file:resources/img/irish.png");
		this.photo.setImage(defaultImg);
	}

	// 更改当前联系人信息
	@FXML
	void modifiePersonInfo(ActionEvent event) {
		if (this.currentPerson != null) {
			// 当前联系人
			ContactPerson cp = Data.contactList.get(this.currentPerson);
			System.out.println(cp.getName());

			cp.setPostcode(this.tfppostcode.getText());
			cp.setPhone(this.tfpphone.getText());
			cp.setTelephone(this.tfptelephone.getText());
			cp.setDescription(this.tfpnotes.getText());
			cp.setEmail(this.tfpemail.getText());
			cp.setAddress(this.tfpaddress.getText());
			cp.setBirthday(this.tfpbirthday.getText());
			cp.setCompany(this.tfpcompany.getText());
			cp.setName(this.tfpname.getText());
			cp.setPersonalPage(this.tfphomepage.getText());
			cp.setEmergency(this.tfemergency.getText());
			cp.setEmergencyNumber(this.tfemerNumber.getText());

			repaint();
		}
	}

	// 删除/增加分组
	@FXML
	void addGroup(ActionEvent event) {
		/**
		 * 1. 先单击，让textfield editable 2. 再输入组名，单击保存，让textfield uneditable
		 */
		if (!this.tfgroupName.isEditable()) {
			this.tfgroupName.setEditable(true);
			this.tfgroupName.setPromptText("group name");
		} else if (this.tfgroupName.isEditable()) {
			String groupName = this.tfgroupName.getText();
			if (groupName != null && !groupName.equals("")) {
				// 已存在该组组名
				if (Data.groupsList.containsKey(groupName)) {
					this.taddGroupNotice.setText("The group already exists.");
				} else {
					Group group = new Group(groupName, false);
					Data.groupsList.put(groupName, group);
					this.tfgroupName.clear();
					this.taddGroupNotice.setText(" ");
					this.tfgroupName.setEditable(false);
					this.tfgroupName.setPromptText("click ADD");
					repaint();
				}
			}
		}
	}

	@FXML
	void deleteGroup(ActionEvent event) {
		// 直接删除这个组，connect删除，联系人若不在其他组，则归为未分组类
		if (this.currentGroup != null) {
			if (!this.currentGroup.equals("AllContacts") && !this.currentGroup.equals("Ungroup")) {
				String groupName = this.currentGroup;
				utility.deleteConnectWithGroupname(groupName);
				// Data.groupsList.remove(groupName);
				Data.groupsList.get(groupName).setDelete(true);

				// 删掉这个组以合，personlistview换成全部联系人
				this.currentGroup = "AllContacts";
				repaint();
			}
		}
	}

	@FXML
	void addPerson(ActionEvent event) {
		ContactPerson cp = null;
		// currentPerson=null
		if (this.currentPerson == null) {
			// 从界面获取输入，新建对象
			if (!this.tfpname.getText().isEmpty()) {
				int i = (int) (Math.random() * 11 + Math.random() * 22 + this.tfpname.getText().hashCode());
				cp = new ContactPerson(i, this.tfpname.getText(), false);
				cp.setTelephone(this.tfptelephone.getText());
				cp.setPhone(this.tfpphone.getText());
				cp.setBirthday(this.tfpbirthday.getText());
				cp.setEmail(this.tfpemail.getText());
				cp.setAddress(this.tfpaddress.getText());
				cp.setCompany(this.tfpcompany.getText());
				cp.setDescription(this.tfpnotes.getText());
				cp.setEmergency(this.tfemergency.getText());
				cp.setEmergencyNumber(this.tfemerNumber.getText());
				cp.setPostcode(this.tfppostcode.getText());
				cp.setPersonalPage(this.tfphomepage.getText());
			}

			// 查看当前分组
			if (!this.currentGroup.equals("AllContacts") && !this.currentGroup.equals("Ungroup")) {
				String groupName = this.currentGroup;
				// 在组中新建联系人，新建connect
				Data.connectList.add(new Connect(cp.getId(), groupName, false));
			}
			Data.contactList.put(cp.getId(), cp);
			// 防止重复加入
			this.currentPerson = cp.getId();
			// 刷新界面
			repaint();
			// 解决不在DB里面的联系人无法插入图片的问题
			app.App.getSQLDemo().saveLinkmanToDB(Data.currentClient.getAccount());
		}
	}

	@FXML
	void deletePerson(ActionEvent event) {
		/**
		 * 如果当前分组，为全部联系人/未分组联系人（AllContacts/Ungroup），则删除此联系人
		 * 如果当前分组为个别小组，则，只从该组删除其与联系人的联系（connect）
		 */
		if (this.ContactPerson.getSelectionModel().getSelectedItem() != null) {
			PersonCell cell = this.ContactPerson.getSelectionModel().getSelectedItem();
			ContactPerson cp = Data.contactList.get(cell.getTheId());
			if (this.currentGroup.equals("AllContacts") || this.currentGroup.equals("Ungroup")) {
				// 删除所有connect 和这个 contactPerson

				// Data.contactList.remove(cp.getId());
				Data.contactList.get(cp.getId()).setDelete(true);

				utility.deleteConnectWithLinkmanID(cp.getId());
				repaint();
			} else {
				// 删除该组与该联系人的Connect
				String groupName = this.currentGroup;
				String id = cp.getId();
				Iterator<Connect> iter = Data.connectList.iterator();
				while (iter.hasNext()) {
					Connect con = iter.next();
					if (con.getGroupName().equals(groupName) && con.getLinkmanID().equals(id)) {

						con.setDelete(true);
						// Data.connectList.remove(con);
						// 该组人数减一
						Group p = Data.groupsList.get(groupName);
						p.setNumberOfPeople(p.getNumberOfPeople() - 1);
						repaint();
						break;
					}
				}
			}
		}
		toClearTextfield();
	}

	// 显示所有联系人
	@FXML
	void showAllContacts(ActionEvent event) {
		this.initPersonCell();
		this.currentGroup = "AllContacts";
	}

	// 显示未分组的联系人
	@FXML
	void showUngroup(ActionEvent event) {
		this.currentGroup = "Ungroup";
		toShowUngroup();
	}

	void toShowUngroup() {
		ContactPerson.setCellFactory(new Callback<ListView<PersonCell>, ListCell<PersonCell>>() {
			@Override
			public ListCell<PersonCell> call(ListView<PersonCell> arg0) {
				ListCell<PersonCell> cell = new ListCell<PersonCell>() {
					@Override
					protected void updateItem(PersonCell n, boolean bt) {
						super.updateItem(n, bt);
						if (n != null) {
							setGraphic(n.getHbox());
						}
					}
				};
				return cell;
			}

		});
		ContactPerson.setItems(utility.searchUngroupedContacts());
	}

	void showPersonWithGroup(String groupName) {
		ContactPerson.setCellFactory(new Callback<ListView<PersonCell>, ListCell<PersonCell>>() {
			@Override
			public ListCell<PersonCell> call(ListView<PersonCell> arg0) {
				ListCell<PersonCell> cell = new ListCell<PersonCell>() {
					@Override
					protected void updateItem(PersonCell n, boolean bt) {
						super.updateItem(n, bt);
						if (n != null) {
							setGraphic(n.getHbox());
						}
					}
				};
				return cell;
			}

		});
		ContactPerson.setItems(utility.searchContactsWithGroup(groupName));
	}

	// 搜索联系人输入监听,根据用户输入实时更新comboBox内容
	@SuppressWarnings("unchecked")
	void searchInputListener() {
		try {
			this.cbSearch.getEditor().textProperty().addListener(o -> {
				String input = this.cbSearch.getEditor().getText();
				this.cbSearch.setVisibleRowCount(5);// combo能显示多少行
				if (input.length() != 0) {
					try {
						ObservableList<String> outcome = utility.enquiryContact(input);
						if (outcome.size() != 0) {
							// 对搜索结果按照姓名分类
//							Iterator<String> iter = outcome.iterator();
//							String s1 = iter.next();
//							String c1 = utility.getPingYin(s1.substring(0, 0));
//							cbSearch.getItems().add(s1);
//							
//							while (iter.hasNext()) {
//								String s2 = iter.next();
//								String c2 = utility.getPingYin(s2.substring(0, 0));
//								
//								if (c2.equals(c1)) {
//									cbSearch.getItems().add(s2);
//									s1=s2;c1=c2;		
//								} else {
//									cbSearch.getItems().add(new Separator());
//									cbSearch.getItems().add(s2);
//									s1=s2;c1=c2;
//								}
//							}

							cbSearch.setItems(outcome);
						}
					} catch (Exception e) {
					}
				}
			});
		} catch (NullPointerException e) {
		}

	}

	// 对group--Listview进行监听
	void selectGroupListener() {
		try {
			this.ContactGroup.getSelectionModel().selectedItemProperty().addListener(o -> {
				GroupCell cell = this.ContactGroup.getSelectionModel().getSelectedItem();
				// System.out.println(cell.getGroupName());
				if (cell.getGroupName() != null) {
					this.currentGroup = cell.getGroupName();
					// 根据选中的Group刷新界面联系人内容
					ContactPerson.setCellFactory(new Callback<ListView<PersonCell>, ListCell<PersonCell>>() {
						@Override
						public ListCell<PersonCell> call(ListView<PersonCell> arg0) {
							ListCell<PersonCell> cell = new ListCell<PersonCell>() {
								@Override
								protected void updateItem(PersonCell n, boolean bt) {
									super.updateItem(n, bt);
									if (n != null) {
										setGraphic(n.getHbox());
									}
								}
							};
							return cell;
						}

					});
					ContactPerson.setItems(utility.searchContactsWithGroup(cell.getGroupName()));
				}
			});
		} catch (NullPointerException e) {

		}

	}

	// 对contactPerson--listview进行监听----更新联系人详细资料
	void selectPersonListener() {
		try {
			this.ContactPerson.getSelectionModel().selectedItemProperty().addListener(o -> {
				if (this.ContactPerson.getSelectionModel().getSelectedItem() != null) {
					PersonCell cell = this.ContactPerson.getSelectionModel().getSelectedItem();
					ContactPerson cp = Data.contactList.get(cell.getTheId());
					// 更新联系人详细资料
					refreshDetailInformation(cp);
					this.currentPerson = cp.getId();
				}

			});
		} catch (NullPointerException e) {
			System.out.println("the group is empty.");
		}
	}

	// 更新联系人详细资料---图片还没加
	void refreshDetailInformation(ContactPerson cp) {
		// 从数据库中读取该联系人照片，位置resources/linkmanPhoto/id.jpg TODO
		// 如果没有照片，则用DEFAULT IMG

			app.App.getSQLDemo().readLinkmanImageFromDB(cp.getId());
			Image img = new Image("file:resources/linkmanPhoto/" + cp.getId() + ".jpg");
			this.photo.setImage(img);
			
			if (img.getHeight() < 0.1) {
				//删除错误导入文件 TODO
				File f=new File("resources/linkmanPhoto/" + cp.getId() + ".jpg");
				if(f.exists()) {
					f.delete();
				}
				//设置默认头像
				Image defaultImg = new Image("file:resources/img/irish.png");
				this.photo.setImage(defaultImg);
			}
		   
			System.out.println(cp.getName());
			System.out.print("ID" + cp.getId() + "照片：" + img.getUrl().toString());
		   

		// ---------------------------------------------------------
		this.tfpname.setText(cp.getName());
		this.tfpphone.setText(cp.getPhone());
		this.tfptelephone.setText(cp.getTelephone());
		this.tfpbirthday.setText(cp.getBirthday());
		this.tfpemail.setText(cp.getEmail());
		this.tfpaddress.setText(cp.getAddress());
		this.tfpcompany.setText(cp.getCompany());
		this.tfpnotes.setText(cp.getDescription());
		this.tfemergency.setText(cp.getEmergency());
		this.tfemerNumber.setText(cp.getEmergencyNumber());
		this.tfppostcode.setText(cp.getPostcode());
		this.tfphomepage.setText(cp.getPersonalPage());
	}

	// 放大镜--查找联系人，根据结果更新界面
	@FXML
	void SearchContact(ActionEvent event) {
		if (this.cbSearch.getValue() != null && !this.cbSearch.getValue().toString().isEmpty()) {
			String info = this.cbSearch.getValue().toString();
			ContactPerson cp = utility.getSearchContactPerson(info);
			this.currentPerson = cp.getId();
			// 根据该联系人更新界面信息
			refreshDetailInformation(cp);
		}

	}

	void initGroupCell() {

		ContactGroup.setCellFactory(new Callback<ListView<GroupCell>, ListCell<GroupCell>>() {
			@Override
			public ListCell<GroupCell> call(ListView<GroupCell> arg0) {
				ListCell<GroupCell> cell = new ListCell<GroupCell>() {
					@Override
					protected void updateItem(GroupCell n, boolean bt) {
						super.updateItem(n, bt);
						if (n != null) {
							setGraphic(n.getHbox());
						}
					}
				};
				return cell;
			}
		});
		ContactGroup.setItems(listviewCell.cellList.refreshGroupList());
	}

	void initPersonCell() {
		// 显示所有联系人
		ContactPerson.setCellFactory(new Callback<ListView<PersonCell>, ListCell<PersonCell>>() {
			@Override
			public ListCell<PersonCell> call(ListView<PersonCell> arg0) {
				ListCell<PersonCell> cell = new ListCell<PersonCell>() {
					@Override
					protected void updateItem(PersonCell n, boolean bt) {
						super.updateItem(n, bt);
						if (n != null) {
							setGraphic(n.getHbox());
						}
					}
				};
				return cell;
			}

		});
		ContactPerson.setItems(listviewCell.cellList.refreshPersonList());
	}

}
