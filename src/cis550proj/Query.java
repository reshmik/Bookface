package cis550proj;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import java.sql.*;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Query
 */
public class Query extends HttpServlet implements Servlet {
	private static final long serialVersionUID = 1L;
	 private Map<String, String> numToMonth;  
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Query() {
        super();
	   	 numToMonth = new HashMap<String, String>();
		 numToMonth.put("01", "January");
		 numToMonth.put("02", "Februrary");
		 numToMonth.put("03", "March");
		 numToMonth.put("04", "April");
		 numToMonth.put("05", "May");
		 numToMonth.put("06", "June");
		 numToMonth.put("07", "July");
		 numToMonth.put("08", "August");
		 numToMonth.put("09", "September");
		 numToMonth.put("10", "October");
		 numToMonth.put("11", "November");
		 numToMonth.put("12", "December");
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
		// TODO Auto-generated method stub
	}
	
	//gets a connection to the oracle server
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
	
	//item is either firstname, lastname, email, birthdate, gender, address, bio
	//finds the information of that item from userAccounts with userID
	protected String queryUserAccounts(Connection conn, String item, int userID) throws SQLException, IOException, ClassNotFoundException{
		String result;
	    Statement stmt = conn.createStatement();
	    String query = "SELECT "+item+" FROM UserAccounts WHERE userID="+String.valueOf(userID);
		ResultSet rs = stmt.executeQuery(query);
		rs.next();
		try{result =rs.getString(item);}
		catch(Exception e){
			getDisconnected(null,stmt,rs);
			return null;
		}
		getDisconnected(null,stmt,rs);
	    return result;
	}
	
	//prints out general information
	protected void printGeneral(Connection conn, PrintWriter out, int userID, int currentUserID) throws SQLException, IOException, ClassNotFoundException{
		String birthdate = queryUserAccounts(conn, "birthdate", userID);
		String email = queryUserAccounts(conn, "email", userID);
		String gender = queryUserAccounts(conn, "gender", userID);
		String address = queryUserAccounts(conn, "address", userID);
		String bio = queryUserAccounts(conn, "bio", userID);
        
        String byear = birthdate.substring(0,4);
        String bmonth = numToMonth.get(birthdate.substring(5,7));
        String bday = birthdate.substring(8,10);
        
        int gID = getFriendGID(conn, userID, currentUserID);
        
        out.println("<p>Birthdate: "+bmonth+" "+bday+", "+byear);
        out.println("<br/>Gender: "+gender);
        
        
        if (gID == 3 || userID == currentUserID){
	        out.println("<br/>Address: "+address);
	        out.println("<br/>Email: "+email);
	        printUserPartner(conn, out, userID, currentUserID);
		}
        
        if (gID == 1 || gID == 3 || gID == 4 || userID == currentUserID){
	        if (bio != null) out.println("<p>Bio: "+bio+"</p>");
	        else out.println("<p>Bio: </p>");
        }
	}
	
	protected void printNameAndStatus(Connection conn, PrintWriter out, int userID, int currentUserID, boolean editable) throws SQLException, IOException, ClassNotFoundException{
		String firstname = queryUserAccounts(conn, "firstname", userID);
		String lastname = queryUserAccounts(conn, "lastname", userID);
		out.println("<center><h2 style=\"padding:20px 0px 0px 0px; margin:5px 0px 0px 0px;\">"+firstname+" "+lastname+"'s Profile</h2></center>");
		
        printStatus(conn, out, userID, firstname, lastname, editable);
	}
	
