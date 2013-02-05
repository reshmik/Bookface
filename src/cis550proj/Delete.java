package cis550proj;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.LinkedList;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Delete
 */
public class Delete extends HttpServlet implements Servlet {
	private static final long serialVersionUID = 1L;
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Delete(){
		super();
	}

	public Connection getConnected() throws SQLException, IOException, ClassNotFoundException{
		Class.forName("oracle.jdbc.driver.OracleDriver");
		Connection conn = DriverManager.getConnection
		("jdbc:oracle:thin:@fling.seas.upenn.edu:1521/cisora", "jasonns", "suapengco");
		return conn;
	}

	//disconnects from oracle server
	public void getDisconnected(Connection conn, Statement stmt, ResultSet rs) throws SQLException{
		if(rs!=null){
			rs.close();
		}
		if(stmt!=null){
			stmt.close();
		}
		if(conn!=null){
			conn.commit();
			conn.close();
		}
	}


	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		String queryType = request.getParameter("queryType").toString();
		PrintWriter out = response.getWriter();
		// for deleting the entire profile of a user with user education, employment and so on.
		Connection conn = null;
		try {
			conn = getConnected();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		if(queryType.equals("deleteInterest")){
			String interest = request.getParameter("interest");
			String id = request.getParameter("userID");
			int userID= Integer.parseInt(id);
			
			try {
				deleteUserInterest(conn, userID, interest);
				
				Search.printHeader(out, "Deleted Interest");
				
				out.println("<br/><br/><center><h2 style=\"margin-top:0px;\">Deleted \"" + interest + "\" from your interests!</h2></center>");
				out.println("<center><form action=\"Create\" method=\"POST\">");
            	out.println("<input type=\"hidden\" name=\"userID\" value=\""+String.valueOf(userID)+"\">");
        		out.println("<input type=\"hidden\" name=\"queryType\" value=\"editInterests\">");
                out.println("<input type=\"submit\" value=\"Return to interests\"></form></center>");
                out.println("<br/>");
				out.println("<center><form action=\"Profile\" method=\"POST\">");
            	out.println("<input type=\"hidden\" name=\"userID\" value=\""+String.valueOf(userID)+"\">");
        		out.println("<input type=\"hidden\" name=\"queryType\" value=\"user\">");
                out.println("<input type=\"submit\" value=\"Return to your profile\"></form></center>");
                out.println("<br/>");
                
                Search.printFooter(out);
				
				
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			
			
		}
		if(queryType.equals("deleteEmployment")){
			String employer = request.getParameter("employer");
			String id = request.getParameter("userID");
			int userID= Integer.parseInt(id);
			
			try {
				deleteUserEmployment(conn, userID, employer);
				
				Search.printHeader(out, "Deleted Employer");
				
				out.println("<br/><br/><center><h2 style=\"margin-top:0px;\">Deleted \"" + employer + "\" from your employment list!</h2></center>");
				out.println("<center><form action=\"Create\" method=\"POST\">");
            	out.println("<input type=\"hidden\" name=\"userID\" value=\""+String.valueOf(userID)+"\">");
        		out.println("<input type=\"hidden\" name=\"queryType\" value=\"editEmployment\">");
                out.println("<input type=\"submit\" value=\"Return to employment\"></form></center>");
                out.println("<br/>");
				out.println("<center><form action=\"Profile\" method=\"POST\">");
            	out.println("<input type=\"hidden\" name=\"userID\" value=\""+String.valueOf(userID)+"\">");
        		out.println("<input type=\"hidden\" name=\"queryType\" value=\"user\">");
                out.println("<input type=\"submit\" value=\"Return to your profile\"></form></center>");
                out.println("<br/>");
                
                
                Search.printFooter(out);
				
				
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			
			
		}
		if(queryType.equals("deleteEducation")){
			String school = request.getParameter("school");
			String id = request.getParameter("userID");
			int userID= Integer.parseInt(id);
			
			try {
				deleteUserEducation(conn, userID, school);
				
				Search.printHeader(out, "Deleted Education");
				
				out.println("<br/><br/><center><h2 style=\"margin-top:0px;\">Deleted \"" + school + "\" from your education list!</h2></center>");
				out.println("<center><form action=\"Create\" method=\"POST\">");
            	out.println("<input type=\"hidden\" name=\"userID\" value=\""+String.valueOf(userID)+"\">");
        		out.println("<input type=\"hidden\" name=\"queryType\" value=\"editEducation\">");
                out.println("<input type=\"submit\" value=\"Return to aducation\"></form></center>");
                out.println("<br/>");
				out.println("<center><form action=\"Profile\" method=\"POST\">");
            	out.println("<input type=\"hidden\" name=\"userID\" value=\""+String.valueOf(userID)+"\">");
        		out.println("<input type=\"hidden\" name=\"queryType\" value=\"user\">");
                out.println("<input type=\"submit\" value=\"Return to your profile\"></form></center>");
                out.println("<br/>");
                
                
                Search.printFooter(out);
				
				
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			
			
		}
		if(queryType.equals("profile"))
		{		
			// deleting a particular profile
			// have 2 pass userid
			// i wrote the 1st  line coz i was taking in the userid from a jsp page
			// the userid will be already there 
			String id = request.getParameter("userID");
			int userid= Integer.parseInt(id);

			try {
				deleteUserProfile(conn, userid);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// for deleting user education
		if(queryType.equals("education"))
		{		
			// deleting for a particular school and userid
			// have 2 pass userid
			// i wrote the 1st  line coz i was taking in the userid from a jsp page
			// the userid will be already there 
			String id = request.getParameter("userID");
			int userid= Integer.parseInt(id);
			String school = request.getParameter("school");
			try {
				deleteUserEducation(conn,userid, school);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (queryType.equals("employment"))
		{ 
			// have 2 pass userid


			String id = request.getParameter("userID");
			int userid= Integer.parseInt(id);
			String employer = request.getParameter("employer");
			try {
				deleteUserEmployment(conn,userid, employer);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		if (queryType.equals("status"))
		{ 
			// have 2 pass userid

			String id = request.getParameter("userID");
			int userid= Integer.parseInt(id);
			String statusupdate = request.getParameter("statusupdate");
			try {
				deleteUserStatus(conn,userid, statusupdate);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		if (queryType.equals("interests"))
		{ 
			// have 2 pass userid
			{   String id = request.getParameter("userID");
			int userid= Integer.parseInt(id);
			String interest = request.getParameter("interest");
			try {
				deleteUserInterest(conn,userid, interest);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}

		}
		if (queryType.equals("groups"))
		{ 
			String id = request.getParameter("gid");
			int gid= Integer.parseInt(id);
			try {
				deleteUserGroup(conn, gid);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// for deleting friends
		if (queryType.equals("connectedto"))
		{    
			String id = request.getParameter("userID");
			int userid= Integer.parseInt(id);
			String id1 = request.getParameter("frienduserid");
			int frienduserid= Integer.parseInt(id1);

			try {
				deleteConnectedTo(conn,userid, frienduserid);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}     
		try {
			getDisconnected(conn,null,null);	
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	} //end of do POST


	//depending on the above querytypes the methods described below are called.

	/// CAUTION CAUTION !!! PLEASE INCLUDE ALL THE DELETE COMMAND IN THIS FUNCTION
	/// BEFORE CALLING DELETE USERACCOUNTS BECAUSE IT CONTAINS THE PRIMARY KEY
	/// SO THIS IS GOING TO THROW INTEGRITY CONSTRAINTS ERROR HERE IF WE
	// DONT INCLUDE ALL THE FOREIGN KEYS
	protected void deleteUserProfile(Connection conn, int userID) throws SQLException, IOException, ClassNotFoundException{
		try{	
			
			//deletes connected to for user
			String query5 = "DELETE FROM CONNECTEDTO WHERE USERID=?";	
			String query6 = "DELETE FROM CONNECTEDTO WHERE FRIENDUSERID=?";	

			PreparedStatement pStmt5 = null;
			pStmt5 = conn.prepareStatement(query5);
			pStmt5.setInt(1,userID);
			pStmt5.executeUpdate();
			pStmt5.close();
			PreparedStatement pStmt6 = null;
			pStmt6 = conn.prepareStatement(query6);
			pStmt6.setInt(1,userID);
			pStmt6.executeUpdate();
			pStmt6.close();
			
			String query7 = "DELETE FROM UserPartner WHERE userID=?";
			String query8 = "DELETE FROM UserPartner WHERE partnerUserID=?";
			PreparedStatement pStmt7 = null;
			pStmt7 = conn.prepareStatement(query7);
			pStmt7.setInt(1,userID);
			pStmt7.executeUpdate();
			pStmt7.close();
			PreparedStatement pStmt8 = null;
			pStmt8 = conn.prepareStatement(query8);
			pStmt8.setInt(1,userID);
			pStmt8.executeUpdate();
			pStmt8.close();
			
			String query4 = "DELETE FROM USERINTERESTS WHERE USERID=?";		
			//Statement stmt = conn.createStatement();
			PreparedStatement pStmt4 = null;
			pStmt4 = conn.prepareStatement(query4);
			pStmt4.setInt(1,userID);
			pStmt4.executeUpdate();
			pStmt4.close();
			
			String query1 = "DELETE FROM USEREDUCATION WHERE USERID=?";
			PreparedStatement pStmt1 = null;
			pStmt1 = conn.prepareStatement(query1);
			pStmt1.setInt(1,userID);
			//ResultSet rs = stmt.executeUpdate();
			pStmt1.executeUpdate();
			pStmt1.close();
			
			String query3 = "DELETE FROM USERSTATUS WHERE USERID=?";
			PreparedStatement pStmt3 = null;
			pStmt3 = conn.prepareStatement(query3);
			pStmt3.setInt(1,userID);
			pStmt3.executeUpdate();
			pStmt3.close();
			
			String query2 = "DELETE FROM USEREMPLOYMENT WHERE USERID=?";
			PreparedStatement pStmt2 = null;
			pStmt2 = conn.prepareStatement(query2);
			pStmt2.setInt(1,userID);
			pStmt2.executeUpdate();
			pStmt2.close();
			
			String query9 = "DELETE FROM UserPasswords WHERE USERID=?";
			PreparedStatement pStmt9 = null;
			pStmt9 = conn.prepareStatement(query9);
			pStmt9.setInt(1,userID);
			pStmt9.executeUpdate();
			pStmt9.close();
			
			String query10 = "DELETE FROM FriendRequest WHERE requestedID=?";
			String query11 = "DELETE FROM FriendRequest WHERE placedID=?";
			PreparedStatement pStmt10 = null;
			pStmt10 = conn.prepareStatement(query10);
			pStmt10.setInt(1,userID);
			pStmt10.executeUpdate();
			pStmt10.close();
			PreparedStatement pStmt11 = null;
			pStmt11 = conn.prepareStatement(query11);
			pStmt11.setInt(1,userID);
			pStmt11.executeUpdate();
			pStmt11.close();
			
			 Statement stmt = conn.createStatement();
			 String query12 = "SELECT mID FROM UserMessages WHERE userID="+String.valueOf(userID);
			 ResultSet rs = stmt.executeQuery(query12);
			 
			 LinkedList<Integer> messageIDs = new LinkedList<Integer>();
			 while (rs.next()) {
			   messageIDs.add(rs.getInt("mID"));
			 }
			 rs.close();
			 stmt.close();
			for(int i: messageIDs){
				deleteMessage(conn, i);
			}
			 
			String query = "DELETE FROM USERACCOUNTS WHERE USERID=?";
			PreparedStatement pStmt = null;
			pStmt = conn.prepareStatement(query);
			pStmt.setInt(1,userID);
			pStmt.executeUpdate();
			pStmt.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		conn.commit();
	}


	protected void deleteUserEducation(Connection conn,int userid, String school) throws SQLException, IOException, ClassNotFoundException{
		try{	
			String query = "DELETE FROM USEREDUCATION WHERE USERID=? AND SCHOOL=?";
			PreparedStatement pStmt = null;
			pStmt = conn.prepareStatement(query);
			pStmt.setInt(1,userid);
			pStmt.setString(2, school);
			//ResultSet rs = stmt.executeUpdate();
			pStmt.executeUpdate();
			pStmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		conn.commit();
	}


	protected void deleteUserEmployment(Connection conn,int userid, String employer) throws SQLException, IOException, ClassNotFoundException{
		try{
		String query = "DELETE FROM USEREMPLOYMENT WHERE USERID=? AND EMPLOYER=?";
		PreparedStatement pStmt = null;
		pStmt = conn.prepareStatement(query);
		pStmt.setInt(1,userid);
		pStmt.setString(2,employer);

		//ResultSet rs = stmt.executeUpdate();
		pStmt.executeUpdate();
		pStmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		conn.commit();
	}


	protected void deleteUserStatus(Connection conn,int userid, String statusupdate) throws SQLException, IOException, ClassNotFoundException{
		try{
		//isstatus=1 from userstatus
		String query = "DELETE FROM USERSTATUS WHERE USERID=? AND STATUSUPDATE=?";
		PreparedStatement pStmt = null;
		pStmt = conn.prepareStatement(query);
		pStmt.setInt(1,userid);
		pStmt.setString(2,statusupdate);

		pStmt.executeUpdate();
		pStmt.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		conn.commit();
		//return userid; 
	}


	protected void deleteUserInterest(Connection conn,int userid, String interest) throws SQLException, IOException, ClassNotFoundException{
		try{
		String query = "DELETE FROM USERINTERESTS WHERE USERID=? AND INTEREST=?";		
		//Statement stmt = conn.createStatement();
		//String query = "INSERT INTO USERACCOUNTS (USERID,FIRSTNAME,LASTNAME,EMAIL,BIRTHDATE,GENDER,ADDRESS,BIO) VALUES"+"("+19+","+String.valueOf(firstname)+","+String.valueOf(lastname)+","+String.valueOf(email)+","+String.valueOf(birthdate)+","+String.valueOf(gender)+","+String.valueOf(address)+","+String.valueOf(bio)+")";

		PreparedStatement pStmt = null;
		pStmt = conn.prepareStatement(query);
		pStmt.setInt(1,userid);
		pStmt.setString(2,interest);
		//ResultSet rs = stmt.executeUpdate();
		pStmt.executeUpdate();
		pStmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		conn.commit();
		//return userid; 
	}


	protected void deleteUserGroup(Connection conn, int gid) throws SQLException, IOException, ClassNotFoundException{
		try{
		String query = "DELETE FROM USERGROUPS WHERE GID=?";
		//String query = "INSERT INTO USERACCOUNTS (USERID,FIRSTNAME,LASTNAME,EMAIL,BIRTHDATE,GENDER,ADDRESS,BIO) VALUES"+"("+19+","+String.valueOf(firstname)+","+String.valueOf(lastname)+","+String.valueOf(email)+","+String.valueOf(birthdate)+","+String.valueOf(gender)+","+String.valueOf(address)+","+String.valueOf(bio)+")";

		PreparedStatement pStmt = null;
		pStmt = conn.prepareStatement(query);
		pStmt.setInt(1,gid);

		pStmt.executeUpdate();
		pStmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		conn.commit();
	}


	protected void deleteConnectedTo(Connection conn,int userid, int frienduserid) throws SQLException, IOException, ClassNotFoundException{
		try{
		String query = "DELETE FROM CONNECTEDTO WHERE USERID=? AND FRIENDUSERID=?";	
		String query1 = "DELETE FROM CONNECTEDTO WHERE USERID=? AND FRIENDUSERID=?";	
		//Statement stmt = conn.createStatement();
		//String query = "INSERT INTO USERACCOUNTS (USERID,FIRSTNAME,LASTNAME,EMAIL,BIRTHDATE,GENDER,ADDRESS,BIO) VALUES"+"("+19+","+String.valueOf(firstname)+","+String.valueOf(lastname)+","+String.valueOf(email)+","+String.valueOf(birthdate)+","+String.valueOf(gender)+","+String.valueOf(address)+","+String.valueOf(bio)+")";

		PreparedStatement pStmt = null;
		pStmt = conn.prepareStatement(query);
		pStmt.setInt(1,userid);
		pStmt.setInt(2,frienduserid);
		pStmt.executeUpdate();
		pStmt.close();
		PreparedStatement pStmt1 = null;
		pStmt1 = conn.prepareStatement(query1);
		pStmt1.setInt(1,frienduserid);
		pStmt1.setInt(2,userid);
		pStmt1.executeUpdate();
		pStmt1.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		conn.commit();
	}
	protected void deleteMessage(Connection conn, int mID) throws SQLException, IOException, ClassNotFoundException{
		Statement stmt = null;
		ResultSet rs = null;
		stmt = conn.createStatement();
		rs = stmt.executeQuery("SELECT subject, content FROM UserMessages WHERE mID="+mID);
		rs.next();
		String subject = rs.getString("subject");
		String content = rs.getString("content");
		String[] contentWords = content.split("\\W");
		String[] subjectWords = subject.split("\\W");
		rs.close();
		stmt.close();
		stmt = conn.createStatement();
		stmt.executeUpdate("DELETE FROM Occurs WHERE mID="+mID);
		rs.close();
		stmt.close();
		for(int i=0; i<contentWords.length;i++){
			if(!contentWords[i].equals("")){
				removeWord(conn, contentWords[i].toLowerCase(), mID);
			}
		}
		for(int i=0; i<subjectWords.length;i++){
			if(!subjectWords[i].equals("")){
				removeWord(conn, subjectWords[i].toLowerCase(), mID);
			}
		}
		stmt = conn.createStatement();
		stmt.executeUpdate("DELETE FROM UserMessages WHERE mID="+mID);
		getDisconnected(null, stmt, null);
		conn.commit();
	}

	//helper method for deleteMessage
	private void removeWord(Connection conn, String word, int mID) throws SQLException, IOException, ClassNotFoundException{
		int wordID;
		Statement stmt = conn.createStatement();
		String query = "SELECT wordID FROM Lexicon WHERE wordname='"+word.toLowerCase()+"'";
		ResultSet rs = stmt.executeQuery(query);
		rs.next();
		try{wordID =rs.getInt("wordID");}
		catch(Exception e){
			getDisconnected(conn, stmt, rs);
			return;
		}
		rs.close();
		stmt.close();
		stmt = conn.createStatement();
		rs = stmt.executeQuery("SELECT COUNT(*) FROM Occurs WHERE mID!="+mID+" AND wordID="+wordID);
		rs.next();
		int count =rs.getInt("COUNT(*)");
		rs.close();
		stmt.close();
		if(count==0){
			stmt=conn.createStatement();
			stmt.executeUpdate("DELETE FROM Lexicon WHERE wordID="+wordID);
			stmt.close();
		}
		conn.commit();
	}
	
	protected boolean deletePassword(Connection conn, int userID, String password) throws SQLException, IOException, ClassNotFoundException{
		Statement stmt = null;
		try{
			stmt = conn.createStatement();
			String query = "DELETE FROM UserPasswords WHERE userID="+String.valueOf(userID);
			stmt.executeUpdate(query);
		}catch(Exception e){
			getDisconnected(null,stmt,null);
			conn.commit();
			return false;
		}
		getDisconnected(null,stmt,null);
		conn.commit();
		return true;
	}
	
	protected boolean deleteFriendRequest(Connection conn, int placedID, int requestedID) throws SQLException, IOException, ClassNotFoundException{
		Statement stmt = null;
		try{
			stmt = conn.createStatement();
			String query = "DELETE FROM FriendRequest WHERE requestedID="+requestedID+" AND placedID="+placedID;
			stmt.executeUpdate(query);
		}catch(Exception e){
			getDisconnected(null,stmt,null);
			conn.commit();
			return false;
		}
		getDisconnected(null,stmt,null);
		conn.commit();
		return true;
	}
	
	//to be done at each login
	protected void deleteAllUserUpdates(Connection conn, int userID) throws SQLException, IOException, ClassNotFoundException{
	    String query = "DELETE FROM UserUpdates WHERE userID="+userID;
	    Statement stmt= conn.createStatement();
	    stmt.executeUpdate(query);
	    getDisconnected(null,stmt,null);
	    conn.commit();
	}
}
