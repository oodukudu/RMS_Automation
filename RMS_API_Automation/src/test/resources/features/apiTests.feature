Feature: API Testing

  Scenario: Verify API response status and time
    Given the API is accessible
    When I make a GET request to the endpoint
    Then the HTTP status code should be 200
    And the response time should be below 1000 milliseconds

  Scenario: Validate id fields are not null or empty
    Given the API is accessible
    When I make a GET request to the endpoint
    Then the id field should not be null or empty for all items and the type field in episode should always be "episode"

  Scenario: Verify the title field in episode is not empty
    Given the API is accessible
    When I make a GET request to the endpoint
    Then the title field in episode should not be null or empty for any schedule item

  Scenario: Ensure only one episode is live
    Given the API is accessible
    When I make a GET request to the endpoint
    Then only one episode should have live field set to true

  Scenario: Check transmission dates
    Given the API is accessible
    When I make a GET request to the endpoint
    Then the transmission_start date should be before transmission_end date

  Scenario: Verifying the 'Date' value in the response header
    Given the API is accessible
    When I make a GET request to the endpoint
    Then the date value in the response header is present

  Scenario: Verifying Error Response for Invalid Endpoint
    Given I make a GET request to the an invalid endpoint
    Then the HTTP status code should be 404
    Then the error object should have these properties "details" and "http_response_code"
