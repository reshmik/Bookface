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
public class Friend extends HttpServlet implements Servlet {
	private static final long serialVersionUID = 1L;
	 private Query q;
		/**
	     * @see HttpServlet#HttpServlet()
	     */
	     public Friend() {
	    	 super();
	    	 q=new Query(); 
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

        if(queryType.equals("friend")){
        	int fid = Integer.parseInt(request.getParameter("friendUserID").toString());
        	try{
        		Connection conn = getConnected();
            	String firstname = q.queryUserAccounts(conn, "firstname", fid);
    			String lastname = q.queryUserAccounts(conn, "lastname", fid);

    	        Profile.printHeader(out, "Bookface | "+firstname+" "+lastname, userID);
    	        
		        printSidebar(conn, q, out, fid, userID);
		        
		        printMain(conn, q, out, fid, userID);
		        getDisconnected(conn,null,null);
		        conn.commit();
        	}catch (SQLException e) {
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
	 
	protected void printSidebar(Connection conn, Query q, PrintWriter out, int fid, int userID) throws ServletException, IOException, ClassNotFoundException, SQLException{		
		out.println("  <div id=\"sidebar1\">");
		q.printGeneral(conn, out, fid, userID);
		q.printFriends(conn, out, fid, userID);
		out.println("  <!-- end #sidebar1 --></div>");

	}
	
	protected void printMain(Connection conn, Query q, PrintWriter out, int fid, int userID) throws ServletException, IOException, ClassNotFoundException, SQLException{
        out.println("  <div style=\"margin-top:5px;\" id=\"mainContent\">");
        
        int gID = q.getFriendGID(conn, fid, userID);
        
        q.printNameAndStatus(conn,out, fid, fid, false);
        
        if (fid != userID){
        	String name = q.queryUserAccounts(conn, "firstname", fid) + " " + q.queryUserAccounts(conn, "lastname", fid);
			
        	
        	
	        	out.println("<br/><div style=\"margin: 0 0 0 155px; width:300px;text-align:center;\">");
	        	out.println("<form style=\"float:left;margin-right:20px;\" action=\"Inbox\" method=\"POST\">");
		    	out.println("<input type=\"hidden\" name=\"userID\" value=\""+String.valueOf(userID)+"\">");
		    	out.println("<input type=\"hidden\" name=\"friendID\" value=\""+String.valueOf(fid)+"\">");
		    	out.println("<input type=\"hidden\" name=\"name\" value=\""+name+"\">");
				out.println("<input type=\"hidden\" name=\"queryType\" value=\"compose\">");
		        out.println("<input type=\"submit\" value=\"Send Message\"></form>");
		        
		    if( gID == 2 || gID == 4 || gID == 3){
		        out.println("<form style=\"float:left;\" action=\"Inbox\" method=\"POST\">");
		    	out.println("<input type=\"hidden\" name=\"userID\" value=\""+String.valueOf(userID)+"\">");
		    	out.println("<input type=\"hidden\" name=\"friendID\" value=\""+String.valueOf(fid)+"\">");
		    	out.println("<input type=\"hidden\" name=\"name\" value=\""+name+"\">");
				out.println("<input type=\"hidden\" name=\"queryType\" value=\"composeWall\">");
		        out.println("<input type=\"submit\" value=\"Write on "+ q.queryUserAccounts(conn, "firstname", fid) + "'s wall\"></form>");
		        
        	}
		    
		    out.println("</div>");
	        out.println("<br/>");
        }
        
		if (gID == 1 || gID == 3 || gID == 4) {
			q.printInterests(conn, out, fid, false);
		}
		
		if (gID == 3) {
			q.printUserEducation(conn, out, fid, false);
			q.printUserEmployment(conn, out, fid, false);
		}
		
		if (gID == 2 || gID == 3 || gID == 4) q.printStatusandWall(conn, out, fid);
		
		
		out.println("<br><center><form action=\"Profile\" method=\"POST\">");
    	out.println("<input type=\"hidden\" name=\"userID\" value=\""+String.valueOf(userID)+"\">");
		out.println("<input type=\"hidden\" name=\"queryType\" value=\"user\">");
        out.println("<input type=\"submit\" value=\"Return to your profile\"></form></center>");
        out.println("<br/>");
        out.println("</div>");
        
    	//footer
    	out.println("	<!-- This clearing element should immediately follow the #mainContent div in order to force the #container div to contain all child floats -->");
    	out.println("  <div id=\"footer\">");
    	out.println("    <p>&copy; 2009 Copyrights Reserved</p>");
    	out.println("  <!-- end #footer --></div>");
    	out.println("<!-- end #container --></div>");

	}
	 
}