	//prints the partner and level of userID
	protected void printUserPartner(Connection conn, PrintWriter out, int userID, int currentUserID) throws SQLException, IOException, ClassNotFoundException{
		int pid = -1;
		String relationshipLevel = "";
	    Statement stmt = conn.createStatement();
	    String query = "SELECT partnerUserID, relationshipLevel FROM UserPartner WHERE userID="+String.valueOf(userID);
		ResultSet rs = stmt.executeQuery(query);
		rs.next();
		try{
			pid =rs.getInt("partnerUserID");
			relationshipLevel =rs.getString("relationshipLevel");
		}
		catch(Exception e){
			getDisconnected(null,stmt,rs);
			return;
		}
		if(pid!=0 && pid!=-1){
			String firstname = queryUserAccounts(conn, "firstname", pid);
			String lastname = queryUserAccounts(conn, "lastname", pid);
			out.println("<br/>Relationship status: "+relationshipLevel+" with ");
			printFormToUserID(out, pid, currentUserID, firstname+" "+lastname, "partner", true);
		}
		else{
			out.println("<br/>Relationship status: "+relationshipLevel);
		}
		getDisconnected(null,stmt,rs);
	}
	
	
	//prints usereducation in bullet form of userID
	protected void printUserEducation(Connection conn, PrintWriter out, int userID, boolean edit) throws SQLException, IOException, ClassNotFoundException{
		Statement stmt = conn.createStatement();
	    String query = "SELECT * FROM UserEducation WHERE userID="+String.valueOf(userID);
	    int count = 0;
	    ResultSet rs = null;
	    boolean test=false;
	    out.println("<h4>Education:</h4>");
	    try{
			  rs = stmt.executeQuery(query);
			  while (rs.next()) {
				  count++;
				  if(test==false){
					  test=true;
					  out.println("<ul>");
				  }
				  String school = rs.getString("school");
				  String degree = rs.getString("degree");
				  String sYear = rs.getString("startYear");
				  String fYear = rs.getString("finishYear");
				  String startYear = numToMonth.get(sYear.substring(5,7))+ " "+sYear.substring(8,10)+", "+sYear.substring(0,4);
				  String finishYear = numToMonth.get(fYear.substring(5,7))+ " "+fYear.substring(8,10)+", "+fYear.substring(0,4);
				  out.println("<li style=\"font-weight:normal;\">"+school+" from "+startYear+" to "+finishYear+" ("+degree+")</li>");
				  if(edit){
						out.println("<a style=\"font-size:12px;\" href=\"javascript: void(0);\" onclick=\"document.DeleteEducation" + String.valueOf(count)  + ".submit();return false;\">Delete</a></li>");
						out.println("<form name=\"DeleteEducation" + String.valueOf(count) + "\" action=\"Delete\" method=\"POST\">");
			        	out.println("<input type=\"hidden\" name=\"userID\" value=\""+String.valueOf(userID)+"\">");
			        	out.println("<input type=\"hidden\" name=\"school\" value=\""+school+"\">");
			    		out.println("<input type=\"hidden\" name=\"queryType\" value=\"deleteEducation\"></form>");
			            out.println("<br/>");
				  }
			  }
			  if(test==false){
					 out.println("<p>User has no educational experiences.</p>");
			  }else{
				  out.println("</ul>");
			  }
			  
			  if (edit){
				out.println("<center><form action=\"Create\" method=\"POST\">");
	        	out.println("<input type=\"hidden\" name=\"userID\" value=\""+String.valueOf(userID)+"\">");
	    		out.println("<input type=\"hidden\" name=\"queryType\" value=\"addSchool\">");
	            out.println("<input type=\"submit\" value=\"Add School\"></form></center>");
	            out.println("<br/>");
			  }
				 
		 }catch(Exception e){
			  getDisconnected(null,stmt,rs);
			  return;
		 }
		 getDisconnected(null, stmt, rs);
	}
	
