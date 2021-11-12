/* QUESTA L'HA GIA' FATTA RICK

async function browseProducts(){
    try{
        let listProducts;
        const response = await fetch ("/api/product/all");
        listProducts = await response.json();
        if(response.ok)
            return listProducts;
        else
            return undefined;
    }
    catch (err) {
        console.log("Some error occourred");
        return undefined;
    }
}*/

async function addToCart(product)
{
    console.log(JSON.stringify(product))
    try{
        const response = await fetch ("/api/product/addToCart", {
            method: 'POST',
            headers: {'Content-Type' : 'application/json'},
            body: JSON.stringify(product)
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

async function getWallet(data)
{
    try{
        const response = await fetch ("/api/customer/getWallet?email="+data.email, {
            method: 'GET',
        });
        let wallet = await response.json();
        if(response.ok)
            return wallet;
        else
            return undefined;
    }
    catch(err) {
        console.log(err);
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

async function getCart(data)
{
    try{
        const response = await fetch ("/api/customer/getCart?email="+data.email, {
            method: 'GET',
        });
        let cart = await response.json();
        if(response.ok)
            return cart;
        else
            return undefined;
    }
    catch(err) {
        console.log(err);
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


const API = {/*browseProducts,*/ placeOrder, addToCart, getWallet, topUp, getCart, customerExistsByMail, dropOrder, getOrdersByEmail, deliverOrder};
export {API}