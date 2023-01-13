# Tikal - Backend Developer Assignment
### Author: Tamir Mayblat

This is the ATM application for the BE-Challenge, developed using Java as a spring boot application.

### Prerequisite
* Java and tomcat, deploy the code onto a server or simply run it from your ide (intellij etc...).
* Run ```mvn clean install```
* Connection to Mongodb is also provided by default and can be changed if needed by modifying the fields starting with ```db.``` in application.properties.
* I've already run a process (see comment out in Application.java) which fills the db with default money settings and initiates the atm, the code for this process can be found in MoneyService.

### Using the web service
* Start the server and wait for log ```server & db ready now```
* Go to http://127.0.0.1:8080/tikal-atm/api/swagger-ui.html#/atm-controller/
* You should have access to Swagger api page which controls the web service.

### Features
You can find the following apis in the web service:

###### POST /atm/refill -
* Add items of valid bills or coins, list of valid items can be found in application.properties (```money.bills.values, money.coins.values```).
* If an unknown bill/coin is provided then the server should respond with status 422 (Unprocessable Entity) and appropriate message.
* Error handling: If an unknown bill/coin is provided then the would server respond with status 422.
* If no error had occurred the server would return 200 with text 'done'.
* Example for using refill:
```
{
  "money": {
    "200": 7,
    "100": 4
    "20": 15,
    "10": 10,
    "5": 1,
    "1": 10,
    "0.1": 12,
    "0.01": 21,
  }
}
```

###### POST /atm/withdrawal -
* Withdrawal api should be able to receive any required amount in 2 point decimal notation.
* Withdrawal api returns the biggest bills or biggest coins available in the ATM.
* Error handling: 
  * TheMaximum amount for a single withdrawal is 2000, configurable in application.properties (```atm.max.withdrawal```). If exceeded the server response with status 422.
  * If there is not enough money (bills or coins) in the ATM return http status 409, the error includes the closest amount that is available for withdrawal.
  * Result will not include more than 50 coins, if so server returns http status 422.
  * Example for using withdrawal:
    * If the ATM had run refill with the above configuration and calling withdrawal with the amount as describe below:
      ```
      {
          "amount": 837.44
      }
      ```
      expected result: 
      ```
          {
              "bills": [
                    {
                    "id": "200.0",
                    "amount": 4
                    },
                    {
                    "id": "20.0",
                    "amount": 1
                    }
                    ],
              "coins": [
                    {
                    "id": "10.0",
                    "amount": 1
                    },
                    {
                    "id": "5.0",
                    "amount": 1
                    },
                    {
                    "id": "1.0",
                    "amount": 2
                    },
                    {
                    "id": "0.1",
                    "amount": 4
                    },
                    {
                    "id": "0.01",
                    "amount": 4
                    }
              ]
          }
      ```
    

### TODOs
* Should add more tests.

Please let me know if anything is missing or needs modifications.
### Thank you!
