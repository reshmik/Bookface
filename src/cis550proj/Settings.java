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
 * Servlet implementation class Friend
 */
public class Settings extends HttpServlet implements Servlet {
	private static final long serialVersionUID = 1L;
		/**
	     * @see HttpServlet#HttpServlet()
	     */
	     public Settings() {
	    	 super();
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
        int userID = Integer.parseInt(request.getParameter("userID").toString());

        if(queryType.equals("settings")){

    	        Search.printHeader(out, "Bookface | Settings");
    	        out.println("<br/><h2>Settings</h2>");
    	        
    	        out.println("   <a href=\"javascript: void(0);\" onclick=\"document.Education.submit();return false;\">Edit Education</a></li>");
				out.println("<form name=\"Education\" action=\"Create\" method=\"POST\">");
				out.println("<input type=\"hidden\" name=\"userID\" value=\""+String.valueOf(userID)+"\">");
				out.println("<input type=\"hidden\" name=\"queryType\" value=\"editEducation\"></form>");

				out.println("   <a href=\"javascript: void(0);\" onclick=\"document.Interests.submit();return false;\">Edit Interests</a></li>");
				out.println("<form name=\"Interests\" action=\"Create\" method=\"POST\">");
				out.println("<input type=\"hidden\" name=\"userID\" value=\""+String.valueOf(userID)+"\">");
				out.println("<input type=\"hidden\" name=\"queryType\" value=\"editInterests\"></form>");
				
				out.println("   <a href=\"javascript: void(0);\" onclick=\"document.Employment.submit();return false;\">Edit Eployment</a></li>");
				out.println("<form name=\"Employment\" action=\"Create\" method=\"POST\">");
				out.println("<input type=\"hidden\" name=\"userID\" value=\""+String.valueOf(userID)+"\">");
				out.println("<input type=\"hidden\" name=\"queryType\" value=\"editEmployment\"></form>");
				
				out.println("   <a href=\"javascript: void(0);\" onclick=\"document.Bio.submit();return false;\">Edit Bio</a></li>");
				out.println("<form name=\"Bio\" action=\"Create\" method=\"POST\">");
				out.println("<input type=\"hidden\" name=\"userID\" value=\""+String.valueOf(userID)+"\">");
				out.println("<input type=\"hidden\" name=\"queryType\" value=\"editBio\"></form>");

				out.println("   <a href=\"javascript: void(0);\" onclick=\"document.Relationship.submit();return false;\">Edit Relationship Status</a></li>");
				out.println("<form name=\"Relationship\" action=\"Search\" method=\"POST\">");
				out.println("<input type=\"hidden\" name=\"userID\" value=\""+String.valueOf(userID)+"\">");
				out.println("<input type=\"hidden\" name=\"search\" value=\"\">");
				out.println("<input type=\"hidden\" name=\"queryType\" value=\"searchRel\"></form>");
				
				out.println("   <a href=\"javascript: void(0);\" onclick=\"document.Privacy.submit();return false;\">Edit privacy settings for friends</a></li>");
				out.println("<form name=\"Privacy\" action=\"Search\" method=\"POST\">");
				out.println("<input type=\"hidden\" name=\"userID\" value=\""+String.valueOf(userID)+"\">");
				out.println("<input type=\"hidden\" name=\"search\" value=\"\">");
				out.println("<input type=\"hidden\" name=\"queryType\" value=\"searchPrivacy\"></form>");
				
				out.println("   <a href=\"javascript: void(0);\" onclick=\"document.Password.submit();return false;\">Change Password</a></li>");
				out.println("<form name=\"Password\" action=\"Create\" method=\"POST\">");
				out.println("<input type=\"hidden\" name=\"userID\" value=\""+String.valueOf(userID)+"\">");
				out.println("<input type=\"hidden\" name=\"queryType\" value=\"changePassword\"></form>");
				
				out.println("<br/>");
				
				Search.printFooter(out);
        }
        else{
	        Profile.printHeader(out, "Error!", userID);
	        out.println("<h1>Error!</h1>");
	        out.println("<p>Please Contact Webmaster.</p>");
	        Search.printFooter(out);
        }
        out.println("</html>");
        out.close();	
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
	 
}
