package controllers;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;

import beans.Verbale;
import dao.VerbaleDAO;
import utils.DBTemplateHttpServlet;
import utils.PathUtils;

/**
 * Display the courses
 */
@WebServlet("/GoToVerbale")
public class GoToVerbale extends DBTemplateHttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GoToVerbale() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * Uses the template engine to render the corso.html page
	 * Handle warning message parameters.
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		String parameter = StringEscapeUtils.escapeJava(request.getParameter("idVerbale"));
		if ( parameter == null ) {
			renderError(request,response,"idVerbale can't be null");
			return;
		}
		int idVerbale;
		try {
			idVerbale = Integer.parseInt(parameter);
		}catch (NumberFormatException e) {
			renderError(request,response,"idVerbale must be a number");
			return;
		}
		
		int maxIdVerbale;
        VerbaleDAO verbaleDAO = new VerbaleDAO(connection);
        try {
			maxIdVerbale = verbaleDAO.getMaxId();
		} catch (SQLException e) {
			renderError(request, response, e.getMessage());
			return;
		}
    	if ( idVerbale > maxIdVerbale ) {
			renderError(request,response,"idVerbale does not exist");
			return;
		}
        
    	Verbale verbale;
		try {
			verbale = verbaleDAO.getVerbale(idVerbale);
		} catch (SQLException e) {
			renderError(request, response, e.getMessage());
			return;
		}
		
		request.setAttribute("verbale", verbale);		
		
		renderPage(request, response, PathUtils.pathToVerbalePage);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}