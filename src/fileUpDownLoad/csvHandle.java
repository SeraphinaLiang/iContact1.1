package fileUpDownLoad;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javax.imageio.stream.FileImageOutputStream;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;

import basic.Connect;
import basic.ContactPerson;
import staticStuff.Data;
import staticStuff.utility;

public class csvHandle {

	public static void readCSV(String path) {
		File file = new File(path);

		try {

			String name = null, telephone = null, phone = null, emergency = null, emergencyNumber = null;
			String email = null, personalPage = null, birthday = null, company = null, address = null;
			String postcode = null, description = null, group = null;

			InputStreamReader is = new InputStreamReader(new FileInputStream(file), "GBK");
			CSVParser csvParser = new CSVParserBuilder().withSeparator('\t').build();
			CSVReader reader = new CSVReaderBuilder(is).withCSVParser(csvParser).build();
			String[] nextLine;

			while ((nextLine = reader.readNext()) != null) {
				for (int k = 0; k < nextLine.length; k++) {
					System.out.println(nextLine[k]);

					String[] line = nextLine[k].split("\n");

					// 对每个联系人操作************************************
					for (int i = 0; i < line.length; i++) {

						String[] info = line[i].split(",");

						// -------------------------
						name = info[0];
						telephone = info[1];
						phone = info[2];
						emergency = info[3];
						emergencyNumber = info[4];
						email = info[5];
						personalPage = info[6];
						birthday = info[7];
						company = info[8];
						address = info[9];
						postcode = info[10];
						description = info[11];
						group = info[12];

						String[] groups = group.split("@");// @group1@group2@

						// ---------生成对象并扔进数据库-------------------
						int id = (int) (Math.random() * 113 + Math.random() * 2012 + (int) name.hashCode());// 新账号
						ContactPerson cp = new ContactPerson(id, name, false);
						if (address != null) {
							cp.setAddress(address);
						}
						cp.setEmail(email);
						cp.setBirthday(birthday);
						cp.setCompany(company);
						cp.setDescription(description);
						cp.setEmergency(emergencyNumber);
						cp.setEmergencyNumber(emergencyNumber);
						cp.setPersonalPage(personalPage);
						cp.setPhone(phone);
						cp.setTelephone(telephone);
						cp.setPostcode(postcode);

						Data.contactList.put(String.valueOf(id), cp);
						// --------------生成相应的connect并扔进数据库----------------

						for (int j = 0; j < groups.length; j++) {
							// 如果有这个联系
							if (Data.groupsList.containsKey(groups[j])) {
								Connect con = new Connect(String.valueOf(id), groups[j], false);
								Data.connectList.add(con);
							}
						}

					app.App.getSQLDemo().saveSingleLinkmanToDB(Data.currentClient.getAccount(),cp);
						
						// 读取照片————————————————————————————————————————
					int index=path.lastIndexOf("\\");
					String sub=path.substring(0,index+1);
					File src=new File(sub+cp.getName()+".png");
					System.out.println(src.getAbsolutePath());
					app.App.getSQLDemo().putLinkmanPhotoWithPath(src.getAbsolutePath(), cp.getId());

					}
					// *******************************************************************

				}
			} 
          app.App.getSQLDemo().saveConnectToDB();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (CsvException e) {
			e.printStackTrace();
		}

	}

	public static void exportCSV(String dir) {
		try {

			File file = new File(dir + "/" + "information.csv");
			FileOutputStream fos = new FileOutputStream(file);
			OutputStreamWriter out = new OutputStreamWriter(fos, "GBK");

			Iterator<Entry<String, ContactPerson>> iter = Data.contactList.entrySet().iterator();
			while (iter.hasNext()) {
				Entry<String, ContactPerson> entry = iter.next();
				ContactPerson cp = entry.getValue();
				// *************************************************************************************
				// 该联系人未被删除
				if (!cp.isDelete()) {

					out.write(cp.getName() + ",");
					out.write(cp.getTelephone() + ",");
					out.write(cp.getPhone() + ",");
					out.write(cp.getEmergency() + ",");
					out.write(cp.getEmergencyNumber() + ",");
					out.write(cp.getEmail() + ",");
					out.write(cp.getPersonalPage() + ",");
					out.write(cp.getBirthday() + ",");
					out.write(cp.getCompany() + ",");
					out.write(cp.getAddress() + ",");
					out.write(cp.getPostcode() + ",");
					out.write(cp.getDescription() + ",");
					out.write("@" + utility.getGroupnameWithLinkmanID(cp.getId()) + ",");

					// photo-----------------------------------------------
					app.App.getSQLDemo().readLinkmanImageFromDB(cp.getId());

					String src = "resources/linkmanPhoto/" + cp.getId() + ".png";
					String des = dir + "/" + cp.getName() + ".png";
					copyFile(src,des);
//					File srcFile = new File(src);
//					File desFile = new File(des);
//					copyFileUsingFileStreams(srcFile, desFile);

					// end
					out.write("\n");
					out.flush();

				} // if

			} // while

//************************************************************************************
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

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

	public static void copyFileUsingFileStreams(File source, File dest) throws IOException {
		InputStream input = null;
		OutputStream output = null;
		try {
			input = new FileInputStream(source);
			output = new FileOutputStream(dest);
			byte[] buf = new byte[1024];
			int bytesRead;
			while ((bytesRead = input.read(buf)) > 0) {
				output.write(buf, 0, bytesRead);
			}
		} finally {
			input.close();
			output.close();
		}
	}

	public static void copyFile(String src, String target) {
		File srcFile = new File(src);
		File targetFile = new File(target);
		try {
			InputStream in = new FileInputStream(srcFile);
			OutputStream out = new FileOutputStream(targetFile);
			byte[] bytes = new byte[1024];
			int len = -1;
			while ((len = in.read(bytes)) != -1) {
				out.write(bytes, 0, len);
			}
			in.close();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("文件复制成功");
	}

}
