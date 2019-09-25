package be.naturalsciences.bmdc.ears.ontology.rest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;




public class UserCredentials
  implements ServletContextListener
{
	
	
  private static Properties properties = new Properties();
  private static Logger logger = Logger.getLogger(UserCredentials.class.getName());
  
  
  
  public UserCredentials() {}
  
  public void contextInitialized(ServletContextEvent servletContextEvent) { ServletContext servletContext = servletContextEvent.getServletContext();
    if (servletContext != null) {
    	
    	
    	
      String cfgfile = servletContext.getInitParameter("config_file");
      File configDir = new File(System.getProperty("catalina.base"), "ears");
	  File configFile = new File(configDir, "application.properties");
	
      
      
      logger.info(cfgfile);
      if (cfgfile != null) {
        try {
        	
          properties.load(new FileInputStream(configFile));
         
          logger.info("load properties");
          logger.info(properties.getProperty("useSSL"));
       
          
        } catch (IOException ex) {
          Logger.getLogger(UserCredentials.class.getName()).log(Level.SEVERE, "There was a problem finding/reading the properties file.", ex);
        }
      } else {
        Logger.getLogger(UserCredentials.class.getName()).log(Level.SEVERE, "Could not load config file location from servletcontainer.");
      }
    } else {
      Logger.getLogger(UserCredentials.class.getName()).log(Level.SEVERE, "ServletContext is null");
    }
  }
  
  private static String getUsername() {
    String name = properties.getProperty("be.naturalsciences.bmdc.ears.ontology.rest.username", null);
    logger.info(name);
    return name;
  }
  
  private static String getPassword() {
    String password = properties.getProperty("be.naturalsciences.bmdc.ears.ontology.rest.password", null);
    logger.info(password);
    return password;
  }
  
  public static boolean authenticate(String username, String password) {
    return (username.equals(getUsername())) && (password.equals(getPassword()));
  }
  
  public void contextDestroyed(ServletContextEvent sce) {}
}