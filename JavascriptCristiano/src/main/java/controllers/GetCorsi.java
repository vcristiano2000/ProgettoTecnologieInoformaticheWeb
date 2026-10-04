package controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import beans.Corso;
import beans.User;
import utils.DBHttpServlet;
import dao.CoursesDAO;

/**
 * Display the courses
 */
@WebServlet("/GetCorsi")
@MultipartConfig
public class GetCorsi extends DBHttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetCorsi() {
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
		
		CoursesDAO coursesDAO = new CoursesDAO(connection);
		List<Corso> courses;
		try {
			if(user.isProfessore()) {
				courses = coursesDAO.getProfCourses(user.getIdDocente());
			}
			else {
				courses = coursesDAO.getStudCourses(user.getMatricola());
			}
		} catch (SQLException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println("server error");
			return;
		}
		

		String corsi = new Gson().toJson(courses);
		response.setStatus(HttpServletResponse.SC_OK);	
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().println(corsi);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}