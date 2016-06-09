package helper;

import javax.servlet.ServletContext;
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
	public static final String ACCOUNT_MANEGER_ATTRIBUTE_NAME = "ContextStartupListener.AccountManagerAttribute";

	/** name which will be attribute name for ExamManager in Context */
	public static final String EXAM_MANEGER_ATTRIBUTE_NAME = "ContextStartupListener.ExamManagerAttribute";

	/**
	 * Default constructor.
	 */
	public ContextStartupListener() {
	}

	/**
	 * @see ServletContextListener#contextDestroyed(ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent arg0) {
	}

	/**
	 * @see ServletContextListener#contextInitialized(ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		initLogginSystem(servletContextEvent);
		initAccountManager(servletContextEvent);
		initExamManager(servletContextEvent);
	}

	/* initializes system for log messages */
	private void initLogginSystem(ServletContextEvent servletContextEvent) {
		LogManager.setServletContext(servletContextEvent.getServletContext());
	}

	// Sets AccountManager to the ServletContext.
	private void initAccountManager(ServletContextEvent servletContextEvent) {
		ServletContext context = servletContextEvent.getServletContext();
		AccountManager manager = new AccountManager();
		context.setAttribute(ACCOUNT_MANEGER_ATTRIBUTE_NAME, manager);
	}

	/* initializes exam manager */
	private void initExamManager(ServletContextEvent servletContextEvent) {
		ServletContext context = servletContextEvent.getServletContext();
		ExamManager examManager = new ExamManager();
		context.setAttribute(EXAM_MANEGER_ATTRIBUTE_NAME, examManager);
	}

}
