package controllers;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringEscapeUtils;

import beans.Iscritto;
import beans.User;
import utils.DBTemplateHttpServlet;
import utils.PathUtils;
import dao.AppelliDAO;
import dao.CoursesDAO;
import dao.IscrittiDAO;
import dao.IscrittiDAO.Ordering;

/**
 * Display the courses
 */
@WebServlet("/GoToIscritti")
public class GoToIscritti extends DBTemplateHttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GoToIscritti() {
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
		else if(idDocente != user.getIdDocente()) {
			renderError(request,response,"This course is not yours");
			return;
		}
		
		
		String parameter2 = StringEscapeUtils.escapeJava(request.getParameter("data"));
		Date data;
		if ( parameter2 == null ) {
			renderError(request,response,"data can't be null");
			return;
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try{
        	data = new Date(sdf.parse(parameter2).getTime());
        }catch(ParseException e) {
        	renderError(request,response,"date is not valid");
			return;
        }
        AppelliDAO appelliDAO = new AppelliDAO(connection);
		List<Date> dateEsistenti;
		try {
			dateEsistenti = appelliDAO.getDateAppelli(idCorso);
			} catch (SQLException e) {
				renderError(request, response, e.getMessage());
				return;
			}
		if ( !dateEsistenti.contains(data)) {
			renderError(request,response,"there are no appeals on this date");
			return;
		}
		
                
        String parameter3 = StringEscapeUtils.escapeJava(request.getParameter("ord"));
        if ( parameter3 == null ) {
			parameter3="MATRICOLA_ASC";
		}
        Ordering ord = Ordering.getFromString(parameter3); 
        if ( ord == null ) {
			renderError(request,response,"this oredering does not exist");
			return;
		}
		
		IscrittiDAO iscrittiDAO = new IscrittiDAO(connection);
		List<Iscritto> iscritti;
		try {
			iscritti = iscrittiDAO.getIscritti(idCorso,data,ord);
		} catch (SQLException e) {
			renderError(request, response, e.getMessage());
			return;
		}
		
		boolean verbalizza = iscritti.stream().anyMatch(iscritto -> 
		iscritto.getStatoDellaValutazione().equals("pubblicato")
		|| iscritto.getStatoDellaValutazione().equals("rifiutato"));
		
		boolean pubblica = iscritti.stream().anyMatch(iscritto -> 
		iscritto.getStatoDellaValutazione().equals("inserito"));
		
		request.setAttribute("pubblica", pubblica);
		request.setAttribute("verbalizza", verbalizza);
		request.setAttribute("ord", parameter3);
		request.setAttribute("idCorso", idCorso);
		request.setAttribute("iscritti", iscritti);
		request.setAttribute("dataAppello", data);
		request.setAttribute("nomeCorso", nomeCorso);
		
		renderPage(request, response, PathUtils.pathToIscrittiPage);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}