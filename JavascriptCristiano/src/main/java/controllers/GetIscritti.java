package controllers;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringEscapeUtils;

import com.google.gson.Gson;

import beans.Iscritto;
import beans.User;
import utils.DBHttpServlet;
import dao.AppelliDAO;
import dao.CoursesDAO;
import dao.IscrittiDAO;

/**
 * Display the courses
 */
@WebServlet("/GetIscritti")
@MultipartConfig
public class GetIscritti extends DBHttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetIscritti() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * Uses the template engine to render the corso.html page
	 * Handle warning message parameters.
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		HttpSession s = request.getSession(false);
		User user = (User)s.getAttribute("user");
		
		String parameter = StringEscapeUtils.escapeJava(request.getParameter("idCorso"));
		if ( parameter == null ) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("id can't be null");
			return;
		}
		int idCorso;
		try {
			idCorso = Integer.parseInt(parameter);
		}catch (NumberFormatException e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("id must be a number");
			return;
		}
		CoursesDAO coursesDAO = new CoursesDAO(connection);
		int idDocente;
		try {
			idDocente = coursesDAO.getDocenteCorso(idCorso);
			} catch (SQLException e) {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				response.getWriter().println("server error");
				return;
			}
		if(idDocente == -1){
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("This course does not exist");
			return;
			}
		else if(idDocente != user.getIdDocente()) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("This course is not yours");
			return;
		}
		
		
		String parameter2 = StringEscapeUtils.escapeJava(request.getParameter("data"));
		Date data;
		if ( parameter2 == null ) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("data can't be null");
			return;
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try{
        	data = new Date(sdf.parse(parameter2).getTime());
        }catch(ParseException e) {
        	response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("data is not valid");
			return;
        }
        AppelliDAO appelliDAO = new AppelliDAO(connection);
		List<Date> dateEsistenti;
		try {
			dateEsistenti = appelliDAO.getDateAppelli(idCorso);
			} catch (SQLException e) {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				response.getWriter().println("server error");
				return;
			}
		if ( !dateEsistenti.contains(data)) {
		  	response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("there are no appeals on this date");
			return;
		}
		
          
		
		IscrittiDAO iscrittiDAO = new IscrittiDAO(connection);
		List<Iscritto> iscritti;
		try {
			iscritti = iscrittiDAO.getIscritti(idCorso,data);
		} catch (SQLException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println("server error");
			return;
		}
		
		String iscrittiAppello = new Gson().toJson(iscritti);
		response.setStatus(HttpServletResponse.SC_OK);	
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().println(iscrittiAppello);
		
	}

}