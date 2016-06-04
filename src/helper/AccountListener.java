package helper;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Application Lifecycle Listener implementation class AccountListener
 *
 */
@WebListener
public class AccountListener implements ServletContextListener {

    /**
     * Default constructor. 
     */
    public AccountListener() {
    	
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0)  {
    	ServletContext ctx = arg0.getServletContext();
    	AccountManager manager = (AccountManager) ctx.getAttribute("Manager");
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0)  {
    	ServletContext ctx = arg0.getServletContext();
    	AccountManager manager = new AccountManager();
    	ctx.setAttribute("Manager", manager); // Sets AccountManager to the ServletContext.
    }
	
}