	//prints userEmployment of userID in bullet form
	protected void printUserEmployment(Connection conn, PrintWriter out, int userID, boolean edit) throws SQLException, IOException, ClassNotFoundException{
		Statement stmt = conn.createStatement();
	    String query = "SELECT * FROM UserEmployment WHERE userID="+String.valueOf(userID);
	    ResultSet rs = null;
	    int count = 0;
	    boolean test=false;
	    out.println("<h4>Employment:</h4>");
	    try{
			  rs = stmt.executeQuery(query);
			  while (rs.next()) {
				  count ++;
				  if(test==false){
					  test=true;
					  out.println("<ul>");
				  }
				  String employer = rs.getString("employer");
				  String address = rs.getString("address");
				  String jobtitle = rs.getString("jobtitle");
				  String salary = rs.getString("salary");
				  String sYear = rs.getString("startYear");
				  String fYear = rs.getString("endYear");
				  String startYear = numToMonth.get(sYear.substring(5,7))+ " "+sYear.substring(8,10)+", "+sYear.substring(0,4);
				  String finishYear = numToMonth.get(fYear.substring(5,7))+ " "+fYear.substring(8,10)+", "+fYear.substring(0,4);
				  out.println("<li>"+employer+" from "+startYear+" to "+finishYear+" ("+jobtitle+")<br/>Location: &nbsp;&nbsp;"+address+"<br/>Salary:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; $"+salary+"</li>");
			  
				  if(edit){
						out.println("<a style=\"font-size:12px;\" href=\"javascript: void(0);\" onclick=\"document.DeleteEmployment" + String.valueOf(count) + ".submit();return false;\">Delete</a></li>");
						out.println("<form name=\"DeleteEmployment" + String.valueOf(count) + "\" action=\"Delete\" method=\"POST\">");
			        	out.println("<input type=\"hidden\" name=\"userID\" value=\""+String.valueOf(userID)+"\">");
			        	out.println("<input type=\"hidden\" name=\"employer\" value=\""+employer+"\">");
			    		out.println("<input type=\"hidden\" name=\"queryType\" value=\"deleteEmployment\"></form>");
			            out.println("<br/>");
				  }
			  }
			  if(test==false){
					 out.println("<p>User has never been employed.</p>");
			  }else{
				  out.println("</ul>");
			  }
			  
			  if (edit){
					out.println("<center><form action=\"Create\" method=\"POST\">");
		        	out.println("<input type=\"hidden\" name=\"userID\" value=\""+String.valueOf(userID)+"\">");
		    		out.println("<input type=\"hidden\" name=\"queryType\" value=\"addEmployment\">");
		            out.println("<input type=\"submit\" value=\"Add Employment\"></form></center>");
		            out.println("<br/>");
			  }
				 
		 }catch(Exception e){
			  getDisconnected(null,stmt,rs);
			  return;
		 }
		 getDisconnected(null, stmt, rs);
	}
	
	
	//prints list of interests in bullet form
	protected void printInterests(Connection conn, PrintWriter out, int userID, boolean edit) throws SQLException, IOException, ClassNotFoundException{
		LinkedList<String> result = new LinkedList<String>();
		
		  Statement stmt = conn.createStatement();
		  String query = "SELECT interest FROM UserInterests WHERE userID="+String.valueOf(userID);
		  ResultSet rs = stmt.executeQuery(query);
		  int count = 0;
		  while (rs.next()) {
		   result.add(rs.getString("interest"));
		  }
		  getDisconnected(null,stmt,rs);
	        out.println("<h4>Interests:</h4>");
	        if(result.size()==0)
	        	out.println("<p>User has no interests</p>");
	        else{
	        	out.println("<ul>");
				for(String s : result){
					count++;
					out.println("<li>"+s+"</li>");
					if(edit){
						out.println("<a style=\"font-size:12px;\" href=\"javascript: void(0);\" onclick=\"document.DeleteInterest" + String.valueOf(count) + ".submit();return false;\">Delete</a></li>");
						out.println("<form name=\"DeleteInterest" + String.valueOf(count) + "\" action=\"Delete\" method=\"POST\">");
			        	out.println("<input type=\"hidden\" name=\"userID\" value=\""+String.valueOf(userID)+"\">");
			        	out.println("<input type=\"hidden\" name=\"interest\" value=\""+s+"\">");
			    		out.println("<input type=\"hidden\" name=\"queryType\" value=\"deleteInterest\"></form>");
			            out.println("<br/>");
					}
				}
				out.println("</ul>");
	        }
	       
	      if (edit){
	    	out.println("<center><form action=\"Create\" method=\"POST\">");
        	out.println("<input type=\"hidden\" name=\"userID\" value=\""+String.valueOf(userID)+"\">");
    		out.println("<input type=\"hidden\" name=\"queryType\" value=\"addInterest\">");
            out.println("<input type=\"submit\" value=\"Add Interest\"></form></center>");
            out.println("<br/>");
	      }
	      
	}
	
	//prints list of users friends, with link to each friend
	protected void printFriends(Connection conn, PrintWriter out, int userID, int currentUserID) throws SQLException, IOException, ClassNotFoundException{
		LinkedList<String> result = new LinkedList<String>();
		
		  Statement stmt = conn.createStatement();
		  String query = "SELECT friendUserID FROM ConnectedTo WHERE userID="+String.valueOf(userID);
		  ResultSet rs = null;
		  try{
			  rs = stmt.executeQuery(query);
			  while (rs.next()) {
				  int fid = rs.getInt("friendUserID");
				  result.add(String.valueOf(fid));
			  }
		  }catch(Exception e){
			  getDisconnected(null,stmt,rs);
			  return;
		  }
		  getDisconnected(null,stmt,rs);
		
		
		out.println("<h4>Friends:</h4>");
        if(result.size()==0)
        	out.println("<p>User has no friends</p>");
        else{
        	out.println("<ul>");
        	
    		for(String s : result){
    			int fid = Integer.parseInt(s);
    			String name = queryUserAccounts(conn, "firstname", fid) +" "+queryUserAccounts(conn, "lastname", fid);
    			out.println("<li>");
    			printFormToUserID(out, fid, currentUserID, name, "friend", true);
                out.println("</li>");
    		}
    		out.println("</ul>");	
        }
	}
	
