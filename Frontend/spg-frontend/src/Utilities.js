/**
 * Mapped an object type {key:valye,...} on a form body for the login request
 * Note: generally the object details is composed as follows:
 * {
 *     username : xxx@yyy.zzzz
 *     password : hashedPassword
 * }
 */
function buildLoginBody(details){
    let formBody = [];
    for(let prop in details){
        var encodedKey = encodeURIComponent(prop);
        var encodedValue = encodeURIComponent(details[prop]);
        formBody.push(encodedKey + "=" + encodedValue);
    }
    return formBody.join("&");
}

export {buildLoginBody}