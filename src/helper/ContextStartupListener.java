package helper;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Application Lifecycle Listener implementation class ContextStartupListener
 * Creates main structures and DataSets for application when its started 
 */
@WebListener
public class ContextStartupListener implements ServletContextListener {
	
	/** name which will be attribute name for AccountManager in Context */
	public static final String ACCOUNT_MANEGER_ATTRIBUTE_NAME = "ContextStartupListener.DataManegerClassInstance";
	
	/** name which will be attribute name for ExamManager in Context */
	public static final String EXAM_MANEGER_ATTRIBUTE_NAME = "ContextStartupListener.DataManegerClassInstance";
	
	
	
    /**
     * Default constructor. 
     */
    public ContextStartupListener() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0)  { 
         // TODO Auto-generated method stub
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent servletContextEvent)  { 
    	initLogginSystem(servletContextEvent);
         initAccountManager();
         initExamManager();
    }
    
    /* initializes system for log messages */
    private void initLogginSystem(ServletContextEvent servletContextEvent){
    	LogManager.setServletContext(servletContextEvent.getServletContext());
    }
    
    
    private void initAccountManager(){
    	
    }
    
    private void initExamManager(){
    	
    }
	
}