	//prints user status
	protected void printStatus(Connection conn, PrintWriter out, int userID, String fname, String lname, boolean editable) throws SQLException, IOException, ClassNotFoundException{
		Statement stmt = conn.createStatement();
	    String query = "SELECT statusUpdate FROM UserStatus WHERE userID = "+String.valueOf(userID)+
	    				" AND statusDate >= ALL(SELECT statusDate FROM UserStatus WHERE userID="+String.valueOf(userID)+")";
	    ResultSet rs = null;
	    try{
			  rs = stmt.executeQuery(query);
			  rs.next();
			  String update = rs.getString("statusUpdate");
			  out.println("<center style=\"font-size:12px\">" + fname+" "+lname+" is "+update);
			if (editable) {
				out.println("   <a style=\"color:blue\" href=\"javascript: void(0);\" onclick=\"document.Status.submit();return false;\">[Update]</a></li>");
				out.println("<form name=\"Status\" action=\"Profile\" method=\"POST\">");
				out.println("<input type=\"hidden\" name=\"userID\" value=\""+String.valueOf(userID)+"\">");
				out.println("<input type=\"hidden\" name=\"queryType\" value=\"status\"></form>");
			}
			  out.println("</center>");
				 
		 }catch(Exception e){
			 out.println("<center style=\"font-size:12px\">" + fname+" "+lname+" is ");
				
			 if(editable) {
				 out.println("   <a style=\"color:blue\" href=\"javascript: void(0);\" onclick=\"document.Status.submit();return false;\">[Update]</a></li>");
				out.println("<form name=\"Status\" action=\"Profile\" method=\"POST\">");
				out.println("<input type=\"hidden\" name=\"userID\" value=\""+String.valueOf(userID)+"\">");
				out.println("<input type=\"hidden\" name=\"queryType\" value=\"status\"></form>");
			 }
			 out.println("</center>");
			 
			  getDisconnected(null,stmt,rs);
			  return;
		 }
		 getDisconnected(null, stmt, rs);
	}
	
	//prints status and wall 
	protected void printStatusandWall(Connection conn, PrintWriter out, int userID) throws SQLException, IOException, ClassNotFoundException{
		Statement stmt = conn.createStatement();
		String query = 	"SELECT senderID, message, to_char(mdate, 'mm/dd/yyyy hh:mi am') as mdate, isStatus FROM " +
				"(SELECT senderID, message, mdate, isStatus FROM " +
				"((SELECT userID as senderID, statusUpdate as message, statusDate as mdate, isStatus " +
				"FROM UserStatus WHERE userID="+String.valueOf(userID)+") UNION " +
				"(SELECT senderID, content as message, sendDate as mdate, isStatus " +
				"FROM UserMessages WHERE userID="+String.valueOf(userID)+"))ORDER BY mdate DESC)";
		ResultSet rs = null;
		boolean test=false;
	    out.println("<br/><h3>Wall and Status Updates:</h3>");
	    try{
			  rs = stmt.executeQuery(query);
			  while (rs.next()) {
				  if(test==false){
					  test=true;
				  }
				  String message = rs.getString("message");
				  String mdate = rs.getString("mdate");
				  String newDate = mdate.replaceAll("/", "");
				  newDate = newDate.replaceAll(" ", "");
				  newDate = newDate.replaceAll(":", "");
				  int isStatus = rs.getInt("isStatus");
				  if(isStatus==1){
					  String firstname = queryUserAccounts(conn, "firstname", userID);
					  String lastname = queryUserAccounts(conn, "lastname", userID);
					  out.println("<p>"+firstname+" "+lastname+" is "+message+"<span style=\"font-size:10px;\"> &nbsp; &nbsp; Sent on "+mdate+"</span></p>");
				  }else{
					  int senderID = rs.getInt("senderID");
					  String firstname = queryUserAccounts(conn, "firstname", senderID);
					  String lastname = queryUserAccounts(conn, "lastname", senderID);
					  
					  out.println("<p>");
					  printFormToUserID(out, senderID, userID, firstname +" "+lastname, "wall"+newDate, false);
					  out.println(" says: "+message+"<span style=\"font-size:10px;\"> &nbsp; &nbsp; Sent on "+mdate+"</span></p>");
				  }
			  }
			  if(test==false){
					 out.println("<p>User has no wall messages or status updates.</p>");
			  }
		 }catch(Exception e){
			  getDisconnected(null,stmt,rs);
			  return;
		 }
		 getDisconnected(null, stmt, rs);
		
	
	}
	
