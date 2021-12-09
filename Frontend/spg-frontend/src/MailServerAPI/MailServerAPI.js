/**
 * Mail server APIs
 */
//Authentication headers
const getAuthenticationHeaders = () => {
    let accessToken = "Bearer " + sessionStorage.getItem("accessToken");
    return {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        'Authorization': accessToken
    };
};

//Solicit customer top-up for orders
async function solicitCustomerTopUp(senderMail, receiverMail) {
    try {
        const bodyJsonData = {
            'sender': senderMail,
            'recipient': receiverMail,
            'mailBody': ""
        }
        const response = await fetch("/api/mail/solicitTopUp ",
            {
                method: 'POST',
                headers: getAuthenticationHeaders(),
                body: JSON.stringify(bodyJsonData)
            });
        if (response.ok) {
            return true;
        }
        return false;
    } catch (error) {
        console.log("Error: " + error);
        return false;
    }
}

async function getPendingOrdersMail() {
    try {
        const response = await fetch("/api/mail/getPendingOrdersEmail",
            {
                method: 'GET',
                headers: getAuthenticationHeaders(),
            });
        if (response.ok) {
            const data = await response.json();
            return data;
        }
    } catch (error) {
        console.log("Error: " + error);
        return undefined;
    }
}

const MailServerAPI = {
    solicitCustomerTopUp,
    getPendingOrdersMail,
};

export {MailServerAPI}