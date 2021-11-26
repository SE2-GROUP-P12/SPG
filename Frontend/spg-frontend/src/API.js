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
    let successfulHandlerObject = {
        'status': response.status,
        'data': await response.json()
    };
    return successfulHandlerObject;
}


/**
 *
 * FETCH DECLARATIONS HERE
 *
 */

async function browseProducts(setErrorMessage) {
    try {
        let listProducts;
        const response = await fetch("/api/product/all", {
            method: 'GET',
            headers: getAuthenticationHeaders(),
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


async function getCart(data, setErrorMessage) {
    console.log("here");
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

async function customerExistsByMail(data) {
    try {
        const response = await fetch("/api/customer/customerExistsByMail?email=" + data, {
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

async function placeOrder(data) {
    try {
        const response = await fetch("/api/customer/placeOrder", {
            method: 'POST',
            headers: getAuthenticationHeaders(),
            body: JSON.stringify({'email': data.email})
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

async function dropOrder(data) {
    try {
        const response = await fetch("/api/customer/dropOrder", {
            method: 'DELETE',
            headers: getAuthenticationHeaders(),
            body: JSON.stringify({'email': data.email})
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

async function getWallet(email) {

    try {
        const response = await fetch("/api/customer/getWallet?email=" + email, {
            method: 'GET',
            headers: getAuthenticationHeaders()
        });
        const data = await response.json();
        console.log(JSON.stringify(data));
        if (response.ok) {
            return data;
        }
    } catch (err) {
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
            body: JSON.stringify({email: email, ssn: ssn})
        })
        const data = await response.json();
        if (response.ok) {
            return data.exist;
        }
    } catch (err) {
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
        if (response.ok) {
            console.log("done!");
            return response.ok;
        }
    } catch (err) {
        console.log(err);
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
    getOrdersByEmail,
    deliverOrder,
    addCustomer,
    sessionReloader
};
export {API}

