package basic;
import java.util.*;
public class ContactPerson {
/**
 * 姓名、电话、手机、即时通信工具及号码、电子邮箱、个人主页、生日、像片、工作单位、 家庭地址、邮编、所属组、备注
 */
	private String name;
	private String telephone;  //电话
	private String phone;      //手机
	private String emergency;  //及时通信工具
	private String emergencyNumber;
	private String email;
	private String personalPage; //个人主页
	private String birthday;
	
	private String company; //工作单位
	private String address; //家庭地址
	private String postcode; //邮编
	private String description; //备注
	
	
//-----------------database---------------------
	private String id; //唯一标识
	private boolean inDB; //是否已经存在于数据库内
	private boolean isDelete=false; //用户是否已删除该联系人
	
	//photo
	
	public ContactPerson(String id,String name,boolean inDB) {
		this.id=id;
		this.name=name;
		this.inDB=inDB;
	}
	
	public ContactPerson(int id,String name,boolean inDB) {
		this.id=String.valueOf(id);
		this.name=name;
		this.inDB=inDB;
	}
	
//--------------------------------------------------------------------------------------------------------------------	
	
	
	public boolean isInDB() {
		return inDB;
	}

	public boolean isDelete() {
		return isDelete;
	}



	public void setDelete(boolean isDelete) {
		this.isDelete = isDelete;
	}



	public void setInDB(boolean inDB) {
		this.inDB = inDB;
	}

	public String getEmergency() {
		return emergency;
	}

	public String getEmergencyNumber() {
		return emergencyNumber;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setEmergency(String emergency) {
		this.emergency = emergency;
	}

	public void setEmergencyNumber(String emergencyNumber) {
		this.emergencyNumber = emergencyNumber;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public String getTelephone() {
		return telephone;
	}

	public String getPhone() {
		return phone;
	}

	public String getEmail() {
		return email;
	}

	public String getPersonalPage() {
		return personalPage;
	}

	public String getBirthday() {
		return birthday;
	}

	public String getCompany() {
		return company;
	}

	public String getAddress() {
		return address;
	}

	public String getDescription() {
		return description;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPersonalPage(String personalPage) {
		this.personalPage = personalPage;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
