package be.naturalsciences.bmdc.ears.ontology.rest;


import java.util.HashSet;
import java.util.Set;


import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;







@ApplicationPath("/")
public class MyApplication extends Application {
	

	 private Set<Object> singletons = new HashSet<Object>();

	 
	    public  MyApplication() {
	    
	        singletons.add(new RestVesselOntology());
	    /* INFO: RESTEASY002220: Adding singleton resource be.naturalsciences.bmdc.ears.ontology.rest.RestVesselOntology from Application class be.naturalsciences.bmdc.ears.ontology.rest.MyApplication$Proxy$_$$_WeldClientProxy
	        juil. 05, 2019 2:07:27 PM org.apache.catalina.core.StandardContext reload
	        INFOS: Le rechargement de ce contexte est terminé
	        
	     *   
	     */
	       
	      
	    }
	 
	    @Override
	    public Set<Object> getSingletons() {
	        return singletons;
	    }
	    
	    
}
