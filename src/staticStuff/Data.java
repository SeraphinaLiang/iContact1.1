package staticStuff;

import java.util.ArrayList;
import java.util.HashMap;

import basic.Client;
import basic.Connect;
import basic.ContactPerson;
import basic.Group;

public class Data {
	/**
	 * 开启app时数据到这，最后关闭时再返回数据库。
	 * 切换用户时要更新数据（先保存进数据库，再导入新用户数据）
	 *               1. 如果isDelete=false，inDB=true， 更新数据回数据库
	 *               2.如果isDelete=false，inDB=false，insert values
	 *               3.如果isDelete=true, 从数据库删除该行
	 */

	//HashMap<String Account,Client>
	public static HashMap<String,Client> clientList=new HashMap<>();
	
	//HashMap<String groupName,Group>
	public static HashMap<String,Group> groupsList=new HashMap<>();
	
	//HashMap<String id,ContactPerson>
	public static HashMap<String,ContactPerson> contactList=new HashMap<>();
	
	//关闭时，不在connectList里的要从数据库里删除
	public static ArrayList<Connect> connectList =new ArrayList<>();
	
	//当前用户
	public static Client currentClient;
	
}
