package database;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Map.Entry;

import basic.Client;
import basic.Connect;
import basic.ContactPerson;
import basic.Group;
import listviewCell.GroupCell;
import staticStuff.Data;

public class SQLDemo {

	public SQLDemo() {

	}

	// JDBC 驱动名及数据库 URL
	static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	// schema的名字 contactjava
	static final String DB_URL = "jdbc:mysql://localhost:3306/contactjava?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC";

	// 数据库的用户名与密码，需要根据自己的设置
	static final String USER = "root";
	static final String PASS = "123456";

	Connection conn = null;
	Statement stmt = null;

	public void initDatabase() {

		// 注册 JDBC 驱动
		try {
			Class.forName(JDBC_DRIVER);
			// 打开链接
			System.out.println("连接数据库...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			// 执行查询
			System.out.println(" 实例化Statement对象...");
			stmt = conn.createStatement();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	// query model

	public void closeDatabase() {
		try {
			stmt.close();
			conn.close();
		} catch (SQLException se) {
			// 处理 JDBC 错误
			se.printStackTrace();
		} catch (Exception e) {
			// 处理 Class.forName 错误
			e.printStackTrace();
		} finally {
			// 关闭资源
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			} // 什么都不做
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		System.out.println("database close");
	}

//启动app, 从数据库导入数据到容器类**********************************************************************************************************************************************
	public void getDataFromDBClient() {
		// 导入所有client信息 (未加入照片)
		try {
			String sql;
			sql = "SELECT account,password,firstname,lastname,email,birthday,gender," // 一定存在的数据
					+ "address,myjob,myskill,edubackground" // 不一定存在的数据
					+ " FROM client";
			ResultSet rs;
			rs = stmt.executeQuery(sql);

			// 展开结果集数据库
			while (rs.next()) {
				// 通过字段检索
				String account = rs.getString("account");
				String password = rs.getString("password");
				String firstname = rs.getString("firstname");
				String lastname = rs.getString("lastname");
				String email = rs.getString("email");
				String birthday = rs.getString("birthday");
				int gender = rs.getInt("gender");
				// -----------------------------
				String address = rs.getString("address");
				String myjob = rs.getString("myjob");
				String myskill = rs.getString("myskill");
				String edubackground = rs.getString("edubackground");

				// 新建用户对象,加入到staticStuff.Data.clientList
				// 从DB里导入的对象，inDB为true
				Client client = new Client(firstname, lastname, account, password, email, birthday, gender, true);
				Data.clientList.put(account, client);

				if (!(address == null)) {
					client.setAddress(address);
				}
				if (!(myjob == null)) {
					client.setMyJob(myjob);
				}
				if (!(myskill == null)) {
					client.setMySkill(myskill);
				}
				if (!(edubackground == null)) {
					client.setEduBackground(edubackground);
				}
				// 输出数据
				// System.out.println(account+" "+password+" "+firstname+" "+lastname+"
				// "+email+" "+birthday+" "+gender);
			}
			// 完成后关闭
			rs.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void getDataFromDBLinkman(String clientAccount) {
		// 根据用户的登入账号导入 (未加入照片)
		PreparedStatement ps = null;
		try {

			String sql;
			sql = "SELECT id,name,telephone,phone,emergency,emer_number,email,"
					+ "personalpage,birthday,company,address,postcode,description FROM linkman "
					+ "where client_account=?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, clientAccount);
			ResultSet rs;
			rs = ps.executeQuery();

			// 展开结果集数据库
			while (rs.next()) {
				// 通过字段检索
				String id = rs.getString("id");
				String name = rs.getString("name");
				// --------------------------------
				String telephone = rs.getString("telephone");
				String phone = rs.getString("phone");
				String emergency = rs.getString("emergency");
				String emer_number = rs.getString("emer_number");
				String email = rs.getString("email");
				String personalpage = rs.getString("personalpage");
				String birthday = rs.getString("birthday");
				String company = rs.getString("company");
				String address = rs.getString("address");
				String postcode = rs.getString("postcode");
				String description = rs.getString("description");

				// 新建ContactPerson对象,加入到staticStuff.Data.contactList
				ContactPerson cp = new ContactPerson(id, name, true);
				Data.contactList.put(id, cp);

				if (!(telephone == null)) {
					cp.setTelephone(telephone);
				}
				if (!(phone == null)) {
					cp.setPhone(phone);
				}
				if (!(emergency == null)) {
					cp.setEmergency(emergency);
				}
				if (!(emer_number == null)) {
					cp.setEmergencyNumber(emer_number);
				}
				if (!(email == null)) {
					cp.setEmail(email);
				}
				if (!(personalpage == null)) {
					cp.setPersonalPage(personalpage);
				}
				if (!(birthday == null)) {
					cp.setBirthday(birthday);
				}
				if (!(company == null)) {
					cp.setCompany(company);
				}
				if (!(address == null)) {
					cp.setAddress(address);
				}
				if (!(postcode == null)) {
					cp.setPostcode(postcode);
				}
				if (!(description == null)) {
					cp.setDescription(description);
				}

			}
			// 完成后关闭
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (null != ps) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void getDataFromDBGroup(String clientAccount) {
		// 根据用户的登入账号导入
		PreparedStatement ps = null;
		try {
			String sql;
			sql = "SELECT groupname,numberofpeople,ifstar" + " FROM contactjava.group where clientaccount= ?";
			ResultSet rs;
			ps = conn.prepareStatement(sql);
			ps.setString(1, clientAccount);
			rs = ps.executeQuery();

			// 展开结果集数据库
			while (rs.next()) {
				// 通过字段检索
				String groupname = rs.getString("groupname");
				int numberOfPeople = rs.getInt("numberofpeople"); // default 0
				
			//	System.out.println("read"+numberOfPeople);
				
				int ifStar = rs.getInt("ifstar"); // 0 false 1 true

				// 新建Group对象,加入到staticStuff.Data.groupList
				Group group = new Group(groupname, true);
				Data.groupsList.put(groupname, group);

				group.setNumberOfPeople(numberOfPeople);
				if (ifStar != 0) {
					group.setIfStar(true);
				}
				// 输出数据
				// System.out.println(groupname);

			}
			// 完成后关闭
			rs.close();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (null != ps) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void getDataFromDBConnect(String clientAccount) {
		PreparedStatement ps = null;
		try {
			// 相应用户的所有connect
			String sql;
			sql = "SELECT linkman_id,group_name " + "FROM connect,contactjava.group "
					+ "WHERE groupname=group_name and clientaccount=?";
			ResultSet rs;
			ps = conn.prepareStatement(sql);
			ps.setString(1, clientAccount);
			rs = ps.executeQuery();

			// 展开结果集数据库
			while (rs.next()) {
				// 通过字段检索
				String linkmanid = rs.getString("linkman_id");
				String groupname = rs.getString("group_name");

				// 新建用户对象,加入到staticStuff.Data.connectList
				// 从DB里导入的对象，inDB为true

				Connect cont = new Connect(linkmanid, groupname, true);
				Data.connectList.add(cont);

				// 输出数据
				// System.out.println(linkmanid+" "+groupname);

			}
			// 完成后关闭
			rs.close();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (null != ps) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

// 关闭app， 将容器类的数据导入到数据库------根据关系依赖的先后保存***************************************************************************************************

	/**
	 * 先存Client-->Linkman---->group---->connect
	 * 
	 * 1. 如果isDelete=false，inDB=true， 更新数据回数据库 2.如果isDelete=false，inDB=false，向表中插入新行
	 * 3.如果isDelete=true, inDB=true, 从数据库删除该行 4.如果isDelete=true, inDB=false,
	 * 从容器类里面删除
	 */

	// 保存Client
	public void saveClientToDB() {

		Iterator<Entry<String, Client>> iter = Data.clientList.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, Client> entry = iter.next();
			Boolean inDB = entry.getValue().isInDB();
			Boolean isDelete = entry.getValue().isDelete();
			Client c = entry.getValue();

			// 1. inDB=false && isDelete=true----从容器类删除
			if (!inDB && isDelete) {
				Data.clientList.remove(entry.getKey());
			}
			// 2. inDB=false && isDelete=false-----插入新行
			else if (!inDB && !isDelete) {
				String sql;
				PreparedStatement ps = null;
				sql = "insert into client(account,password,firstname,lastname,email,birthday,gender,address,myjob,myskill,edubackground) "
						+ "values (?,?,?,?,?,?,?,?,?,?,?)";
				try {
					ps = conn.prepareStatement(sql);
					ps.setString(1, entry.getKey());
					ps.setString(2, c.getPassword());
					ps.setString(3, c.getFirstName());
					ps.setString(4, c.getLastName());
					ps.setString(5, c.getEmail());
					ps.setString(6, c.getBirthday());
					ps.setInt(7, c.getGender());
					ps.setString(8, c.getAddress());
					ps.setString(9, c.getMyJob());
					ps.setString(10, c.getMySkill());
					ps.setString(11, c.getEduBackground());
					// ----------------------------------
					ps.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					if (null != ps) {
						try {
							ps.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				}

			}
			// 3. inDB=true && isDelete=true-----DB删除该行
			else if (inDB && isDelete) {
				PreparedStatement ps = null;
				String sql;
				sql = "delete from client where account=?";
				try {
					ps = conn.prepareStatement(sql);
					ps.setString(1, c.getAccount());
					ps.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					if (null != ps) {
						try {
							ps.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				}
			}
			// 4. inDB=true && isDelete=false update table
			else if (inDB && !isDelete) {
				PreparedStatement ps = null;
				// 除了account和照片之外都更新
				String sql = "update contactjava.client"
						+ " set password= ? , firstname=?, lastname=? , email= ?,birthday=?, "
						+ "gender=?, address=?, myjob=?,myskill=?, edubackground=? " + "WHERE account=?";
				try {
					ps = conn.prepareStatement(sql);
					ps.setString(1, c.getPassword());
					ps.setString(2, c.getFirstName());
					ps.setString(3, c.getLastName());
					ps.setString(4, c.getEmail());
					ps.setString(5, c.getBirthday());
					ps.setInt(6, c.getGender());
					ps.setString(7, c.getAddress());
					ps.setString(8, c.getMyJob());
					ps.setString(9, c.getMySkill());
					ps.setString(10, c.getEduBackground());
					ps.setString(11, c.getAccount());
					// ----------------------------------
					ps.executeUpdate();
				} catch (SQLException e1) {
					e1.printStackTrace();
				} finally {
					if (null != ps) {
						try {
							ps.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

	// 保存Linkman联系人 client account
	public void saveLinkmanToDB(String account) {

		Iterator<Entry<String, ContactPerson>> iter = Data.contactList.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, ContactPerson> entry = iter.next();
			Boolean inDB = entry.getValue().isInDB();
			Boolean isDelete = entry.getValue().isDelete();
			ContactPerson cp = entry.getValue();

			// 1. inDB=false && isDelete=true----从容器类删除
			if (!inDB && isDelete) {
				Data.contactList.remove(entry.getKey());
			}
			// 2. inDB=false && isDelete=false-----插入新行（要加clientAccount）
			else if (!inDB && !isDelete) {
				PreparedStatement ps=null;
				String sql;
				sql = "insert into linkman(id,name,telephone,phone,emergency,emer_number,email,"
						+ "personalpage,birthday,company,address,postcode,description,client_account) " 
						+ "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				try {
					ps=conn.prepareStatement(sql);
					
					ps.setString(1,cp.getId() );
					ps.setString(2, cp.getName());
					ps.setString(3, cp.getTelephone());
					ps.setString(4, cp.getPhone());
					ps.setString(5, cp.getEmergency());
					ps.setString(6, cp.getEmergencyNumber());
					ps.setString(7, cp.getEmail());
					ps.setString(8, cp.getPersonalPage());
					ps.setString(9, cp.getBirthday());
					ps.setString(10, cp.getCompany());
					ps.setString(11, cp.getAddress());
					ps.setString(12, cp.getPostcode());
					ps.setString(13, cp.getDescription());
					ps.setString(14, account);
					
					ps.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				}finally {
					if (null != ps) {
						try {
							ps.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				}
				
			}
			// 3. inDB=true && isDelete=true-----DB删除该行（删除不存在的行没有影响）
			else if (inDB && isDelete) {
				PreparedStatement ps = null;
				String sql;
				sql = "delete from linkman where id= ? ";
				try {
					ps = conn.prepareStatement(sql);
					ps.setString(1, cp.getId());
					ps.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					if (null != ps) {
						try {
							ps.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				}
			}
			// 4. inDB=true && isDelete=false update table
			else if (inDB && !isDelete) {
				PreparedStatement ps = null;
				// 除了id,client_account和照片之外都更新

				String sql = "update contactjava.linkman"
						+ " set name= ? , telephone=?, phone=? , emergency= ?,emer_number=?, "
						+ "email=?,personalpage =?, birthday=?,company=?, address=?,postcode=?,description=? "
						+ "WHERE id=?";
				try {
					ps = conn.prepareStatement(sql);

					ps.setString(1, cp.getName());
					ps.setString(2, cp.getTelephone());
					ps.setString(3, cp.getPhone());
					ps.setString(4, cp.getEmergency());
					ps.setString(5, cp.getEmergencyNumber());
					ps.setString(6, cp.getEmail());
					ps.setString(7, cp.getPersonalPage());
					ps.setString(8, cp.getBirthday());
					ps.setString(9, cp.getCompany());
					ps.setString(10, cp.getAddress());
					ps.setString(11, cp.getPostcode());
					ps.setString(12, cp.getDescription());
					ps.setString(13, cp.getId());

					// ----------------------------------
					ps.executeUpdate();
				} catch (SQLException e1) {
					e1.printStackTrace();
				} finally {
					if (null != ps) {
						try {
							ps.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

	// 保存Group
	public void saveGroupToDB(String account) {

		Iterator<Entry<String, Group>> iter = Data.groupsList.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, Group> entry = iter.next();
			Boolean inDB = entry.getValue().isInDB();
			Boolean isDelete = entry.getValue().isDelete();
			Group g = entry.getValue();
			
		//	System.out.println("1.save:"+g.getNumberOfPeople());

			// 1. inDB=false && isDelete=true----从容器类删除
			if (!inDB && isDelete) {
				Data.groupsList.remove(entry.getKey());
			}
			// 2. inDB=false && isDelete=false-----插入新行（要加clientAccount）
			else if (!inDB && !isDelete) {
				String sql;
				int ifStar = 0;
				if (g.isIfStar()) {
					ifStar = 1;
				}
				sql = "insert into contactjava.group(groupname,numberofpeople,ifstar,clientaccount) values (?,?,?,?);";
				PreparedStatement ps = null;
				try {
					ps=conn.prepareStatement(sql);
					ps.setString(1, g.getGroupName());
					ps.setInt(2,g.getNumberOfPeople());
					
			//		System.out.println("2.insert save:"+g.getNumberOfPeople());
					
					ps.setInt(3,ifStar);
					ps.setString(4, account);
					
					ps.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				finally {
					if (null != ps) {
						try {
							ps.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				}
			
			}
			// 3. inDB=true && isDelete=true-----DB删除该行（删除不存在的行没有影响）
			else if (inDB && isDelete) {
				PreparedStatement ps = null;
				String sql;
				sql = "delete from contactjava.group where groupname= ? and clientaccount= ? ;";
				try {
					ps=conn.prepareStatement(sql);
					ps.setString(1, g.getGroupName());
					ps.setString(2, account);
					ps.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				finally {
					if (null != ps) {
						try {
							ps.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				}
			
			}
			// 4. inDB=true && isDelete=false update table
			else if (inDB && !isDelete) {
				PreparedStatement ps = null;
				int ifStar = 0;
				if (g.isIfStar()) {
					ifStar = 1;
				}
				String sql = "update contactjava.group" + " set numberofpeople= ? , ifstar=? "
						+ "where groupname= ? and clientaccount= ? ;";
				try {
					ps = conn.prepareStatement(sql);
					ps.setInt(1, g.getNumberOfPeople());
					
					//System.out.println("update save:"+g.getNumberOfPeople());
					
					ps.setInt(2, ifStar);
					ps.setString(3, g.getGroupName());
					ps.setString(4, account);
					// ----------------------------------
					ps.executeUpdate();
				} catch (SQLException e1) {
					e1.printStackTrace();
				} finally {
					if (null != ps) {
						try {
							ps.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				}
				
			}
		}

	}

	// 保存Connect
	public void saveConnectToDB() {
		Iterator<Connect> iter = Data.connectList.iterator();
		while (iter.hasNext()) {
			Connect ct = (Connect) iter.next();
			Boolean inDB = ct.isInDB();
			Boolean isDelete = ct.isDelete();

			// isDelete=true, DB和容器类都删除
			if (isDelete) {
				Data.connectList.remove(ct);
				String sql;
				PreparedStatement ps = null;
				sql = "delete from contactjava.connect" + " where linkman_id= ? and group_name= ? ;";
				try {
					ps = conn.prepareStatement(sql);
					ps.setString(1, ct.getLinkmanID());
					ps.setString(2, ct.getGroupName());
					ps.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					if (null != ps) {
						try {
							ps.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				}
			} else {// isDelete=false---- inDB=true不用管,inDB=false 表中插入新行
				if (!inDB) {
					PreparedStatement ps = null;
					String sql = "insert into contactjava.connect(linkman_id,group_name) " + "values ( ? , ? ) ;";
					try {
						ps = conn.prepareStatement(sql);
						ps.setString(1, ct.getLinkmanID());
						ps.setString(2, ct.getGroupName());
						ps.executeUpdate();
					} catch (SQLException e) {
						e.printStackTrace();
					} finally {
						if (null != ps) {
							try {
								ps.close();
							} catch (SQLException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		}
	}

//  ***************************图片存取--修改已经存在数据库的用户/联系人照片******************************

	/**
	 * 每个用户的图片根据account命名， linkman的图片根据id命名(假设只有一张照片)
	 */
	// 将图片插入数据库(用户) 图片大小小于4M
	public void writeClientImageToDB(String account) {
		String path = "resources/clientPhoto/" + account + ".jpg"; // 需要插入的图片路径

		PreparedStatement ps = null;
		FileInputStream in = null;
		try {
			in = ImageUtil.readImage(path);

			String sql = "update client set photo=? where account= ? ;";
			ps = conn.prepareStatement(sql);
			ps.setBinaryStream(1, in, in.available()); // 第三个？，inputStream，可读剩余字节长
			ps.setString(2, account);
			// ps.setString(1, account); //第一个？，插入的值
			// ---------------------------------------
			/**
			 * 函数解释 void setBinaryStream​(int parameterIndex, InputStream x, int length)
			 * 
			 */
			// ----------------------------------------
			int count = ps.executeUpdate();
			if (count > 0) {
				System.out.println("插入成功！");
			} else {
				System.out.println("插入失败！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			if (null != ps) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// 将图片插入数据库(联系人) 图片大小小于4M
	public void writeLinkmanImageToDB(String id) {
		String path = "resources/linkmanPhoto/" + id + ".jpg"; // 需要插入的图片路径

		PreparedStatement ps = null;
		FileInputStream in = null;
		try {
			in = ImageUtil.readImage(path);

			String sql = "update linkman set photo = ? where id= ? ;";
			ps = conn.prepareStatement(sql);
			ps.setBinaryStream(1, in, in.available()); // 第三个？，inputStream，可读剩余字节长
            ps.setString(2, id);
			// ps.setString(1, account); //第一个？，插入的值
			// ---------------------------------------
			/**
			 * 函数解释 void setBinaryStream​(int parameterIndex, InputStream x, int length)
			 * 
			 */
			// ----------------------------------------
			int count = ps.executeUpdate();
			if (count > 0) {
				System.out.println("插入成功！");
			} else {
				System.out.println("插入失败！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			if (null != ps) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// 读取数据库中图片(用户）
	public void readClientImageFromDB(String account) {
		String targetPath = "resources/clientPhoto/" + account + ".jpg"; // 从数据库中读取，放到本地文件夹
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			String sql = "select photo from client where account = ? ;";
			ps = conn.prepareStatement(sql);
			ps.setString(1, account);
			rs = ps.executeQuery();

			while (rs.next()) {
				InputStream in = rs.getBinaryStream("photo");
				// java.sql.ResultSet.getBinaryStream(String columnLabel)
				ImageUtil.readBin2Image(in, targetPath);
			}
			// -----------------------------------------------------
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// 读取数据库中图片(联系人）
	public void readLinkmanImageFromDB(String id) {
		String targetPath = "resources/linkmanPhoto/" + id + ".jpg"; // 从数据库中读取，放到本地文件夹
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			String sql = "select photo from linkman where id = ? ;" ;
			ps = conn.prepareStatement(sql);
			ps.setString(1, id);
			rs = ps.executeQuery();

			while (rs.next()) {
				InputStream in = rs.getBinaryStream("photo");
				// java.sql.ResultSet.getBinaryStream(String columnLabel)
				ImageUtil.readBin2Image(in, targetPath);
			}
			// -----------------------------------------------------
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
