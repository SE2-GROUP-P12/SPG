# SPG
SPG SE2_Project


Database design sprint 1
```plantuml
@startuml
Class User{
    User_id (PK): Integer
    Name: String
    Surname: Integer
    SSN: String
    Phone number: Integer
    Email: Integer
    Password: String
    Role: String
}
Class Basket{
    User_id(PK): Integer
    Product_id(PK): Integer
    Quantity: Float
}
Class Porduct{
    Product_id(PK): Integer
    Name: String
    Quantity: Integer
    Price: Float
    Unit: String
}
Class Wallet{
    User_id(PK): Integer
    Value: Float
}
User--Basket
Basket - Porduct
User-Wallet
@enduml
```
