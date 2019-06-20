package com.dollar.service;

import java.util.Map;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.JSONObject;

import com.dollar.model.UserModel;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;


@Path("/dollar")
public class DollarService {

	@GET
	@Path("/serviceInfo")
	public String serviceVersion() {		
		return "dollar version 0.1";
	}
	
	@POST
	@Path("/getUserInfo")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String getReportStatus(@Valid UserModel request) {
		JSONObject info = new JSONObject();
		info.put("name",request.getName());
		info.put("empId", request.getEmpId());
		return info.toString(1);
	}
	
	@POST
	@Path("/saveUserInfo")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String createDb(@Valid UserModel request)
	{
		MongoClient Db=new MongoClient();
		DB db=Db.getDB("Tutorial");  /* Fetching the Dtabase*/
		
		DBCollection table=db.getCollection("empDetail"); 
		/* Fetching the Collection within the database*/
		
		BasicDBObject object=new BasicDBObject();   /* Creating the document*/
		
		object.put("name",request.getName());  /*Inserting the fields into the document*/
		object.put("empId", request.getEmpId());
		table.insert(object);
		
		BasicDBObject find=new BasicDBObject();  /*Updating the field called Mykyong to Mykyong_updated*/
		find.put("name", "samhitha");
		BasicDBObject replace=new BasicDBObject();
		replace.put("name", "abinaya");
		BasicDBObject update_obj=new BasicDBObject();
		update_obj.put("$set", replace);
		table.update(find, update_obj);
		
		return "{\"status\":\"success\"}";
		
		
	}
	
	
}
