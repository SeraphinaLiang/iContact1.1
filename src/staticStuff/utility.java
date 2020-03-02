package staticStuff;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import basic.Client;
import basic.Connect;
import basic.ContactPerson;
import basic.Group;
import database.SQLDemo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import listviewCell.GroupCell;
import listviewCell.PersonCell;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.*;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class utility {

	//根据联系人ID得到其所在分组
	public static String getGroupnameWithLinkmanID(String id) {
		StringBuffer outcome=new StringBuffer();
		Iterator<Connect> iter = Data.connectList.iterator();
		while (iter.hasNext()) {
			Connect con = iter.next();
			if (con.getLinkmanID().equals(id)) {
				outcome.append(con.getGroupName()+",");
			}
		}
		return outcome.toString();
	}
	
	public static void deleteConnectWithGroupname(String groupName) {
		// 存放需要删除的对象
		ArrayList<Connect> delete=new ArrayList<>();
		
		Iterator<Connect> iter = Data.connectList.iterator();
		while (iter.hasNext()) {
			Connect con = iter.next();
			if (con.getGroupName().equals(groupName)) {
				// Data.connectList.remove(con);
				delete.add(con);
			}
		}
		
		Iterator<Connect> i=delete.iterator();
		while(i.hasNext()) {
			Connect con = i.next();
			Data.connectList.remove(con);
		}
	}

	// 根据contactPerson(id)删除所有connect,相应的Group人数减一
	/**
	 * 不可以遍历ArrayList的同时删除元素
	 */
	public static void deleteConnectWithLinkmanID(String id) {
		// 存放需要删除的下标
		int delete[] = new int[10000];
		int i = 0;

		Iterator<Connect> iter = Data.connectList.iterator();
		while (iter.hasNext()) {
			Connect con = iter.next();
			if (con.getLinkmanID().equals(id)) {
				// 对应组个数减一
				String groupName = con.getGroupName();
				Group g = Data.groupsList.get(groupName);
				g.setNumberOfPeople(g.getNumberOfPeople() - 1);
				// Data.connectList.remove(con);
				delete[i] = Data.connectList.indexOf(con);
				i++;
			}
		}
		for (int j = 0; j < i; j++) {
			Data.connectList.remove(delete[j]);
		}
	}

	// 查找并返回未分组联系人--button(Ungrouped)
	public static ObservableList<PersonCell> searchUngroupedContacts() {
		// 全部联系人（全部联系人- 有组的联系人=未分组联系人）
		HashMap<String, ContactPerson> allPeople = new HashMap<>(Data.contactList);
		// 返回的无组名单
		ObservableList<PersonCell> personList = FXCollections.observableArrayList();
		// 遍历Connect
		Iterator<Connect> iterator = Data.connectList.iterator();
		while (iterator.hasNext()) {
			Connect con = iterator.next();
			if (allPeople.containsKey(con.getLinkmanID())) {
				// 去掉有组的
				allPeople.remove(con.getLinkmanID());
			}
		}
		// 遍历剩下的All People
		Iterator<Entry<String, ContactPerson>> iter = allPeople.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, ContactPerson> entry = iter.next();
			ContactPerson c = entry.getValue();
			PersonCell cell = new PersonCell(c.getName(), c.getEmail(), c.getPhone(), c.getAddress(), "non");
			cell.setTheId(c.getId());
			personList.add(cell);
		}
		return personList;
	}

	// 根据分组查询联系人---单击ListView(Group)
	public static ObservableList<PersonCell> searchContactsWithGroup(String groupName) {
		// 返回的名单
		ObservableList<PersonCell> personList = FXCollections.observableArrayList();
		// 遍历Connect
		Iterator<Connect> iter = Data.connectList.iterator();
		while (iter.hasNext()) {
			Connect con = iter.next();
			if (con.getGroupName().equals(groupName)) {
				String id = con.getLinkmanID();
				// 找到这个联系人
				ContactPerson c = Data.contactList.get(id);
				PersonCell cell = new PersonCell(c.getName(), c.getEmail(), c.getPhone(), c.getAddress(), groupName);
				cell.setTheId(c.getId());
				personList.add(cell);
			}
		}
		return personList;
	}

	// 得到查询的联系人
	public static ContactPerson getSearchContactPerson(String info) {
		ContactPerson cp = null;
		// 得到相应的hashmap
		HashMap<String, ContactPerson> people = Data.contactList;
		// 遍历
		Iterator<Entry<String, ContactPerson>> iter = people.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, ContactPerson> entry = iter.next();
			ContactPerson c = entry.getValue();
			if (info.contains(c.getName())) {
				cp = c;
			}
		}
		return cp;
	}

	// 查询联系人---界面中允许输入（姓名、电话号码、手机、姓名的汉语拼音的声母或全 拼）后列出符合条件的联系人列表。
	@SuppressWarnings("unchecked")
	public static ObservableList<String> enquiryContact(String info) {
		// 返回的字符串
		HashSet<String> list = new HashSet<>();
		// 遍历所有联系人
		HashMap<String, ContactPerson> people = Data.contactList;
		Iterator<Entry<String, ContactPerson>> iter = people.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, ContactPerson> entry = iter.next();
			ContactPerson c = entry.getValue();

			if (c.getName().contains(info)) {
				list.add(c.getName());
				// System.out.println("1." + c.getName());
				continue;
			}
			if (getPingYin(c.getName()).contains(info)) {
				list.add(c.getName());
				// System.out.println("2." + c.getName());
				continue;
			}
			if (getPinYinHeadChar(c.getName()).contains(info)) {
				list.add(c.getName());
				// System.out.println("3." + c.getName());
				continue;
			}
			if (c.getPhone() != null) {
				if (c.getPhone().contains(info)) {
					list.add(c.getName() + " " + c.getPhone());
					// System.out.println(c.getName() + " " + c.getPhone());
					continue;
				}
			}
			if (c.getTelephone() != null) {
				if (c.getTelephone().contains(info)) {
					list.add(c.getName() + " " + c.getTelephone());
					// System.out.println(c.getName() + " " + c.getTelephone());
					continue;
				}
			}
			if (list.size() > 5) {// 最多查找五个匹配项
				break;
			}
		}
		
		ObservableList<String> outcome = null;
		if (list.size() != 0) {
			outcome = FXCollections.observableArrayList(list);
		}
		
		Collections.sort(outcome,new NameComparator());
		return outcome;
	}

	// 设定当前用户,从数据库调入当前用户相关信息---从loginPageControl调用
	public static void setCurrentClient(String account) {
		// 设定当前用户
		Data.currentClient = Data.clientList.get(account);
		// 从数据库调入相关数据
		SQLDemo sql = app.App.getSQLDemo();
		sql.getDataFromDBLinkman(account);
		sql.getDataFromDBGroup(account);
		sql.getDataFromDBConnect(account);
	}

	// login
	public static boolean isAccountMatch(String acc, String key) {
		if (Data.clientList.containsKey(acc)) {
			Client client = Data.clientList.get(acc);
			if (client.getPassword().equals(key)) {
				return true;
			}
		}
		return false;
	}

	// 将汉字转换为全拼
	public static String getPingYin(String src) {
		char[] t1 = null;
		t1 = src.toCharArray();
		String[] t2 = new String[t1.length];
		HanyuPinyinOutputFormat t3 = new HanyuPinyinOutputFormat();

		t3.setCaseType(HanyuPinyinCaseType.LOWERCASE); // 返回小写
		t3.setToneType(HanyuPinyinToneType.WITHOUT_TONE); // 返回无声调
		t3.setVCharType(HanyuPinyinVCharType.WITH_V); // 以V表示该字符
		String t4 = "";
		int t0 = t1.length;
		try {
			for (int i = 0; i < t0; i++) {
				// 判断是否为汉字字符
				if (java.lang.Character.toString(t1[i]).matches("[\\u4E00-\\u9FA5]+")) { // 正则表达式，至少匹配一个汉字
					t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i], t3);
					t4 += t2[0];
				} else
					t4 += java.lang.Character.toString(t1[i]);
			}
			return t4;
		} catch (BadHanyuPinyinOutputFormatCombination e1) {
			e1.printStackTrace();
		}
		return t4;
	}

	// 返回中文的首字母
	public static String getPinYinHeadChar(String str) {
		String convert = "";
		for (int j = 0; j < str.length(); j++) {
			char word = str.charAt(j);
			String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
			if (pinyinArray != null) {
				convert += pinyinArray[0].charAt(0);
			} else {
				convert += word;
			}
		}
		return convert;
	}
	
}
