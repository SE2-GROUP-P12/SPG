
/* browse di RICK

async function browseProducts() {
    fetch('/api/product/all').then(response => {
        if (response.ok) {
            response.json().then((body) => {
                setProducts([...body]);
            });
        } else
            console.log("Error orrcuored -> handle redirection") //TODO: to implement redirection in case of srver errors.
        setLoadCompleted(true);
    }).catch(err => console.log(err));
}
*/

// GET the list of all the products, if error occurs it returns undefined 
async function browseProducts() {
    try {
        let listProducts;
        const response = await fetch("/api/product/all", {
            method: 'GET',
            headers: { 
                'Accept': 'application/json',
                'Content-Type': 'application/json' },
        });
        listProducts = await response.json();
        if (response.ok)
            return listProducts;
        else{
            //TODO: handle in a better way the error message
            window.location.href = "http://localhost:3000/Unauthorized";
        }
    }
    catch (err) {
        console.log("Some error occourred");
        return undefined;
    }
}

//GET the cart related to the requested user by his/her email address, if error occurs it returns undefined 
async function getCart(data) {
    try {
        const response = await fetch("/api/customer/getCart?email=" + data.email, {
            method: 'GET',
            headers: { 
                'Accept': 'application/json',
                'Content-Type': 'application/json' },
        });
        let cart = await response.json();
        if (response.ok)
            return cart;
        else
            return undefined;
    }
    catch (err) {
        console.log(err);
        return undefined;
    }
}

//POST: add products in the cart, it returns boolean
async function addToCart(product) {
    try {
        const response = await fetch("/api/product/addToCart", {
            method: 'POST',
            headers: { 
                'Accept': 'application/json',
                'Content-Type': 'application/json' },
            body: JSON.stringify(product)
        });
        if (response.ok)
            return true;
        else
            return false;
    }
    catch (err) {
        console.log("Some error occourred");
        return undefined;
    }
}

//POST: increase the amount of money (data.value) in the wallet of a customer (data.email), it returns a boolean
async function topUp(data)
{
    try{
        const response = await fetch ("/api/customer/topUp", {
            method: 'POST',
            headers: { 
                'Accept': 'application/json',
                'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });
        if(response.ok)
            return true;
        else
            return false;
    }
    catch(err) {
        console.log("Some error occourred");
        return undefined;
    }
}

//Check whether the customer exists or not by his/her email address, if error occurs it returns undefined, otherwise true
async function customerExistsByMail(email)
{
    try{
        const response = await fetch ("/api/customer/customerExistsByMail?email="+email, {
            method: 'GET',
            headers: { 
                'Accept': 'application/json',
                'Content-Type': 'application/json' },
        });
        let exists = await response.json();
        if(response.ok)
            return exists;
        else
            return undefined;
    }
    catch(err) {
        console.log(err);
        return undefined;
    }
}

//POST: place an order by the email address (data.email) of a customer (must be checked before), it returns boolean 
async function placeOrder(data)
{
    try{
        const response = await fetch ("/api/customer/placeOrder", {
            method: 'POST',
            headers: { 
                'Accept': 'application/json',
                'Content-Type': 'application/json' },
            body: JSON.stringify({'email' : data.email, 'customer' : data.customer})
        });
        if(response.ok)
            return true;
        else
            return false;
    }
    catch(err) {
        console.log(err);
        return undefined;
    }
}

//DELETE: delete the order of a customer by his/her email (data.email), it returns a boolean
//TODO: what happen if a customer has more than one order?
async function dropOrder (data)
{
    try{
        const response = await fetch ("/api/customer/dropOrder", {
            method: 'DELETE',
            headers: { 
                'Accept': 'application/json',
                'Content-Type': 'application/json' },
            body: JSON.stringify({'email' : data.email})
        });
        if(response.ok)
            return true;
        else
            return false;
    }
    catch(err) {
        console.log(err);
        return undefined;
    }
}

//GET: get a list of all orders. 
//If no order is found, return empty list. 
//If an error occours, return null
async function getAllOrders(){
    try {
        const response = await fetch ("api/customer/getAllOrders", {
            method: 'GET',
            headers: { 
                'Accept': 'application/json',
                'Content-Type': 'application/json' },
        });
        const data = await response.json();
        if(response.ok){
            if(data.length===0)
                return ([]);
            return data;
        }
        else {
            return null;
        }
    } catch (error) {
        console.log(error);
    }
} 

// GET: retrieve the list of all orders by customer's email address. 
//      If error occurs it returns null, otherwise it returns a json with orderId + List of Products
async function getOrdersByEmail(email){
    try {
        const response = await fetch ("api/customer/getOrdersByEmail?email="+email, {
            method: 'GET',
            headers: { 
                'Accept': 'application/json',
                'Content-Type': 'application/json' },
        });
        const data = await response.json();
        if(response.ok){
            console.log(data);
            return data;
        }
        else {
            return null;
        }
    } catch (error) {
        console.log(error);
    }
}

//POST: handout of an order by the orderId, it returns a boolean
async function deliverOrder(orderId){
    try {
        const response = await fetch ("/api/customer/deliverOrder", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type' : 'application/json'
        },
            body: JSON.stringify(orderId)
        });
        const data = await response.json();
        if (response.ok){
            return data;
        }
        else{
            return false;
        }
    } catch (error) {
        console.log(error);
    }
}

//GET: retrieve the total amount of a customer's wallet by his/her email address
async function getWallet(email) {
    
    try {
        const response = await fetch("/api/customer/getWallet?email="+email, {
            method: 'GET',
            headers: { 
                'Content-Type': 'application/json', 
                'Accept': 'application/json'}
        });
        const data = await response.json();
        if (response.ok) {
            return data;
        }
    }
    catch (err) {
        console.log(err);
    }
}

//GET: check whther the customer exists by its email and its SSN code, it returns a boolean
async function customerExists(data)
{
    try{
        const response = await fetch ("/api/customer/customerExists?email="+data.email+"&ssn="+data.ssn , 
        {
            method: 'GET',
            headers: { 
                'Accept':'application/json',
                'Content-Type': 'application/json' 
            } 
        });
        let answer = await response.json();
        if(response.ok)
            return answer.exist;
        else
            return undefined;
    }
    catch(err) {
        console.log(err);
        return undefined;
    }
}

//POST: register a new customer by sending all his/her info, it returns a boolean
async function addCustomer(data)
{
    console.log(JSON.stringify(data));
    try
    {
        const response = await fetch("/api/customer/addCustomer", 
        {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });
        if(response.ok)
            return true;
        return undefined;
    }
    catch(err)
    {
        console.log(err);
        return undefined;
    }
}

const API = {browseProducts, placeOrder, addToCart, getWallet, topUp, getCart, customerExists, customerExistsByMail, dropOrder, getAllOrders, getOrdersByEmail, deliverOrder, addCustomer};
export {API}

