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


const API = {/*browseProducts,*/ addToCart, getWallet};
export {API}