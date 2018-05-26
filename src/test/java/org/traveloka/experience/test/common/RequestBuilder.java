package org.traveloka.experience.test.common;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import com.google.gson.JsonParser;

import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.restassured.builder.RequestSpecBuilder;
import java.io.FileNotFoundException;
import java.io.FileReader;


import static io.restassured.RestAssured.given;

/**
 * // TODO Comment
 */
public class RequestBuilder {


  private static RequestSpecification request;
  private  PropertyReader pReader;

  public  RequestBuilder(){
    pReader = new PropertyReader();
    String env=pReader.prop.getProperty("env") ;
    String baseUrl= (String) PropertyReader.prop.get(env+".url");
    request = new RequestSpecBuilder()
        .setContentType(ContentType.JSON)
        .addHeader("Origin","m.traveloka.com")
        .setBaseUri(baseUrl)
        .build();
  }

  public JsonObject createRequestBody(JsonObject jsonFileObject){

    String env=pReader.prop.getProperty("env") ;
    String tvLifetime= (String) pReader.prop.get(env+".tvLifetime");
    String tvSession= (String) pReader.prop.get(env+".tvSession");

    JsonObject context = new JsonObject();

    context.addProperty("tvLifetime", tvLifetime);
    context.addProperty("tvSession", tvSession);

    jsonFileObject.add("context",context);
    jsonFileObject.addProperty("clientInterface","mobile-iOS");

    System.out.println(jsonFileObject);
    return jsonFileObject;
  }


  public ValidatableResponse sendRequest(JsonObject jsonReqBody,String pathUri){

     ValidatableResponse response =
         given()
           .spec(request)
           .body(jsonReqBody.toString())
           .when()
           .post(pathUri)
           .then().log().ifError();
     //    .log().all();

      return response;
  }

  public JsonObject readJSONFiles(String fileName) throws FileNotFoundException{

      String filePath = System.getProperty("user.dir") +fileName;
      JsonParser parser = new JsonParser();
      JsonObject jsonObject = new JsonObject();

          JsonElement jsonElement = parser.parse(new FileReader(filePath));
          jsonObject = jsonElement.getAsJsonObject();

        //convert Object to JSONObject
         System.out.println(jsonObject);
    return jsonObject;
  }

  
  public static void main(String[] args) throws FileNotFoundException{

    RequestBuilder rb=new RequestBuilder();
    JsonObject jsonFileObject=rb.readJSONFiles("resources/request-json/landingPageV2.json");
    JsonObject jsonReqBody=rb.createRequestBody(jsonFileObject);
    rb.sendRequest(jsonReqBody,"/en-id/v2/experience/landingPageV2");
  }
}


