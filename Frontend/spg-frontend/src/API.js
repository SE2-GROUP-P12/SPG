async function browseProducts(){
    try{
        let listProducts;
        console.log("CHECKPOINT 1");
        const response = await fetch ("/api/product/all");
        console.log("CHECKPOINT 2");
        listProducts = await response.json();
        console.log("CHECKPOINT 3");
        if(response.ok)
            return listProducts;
        else
            return undefined;
    }
    catch (err) {
        console.log("Some error occourred");
        return undefined;
    }
}

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
            return undefined;
    }
    catch(err) {
        console.log("Some error occourred");
        return undefined;
    }
}

const API = {browseProducts, addToCart};
export {API}