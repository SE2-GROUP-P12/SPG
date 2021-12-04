import CartImg from './resources/cart.jpg'
import OrderImg from './resources/orders.jpg'
import WalletImg from './resources/wallett.jpg'
import SettingImg from './resources/settings.jpg'

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
function getCustomerServices(){
    //Customer services
    const customerServices = [
        {
            "type": "Your Cart",
            "description": "Get your current cart: modifiy, add, remove and handle youritems.",
            "imageUrl": CartImg,
            "buttonLabel" : "show cart"
        },
        {
            "type": "Your Orders",
            "description": "Handle your orders and check tehir status in a simple way",
            "imageUrl": OrderImg,
            "buttonLabel" : "show orders"
        },
        {
            "type": "Your Wallet",
            "description": "Handle you online wallet: recharge and place the orders as you want",
            "imageUrl": WalletImg,
            "buttonLabel" : "show wallet operation"
        },
        {
            "type": "Settings",
            "description": "Handle you oaccount: change your shipping information, reset password and many more in one place",
            "imageUrl": SettingImg,
            "buttonLabel" : "go to setting"
        }
    ];
    return customerServices;
}

//Exporting functions
export {
    buildLoginBody,
    getSalt,
    getCustomerServices,
}