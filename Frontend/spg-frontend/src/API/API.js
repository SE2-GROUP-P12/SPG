/**
 *
 * COSTANTS DEFINITION HERE
 *
 */

const getAuthenticationHeaders = () => {
    let accessToken = "Bearer " + sessionStorage.getItem("accessToken");
    let headers = {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        'Authorization': accessToken
    };
    return headers;
};

const getSessionReloadHeaders = () => {
    let refreshToken = "Bearer " + sessionStorage.getItem("refreshToken");
    let headers = {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        'Authorization': refreshToken
    };
    return headers;
};


/**
 *
 * UTILITIES HERE
 *
 */

//SELECT POSSIBLE MITIGATION BASED ON ERRORs
function selectMitigationType(errorStatus) {
    if (errorStatus == 400) {
        return "Try to perform login";
    } else if (errorStatus == 500) {
        return "retry in while, server is offline";
    }
    //Add others... else return deafult
    return "no mitigation found :(";
}

//CREATE OBJECT TO HANDLE ERRORs
//Note: since fetch is not able to handler a 5xx http status code without thorw an excpetion,
//that function permits to pass tow typer of parameter (response: all status except 5xx, status: 5xx staus)
//PLEASE SET THEM CORRECTLY IN THE CALLER!
async function createErrorHandlerObject(response, status) {
    let errorHandlerObject;
    if (response == null) {
        errorHandlerObject = {
            'status': 500,
            'errorMessage': 'server is not reachable',
            'mitigation': selectMitigationType(500)
        };
    } else {
        errorHandlerObject = {
            'status': response.status,
            'errorMessage': (await response.json())['errorMessage'],
            'mitigation': selectMitigationType(response.status)
        }
    }
    return errorHandlerObject;
}

//Create a succesfull object to pass to the Component
//The object contains the status again in order to know if render or not the data and the requested data
async function createSuccesfulHandlerObject(response) {
    const data = await response.json()
    let successfulHandlerObject = {
        'status': response.status,
        'data': data
    };
    return successfulHandlerObject;
}


/**
 *
 * FETCH DECLARATIONS HERE
 *
 */

// GET the list of all the products, if error occurs it returns undefined
async function browseProducts(setErrorMessage) {
    try {
        let listProducts;
        const response = await fetch("/api/product/all", {
            method: 'GET',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
        });
        if (response.ok) {
            //response.json().then(body => console.log(body));
            return createSuccesfulHandlerObject(response);
        } else {
            let error = await createErrorHandlerObject(response, null)
            //console.log(error);
            setErrorMessage(error);
            return null;
        }
    } catch (err) {
        let error = await createErrorHandlerObject(null, 500);
        setErrorMessage(error);
        console.log("error: " + err);
        return null;
    }
}

//GET the cart related to the requested user by his/her email address, if error occurs it returns undefined
async function getCart(data, setErrorMessage) {
    //console.log("here");
    try {
        const response = await fetch("/api/customer/getCart?email=" + data.email, {
            method: 'GET',
            headers: getAuthenticationHeaders(),
        });
        if (response.ok) {
            let cart = await response.json();
            return cart;
        } else if (response.status === 404) {
            console.log("404");
            return undefined
        } else if (response.status === 400) {
            let error = await createErrorHandlerObject(response, null)
            console.log(error);
            setErrorMessage(error);
            return null;
        }
    } catch (err) {
        let error = await createErrorHandlerObject(null, 500);
        setErrorMessage(error);
        console.log("error: " + err);
        return null;
    }
}

//POST: add products in the cart, it returns boolean
async function addToCart(product) {
    try {
        const response = await fetch("/api/product/addToCart", {
            method: 'POST',
            headers: getAuthenticationHeaders(),
            body: JSON.stringify(product)
        });
        if (response.ok)
            return true;
        else
            return false;
    } catch (err) {
        console.log("Some error occourred");
        return undefined;
    }
}

//POST: increase the amount of money (data.value) in the wallet of a customer (data.email), it returns a boolean
async function topUp(data) {
    try {
        const response = await fetch("/api/customer/topUp", {
            method: 'POST',
            headers: getAuthenticationHeaders(),
            body: JSON.stringify(data)
        });
        if (response.ok)
            return true;
        else
            return false;
    } catch (err) {
        console.log("Some error occourred");
        return undefined;
    }
}

//Check whether the customer exists or not by his/her email address, if error occurs it returns undefined, otherwise true
async function customerExistsByMail(email) {
    try {
        const response = await fetch("/api/customer/customerExistsByEmail?email=" + email, {
            method: 'GET',
            headers: getAuthenticationHeaders(),
        });
        let exists = await response.json();
        if (response.ok)
            return exists;
        else
            return undefined;
    } catch (err) {
        console.log(err);
        return undefined;
    }
}

