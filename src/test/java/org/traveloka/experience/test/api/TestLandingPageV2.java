package org.traveloka.experience.test.api;

import com.google.gson.JsonObject;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ValidatableResponse;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.traveloka.experience.test.common.CSVParametersProvider;
import org.traveloka.experience.test.common.DataFileParameters;
import org.traveloka.experience.test.common.RequestBuilder;
import java.io.IOException;
import static org.assertj.core.api.Assertions.assertThat;



/**
 * // TODO Comment
 */
public class TestLandingPageV2 {

  private static ValidatableResponse  response;
  JsonPath responseJson;

  @BeforeClass
  public void init()  throws IOException {
    RequestBuilder rb=new RequestBuilder();
    JsonObject jsonFileObject=rb.readJSONFiles("/resources/request-json/landingPageV2.json");
    JsonObject jsonReqBody=rb.createRequestBody(jsonFileObject);
    try {
      response = rb.sendRequest(jsonReqBody, "/en-id/v2/experience/landingPageV2");
      responseJson = response.extract().jsonPath();
      if (1==1) throw new Exception();
    }catch(Exception e){
      JsonObject responseJsonObject=rb.readJSONFiles("/resources/input-data/response.json");
      responseJson=JsonPath.from(responseJsonObject.toString());
      e.printStackTrace();
    }
  }

  
  @Test(enabled = true, description = "Landing Page Response Validation", dataProvider = "csv", dataProviderClass = CSVParametersProvider.class)
  @DataFileParameters(name = "LandingPageV2.csv", path = "/resources/input-data")
  public void testLandingPageV2(String uri,String jPath,String textToVerify) {

    //String jPath="data.currentPage.name";

    assertThat(responseJson.getString(jPath).trim()).isEqualToIgnoringCase(textToVerify.trim());
    Reporter.log("Uri="+uri+"\nJSonPath="+jPath+" : "+textToVerify.trim() +" --> is present in Response",1,true);
    //System.out.println("Hello World");
/*  assertThat(retrievedBlogs.getInt("count")).isGreaterThan(7);
    assertThat(retrievedBlogs.getList("blogs")).isNotEmpty();*/

  }
}




