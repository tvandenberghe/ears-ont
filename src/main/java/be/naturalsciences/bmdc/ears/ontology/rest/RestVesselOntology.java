package be.naturalsciences.bmdc.ears.ontology.rest;

import be.naturalsciences.bmdc.ontology.IOntologyModel;
//replace by local class import be.naturalsciences.bmdc.ontology.writer.FileUtils;

import be.naturalsciences.bmdc.ontology.writer.ScopeMap;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;


import org.apache.commons.codec.binary.Base64;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.MultipartStream;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.core.Headers;
import org.jboss.resteasy.core.ServerResponse;



@Path("/")
@MultipartConfig
public class RestVesselOntology
{
	
	
  //private static final String VESSEL_ONTOLOGY_DIR = "/var/www/ears2/";
 // private static final String VESSEL_ONTOLOGY_FILE_NAME = "earsv2-onto-vessel.rdf";
  //private static final String VESSEL_ONTOLOGY_FILE_LOCATION = "/var/www/ears2/earsv2-onto-vessel.rdf";
  
  private static Logger logger = Logger.getLogger(RestVesselOntology.class.getName());
  private static final String AUTHORIZATION_PROPERTY = "Authorization";
  private static final String AUTHENTICATION_SCHEME = "Basic";
  
  
 // @Context
  //private UriInfo context;
  
  public RestVesselOntology() {}
  
  
  //YS OK
	@GET   
	@Path("/getServiceUp")   
	 @Produces(MediaType.TEXT_PLAIN)
	public String getServiceUp() 
	{
		  
		logger.log(Level.INFO, "Module ears2Ont  getServiceUp");
		
	

		return "Service is Up";
		
		
	}
	

  
  
  
  //YS curl  -X POST  --user ears:earsBelgica8400 -F earsv2-onto-vessel2.rdf=@earsv2-onto-vessel2.rdf  http://localhost/ears2Ont/uploadVesselOntology 
  @POST
  @Path("/uploadVesselOntology")
  @Consumes({"multipart/form-data"})
  @Produces({"application/xml"})
  public Response uploadVesselOntology(@Context HttpServletRequest request, InputStream uploadedInputStream, @Context HttpHeaders headers)
  {
	  
	//  testMalformedStreamException();
	  logger.info("Module ears2Ont  uploadVesselOntology");
	  // <scope rdf:datatype="http://www.w3.org/2001/XMLSchema#string">VESSEL</scope>
    return uploadOntology(request, uploadedInputStream, headers, true, ScopeMap.Scope.VESSEL, "earsv2-onto-vessel.rdf");
  }
  






//YS curl  -X POST  --user ears:earsBelgica8400 -F bmdc-test.rdf=@bmdc-test.rdf  http://localhost/ears2Ont/uploadProgramOntology 