	//prints Form that that provides a link to the userID
	//text is what link will be
	//identifier is a way of labling different forms
	//begin is boolean for whether the link should be before or after the form (affects formatting)
	//userID is userID of person being linked to
	//currentUserID is ID of person who is logged on
	protected void printFormToUserID(PrintWriter out, int userID, int currentUserID, String text, String identifier, boolean begin){

		if (userID != currentUserID){
			if(begin) out.println("<a href=\"javascript: void(0);\" onclick=\"document.P"+identifier+userID+".submit();return false;\">"+text+"</a>");	
			out.println("<form name=\"P"+identifier+userID+"\"action=\"Friend\" method=\"POST\">");
	    	out.println("<input type=\"hidden\" name=\"userID\" value=\""+String.valueOf(currentUserID)+"\">");
	    	out.println("<input type=\"hidden\" name=\"friendUserID\" value=\""+userID+"\">");
			out.println("<input type=\"hidden\" name=\"queryType\" value=\"friend\">");
	        out.println("</form>");
	    	if(!begin) out.println("<a href=\"javascript: void(0);\" onclick=\"document.P"+identifier+userID+".submit();return false;\">"+text+"</a>");	

		}
		else{
			if(begin) out.println("<a href=\"javascript: void(0);\" onclick=\"document.P"+identifier+userID+".submit();return false;\">"+text+"</a>");	
			out.println("<form name=\"P"+identifier+userID+"\"action=\"Profile\" method=\"POST\">");
	    	out.println("<input type=\"hidden\" name=\"userID\" value=\""+String.valueOf(currentUserID)+"\">");
			out.println("<input type=\"hidden\" name=\"queryType\" value=\"user\">");
	        out.println("</form>");
	    	if(!begin) out.println("<a href=\"javascript: void(0);\" onclick=\"document.P"+identifier+userID+".submit();return false;\">"+text+"</a>");	
		}
		
	}
	
	protected String returnFormToUserID(int userID, int currentUserID, String text, String identifier, boolean begin){

		String buf = "";
		
		if (userID != currentUserID){
			if(begin) buf += "<a href=\"javascript: void(0;\" onclick=\"document.P"+identifier+userID+".submit(;return false;\">"+text+"</a>";	
			buf += "<form name=\"P"+identifier+userID+"\"action=\"Friend\" method=\"POST\">";
	    	buf += "<input type=\"hidden\" name=\"userID\" value=\""+String.valueOf(currentUserID)+"\">";
	    	buf += "<input type=\"hidden\" name=\"friendUserID\" value=\""+userID+"\">";
			buf += "<input type=\"hidden\" name=\"queryType\" value=\"friend\">";
	        buf += "</form>";
	    	if(!begin) buf += "<a href=\"javascript: void(0);\" onclick=\"document.P"+identifier+userID+".submit();return false;\">"+text+"</a>";	

		}
		else{
			if(begin) buf += "<a href=\"javascript: void(0);\" onclick=\"document.P"+identifier+userID+".submit();return false;\">"+text+"</a>";	
			buf += "<form name=\"P"+identifier+userID+"\"action=\"Profile\" method=\"POST\">";
	    	buf += "<input type=\"hidden\" name=\"userID\" value=\""+String.valueOf(currentUserID)+"\">";
			buf += "<input type=\"hidden\" name=\"queryType\" value=\"user\">";
	        buf += "</form>";
	    	if(!begin) buf += "<a href=\"javascript: void(0);\" onclick=\"document.P"+identifier+userID+".submit();return false;\">"+text+"</a>";	
		}
		
		return buf;
		
	}
	
