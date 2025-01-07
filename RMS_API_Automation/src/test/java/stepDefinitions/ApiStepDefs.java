package stepDefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Assert;
import org.json.JSONArray;
import org.json.JSONObject;


import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static io.restassured.RestAssured.given;

public class ApiStepDefs {

    private Response response;
    private final String BASE_URL = "https://testapi.io/api/RMSTest/ibltest";

    @Given("the API is accessible")
    public void the_api_is_accessible() {
        RestAssured.baseURI = BASE_URL;
    }

    @When("I make a GET request to the endpoint")
    public void iMakeAGetRequestToTheEndpoint() {
        response = given().get();
    }

    @Then("the HTTP status code should be {int}")
    public void theHttpStatusCodeShouldBe(int statusCode) {
        Assert.assertEquals(statusCode, response.getStatusCode());
    }

    @Then("the response time should be below {int} milliseconds")
    public void theResponseTimeShouldBeBelowMilliseconds(int time) {
        Assert.assertTrue(response.time() < time); // this step fails because the response time is above 1000ms everytime the test has been run
    }

    @Then("the id field should not be null or empty for all items and the type field in episode should always be {string}")
    public void theIdFieldShouldNotBeNullOrEmptyForAllItems(String expectedType) {
        JSONObject schedule = new JSONObject(response.asString()).getJSONObject("schedule");
        JSONArray elements = schedule.getJSONArray("elements");

        for (int i = 0; i < elements.length(); i++) {
            JSONObject item = elements.getJSONObject(i);
            String id = item.getString("id");
            String type = item.getJSONObject("episode").getString("type");

            Assert.assertNotNull("ID should not be null", id);
            Assert.assertFalse("ID should not be empty", id.isEmpty());
            Assert.assertEquals("Type should be 'episode'", expectedType, type);
        }
    }

    @Then("the title field in episode should not be null or empty for any schedule item")
    public void theTitleFieldInEpisodeShouldNotBeNullOrEmptyForAnyScheduleItem() {
        JSONObject schedule = new JSONObject(response.asString()).getJSONObject("schedule");
        JSONArray elements = schedule.getJSONArray("elements");

        for (int i = 0; i < elements.length(); i++) {
            String title = elements.getJSONObject(i).getJSONObject("episode").getString("title");

            Assert.assertNotNull("Title should not be null", title);
            Assert.assertFalse("Title should not be empty", title.trim().isEmpty());
        }

    }

    @Then("only one episode should have live field set to true")
    public void onlyOneEpisodeShouldHaveLiveFieldSetToTrue() {
        JSONObject schedule = new JSONObject(response.asString()).getJSONObject("schedule");
        JSONArray elements = schedule.getJSONArray("elements");
        int liveCount = 0;

        for (int i = 0; i < elements.length(); i++) {
            boolean isLive = elements.getJSONObject(i).getJSONObject("episode").getBoolean("live");
            if (isLive) {
                liveCount++;
            }
        }
        Assert.assertEquals("Only one episode should be live", 1, liveCount);
    }

    @Then("the transmission_start date should be before transmission_end date")
    public void theTransmissionStartDateShouldBeBeforeTransmissionEndDate() {
        JSONObject schedule = new JSONObject(response.asString()).getJSONObject("schedule");
        JSONArray elements = schedule.getJSONArray("elements");

        for (int i = 0; i < elements.length(); i++) {
            String start = elements.getJSONObject(i).getString("transmission_start");
            String end = elements.getJSONObject(i).getString("transmission_end");

            ZonedDateTime startTime = ZonedDateTime.parse(start, DateTimeFormatter.ISO_DATE_TIME);
            ZonedDateTime endTime = ZonedDateTime.parse(end, DateTimeFormatter.ISO_DATE_TIME);

            Assert.assertTrue("Transmission start should be before end", startTime.isBefore(endTime));
        }
    }

    @Given("I make a GET request to the an invalid endpoint")
    public void iMakeAGETRequestToTheAnInvalidEndpoint() {
        response = RestAssured.get("https://testapi.io/api/RMSTest/ibltest/2023-09-11");
    }

    @Then("the error object should have these properties {string} and {string}")
    public void theErrorObjectShouldHaveThesePropertiesAnd(String detailsProperty, String responseCodeProperty) {
        JSONObject errorResponse = new JSONObject(response.asString()).getJSONObject("error");
        Assert.assertTrue("Error response should contain 'details'", errorResponse.has(detailsProperty));
        Assert.assertTrue("Error response should contain 'http_response_code'", errorResponse.has(responseCodeProperty));
    }

    @Then("the date value in the response header is present")
    public void theDateValueInTheResponseHeaderIsPresent() {
        String dateHeader = response.getHeader("Date");
        Assert.assertNotNull("Date header should be present", dateHeader);
    }
}