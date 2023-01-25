## Dream Job 3000
### Overview
Collects vacancy information from HH and provides statistics based on the gathered data.  
Statistics is available for users on a prepaid basis.

### Project architecture
![Architecture](images/architecture.png?raw=true "Architecture")

### Technology stack
- Java 17
- Spring (Boot, Data, Security, Test, MVC)
- H2 database
- Apache Kafka
- Junit 5, Mockito
- SLF4J
- Swagger 3
- Maven

### How it works
**Module Vacancy**
- Retrieves vacancies from public HeadHunter API
- Retrieves currency exchange rates from Bank of Russia 
- Writes information to the DB
- Provides statistics based on the gathered data
- Data is updated at regular intervals defined in the properties
- Sends statistics request messages to the Billing service
- Statistics is only available to eligible users (with positive balance)
- Accepts user eligibility messages from the Billing service

**Module Billing**
- Processes user payments
- Provides information on balance and history of payments and statistics requests
- Sends user eligibility messages to the Vacancy service
- Accepts statistics request messages from the Vacancy service

### Endpoints
**Module Vacancy**
- POST **/authenticate**  
    Authenticates user by login/password, returns JWT


- GET **/statistics/salary**  
 Returns a list of DTOs with experience level required, number of vacancies, and average salary.


- GET **/statistics/skills**  
    Returns a list of Strings with top 10 skills needed for jobs with a certain experience level requirement.  
    Expects '**exp**' parameter, possible values:
  - noExperience
  - between1And3
  - between3And6
  - moreThan6

**Module Billing**
- POST **/authenticate**  
  Authenticates user by login/password, returns JWT


- GET **/billing/balance**  
 Returns user balance information DTO


- POST **/billing/payment**  
  Processes user payment, returns updated user balance information DTO.  
  Expects '**payment**' parameter - DTO with BigDecimal payload.


- GET **/billing/paymentHistory**  
  Returns a list of user payment DTOs with username, payment time, and amount


- GET **/billing/requestHistory**  
  Returns a list of user statistics request DTOs with username, request time, statistics endpoint, and the price