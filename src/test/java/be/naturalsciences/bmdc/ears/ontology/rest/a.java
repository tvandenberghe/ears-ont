package be.naturalsciences.bmdc.ears.ontology.rest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;

import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.util.EntityUtils;

public class a {

	  public static void main(String[] args)
	  {
		    try {
		  CloseableHttpClient client = HttpClients.createDefault();
		    HttpPost httpPost = new HttpPost("http://www.bmdc.be/");
		 
		    List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		    params.add(new BasicNameValuePair("username", "casino"));
		    params.add(new BasicNameValuePair("password", "casino"));
		    httpPost.setEntity((HttpEntity) new UrlEncodedFormEntity(params));
		 
		    CloseableHttpResponse response = client.execute(httpPost);
		 System.out.println(response.getStatusLine().getStatusCode());
		    client.close();
	    }
	    catch (ClientProtocolException e) {
	      System.err.println("Unable to make connection");
	      e.printStackTrace();
	    } catch (IOException e) {
	      System.err.println("Unable to read file");
	      e.printStackTrace();
	    } 
	     
	  }

}
