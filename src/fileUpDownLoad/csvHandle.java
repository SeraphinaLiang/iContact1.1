package fileUpDownLoad;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
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

public class csvHandle {

	public static void readCSV(String path) {
		File file = new File(path);

		try {

			String name = null, telephone = null, phone = null, emergency = null, emergencyNumber = null;
			String email = null, personalPage = null, birthday = null, company = null, address = null;
			String postcode = null, description = null,group=null;

			InputStreamReader is = new InputStreamReader(new FileInputStream(file), "GBK");
			CSVParser csvParser = new CSVParserBuilder().withSeparator('\t').build();
			CSVReader reader = new CSVReaderBuilder(is).withCSVParser(csvParser).build();
			String[] nextLine;

			while ((nextLine = reader.readNext()) != null) {
				String[] line = nextLine[0].split("\n");

				//对每个联系人操作************************************
				for (int i = 0; i < line.length; i++) {
					System.out.println(line[i]);
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
					group=info[12];
					
					String[] groups=group.split("@");// @group1@group2@
					
					//---------生成对象并扔进数据库-------------------
					int id = (int) (Math.random() * 113 + Math.random() * 212 + name.hashCode());//新账号
					ContactPerson cp=new ContactPerson(id, name, false);
					cp.setAddress(address);
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
					app.App.getSQLDemo().saveLinkmanToDB(Data.currentClient.getAccount());
					//--------------生成相应的connect并扔进数据库----------------
					
					for(int j=0;j<groups.length;j++) {
						//如果有这个联系
						if(Data.groupsList.containsKey(groups[j])) {
							Connect con=new Connect(String.valueOf(id), groups[j], false);
							Data.connectList.add(con);
						}
					}			
					app.App.getSQLDemo().saveConnectToDB();
					
					//读取照片————————————————————————————————————————
					
					byte[] img =info[13].getBytes("GBK");
					String imgPath = "resources/linkmanPhoto/" + cp.getId() + ".png";
					byte2image(img, imgPath);
					File f = new File("resources/linkmanPhoto/" + cp.getId() + ".png");
					app.App.getSQLDemo().putLinkmanPhotoWithPath(f.getAbsolutePath(), cp.getId());
					
				}
				//*******************************************************************

			}

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
		
		CSVWriter writer=null; 
	     // feed in your array (or convert your data to an array)
	     String[] entries = "first#second#third".split("#");
	     writer.writeNext(entries);
	    
	     
	     //------------------------------------------------------------------
		Iterator<Entry<String, ContactPerson>> iter = Data.contactList.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, ContactPerson> entry = iter.next();
			ContactPerson cp = entry.getValue();

			//该联系人未被删除
			if (!cp.isDelete()) {
				 File file = new File(dir + "/" + cp.getId() + ".csv");

            //  writer= new CSVWriter(new FileWriter( new OutputStreamReader(new FileOutputStream(file), "GBK")));
				
              try {
				BufferedWriter bw = new BufferedWriter(new FileWriter(file,true));
				
				
				
			} catch (IOException e) {
				e.printStackTrace();
			} 
				
				
				
			//	writer.flush();
			}
		}
	//	 writer.close();
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

}
