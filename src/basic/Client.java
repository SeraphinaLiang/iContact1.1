package basic;

import javafx.scene.image.Image;

public class Client {

	private String firstName;
	private String lastName;
	private String account;
	private String password;
	private String email;
	private String birthday;
	private int gender;    //1 male 2 female 0没选
//----------------------------
	private String photopath;
	private String address;
	private String myJob;
	private String mySkill;
	private String EduBackground;
//--------------------------------
	private boolean inDB; //是否已经存在于数据库里
	private boolean isDelete=false; //用户是否已删除账号
	
	//照片可以通过电脑上传

	public Client(String firstName, String lastName, String account, String password, 
			String email, String birthday, int gender,boolean inDB) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.account = account;
		this.password = password;
		this.email = email;
		this.birthday = birthday;
		this.gender = gender;
		this.inDB=inDB;
		
	}
	
	public void print() {
		System.out.println(firstName+","+lastName+","+account+","+ password+","+email+","+birthday+","+gender);
	}
	
	//---------------------------------------------------------------------------------------------------
	
	
	public String getPhotopath() {
		return photopath;
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

	public String getAddress() {
		return address;
	}


	public String getMyJob() {
		return myJob;
	}


	public String getMySkill() {
		return mySkill;
	}


	public String getEduBackground() {
		return EduBackground;
	}


	public void setPhotopath(String photopath) {
		this.photopath = photopath;
	}


	public void setAddress(String address) {
		this.address = address;
	}


	public void setMyJob(String myJob) {
		this.myJob = myJob;
	}


	public void setMySkill(String mySkill) {
		this.mySkill = mySkill;
	}


	public void setEduBackground(String eduBackground) {
		EduBackground = eduBackground;
	}
	
	
	
	public String getFirstName() {
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	public String getAccount() {
		return account;
	}
	public String getPassword() {
		return password;
	}
	public String getEmail() {
		return email;
	}
	public String getBirthday() {
		return birthday;
	}
	public int getGender() {
		return gender;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public void setGender(int gender) {
		this.gender = gender;
	}
	
	
}