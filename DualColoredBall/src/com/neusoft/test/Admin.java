package com.neusoft.test;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Admin extends Users {
	// private static Connection con;
	private static String AdName;
	private static String AdPass;
	private static ResultSet rs;
	public static Scanner sc = new Scanner(System.in);

	public static void mainMenu() throws Exception {
		System.out.println("*************************************");
		System.out.println("*****\t\t欢迎您管理员\t\t*****");
		System.out.println("*****\t\t1.登陆\t\t*****");
		System.out.println("*************************************");
		int choose = sc.nextInt();
		switch (choose) {
		case 1:
			login();
			break;

		default:
			System.out.println("输入有误,重新输入");
			mainMenu();
			break;
		}
	}

	public static void setPstate() throws Exception {
		PreparedStatement pstate = con.prepareStatement("select * from ADMIN where name=? and password=?");
		pstate.setString(1, getAdName());
		pstate.setString(2, getAdPass());
		setRs(pstate.executeQuery());
	}

	public static void getPstate() throws Exception {
		PreparedStatement pstate = con.prepareStatement("select * from ADMIN where name=? and password=?");
		pstate.setString(1, getAdName());
		pstate.setString(2, getAdPass());
		getRs();
	}

	// public static Connection getCon() {
	// return con;
	// }
	//
	// public static void setCon(Connection con) {
	// Admin.con = con;
	// }

	public static ResultSet getRs() {
		return rs;
	}

	public static void setRs(ResultSet rs) {
		Admin.rs = rs;
	}

	public static void login() throws Exception {// 登陆
		System.out.println("输入用户名");
		setAdName(sc.next());
		System.out.println("输入密码");
		setAdPass(sc.next());
		setPstate();
		if (rs.next()) {
			System.out.println("恭喜" + rs.getString("name") + "登陆成功!");
			AdMenu();
		} else {
			System.out.println("登录失败,用户名或密码错误");
			login();
		}

	}

	public static void AdMenu() throws Exception {
		System.out.println("1.查看所有注册信息");
		System.out.println("2.查看指定人的充值记录,消费记录,中奖纪录");
		System.out.println("3.修改用户信息");
		System.out.println("4.禁止用户");
		System.out.println("5.查看往期所有人员的信息及奖金");
		int flag = sc.nextInt();
		switch (flag) {
		case 1:
			viewAll();
			break;
		case 2:
			viewSb();
			break;
		case 3:
			updateSb();
			break;
		case 4:

			break;
		case 5:
			selectAll();
			break;
		default:
			break;
		}
	}

	public static void viewAll() throws Exception {
		PreparedStatement pstate = con.prepareStatement("select * from USERS");
		ResultSet rs1 = pstate.executeQuery();
		while (rs1.next()) {
			System.out.println("用户名:\t" + rs1.getString("NAME") + "\t密码:\t" + rs1.getString("PASSWORD") + "\t邮箱:\t"
					+ rs1.getString("MAIL") + "\t余额" + rs1.getInt("MONEY"));
			System.out.println("查询完成!");
			Users.mainMenu();

		}
	}

	public static void viewSb() throws Exception {
		System.out.println("请输入需要查询的用户名");
		String name1 = sc.next();
		PreparedStatement pstate = con.prepareStatement("select MOADD,MOSUB,WINMO from USERS where NAME=?");
		pstate.setString(1, name1);

		ResultSet rs1 = pstate.executeQuery();
		while (rs1.next()) {
			System.out.println("充值记录:\t" + rs1.getString("MOADD") + "\t消费记录:\t" + rs1.getString("MOSUB") + "\t中奖纪录:\t"
					+ rs1.getString("WINMO"));
			System.out.println("查询完成!");
			Users.mainMenu();
		}

	}

	public static void updateSb() throws Exception {
		System.out.println("请输入需要修改的账户的用户名");
		String name1 = sc.next();
		PreparedStatement pstate = con.prepareStatement("update users set name =?, password=?, mail=? where name=?");
		pstate.setString(4, name1);
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
		Users.mainMenu();
	}

	public static void selectAll() throws Exception {
		PreparedStatement pstate = con.prepareStatement("select * from USERS");
		ResultSet rs1 = pstate.executeQuery();
		while (rs1.next()) {
			System.out.println("用户名:\t" + rs1.getString("NAME") + "\t密码:\t" + rs1.getString("PASSWORD") + "\t邮箱:\t"
					+ rs1.getString("MAIL") + "\t余额" + rs1.getInt("MONEY") + "\t充值记录:\t" + rs1.getString("MOADD")
					+ "\t消费记录:\t" + rs1.getString("MOSUB") + "\t中奖纪录:\t" + rs1.getString("WINMO"));
			System.out.println("查询完成!");
			Users.mainMenu();
		}
	}

	public static void banSb() throws Exception {// 限制登陆
		PreparedStatement pstate = con.prepareStatement("update users set name =? where name=?");
		System.out.println("请输入需要限制的账户的用户名");
		String name1 = sc.next();
		pstate.setString(1, name1+"*");
		pstate.setString(2, name1);
		pstate.executeUpdate();
		System.out.println("用户已被限制");
		
	}

	public static String getAdName() {
		return AdName;
	}

	public static void setAdName(String adName) {
		Admin.AdName = adName;
	}

	public static String getAdPass() {
		return AdPass;
	}

	public static void setAdPass(String adPass) {
		Admin.AdPass = adPass;
	}
}
