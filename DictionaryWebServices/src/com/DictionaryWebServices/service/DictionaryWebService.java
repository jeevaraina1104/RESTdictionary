package com.DictionaryWebServices.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.glassfish.jersey.client.ClientConfig;

import com.DictionaryWebServices.model.DictionaryModel;
import com.DictionaryWebServices.model.UserModel;
import com.google.gson.Gson;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.sun.jersey.api.container.filter.LoggingFilter;

@Path("/dictionarywebservice")
public class DictionaryWebService {
	
	@GET
	@Path("/serviceInfo")
	public String serviceVersion() {		
		return "dictionary version 0.1";

	}
	
	@GET
	@Path("/getRecords")
	@Produces(MediaType.APPLICATION_JSON)
	public List<String> getRecords() {

		List<String> list = new ArrayList<String>();
		//DictionaryModel dic = new DictionaryModel();
		 try {
			 	
			 	

				DefaultHttpClient httpClient = new DefaultHttpClient();
				HttpGet getRequest = new HttpGet(
					"http://localhost:9000/webservices/getRecords");
				getRequest.addHeader("accept", "application/json");
				
			

				HttpResponse response = httpClient.execute(getRequest);

				if (response.getStatusLine().getStatusCode() != 200) {
					throw new RuntimeException("Failed : HTTP error code : "
					   + response.getStatusLine().getStatusCode());
				}

				BufferedReader br = new BufferedReader(
		                         new InputStreamReader((response.getEntity().getContent())));

				String output;
				System.out.println("Output from Server .... \n");
				while ((output = br.readLine()) != null) {
					System.out.println(output);
					
					
					
					  Gson gson = new Gson();
					   String json = gson.toJson(output); 
					   list.add(json);
					
				}

				httpClient.getConnectionManager().shutdown();
				

			  } catch (ClientProtocolException e) {
			
				e.printStackTrace();

			  } catch (IOException e) {
			
				e.printStackTrace();
			  }
		return list;
			
		
		
	}
	
	@PUT
	@Path("/update/{type}/{word}/{replace}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String update(@PathParam("type") String type,@PathParam("word") String word,@PathParam("replace") String replace) throws IOException {

		
		   try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
	            HttpPut httpPut = new HttpPut("http://localhost:9000/webservices/update/"+type+"/"+word+"/"+replace);
	            httpPut.setHeader("Accept", "application/json");
	            httpPut.setHeader("Content-type", "application/json");
	        

	            System.out.println("Executing request " + httpPut.getRequestLine());

	            // Create a custom response handler
	            ResponseHandler < String > responseHandler = response -> {
	                int status = response.getStatusLine().getStatusCode();
	                if (status >= 200 && status < 300) {
	                    HttpEntity entity = response.getEntity();
	                    return entity != null ? EntityUtils.toString(entity) : null;
	                } else {
	                    throw new ClientProtocolException("Unexpected response status: " + status);
	                }
	            };
	            String responseBody = httpclient.execute(httpPut, responseHandler);
	            System.out.println("----------------------------------------");
	            System.out.println(responseBody);
	            return responseBody;
	        }
		
		
		
		
	}
	
	@DELETE
	@Path("/delete/{type}/{word}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String delete(@PathParam("type") String type,@PathParam("word") String word) throws ClientProtocolException, IOException {

		 try (CloseableHttpClient httpclient = HttpClients.createDefault()) {

	            HttpDelete httpDelete = new HttpDelete("http://localhost:9000/webservices/delete/"+type+"/"+word);
	            System.out.println("Executing request " + httpDelete.getRequestLine());

	            // Create a custom response handler
	            ResponseHandler < String > responseHandler = response -> {
	                int status = response.getStatusLine().getStatusCode();
	                if (status >= 200 && status < 300) {
	                    HttpEntity entity = response.getEntity();
	                    return entity != null ? EntityUtils.toString(entity) : null;
	                } else {
	                    throw new ClientProtocolException("Unexpected response status: " + status);
	                }
	            };
	            String responseBody = httpclient.execute(httpDelete, responseHandler);
	            System.out.println("----------------------------------------");
	            System.out.println(responseBody);
	            return responseBody;
	        }
			 
		
		
	}
	
}
