package cis550proj;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.*;

/**
 * Servlet implementation class Profile
 */
public class Update extends HttpServlet implements Servlet {
	private static final long serialVersionUID = 1L;
	private Query q;
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	
	public Update() {
		super();
		q = new Query();
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

		// there is one if condition for all the tables

		// since we are alread going to be in the user page
		// we have to pass userid to all the functions here
		Connection conn = null;
		try {
			conn = getConnected();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		if(queryType.equals("relChange")){
			int userID=Integer.parseInt(request.getParameter("userID").toString());
			int friendID = Integer.parseInt(request.getParameter("friendID").toString());
			String name = request.getParameter("name").toString();
			
			Search.printHeader(out, "Relationship status");
    		
    		out.println("<br/><h2>Change relationship status with " +name + " to:</h2>");
    		
    		out.println("	<form action=\"Update\" method=\"post\">");
    		out.println("<select name=\"privilege\" id=\"privilege\">");
    		out.println("              <option selected=\"selected\">Single</option>");
    		out.println("              <option>In a relationship</option>");
    		out.println("              <option>In an open relationship</option>");
    		out.println("              <option>Engaged</option>");
    		out.println("              <option>Married</option>");
    		out.println("              <option>Widowed</option>");
    		out.println("              <option>It's complicated</option>");
    		out.println("            </select>");
    		out.println("   <input type=\"hidden\" name=\"queryType\" value=\"submitRel\" />");
    		out.println("   <input type=\"hidden\" name=\"name\" value=\"" + name + "\" />");
    		out.println("<input type=\"hidden\" name=\"userID\" value=\""+String.valueOf(userID)+"\">");
    		out.println("<input type=\"hidden\" name=\"friendID\" value=\""+String.valueOf(friendID)+"\">");
    		out.println("   <br/><input type=\"submit\" value=\"Set\" /><br/>");
    		out.println("   </form>");
    		out.println("<br/>");
    		
    		Search.printFooter(out);
			
			
		}
		if(queryType.equals("privacyChange")){
			int userID=Integer.parseInt(request.getParameter("userID").toString());
			int friendID = Integer.parseInt(request.getParameter("friendID").toString());
			String name = request.getParameter("name").toString();
			int gID = 3;
			
			try {
				gID = q.getFriendGID(conn, friendID, userID);
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

    		Search.printHeader(out, "Change privacy settings");
    		
    		out.println("<br/><h2>Change privacy settings for " + name + "</h2>");
    		out.println("<h4>How much can " + name + " see? </h4>");
    		out.println("	<form action=\"Update\" method=\"post\">");
    		out.println("<select name=\"privacy\" id=\"privacy\">");
    		
    		if (gID == 3) out.println("              <option selected=\"selected\">Everything</option>");
    		else out.println("              <option>Everything</option>");
    		if (gID == 1) out.println("              <option selected=\"selected\">My profile only</option>");
    		else out.println("              <option>My profile only</option>");
    		if (gID == 2) out.println("              <option selected=\"selected\">My wall only</option>");
    		else out.println("              <option>My wall only</option>");
    		if (gID == 4) out.println("              <option selected=\"selected\">My wall and profile only</option>");
    		else out.println("              <option>My wall and profile only</option>");
    		out.println("            </select>");
    		out.println("   <input type=\"hidden\" name=\"queryType\" value=\"submitPrivacy\" />");
    		out.println("   <input type=\"hidden\" name=\"name\" value=\"" + name + "\" />");
    		out.println("<input type=\"hidden\" name=\"userID\" value=\""+String.valueOf(userID)+"\">");
    		out.println("<input type=\"hidden\" name=\"friendID\" value=\""+String.valueOf(friendID)+"\">");
    		out.println("   <br/><input type=\"submit\" value=\"Update\" /><br/>");
    		out.println("   </form>");
    		out.println("<br/>");
    		
    		Search.printFooter(out);
		}
		if(queryType.equals("submitRel")){
			int userID = Integer.parseInt(request.getParameter("userID").toString());
			int friendID = Integer.parseInt(request.getParameter("friendID").toString());
			String name = request.getParameter("name").toString();
			String privilege1 = request.getParameter("privilege").toString();
			String privilege = privilege1;
			
			if (privilege.equals("It's complicated")) privilege = "It''s complicated";
			
			try {
				String userName = q.queryUserAccounts(conn, "firstname", userID) + " " +
				q.queryUserAccounts(conn, "lastname", userID);				
				
				if (!privilege.equals("Single") && !privilege.equals("Widowed")){
					updatePartner(conn, userID, friendID, privilege);
//					updatePartner(friendID, userID, privilege);
					
					addUserUpdate(conn, userID, userName + " is now " + privilege.toLowerCase() + " with " + name );
//					addUserUpdate(friendID, name + " is now " + privilege.toLowerCase() + " with " + userName );
				}
				else{
					updatePartner(conn, userID, -1, privilege);
//					updatePartner(friendID, -1, privilege);
					
					addUserUpdate(conn, userID, userName + " is now " + privilege.toLowerCase());
//					addUserUpdate(friendID, name + " is now " + privilege.toLowerCase());
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			
			Search.printHeader(out, "Relationship status");

			if (!privilege.equals("Single") && !privilege.equals("Single")) out.println("<center><h2 style=\"margin-top:0px;\"><br><br>You are now " + privilege1.toLowerCase() +  " with " + name + ".</h2><br></center><br/>");
			else out.println("<center><h2 style=\"margin-top:0px;\"><br><br>You are now " + privilege1.toLowerCase() + ".</h2><br></center><br/>");
			
			out.println("<center><form action=\"Profile\" method=\"POST\">");
        	out.println("<input type=\"hidden\" name=\"userID\" value=\""+String.valueOf(userID)+"\">");
    		out.println("<input type=\"hidden\" name=\"queryType\" value=\"user\">");
            out.println("<input type=\"submit\" value=\"Return to your profile\"></form></center>");
            out.println("<br/>");
			
			Search.printFooter(out);

		}
		if(queryType.equals("submitPrivacy")){
			int userID = Integer.parseInt(request.getParameter("userID").toString());
			int friendID = Integer.parseInt(request.getParameter("friendID").toString());
			String name = request.getParameter("name").toString();
			String privacy = request.getParameter("privacy").toString();
			int privacyID = 3;
			
			if (privacy.equals("My profile only")) privacyID = 1;
			else if (privacy.equals("My wall only")) privacyID = 2;
			else if (privacy.equals("My wall and profile only")) privacyID = 4;
			
			Search.printHeader(out, "Change privacy settings");
			
			try {    			
				updateGID(conn, userID, friendID, privacyID);
				
				out.println("<center><h2 style=\"margin-top:0px;\"><br><br>Changed privacy status for " + name + ".</h2><br></center><br/>");
				out.println("<center><form action=\"Profile\" method=\"POST\">");
            	out.println("<input type=\"hidden\" name=\"userID\" value=\""+String.valueOf(userID)+"\">");
        		out.println("<input type=\"hidden\" name=\"queryType\" value=\"user\">");
                out.println("<input type=\"submit\" value=\"Return to your profile\"></form></center>");
                out.println("<br/>");
				
    		} catch (SQLException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			
			Search.printFooter(out);
			
		}
		if(queryType.equals("account")){

			try{	
				String id = request.getParameter("userID");
				String item = request.getParameter("item").toString();
				String input = request.getParameter("input").toString();
				int userID= Integer.parseInt(id);			
				
				if(item.equals("birthdate")){
					SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
					java.util.Date birthd = dateFormat.parse(input);
					java.sql.Date birthdate = new java.sql.Date(birthd.getTime());
					updateAccount(conn, userID, item, input, birthdate);
				}else{
					updateAccount(conn, userID, item, input, null);
				}
			}

			catch (SQLException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if(queryType.equals("education")){		
			try {
				// have 2 pass userid
				{  // i wrote the 1st two lines coz i was taking in the userid from a jsp page
					// the userid will be already there 
					String id = request.getParameter("userID");
					int userid= Integer.parseInt(id);
					String school = request.getParameter("school").toString();
					String degree = request.getParameter("degree").toString();
					String startY = request.getParameter("startYear");
					String finishY = request.getParameter("finishYear");
					SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
					java.util.Date startYr = dateFormat.parse(startY);
					java.util.Date finishYr = dateFormat.parse(finishY);
					java.sql.Date startYear = new java.sql.Date(startYr.getTime());
					java.sql.Date finishYear = new java.sql.Date(finishYr.getTime());

					// finishYear should be after startYear
					if(finishYear.after(startYear))
					{updateUserEducation(conn,userid,school,degree,startYear, finishYear);}
					else
					{
						System.out.println("please enter correct date");
					}
				}

			} catch (SQLException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (queryType.equals("employment"))
		{ try {
			// have 2 pass userid
			{   

				String id = request.getParameter("userID");
				int userid= Integer.parseInt(id);
				String employer = request.getParameter("employer").toString();
				String address = request.getParameter("address").toString();
				String jobtitle = request.getParameter("jobtitle").toString();
				String s = request.getParameter("salary");
				int salary= Integer.parseInt(s);

				String startY = request.getParameter("startYear");
				String endY = request.getParameter("endYear");
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
				java.util.Date startYr = dateFormat.parse(startY);
				java.util.Date endYr = dateFormat.parse(endY);
				java.sql.Date startYear = new java.sql.Date(startYr.getTime());
				java.sql.Date endYear = new java.sql.Date(endYr.getTime());

				// endYear should be after startYear
				if(endYear.after(startYear))
				{
					updateUserEmployment(conn,userid,employer,address,jobtitle,salary,startYear, endYear);
				}
				else
				{
					System.out.println("please enter correct date");}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		if (queryType.equals("status"))
		{ try {
			// have 2 pass userid
			// takes in the system date
			// while displaying will also have to display the time
			{   String id = request.getParameter("userID");

			int userid= Integer.parseInt(id);
			String statusupdate = request.getParameter("statusupdate").toString();
			DateFormat dateFormat = new SimpleDateFormat ("dd/MM/yyyy");
			java.util.Date date = new java.util.Date ();
			String dateStr = dateFormat.format (date);
			java.util.Date date2 = dateFormat.parse (dateStr); 
			java.sql.Date date3 = new java.sql.Date(date2.getTime());
			if(statusupdate==null)
			{updateUserStatus(conn,userid, null, date3);
			}
			else
			{updateUserStatus(conn,userid, statusupdate, date3);
			}
			} }catch (SQLException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (queryType.equals("interests"))
		{ try {
			// have 2 pass userid
			{   String id = request.getParameter("userID");
			int userid= Integer.parseInt(id);
			String interest = request.getParameter("interest").toString();
			updateUserInterest(conn,userid, interest);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		}
		if (queryType.equals("groups"))
		{ try {
			// have 2 pass userid

			{   String id = request.getParameter("gid");
			int gid= Integer.parseInt(id);
			String name = request.getParameter("name").toString();
			String id1 = request.getParameter("privilegelevel");
			int privilegelevel= Integer.parseInt(id1);
			updateUserGroup(conn,gid,name, privilegelevel);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		}
		if (queryType.equals("connectedto"))
		{ try {
			// have 2 pass userid,fid, by default the gid is equal to 3,please change wherever required

			{   
				String id = request.getParameter("userid");
				int userid= Integer.parseInt(id);
				String id1 = request.getParameter("frienduserid");
				int frienduserid= Integer.parseInt(id1);
				String id2 = request.getParameter("gid");
				int gid= Integer.parseInt(id2);
				updateGID(conn,userid,frienduserid, gid);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		}  
		try {
			conn.commit();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	//item should be 'firstname', 'lastname', 'email', 'birthdate', 'gender', 'address', or 'bio'
	//if item is birthdate than birthdate should be set as a java.sql.Date, otherwise it is null
	protected void updateAccount(Connection conn, int userID, String item, String input, java.sql.Date birthdate) throws SQLException, IOException, ClassNotFoundException{
		try{	

		String query = "UPDATE useraccounts SET "+item+"=? where userID=?";
		PreparedStatement pStmt = null;
		pStmt = conn.prepareStatement(query);
		if(item.equals("birthdate")){
			pStmt.setDate(1, (java.sql.Date) birthdate);
		}else{
			pStmt.setString(1, input);
		}
		pStmt.setInt(2,userID);
		pStmt.executeUpdate();
		pStmt.close();

		} catch (Exception e){
			e.printStackTrace();
		}
		conn.commit();

	}

	//unnecessary method that shouldn't be used (education should be added or deleted)
	protected void updateUserEducation(Connection conn,int userid, String school,
			String degree,java.sql.Date startYear, java.sql.Date finishYear) throws SQLException, IOException, ClassNotFoundException{
		try{

		String query = "UPDATE usereducation SET school=?,degree=?,startYear=?,finishYear=? where userid=?";
		PreparedStatement pStmt = null;
		pStmt = conn.prepareStatement(query);

		pStmt.setString(1,school);
		pStmt.setString(2,degree);
		pStmt.setDate(3, (java.sql.Date) startYear);
		pStmt.setDate(4,(java.sql.Date) finishYear);
		pStmt.setInt(5,userid);
		//ResultSet rs = stmt.executeUpdate();
		pStmt.executeUpdate();
		pStmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		conn.commit();
	}
	
	//unnecessary method that shouldn't be used (employment should be added or deleted)
	protected void updateUserEmployment(Connection conn,int userid, String employer,String address, String jobtitle,
			int salary,java.sql.Date startYear, java.sql.Date endYear) throws SQLException, IOException, ClassNotFoundException{
		try{

		String query = "UPDATE useremployment SET employer=?,address=?,jobtitle=?,salary=?,startyear=?,endyear=? where userid=?";
		PreparedStatement pStmt = null;
		pStmt = conn.prepareStatement(query);

		pStmt.setString(1,employer);
		pStmt.setString(2,address);
		pStmt.setString(3,jobtitle);
		pStmt.setInt(4,salary);
		pStmt.setDate(5, (java.sql.Date) startYear);
		pStmt.setDate(6,(java.sql.Date) endYear);
		pStmt.setInt(7,userid);
		//ResultSet rs = stmt.executeUpdate();
		pStmt.executeUpdate();
		pStmt.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		conn.commit();
	}
	
	//unnecessary method that shouldn't be used (status should be added or deleted)
	protected void updateUserStatus(Connection conn,int userid,String statusupdate, java.sql.Date date3) throws SQLException, IOException, ClassNotFoundException{
		try{	
		//isstatus=1 from userstatus
		String query = "UPDATE userstatus SET statusupdate=?,statusdate=?,isstatus=1 where userid=?";
		PreparedStatement pStmt = null;
		pStmt = conn.prepareStatement(query);

		pStmt.setString(1,statusupdate);
		pStmt.setDate(2, (java.sql.Date) date3);
		pStmt.setInt(3,userid);
		pStmt.executeUpdate();
		pStmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		//return userid; 
	}
	
	//unnecessary method that shouldn't be used (interests should be added or deleted)
	protected void updateUserInterest(Connection conn,int userid, String interest) throws SQLException, IOException, ClassNotFoundException{
		try{
		String query = "UPDATE userinterests SET interest=? where userid=?";		
		//Statement stmt = conn.createStatement();
		//String query = "INSERT INTO USERACCOUNTS (USERID,FIRSTNAME,LASTNAME,EMAIL,BIRTHDATE,GENDER,ADDRESS,BIO) VALUES"+"("+19+","+String.valueOf(firstname)+","+String.valueOf(lastname)+","+String.valueOf(email)+","+String.valueOf(birthdate)+","+String.valueOf(gender)+","+String.valueOf(address)+","+String.valueOf(bio)+")";

		PreparedStatement pStmt = null;
		pStmt = conn.prepareStatement(query);
		pStmt.setString(1,interest);
		pStmt.setInt(2,userid);
		//ResultSet rs = stmt.executeUpdate();
		pStmt.executeUpdate();
		pStmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		//return userid; 
	}
	protected void updateUserGroup(Connection conn,int gid,String name, int privilegelevel) throws SQLException, IOException, ClassNotFoundException{
		try{
		String query = "UPDATE usergroups SET name=?,privilegelevel=? where gid=?";
		//Statement stmt = conn.createStatement();
		//String query = "INSERT INTO USERACCOUNTS (USERID,FIRSTNAME,LASTNAME,EMAIL,BIRTHDATE,GENDER,ADDRESS,BIO) VALUES"+"("+19+","+String.valueOf(firstname)+","+String.valueOf(lastname)+","+String.valueOf(email)+","+String.valueOf(birthdate)+","+String.valueOf(gender)+","+String.valueOf(address)+","+String.valueOf(bio)+")";

		PreparedStatement pStmt = null;
		pStmt = conn.prepareStatement(query);
		pStmt.setString(1,name);
		pStmt.setInt(2,privilegelevel);
		pStmt.setInt(3,gid);
		pStmt.executeUpdate();
		pStmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		conn.commit();
	}
	protected void updateGID(Connection conn,int userid,int frienduserid, int gid) throws SQLException, IOException, ClassNotFoundException{
		try{	
		String query = "UPDATE connectedto SET gid=? where userid=? AND frienduserid=?";	
		String query1 = "UPDATE connectedto SET gid=? where userid=? AND frienduserid=?";	
		//Statement stmt = conn.createStatement();
		//String query = "INSERT INTO USERACCOUNTS (USERID,FIRSTNAME,LASTNAME,EMAIL,BIRTHDATE,GENDER,ADDRESS,BIO) VALUES"+"("+19+","+String.valueOf(firstname)+","+String.valueOf(lastname)+","+String.valueOf(email)+","+String.valueOf(birthdate)+","+String.valueOf(gender)+","+String.valueOf(address)+","+String.valueOf(bio)+")";

		PreparedStatement pStmt = null;
		pStmt = conn.prepareStatement(query);
		pStmt.setInt(1,gid);
		pStmt.setInt(2,userid);
		pStmt.setInt(3,frienduserid);

		pStmt.executeUpdate();
		pStmt.close();
		PreparedStatement pStmt1 = null;
		pStmt1 = conn.prepareStatement(query1);
		pStmt1.setInt(1,gid);
		pStmt1.setInt(2,frienduserid);
		pStmt1.setInt(3,userid);

		pStmt1.executeUpdate();
		pStmt1.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		conn.commit();
	}
	
	//boolean that returns true if sets userID to inputed password
	protected boolean updatePassword(Connection conn, int userID, String password) throws SQLException, IOException, ClassNotFoundException{
		Statement stmt = null;
		ResultSet rs = null;
		String query = null;
		try{
			stmt=conn.createStatement();
			query="SELECT * FROM UserPasswords WHERE userID="+String.valueOf(userID);
			rs = stmt.executeQuery(query);
			rs.next();
			rs.getString("userID");
		}catch(Exception e){
			getDisconnected(conn,stmt,rs);
			return false;
		}
		stmt.close();
		rs.close();
		try{
			stmt = conn.createStatement();
			query="UPDATE UserPasswords SET password='"+password+"' WHERE userID="+String.valueOf(userID);
			stmt.executeUpdate(query);
		}catch(Exception e){
			getDisconnected(null,stmt,rs);
			return false;
		}
		getDisconnected(null,stmt,rs);
		conn.commit();
		return true;
	}
	
	protected void updatePartner(Connection conn, int userID, int partnerUserID, String relationshipLevel) throws SQLException, IOException, ClassNotFoundException{
        Statement stmt = null;
        ResultSet rs = null;
        String query = null;
        String temp=String.valueOf(partnerUserID);
        if(partnerUserID<0){
            temp="null";
        }
        try{
            stmt = conn.createStatement();
            query="UPDATE UserPartner SET partnerUserID="+temp+", relationshipLevel='"+relationshipLevel+"' WHERE userID="+String.valueOf(userID);
            stmt.executeUpdate(query);
        }catch(Exception e){
            getDisconnected(null,stmt,rs);
            return;
        }
        getDisconnected(null,stmt,rs);
        conn.commit();
    }
	
	protected void addUserUpdate(Connection conn, int userID, String userUpdate) throws SQLException, IOException, ClassNotFoundException{
	    Statement stmt = conn.createStatement();
	    String query = "INSERT INTO UserUpdates(userID, updateDate, userUpdate) VALUES ("+userID+", (SELECT sysdate FROM dual), '"+userUpdate+"')";
	    stmt.executeUpdate(query);
	    getDisconnected(null,stmt, null);
	}
}