//POST: place an order by the email address (data.email) of a customer (must be checked before), it returns boolean
async function placeOrder(data) {
    try {
        const response = await fetch("/api/customer/placeOrder", {
            method: 'POST',
            headers: getAuthenticationHeaders(),
            body: JSON.stringify({ 'email': data.email, 'customer': data.customer })
        });
        if (response.ok)
            return true;
        else
            return false;
    } catch (err) {
        console.log(err);
        return undefined;
    }
}

//DELETE: delete the order of a customer by his/her email (data.email), it returns a boolean
//TODO: what happen if a customer has more than one order?
async function dropOrder(data) {
    try {
        const response = await fetch("/api/customer/dropOrder", {
            method: 'DELETE',
            headers: getAuthenticationHeaders(),
            body: JSON.stringify({ 'email': data.email })
        });
        if (response.ok)
            return true;
        else
            return false;
    } catch (err) {
        console.log(err);
        return undefined;
    }
}

//GET: get a list of all orders.
//If no order is found, return empty list.
//If an error occours, return null
async function getAllOrders() {
    try {
        const response = await fetch("api/customer/getAllOrders", {
            method: 'GET',
            headers: getAuthenticationHeaders(),
        });
        const data = await response.json();
        if (response.ok) {
            if (data.length === 0)
                return ([]);
            return data;
        } else {
            return null;
        }
    } catch (error) {
        console.log(error);
    }
}

// GET: retrieve the list of all orders by customer's email address.
//      If error occurs it returns null, otherwise it returns a json with orderId + List of Products
async function getOrdersByEmail(email) {
    try {
        const response = await fetch("api/customer/getOrdersByEmail?email=" + email, {
            method: 'GET',
            headers: getAuthenticationHeaders(),
        });
        const data = await response.json();
        if (response.ok) {
            console.log(data);
            return data;
        } else {
            return null;
        }
    } catch (error) {
        console.log(error);
    }
}

//POST: handout of an order by the orderId, it returns a boolean
async function deliverOrder(orderId) {
    try {
        const response = await fetch("/api/customer/deliverOrder", {
            method: 'POST',
            headers: getAuthenticationHeaders(),
            body: JSON.stringify(orderId)
        });
        const data = await response.json();
        if (response.ok) {
            return data;
        } else {
            return false;
        }
    } catch (error) {
        console.log(error);
    }
}

//GET: retrieve the total amount of a customer's wallet by his/her email address
async function getWallet(email) {
    try {
        const response = await fetch("/api/customer/getWallet?email=" + email, {
            method: 'GET',
            headers: getAuthenticationHeaders()
        });
        const data = await response.json();
        //console.log(JSON.stringify(data));
        if (response.ok) {
            return data;
        }
    } catch (err) {
        console.log(err);
    }
}

async function getWalletWarning(email){
    try {
        console.log("CHECKPOINT: "+email);
        const response = await fetch("/api/customer/retrieveError?email="+email,{
            method: 'GET',
            headers: getAuthenticationHeaders()
        });
        const data = await response.json();
        return data;
    } catch (error) {
        console.log(error);
    }
}

//GET: check whther the customer exists by its email and its SSN code, it returns a boolean
async function customerExists(data) {
    try {
        const response = await fetch("/api/customer/customerExists?email=" + data.email + "&ssn=" + data.ssn,
            {
                method: 'GET',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                }
            });
        let answer = await response.json();
        if (response.ok)
            return answer.exist;
        else
            return undefined;

    } catch (err) {
        console.log(err);
        return undefined;
    }
}



//POST: register a new customer by sending all his/her info, it returns a boolean
async function addCustomer(data) {
    //console.log(JSON.stringify(data));
    try {
        const response = await fetch("/api/customer/addCustomer",
            {
                method: 'POST',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(data)
            });
        if (response.status === 201) {
            let res = await response.json();
            console.log(res);
            return res;
        }
        return undefined;

    } catch
        (err) {
        console.log(err);
        return undefined;
    }
}


async function sessionReloader() {
    try {
        const response = await fetch("/api/token/refresh", {
            method: 'GET',
            headers: getSessionReloadHeaders(),
        });
        if (response.ok) {
            //console.log("done!");
            return (await response.json());
        } else {
            return null;
        }
    } catch (err) {
        console.log(err);
    }
}

const API = {
    browseProducts,
    placeOrder,
    addToCart,
    getWallet,
    topUp,
    getCart,
    customerExists,
    customerExistsByMail,
    dropOrder,
    getAllOrders,
    getOrdersByEmail,
    deliverOrder,
    addCustomer,
    sessionReloader,
    getWalletWarning
};
export { API }

