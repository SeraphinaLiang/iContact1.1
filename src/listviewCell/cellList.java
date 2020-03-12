package listviewCell;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import basic.ContactPerson;
import basic.Group;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import staticStuff.Data;
import staticStuff.utility;

public class cellList {
	private static ObservableList<GroupCell> groupList = FXCollections.observableArrayList();

	private static ObservableList<PersonCell> personList = FXCollections.observableArrayList();

	// *******************************************************************************************

// 根据 Data.HashMap<groupname,Group> 里面的内容更新界面
	public static ObservableList<GroupCell> refreshGroupList() {
		// 得到相应的hashmap
		HashMap<String, Group> groups = Data.groupsList;
		groupList.clear();// 清空原数据
		// 遍历
		Iterator<Entry<String, Group>> iter = groups.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, Group> entry = iter.next();
			Group g=entry.getValue();
			if (!g.isDelete()) {
				String s = (String) entry.getKey();
				int n=utility.searchContactsWithGroup(s).size();
			//	int n = (int) entry.getValue().getNumberOfPeople(); 这句有点毛病
				
				System.out.println("1.running: "+n);
				System.out.println("2.running normal: "+utility.searchContactsWithGroup(s).size());
				
				boolean star = (boolean) entry.getValue().isIfStar();
				groupList.add(new GroupCell(s, n, star));
			}

		}
		return groupList;
	}

	// 根据 Data.HashMap<String id,ContactPerson> 里面的内容更新界面(全部联系人)
	public static ObservableList<PersonCell> refreshPersonList() {
		// 得到相应的hashmap
		HashMap<String, ContactPerson> people = Data.contactList;
		personList.clear();// 清空原数据
		// 遍历
		Iterator<Entry<String, ContactPerson>> iter = people.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, ContactPerson> entry = iter.next();
			ContactPerson c = entry.getValue();
			if (!c.isDelete()) {
				PersonCell cell = new PersonCell(c.getName(), c.getEmail(), c.getPhone(), c.getAddress(),
						utility.getGroupnameWithLinkmanID(c.getId()));
				cell.setTheId(c.getId());
				personList.add(cell);
			}

		}
		return personList;
	}

}
