
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

async function getCart() {
    try {
        const response = await fetch("/api/product/getCart");
        const data = await response.json();
        if (response.ok) {
            return data;
        }
    }
    catch (err) {
        console.log(err);
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
            return data;
        }
    }
    catch (err) {
        console.log(err);
    }
}
async function addCustomer() {
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
        }
    }
    catch (err) {
        console.log(err);
    }
}

const API = { browseProducts, getCart, addToCart, getWallet };
export { API }