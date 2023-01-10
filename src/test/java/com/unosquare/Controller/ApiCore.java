package com.unosquare.Controller;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.Reporter;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ApiCore {
    JSONObject requestParams;

    public ApiCore(String url) {
        RestAssured.baseURI = url;
        requestParams = new JSONObject();
    }

    public Response getAllUsers(){
        RequestSpecification httpRequest = RestAssured.given();
        httpRequest.headers("Content-Type", "application/json");
        Response response = httpRequest.get("users/");
        return response;
    }

    public Response getSpecificUser(int id){
        RequestSpecification httpRequest = RestAssured.given();
        httpRequest.headers("Content-Type", "application/json");
        Response response = httpRequest.get("users/"+Integer.toString(id));
        return response;
    }

    public Response postUser(String name,String job) {
        requestParams.put("name", name);
        requestParams.put("job", job);
        RequestSpecification httpRequest = RestAssured.given();
        httpRequest.headers("Content-Type", "application/json");
        httpRequest.body(requestParams.toString());
        Response response = httpRequest.post("/users");
        return response;
    }

    public void addMessagesToReport(Response response,String url) {
        Reporter.log("Response Body " + response.prettyPrint());
        Reporter.log("Response code " + response.getStatusCode());
        Reporter.log("Request Body " + requestParams.toString());
        Reporter.log("Request URL " + url);
    }

    public Response createUserFromFile(String filePath,String url) throws IOException, ParseException {
        Response response = this.sendHttpRequestFromFile(filePath,"post",url);
        return response;
    }

    public Response updateUserFromFile(String filePath,String url,int id) throws IOException, ParseException {
        Response response = this.sendHttpRequestFromFile(filePath,"put",url+Integer.toString(id));
        return response;
    }

    public void convertJsonFromFile(String filePath) throws IOException, ParseException {
        JSONParser json = new JSONParser();
        FileReader reader = new FileReader(filePath);
        Object requestParams = json.parse(reader);
    }

    public Response sendHttpRequestFromFile(String filePath,String method,String url){
        Response response = null;
        try {
            this.convertJsonFromFile(filePath);
            RequestSpecification httpRequest = RestAssured.given();
            httpRequest.headers("Content-Type", "application/json");
            httpRequest.body(requestParams.toString());
            response = method.equalsIgnoreCase("post") ?
                    httpRequest.post(url):
                    httpRequest.put(url);
        } catch (IOException exception) {
            exception.printStackTrace();
        } catch (ParseException exception) {
            exception.printStackTrace();
        }
        return response;
    }

    public String getValueFromJson(Response response,String attribute){
        JsonPath jsonPath = new JsonPath(response.asString());
        return jsonPath.getString(attribute);
    }

}
