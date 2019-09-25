package be.naturalsciences.bmdc.ears.ontology.rest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.util.EntityUtils;




public class FileUploaderClient
{
	
	 private static Logger logger = Logger.getLogger(FileUploaderClient.class.getName());
	  
  public FileUploaderClient() {}
  
  public static void main(String[] args)
  {
   //YS  File inFile = new File("/home/thomas/NetBeansProjects/PlatformEARS/build/testuserdir/config/onto/earsv2-onto-vessel.rdf");
	  
	File inFile = new File("D:/YS_apachetomcat9020/ears/earsv2-onto-vessel.rdf");
	
	
	
	
    FileInputStream fis = null;
    try {  
      fis = new FileInputStream(inFile);
      
      ////////////
    /*  
      CloseableHttpClient client = HttpClients.createDefault(); 
      File file = new File("D:/YS_apachetomcat9020/ears/earsv2-onto-vessel.rdf");
      HttpPost post = new HttpPost("http://localhost/ears2Ont/uploadVesselOntology");
      post.addHeader("user","earsss");
      post.addHeader("password","earsBelgica8400");
      FileBody fileBody = new FileBody(file, ContentType.DEFAULT_BINARY);
  
   
      MultipartEntityBuilder builder = MultipartEntityBuilder.create();
      builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
      builder.addPart("upfile", fileBody);
    
   
      HttpEntity entityd = builder.build();
     
      post.setEntity(entityd);
      HttpResponse responses = client.execute(post);
      
      logger.log(Level.INFO, responses.toString());
      */
      

      
     
  	/////////////
      
      

      DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
      

   // YS  HttpPost httppost = new HttpPost("http://localhost:8080/ears2Ont/uploadVesselOntology");
      HttpPost httppost = new HttpPost("http://localhost/ears2Ont/uploadVesselOntology");
      MultipartEntity entity = new MultipartEntity();
      
      entity.addPart("file", new InputStreamBody(fis, inFile.getName()));
      
      logger.log(Level.INFO, inFile.getName());
    
     //YS UsernamePasswordCredentials creds = new UsernamePasswordCredentials("ears", "REPLACEME");
      UsernamePasswordCredentials creds = new UsernamePasswordCredentials("ears", "earsBelgica8400");

      httppost.addHeader(new BasicScheme(StandardCharsets.UTF_8).authenticate(creds, httppost, null));
          
      httppost.removeHeaders("Content-Disposition");
      httppost.removeHeaders("Content-Transfer-Encoding");
      httppost.removeHeaders("Content-Type");
      httppost.setEntity(entity);
      logger.log(Level.INFO,"httppost.setEntity(entity)");
      HttpResponse response = httpclient.execute(httppost);
      logger.log(Level.INFO,"response");
      int statusCode = response.getStatusLine().getStatusCode();
      logger.log(Level.INFO,"statusCode"+statusCode);
      HttpEntity responseEntity = response.getEntity();
      logger.log(Level.INFO,"responseEntity"+responseEntity);
      String responseString = EntityUtils.toString(responseEntity, "UTF-8");
      logger.log(Level.INFO,"[  " + statusCode + "  ] " + responseString);
      
      return;
    }
    catch (ClientProtocolException e) {
      System.err.println("Unable to make connection");
      e.printStackTrace();
    } catch (IOException e) {
      System.err.println("Unable to read file");
      e.printStackTrace();
    }
    catch (AuthenticationException e) {
      System.err.println("Unable to authenticate");
      e.printStackTrace();
    } finally {
      try {
        if (fis != null) {
          fis.close();
        }
      }
      catch (IOException localIOException5) {
    	  
    	    System.err.println("localIOException5" +localIOException5.getMessage());
    	  
      }
    }
  }
}