Feature: Additional Functional Testing

  Scenario: Verify the data types of key fields in each schedule item
    Given the API endpoint "https://testapi.io/api/RMSTest/ibltest" is available
    When I send a GET request to the endpoint
    Then the response status code should be 200
    And each schedule item should have fields with the following data types:
      | Field              | Type      |
      | id                 | String    |
      | title              | String    |
      | duration           | Integer   |
      | transmission_start | Timestamp |
      | transmission_end   | Timestamp |
    And the "metadata" object should have fields with correct data types:
      | Field            | Type      |
      | channel_name     | String    |
      | last_updated     | Timestamp |
      | total_programmes | Integer   |

  Scenario: Validate duration field value
    Given the API endpoint "https://testapi.io/api/RMSTest/ibltest" is available
    When I send a GET request to the endpoint
    Then the response status code should be 200
    And each schedule item’s "duration" field should be an integer greater than 0
    And "duration" should be consistent with the difference between "transmission_start" and "transmission_end"

  Scenario: Validate the order of programmes in the schedule array
    Given the API endpoint "https://testapi.io/api/RMSTest/ibltest" is available
    When I send a GET request to the endpoint
    Then the response status code should be 200
    And all schedule items should be ordered by "transmission_start" in ascending order