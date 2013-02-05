package cis550proj;

import java.io.*;


import java.sql.*;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Profile
 */
public class Profile extends HttpServlet implements Servlet {
	private static final long serialVersionUID = 1L;
    private Query q;
    private Delete d;
	/**
     * @see HttpServlet#HttpServlet()
     */
     public Profile() {
    	 super();
    	 q = new Query();
    	 d = new Delete();
     }
    
	 public Connection getConnected() throws SQLException, IOException, ClassNotFoundException{
		  Class.forName("oracle.jdbc.driver.OracleDriver");
		  Connection conn = DriverManager.getConnection
		  ("jdbc:oracle:thin:@fling.seas.upenn.edu:1521/cisora", "jasonns", "suapengco");
		  return conn;
	 }
	 
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
        PrintWriter out = response.getWriter();
        String queryType = request.getParameter("queryType").toString();
        int userID;

        if (queryType.equals("status")){
    			userID=Integer.parseInt(request.getParameter("userID").toString());

        		Search.printHeader(out, "Status Update");
        		
        		out.println("	<form action=\"Create\" method=\"post\">");
        		out.println("	 <br/>");
        		out.println("<h4>New Status: <input type=\"text\" name=\"update\" size=\"50\" /></h4>");
        		out.println("   <input type=\"hidden\" name=\"queryType\" value=\"statusUpdate\" />");
        		out.println("<input type=\"hidden\" name=\"userID\" value=\""+String.valueOf(userID)+"\">");
        		out.println("   <input type=\"submit\" value=\"Update\" /><br/>");
        		out.println("   </form>");
        		
        		Search.printFooter(out);
        }
        else if(queryType.equals("login")||queryType.equals("user")||queryType.equals("home")){		
        	try {
        		Connection conn = getConnected();
        		if(queryType.equals("login")){
        			
        			String username = request.getParameter("username").toString();
                	String password = request.getParameter("password").toString();
        			userID=tryLogin(conn, username, password);
        			
        			
        		}else{
        			userID=Integer.parseInt(request.getParameter("userID").toString());
        		}
				if(userID>=0){ //indicates sucessful login
									
					if (queryType.equals("user")){
						printHeader(out, "Profile", userID);
						
						printSidebar(conn, q, out, userID);
						
						printMain(conn, q, out, userID, true);
						out.println("</body></html>");
					}
					else{
						
						if(queryType.equals("login")) d.deleteAllUserUpdates(conn, userID); //Delete updates
						
						printHeader(out, "Profile", userID);
						
						printSidebar(conn, q, out, userID);
						
						printHomeMain(conn, q, out, userID, true);
						
						out.println("</body></html>");
					}

					
				}else if(userID==-1){
					Search.printHeader(out, "Bookface | Invalid Login");
					out.println("<center><h1 style=\"margin-top:0px;\"><br><br>INCORRECT USERNAME.</h1><br></center>");
					out.println("<center><FORM><INPUT TYPE=\"button\" VALUE=\"Try again.\" onClick=\"history.go(-1);return true;\"> </FORM></center> ");
					Search.printFooter(out);
				}else{
					Search.printHeader(out, "Bookface | Invalid Login");
					out.println("<center><h1 style=\"margin-top:0px;\" ><br><br>INCORRECT PASSWORD.</h1><br></center>");
					out.println("<center><FORM><INPUT TYPE=\"button\" VALUE=\"Try again.\" onClick=\"history.go(-1);return true;\"> </FORM></center> ");
					Search.printFooter(out);
				}
				getDisconnected(conn,null,null);
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
        }
        out.println("</html>");
        out.close();
	}
	
