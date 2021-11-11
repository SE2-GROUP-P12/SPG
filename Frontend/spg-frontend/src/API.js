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

async function dropOrder (data)
{
    console.log("CHECKPOINT API: "+JSON.stringify(data));
    try{
        const response = await fetch ("/api/customer/dropOrder", {
            method: 'DELETE',
            headers: {'Content-Type' : 'application/json'},
            body: JSON.stringify(data)
        });
        let success = await response.json();
        if(response.ok)
            return success;
        else
            return undefined;
    }
    catch(err) {
        console.log(err);
        return undefined;
    }
}

const API = {/*browseProducts,*/ addToCart, getWallet, topUp, getCart, customerExistsByMail, dropOrder};
export {API}