	//print private messages sorted by input sort, and ascending or descending based on input asc
	//sort is either senderID, subject, content, mdate, firstname, lastname
	//asc is either ASC or DESC
	protected void printPrivateMessages(Connection conn, PrintWriter out, int userID, String sort, String asc) throws SQLException, IOException, ClassNotFoundException{
		Statement stmt = conn.createStatement();
		String query = "SELECT M.senderID, M.subject, M.content, to_char(M.sendDate, 'mm/dd/yyyy hh:mi am') as mdate," +
					" A.lastname, A.firstname FROM UserMessages M, UserAccounts A " +
					"WHERE M.privilegeLevel=1 AND M.userID="+String.valueOf(userID)+" AND M.senderID = A.userID " +
					"ORDER BY "+sort+" "+asc;
		ResultSet rs = null;
		boolean test=false;
	    out.println("<br/><h3>Private Messages:</h3>");
	    try{
			  rs = stmt.executeQuery(query);
			  while (rs.next()) {
				  if(test==false){
					  test=true;
				  }
				 int senderID = rs.getInt("senderID");
				 String subject =rs.getString("subject");
				 String message = rs.getString("content");
				  String mdate = rs.getString("mdate");
				  String name = rs.getString("firstname")+" "+rs.getString("lastname");
				  String newDate = mdate.replaceAll("/", "");
				  newDate = newDate.replaceAll(" ", "");
				  newDate = newDate.replaceAll(":", "");
				  
					out.println("<b>From: </b> ");
					printFormToUserID(out, senderID, userID, name, "inbox"+newDate, true);
					out.println("<span style=\"font-size:10px;\"><b>Sent on "+mdate+"</b></span><br/>");
					out.println("<b>Subject: </b>"+subject+"<br/>");
					out.println("<b>Message: </b>"+message+"<br/>");
		        	out.println("<form action=\"Inbox\" method=\"POST\">");
			    	out.println("<input type=\"hidden\" name=\"userID\" value=\""+String.valueOf(userID)+"\">");
			    	out.println("<input type=\"hidden\" name=\"friendID\" value=\""+String.valueOf(senderID)+"\">");
			    	out.println("<input type=\"hidden\" name=\"name\" value=\""+name+"\">");
					out.println("<input type=\"hidden\" name=\"queryType\" value=\"compose\">");
			        out.println("<input type=\"submit\" value=\"Reply\"></form>");
			        out.println("<br/><br/>");
			        
			  };
			  if(test==false){
					 out.println("<p>User has no private messages.</p>");
			  }
		 }catch(Exception e){
			 if(test==false){
				 out.println("<p>User has no private messages.</p>");
			 }
			  getDisconnected(null,stmt,rs);
			  return;
		 }
		 getDisconnected(null, stmt, rs);
		
	}
	
	//Checks the GID of friendUserID in relation to userID
	//ex if friendUserID is 1 and userID is 2, then it would return the level that user 2 has set for user 1.
	//when using this function the user that is logged in should be the friendUserID since we want to see what the other useres
	//have set for the logged in user.
	protected int getFriendGID(Connection conn, int friendUserID, int userID) throws SQLException, IOException, ClassNotFoundException{
		int gID;
	    Statement stmt = conn.createStatement();
	    String query = "SELECT gID FROM ConnectedTo WHERE userID="+String.valueOf(userID)+
	    " AND friendUserID="+String.valueOf(friendUserID);
		ResultSet rs = stmt.executeQuery(query);
		rs.next();
		try{gID =rs.getInt("gID");}
		catch(Exception e){
			getDisconnected(null,stmt,rs);
			return -1;
		}
		getDisconnected(null,stmt,rs);
	    return gID;
	}
	
	//queries userGroups Relation for inputted item
	//item is either 'name' or 'privilegeLevel'
	protected String queryUserGroups(Connection conn, int gID, String item) throws SQLException, IOException, ClassNotFoundException{
		String result = null;
	    Statement stmt = conn.createStatement();
	    String query = "SELECT "+item+" FROM UserGroups WHERE gID="+String.valueOf(gID);
		ResultSet rs = stmt.executeQuery(query);
		rs.next();
		try{result =rs.getString(item);}
		catch(Exception e){
			getDisconnected(null,stmt,rs);
			return null;
		}
		getDisconnected(null,stmt,rs);
		return result;
	}
	
	
	
	//prints the users who have requested you as a friend 
	// needs reshmi's addFriend function to add a button after each user to accept request
	protected void printRequested(Connection conn, PrintWriter out, int userID) throws SQLException, IOException, ClassNotFoundException{
		LinkedList<String> result = new LinkedList<String>();
		  Statement stmt = conn.createStatement();
		  String query = "SELECT placedID FROM FriendRequest WHERE requestedID="+String.valueOf(userID);
		  ResultSet rs = null;
		  try{
			  rs = stmt.executeQuery(query);
			  while (rs.next()) {
				  int placedID = rs.getInt("placedID");
				  result.add(String.valueOf(placedID));
			  }
		  }catch(Exception e){
			  getDisconnected(null,stmt,rs);
			  return;
		  }
		  getDisconnected(null,stmt,rs);
		
		
		out.println("<h4>Friend Requests:</h4>");
      if(result.size()==0)
      	out.println("<p>User has no friend requests</p>");
      else{
  		for(String s : result){
  			int placedID = Integer.parseInt(s);
  			String name = queryUserAccounts(conn, "firstname", placedID) +" "+queryUserAccounts(conn, "lastname", placedID);
  			out.println("<div style=\"clear:both;\"><p>");
  			printFormToUserID(out, placedID, userID, name, "friend", false);
  			
  			out.println("</p><div style=\"clear:both;\">");
  			out.println("<a style=\"font-size:12px;clear: left; float:left;\" href=\"javascript: void(0);\" onclick=\"document.Accept.submit();return false;\">Accept</a>");
  			out.println("<form name=\"Accept\" action=\"Create\" method=\"POST\">");
  			out.println("<input type=\"hidden\" name=\"userID\" value=\""+String.valueOf(userID)+"\">");
  			out.println("<input type=\"hidden\" name=\"friendUserID\" value=\""+String.valueOf(placedID)+"\">");
  			out.println("<input type=\"hidden\" name=\"queryType\" value=\"acceptRequest\"></form>");
  			
  			out.println("<a style=\"font-size:12px; clear: right; float:right;\" href=\"javascript: void(0);\" onclick=\"document.Ignore.submit();return false;\">Ignore</a>");
  			out.println("<form name=\"Ignore\" action=\"Create\" method=\"POST\">");
  			out.println("<input type=\"hidden\" name=\"userID\" value=\""+String.valueOf(userID)+"\">");
  			out.println("<input type=\"hidden\" name=\"friendUserID\" value=\""+String.valueOf(placedID)+"\">");
  			out.println("<input type=\"hidden\" name=\"queryType\" value=\"ignoreRequest\"></form>");
  			out.println("</div>");
  			out.println("</div>");
            
    
  		}	
  		out.println("<br/>");
      }
	}
	
