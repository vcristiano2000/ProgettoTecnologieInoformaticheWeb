package filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import beans.User;
import utils.PathUtils;

/**
 * Filter requests that require the user to be logged in
 */
@WebFilter(urlPatterns = {"/GoToVoto", "/RifiutaVoto"})
public class CheckStudent extends HttpFilter implements Filter {
       
    private static final long serialVersionUID = 1L;
    
	/**
     * @see HttpFilter#HttpFilter()
     */
    public CheckStudent() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		HttpSession s = req.getSession(false);
		
		// Refuse any request where the session has no user attribute
		if(s != null) {
			User user = (User)s.getAttribute("user");
			if(!user.isProfessore()) {
				chain.doFilter(request, response);
				return;
			}
		} 
		
		res.sendRedirect(request.getServletContext().getContextPath() + PathUtils.pathToCorsiServlet);
	}
	
}