	protected static void printHeader(PrintWriter out, String s, int userID){
		out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
		out.println("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
		out.println("<head>");
		out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />");
		out.println("<!-- TemplateBeginEditable name=\"doctitle\" -->");
		out.println("<title>" + s + "</title>");
		out.println("<!-- TemplateEndEditable -->");
		out.println("<!-- TemplateBeginEditable name=\"head\" -->");
		out.println("<!-- TemplateEndEditable -->");
		out.println("");
		out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"css/style.css\" media=\"screen\" />");
		out.println("");
		out.println("<!--[if IE 5]>");
		out.println("<style type=\"text/css\"> ");
		out.println("/* place css box model fixes for IE 5* in this conditional comment */");
		out.println(".twoColFixLtHdr #sidebar1 { width: 230px; }");
		out.println("</style>");
		out.println("<![endif]--><!--[if IE]>");
		out.println("<style type=\"text/css\"> ");
		out.println("/* place css fixes for all versions of IE in this conditional comment */");
		out.println(".twoColFixLtHdr #sidebar1 { padding-top: 30px; }");
		out.println(".twoColFixLtHdr #mainContent { zoom: 1; }");
		out.println("/* the above proprietary zoom property gives IE the hasLayout it needs to avoid several bugs */");
		out.println("</style>");
		out.println("<![endif]-->");
		out.println("<script src=\"SpryAssets/SpryMenuBar.js\" type=\"text/javascript\"></script>");
		out.println("<script src=\"SpryAssets/SpryValidationCheckbox.js\" type=\"text/javascript\"></script>");
		out.println("<script src=\"SpryAssets/SpryValidationTextField.js\" type=\"text/javascript\"></script>");
		out.println("<link href=\"SpryAssets/SpryMenuBarHorizontal.css\" rel=\"stylesheet\" type=\"text/css\" />");
		out.println("<style type=\"text/css\">");
		out.println("<!--");
		out.println("a:link {");
		out.println("	color: #FFF;");
		out.println("	text-decoration: none;");
		out.println("}");
		out.println("a:visited {");
		out.println("	text-decoration: none;");
		out.println("	color: #FFF;");
		out.println("}");
		out.println("a:hover {");
		out.println("	text-decoration: none;");
		out.println("	color: #FFF;");
		out.println("}");
		out.println("a:active {");
		out.println("	text-decoration: none;");
		out.println("	color: #FFF;");
		out.println("}");
		out.println("-->");
		out.println("</style>");
		out.println("");
		out.println("");
		out.println("<link href=\"SpryAssets/SpryValidationCheckbox.css\" rel=\"stylesheet\" type=\"text/css\" />");
		out.println("<link href=\"SpryAssets/SpryValidationTextField.css\" rel=\"stylesheet\" type=\"text/css\" />");
		out.println("<script type=\"text/javascript\">");
		out.println("<!--");
		out.println("function MM_jumpMenuGo(objId,targ,restore){ //v9.0");
		out.println("  var selObj = null;  with (document) { ");
		out.println("  if (getElementById) selObj = getElementById(objId);");
		out.println("  if (selObj) eval(targ+\".location='\"+selObj.options[selObj.selectedIndex].value+\"'\");");
		out.println("  if (restore) selObj.selectedIndex=0; }");
		out.println("}");
		out.println("//-->");
		out.println("</script>");
		out.println("</head>");
		out.println("<body text=\"#FFFFFF\" class=\"twoColFixLtHdr\">");
		out.println("");
		out.println("<div id=\"container\">");
		out.println("  <div id=\"header\">");
		out.println("    <h1 style=\"display:none\">PennSocial</h1>");
		out.println("    <div style=\"padding-top:80px;background-color:none\" align=\"left\">");
		out.println("      <ul id=\"MenuBar\" class=\"MenuBarHorizontal\">");
		out.println("        <li><a href=\"javascript: void(0);\" onclick=\"document.HomeMenuItem.submit();return false;\">Home</a> </li>");
		out.println("<form name=\"HomeMenuItem\" action=\"Profile\" method=\"POST\">");
     	out.println("<input type=\"hidden\" name=\"userID\" value=\""+String.valueOf(userID)+"\">");
 		out.println("<input type=\"hidden\" name=\"queryType\" value=\"home\"></form>");
		out.println("        <li><a href=\"javascript: void(0);\" onclick=\"document.ProfileMenuItem.submit();return false;\">Profile</a> </li>");
		out.println("<form name=\"ProfileMenuItem\" action=\"Profile\" method=\"POST\">");
     	out.println("<input type=\"hidden\" name=\"userID\" value=\""+String.valueOf(userID)+"\">");
 		out.println("<input type=\"hidden\" name=\"queryType\" value=\"user\"></form>");
		out.println("        <li><a href=\"javascript: void(0);\" onclick=\"document.SettingsMenuItem.submit();return false;\">Settings</a> </li>");
		out.println("<form name=\"SettingsMenuItem\" action=\"Settings\" method=\"POST\">");
     	out.println("<input type=\"hidden\" name=\"userID\" value=\""+String.valueOf(userID)+"\">");
 		out.println("<input type=\"hidden\" name=\"queryType\" value=\"settings\"></form>");
		out.println("        <li><a href=\"javascript: void(0);\" onclick=\"document.InboxMenuItem.submit();return false;\">Inbox</a></li>");
		out.println("<form name=\"InboxMenuItem\" action=\"Inbox\" method=\"POST\">");
		out.println("<input type=\"hidden\" name=\"userID\" value=\""+String.valueOf(userID)+"\">");
		out.println("<input type=\"hidden\" name=\"queryType\" value=\"inbox\">");
		out.println("<input type=\"hidden\" name=\"sort\" value=\"mdate\">");
		out.println("<input type=\"hidden\" name=\"asc\" value=\"DESC\"></form>");
		out.println("        <li><a href=\"index.jsp\">Logout</a></li>");
		out.println("      </ul>");
		out.println("    </div>");
		out.println("  </div>");

	}
	