	//boolean that checks if inputted users are friends aready
	protected boolean alreadyFriends(Connection conn, int userID, int friendID) throws SQLException, IOException, ClassNotFoundException{
	    Statement stmt = conn.createStatement();
	    String query = "SELECT userID, friendUserID FROM ConnectedTo WHERE userID="+String.valueOf(userID)+" AND friendUserID="+String.valueOf(friendID);
		ResultSet rs = stmt.executeQuery(query);
		rs.next();
		try{@SuppressWarnings("unused")
		int uID =rs.getInt("userID");}
		catch(Exception e){
			getDisconnected(null,stmt,rs);
			return false;
		}
		getDisconnected(null,stmt,rs);
	    return true;
	}
	
	
	//prints a search form that sends a search query to Search.java (used to search for names)
	protected void printFriendSearch(PrintWriter out, int userID){
		out.println("<form action=\"Search\" method=\"POST\">");
		out.println("<input type=\"hidden\" name=\"userID\" value=\""+String.valueOf(userID)+"\">");
		out.println("<input type=\"hidden\" name=\"queryType\" value=\"search\">");
		out.println("Search: <input type=\"text\" name=\"search\" size=\"20\">");
    	out.println("<input type=\"submit\" value=\"Go!\"></form>");
	}
	
	//prints list of FriendRecommendations (Need to edit to add buttons after each name)
	protected void printFriendRecommendations(Connection conn, PrintWriter out, int userID) throws SQLException, IOException, ClassNotFoundException{
		Set<String> result = new HashSet<String>();
		try {
			Set<String> friendsOfFriend = getFriendsOfFriend(conn, userID);
			Set<String> classmates = getClassmates(conn, userID);
			Set<String> coworkers = getCoworkers(conn, userID);
			result.addAll(friendsOfFriend);
			result.addAll(classmates);
			result.addAll(coworkers);
		} catch (Exception e) {
			//e.printStackTrace();
		} 
		out.println("<h4>Friend Recommendations:</h4>");
        if(result.size()==0)
        	out.println("<p>No recommended friends</p>");
        else{
        	out.println("<ul>");
        	
    		for(String s : result){
    			int fid = Integer.parseInt(s);
    			String name = queryUserAccounts(conn, "firstname", fid) +" "+queryUserAccounts(conn, "lastname", fid);
    			out.println("<li>");
    			printFormToUserID(out, fid, userID, name, "friend", true);
                out.println("</li>");
    		}
    		out.println("</ul>");	
        }
		
	}
	
	//finds non-friends that are friends of a friend (used in printFriendRecommendations())
	protected Set<String> getFriendsOfFriend(Connection conn, int userID) throws SQLException, IOException, ClassNotFoundException{
		Set<String> result = new HashSet<String>();
		  Statement stmt = conn.createStatement();
		  String query = "SELECT DISTINCT C2.friendUserID FROM ConnectedTo C1, ConnectedTo C2 " +
		  		"WHERE C1.userID="+String.valueOf(userID)+ " AND C1.userID != C2.friendUserID AND C1.friendUserID=C2.userID " +
		  		"AND NOT EXISTS(SELECT * FROM ConnectedTo C3 WHERE C3.userID=C1.userID AND C2.friendUserID=C3.friendUserID)";
		  ResultSet rs = null;
		  try{
			  rs = stmt.executeQuery(query);
			  while (rs.next()) {
				  int fid = rs.getInt("friendUserID");
				  result.add(String.valueOf(fid));
			  }
		  }catch(Exception e){
			  getDisconnected(null,stmt,rs);
			  return null;
		  }
		  getDisconnected(null,stmt,rs);
		return result;
	}
	
