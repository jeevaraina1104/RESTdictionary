import java.net.URI;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientResponse;



public class DictionaryClientApi {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		String url = "http://localhost:9000/webservices";
	 String jsonInput = "{\"name\":\"AAAA\",\"type\":\"admin\"}";
//		 ClientConfig clientConfig = new ClientConfig();
//			Client client = ClientBuilder.newClient(clientConfig);
//			URI serviceURI = UriBuilder.fromUri(url).build();
//			WebTarget webTarget = client.target(serviceURI);
//			System.out.println(webTarget.path("getRecords").request()
//					.accept(MediaType.APPLICATION_JSON).get(String.class).toString());
		
		Client client = ClientBuilder.newClient( new ClientConfig());
		WebTarget webTarget = client.target("http://localhost:9000/webservices").path("update").path("Report");
		

		Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
		Response response = invocationBuilder.post(Entity.entity(jsonInput, MediaType.APPLICATION_JSON));
		
		 
		
		     
		System.out.println(response.getStatus());
		
		
		
		
	}

}
