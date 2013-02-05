package cis550proj;

import java.io.IOException;
import java.io.PrintWriter;
//import java.io.PrintWriter;
import java.sql.*;
//import java.text.ParseException;
import java.text.SimpleDateFormat;


import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Create
 */
public class Create extends HttpServlet implements Servlet {
	private static final long serialVersionUID = 1L;
	Query q;
	Delete d;
	Update u;
	
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Create() {
		super();
		q = new Query();
		d = new Delete();
		u = new Update();
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
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		String queryType = request.getParameter("queryType").toString();
		PrintWriter out = response.getWriter();
		
		Search.printHeader(out, "Update");
		Connection conn = null;
		try {
			conn = getConnected();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		if (queryType.equals("editEducation") || queryType.equals("editInterests") ||
				queryType.equals("editEmployment") || queryType.equals("editPassword")){
			int userID=Integer.parseInt(request.getParameter("userID").toString());
			
			out.println("<br/>");
			
			if (queryType.equals("editEducation")){
				out.println("<h2>Change your education</h2>");
				try {
					q.printUserEducation(conn, out, userID, true);
	    		} catch (SQLException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}

			}
			else if (queryType.equals("editInterests")){
				out.println("<h2>Change your interests</h2>");
				
				try {
					q.printInterests(conn, out, userID, true);
	    		} catch (SQLException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}				
				
			}
			else if (queryType.equals("editEmployment")){
				out.println("<h2>Change your employment</h2>");
				
				try {
					q.printUserEmployment(conn, out, userID, true);
	    		} catch (SQLException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
			
			out.println("<br/>");
			out.println("<center><form action=\"Profile\" method=\"POST\">");
        	out.println("<input type=\"hidden\" name=\"userID\" value=\""+String.valueOf(userID)+"\">");
    		out.println("<input type=\"hidden\" name=\"queryType\" value=\"user\">");
            out.println("<input type=\"submit\" value=\"Return to your profile\"></form></center>");
			out.println("<br/>");
		}
		else if(queryType.equals("addSchool") || queryType.equals("addInterest")
				|| queryType.equals("changePassword") || queryType.equals("addEmployment")
				|| queryType.equals("editBio")){
			
			int userID=Integer.parseInt(request.getParameter("userID").toString());
			
			if(queryType.equals("addSchool")){
				printSchoolForm(out, userID);
				
			}
			else if(queryType.equals("addInterest")){
				printInterestForm(out, userID);
			}
			else if(queryType.equals("addEmployment")){
				printEmploymentForm(out, userID);
			}
			else if(queryType.equals("changePassword")){
				printPasswordForm(out, userID);
			}
			else if (queryType.equals("editBio")){
				printBioForm(conn, out, userID);
			}
			
			out.println("<center><form action=\"Profile\" method=\"POST\">");
        	out.println("<input type=\"hidden\" name=\"userID\" value=\""+String.valueOf(userID)+"\">");
    		out.println("<input type=\"hidden\" name=\"queryType\" value=\"user\">");
            out.println("<input type=\"submit\" value=\"Return to your profile\"></form></center>");
			
		}
		else if (queryType.equals("submitSchool") || queryType.equals("submitInterest")
				|| queryType.equals("submitEmployment") || queryType.equals("submitPassword")
				|| queryType.equals("submitBio")){
			
			int userID=Integer.parseInt(request.getParameter("userID").toString());
			
			if (queryType.equals("submitSchool")){
				String school = request.getParameter("school").toString();
				String degree = request.getParameter("degree").toString();
				String start = request.getParameter("startDate").toString();
				String end = request.getParameter("endDate").toString();
				
				try {
					SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");			
					java.util.Date startDate = dateFormat.parse(start);
					java.sql.Date startDate2 = new java.sql.Date(startDate.getTime());
					
					java.util.Date endDate = dateFormat.parse(end);
					java.sql.Date endDate2 = new java.sql.Date(endDate.getTime());
					
					createUserEducation(conn, userID, school, degree, startDate2, endDate2);
				} catch (java.text.ParseException e) {
					e.printStackTrace();
				} catch (SQLException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				
				out.println("<br/>");
				out.println("<h2>Added your school</h2>");
				out.println("<br/>");

			}
			else if (queryType.equals("submitInterest")){
				String interest = request.getParameter("interest").toString();
				
				try {
					createUserInterest(conn, userID, interest);
				} catch (SQLException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				

				out.println("<br/>");
				out.println("<h2>Added \"" + interest + "\" to your interests!</h2>");
				out.println("<br/>");

			}
			else if (queryType.equals("submitEmployment")){
				String employer = request.getParameter("employer").toString();
				String jobtitle = request.getParameter("jobTitle").toString();
				String address = request.getParameter("employAdd").toString();
				int salary = Integer.parseInt(request.getParameter("salary").toString());
				String start = request.getParameter("startDate").toString();
				String end = request.getParameter("endDate").toString();
				
				try {
					SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");			
					java.util.Date startDate = dateFormat.parse(start);
					java.sql.Date startDate2 = new java.sql.Date(startDate.getTime());
					
					java.util.Date endDate = dateFormat.parse(end);
					java.sql.Date endDate2 = new java.sql.Date(endDate.getTime());
					
					createUserEmployment(conn, userID, employer, jobtitle, address, salary, startDate2, endDate2);
				} catch (java.text.ParseException e) {
					e.printStackTrace();
				} catch (SQLException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				
				out.println("<br/>");
				out.println("<h2>Added your employer</h2>");
				out.println("<br/>");

			}
			else if (queryType.equals("submitPassword")){
				String password = request.getParameter("password").toString();
				String confirm = request.getParameter("confirm").toString();

				if (!password.equals(confirm)){
					out.println("<br/>");
					out.println("<h2>Passwords don't match!</h2>");
					out.println("<br/>");
					out.println("<center><FORM><INPUT TYPE=\"button\" VALUE=\"Try again.\" onClick=\"history.go(-1);return true;\"> </FORM></center> ");
					out.println("<br/>");
				}
				else{	
					try {
						u.updatePassword(conn, userID, password);
						
						out.println("<br/>");
						out.println("<h2>Successfully changed your password!</h2>");
						out.println("<br/>");
					} catch (SQLException e) {
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}

			}
			else if (queryType.equals("submitBio")){
				String bio = request.getParameter("bio").toString();
				
				try {
					u.updateAccount(conn, userID, "bio", bio, null);
				} catch (SQLException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				

				out.println("<br/>");
				out.println("<h2>Updated your bio!</h2>");
				out.println("<br/>");

			}
			
			
			
			out.println("<center><form action=\"Profile\" method=\"POST\">");
        	out.println("<input type=\"hidden\" name=\"userID\" value=\""+String.valueOf(userID)+"\">");
    		out.println("<input type=\"hidden\" name=\"queryType\" value=\"user\">");
            out.println("<input type=\"submit\" value=\"Return to your profile\"></form></center>");
            out.println("<br/>");
            
		}
		else if (queryType.equals("acceptRequest") || queryType.equals("ignoreRequest")){
    		
        	int userID=Integer.parseInt(request.getParameter("userID").toString());
        	int friendID=Integer.parseInt(request.getParameter("friendUserID").toString());
            
    		try {
    			if (queryType.equals("acceptRequest")) {
    				createConnectedTo(conn, userID, friendID, 3);
    			}
    			d.deleteFriendRequest(conn, friendID, userID);
    			
    			String firstname = q.queryUserAccounts(conn, "firstname", friendID);
    			String lastname = q.queryUserAccounts(conn, "lastname", friendID);
    			
    			String first = q.queryUserAccounts(conn, "firstname", userID);
    			String last = q.queryUserAccounts(conn, "lastname", userID);
    			
    			if (queryType.equals("acceptRequest")) {
    				try{
    					addUserUpdate(conn, friendID, first +" " + last + " accepted " + firstname + " " + lastname + " as a friend!");
	    			} catch (SQLException e) {
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
    				out.println("<center><h2 style=\"margin-top:0px;\"><br><br>Added " + firstname + " " + lastname + " as a friend!</h2><br></center><br/>");
    			}
    			else {
    				out.println("<center><h2 style=\"margin-top:0px;\"><br><br>Ignored " + firstname + " " + lastname + "'s friend request!</h2><br></center><br/>");
    			}
    			out.println("<center><form action=\"Profile\" method=\"POST\">");
            	out.println("<input type=\"hidden\" name=\"userID\" value=\""+String.valueOf(userID)+"\">");
        		out.println("<input type=\"hidden\" name=\"queryType\" value=\"user\">");
                out.println("<input type=\"submit\" value=\"Return to your profile\"></form></center>");
				
    		} catch (SQLException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			
			
    		
        }
		
		else if (queryType.equals("statusUpdate")){
    		
        	String update = request.getParameter("update").toString();
        	int userID=Integer.parseInt(request.getParameter("userID").toString());
            
    		try {
    			String firstname = q.queryUserAccounts(conn, "firstname", userID);
    			String lastname = q.queryUserAccounts(conn, "lastname", userID);
    			
				createUserStatus(conn, userID, update);
				addUserUpdate(conn, userID, firstname + " " + lastname + " changed status to \"" + update + "\"");
				
				out.println("<center><h2 style=\"margin-top:0px;\"><br><br>Status Updated.</h2><br></center><br/>");
				out.println("<center><form action=\"Profile\" method=\"POST\">");
            	out.println("<input type=\"hidden\" name=\"userID\" value=\""+String.valueOf(userID)+"\">");
        		out.println("<input type=\"hidden\" name=\"queryType\" value=\"user\">");
                out.println("<input type=\"submit\" value=\"Return to your profile\"></form></center>");
				
    		} catch (SQLException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			
			
    		
        }
		else if(queryType.equals("profile")){
			try{
				String firstname = request.getParameter("firstname").toString();
				String lastname = request.getParameter("lastname").toString();
				String email = request.getParameter("email").toString();
				String birth = request.getParameter("birthdate").toString();
				String gender = request.getParameter("gender").toString();
				String address = request.getParameter("address").toString();
				String password = request.getParameter("password").toString();
				String school = request.getParameter("school").toString();
				String degree = request.getParameter("degree").toString();
				String start = request.getParameter("startDate").toString();
				String end = request.getParameter("endDate").toString();
				
				SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
				java.util.Date birthd = dateFormat.parse(birth);
				java.sql.Date birthdate = new java.sql.Date(birthd.getTime());
				
				java.util.Date startDate = dateFormat.parse(start);
				java.sql.Date startDate2 = new java.sql.Date(startDate.getTime());
				
				java.util.Date endDate = dateFormat.parse(end);
				java.sql.Date endDate2 = new java.sql.Date(endDate.getTime());

				int userID = createProfile(conn,firstname,lastname,email,birthdate,gender,address, "");
				
				createUserEducation(conn, userID, school, degree, startDate2, endDate2);

				createPartner(conn, userID, -1, "Single");
				
				createPassword(conn, userID, password);
				
				if (0 != -1) out.println("<br/><br/><p>Succesfully registered! Now you can <a style=\"text-decoration:none;text-color:gray;\" href=\"index.jsp\">login</a>!</p><br/><br/>");
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		else if(queryType.equals("education")){		
			try {
				// have 2 pass userid
				// i wrote the 1st two lines coz i was taking in the userid from a jsp page
				// the userid will be already there 
				String id = request.getParameter("userID");
				int userid= Integer.parseInt(id);
				String school = request.getParameter("school").toString();
				String degree = request.getParameter("degree").toString();
				String startY = request.getParameter("startYear");
				String finishY = request.getParameter("finishYear");
				SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
				java.util.Date startYr = dateFormat.parse(startY);
				java.util.Date finishYr = dateFormat.parse(finishY);
				java.sql.Date startYear = new java.sql.Date(startYr.getTime());
				java.sql.Date finishYear = new java.sql.Date(finishYr.getTime());

				// finishYear should be after startYear
				if(finishYear.after(startYear)){
					createUserEducation(conn,userid,school,degree,startYear, finishYear);
				}
				else{
					System.out.println("please enter correct date");
				}
			}catch (Exception e){
				e.printStackTrace();
			}
		}
		else if (queryType.equals("employment")){ 
			try {
				// have 2 pass userid

				String id = request.getParameter("userID");
				int userid= Integer.parseInt(id);
				String employer = request.getParameter("employer").toString();
				String address = request.getParameter("address").toString();
				String jobtitle = request.getParameter("jobtitle").toString();
				String s = request.getParameter("salary");
				int salary= Integer.parseInt(s);

				String startY = request.getParameter("startYear");
				String endY = request.getParameter("endYear");
				SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
				java.util.Date startYr = dateFormat.parse(startY);
				java.util.Date endYr = dateFormat.parse(endY);
				java.sql.Date startYear = new java.sql.Date(startYr.getTime());
				java.sql.Date endYear = new java.sql.Date(endYr.getTime());

				// endYear should be after startYear
				if(endYear.after(startYear)){
					createUserEmployment(conn,userid,employer,address,jobtitle,salary,startYear, endYear);
				}
				else{
					System.out.println("please enter correct date");
				}
			}catch (Exception e) {
				e.printStackTrace();
			} 
		}
		else if (queryType.equals("status")){ 
			try {
				// have 2 pass userid
				// takes in the system date
				// while displaying will also have to display the time
				String id = request.getParameter("userid");

				int userid= Integer.parseInt(id);
				String statusupdate = request.getParameter("statusupdate").toString();
				if (statusupdate==null){
					createUserStatus(conn,userid, null);
				}
				else{
					createUserStatus(conn,userid, statusupdate);
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if (queryType.equals("interest")){ 
			try {
				// have 2 pass userid
				String id = request.getParameter("userid");
				int userid= Integer.parseInt(id);
				String interest = request.getParameter("interest").toString();
				createUserInterest(conn,userid, interest);
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if (queryType.equals("groups")){ 
			try {// have 2 pass userid
				String name = request.getParameter("name").toString();
				String id = request.getParameter("privilegelevel");
				int privilegelevel= Integer.parseInt(id);
				createUserGroup(conn,name, privilegelevel);
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (queryType.equals("connectedto")){ 
			try {
				// have 2 pass userid,fid, by default the gid is equal to 3,please change wherever required
				String id = request.getParameter("userid");
				int userid= Integer.parseInt(id);
				String id1 = request.getParameter("frienduserid");
				int frienduserid= Integer.parseInt(id1);
				int gid=3;
				createConnectedTo(conn,userid,frienduserid, gid);
			}catch (Exception e) {
				e.printStackTrace();
			}
		}  
		
		Search.printFooter(out);
		try {
			conn.commit();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	//depending on the above querytypes the methods described below are called.

	protected void createUserEducation(Connection conn,int userid, String school,
			String degree,java.sql.Date startYear, java.sql.Date finishYear) throws SQLException, IOException, ClassNotFoundException{
		try{

		String query = "INSERT INTO USEREDUCATION (USERID,SCHOOL,DEGREE,STARTYEAR,FINISHYEAR) VALUES(?,?,?,?,?)";
		PreparedStatement pStmt = null;
		pStmt = conn.prepareStatement(query);
		pStmt.setInt(1,userid);
		pStmt.setString(2,school);
		pStmt.setString(3,degree);
		pStmt.setDate(4, (java.sql.Date) startYear);
		pStmt.setDate(5,(java.sql.Date) finishYear);

		pStmt.executeUpdate();
		pStmt.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		conn.commit();
	}
	protected void createUserEmployment(Connection conn,int userid, String employer,String address, String jobtitle,
			int salary,java.sql.Date startYear, java.sql.Date endYear) throws SQLException, IOException, ClassNotFoundException{
		try{

		String query = "INSERT INTO USEREMPLOYMENT (USERID,EMPLOYER,ADDRESS,JOBTITLE,SALARY,STARTYEAR,ENDYEAR) VALUES(?,?,?,?,?,?,?)";
		PreparedStatement pStmt = null;
		pStmt = conn.prepareStatement(query);
		pStmt.setInt(1,userid);
		pStmt.setString(2,employer);
		pStmt.setString(3,address);
		pStmt.setString(4,jobtitle);
		pStmt.setInt(5,salary);
		pStmt.setDate(6, (java.sql.Date) startYear);
		pStmt.setDate(7,(java.sql.Date) endYear);

		//ResultSet rs = stmt.executeUpdate();
		pStmt.executeUpdate();
		pStmt.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		conn.commit();
	}

	protected void createUserStatus(Connection conn,int userid, String statusupdate) throws SQLException, IOException, ClassNotFoundException{
		try{
		//isstatus=1 from userstatus
		String query = "INSERT INTO USERSTATUS (USERID,STATUSUPDATE,STATUSDATE,ISSTATUS) VALUES(?,?,(SELECT sysdate FROM dual),1)";
		PreparedStatement pStmt = null;
		pStmt = conn.prepareStatement(query);
		pStmt.setInt(1,userid);
		pStmt.setString(2,statusupdate);
		pStmt.executeUpdate();
		pStmt.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		conn.commit();
	}

	protected void createUserInterest(Connection conn,int userid, String interest) throws SQLException, IOException, ClassNotFoundException{
		try{
		String query = "INSERT INTO USERINTERESTS (USERID,INTEREST) VALUES(?,?)";		
		//Statement stmt = conn.createStatement();
		//String query = "INSERT INTO USERACCOUNTS (USERID,FIRSTNAME,LASTNAME,EMAIL,BIRTHDATE,GENDER,ADDRESS,BIO) VALUES"+"("+19+","+String.valueOf(firstname)+","+String.valueOf(lastname)+","+String.valueOf(email)+","+String.valueOf(birthdate)+","+String.valueOf(gender)+","+String.valueOf(address)+","+String.valueOf(bio)+")";

		PreparedStatement pStmt = null;
		pStmt = conn.prepareStatement(query);
		pStmt.setInt(1,userid);
		pStmt.setString(2,interest);
		//ResultSet rs = stmt.executeUpdate();
		pStmt.executeUpdate();
		pStmt.close();

		} catch (Exception e) {
			e.printStackTrace();
		} 
		conn.commit();
	}
	protected void createUserGroup(Connection conn,String name, int privilegelevel) throws SQLException, IOException, ClassNotFoundException{
		try{
		String query = "INSERT INTO USERGROUPS (GID,NAME,PRIVILEGELEVEL) VALUES((select (gid+1) from usergroups where gid = ( select max(gid) from usergroups)),?,?)";		
		//Statement stmt = conn.createStatement();
		//String query = "INSERT INTO USERACCOUNTS (USERID,FIRSTNAME,LASTNAME,EMAIL,BIRTHDATE,GENDER,ADDRESS,BIO) VALUES"+"("+19+","+String.valueOf(firstname)+","+String.valueOf(lastname)+","+String.valueOf(email)+","+String.valueOf(birthdate)+","+String.valueOf(gender)+","+String.valueOf(address)+","+String.valueOf(bio)+")";

		PreparedStatement pStmt = null;
		pStmt = conn.prepareStatement(query);
		pStmt.setString(1,name);
		pStmt.setInt(2,privilegelevel);

		pStmt.executeUpdate();
		pStmt.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		conn.commit();
	}
	protected void createConnectedTo(Connection conn,int userid,int frienduserid, int gid) throws SQLException, IOException, ClassNotFoundException{
		try{	
		String query = "INSERT INTO connectedto (userid,frienduserid,gid) VALUES(?,?,?)";	
		String query1 = "INSERT INTO connectedto (userid,frienduserid,gid) VALUES(?,?,?)";	
		//Statement stmt = conn.createStatement();
		//String query = "INSERT INTO USERACCOUNTS (USERID,FIRSTNAME,LASTNAME,EMAIL,BIRTHDATE,GENDER,ADDRESS,BIO) VALUES"+"("+19+","+String.valueOf(firstname)+","+String.valueOf(lastname)+","+String.valueOf(email)+","+String.valueOf(birthdate)+","+String.valueOf(gender)+","+String.valueOf(address)+","+String.valueOf(bio)+")";

		PreparedStatement pStmt = null;
		pStmt = conn.prepareStatement(query);
		pStmt.setInt(1,userid);
		pStmt.setInt(2,frienduserid);
		pStmt.setInt(3,gid);
		pStmt.executeUpdate();
		pStmt.close();
		PreparedStatement pStmt1 = null;
		pStmt1 = conn.prepareStatement(query1);
		pStmt1.setInt(1,frienduserid);
		pStmt1.setInt(2,userid);
		pStmt1.setInt(3,gid);
		pStmt1.executeUpdate();
		pStmt1.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		conn.commit();
	}
	//username and password to check against UserPasswords relation
	protected int createProfile(Connection conn, String firstname,
            String lastname, String email,java.sql.Date birthdate,
            String gender, String address, String bio) throws SQLException, IOException, ClassNotFoundException{
       
        int userID=-1;
       
        try{   
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT MIN(userID)+1 FROM UserAccounts u1 WHERE userID>0 AND NOT EXISTS(SELECT * FROM UserAccounts u2 WHERE (u1.userID+1)=u2.userID)");
            rs.next();
            userID =rs.getInt("MIN(userID)+1");
            rs.close();
            stmt.close();
       
        //String query = "INSERT INTO USERACCOUNTS(USERID,FIRSTNAME,LASTNAME,EMAIL,BIRTHDATE,GENDER,ADDRESS,BIO) VALUES((SELECT (userid+1) from USERACCOUNTS WHERE USERID = (SELECT MAX(USERID) FROM USERACCOUNTS)),?,?,?,?,?,?,?)";
        //String query1 = "(select (userid+1) from useraccounts where userid = ( select max(userid) from useraccounts))";
        String query = "INSERT INTO USERACCOUNTS(USERID,FIRSTNAME,LASTNAME,EMAIL,BIRTHDATE,GENDER,ADDRESS,BIO) VALUES " +
                        "((SELECT MIN(userID)+1 FROM UserAccounts a1 WHERE userID>0 AND " +
                        "NOT EXISTS(SELECT * FROM UserAccounts a2 WHERE (a1.userID+1)=a2.userID)),?,?,?,?,?,?,?)";
        PreparedStatement pStmt = null;
        pStmt = conn.prepareStatement(query);
        //pStmt = conn.prepareStatement(query1);
        //pStmt.setInt(1,userid);
        pStmt.setString(1,firstname);
        pStmt.setString(2,lastname);
        pStmt.setString(3,email);
        pStmt.setDate(4, (java.sql.Date) birthdate);
        pStmt.setString(5,gender);
        pStmt.setString(6,address);
        pStmt.setString(7,bio);

        pStmt.executeUpdate();
        pStmt.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        conn.commit();
        return userID;

    }
	
	//partnerUserID should be -1 if single or widowed
	protected void createPartner(Connection conn, int userID, int partnerUserID, String relationshipLevel) throws SQLException, IOException, ClassNotFoundException{
		Statement stmt = null;
		stmt = conn.createStatement();
		String pid=String.valueOf(partnerUserID);
		if(partnerUserID==-1){
			pid="NULL";
		}
		String query = "INSERT INTO UserPartner (userID,partnerUserID,relationshipLevel) VALUES ("+userID+","+pid+",'"+relationshipLevel+"')";
		stmt.executeUpdate(query);
		stmt.close();
		conn.commit();
	}
	
	protected void sendMessage(Connection conn, int userID, String subject, String content, int senderID, int privilegeLevel) throws SQLException, IOException, ClassNotFoundException{
		Statement stmt = null;
		ResultSet rs = null;
		int mID = -1;
		String[] contentWords = content.split("\\W");
		String[] subjectWords = subject.split("\\W");
		
		try{
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT MIN(mID)+1 FROM UserMessages m1 WHERE mID>0 AND NOT EXISTS(SELECT * FROM Usermessages m2 WHERE (m1.mID+1)=m2.mID)");
			rs.next();
			try{mID =rs.getInt("MIN(mID)+1");}
			catch(Exception e1){
				e1.printStackTrace();
				return;
			}
			stmt.close();
			rs.close();
			String query = "INSERT INTO UserMessages(userID, mID, subject, content, sendDate, senderID, privilegeLevel, isStatus) " +
						   "VALUES ("+userID+", "+mID+",'"+subject+"', '"+content+"', " +
						   "(SELECT sysdate FROM dual), "+senderID+", "+privilegeLevel+", 0)";
			stmt = conn.createStatement();
			stmt.executeUpdate(query);
		}catch(Exception e){
			e.printStackTrace();
		}
		for(int i=0; i<contentWords.length;i++){
			if(!contentWords[i].equals("")){
				insertWord(conn, contentWords[i].toLowerCase(), mID, "content");
				
			}
		}
		for(int i=0; i<subjectWords.length;i++){
			if(!subjectWords[i].equals("")){
				insertWord(conn, subjectWords[i].toLowerCase(), mID, "subject");
			}
		}
		getDisconnected(null,stmt,null);
		conn.commit();
		return;
	}
	
	//helper method for sendMessage
	private void insertWord(Connection conn, String word, int mID, String location) throws SQLException, IOException, ClassNotFoundException{
		int wordID;
	    Statement stmt = conn.createStatement();
	    String query = "SELECT wordID FROM Lexicon WHERE wordname='"+word.toLowerCase()+"'";
		ResultSet rs = stmt.executeQuery(query);
		rs.next();
		try{wordID =rs.getInt("wordID");
			rs.close();
		}
		catch(Exception e){
			rs.close();
			stmt.close();
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT max(wordID)+1 FROM Lexicon");
			rs.next();
			try{wordID =rs.getInt("max(wordID)+1");}
			catch(Exception e1){
				e1.printStackTrace();
				return;
			}
			rs.close();
			stmt.close();
			
			stmt=conn.createStatement();
			stmt.executeUpdate("INSERT INTO Lexicon (wordID, wordname) VALUES ("+wordID+", '"+word+"')");
		}
		stmt.close();
		
		
		stmt=conn.createStatement();
		stmt.executeUpdate("INSERT INTO Occurs (wordID, mID, location) VALUES ("+wordID+", "+mID+", '"+location+"')");
		
		getDisconnected(null,stmt,null);
		conn.commit();
	}
	
	//boolean that returns true if creates userID with inputed password
	protected boolean createPassword(Connection conn, int userID, String password) throws SQLException, IOException, ClassNotFoundException{
		Statement stmt = null;
		ResultSet rs = null;
		String query = null;

		try{
			stmt = conn.createStatement();
			query="INSERT INTO UserPasswords(userID, password) VALUES ("+String.valueOf(userID)+", '"+password+"')";
			stmt.executeUpdate(query);
		}catch(Exception e){
			getDisconnected(null,stmt,rs);
			return false;
		}
		getDisconnected(null,stmt,rs);
		conn.commit();
		return true;
	}
	
	//send friend request to requestedID from requestorID
	protected int sendFriendRequest(Connection conn,int requestorID, int requestedID) throws SQLException, IOException, ClassNotFoundException{
		if(requestorID==requestedID){
			return 0;
		}
		if(q.alreadyFriends(conn, requestorID, requestedID))
			//already friends fail
			return -1;
	    Statement stmt = conn.createStatement();
	    String query = "SELECT * FROM FriendRequest WHERE (placedID="+String.valueOf(requestorID)+" AND requestedID="+String.valueOf(requestedID)+
	    				") OR (placedID="+String.valueOf(requestedID)+" AND requestedID="+String.valueOf(requestorID)+")";
		ResultSet rs = stmt.executeQuery(query);
		rs.next();
		try{
			int temp =rs.getInt("requestedID");
			if(temp==requestedID)//request already sent
				return -2;
			if(temp==requestorID)//friend is currently inviting you
				return -3;
		}
		catch(Exception e){
			rs.close();
			stmt.close();
		}
		stmt=conn.createStatement();
		query="INSERT INTO FriendRequest (requestedID, placedID) VALUES ("+ requestedID +", "+requestorID +")";
		stmt.executeUpdate(query);
		getDisconnected(null,stmt,rs);
		conn.commit();
	    return 1;
	}
	
	protected void addUserUpdate(Connection conn, int userID, String userUpdate) throws SQLException, IOException, ClassNotFoundException{
	    Statement stmt = conn.createStatement();
	    String query = "INSERT INTO UserUpdates(userID, updateDate, userUpdate) VALUES ("+userID+", (SELECT sysdate FROM dual), '"+userUpdate+"')";
	    stmt.executeUpdate(query);
	    getDisconnected(null,stmt, null);
	    conn.commit();
	}
	
	protected void printSchoolForm(PrintWriter out, int userID){
		out.println("<form action=\"Create\" method=\"POST\">");
		out.println("<strong>Education</strong><br />");
		out.println("   <br />");
		out.println("   School Name");
		out.println("   <span id=\"sprytextfield7\">");
		out.println("   <input name=\"school\" type=\"text\" id=\"school\" size=\"64\" maxlength=\"64\" />");
		out.println("   <span class=\"textfieldRequiredMsg\">A value is required.</span></span> <br />");
		out.println("   <br />");
		out.println("  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Degree <span id=\"sprytextfield8\">");
		out.println("  <input type=\"text\" name=\"degree\" id=\"degree\" />");
		out.println("  <span class=\"textfieldRequiredMsg\">A value is required.</span></span> <br />");
		out.println("   <br />");
		out.println("  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Start Date");
		out.println("   <span id=\"sprytextfield9\">");
		out.println("   <input type=\"text\" name=\"startDate\" id=\"startDate\" />");
		out.println(" <span class=\"textfieldRequiredMsg\">A value is required.</span><span class=\"textfieldInvalidFormatMsg\">Invalid format.</span></span> &nbsp;  &nbsp;&nbsp;&nbsp;&nbsp;</p>");
		out.println(" <p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; End Date&nbsp; <span id=\"sprytextfield10\">");
		out.println(" <input type=\"text\" name=\"endDate\" id=\"endDate\" />");
		out.println(" <span class=\"textfieldRequiredMsg\">A value is required.</span><span class=\"textfieldInvalidFormatMsg\">Invalid format.</span></span> <br />");
		out.println("<input type=\"hidden\" name=\"userID\" value=\""+String.valueOf(userID)+"\">");
		out.println("<input type=\"hidden\" name=\"queryType\" value=\"submitSchool\">");
		out.println("<br/>");
		out.println("<input type=\"submit\" value=\"Add\">");
		out.println("</form>");
		out.println("<br/>");
	}
	
	protected void printInterestForm(PrintWriter out, int userID){
		out.println("	<form action=\"Create\" method=\"post\">");
		out.println("	 <br/>");
		out.println("<h4>New Interest: <input type=\"text\" name=\"interest\" size=\"50\" /></h4>");
		out.println("   <input type=\"hidden\" name=\"queryType\" value=\"submitInterest\" />");
		out.println("<input type=\"hidden\" name=\"userID\" value=\""+String.valueOf(userID)+"\">");
		out.println("   <input type=\"submit\" value=\"Add\" /><br/>");
		out.println("   </form>");
	}
	
	protected void printBioForm(Connection conn, PrintWriter out, int userID){
		String bio = "";
		
		try {
			bio = q.queryUserAccounts(conn, "bio", userID);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		out.println("	<form action=\"Create\" method=\"post\">");
		out.println("	 <br/>");
		out.println("<h4>Bio: <br/><textarea name=\"bio\" style=\"height:200px; width:300px;\" value=\""+ bio + "\"></textarea></h4>");
		out.println("   <input type=\"hidden\" name=\"queryType\" value=\"submitBio\" />");
		out.println("<input type=\"hidden\" name=\"userID\" value=\""+String.valueOf(userID)+"\">");
		out.println("   <input type=\"submit\" value=\"Submit Changes\" /><br/>");
		out.println("   </form>");
	}
	
	protected void printEmploymentForm(PrintWriter out, int userID){
		out.println("<br/>");
		out.println("<form id=\"form1\" action=\"Create\" method=\"POST\">");
		out.println("<strong>Employment History</strong><br />");
		out.println("<br />");
		out.println("<br />");
		out.println("<span id=\"sprytextfield3\">");
		out.println("          <label>Employer  ");
		out.println("            <input type=\"text\" name=\"employer\" id=\"employer\" />");
		out.println("          </label>");
		out.println("          <span class=\"textfieldRequiredMsg\">A value is required.</span></span>");
		out.println("<br/>");
		out.println("<br/>");
		out.println("<span id=\"sprytextfield6\">");
		out.println("          <label>Job Title  ");
		out.println("            <input type=\"text\" name=\"jobTitle\" id=\"jobTitle\" />");
		out.println("          </label>");
		out.println("          <span class=\"textfieldRequiredMsg\">A value is required.</span></span>");
		out.println("<br/>");
		out.println("<br/>");
		out.println("<span id=\"sprytextfield7\">");
		out.println("          <label>Employer Address  ");
		out.println("            <input type=\"text\" name=\"employAdd\" id=\"employAdd\" />");
		out.println("          </label>");
		out.println("          <span class=\"textfieldRequiredMsg\">A value is required.</span></span>");
		out.println("<br/>");
		out.println("<br/>");
		out.println("<span id=\"sprytextfield8\">");
		out.println("          <label>Salary  ");
		out.println("            <input type=\"text\" name=\"salary\" id=\"salary\" />");
		out.println("          </label>");
		out.println("          <span class=\"textfieldRequiredMsg\">A value is required.</span></span>");
		out.println("<br/>");
		out.println("<br/>");
		out.println("<span id=\"sprytextfield9\">");
		out.println("          <label>Start Date  ");
		out.println("            <input type=\"text\" name=\"startDate\" id=\"startDate\" />");
		out.println("          </label>");
		out.println("          <span class=\"textfieldRequiredMsg\">A value is required.</span><span class=\"textfieldInvalidFormatMsg\">Invalid format.</span></span>");
		out.println("<br/>");
		out.println("<br/>");
		out.println("<span id=\"sprytextfield10\">");
		out.println("          <label>End Date  ");
		out.println("            <input type=\"text\" name=\"endDate\" id=\"endDate\" />");
		out.println("          </label>");
		out.println("          <span class=\"textfieldRequiredMsg\">A value is required.</span><span class=\"textfieldInvalidFormatMsg\">Invalid format.</span></span>");
		out.println("<input type=\"hidden\" name=\"userID\" value=\""+String.valueOf(userID)+"\">");
		out.println("<input type=\"hidden\" name=\"queryType\" value=\"submitEmployment\">");
		out.println("<br/>");
		out.println("<br/>");
		out.println("<input type=\"submit\" name=\"addEdu2\" id=\"addEdu2\" value=\"Add\" />");
		out.println("</form>");
		out.println("<br/>");

	}
	
	protected void printPasswordForm(PrintWriter out, int userID){
		out.println("	<form action=\"Create\" method=\"post\">");
		out.println("	 <br/>");
		out.println("<p>New Password: <input type=\"password\" name=\"password\" size=\"20\" /></p>");
		out.println("<p>Confirm New Password: <input type=\"password\" name=\"confirm\" size=\"20\" /></p>");
		out.println("   <input type=\"hidden\" name=\"queryType\" value=\"submitPassword\" />");
		out.println("<input type=\"hidden\" name=\"userID\" value=\""+String.valueOf(userID)+"\">");
		out.println("   <input type=\"submit\" value=\"Change\" /><br/>");
		out.println("   </form>");
	}
}