	//find non-friends that are at the school at the same time as you (used in printFriendRecommendations())
	protected Set<String> getClassmates(Connection conn, int userID) throws SQLException, IOException, ClassNotFoundException{
		Set<String> result = new HashSet<String>();
		  Statement stmt = conn.createStatement();
		  String query = "SELECT E2.userID FROM UserEducation E1, UserEducation E2 WHERE E1.userID="+String.valueOf(userID)+
		  				 " AND E1.userID!=E2.userID AND E1.school=E2.school AND (E1.startYear, E1.finishYear) " +
		  				 "OVERLAPS (E2.startYear, E2.finishYear) AND NOT EXISTS(SELECT * FROM ConnectedTo C3 " +
		  				 "WHERE C3.userID=E1.userID AND E2.friendUserID=C3.friendUserID)";
		  ResultSet rs = null;
		  try{
			  rs = stmt.executeQuery(query);
			  while (rs.next()) {
				  int fid = rs.getInt("friendUserID");
				  result.add(String.valueOf(fid));
			  }
		  }catch(Exception e){
			  getDisconnected(null,stmt,rs);
			  return null;
		  }
		  getDisconnected(null,stmt,rs);
		return result;
	}
	
	//find non-friends that are at the school at the same time as you (used in printFriendRecommendations())
	protected Set<String> getCoworkers(Connection conn, int userID) throws SQLException, IOException, ClassNotFoundException{
		Set<String> result = new HashSet<String>();
		  Statement stmt = conn.createStatement();
		  String query = "SELECT E2.userID FROM UserEmployment E1, UserEmployment E2"+
		  				 " WHERE E1.userID="+String.valueOf(userID)+" AND E1.userID!=E2.userID AND E1.address=E2.address AND E1.employer=E2.employer " +
		  				 "AND (E1.startYear, E1.endYear) OVERLAPS (E2.startYear, E2.endYear)"+
		  				 " AND NOT EXISTS(SELECT * FROM ConnectedTo C3 " +
		  				 "WHERE C3.userID=E1.userID AND E2.friendUserID=C3.friendUserID)";
		  ResultSet rs = null;
		  try{
			  rs = stmt.executeQuery(query);
			  while (rs.next()) {
				  int fid = rs.getInt("friendUserID");
				  result.add(String.valueOf(fid));
			  }
		  }catch(Exception e){
			  getDisconnected(null,stmt,rs);
			  return null;
		  }
		  getDisconnected(null,stmt,rs);
		return result;
	}
	
	
	protected boolean hasOneEmployment(Connection conn, int userID) throws SQLException, IOException, ClassNotFoundException{
		Statement stmt = conn.createStatement();
		String query = "SELECT COUNT(*) FROM UserEmployment WHERE userID="+userID;
		ResultSet rs=stmt.executeQuery(query);
		int result = -1;
		try{
			rs.next();
			result=rs.getInt("COUNT(*)");
		}catch(Exception e){
			e.printStackTrace();
			getDisconnected(null,stmt,rs);
			return false;
		}
		if(result==1){
			getDisconnected(null,stmt,rs);
			return false;
		}
		getDisconnected(null,stmt,rs);
		return true;
	}
	
	protected void printUserUpdate(Connection conn, PrintWriter out, int userID) throws SQLException, IOException, ClassNotFoundException{
	    String query = "SELECT userUpdate, to_char(updateDate, 'mm/dd/yyyy hh:mi: am') as updateDate FROM UserUpdates WHERE userID="+userID+"  OR userID=ANY(SELECT friendUserID FROM ConnectedTo WHERE userID="+userID+") ORDER BY  updateDate DESC";
	    Statement stmt = conn.createStatement();
	    ResultSet rs = stmt.executeQuery(query);
	    boolean test = false;
	    out.println("<br/><h3>Notifications:</h3>");
	    while(rs.next()){
	        if(test==false){
	            test=true;
	        }
	        String update = rs.getString("userUpdate");
	        String date = rs.getString("updateDate");
	        out.println("<p>"+update+"<span style=\"font-size:10px;\"> &nbsp; &nbsp; Sent on "+date+"</span></p>");
	               
	    }
	    if(test==false){
	        out.println("<p>User has no notifications.</p>");
	    }
	    getDisconnected(null,stmt,rs);

	}
}
