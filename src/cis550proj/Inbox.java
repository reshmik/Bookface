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
 * Servlet implementation class Inbox
 */
public class Inbox extends HttpServlet implements Servlet {
	private static final long serialVersionUID = 1L;
	private Query q;
	private Search s;
	private Create c;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Inbox() {
        super();
        q = new Query();
        s = new Search();
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
        if(queryType.equals("inbox")||queryType.equals("keywordSearch")||queryType.equals("nameSearch")){		
        	try {
        			
        			userID=Integer.parseInt(request.getParameter("userID").toString());
        			String sort = request.getParameter("sort").toString();
        			String asc = request.getParameter("asc").toString();
					String firstname = q.queryUserAccounts(conn, "firstname", userID);
	    			String lastname = q.queryUserAccounts(conn, "lastname", userID);
	    			
	    			Search.printHeader(out, "Bookface | Inbox : "+firstname+" "+lastname);
	    			out.println("<br/><br/>");
	    			
	    			out.println("<center><form action=\"Search\" method=\"POST\">");
	            	out.println("<input type=\"hidden\" name=\"userID\" value=\""+String.valueOf(userID)+"\">");
	        		out.println("<input type=\"hidden\" name=\"queryType\" value=\"compose\">");
	        		out.println("<input type=\"hidden\" name=\"search\" value=\"\">");
	                out.println("<input type=\"submit\" value=\"Compose Message\"></form></center>");
	    			out.println("<br/>");
	    			
	    			
			        printSearchBox(out, userID, sort, asc);
			        if(queryType.equals("inbox")){
			        	
			        	printInboxSort(out, userID, "mdate", "DESC");
			        	q.printPrivateMessages(conn, out, userID, sort, asc);
			        }
			        else if(queryType.equals("keywordSearch")||queryType.equals("nameSearch")){
			        	String searchString = request.getParameter("search");
			        	if(queryType.equals("keywordSearch"))
			        		s.searchMessagesByKeyword(conn, out, userID, searchString, sort, asc);
			        	else
			        		s.searchMessagesBySender(conn, out, userID, searchString, sort, asc);
			        	out.println("<center><form action=\"Inbox\" method=\"POST\">");
						out.println("<input type=\"hidden\" name=\"userID\" value=\""+String.valueOf(userID)+"\">");
						out.println("<input type=\"hidden\" name=\"queryType\" value=\"inbox\">");
						out.println("<input type=\"hidden\" name=\"sort\" value=\"mdate\">");
						out.println("<input type=\"hidden\" name=\"asc\" value=\"DESC\">");
				    	out.println("<input type=\"submit\" value=\"Return to Inbox\"></form></center><br>");
			        }
			        out.println("<center><form action=\"Profile\" method=\"POST\">");
	            	out.println("<input type=\"hidden\" name=\"userID\" value=\""+String.valueOf(userID)+"\">");
	        		out.println("<input type=\"hidden\" name=\"queryType\" value=\"user\">");
	                out.println("<input type=\"submit\" value=\"Return to your profile\"></form></center>");
	                
					Search.printFooter(out);
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
        }
        else if(queryType.equals("compose") || queryType.equals("composeWall")){
			userID=Integer.parseInt(request.getParameter("userID").toString());
			int friendID=Integer.parseInt(request.getParameter("friendID").toString());
			String name = request.getParameter("name").toString();

        	Search.printHeader(out, "Message to "+ name);
        	
        	out.println("<br/>");
			if (queryType.equals("compose")) out.println("<h2>Send a message to " + name + "</h2>");
			else out.println("<h2>Post to " + name + "'s wall</h2>");
        	out.println("<br/>");
        	out.println("<form action=\"Inbox\" method=\"post\">");
        	if (queryType.equals("compose")) out.println("		<br/>Subject:<input type=\"text\" name=\"subject\" size=\"20\"/><br/>");
        	else out.println("<input type=\"hidden\" name=\"subject\" value=\"N/A\">");
        	out.println("		<br/>Compose:<br/><textarea name=\"message\" style=\"width:400px; height:200px;\"></textarea>");
        	out.println("       <input type=\"hidden\" name=\"name\" value=\"" + name + "\" />");
        	out.println("        <input type=\"hidden\" name=\"userID\" value=\"" + String.valueOf(userID) + "\" />");
        	out.println("        <input type=\"hidden\" name=\"friendID\" value=\"" + String.valueOf(friendID) + "\" />");
        	if (queryType.equals("compose")) {
        		out.println("        <input type=\"hidden\" name=\"queryType\" value=\"sendMessage\" />");
        		out.println("       <br/> <br/> <input type=\"submit\" value=\"Send Message\" />");
        	}
        	else {
        		out.println("        <input type=\"hidden\" name=\"queryType\" value=\"sendWallMessage\" />");
        		out.println("       <br/> <br/> <input type=\"submit\" value=\"Post\" />");
        	}
        	
        	out.println("	</form>");
        	out.println("<br/>");
	        out.println("<center><form action=\"Profile\" method=\"POST\">");
        	out.println("<input type=\"hidden\" name=\"userID\" value=\""+String.valueOf(userID)+"\">");
    		out.println("<input type=\"hidden\" name=\"queryType\" value=\"user\">");
            out.println("<input type=\"submit\" value=\"Return to your profile (and Cancel)\"></form></center>");
            out.println("<br/>");
            
        	
        	Search.printFooter(out);
        }
        else if(queryType.equals("sendMessage") || queryType.equals("sendWallMessage")){
			userID=Integer.parseInt(request.getParameter("userID").toString());
			int friendID=Integer.parseInt(request.getParameter("friendID").toString());
			String name = request.getParameter("name").toString();
			String message = request.getParameter("message").toString();
			String subject = request.getParameter("subject").toString();

        	Search.printHeader(out, "Message Sent");
        	
        	try {
    			String myName = q.queryUserAccounts(conn, "firstname", userID) + " " + q.queryUserAccounts(conn, "lastname", userID);

				if (queryType.equals("sendMessage")){
	        		c.sendMessage(conn, friendID, subject, message, userID, 1);
				}
				else{
					c.sendMessage(conn, friendID, subject, message, userID, 0);
					c.addUserUpdate(conn, userID, myName + " wrote on " + name + "''s wall");
				}
				
			} catch (SQLException e) {
		        out.println("<h1>Error!</h1>");
		        out.println("<p>Please Contact Webmaster.</p>");
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
		        out.println("<h1>Error!</h1>");
		        out.println("<p>Please Contact Webmaster.</p>");
				e.printStackTrace();
			}
        	
        	out.println("<br/>");
        	if (queryType.equals("sendMessage")) out.println("<h2>Message sent to " + name + "</h2>");
        	else out.println("<h2>Posted to " + name + "'s wall</h2>");
        	out.println("<br/>");
	        out.println("<center><form action=\"Profile\" method=\"POST\">");
        	out.println("<input type=\"hidden\" name=\"userID\" value=\""+String.valueOf(userID)+"\">");
    		out.println("<input type=\"hidden\" name=\"queryType\" value=\"user\">");
            out.println("<input type=\"submit\" value=\"Return to your profile\"></form></center>");
            out.println("<br/>");
        	
        	Search.printFooter(out);
        }
        else{
        	Search.printHeader(out, "Error!");
	        out.println("<h1>Error!</h1>");
	        out.println("<p>Please Contact Webmaster.</p>");
	        Search.printFooter(out);
        }
        out.close();
        try {
			q.getDisconnected(conn, null, null);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	protected void printInboxSort(PrintWriter out, int userID, String currentSort, String asc){
		//senderID, subject, content, mdate, firstname, lastname
			out.print("<center><table border=\"0\" cellspacing=\"20\"><tr><td>");
			printSortButtons(out, userID, "firstname", asc, "Sort by first name");
			out.print("</td><td>");
        	printSortButtons(out, userID, "lastname", asc, "Sort by last name");
        	out.print("</td><td>");
        	printSortButtons(out, userID, "mdate", asc, "Sort by date");
        	out.print("</td><td>");
        	printSortButtons(out, userID, "subject", asc, "Sort by subject");
        	out.print("</td><td>");
        	printSortButtons(out, userID, "content", asc, "Sort by content");
        	out.print("</td></tr></table></center>");
	}
	
	protected void printSortButtons(PrintWriter out, int userID, String sort, String asc, String desc){
		out.print("<center>"+desc+" ");
		out.print("<form action=\"Inbox\" method=\"POST\">");
		out.print("<input type=\"hidden\" name=\"userID\" value=\""+String.valueOf(userID)+"\">");
		out.print("<input type=\"hidden\" name=\"queryType\" value=\"inbox\">");
		out.print("<input type=\"hidden\" name=\"sort\" value=\""+sort+"\">");
		out.print("<input type=\"hidden\" name=\"asc\" value=\"ASC\">");
    	out.print("<input type=\"submit\" value=\"Ascending\"></form>");
    	out.print("<form action=\"Inbox\" method=\"POST\">");
		out.print("<input type=\"hidden\" name=\"userID\" value=\""+String.valueOf(userID)+"\">");
		out.print("<input type=\"hidden\" name=\"queryType\" value=\"inbox\">");
		out.print("<input type=\"hidden\" name=\"sort\" value=\""+sort+"\">");
		out.print("<input type=\"hidden\" name=\"asc\" value=\"DESC\">");
    	out.print("<input type=\"submit\" value=\"Descending\"></form>");
    	out.println("</center>");
	}
	
	protected void printSearchBox(PrintWriter out, int userID, String sort, String asc){
		out.println("<form action=\"Inbox\" method=\"POST\">");
		out.println("<input type=\"hidden\" name=\"userID\" value=\""+String.valueOf(userID)+"\">");
		out.println("<input type=\"hidden\" name=\"queryType\" value=\"keywordSearch\">");
		out.println("<input type=\"hidden\" name=\"sort\" value=\""+sort+"\">");
		out.println("<input type=\"hidden\" name=\"asc\" value=\""+asc+"\">");
		out.println("Search by keyword: <input type=\"text\" name=\"search\" size=\"20\">");
    	out.println("<input type=\"submit\" value=\"Go!\"></form>");
    	
    	out.println("<br/>");
    	
    	out.println("<form action=\"Inbox\" method=\"POST\">");
		out.println("<input type=\"hidden\" name=\"userID\" value=\""+String.valueOf(userID)+"\">");
		out.println("<input type=\"hidden\" name=\"queryType\" value=\"nameSearch\">");
		out.println("<input type=\"hidden\" name=\"sort\" value=\""+sort+"\">");
		out.println("<input type=\"hidden\" name=\"asc\" value=\""+asc+"\">");
		out.println("Search by name: <input type=\"text\" name=\"search\" size=\"20\">");
    	out.println("<input type=\"submit\" value=\"Go!\"></form>");
	}

}
