package basic;

public class Group {

	private String groupName;
	private int NumberOfPeople=0;
	private boolean ifStar=false;
	private boolean inDB;
	private boolean isDelete=false;
	
	public Group(String name,boolean inDB) {
		this.groupName=name;
		this.inDB=inDB;
	}
//___________________________________________________________________________________

	
	
	public String getGroupName() {
		return groupName;
	}

	public boolean isDelete() {
		return isDelete;
	}



	public void setDelete(boolean isDelete) {
		this.isDelete = isDelete;
	}



	public boolean isInDB() {
		return inDB;
	}

	public void setInDB(boolean inDB) {
		this.inDB = inDB;
	}

	public boolean isIfStar() {
		return ifStar;
	}

	public void setIfStar(boolean ifStar) {
		this.ifStar = ifStar;
	}

	public int getNumberOfPeople() {
		return NumberOfPeople;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public void setNumberOfPeople(int numberOfPeople) {
		NumberOfPeople = numberOfPeople;
	}
	
}
