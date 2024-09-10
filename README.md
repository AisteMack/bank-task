# Bank System
Mini bank system that allows you to create, update and  search the account owners (bank customers).

## Setup
###### Make sure Maven and Java 17 are Installed

### Build the JAR file
````
mvn clean install
````
### Run the JAR file
````
java -jar target/bank-0.0.1-SNAPSHOT.jar
````

## Features
* POST [/api/customers](http://localhost:8080/swagger-ui/index.html#/customer-controller/createCustomer) - creates new customer in bank system and assigns account. Returns created customer id.
* PUT [/api/customers/_{id}_](http://localhost:8080/swagger-ui/index.html#/customer-controller/updateCustomer) - updates customers private information. Parameter id - customer id, that you want to update.
* GET [/api/customers?searchTerm=_{N}_&page=_{N1}_&pageSize=_{N2}_](http://localhost:8080/api/customers?searchTerm={N}&page={N1}&pageSize={N2}) - retrieves customers by search term (N). If search term not provided then returns all bank customers. N1 - page number, N2 - element count per page.

## Usable examples
Requests can be done via _Postman_, _curl_ or [_Swagger_](http://localhost:8080/swagger-ui/index.html)

#### POST [/api/customers](http://localhost:8080/swagger-ui/index.html#/customer-controller/createCustomer)
##### Request:
````json
{
  "name": "Jonas",
  "lastName": "Jonaitis",
  "customerType": "PRIVATE",
  "birthDate": "2000-01-15",
  "phoneNumber": "+37060000000",
  "email": "test@test.test",
  "addresses": [
    {
      "country": "Lietuva",
      "city": "Vilnius",
      "street": "Gatve",
      "houseNumber": 3
    }
  ]
}
````
##### Response:
````json
1
````
#### PUT [/api/customers/1](http://localhost:8080/swagger-ui/index.html#/customer-controller/updateCustomer)
##### Request:
````json
{
  "name": "Petras",
  "lastName": "Jonaitis",
  "customerType": "PRIVATE",
  "birthDate": "2000-01-15",
  "phoneNumber": "+37060000000",
  "email": "test@test.test",
  "addresses": [
    {
      "id": 1,
      "country": "Lietuva",
      "city": "Vilnius",
      "street": "Gatve",
      "houseNumber": 5
    }
  ]
}
````
##### Response:
````json
{
  "name": "Petras",
  "lastName": "Jonaitis",
  "birthDate": "2000-01-15",
  "customerType": "PRIVATE",
  "phoneNumber": "+37060000000",
  "email": "test@test.test",
  "addresses": [
    {
      "id": 1,
      "country": "Lietuva",
      "city": "Vilnius",
      "street": "Gatve",
      "houseNumber": 5
    }
  ],
  "accounts": [
    {
      "accountNumber": "LT001609816961471121"
    }
  ],
  "id": 1
}
````
#### GET [/api/customers?_searchTerm=jonai&page=0&pageSize=1_](http://localhost:8080/swagger-ui/index.html#/customer-controller/getCustomers)
##### Response:
````json
{
  "totalCustomerCount": 1,
  "customers": [
    {
      "name": "Petras",
      "lastName": "Jonaitis",
      "birthDate": "2000-01-15",
      "customerType": "PRIVATE",
      "phoneNumber": "+37060000000",
      "email": "test@test.test",
      "addresses": [
        {
          "id": 1,
          "country": "Lietuva",
          "city": "Vilnius",
          "street": "Gatve",
          "houseNumber": 5
        }
      ],
      "accounts": [
        {
          "accountNumber": "LT001609816961471121"
        }
      ],
      "id": 1
    }
  ]
}
````