  @POST
  @Path("/uploadProgramOntology")
  @Consumes({"multipart/form-data"})
  @Produces({"application/xml"})
  public Response uploadProgramOntology(@Context HttpServletRequest request, InputStream uploadedInputStream, @Context HttpHeaders headers)
  {
	  logger.log(Level.INFO, "Module ears2Ont  /uploadProgramOntology");
   return uploadOntology(request, uploadedInputStream, headers, false, ScopeMap.Scope.PROGRAM, null);
  }
  
  
  
  
//YS curl  -X POST  --user ears:earsBelgica8400 -F bmdc-test.rdf=@bmdc-test.rdf  http://localhost/ears2Ont/uploadProgramOntology 
//AND 
//  YS curl  -X POST  --user ears:earsBelgica8400 -F earsv2-onto-vessel2.rdf=@earsv2-onto-vessel2.rdf  http://localhost/ears2Ont/uploadVesselOntology 
  private Response uploadOntology(HttpServletRequest request, InputStream uploadedInputStream, @Context HttpHeaders headers, boolean authenticate, ScopeMap.Scope matchScope, String overwriteWithFileName) {
	 	 
 	 String home = System.getProperty("catalina.base");

 	// inserts correct file path separator on *nix and Windows
 	// works on *nix
 	// works on Windows
 	java.nio.file.Path path = java.nio.file.Paths.get(home, "var", "www", "ears2");
 	java.nio.file.Path pathTempFile = java.nio.file.Paths.get(home, "var", "www", "temp");
	  
	  boolean isMultipart = ServletFileUpload.isMultipartContent(request);
	  if (isMultipart) {
		  logger.log(Level.INFO,"Module ears2Ont  isMultipart");
	}
	
	  
	  if (authenticate) {
	    	
    	
      List<String> authorizationHeader = headers.getRequestHeader(AUTHORIZATION_PROPERTY);
      String authorizationHeaderValue = (String)authorizationHeader.get(0);
      if ((authorizationHeaderValue != null) && (authorizationHeaderValue.startsWith(AUTHENTICATION_SCHEME))) {
        String encodedString = authorizationHeaderValue.replaceAll(AUTHENTICATION_SCHEME, "");
        Map<String, String> decode = decodeBase64(encodedString);
      
        if (!UserCredentials.authenticate((String)decode.get("username"), (String)decode.get("password"))) {
          MessageBean m = new MessageBean(null, 500, "Authentication failed: wrong credentials");
          return Response.status(500).type("application/xml").entity(m).build();
        }
      } else {
    	  MessageBean m = new MessageBean(null, 500, "Cannot authenticate");
        return Response.status(500).type("application/xml").entity(m).build();
      }
      logger.log(Level.INFO, "Module ears2Ont  /Authentication OK");
    }
	  
	    if (uploadedInputStream == null) {
	 
	      MessageBean m = new MessageBean(null, 400, "Invalid form data");
	      return Response.status(400).type("application/xml").entity(m).build();
	    }
	  
	    try
	    {
	    	
	 
	     FileUtils.createDirectoryIfNotExistsWithPath(path);
	     FileUtils.createDirectoryIfNotExistsWithPath(pathTempFile);
	     

	    
	    } catch (SecurityException se) {
	       
	      MessageBean m = new MessageBean(null, 500, "Cannot create destination folder on server");
	      return Response.status(500).type("application/xml").entity(m).build();
	    }
	    
	    List<String> authorizationHeader = headers.getRequestHeader("filename");
	   for (Iterator iterator = authorizationHeader.iterator(); iterator.hasNext();) {
		String string = (String) iterator.next();
		System.out.println(string);
		
	}
	    
	  //https://commons.apache.org/proper/commons-fileupload/using.html
	    DiskFileItemFactory factory = new DiskFileItemFactory();
	    factory.setRepository(
	      new File(pathTempFile.toString()));
	    factory.setSizeThreshold(
	      DiskFileItemFactory.DEFAULT_SIZE_THRESHOLD);
	    factory.setFileCleaningTracker(null);
	 
	    ServletFileUpload upload = new ServletFileUpload(factory);
	    List items;
	    String fileName ="";
		try {
			
			items = upload.parseRequest(request);
		    Iterator iter = items.iterator();
		    
		    
		    while (iter.hasNext()) {
		        FileItem item = (FileItem) iter.next();
		
		 
	 ///////////
		       fileName = item.getName();
		 
		      if (overwriteWithFileName != null) {
		      fileName = overwriteWithFileName;
		    
		      } else if (fileName == null) {
		      MessageBean m = new MessageBean(null, 500, "Cannot save file: no default file name provided and file name is not provided in POST header.");
		        return Response.status(500).type("application/xml").entity(m).build();
		      }
	 //////////////
		 
		  
		 
		 
		 
		// System.err.println("------------------"+item.getFieldName());
		// System.err.println("------------------"+item.toString());
		 //curl  -X POST  --user ears:earsBelgica8400 -F file=@earsv2-onto-vessel.rdf;filename=earsv2-onto-vessel.rdf  http://localhost/ears2Ont/uploadVesselOntology  

	//curl  -X POST  --user ears:earsBelgica8400 -F earsv2-onto-vessel=@earsv2-onto-vessel.rdf  http://localhost/ears2Ont/uploadVesselOntology 	 
	
		        if (!item.isFormField()) {
		        	  
		            try (
		              InputStream uploadedStream = item.getInputStream();
		              InputStream uploadedStreamCopyToCheckScope = item.getInputStream();
		            		
		            	
               
		              OutputStream out = new FileOutputStream(path.toString()  + File.separator + fileName);) {
		                
		             	if (!((String)IOntologyModel.getStaticStuff(uploadedStreamCopyToCheckScope).get("SCOPE")).equals(matchScope.name())) {
	            	        MessageBean m = new MessageBean(null, 500, "Cannot save file: not recognized as a " + matchScope.name() + " ontology");
	            	        return Response.status(500).type("application/xml").entity(m).build();
	            	      }
		             	else {
		             		uploadedStreamCopyToCheckScope.close();
		             		 IOUtils.copy(uploadedStream, out);
						}
	          
		            } catch (IOException ex) {
						// TODO Auto-generated catch block
		            	 MessageBean m = new MessageBean(null, 500, "Cannot save file IO: " + ex.getClass().getSimpleName() + " because of " + ex.getMessage());
		   		      return Response.status(500).type("application/xml").entity(m).build();
					}
		        }
		    }    

			
		} catch (FileUploadException ex) {
			// TODO Auto-generated catch block
			 MessageBean m = new MessageBean(null, 500, "Cannot save file IO: " + ex.getClass().getSimpleName() + " because of " + ex.getMessage());
  		      return Response.status(500).type("application/xml").entity(m).build();
		}
	 
/*
	  
	  if (authenticate) {
    	
    	  logger.log(Level.INFO, "/232");
      List<String> authorizationHeader = headers.getRequestHeader(AUTHORIZATION_PROPERTY);
      String authorizationHeaderValue = (String)authorizationHeader.get(0);
      if ((authorizationHeaderValue != null) && (authorizationHeaderValue.startsWith(AUTHENTICATION_SCHEME))) {
        String encodedString = authorizationHeaderValue.replaceAll(AUTHENTICATION_SCHEME, "");
        Map<String, String> decode = decodeBase64(encodedString);
      
        if (!UserCredentials.authenticate((String)decode.get("username"), (String)decode.get("password"))) {
        	  logger.log(Level.INFO, "/Authentication failed");
          MessageBean m = new MessageBean(null, 500, "Authentication failed: wrong credentials");
          return Response.status(500).type("application/xml").entity(m).build();
        }
      } else {
    	  logger.log(Level.INFO, "/authorizationHeaderValue NOK");
        MessageBean m = new MessageBean(null, 500, "Cannot authenticate");
        return Response.status(500).type("application/xml").entity(m).build();
      }
      logger.log(Level.INFO, "/Authentication OKOKOKOKOKOKOK");
    }
    
    if (uploadedInputStream == null) {
    	 logger.log(Level.INFO, "uploadedInputStream not null");
      MessageBean m = new MessageBean(null, 400, "Invalid form data");
      return Response.status(400).type("application/xml").entity(m).build();
    }
    try
    {
    	 logger.log(Level.INFO, "/createDirectoryIfNotExists");
    	 

    	 
    //	 String home = System.getProperty("catalina.base");

    	// inserts correct file path separator on *nix and Windows
    	// works on *nix
    	// works on Windows
    //	java.nio.file.Path path = java.nio.file.Paths.get(home, "var", "www", "ears2");
    
    	
    //	 String path = System.getProperty("catalina.base")+ File.separator+ "var" + File.separator + "www" + File.separator + "ears2";
    	 logger.log(Level.INFO,"path" +path.toString());
     FileUtils.createDirectoryIfNotExistsWithPath(path);
     

      logger.log(Level.INFO, "/createDirectoryIfNotExists222");
    } catch (SecurityException se) {
        logger.log(Level.INFO, "/SecurityException");
      MessageBean m = new MessageBean(null, 500, "Cannot create destination folder on server");
      return Response.status(500).type("application/xml").entity(m).build();
    }
    logger.log(Level.INFO, "ByteArrayOutputStream start");
 -----------------------done YS
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    byte[] bytes = null;
    try {
      IOUtils.copy(uploadedInputStream, baos);
      bytes = baos.toByteArray();
    } catch (IOException ex) {
      Logger.getLogger(RestVesselOntology.class.getName()).log(Level.SEVERE, null, ex);
    }
    uploadedInputStream = new ByteArrayInputStream(bytes);
    logger.log(Level.INFO, "ByteArrayOutputStream start");
    
    if (!((String)IOntologyModel.getStaticStuff(uploadedInputStream).get("SCOPE")).equals(matchScope.name())) {
      MessageBean m = new MessageBean(null, 500, "Cannot save file: not recognized as a " + matchScope.name() + " ontology");
      return Response.status(500).type("application/xml").entity(m).build();
    }
    -----------------------done YS
 //doublon ??????????????ys   uploadedInputStream = new ByteArrayInputStream(bytes);
    String contentType = request.getContentType();
    
    int boundaryIndex = contentType.indexOf("boundary=");
    byte[] boundary = contentType.substring(boundaryIndex + 9).getBytes();
    MultipartStream multipartStream = new MultipartStream(uploadedInputStream, boundary, 5000000, null);
    try
    {
    	logger.log(Level.INFO, "1");
      boolean nextPart = multipartStream.skipPreamble();
      logger.log(Level.INFO, "2");
      String header = multipartStream.readHeaders();
      logger.log(Level.INFO, "3");
      String fileName = null;
      
      Pattern pattern = Pattern.compile("filename=\\\"([^\\\"]+)\\\"");
      Matcher matcher = pattern.matcher(header);
      logger.log(Level.INFO, "4");
      if (matcher.find()) {
    	  logger.log(Level.INFO, "5");
        fileName = matcher.group(1);
      }
      if (overwriteWithFileName != null) {
    	  logger.log(Level.INFO, "6");
        fileName = overwriteWithFileName;
        logger.log(Level.INFO, "7");
      } else if (fileName == null) {
    	  logger.log(Level.INFO, "8");
        MessageBean m = new MessageBean(null, 500, "Cannot save file: no default file name provided and file name is not provided in POST header.");
        return Response.status(500).type("application/xml").entity(m).build();
      }
      while (nextPart) {
    	  logger.log(Level.INFO, "9");
        ByteArrayOutputStream data = new ByteArrayOutputStream();
        
        multipartStream.readBodyData(data);
   
   //	 String home = System.getProperty("catalina.base");

 	//java.nio.file.Path path = java.nio.file.Paths.get(home, "var", "www", "ears2");
        
    FileUtils.saveToFile(data, path.toString() + fileName);
     //   FileUtils.saveToFile(data, "/var/www/ears2/" + fileName);
        nextPart = multipartStream.readBoundary();
      }
    } catch (IOException ex) {
      MessageBean m = new MessageBean(null, 500, "Cannot save file IO: " + ex.getClass().getSimpleName() + " because of " + ex.getMessage());
      return Response.status(500).type("application/xml").entity(m).build();
    }
    MessageBean m = new MessageBean(null, 200, "File correctly saved.");
    return Response.status(200).type("application/xml").entity(m).build();
    
    
    */
		
		   MessageBean m = new MessageBean(null, 200, "File correctly saved.");
		    return Response.status(200).type("application/xml").entity(m).build();
		
  }
  
  
  //YS http://localhost/ears2Ont/ontology/vessel ok
  //curl  -X GET  --user ears:earsBelgica8400  http://localhost/ears2Ont/ontology/vessel 
  @GET
  @Path("/ontology/vessel")
  @Produces({"application/xml"})
  public Response getVesselOntology() {
		logger.log(Level.INFO, "/ontology/vessel");
		
	 	 String home = System.getProperty("catalina.base");

	  	java.nio.file.Path path = java.nio.file.Paths.get(home, "var", "www", "ears2");
	         
	  
	 //not portable 	File file = new File("/var/www/ears2/earsv2-onto-vessel.rdf");
		
		
    File file = new File( path.toString()  + File.separator +"earsv2-onto-vessel.rdf");
    if (file.exists()) {
    	logger.log(Level.INFO, "exists");
      Response.ResponseBuilder response = Response.ok(file);
      response.header("Content-Disposition", "attachment; filename=earsv2-onto-vessel.rdf");
      response.header("Content-Type", "application/rdf+xml");
      return response.build();
    }
    else {
    	logger.log(Level.INFO, "file not exist in var www ears2 ");
	}
    
    MessageBean m = new MessageBean(null, 500, "Cannot return vessel ontology file because it can't be found on the server.");
    return Response.status(500).entity(m).build();
  }
  
  
  //YS http://localhost/ears2Ont/ontology/vessel/date => 2018-02-15T14:00:23.124Z
  /*
   *       <dc:modified rdf:datatype="http://www.w3.org/2001/XMLSchema#dateTime">2018-02-15T14:00:23.124Z</dc:modified>
    </owl:Ontology>
   */
  
