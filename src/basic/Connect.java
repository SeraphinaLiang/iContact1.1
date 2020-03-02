package basic;

import staticStuff.Data;

public class Connect {

	private String linkmanID;
	private String groupName;
	
	private boolean inDB;
	private boolean isDelete=false;//用户是否已删除该联系
	
	public Connect(String linkmanID, String groupName, boolean inDB) {
		super();
		this.linkmanID = linkmanID;
		this.groupName = groupName;
		this.inDB = inDB;
		Data.groupsList.get(groupName).setNumberOfPeople(Data.groupsList.get(groupName).getNumberOfPeople()+1);
	}
	
	
	public boolean isDelete() {
		return isDelete;
	}
	public void setDelete(boolean isDelete) {
		this.isDelete = isDelete;
	}
	public String getLinkmanID() {
		return linkmanID;
	}
	public String getGroupName() {
		return groupName;
	}
	public boolean isInDB() {
		return inDB;
	}
	public void setLinkmanID(String linkmanID) {
		this.linkmanID = linkmanID;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public void setInDB(boolean inDB) {
		this.inDB = inDB;
	}
	
	
}
