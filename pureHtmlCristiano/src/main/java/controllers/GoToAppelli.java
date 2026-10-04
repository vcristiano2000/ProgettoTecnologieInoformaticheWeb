package controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringEscapeUtils;

import beans.Appello;
import beans.User;
import utils.DBTemplateHttpServlet;
import utils.PathUtils;
import dao.AppelliDAO;
import dao.CoursesDAO;
import dao.IscrittiDAO;

/**
 * Display the courses
 */
@WebServlet("/GoToAppelli")
public class GoToAppelli extends DBTemplateHttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GoToAppelli() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * Uses the template engine to render the corso.html page
	 * Handle warning message parameters.
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession s = request.getSession(false);
		User user = (User)s.getAttribute("user");
		
		String parameter = StringEscapeUtils.escapeJava(request.getParameter("idCorso"));
		if ( parameter == null ) {
			renderError(request,response,"id can't be null");
			return;
		}
		int idCorso;
		try {
			idCorso = Integer.parseInt(parameter);
		}catch (NumberFormatException e) {
			renderError(request,response,"id must be a number");
			return;
		}
		CoursesDAO coursesDAO = new CoursesDAO(connection);
		String nomeCorso;
		try {
		nomeCorso = coursesDAO.getNomeCorso(idCorso);
		} catch (SQLException e) {
			renderError(request, response, e.getMessage());
			return;
		}
		int idDocente;
		try {
			idDocente = coursesDAO.getDocenteCorso(idCorso);
			} catch (SQLException e) {
				renderError(request, response, e.getMessage());
				return;
			}
		if(idDocente == -1){
			renderError(request, response, "This course does not exist");
			return;
			}
		else if(idDocente != user.getIdDocente() && user.isProfessore()==true) {
			renderError(request,response,"This course is not yours");
			return;
		}
		
		IscrittiDAO iscrittiDAO = new IscrittiDAO(connection);
		List<Integer> idCorsi;
		try {
			idCorsi = iscrittiDAO.getStudentIdCourses(user.getMatricola());
			} catch (SQLException e) {
				renderError(request, response, e.getMessage());
				return;
			}
		if(!idCorsi.contains(idCorso) && user.isProfessore()!=true) {
			renderError(request,response,"This course is not yours");
			return;
		}
	
		
		
		AppelliDAO appelliDAO = new AppelliDAO(connection);
		List<Appello> appelli;
		try {
			if(user.isProfessore()) {
				appelli = appelliDAO.getProfAppelli(idCorso);
			}
			else {
				appelli = appelliDAO.getStudAppelli(idCorso,user.getMatricola());
			}
		} catch (SQLException e) {
			renderError(request, response, e.getMessage());
			return;
		}
		
		
		
		request.setAttribute("appelli", appelli);
		request.setAttribute("nomeCorso", nomeCorso);
		
		renderPage(request, response, PathUtils.pathToAppelliPage);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}