# API LIST
## Login 
- POST `/api/sessions` //for login
  - request parameters: {}
  - request body content: {"email":"customer@gmail.com"  , "password":"$2a$10$pHD31er2rY6Izwb9TbmxwOvU.kQp2QWJQTWDi2KmmMCGrorFXg1G6"} 
  - response body content: {"role":"customer"}

- DELETE `/api/sessions/current` //for logout
  - request parameters: {}
  - request body content: {}
  - response body content: {}

- GET `/api/sessions/current` //to check the current user
  - request parameters: {}
  - request body content: {}
  - response body content: {"email":"customer@gmail.com", "role":"customer"}
  - 
## Shop Employee
