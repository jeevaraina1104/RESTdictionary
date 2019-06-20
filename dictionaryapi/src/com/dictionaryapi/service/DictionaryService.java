package com.dictionaryapi.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.xmlbeans.impl.regex.REUtil;
import org.bson.Document;
import org.json.JSONObject;

import com.dictionaryapi.model.DictionaryModel;
import com.dictionaryapi.model.UserModel;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.Updates;


@Path("/webservices")
public class DictionaryService {
	
	@GET
	@Path("/serviceInfo")
	public String serviceVersion() {		
		return "dollar version 0.1";
	}
	
	@GET
	@Path("/getRecords")
	@Produces(MediaType.APPLICATION_JSON)
	public List<DictionaryModel> getRecords() {
		MongoClient Db=new MongoClient();
		DB db=Db.getDB("mydb");  /* Fetching the Dtabase*/
		
		DBCollection table=db.getCollection("dictionary"); 
	      DBCursor cursor = table.find(); 
	      List list = new ArrayList();
	      while(cursor.hasNext()) {
	    	  DBObject obj = cursor.next();
	    	  DictionaryModel dic = new DictionaryModel();
	    	  dic.setWord((String) obj.get("word"));
	    	  dic.setMean((String)obj.get("mean"));
	    	  list.add(dic);
	      }
	   return list;
	}
	
	@GET
	@Path("/getRecord")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List<DictionaryModel> getRecordFromWord(@Valid DictionaryModel request){
	
		MongoClient Db=new MongoClient();
		DB db=Db.getDB("mydb");  
		
		DBCollection table=db.getCollection("dictionary"); 
	  
		DBCursor cursor = table.find(new BasicDBObject().append("word", request.getWord()));
		
		 List list = new ArrayList();
		 while(cursor.hasNext()) {
	    	  DBObject obj = cursor.next();
	    	  DictionaryModel dic = new DictionaryModel();
	    	  dic.setWord((String) obj.get("word"));
	    	  dic.setMean((String)obj.get("mean"));
	    	  list.add(dic);
	      }

		return list;
	}
	
	@GET
	@Path("/getSuggestions")
	@Produces(MediaType.APPLICATION_JSON)
	public List<DictionaryModel> getSuggestions() {
		MongoClient Db=new MongoClient();
		DB db=Db.getDB("mydb");  /* Fetching the Dtabase*/
		
		
		
		DBCollection table=db.getCollection("dictionary"); 
		//table.createIndex( (DBObject) Indexes.text("word"));
		
		//BasicDBObject searchQuery = new BasicDBObject();
		String m ="Record";
		BasicDBObject q = new BasicDBObject();
		q.put("word",  java.util.regex.Pattern.compile(m));
		//table.find(q);
		
		
		 DBCursor cursor = table.find(q); 
	      List list = new ArrayList();
	      while(cursor.hasNext()) {
	    	  DBObject obj = cursor.next();
	    	  DictionaryModel dic = new DictionaryModel();
	    	  dic.setWord((String) obj.get("word"));
	    	  dic.setMean((String)obj.get("mean"));
	    	  list.add(dic);
	      }
	   return list;
	}
	
	
	@POST
	@Path("/insert")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String insert(@Valid DictionaryModel request) {

			      
			      	MongoClient Db=new MongoClient();
					DB db=Db.getDB("mydb");  /* Fetching the Dtabase*/
					
					DBCollection table=db.getCollection("dictionary"); 
					
					
					BasicDBObject object=new BasicDBObject();  
					
					object.put("word",request.getWord()); 
					object.put("mean", request.getMean());
					table.insert(object);
					return "{\"status\":\"success\"}";
	}
	
	
	
	@PUT
	@Path("/update/{type}/{word}/{replace}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("type") String type,@PathParam("word") String word,@PathParam("replace") String replace) {
		DictionaryModel dic = new DictionaryModel();
		UserModel us = new UserModel();
		MongoClient Db=new MongoClient();
		DB db=Db.getDB("mydb"); 
		DictionaryModel updatedict = new DictionaryModel();
	
		  
		  if(type.equals("admin")) {
				DBCollection table1=db.getCollection("dictionary"); 
				
				BasicDBObject find=new BasicDBObject();  
				find.put("word", word);
				BasicDBObject replace1=new BasicDBObject();
				replace1.put("word", replace);
				BasicDBObject update_obj=new BasicDBObject();
				update_obj.put("$set", replace1);
				table1.update(find, update_obj);
				
				updatedict.setWord(replace);
				
				return Response.ok().entity(updatedict.getWord()).build();
			   
		  }else {
			   return Response.status(400).entity("You are not authorized to access !!").build();
		  }
		
		
	}
	
	@DELETE
	@Path("/delete/{type}/{word}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("type") String type,@PathParam("word") String word) {
		DictionaryModel dic = new DictionaryModel();
		UserModel us = new UserModel();
		MongoClient Db=new MongoClient();
		DB db=Db.getDB("mydb");  
		
		
		  
		  if(type.equalsIgnoreCase("admin")) {
			  DBCollection table1=db.getCollection("dictionary");
			  table1.remove(new BasicDBObject().append("word", word));
			   //table1.remove((DBObject) Filters.eq("word", "Record")); 
			   String result = "Record deleted : " + "Record";
			   Gson gson = new Gson();
			   String json = gson.toJson(result); 
			   return Response.status(201).entity(json).build();
			   
		  }else {
			  String result = "You are not authorized to access";
			  return Response.status(201).entity(result).build();
		  }
	}
	
	

}
