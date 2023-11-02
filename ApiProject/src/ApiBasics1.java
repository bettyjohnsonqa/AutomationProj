import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.testng.Assert;

import files.ReusableMethods;
import files.payload;
public class ApiBasics1 {

	public static void main(String[] args) {
		//validate if Add place api is working as expected
		//given - all i/p details- query param,header,body
		//when - submit the api-resource, http method
		//then - validate the response
		//set baseURI first
		RestAssured.baseURI = "https://www.rahulshettyacademy.com";
		String response = given().log().all().queryParam("key", "qaclick123").header("Content-Type","application/json")
		.body(payload.AddPlace()).when().post("maps/api/place/add/json")
		.then().assertThat().statusCode(200).body("scope", equalTo("APP"))
		.header("Server", "Apache/2.4.52 (Ubuntu)").extract().response().asString();
		System.out.println(response);
		JsonPath js = new JsonPath(response);
		String placeid = js.getString("place_id");
		System.out.println(placeid);
		
		//update place
		String newAddress = "45 Summer Walk, USA";
		given().log().all().queryParam("key", "qaclick123").header("Content-Type","application/json")
		.body("{\r\n"
				+ "\"place_id\":\""+placeid+"\",\r\n"
				+ "\"address\":\""+newAddress+"\",\r\n"
				+ "\"key\":\"qaclick123\"\r\n"
				+ "}\r\n"
				+ "")
		.when().put("maps/api/place/update/json")
		.then().assertThat().log().all().statusCode(200).body("msg",equalTo("Address successfully updated"));
	
		//get place
		String getResp = given().log().all().queryParam("key","qaclick123").queryParam("place_id", placeid)
		.when().get("maps/api/place/get/json")
		.then().assertThat().log().all().statusCode(200).extract().response().asString();
		
		JsonPath  js1 = ReusableMethods.rawToJson(getResp);
		String actualAddress = js1.getString("address");
		System.out.println(actualAddress);
		Assert.assertEquals(actualAddress, newAddress);
	}
	
	}


	
