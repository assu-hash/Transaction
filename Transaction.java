package com.tap.app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Savepoint;
import java.util.Scanner;


public class Transaction {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Scanner s = new Scanner(System.in);
		
		String url ="jdbc:mysql://localhost:3306/company";
		String user="root";
		String password="assu@6856";
		
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection Con = DriverManager.getConnection(url,user,password);
			
			String select_Query = "Select * from employee where Acc_num = ? and pin = ?";
			
			PreparedStatement p_stmt = Con.prepareStatement(select_Query);
			
			System.out.println("Enter the acc number: ");
			int acc_no = s.nextInt();
			
			System.out.println("Entre the pin no: ");
			int pin = s.nextInt();
			
			p_stmt.setInt(1, acc_no);
			p_stmt.setInt(2, pin);
			
			ResultSet res = p_stmt.executeQuery();
			
			res.next();
			System.out.println("welcome "+res.getString("names"));
			System.out.println("Your available balance: "+res.getInt("balance"));
			
			Con.setAutoCommit(false);
			Savepoint save = Con.setSavepoint();
			
			
			System.out.println("<------------Transfer Model ------------>");
			System.out.println("Entre the amount that should transfer");
			int t_amt = s.nextInt();
			System.out.println("Entre the benfical/transferd acc_number that should credited");
			int ben_acc = s.nextInt();
		
			
			String debited_Query = " update employee set balance = balance-? where acc_num = ?;";
			PreparedStatement p_stmt1 = Con.prepareStatement(debited_Query);
			p_stmt1.setInt(1, t_amt);
			p_stmt1.setInt(2, acc_no);
			
			p_stmt1.executeUpdate();
			
			System.out.println("<----------Incoming Amount model ------------>");
			System.out.println(res.getString("names")+" Wants to send money:");
			System.out.println("Press Y to accept (OR) press N to reject ");
			String choice = s.next();
			
			
			if(choice.equalsIgnoreCase("y")) {
				String credited_Query = "update employee set balance = balance + ? where acc_num = ?";
				PreparedStatement p_stmt2 = Con.prepareStatement(credited_Query);
				p_stmt2.setInt(1, t_amt);
				p_stmt2.setInt(2, ben_acc);
				int i = p_stmt2.executeUpdate();
//				System.out.println(i);
				
		
				
				PreparedStatement p_stmt3 =Con.prepareStatement("Select * from employee where acc_num=?");
				p_stmt3.setInt(1, ben_acc);
				ResultSet r1 = p_stmt3.executeQuery();
				r1.next();
				System.out.println("Money is credited to your Account Number: "+r1.getInt("acc_num"));
				System.out.println("Updated Account Balance: "+r1.getInt("balance")
									+" User: "+r1.getString("names"));
			}
			else {
				PreparedStatement p_stmt3 =Con.prepareStatement("Select * from employee where acc_num=?");
				p_stmt3.setInt(1, ben_acc);
				ResultSet r1 = p_stmt3.executeQuery();
				r1.next();
			
				System.out.println("Existing Balance: "+r1.getInt("balance")
									+" User: "+r1.getString("names"));
				
				
				Con.rollback(save);
			}
			
			
			
			Con.commit();			
			
			
			

			
		
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

	}

}
