# API LIST

## Login

- POST `/api/sessions` //for login
    - request body content:
  ```json
   {
     "email":"customer@gmail.com", 
     "password":"$2a$10$pHD31er2rY6Izwb9TbmxwOvU.kQp2QWJQTWDi2KmmMCGrorFXg1G6"
   } 
  ```
    - response body content:
        - 200 OK
      ```json
      {
        "role":"customer"
      }
      ```
        - 404 NOT FOUND

- DELETE `/api/sessions/current` //for logout
    - request body content:
      ```json
      {
        "email":"customer@gmail.com"
      }
      ```
    - response body content:
        - 200 OK
        - 404 NOT FOUND

- GET `/api/sessions/current` //to check the current user
    - response body content:
        - 200 OK
      ```json
      {
        "email":"customer@gmail.com", 
        "role":"customer"
      }
      ```
        - 404 NOT FOUND

## Shop Employee

- GET `/api/customer/customerExists` //see if customer already exists
    - request parameters:

      | Name        | Value       |
                                  | ----------- | --------------------------------------------|
      | email       | email of the customer to lookup for         |
      | ssn         | (Optional) ssn of the customer to lookup for|

    - response body content:
        - 200 OK //user exists
      ```json
      {
        "exist": "true"
      }
      ```
        - 200 OK //user doesn't exist
      ```json
      {
        "exist": "false"
      }
      ```
        - 400 BAD REQUEST If parameters aren't present

- POST `/api/customer/addCustomer` //add new customer
    - request body content:
      ```json
      {
        "name":"Mario",
        "surname":"Itsumi",
        "address":"Via Roma 15, 10141 Torino (TO)",
        "ssn": "MRATSM80A01Z219V",
        "phoneNumber":"+39 123456789",
        "email":"customer@gmail.com",
        "password": "$2a$10$pHD31er2rY6Izwb9TbmxwOvU.kQp2QWJQTWDi2KmmMCGrorFXg1G6"
      }
      ```
    - response body content:
        - 200 OK
        - 500 SERVER ERROR

- GET `/api/product/all`
    - response body content:
        - 200 OK
          ```json
            [
              {
                "productId":"1",
                "name": "Apples",
                "producer" : "Tonio Cartonio s.p.a.",
                "unitOfMeasurement":"kg",
                "totalQuantity": 10,
                "quantityAvailable": 10,
                "quantityBaskets": 0,
                "quantityOrdered": 0,
                "quantityDelivered": 0,
                "price": 5,
                "imageUrl" : "http://..."
              },
              {
                "productId":"2",
                "name": "Flour",
                "producer" : "Mulino Bianco",
                "unitOfMeasurement":"kg",
                "totalQuantity": 20,
                "quantityAvailable": 10,
                "quantityBaskets": 5,
                "quantityOrdered": 0,
                "quantityDelivered": 5,
                "price": 7,
                "imageUrl" : "http://..."
              }
            ]
          ```
        - 404 NOT FOUND

- POST `/api/product/addToCart` //add product to cart
    - request body content:
      ```json
      {
        "email": "mario.rossi@gmail.com",
        "productId": 7,
        "quantity": 1
      }
      ```
    - response body content
        - 200 OK
        - 500 SERVER ERROR //TODO add customer id to request body

- GET `/api/customer/getCart` //list products inside current cart
    - request parameters:

  | Name        | Value       | 
            | ----------- | --------------------------------------------|
  | email       | email of the customer to lookup for         |

    - response body content:
        - 200 OK
          ```json
          [
              {
                "productId":"1",
                "name": "Apples",
                "producer" : "Tonio Cartonio s.p.a.",
                "unitOfMeasurement":"kg",
                "totalQuantity": 10,
                "quantityAvailable": 10,
                "quantityBaskets": 0,
                "quantityOrdered": 0,
                "quantityDelivered": 0,
                "price": 5,
                "imageUrl" : "http://..."
              },
              {
                "productId":"2",
                "name": "Flour",
                "producer" : "Mulino Bianco",
                "unitOfMeasurement":"kg",
                "totalQuantity": 20,
                "quantityAvailable": 10,
                "quantityBaskets": 5,
                "quantityOrdered": 0,
                "quantityDelivered": 5,
                "price": 7,
                "imageUrl" : "http://..."
              }
            ]
          ```
        - 404 NOT FOUND

- GET `/api/customer/getWallet` //get customer's wallet
    - request parameters:

  | Name        | Value       | 
            | ----------- | --------------------------------------------|
  | email       | email of the customer to lookup for         |
-
    - response body content:
      ```
      {
        12.50
      }
      ```

- POST `/api/customer/topUp` //top up customer's wallet
    - request body content:
      ```json
      {
        "email":"customer@gmail.com",
        "value": 10.00
      }
      ```
    - response body content:
        - 200 OK
        - 404 NOT FOUND

- POST `/api/customer/placeOrder` //create an order for the customer
    - request body content:
  ```json
  {
    "email":"owner_of@cart.com",
    "customer": "customer@who_ordered.com"
  }
  ```
    - response body content:
        - 200 OK
        - 404 NOT FOUND

- DELETE `/api/customer/dropOrder`
    - request body content:
      ```json 
      {
      "email":"customer@gmail.com"
      }
      ```
    - response body content:
        - 200 OK
        - 500 Internal server error

- POST `/api/customer/deliverOrder` //mark order as delivered
    - request body content:
      ```
      {
        10 //It's the id of the order to deliver
      }
      ```
    - response body content:
        - 200 OK
        - 404 NOT FOUND

- GET `/api/customer/getOrdersByEmail`
  //get all the orders of a single customer using its email
    - request parameters:

      | Name        | Value       |
            | ----------- | --------------------------------------------|
      | email       | email of the customer to lookup for         |
        - response body content:
            ```json 
          [
              {
                  "orderId":60,
                  "email":"paolo.bianchi@gmail.com",
                  "productList": [
                      {
                          "productId":"10",
                          "name":"Bananas",
                          "producer":"default producer",
                          "unit":"Kg",
                          "unit price":"6.25",
                          "amount":"5.0"
                      }
                 ]
              }
          ]
          ```
