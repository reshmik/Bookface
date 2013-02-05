package cis550proj;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Search
 */
public class Search extends HttpServlet implements Servlet {
	private static final long serialVersionUID = 1L;
	private Query q;  
	private Create c;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Search() {
        super();
        q = new Query();
        c = new Create();
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
        PrintWriter out = response.getWriter();
        String queryType = request.getParameter("queryType").toString();
        int userID;
	    Connection conn = null;
		try {
			conn = q.getConnected();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	    if(queryType.equals("friendRequest")){
	    	userID=Integer.parseInt(request.getParameter("userID").toString());
			int requestedID=Integer.parseInt(request.getParameter("requestedID").toString());
			int result = 0;
			
	    	printHeader(out, "PennSocial Login");	  
	    	out.println("<br/><br/>");
	        
			try {
				result = c.sendFriendRequest(conn, userID, requestedID);
				System.out.println("User: " + userID + " Req: " + requestedID);
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			if(result==1)
				out.println("<center><h4><br><br>Friend request sent.</h4><br></center>");
			else if(result==-1)
				out.println("<center><h4><br><br>Users are already friends.</h4><br></center>");
			else if(result==-2)
				out.println("<center><h4><br><br>A current request is pending.</h4><br></center>");
			else if(result==-3)
				out.println("<center><h4><br><br>This friend has already sent you a friend request.</h4><br></center>");
			else if(result==0)
				out.println("<center><h4><br><br>Cannot send yourself a friend request.</h4><br></center>");

			out.println("<center><FORM><INPUT TYPE=\"button\" VALUE=\"Back\" onClick=\"history.go(-1);return true;\"> </FORM></center> ");
			printFooter(out);

	    }
	    else if(queryType.equals("search") || queryType.equals("searchPrivacy") || queryType.equals("searchRel") || queryType.equals("compose")){		
        	try {
        		printHeader(out, "PennSocial Search");
        		
    			userID=Integer.parseInt(request.getParameter("userID").toString());
    			String searchString = request.getParameter("search").toString();
    			
		        printSearch(conn, out, userID, searchString, queryType.equals("compose"), queryType.equals("searchPrivacy"), queryType.equals("searchRel"));
		        
		        if (!queryType.equals("compose")) q.printFriendSearch(out, userID);
		        else {
		        	out.println("<form action=\"Search\" method=\"POST\">");
		    		out.println("<input type=\"hidden\" name=\"userID\" value=\""+String.valueOf(userID)+"\">");
		    		out.println("<input type=\"hidden\" name=\"queryType\" value=\"compose\">");
		    		out.println("Search: <input type=\"text\" name=\"search\" size=\"20\">");
		        	out.println("<input type=\"submit\" value=\"Go!\"></form>");
		        }
		        
		        out.println("<center><form action=\"Profile\" method=\"POST\">");
            	out.println("<input type=\"hidden\" name=\"userID\" value=\""+String.valueOf(userID)+"\">");
        		out.println("<input type=\"hidden\" name=\"queryType\" value=\"user\"><br/>");
                out.println("<input type=\"submit\" value=\"Return to your profile\"></form></center><br/><br/>");
				
				printFooter(out);
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
        }
        else{
        	out.println("<html>");
    	    out.println("<head>");
        	out.println("<title>Error!</title>");
	        out.println("</head>");
	        out.println("<body>");
	        out.println("<h1>Error!</h1>");
	        out.println("<p>Please Contact Webmaster.</p>");
	        out.println("</body>");
	        out.println("</html>");
        }
        try {
			q.getDisconnected(conn, null, null);
		} catch (SQLException e) {
			e.printStackTrace();
		}
        out.close();
	}

	//prints search results for name in searchString
	protected void printSearch(Connection conn, PrintWriter out, int userID, String searchString, boolean compose, boolean isPrivacy, boolean isRel) throws SQLException, IOException, ClassNotFoundException{
		String temp = searchString.trim();
		String[] keywords = null;
		keywords = searchString.trim().split(" ");
		if(keywords.length==1 && keywords[0].equals("")){
			out.println("<br/>");
			if (isPrivacy) out.println("Search for a friend to change his/her privacy settings:");
			else if (isRel) out.println("Search for a friend to change relationship status:");
			else if (compose) out.println("Search for a friend to send a message to:");
			else out.println("Invalid search. Try again");
			out.println("<br/><br/>");
			return;
		}
		searchString=keywords[0].toLowerCase();
		for(int i=1;i<keywords.length;i++){
			searchString=searchString+" "+keywords[i].toLowerCase();
		}
		String query = "SELECT userID, firstname, lastname FROM UserAccounts WHERE " +
					"( LOWER(CONCAT(CONCAT(firstname,' '),lastname))='"+searchString+"' OR LOWER(CONCAT(CONCAT(lastname,' '),firstname))='"+searchString+
					"' OR LOWER(firstname) ='"+searchString+"' OR LOWER(lastname)='"+searchString+"') ORDER BY lastname ASC";

		Statement stmt = conn.createStatement();
		ResultSet rs = null;
		boolean test=false;
		
		if (compose){
			out.println("<br/><h4>Search for friends to send message"+temp+":</h4>");
		}
	    out.println("<br/><br/><h3>Search Results for "+temp+":</h3>");
	    try{
			  rs = stmt.executeQuery(query);
			  while (rs.next()) {
				  if(test==false){
					  out.println("<ul>");
					  test=true;
				  }
				 int sID = rs.getInt("userID");
				 String firstname =rs.getString("firstname");
				 String lastname = rs.getString("lastname");
				 
				 	out.println("<li>");
				 	q.printFormToUserID(out, sID, userID, firstname+" "+lastname, "friend", false);
				 	
				 	if (!compose){
					 	if(q.alreadyFriends(conn, userID, sID)){
					 		out.print("(Already Friends)");
					 	}
					 	else {
					 		if (userID == sID) out.print("(You!)");
					 		else printFriendRequestButton(out, userID, sID);
					 	}
		                out.println("</li>");
				 	}
				 	
				 	if (userID != sID){
					 	out.println("<form action=\"Update\" method=\"POST\">");
				    	out.println("<input type=\"hidden\" name=\"userID\" value=\""+String.valueOf(userID)+"\">");
				    	out.println("<input type=\"hidden\" name=\"friendID\" value=\""+String.valueOf(sID)+"\">");
				    	out.println("<input type=\"hidden\" name=\"name\" value=\""+firstname + " " + lastname +"\">");
						out.println("<input type=\"hidden\" name=\"queryType\" value=\"privacyChange\">");
				        out.println("<input type=\"submit\" value=\"Change privacy settings\"></form>");
				 	}
				 	
				 	if (q.alreadyFriends(conn, userID, sID)){
				 		out.println("<form action=\"Update\" method=\"POST\">");
				    	out.println("<input type=\"hidden\" name=\"userID\" value=\""+String.valueOf(userID)+"\">");
				    	out.println("<input type=\"hidden\" name=\"friendID\" value=\""+String.valueOf(sID)+"\">");
				    	out.println("<input type=\"hidden\" name=\"name\" value=\""+firstname + " " + lastname +"\">");
						out.println("<input type=\"hidden\" name=\"queryType\" value=\"relChange\">");
				        out.println("<input type=\"submit\" value=\"Change relationship status\"></form>");
				 	}
				 	
			        out.println("<form action=\"Inbox\" method=\"POST\">");
			    	out.println("<input type=\"hidden\" name=\"userID\" value=\""+String.valueOf(userID)+"\">");
			    	out.println("<input type=\"hidden\" name=\"friendID\" value=\""+String.valueOf(sID)+"\">");
			    	out.println("<input type=\"hidden\" name=\"name\" value=\""+firstname + " " + lastname +"\">");
					out.println("<input type=\"hidden\" name=\"queryType\" value=\"compose\">");
			        out.println("<input type=\"submit\" value=\"Send Message\"></form>");
			 		
			        out.println("<br/>");
			  }
			  if(test==false){
					 out.println("<p>There are no users that match your search.</p>");
			  }else{
				  out.println("</ul>");	
			  }
		 }catch(Exception e){
			 if(test==false){
				 out.println("<p>There are no users that match your search.</p>");
			 }
			  q.getDisconnected(null,stmt,rs);
			  return;
		 }
		 q.getDisconnected(null, stmt, rs);
	}
	
	protected void printFriendRequestButton(PrintWriter out, int userID, int requestedID){
		out.println("<form action=\"Search\" method=\"POST\">");
    	out.println("<input type=\"hidden\" name=\"userID\" value=\""+String.valueOf(userID)+"\">");
    	out.println("<input type=\"hidden\" name=\"requestedID\" value=\""+String.valueOf(requestedID)+"\">");
		out.println("<input type=\"hidden\" name=\"queryType\" value=\"friendRequest\">");
        out.println("<input type=\"submit\" value=\"Send friend request\"></form>");
	}
	
	//searches through messages by search String (which is a keyword not name) and returns messages.  
	//results are sorted by 'sort' input and are ascending or descending based on 'asc' input
	//sort is either senderID, subject, content, mdate, firstname, lastname
	//asc is either ASC or DESC
	protected void searchMessagesByKeyword(Connection conn, PrintWriter out, int userID, String searchString, String sort, String asc) throws SQLException, IOException, ClassNotFoundException{
		//parse searchString
		String[] keywords = null;
		keywords = searchString.trim().split(" ");
		if(keywords.length==1 && keywords[0].equals("")){
			out.println("invalid search. try again");
			return;
		}
		String query = "(SELECT DISTINCT mID FROM Occurs WHERE wordID = ANY( " +
				"SELECT wordID FROM Lexicon WHERE LOWER(wordname) = '"+keywords[0].toLowerCase()+"'))";
		for(int i=1; i<keywords.length; i++){
			query= query + "INTERSECT " +
			"(SELECT DISTINCT mID FROM Occurs WHERE wordID = ANY( " +
			"SELECT wordID FROM Lexicon WHERE LOWER(wordname) = '"+keywords[i].toLowerCase()+"'))";
		}
		Statement stmt = conn.createStatement();
		ResultSet rs = null;
		boolean test=false;
	    out.println("<br/><h3>Search Results for "+searchString+":</h3>");
	    try{
			  rs = stmt.executeQuery(query);
			  while (rs.next()) {
				  if(test==false){
					  test=true;
				  }
				  String mid = rs.getString("mID");
				  Statement stmt2 = conn.createStatement();
				  String query2 = "SELECT M.senderID, M.subject, M.content, to_char(M.sendDate, 'mm/dd/yyyy hh:mi am') as mdate," +
					" A.lastname, A.firstname FROM UserMessages M, UserAccounts A " +
					"WHERE M.privilegeLevel=1 AND M.userID="+String.valueOf(userID)+" AND M.senderID = A.userID AND M.mID ="+mid +
					" ORDER BY "+sort+" "+asc;
				  ResultSet rs2 = null;
				  rs2 = stmt2.executeQuery(query2);
				while(rs2.next()){
					int senderID = rs2.getInt("senderID");
					 String subject =rs2.getString("subject");
					 String message = rs2.getString("content");
					  String mdate = rs2.getString("mdate");
					  String name = rs2.getString("firstname")+" "+rs2.getString("lastname");
					  String newDate = mdate.replaceAll("/", "");
					  newDate = newDate.replaceAll(" ", "");
					  newDate = newDate.replaceAll(":", "");
					  
						out.println("<b>From: </b> ");
						q.printFormToUserID(out, senderID, userID, name, "inbox"+newDate, true);
						out.println("<span style=\"font-size:10px;\"><b>Sent on "+mdate+"</b></span><br/>");
						out.println("<b>Subject: </b>"+subject+"<br/>");
						out.println("<b>Message: </b>"+message+"<br/><br/>");
				}
				rs2.close();
				stmt2.close();
			  }
			  if(test==false){
					 out.println("<p>There are no messages that match your search.</p>");
			  }
		 }catch(Exception e){
			 if(test==false){
				 out.println("<p>There are no messages that match your search.</p>");
			 }
			  q.getDisconnected(null,stmt,rs);
			  return;
		 }
		 
		 q.getDisconnected(null, stmt, rs);
		
	}
	
	//searches through messages by search String (which is a sendername) and returns messages.  
	//results are sorted by 'sort' input and are ascending or descending based on 'asc' input
	//sort is either senderID, subject, content, mdate, firstname, lastname
	//asc is either ASC or DESC
	protected void searchMessagesBySender(Connection conn, PrintWriter out, int userID, String searchString, String sort, String asc) throws SQLException, IOException, ClassNotFoundException{
		String temp = searchString.trim();
		String[] keywords = null;
		keywords = searchString.trim().split(" ");    
		if(keywords.length==1 && keywords[0].equals("")){
			out.println("invalid search. try again");
			return;
		}
		searchString=keywords[0].toLowerCase();
		for(int i=1;i<keywords.length;i++){
			searchString=searchString+" "+keywords[i].toLowerCase();
		}
		String query = "SELECT M.senderID, M.subject, M.content, to_char(M.sendDate, 'mm/dd/yyyy hh:mi am') as mdate, A.lastname, A.firstname " +
					"FROM UserMessages M, UserAccounts A WHERE M.privilegeLevel=1 AND M.userID="+String.valueOf(userID)+" AND M.senderID=A.userID AND " +
					"( LOWER(CONCAT(CONCAT(A.firstname,' '),A.lastname))='"+searchString+"' OR LOWER(CONCAT(CONCAT(A.lastname,' '),A.firstname))='"+searchString+
					"' OR LOWER(A.firstname) ='"+searchString+"' OR LOWER(A.lastname)='"+searchString+"') ORDER BY lastname "+asc;
		Statement stmt = conn.createStatement();
		ResultSet rs = null;
		boolean test=false;
	    out.println("<br/><h3>Search Results for "+temp+":</h3>");
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
					q.printFormToUserID(out, senderID, userID, name, "inbox"+newDate, true);
					out.println("<span style=\"font-size:10px;\"><b>Sent on "+mdate+"</b></span><br/>");
					out.println("<b>Subject: </b>"+subject+"<br/>");
					out.println("<b>Message: </b>"+message+"<br/><br/>");
			  }
			  if(test==false){
					 out.println("<p>There are no messages that match your search.</p>");
			  }
		 }catch(Exception e){
			 if(test==false){
				 out.println("<p>There are no messages that match your search.</p>");
			 }
			  q.getDisconnected(null,stmt,rs);
			  return;
		 }
		 q.getDisconnected(null, stmt, rs);
		
	}
	
	protected static void printHeader(PrintWriter out, String s){
		out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
		out.println("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
		out.println("<head>");
		out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />");
		out.println("<!-- TemplateBeginEditable name=\"doctitle\" -->");
		out.println("<title>" + s + "</title>");
		out.println("<!-- TemplateEndEditable -->");
		out.println("<!-- TemplateBeginEditable name=\"head\" -->");
		out.println("<!-- TemplateEndEditable -->");
		out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"css/style.css\" media=\"screen\" />");
		out.println("<script src=\"SpryAssets/SpryValidationTextField.js\" type=\"text/javascript\"></script>");
		out.println("<script src=\"SpryAssets/SpryValidationPassword.js\" type=\"text/javascript\"></script>");
		out.println("<link href=\"SpryAssets/SpryValidationTextField.css\" rel=\"stylesheet\" type=\"text/css\" />");
		out.println("<link href=\"SpryAssets/SpryValidationPassword.css\" rel=\"stylesheet\" type=\"text/css\" />");
		out.println("</head>");
		out.println("<body class=\"oneColFixCtrHdr\">");
		out.println("");
		out.println("<div id=\"container\">");
		out.println("  <div id=\"header\">");
		out.println("    <h1 style=\"display:none;\">PennSocial</h1>");
		out.println("  <!-- end #header --></div>");
		out.println("  <div id=\"mainContent\">");
	}
	
	protected static void printFooter(PrintWriter out){
		out.println("</div>");
		out.println("<div id=\"footer\">");
		out.println("<p>&copy;&nbsp;2009 Copyrights reserved</p>");
		out.println("<!-- end #footer --></div>");
		out.println("<!-- end #container --></div>");

		out.println("<script type=\"text/javascript\">");
		out.println("<!--");
		out.println("var spryselect1 = new Spry.Widget.ValidationSelect(\"spryselect1\", {isRequired:false, invalidValue:\"Select Sex\"});");
		out.println("var sprytextfield1 = new Spry.Widget.ValidationTextField(\"username\");");
		out.println("var sprypassword1 = new Spry.Widget.ValidationPassword(\"sprypassword1\");");
		out.println("var sprytextfield2 = new Spry.Widget.ValidationTextField(\"sprytextfield2\");");
		out.println("var sprytextfield3 = new Spry.Widget.ValidationTextField(\"sprytextfield3\");");
		out.println("var sprytextfield4 = new Spry.Widget.ValidationTextField(\"sprytextfield4\", \"email\");");
		out.println("var sprytextfield5 = new Spry.Widget.ValidationTextField(\"sprytextfield5\", \"none\");");
		out.println("var sprytextfield6 = new Spry.Widget.ValidationTextField(\"sprytextfield6\");");
		out.println("var sprytextfield7 = new Spry.Widget.ValidationTextField(\"sprytextfield7\");");
		out.println("var sprytextfield8 = new Spry.Widget.ValidationTextField(\"sprytextfield8\");");
		out.println("var sprytextfield9 = new Spry.Widget.ValidationTextField(\"sprytextfield9\", \"date\", {format:\"mm/dd/yyyy\", hint:\" mm/dd/yyyy\"});");
		out.println("var sprytextfield10 = new Spry.Widget.ValidationTextField(\"sprytextfield10\", \"date\", {hint:\"mm/dd/yyyy\", format:\"mm/dd/yyyy\"});");
		out.println("var spryconfirm1 = new Spry.Widget.ValidationConfirm(\"spryconfirm1\", \"password\");");
		out.println("var sprytextfield11 = new Spry.Widget.ValidationTextField(\"sprytextfield11\", \"integer\");");
		out.println("var spryselect2 = new Spry.Widget.ValidationSelect(\"spryselect2\", {invalidValue:\"Status\"});");
		out.println("var sprytextfield12 = new Spry.Widget.ValidationTextField(\"sprytextfield12\", \"date\", {hint:\"mm/dd/yyyy\", format:\"mm/dd/yyyy\"});");
		out.println("//-->");
		out.println("</script>");
		
		out.println("</body>");
		out.println("</html>");

	}
	
}