	protected void printSidebar(Connection conn, Query q, PrintWriter out, int userID) throws ServletException, IOException, ClassNotFoundException, SQLException{
		out.println("  <div id=\"sidebar1\">");
		q.printGeneral(conn,out, userID, userID);
		out.println("<br/><br/>");
		q.printFriends(conn, out, userID, userID);
		q.printRequested(conn, out, userID);
		out.println("<br/>");
        q.printFriendSearch(out, userID);
        q.printFriendRecommendations(conn, out, userID);		
		out.println("  <!-- end #sidebar1 --></div>");

	}
	
	protected void printMain(Connection conn, Query q, PrintWriter out, int userID, boolean editable) throws ServletException, IOException, ClassNotFoundException, SQLException{
        out.println("  <div id=\"mainContent\">");
        q.printNameAndStatus(conn,out, userID, userID, editable);
        q.printInterests(conn, out, userID, false);
        q.printUserEducation(conn, out, userID, false);
        q.printUserEmployment(conn, out, userID, false);
        q.printStatusandWall(conn, out, userID);
      
        //print button for inbox
        out.println("<center><form name=\"Inbox\" action=\"Inbox\" method=\"POST\">");
		out.println("<input type=\"hidden\" name=\"userID\" value=\""+String.valueOf(userID)+"\">");
		out.println("<input type=\"hidden\" name=\"queryType\" value=\"inbox\">");
		out.println("<input type=\"hidden\" name=\"sort\" value=\"mdate\">");
		out.println("<input type=\"hidden\" name=\"asc\" value=\"DESC\">");
    	out.println("<input type=\"submit\" value=\"Go To Inbox\"></form></center>");
    	out.println("</div>");
    	
    	//footer
    	out.println("	<!-- This clearing element should immediately follow the #mainContent div in order to force the #container div to contain all child floats -->");
    	out.println("  <div id=\"footer\">");
    	out.println("    <p>&copy; 2009 Copyrights Reserved</p>");
    	out.println("  <!-- end #footer --></div>");
    	out.println("<!-- end #container --></div>");

	}
	
	
	protected void printHomeMain(Connection conn, Query q, PrintWriter out, int userID, boolean editable) throws ServletException, IOException, ClassNotFoundException, SQLException{
        out.println("  <div id=\"mainContent\">");
        q.printNameAndStatus(conn,out, userID, userID, editable);
        q.printUserUpdate(conn, out, userID);
      
        //print button for inbox
        out.println("<center><form name=\"Inbox\" action=\"Inbox\" method=\"POST\">");
		out.println("<input type=\"hidden\" name=\"userID\" value=\""+String.valueOf(userID)+"\">");
		out.println("<input type=\"hidden\" name=\"queryType\" value=\"inbox\">");
		out.println("<input type=\"hidden\" name=\"sort\" value=\"mdate\">");
		out.println("<input type=\"hidden\" name=\"asc\" value=\"DESC\">");
    	out.println("<input type=\"submit\" value=\"Go To Inbox\"></form></center>");
    	out.println("</div>");
    	
    	//footer
    	out.println("	<!-- This clearing element should immediately follow the #mainContent div in order to force the #container div to contain all child floats -->");
    	out.println("  <div id=\"footer\">");
    	out.println("    <p>&copy; 2009 Copyrights Reserved</p>");
    	out.println("  <!-- end #footer --></div>");
    	out.println("<!-- end #container --></div>");

	}

	//username and password to check against UserPasswords relation
	protected int tryLogin(Connection conn, String username, String password) throws SQLException, IOException, ClassNotFoundException{
		Statement stmt = conn.createStatement();
		String query = "SELECT userID FROM UserAccounts WHERE email=\'"+username+"\'";
		ResultSet rs = stmt.executeQuery(query);
		rs.next();
		int userID = -1;
		try{userID = rs.getInt("userID");}
		catch(Exception e){return -1;}
		rs.close();
		stmt.close();
		query = "SELECT password FROM UserPasswords WHERE userID="+String.valueOf(userID);
		stmt = conn.createStatement();
		rs = stmt.executeQuery(query);
		rs.next();
		String pass=rs.getString("password");
		getDisconnected(null,stmt,rs);
		if(password.equals(pass)){
			return userID;
		}
		else 
			return -2;
	}
	
	
}
