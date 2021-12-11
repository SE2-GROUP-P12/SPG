import CartImg from './resources/cart.jpg'
import OrderImg from './resources/orders.jpg'
import WalletImg from './resources/wallett.jpg'
import SettingImg from './resources/settings.jpg'
import ConfirmImg from './resources/confirmImg.jpg'
import browseProd from './resources/browseProd.jpg'
import topUp from './resources/topUp.jpg'
import handleOrders from './resources/handleOrders.jpg'
import registration from './resources/registration.jpg'
import estimations from './resources/estimations.jpg'
import addProd from './resources/addProd.jpg'

/**
 * Mapped an object type {key:value,...} on a form body for the login request
 * Note: generally the object details is composed as follows:
 * {
 *     username : xxx@yyy.zzzz
 *     password : hashedPassword
 * }
 */
function buildLoginBody(details) {
    let formBody = [];
    for (let prop in details) {
        var encodedKey = encodeURIComponent(prop);
        var encodedValue = encodeURIComponent(details[prop]);
        formBody.push(encodedKey + "=" + encodedValue);
    }
    return formBody.join("&");
}

/**
 *
 * @returns {string} should be a good entrophy source
 */
function getSalt() {
    return '!.@su^,!H!C<eHsLM-Am;`]fR~?%c3EN'.toString('hex');
}

/**
 *
 * @returns Object with all customer services:
 * the return object mapping is a one layer vector of object
 * {
 * "type" : "name of operations",
 * "description" : "short description of the operation",
 * "imageUrl" : "card button url images",
 * "button label" : "what to show on the button",
 * }
 */
function getAllServices() {
    //Customer services
    const customerServices = [
        /*PLACE ORDER*/
        {
            "type": "Your Cart",
            "description": "Get your current cart: modifiy, add, remove and handle your items.",
            "imageUrl": CartImg,
            "buttonLabel": "Show Cart",
            "linkUrl": "/PlaceOrder",
            "rolesPermitted": ['CUSTOMER', 'ADMIN', 'EMPLOYEE']
        },
        /*CUSTOMER ORDERS*/
        {
            "type": "Your Orders",
            "description": "Handle your orders and check tehir status in a simple way",
            "imageUrl": OrderImg,
            "buttonLabel": "show orders",
            "linkUrl": "/Customer/Orders",
            "rolesPermitted": ['CUSTOMER', 'ADMIN']
        },
        /*BROWSE PRODUCT*/
        {
            "type": "Browse Products",
            "description": "Browse a huge line of item in out online store!",
            "imageUrl": browseProd,
            "buttonLabel": "Browse Products",
            "linkUrl": "/BrowseProducts",
            "rolesPermitted": ['CUSTOMER', 'ADMIN', 'EMPLOYEE']
        },
        /*CUSTOMER WALLET*/
        {
            "type": "Your Wallet",
            "description": "Handle you online wallet: recharge and place the orders as you want",
            "imageUrl": WalletImg,
            "buttonLabel": "show wallet operation",
            "linkUrl": "/Customer/WalletOperations",
            "rolesPermitted": ['CUSTOMER', 'ADMIN']
        },
        /*NEW CUSTOMER*/
        {
            "type": "New Customer",
            "description": "Register new customer and then make people join us",
            "imageUrl": registration,
            "buttonLabel": "Resgister Customer",
            "linkUrl": "/NewCustomer",
            "rolesPermitted": ["ADMIN", "EMPLOYEE"]
        },
        /*TOP UP*/
        {
            "type": "Top Up",
            "description": "Top up customer wallet, choose among several payments methods",
            "imageUrl": topUp,
            "buttonLabel": "Top Up",
            "linkUrl": "/TopUp",
            "rolesPermitted": ['ADMIN', 'EMPLOYEE']
        },
        /*DELIVER ORDER*/
        {
            "type": "Handle Order",
            "description": "Deliver Order to a customer and hanlde pending orders",
            "imageUrl": handleOrders,
            "buttonLabel": "Handle Orders",
            "linkUrl": "/DeliverOrder",
            "rolesPermitted": ['ADMIN', 'EMPLOYEE']
        },
        /*FORECAST PRODUCTS*/
        {
            "type": "Forecast Product",
            "description": "Update your current estimation for the product you want to sell",
            "imageUrl": estimations,
            "buttonLabel": "Forecast Products",
            "linkUrl": "/ProductsForecast",
            "rolesPermitted": ['ADMIN', 'FARMER']
        },
        /*ADD PRODUCT*/
        {
            "type": "Add Product",
            "description": "Add new products in our inventory: propose something new to your customers",
            "imageUrl": addProd,
            "buttonLabel": "Add Product",
            "linkUrl": "/AddProduct",
            "rolesPermitted": ['FARMER', 'ADMIN']
        },
        /*CONFIRM AVAILABILITY*/
        {
            "type": "Confirm Availability",
            "description": "Confirm the availability for your forecasted products",
            "imageUrl": ConfirmImg,
            "buttonLabel": "Confirm Availability",
            "linkUrl": "/ConfirmAvailability",
            "rolesPermitted": ['FARMER']
        },
        /*USER SETTINGS*/
        {
            "type": "Settings",
            "description": "Handle you account: change your data, reset password and many more in one place",
            "imageUrl": SettingImg,
            "buttonLabel": "Settings",
            "linkUrl": "/Settings",
            "rolesPermitted": ['CUSTOMER', 'ADMIN', 'FARMER', 'EMPLOYEE']
        }
    ];
    return customerServices;
}

//Exporting functions
export {
    buildLoginBody,
    getSalt,
    getAllServices,
}