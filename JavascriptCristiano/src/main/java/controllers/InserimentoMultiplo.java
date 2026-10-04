package controllers;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringEscapeUtils;

import beans.User;
import utils.DBHttpServlet;
import dao.AppelliDAO;
import dao.CoursesDAO;
import dao.IscrittiDAO;

/**
 * Login the user to the application
 * Login responds to post requests on the login.html page
 */
@WebServlet("/InserimentoMultiplo")
@MultipartConfig
public class InserimentoMultiplo extends DBHttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public InserimentoMultiplo() {
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
		
		String voto = StringEscapeUtils.escapeJava(request.getParameter("voto"));
		List<String> voti = List.of("Assente","Rimandato", "Riprovato", "18" , "19" , "20" , "21" , "22", "23", "24", "25", "26", "27", "28", "29", "30", "30eLode");
		
		// Error on empty fields
		if(!voti.contains(voto)) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("this vote does not exist");
			return;
		}
		
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
			response.getWriter().println("this course does not exist");
			return;
			}
		else if(idDocente != user.getIdDocente()) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("this course is not yours");
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
		
		String[] arrayParametri = request.getParameterValues("checkBox");
		if ( arrayParametri == null ) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("checkbox can't be null");
			return;
		}
		List<Integer> matricole = new ArrayList<>();
		IscrittiDAO iscrittiDAO = new IscrittiDAO(connection);
		for(int i=0; i< arrayParametri.length ;i++) {
			String mat = StringEscapeUtils.escapeJava(arrayParametri[i]);
			if ( mat == null ) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().println("matricola can't be null");
				return;
			}
			int matricola;
			try {
				matricola = Integer.parseInt(mat);
			}catch (NumberFormatException e) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().println("matricola must be a number");
				return;
			}
			boolean isChangeable;
			try {
				isChangeable = iscrittiDAO.checkIscrittoVoteIsChangeable(idCorso,data,matricola);
			} catch (SQLException e) {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				response.getWriter().println("server error");
				return;
			}
			if ( isChangeable == false ) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().println("this vote does not exist or is not changeable");
				return;
			}
			matricole.add(matricola);
		}
        
		try {
			iscrittiDAO.inserimentoMultiploVoto(voto, idCorso, data, matricole);
		} catch (SQLException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println("server error");
			return;
		}
		
		response.setStatus(HttpServletResponse.SC_OK);		
	}
}