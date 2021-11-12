
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

async function browseProducts() {
    try {
        let listProducts;
        const response = await fetch("/api/product/all");
        listProducts = await response.json();
        if (response.ok)
            return listProducts;
        else
            return undefined;
    }
    catch (err) {
        console.log("Some error occourred");
        return undefined;
    }
}


async function getCart(data) {
    try {
        const response = await fetch("/api/customer/getCart?email=" + data.email, {
            method: 'GET',
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

async function addToCart(product) {
    try {
        const response = await fetch("/api/product/addToCart", {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
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

async function topUp(data)
{
    try{
        const response = await fetch ("/api/customer/topUp", {
            method: 'POST',
            headers: {'Content-Type' : 'application/json'},
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

async function customerExistsByMail(data)
{
    try{
        const response = await fetch ("/api/customer/customerExistsByMail?email="+data, {
            method: 'GET',
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

async function placeOrder(data)
{
    try{
        const response = await fetch ("/api/customer/placeOrder", {
            method: 'POST',
            headers: {'Content-Type' : 'application/json'},
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

async function dropOrder (data)
{
    try{
        const response = await fetch ("/api/customer/dropOrder", {
            method: 'DELETE',
            headers: {'Content-Type' : 'application/json'},
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

async function getOrdersByEmail(email){
    try {
        const response = await fetch ("api/customer/getOrdersByEmail?email="+email, {
            method: 'GET',
        });
        const data = await response.json();
        if(response.ok){
            return data;
        }
        else {
            return null;
        }
    } catch (error) {
        console.log(error);
    }
}

async function deliverOrder(orderId){
    try {
        const response = await fetch ("/api/customer/deliverOrder", {
            method: 'PUT',
            headers: {'Content-Type' : 'application/json'},
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

async function getWallet() {
    try {
        const response = await fetch("/api/product/getWallet");
        const data = await response.json();
        if (response.ok) {
            return data;
        }
    }
    catch (err) {
        console.log(err);
    }
}

async function customerExists(email, ssn) {
    try {
        const response = await fetch('/api/customer/customerExists', {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ email: email, ssn: ssn })
        })
        const data = await response.json();
        if (response.ok) {
            return data.exist;
        }
    }
    catch (err) {
        console.log(err);
    }
}

async function addCustomer(jsonObj) {
    try {
        const response = await fetch("/api/customer/addCustomer", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: jsonObj
        });
        if (response.ok){ 
            console.log("done!");
            return response.ok;
        }
    }
    catch (err) {
        console.log(err);
    }
}

const API = {browseProducts, placeOrder, addToCart, getWallet, topUp, getCart, customerExists, customerExistsByMail, dropOrder, getOrdersByEmail, deliverOrder, addCustomer};
export {API}