  //curl  -X GET  --user ears:earsBelgica8400  http://localhost/ears2Ont/ontology/vessel/date
  @GET
  @Path("/ontology/vessel/date")
  @Produces({"text/plain"})
  public Response getVesselOntologyDate()
  {

		
		
	 	 String home = System.getProperty("catalina.base");

		  	java.nio.file.Path path = java.nio.file.Paths.get(home, "var", "www", "ears2");
		         
		  
			
			
			
	    File file = new File( path.toString()  + File.separator +"earsv2-onto-vessel.rdf");	
//    File file = new File("/var/www/ears2/earsv2-onto-vessel.rdf");
    if (file.exists()) {
      try {
    	  
    	
        String dateModified = (String)IOntologyModel.getStaticStuff(file).get("DATEMODIFIED");
        String dateVersionInfo = (String)IOntologyModel.getStaticStuff(file).get("VERSIONINFO");
        Response.ResponseBuilder response = null;
        if (dateModified != null) {
          response = Response.ok(dateModified, "text/plain");
        } else if (dateVersionInfo != null) {
          response = Response.ok(dateVersionInfo, "text/plain");
        }
        
        return response.build();
      } catch (FileNotFoundException ex) {
        Logger.getLogger(RestVesselOntology.class.getName()).log(Level.SEVERE, null, ex);
        return null;
      }
    }
    else {
    	  logger.log(Level.SEVERE, "/ontology/vessel/date file not exist");
    	   MessageBean m = new MessageBean(null, 500, "Cannot return the vessel ontology date because the file can't be found on the server.");
    	    return Response.status(500).entity(m).build();
	}
 
  }
  
  
  //YS http://localhost/ears2Ont/ontology/program?name=bmdc-test.rdf mais marche aussi si scope autre choses car pas de check
  //curl  -X GET  --user ears:earsBelgica8400  http://localhost/ears2Ont/ontology/program?name=bmdc-test.rdf
  @GET
  @Path("/ontology/program")
  @Produces({"application/xml"})
  public Response getProgramOntology(@Context HttpServletRequest request)
  {
	  logger.log(Level.INFO, "/ontology/program");
    String nameId = request.getParameter("name");
    logger.log(Level.INFO, "nameId"+nameId);
    if (nameId == null) {
      MessageBean m = new MessageBean(null, 500, "Cannot return reponse as the get parameter 'name' is not provided.");
      return Response.status(500).entity(m).build();
    }
    File file = null;
    String fileName = null;
    if (nameId.contains(".rdf")) {
      fileName = new String(nameId);
    } else {
      fileName = new String(nameId + ".rdf");
    }
    
    String home = System.getProperty("catalina.base");
  	java.nio.file.Path path = java.nio.file.Paths.get(home, "var", "www", "ears2");
     file = new File( path.toString()  + File.separator +fileName);	
    
    
    
  //NOt portable  file = new File("/var/www/ears2/" + fileName);
    if (file.exists()) {
      Response.ResponseBuilder response = Response.ok(file);
      response.header("Content-Disposition", "attachment; filename=" + fileName);
      response.header("Content-Type", "application/rdf+xml");
      return response.build();
    }
    else {
    	 MessageBean m = new MessageBean(null, 500, "Cannot return the program ontology because the ontology file can't be found on the server.");
    	 return Response.status(500).entity(m).build();
	}
    
  //  MessageBean m = new MessageBean(null, 500, "Cannot return the program ontology because the ontology file can't be found on the server.");
   // return Response.status(500).entity(m).build();
  }
  
