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
  - request parameters: {}
  - request body content: 
    ```json
    {
      "email":"customer@gmail.com"
    }
    ```
  - response body content: 
    - 200 OK //user exists
    ```json
    {
      "exists": "true"
    }
    ```
    - 200 OK //user doesn't exist
    ```json
    {
      "exists": "false"
    }
    ```
    - 500 SERVER ERROR
- POST `/api/customer/addCustomer` //add new customer
  - request body content: 
    ```json
    {
      "email":"customer@gmail.com",
      "password": "$2a$10$pHD31er2rY6Izwb9TbmxwOvU.kQp2QWJQTWDi2KmmMCGrorFXg1G6",
      "name":"Mario",
      "surname":"Itsumi", 
      "role":"customer",
      "address":"Via Roma 15, 10141 Torino (TO)",
      "phone number":"+39 123456789"
    }
    ```
  - response body content:
    - 200 OK
    - 500 SERVER ERROR

- GET `/api/product/browseProducts` //list sellable products
  - response body content: 
    - 200 OK
      ```json
      {
        "products": 
        [
          {
            "productId":"1",
            "name": "Apples",
            "producer" : "Tonio Cartonio s.p.a.",
            "unit":"kg",
            "unit price" : "1.99",
            "amount" : "100" //available amount (total-sum of orders)
          },
          {
            "productId":"2",
            "name": "Eggs",
            "producer" : "KFC farms", 
            "unit":"unit",
            "unit price" : "0.10",
            "amount" : "50"
          }
        ]
      }
      ```
    - 404 NOT FOUND
//TODO add customer id to request body
    - POST `/api/product/addToCart` //add product to cart
      - request body content:
        ```json
        {
          "productId":"1",
          "name": "Apples",
          "producer" : "Tonio Cartonio s.p.a.",
          "unit":"kg",
          "unit price" : "1.99",
          "amount" : "1" //amount i wish to order
        }
        ```
      - response body content
        - 200 OK
        - 500 SERVER ERROR
          //TODO add customer id to request body
- GET `/api/customer/getCart` //list products inside current cart
  - response body content:
    - 200 OK
      ```json
      {"products":[
        {
          "productId":"1",
          "name": "Apples",
          "producer" : "Tonio Cartonio s.p.a.",
          "unit":"kg",
          "unit price" : "1.99",
          "amount" : "10" //ordered amount 
        },
        {
          "productId":"2",
          "name": "Eggs",
          "producer" : "KFC farms", 
          "unit":"unit",
          "unit price" : "0.10",
          "amount" : "6" 
        }
      ]}
      ```
    - 404 NOT FOUND

- GET `/api/customer/getWallet` //get customer's wallet
  - request body content:
    ```json
    {
      "email":"customer@gmail.com"
    }
    ```
  - response body content:
    ```json
    {
      "wallet":"0.00"
    }
    ```
- POST `/api/customer/topUp` //top up customer's wallet
  - request body content: 
    ```json
    {
      "email":"customer@gmail.com",
      "topUp":"10.00"
    }
    ```
  - response body content:
    - 200 OK
    - 404 NOT FOUND
- POST `/api/customer/placeOrder` //create an order for the customer
  - request body content:
  ```json
  {
    "email":"customer@gmail.com",
    "order": 
      [
        {
          "productId":"1",
          "amount" : "10" //desired amount 
        },
        {
          "productId":"2",
          "amount" : "6"
        }
      ]
  }
  ```
  - response body content:
    - 200 OK
    - 404 NOT FOUND

- DELETE `/api/customer/dropOrder`
  - request body content: 
    ```json 
    {"email":"customer@gmail.com"}
    ```
  - response body content:
    - 200 OK 
    - 500 Internal server error

- GET `/api/customer/browseOrders` //browse all orders to be delivered
  - response body content:
    - 200 OK:
      ```json
      {
        "orders":
        [
          {
            "orderId":1,
            "email":"customer@gmail.com",
            "productList" : 
              [
                {
                  "productId":"1",
                  "name": "Apples",
                  "producer" : "Tonio Cartonio s.p.a.",
                  "unit":"kg",
                  "unit price" : "1.99",
                  "amount" : "10" //ordered amount 
                },
                {
                  "productId":"2",
                  "name": "Eggs",
                  "producer" : "KFC farms", 
                  "unit":"unit",
                  "unit price" : "0.10",
                  "amount" : "6" 
                }
              ] 
          }
        ]
      }
      ```
    - 404 NOT FOUND
- PUT `/api/customer/deliverOrder` //mark order as delivered
  - request body content:
    ```json
    {
      "orderId":1
    }
    ```
  - response body content:
    - 200 OK
    - 404 NOT FOUND