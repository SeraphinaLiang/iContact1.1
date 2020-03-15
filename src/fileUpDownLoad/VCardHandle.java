package fileUpDownLoad;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map.Entry;
import javax.imageio.stream.FileImageOutputStream;

import basic.Connect;
import basic.ContactPerson;
import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.VCardVersion;
import ezvcard.io.StreamWriter;
import ezvcard.io.text.VCardWriter;
import ezvcard.parameter.ImageType;
import ezvcard.property.Address;
import ezvcard.property.Birthday;
import ezvcard.property.Email;
import ezvcard.property.FormattedName;
import ezvcard.property.Note;
import ezvcard.property.Organization;
import ezvcard.property.Photo;
import ezvcard.property.Telephone;
import ezvcard.util.Utf8Reader;
import staticStuff.Data;
import staticStuff.utility;

public class VCardHandle {
	
	
	/**
	 * 尚未实现中文的存储
	 */

//*********************************************************************************************************
	// 以VCard的形式导出所有联系人信息
	public static void exportVCardFile(String dir) {

		Iterator<Entry<String, ContactPerson>> iter = Data.contactList.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, ContactPerson> entry = iter.next();
			ContactPerson cp = entry.getValue();

			//该联系人未被删除
			if (!cp.isDelete()) {
				VCard vc = new VCard();
				// ---------------------------
				vc.setFormattedName(new FormattedName(cp.getName()));// name
				vc.addEmail(new Email(cp.getEmail()));// email
				vc.addUrl(cp.getPersonalPage());// website

				Address add = new Address();
				add.setStreetAddress(cp.getAddress());
				add.setPostalCode(cp.getPostcode());// postcode
				vc.addAddress(add);// address

				vc.setBirthday(new Birthday(cp.getBirthday()));// birthday
				vc.addTelephoneNumber(new Telephone(cp.getTelephone()));// telephone
				vc.addTelephoneNumber(new Telephone(cp.getPhone()));// phone

				Organization org = new Organization();
				org.getValues().add(cp.getCompany());
				vc.setOrganization(org);// company

				vc.addNote("["+cp.getDescription()+"]\n["+cp.getEmergency()+"]\n["+cp.getEmergencyNumber()+
						"]\n[@"+utility.getGroupnameWithLinkmanID(cp.getId())+"]");// group

				//photo
				String path = "resources/linkmanPhoto/" + cp.getId() + ".png";
				File img = new File(path);
				try {
					Photo photo = new Photo(img, ImageType.PNG);
					vc.addPhoto(photo);
				} catch (IOException e) {

				}

				//生成vcf文件
				File file = new File(dir + "/" + cp.getId() + ".vcf");
				VCardWriter writer = null;

				try {
					writer = new VCardWriter(file, VCardVersion.V3_0);
					writer.write(vc);
					if (writer != null)
						writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}

		}

	}
//*********************************************************************************************************
	// 读取vcf(vcard)文件,并生成相应的联系人对象
	public static void readVCard(String filePath) {

		File file = new File(filePath);
//		Utf8Reader utf;
//		try {
//			utf=new Utf8Reader(file);
//		} catch (FileNotFoundException e1) {
//		}

		try {

			VCard vcard = Ezvcard.parse(file).first();

			String name = vcard.getFormattedName().getValue();
			// ------------------------------
			String email = null, address = null, birthday = null;

			if (vcard.getEmails().size() > 0) {
				email = vcard.getEmails().get(0).getValue();
			}
			if (vcard.getAddresses().size() > 0) {
				address = vcard.getAddresses().get(0).getStreetAddress();
			}
			if (vcard.getBirthday() != null) {
				birthday = vcard.getBirthday().getDate().toString();
			}

			// --------------------------
			String telephone = null, phone = null;
			if (vcard.getTelephoneNumbers().size() > 0) {
				telephone = vcard.getTelephoneNumbers().get(0).getText();
			}
			if (vcard.getTelephoneNumbers().size() > 0) {
				phone = vcard.getTelephoneNumbers().get(1).getText();
			}

			// -----------------------------
			String company = null, website = null, postcode = null;

			if (vcard.getOrganization().getValues().size()>0) {
				company = vcard.getOrganization().getValues().get(0);
			}
			if (vcard.getUrls().size()>0) {
				website = vcard.getUrls().get(0).getValue();
			}
			if (vcard.getAddresses().size()>0) {
				postcode = vcard.getAddresses().get(0).getPostalCode();
			}

			// ------------------------------
			String description = null, emergency = null, emerNum = null, group = null;
			String[] groups=null;

			if (vcard.getNotes().size()>0) {
				String message=vcard.getNotes().get(0).getValue();
				String[] mess=message.split("\n");
				
				description=mess[0].substring(1,mess[0].length()-2);
				emergency=mess[1].substring(1,mess[1].length()-2);
				emerNum=mess[2].substring(1,mess[2].length()-2);
				
				String ss=mess[3].substring(1,mess[3].length()-2);
				//根据分组创建connect
				groups=ss.split("@");
			}
			// ------------------------------------------------------------

			//不管导入联系人是否已经在数据库中，都创建新的联系人对象
			int i = (int) (Math.random() * 113 + Math.random() * 212 + name.hashCode());//新账号
			ContactPerson cp = new ContactPerson(i, name, false);
			
			cp.setEmail(email);
			cp.setAddress(address);
			cp.setBirthday(birthday);
			cp.setTelephone(telephone);
			cp.setPhone(phone);
			cp.setCompany(company);
			cp.setPersonalPage(website);
			cp.setPostcode(postcode);
			cp.setDescription(description);
			cp.setEmergency(emergency);
			cp.setEmergencyNumber(emerNum);

			//联系人进数据库
			Data.contactList.put(String.valueOf(i), cp);
			app.App.getSQLDemo().saveLinkmanToDB(Data.currentClient.getAccount());
			
			//根据组别生成connect，并扔进数据库
			if(groups.length>0) {
				for(int j=0;j<groups.length;j++) {
					String groupName=groups[j];
				
					//如果该用户有这个组，没有就不管
					if(Data.groupsList.containsKey(groupName)) {
						Data.connectList.add(new Connect(String.valueOf(i), groupName, false));
					}	
				}
			}
			app.App.getSQLDemo().saveConnectToDB();

			// 把照片扔进数据库里
			if (vcard.getPhotos().size() > 0) {
				Photo photo = vcard.getPhotos().get(0);
				byte[] img = photo.getData();
				String path = "resources/linkmanPhoto/" + cp.getId() + ".png";
				byte2image(img, path);

				File f = new File("resources/linkmanPhoto/" + cp.getId() + ".png");
				app.App.getSQLDemo().putLinkmanPhotoWithPath(f.getAbsolutePath(), cp.getId());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
//*********************************************************************************************************
	public static void byte2image(byte[] data, String path) {

		if (data.length < 3 || path.equals(""))

			return;

		try {

			FileImageOutputStream imageOutput = new FileImageOutputStream(new File(path));

			imageOutput.write(data, 0, data.length);

			imageOutput.close();

			System.out.println("Make Picture success,Please find image in " + path);

		}

		catch (Exception ex) {

			System.out.println("Exception: " + ex);

			ex.printStackTrace();

		}

	}

}
