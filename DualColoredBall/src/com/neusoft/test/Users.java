package com.neusoft.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Users {

	public static Scanner sc = new Scanner(System.in);
	private static String name;
	private static String pass;
	private static String mail;
	// private static int defaultMoney=1000;
	// public static int dm=1000;

	private static ResultSet rs;
	private static int money;
	public static int[] BuyRed;
	public static int BuyBlue;

	public static int getMoney() throws SQLException {
		money = rs.getInt("money");
		return money;
	}

	public static void setMoney(int money) throws Exception {

		Users.money = money + getMoney();
	}

	static {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			String url = "jdbc:oracle:thin:@127.0.0.1:1521:orcl";
			String username = "scott";
			String password = "tiger";

			Users.con = DriverManager.getConnection(url, username, password);
		} catch (Exception e) {
			System.out.println(e + "数据库链接失败");
		}

	}

	public static ResultSet getRs() {
		return rs;
	}

	public static void setRs(ResultSet rs) {
		Users.rs = rs;
	}

	public static void setPstate() throws Exception {
		PreparedStatement pstate = con.prepareStatement("select * from users where name=? and password=?");
		pstate.setString(1, getName());
		pstate.setString(2, getPass());
		setRs(pstate.executeQuery());
	}

	public static void getPstate() throws Exception {
		PreparedStatement pstate = con.prepareStatement("select * from users where name=? and password=?");
		pstate.setString(1, getName());
		pstate.setString(2, getPass());
		getRs();
	}

	public static String getName() {
		return name;
	}

	public static void setName(String name) {
		Users.name = name;
	}

	public static String getPass() {
		return pass;
	}

	public static void setPass(String pass) {
		Users.pass = pass;
	}

	protected static Connection con;

	public static Connection getCon() {
		return con;
	}

	public static String getMail() {
		return mail;
	}

	public static void setMail(String mail) {
		Users.mail = mail;
	}
	// public static int getDefaultMoney() {
	// return defaultMoney;
	// }

	// public static void setDefaultMoney(int defaultMoney) {
	// Users.defaultMoney = defaultMoney;
	// }
	public static void mainMenu() throws Exception {
		System.out.println("*************************************");
		System.out.println("*****\t\tWELCOME\t\t*****");
		System.out.println("*****\t\t请先选择\t\t*****");
		System.out.println("*****\t\t1.登陆\t\t*****");
		System.out.println("*****\t\t2.注册\t\t*****");
		// System.out.println("*************************************");
		System.out.println("*************************************");
		// defineDM();
		int choose = sc.nextInt();
		switch (choose) {
		case 1:
			login();
			break;
		case 2:
			register();
			break;
		default:
			System.out.println("输入有误,重新输入");
			mainMenu();
			break;
		}

	}

	public static void menu() throws Exception {// 主菜单

		System.out.println("********************************");
		System.out.println("*****1.用户界面*****");
		System.out.println("*****2.管理员界面*****");
		System.out.println("********************************");
		int flag = sc.nextInt();
		if (flag == 1) {
			mainMenu();
		} else if (flag == 2) {
			Admin.login();
		}

	}
	// public static void defineDM() throws Exception{
	// PreparedStatement pstate = con.prepareStatement("update users set money=?
	// where name=? ");
	// pstate.setInt(1, getDefaultMoney());
	// pstate.setString(2, getName());
	// setRs(pstate.executeQuery());
	// setMoney(getDefaultMoney());
	// }

	public static void login() throws Exception {// 登陆
		System.out.println("输入用户名");
		setName(sc.next());
		System.out.println("输入密码");
		setPass(sc.next());
		setPstate();
		if (rs.next()) {
			System.out.println("恭喜" + rs.getString("name") + "登陆成功!");
			userMenu();
		} else {
			System.out.println("登录失败,用户名或密码错误");
			login();
		}

	}

	public static void register() throws Exception {// 注册
		System.out.println("输入用户名");
		setName(sc.next());
		System.out.println("输入密码");
		setPass(sc.next());
		System.out.println("输入你的邮箱");
		String mail = sc.next();

		try {
			PreparedStatement pstate = con.prepareStatement("insert into users (name,password,mail) values(?,?,?)");
			pstate.setString(1, getName());
			pstate.setString(2, getPass());
			pstate.setString(3, mail);
			pstate.executeUpdate();
		} catch (Exception e) {
			System.out.println("用户名或邮箱已被注册!");
			register();
		}
		System.out.println("注册成功,即将返回登录界面");
		mainMenu();
	}

	public static void userMenu() throws Exception {

		System.out.println("1.购彩");
		System.out.println("2.查询个人信息");
		System.out.println("3.修改个人信息");
		System.out.println("4.充值");
		System.out.println("5.查询往期中奖号码");
		System.out.println("6.退出");
		int i = sc.nextInt();
		if (i == 1) {

			Ball.buy();
			// Buy.BuyBlue();
		} else if (i == 2) {
			getPstate();
			System.out.println("您的个人信息如下");
			System.out.println(
					"用户名:" + rs.getString("name") + "\t邮箱:" + rs.getString("mail") + "\t总金额" + rs.getInt("money"));
			userMenu();

		} else if (i == 3) {
			updateUserInfo();
		} else if (i == 4) {
			System.out.println("输入充值金额");
			int money1 = sc.nextInt();
			addMoney(money1);
			userMenu();
		} else if (i == 5) {
			passWin();
		} else if (i == 6) {
			System.exit(0);
			con.close();
		}

		else {
			System.out.println("输入有误,重新输入");
			userMenu();
		}
	}

	public static int minusMoney(int mm) throws Exception {
		PreparedStatement pstate = con
				.prepareStatement("update users set money =money-?,mosub =?||TO_CHAR(-?) where name=?");
		pstate.setInt(1, mm);
		pstate.setInt(2, getMoney());
		pstate.setInt(3, mm);
		pstate.setString(4, getName());
		pstate.executeUpdate();
		if (getMoney() > 0) {
			setMoney(-mm);
			System.out.println("本次消费" + mm + "元");
			pstate.executeUpdate();

		} else {
			System.out.println(getMoney());
			System.out.println("余额不足,请及时充值,即将返回主界面");

			userMenu();
		}

		return getMoney();
	}

	private static String String(int mm) {
		// TODO Auto-generated method stub
		return null;
	}

	public static int addMoney(int am) throws Exception {
		PreparedStatement pstate = con
				.prepareStatement("update users set money =money+?,moadd=?||'+' ||TO_CHAR(?) where name=?");
		pstate.setInt(1, am);
		pstate.setInt(2, getMoney());
		pstate.setInt(3, am);
		pstate.setString(4, getName());
		setMoney(am);
		pstate.executeUpdate();

		System.out.println("本次充值了:" + am + "元");
		pstate.executeUpdate();
		System.out.println("充值后请重新登录");
		mainMenu();

		return getMoney();
	}

	public static int addWin(int aw) throws Exception {
		PreparedStatement pstate = con.prepareStatement("update users set winmo=TO_CHAR(?)||'+'  where name=?");
		pstate.setInt(1, aw);
		pstate.setString(2, getName());
		// setMoney(aw);
		pstate.executeUpdate();

		return getMoney();
	}

	public static void updateUserInfo() throws Exception {
		PreparedStatement pstate = con.prepareStatement("update users set name =?, password=?, mail=? where name=?");
		pstate.setString(4, getName());
		System.out.println("请输入修改后的用户名");
		String newName = sc.next();
		setName(newName);
		pstate.setString(1, newName);
		System.out.println("请输入修改后的密码");
		String newPass = sc.next();
		pstate.setString(2, newPass);
		setPass(newPass);
		System.out.println("请输入修改后的邮箱");
		String newMail = sc.next();
		pstate.setString(3, newMail);
		setMail(newMail);
		pstate.executeUpdate();
		System.out.println("用户信息修改成功");
		mainMenu();

	}

	public static void passWin() throws Exception {
		PreparedStatement pstate = con.prepareStatement("select * from sysball");
		ResultSet rs1 = pstate.executeQuery();
		while (rs1.next()) {
			System.out.println(rs1.getInt("redball1") + "," + rs1.getInt("redball2") + "," + rs1.getInt("redball3")
					+ "," + rs1.getInt("redball4") + "," + rs1.getInt("redball5") + "," + rs1.getInt("redball6") + ","
					+ rs1.getInt("blueball"));
		}
	}

	public static void closeQuery(ResultSet rs, Connection con, Statement state) throws Exception {
		if (state != null) {
			state.close();
		}
		if (con != null) {
			state.close();
		}
		if (rs != null) {
			rs.close();
		}
	}

	public static void close(Connection con, Statement state) throws Exception {
		if (state != null) {
			state.close();
		}
		if (con != null) {
			state.close();
		}
		// if (rs!=null) {
		// rs.close();
		// }
	}

}