  //YS http://localhost/ears2Ont/ontology/program/date?name=earsv2-onto-vessel.rdf
  //curl  -X GET  --user ears:earsBelgica8400  http://localhost/ears2Ont/ontology/program/date?name=earsv2-onto-vessel.rdf
  //curl  -X GET   http://localhost/ears2Ont/ontology/program/date?name=earsv2-onto-vessel.rdf
  @GET
  @Path("/ontology/program/date")
  @Produces({"text/plain"})
  public Response getProgramOntologyDate(@Context HttpServletRequest request)
  {
	  
	  logger.log(Level.INFO, "/ontology/program/date");
    String nameId = request.getParameter("name");
    if (nameId == null) {
      MessageBean m = new MessageBean(null, 500, "Cannot return date as the get parameter 'name' is not provided.");
      return Response.status(500).entity(m).build();
    }
    File file = null;
    String fileName = null;
    if (nameId.contains(".rdf")) {
      fileName = new String(nameId);
    } else {
      fileName = new String(nameId + ".rdf");
    }
    
    
    String home = System.getProperty("catalina.base");
  	java.nio.file.Path path = java.nio.file.Paths.get(home, "var", "www", "ears2");
     file = new File( path.toString()  + File.separator +fileName);	
    
    
    
  //not portable  file = new File("/var/www/ears2/" + fileName);
    if (file.exists()) {
      try {
        String dateModified = (String)IOntologyModel.getStaticStuff(file).get("DATEMODIFIED");
        String dateVersionInfo = (String)IOntologyModel.getStaticStuff(file).get("VERSIONINFO");
        Response.ResponseBuilder response = null;
        if (dateModified != null) {
          response = Response.ok(dateModified, "text/plain");
        } else if (dateVersionInfo != null) {
          response = Response.ok(dateVersionInfo, "text/plain");
        }
        
        return response.build();
      } catch (FileNotFoundException ex) {
        Logger.getLogger(RestVesselOntology.class.getName()).log(Level.SEVERE, null, ex);
        return null;
      }
    }
    
    MessageBean m = new MessageBean(null, 500, "Cannot return the program ontology date because the file with name '" + fileName + "' can't be found on the server.");
    return Response.status(500).entity(m).build();
  }
  

 
  
