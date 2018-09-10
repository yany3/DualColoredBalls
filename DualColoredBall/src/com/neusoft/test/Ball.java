package com.neusoft.test;

import java.sql.PreparedStatement;
import java.util.*;


public class Ball extends Users {
	public static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) throws Exception {
		ball();

	}

	

	int sumred = 0;// 定义变量用于接受购买者与机器号生成的红球有几个是相同的
	int sumblue = 0;// 定义蓝球是否相同
	public static TreeSet<Integer> buyRedList = new TreeSet<>();
	public static TreeSet<Integer> tempList = new TreeSet<Integer>();
	public static TreeSet<Object> newList = new TreeSet<Object>();
	public static TreeSet<Integer> numList = new TreeSet<>();
	private static int buyRed;
	private static int buyBlue;
	private static int sysBlueBall;
	private static int times;//倍数
	private static int redSum;//中奖红球数
	private static int blueSum;//中奖蓝球数
	private static int[] buyRedArr = new int[6];
	private static int[] sysRedArr = new int[6];

	public static void buy() throws Exception {// 手选
		for (int i = 0; i < 6; i++) {
			System.out.println("请输入第" + (i + 1) + "红球");
			System.out.println("1-33不可重复");
			setBuyRed(sc.nextInt());
			buyRedList.add(getBuyRed());
		}
		System.out.println("请输入一个蓝球1-16");
		setBuyBlue(sc.nextInt());

		Iterator i = buyRedList.iterator();
		int j = 0;
		while (i.hasNext()) {
			buyRedArr[j] = (int) i.next();
			j++;

		}
		for (int j2 = 0; j2 < 6; j2++) {
			if (buyRedArr[j2] == 0) {
				System.out.println("选择了重复的红球,重新输入");
				mainMenu();
			} else {
				continue;
			}
		}
		

		try {
			PreparedStatement pstate = getCon().prepareStatement(
					"insert into USERBUY (BLUEBALL,REDBALL1,REDBALL2,REDBALL3,REDBALL4,REDBALL5,REDBALL6) values(?,?,?,?,?,?,?) ");
			pstate.setInt(1, buyBlue);
			pstate.setInt(2, buyRedArr[0]);
			pstate.setInt(3, buyRedArr[1]);
			pstate.setInt(4, buyRedArr[2]);
			pstate.setInt(5, buyRedArr[3]);
			pstate.setInt(6, buyRedArr[4]);
			pstate.setInt(7, buyRedArr[5]);
			pstate.executeUpdate();

		} catch (Exception e) {
			System.out.println("已经选择过这一组球了哦");
			mainMenu();

		}

		System.out.println("向数据库中导入选球成功!");
		for (int n : buyRedArr) {
			System.out.print(n + ",");
		}
		System.out.println("蓝球:" + getBuyBlue());
		System.out.println("请选择投注倍数(如无需加倍输入1)");
		setTimes(sc.nextInt());
		minusMoney(getTimes() * 2);
		ball();
		judge();
		

	}

	public static void ball() throws Exception {// 系统生成随机球

		for (int i = 1; i <= 33; i++) {
			numList.add(i);
		}
		Random r = new Random();

		int temp = 0;// 接收产生的随机数
		for (int i = 0; i < 6; i++) {

			temp = r.nextInt(numList.size()) + 1;
			if (!tempList.contains(temp)) {
				tempList.add(temp);
				newList.add(temp);
			} else {
				i--;
			}
		}
		newList.toArray();
		System.out.println("开奖中...........");
		setSysBlueBall(r.nextInt(7) + 1);
		System.out.print("红球为:");
		System.out.print(newList);
		System.out.println(" 蓝球为:" + getSysBlueBall());
		Iterator i = newList.iterator();
		int j = 0;
		while (i.hasNext()) {
			sysRedArr[j] = (int) i.next();
			j++;

		}
		PreparedStatement pstate = getCon().prepareStatement(
				"insert into SYSBALL (BLUEBALL,REDBALL1,REDBALL2,REDBALL3,REDBALL4,REDBALL5,REDBALL6) values(?,?,?,?,?,?,?) ");
		pstate.setInt(1, getSysBlueBall());
		pstate.setInt(2, sysRedArr[0]);
		pstate.setInt(3, sysRedArr[1]);
		pstate.setInt(4, sysRedArr[2]);
		pstate.setInt(5, sysRedArr[3]);
		pstate.setInt(6, sysRedArr[4]);
		pstate.setInt(7, sysRedArr[5]);
		pstate.executeUpdate();

	}

	public static void judge() throws Exception {
		
		for (int i = 0; i < 6; i++) {
			if (buyRedArr[i] == sysRedArr[i]) {
				setRedSum(getRedSum()+1);
			}
			if (getBuyBlue()==getSysBlueBall()) {
					setBlueSum(1);
			}
		}
		
		if (getRedSum() == 6 && getBlueSum() == 1) {// 判断中了几等奖并返回金额
			System.err.println("恭喜你中了"+getTimes()+"倍一等奖一千万! 奖金已存入账户!");
			addMoney(10000000*getTimes());
			addWin(10000000);
			userMenu();
		} else if (getRedSum() == 6 && getBlueSum() == 0) {
			System.err.println("恭喜你中了"+getTimes()+"倍二等奖五百万! 奖金已存入账户!");
			addMoney(5000000*getTimes());
			addWin(5000000);
			userMenu();

		} else if (getRedSum() == 5 && getBlueSum() == 1) {
			System.err.println("恭喜你中了"+getTimes()+"倍三等奖三千元! 奖金已存入账户!");
			addMoney(3000*getTimes());
			addWin(3000);
			userMenu();

		} else if ((getRedSum() == 5 && getBlueSum() == 0) || (getRedSum() == 4 && getBlueSum() == 1)) {
			System.err.println("恭喜你中了"+getTimes()+"倍四等奖二百元! 奖金已存入账户!");
			addMoney(200*getTimes());
			addWin(200);
			userMenu();

		} else if ((getRedSum() == 4 && getBlueSum() == 0) || (getRedSum() == 3 && getBlueSum() == 1)) {
			System.err.println("恭喜你中了"+getTimes()+"倍五等奖十元! 奖金已存入账户!");
			addMoney(10*getTimes());
			addWin(10);
			userMenu();

		} else if ((getRedSum() == 2 && getBlueSum() == 1) || (getRedSum() == 1 && getBlueSum() == 1)) {
			System.err.println("恭喜你中了"+getTimes()+"倍六等奖五元! 奖金已存入账户!");
			addMoney(5*getTimes());
			addWin(5);
			userMenu();

		} else {
			System.err.println("很遗憾,本次未中奖");
			userMenu();

		}
	}

	// public static void saveUserBall(){
	// buyRedList.pollFirst();
	//
	// }

	public static int getRedSum() {
		return redSum;
	}

	public static void setRedSum(int redSum) {
		Ball.redSum = redSum;
	}

	public static int getBlueSum() {
		return blueSum;
	}

	public static void setBlueSum(int blueSum) {
		Ball.blueSum = blueSum;
	}

	public static int getTimes() {
		return times;
	}

	public static void setTimes(int times) {
		Ball.times = times;
	}

	public static int getBuyRed() {
		return buyRed;
	}

	public static void setBuyRed(int buyRed) {
		Ball.buyRed = buyRed;
	}

	public static int getBuyBlue() {
		return buyBlue;
	}

	public static void setBuyBlue(int buyBlue) {
		Ball.buyBlue = buyBlue;
	}
	public int getSumred() {
		return sumred;
	}

	public void setSumred(int sumred) {
		this.sumred = sumred;
	}

	public int getSumblue() {
		return sumblue;
	}

	public void setSumblue(int sumblue) {
		this.sumblue = sumblue;
	}
	public static int getSysBlueBall() {
		return sysBlueBall;
	}

	public static void setSysBlueBall(int sysBlueBall) {
		Ball.sysBlueBall = sysBlueBall;
	}


}
