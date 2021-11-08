import 'bootstrap/dist/css/bootstrap.min.css';
import Form from "react-bootstrap/Form";
import Modal from "react-bootstrap/Modal";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import Button from "react-bootstrap/Button"
import Container from "react-bootstrap/Container";
import {useState} from "react";
import {Link} from "react-router-dom";


const errorModalMessage = {
    id: 1,
    messageTitle: "500 INTERNAL SERVER ERROR",
    messageText: "Something went wrong during the server processment, please retry. (500 Internal Server Error)",
    messageFooterButton: "exit"
};
const okModalMessage = {
    id: 2,
    messageTitle: "CREATION SUCCESFULL",
    messageText: "Everything goes well, new client created.",
    messageFooterButton: "back home"
};
const conflictModalMessage = {
    id: 3,
    messageTitle: "EMAIL ALREADY PRESENT",
    messageText: "The inserted email is already present in the system, use another one.",
    messageFooterButton: "modify data"
};

function NewCustomer() {
    //Use state form fields
    const [name, setName] = useState(""); //MUST BE NOT EMPTY
    const [surname, setSurname] = useState(""); //MUST BE NOT EMPTY
    const [email, setEmail] = useState(""); // MUST CONTAIN AT LEAST ONE '.' AND A '@'
    const [password, setPassword] = useState(""); // MUST BE AT LEAST 10 CHARS LONG, CONTAINS A UPPERCASE CHAR AND A NUMBER
    const [address, setAddress] = useState(""); // MUST BE NOT EMPTY
    const [ssn, setSsn] = useState(""); //TODO: See some controls and implement them....
    const [phoneNumber, setPhoneNumber] = useState(""); // MUST HAVE AT LEAST 9 DIGIT
    //Use state varaibles to validate input data
    const [emailValidator, setEmailValidator] = useState(false);
    const [passwordValidator, setPasswordValidator] = useState(false);
    const [nameValidator, setNameValidator] = useState(false);
    const [surnameValidator, setSurnameValidator] = useState(false);
    const [addressValidator, setAddressValidator] = useState(false);
    const [ssnValidator, setSsnValidator] = useState(false);
    const [phoneNumberValidator, setPhoneNumberValidator] = useState(true);
    //Modal operations state
    const [modalShow, setModalShow] = useState(false);
    const [modalMessage, setModalMessage] = useState({});
    //Validators rarrow function for each field
    const emailHandlerAndChecker = (email) => {
        setEmail(email);
        if (email.includes("@") && email.includes("."))
            setEmailValidator(true);
        else
            setEmailValidator(false);
    }

    const passwordHandlerAndChekcer = (password) => {
        setPassword(password);
        const reCheckNumberPresence = /\d/;
        const reCheckUpperCasePresence = /[A-Z]/;
        if (password.length >= 10 && reCheckNumberPresence.test(password) && reCheckUpperCasePresence.test(password))
            setPasswordValidator(true);
        else
            setPasswordValidator(false);
    }

    const nameHandlerAndChecker = (name) => {
        setName(name);
        if (name !== "")
            setNameValidator(true);
        else
            setNameValidator(false);
    }

    const surnameHandlerandChecker = (surname) => {
        setSurname(surname);
        if (surname !== "")
            setSurnameValidator(true);
        else
            setSurnameValidator(false);
    }

    const addressValidatorAndChecker = (address) => {
        setAddress(address);
        if (address !== "")
            setAddressValidator(true);
        else
            setAddressValidator(false);
    }

    const ssnValidatorAndChecker = (ssn) => {
        setSsn(ssn);
        if (ssn.length != 16)
            setSsnValidator(false);
        else
            setSsnValidator(true);
    }

    const phoneNumberValidatorAndChecker = (phoneNumber) => {
        setPhoneNumber(phoneNumber);
        if (phoneNumber.length === 0) {
            setPhoneNumberValidator(true);
            return;
        }
        if (phoneNumber.length === 10 && !isNaN(phoneNumber))
            setPhoneNumberValidator(true);
        else
            setPhoneNumberValidator(false);
    }

    //JsonParserForBE
    const jsonObjectBuilder = () => {
        let requestBodyObject = {
            name: name,
            surname: surname,
            address: address,
            ssn: ssn,
            phoneNumber: phoneNumber,
            role: "CUSTOMER",
            email: email,
            password: password, //NOT IN CLEAR!!!!!
        };
        return JSON.stringify(requestBodyObject);
    }

    const buttonHandlerSubmission = () => {
        let jsonObj = jsonObjectBuilder();
        //FIRST: check is user is already registered based upon mail as ID(?)
        fetch('/api/customer/customerExists', {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({email: email})
        }).then(response => {
            if (response.ok)
                response.json().then(content => {
                    if (content.exist === false){
                        console.log(content);
                        fetch("/api/customer/addCustomer", {
                            method: 'POST',
                            headers: {
                                'Accept': 'application/json',
                                'Content-Type': 'application/json'
                            },
                            body: jsonObj
                        }).then(() => console.log("done!"));
                    setModalMessage(okModalMessage);
                    }
                else
                    setModalMessage(conflictModalMessage);
                })
            else
                setModalMessage(errorModalMessage);
            setModalShow(true);
        })
    }

    function ModalComponent() {
        return (
            <Modal show={modalShow}
                   onHide={() => setModalShow(false)}>
                <Modal.Header>
                    <Modal.Title>
                        <h2 className="text-center">
                            {modalMessage.messageTitle}
                        </h2>
                    </Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Container>
                        <Row>
                            {modalMessage.messageText}
                        </Row>
                    </Container>
                </Modal.Body>
                <Modal.Footer>
                    {
                        modalMessage.id === 3 ?
                            <Button variant="danger" onClick={() => setModalShow(false)}>
                                {modalMessage.messageFooterButton}
                            </Button>
                            :
                            <Link to="/ShopEmployee">
                                <Button variant="danger">
                                    {modalMessage.messageFooterButton}
                                </Button>
                            </Link>
                    }
                </Modal.Footer>
            </Modal>
        )
    }

    return (
        <Container className="mt-2">
            <Row>
                <h1>CREATE A NEW CUSTOMER</h1>
            </Row>
            <Row clasName="mb-4">
                <ModalComponent/>
                <Form className="mt-4">
                    <Row className="mb-3">
                        {/*EMAIL FIELD*/}
                        {
                            email.length === 0 ?
                                <Form.Group as={Col} controlId="email">
                                    <Form.Label>Email*</Form.Label>
                                    <Form.Control type="email" placeholder="Enter email"
                                                  onChange={(event) => emailHandlerAndChecker(event.target.value)}
                                                  value={email}/>
                                </Form.Group>
                                :
                                <Form.Group as={Col} controlId="email" hasValidation>
                                    <Form.Label>Email*</Form.Label>
                                    <Form.Control type="email" placeholder="Enter email"
                                                  onChange={(event) => emailHandlerAndChecker(event.target.value)}
                                                  value={email} required isInvalid={!emailValidator}/>
                                    <Form.Control.Feedback type="invalid">
                                        Please enter a valid email.
                                    </Form.Control.Feedback>
                                </Form.Group>
                        }
                        {/*PASSWORD FIELD*/}
                        {
                            password.length === 0 ?
                                <Form.Group as={Col} controlId="password">
                                    <Form.Label>Password*</Form.Label>
                                    <Form.Control type="password" placeholder="Password" value={password}
                                                  onChange={(event) => passwordHandlerAndChekcer(event.target.value)}/>
                                </Form.Group>
                                :
                                <Form.Group as={Col} controlId="password">
                                    <Form.Label>Password*</Form.Label>
                                    <Form.Control type="password" placeholder="Password" value={password}
                                                  onChange={(event) => passwordHandlerAndChekcer(event.target.value)}
                                                  required isInvalid={!passwordValidator}/>
                                    <Form.Control.Feedback type="invalid">
                                        Please enter a password with at leat 10 chars.
                                    </Form.Control.Feedback>
                                </Form.Group>

                        }
                    </Row>
                    <Row className="mb-3 mt-4 ">
                        {/*NAME FIELD*/}
                        <Form.Group as={Col} controlId="name">
                            <Form.Label>Name*</Form.Label>
                            <Form.Control type="text" placeholder="Enter name" value={name}
                                          onChange={(event) => nameHandlerAndChecker(event.target.value)}/>
                        </Form.Group>
                        {/*SURNAME FIELD*/}
                        <Form.Group as={Col} controlId="surname">
                            <Form.Label>Surname*</Form.Label>
                            <Form.Control type="text" placeholder="Enter surname" value={surname}
                                          onChange={(event) => surnameHandlerandChecker(event.target.value)}/>
                        </Form.Group>
                        {/*ADDRESS FIELD*/}
                        <Form.Group as={Col} controlId="address">
                            <Form.Label>Address*</Form.Label>
                            <Form.Control placeholder="1234 Main St" value={address}
                                          onChange={(event) => addressValidatorAndChecker(event.target.value)}/>
                        </Form.Group>
                    </Row>
                    {/*SSN FIELD*/}
                    <Row className="mt-4">
                        {
                            ssn.length === 0 ?
                                <Form.Group as={Col} controlId="SSN">
                                    <Form.Label>SSN*</Form.Label>
                                    <Form.Control type="text" placeholder="Enter SSN string" value={ssn}
                                                  onChange={(event) => ssnValidatorAndChecker(event.target.value)}/>
                                </Form.Group>
                                :
                                <Form.Group as={Col} controlId="SSN">
                                    <Form.Label>SSN*</Form.Label>
                                    <Form.Control type="text" placeholder="Enter SSN string" value={ssn}
                                                  onChange={(event) => ssnValidatorAndChecker(event.target.value)}
                                                  required isInvalid={!ssnValidator}/>
                                    <Form.Control.Feedback type="invalid">
                                        Please enter a valid ssn.
                                    </Form.Control.Feedback>
                                </Form.Group>
                        }
                    </Row>
                    {/*PHONE NUMBER FIELD*/}
                    <Row className="mt-4">
                        {
                            phoneNumber.length === 0 ?
                                <Form.Group as={Col} controlId="phoneNumber">
                                    <Form.Label>Phone Number</Form.Label>
                                    <Form.Control type="text" placeholder="Enter phone number" value={phoneNumber}
                                                  onChange={(event) =>
                                                      phoneNumberValidatorAndChecker(event.target.value)
                                                  }/>
                                </Form.Group>
                                :
                                <Form.Group as={Col} controlId="phoneNumber">
                                    <Form.Label>Phone Number</Form.Label>
                                    <Form.Control type="text" placeholder="Enter phone number" value={phoneNumber}
                                                  onChange={(event) => phoneNumberValidatorAndChecker(event.target.value)}
                                                  required isInvalid={!phoneNumberValidator}/>
                                    <Form.Control.Feedback type="invalid">
                                        Please enter a valid phone number.
                                    </Form.Control.Feedback>
                                </Form.Group>
                        }
                    </Row>
                    {/*BUTTON COMPONENT*/}
                    <Row>
                        <Col className="mt-4">
                            {
                                !(emailValidator === true && passwordValidator === true &&
                                    nameValidator === true && surnameValidator === true && addressValidator === true
                                    && ssnValidator === true && phoneNumberValidator === true) ?
                                    <Button variant="success" size="lg" className="mt-4" disabled>
                                        SUBMIT
                                    </Button>
                                    :
                                    <Button variant="success" size="lg" className="mt-4"
                                            onClick={() => buttonHandlerSubmission()}>
                                        SUBMIT
                                    </Button>
                            }
                        </Col>
                    </Row>
                </Form>
            </Row>
        </Container>
    )
}


export {NewCustomer}