  //YS curl  -X GET  --user ears:earsBelgica8400  http://localhost/ears2Ont/authenticate
  @GET  
  @Path("/authenticate")
  @Produces({"text/plain"})
  public String canAuthenticate(@Context HttpHeaders headers)
  {
	  
	  logger.log(Level.INFO, "Module ears2Ont /authenticate");

	 
      //Fetch authorization header

	    
    List<String> authorizationHeader = headers.getRequestHeader(AUTHORIZATION_PROPERTY);


    
 if (authorizationHeader != null) {
      String authorizationHeaderValue = (String)authorizationHeader.get(0);
      if ((authorizationHeaderValue != null) && (authorizationHeaderValue.startsWith(AUTHENTICATION_SCHEME))) {
        String encodedString = authorizationHeaderValue.replaceAll(AUTHENTICATION_SCHEME, "");
        Map<String, String> decode = decodeBase64(encodedString);
        String username = (String)decode.get("username");
        String password = (String)decode.get("password");
        if ((username != null) && (password != null) && (UserCredentials.authenticate((String)decode.get("username"), (String)decode.get("password")))) {
          return Boolean.TRUE.toString();
        }
        return Boolean.FALSE.toString();
      }
      
      return Boolean.FALSE.toString();
    }
    
    return Boolean.FALSE.toString();
   
  }
  




//ys ok

  private static Map<String, String> decodeBase64(String encodedString)
  {
    byte[] decodedBytes = Base64.decodeBase64(encodedString.getBytes());
    String pair = new String(decodedBytes);
    String[] userDetails = pair.split(":", 2);
    Map<String, String> map = new HashMap<String, String>();
    if (userDetails.length == 2) {
      map.put("username", userDetails[0]);
      map.put("password", userDetails[1]);
    }
    return map;
  }
}
