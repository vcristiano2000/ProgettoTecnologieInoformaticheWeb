package controllers;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringEscapeUtils;

import com.google.gson.Gson;

import beans.User;
import utils.DBHttpServlet;
import dao.UserDAO;

/**
 * Login the user to the application
 * Login responds to post requests on the login.html page
 */
@WebServlet("/Login")
@MultipartConfig
public class Login  extends DBHttpServlet {
	
	private static final long serialVersionUID = 1L;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * Check the received credentials. If login is succesfull, forward to GoToHome servlet
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String password = StringEscapeUtils.escapeJava(request.getParameter("password"));
		String parameter = StringEscapeUtils.escapeJava(request.getParameter("id"));
		if ( parameter == null ) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("id can't be null");
			return;
		}
		int id;
		try {
			id = Integer.parseInt(parameter);
		}catch (NumberFormatException e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("id must be a number");
			return;
		}
		
		
		// Error on empty fields
		if(password == null) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);		
			response.getWriter().println("Login fields empty!");
			return;
		}
		
			
		// Attempt login
		UserDAO userDAO = new UserDAO(connection);
		User user = null;
		try {
			user = userDAO.checkCredentials(id, password);
		} catch (SQLException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println("server error");
			return;
		}
		
		// Warning if credentials do not match any existing user
		if(user == null) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("user e password non corrispondono");
			return;
		}
		
		HttpSession session = request.getSession();
		session.setAttribute("user", user);
			
	
		String utente = new Gson().toJson(user);
		response.setStatus(HttpServletResponse.SC_OK);	
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().println(utente);
		}
}