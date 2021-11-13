```
@startuml

package entity { 
class us as "User"
{
- String user_id
- String email
- String password
- String role
}
class se as "Shop Employee"
class cust as "Customer"
{
- String name
- String surname
- String ssn
- String address
- Float wallet
}
class prod as "Product"
{
- Integer product_id
- String name
- String unitOfMeasurement
- Integer quantityInInventory
- Float price
}

class or as "Order"
{
- String order_id
}

class fa as "Farmer"
{
- String name
- String surname
- String ssn
- String address
}
}


package repository {
class prod_repo as "ProductRepo"

class us_repo as "UserRepo"

class ord_repo as "OrderRepo"
}



us <|-- se
us "(t,e)" <|-- cust
us <|-- fa
fa "1" -- "0..*" prod
cust "1" --o "0..*" or
se "1" --o "0..*" or
prod "1..*" --o "0..*" or

prod_repo -- "*" prod
ord_repo -- "*" or
us_repo -- "*" us

@enduml
```