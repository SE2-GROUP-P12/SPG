import 'bootstrap/dist/css/bootstrap.min.css';
import Form from "react-bootstrap/Form";
import Modal from "react-bootstrap/Modal";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import Button from "react-bootstrap/Button"
import Container from "react-bootstrap/Container";
import { useState } from "react";
import { Link } from "react-router-dom";
import validator from 'validator'
import {API} from './API.js'
//pbkdf2 password handling
var pbkdf2 = require('pbkdf2');

const errorModalMessage = {
    id: 1,
    messageTitle: "500 Internal server error",
    messageText: "Something went wrong during the server processment, please retry. (500 Internal Server Error)",
    messageFooterButton: "Exit"
};
const okModalMessage = {
    id: 2,
    messageTitle: "Creation successful",
    messageText: "Everything goes well, new client created.",
    messageFooterButton: "Home"
};
const conflictModalMessage = {
    id: 3,
    messageTitle: "Email and/or SSN already present",
    messageText: "The inserted email is already present in the system, use another one.",
    messageFooterButton: "Modify data"
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
    //Validators arrow function for each field
    const emailHandlerAndChecker = (em) => {
        setEmail(em);
        if (validator.isEmail(em))
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
        if (ssn.length !== 16)
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
            password: pbkdf2.pbkdf2Sync(password, crypto.getRandomValues(new Uint32Array(10)), 1, 32, 'sha512').toString('hex'),
        };
        return JSON.stringify(requestBodyObject);
    }


 
    // Submission by RICK
    const buttonHandlerSubmission = () => {
        let jsonObj = jsonObjectBuilder();
        //FIRST: check is user is already registered based upon mail as ID(?)
        fetch('/api/customer/customerExists', {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({email: email, ssn: ssn})
        }).then(response => {
            if (response.ok)
                response.json().then(content => {
                    if (content.exist === false) {
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
                    } else
                        setModalMessage(conflictModalMessage);
                })
            else
                setModalMessage(errorModalMessage);
            setModalShow(true);
        })
    }
    // ------------------------------------------------------------
 

    /* Submission by PEPPE
    async function checkCustomer() {
        const exists = await API.customerExists(email, ssn);
        console.log (exists);
        return exists;
    }

    async function addCustomer(jsonObj) {
        const okay = await API.addCustomer(jsonObj);
        return okay;
    }

    const buttonHandlerSubmission = () => {
        let jsonObj = jsonObjectBuilder();
        //FIRST: check is user is already registered based upon mail as ID(?)
        if (checkCustomer()) {
            setModalMessage(errorModalMessage);
        }
        else {
            const okay = addCustomer(jsonObj);
            if (okay) {
                setModalMessage(okModalMessage);
            }
            else {
                setModalMessage(conflictModalMessage);
            }
        }
        setModalShow(true);
    }
    // ------------------------------------------------------------
    */

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
                        modalMessage.id !== 2 ?
                            <Button variant="secondary" onClick={() => {
                                setModalShow(false);
                                setEmail("");
                                setPassword("");
                                setSsn("");
                                setEmailValidator(false);
                                setPasswordValidator(false);
                                setSsnValidator(false);
                            }}>
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
                <h1>Registration</h1>
            </Row>
            <Row clasName="mb-4">
                <ModalComponent />
                <Form className="mt-4">
                    <Row className="mb-3">
                        {/*EMAIL FIELD*/}
                        {
                            email.length === 0 ?
                                <Form.Group as={Col} controlId="email">
                                    <Form.Label>Email*</Form.Label>
                                    <Form.Control type="email" placeholder="Enter email"
                                        onChange={(event) => emailHandlerAndChecker(event.target.value)}
                                        value={email} />
                                </Form.Group>
                                :
                                <Form.Group as={Col} controlId="email" hasValidation>
                                    <Form.Label>Email*</Form.Label>
                                    <Form.Control type="email" placeholder="Enter email"
                                        onChange={(event) => emailHandlerAndChecker(event.target.value)}
                                        value={email} required isInvalid={!emailValidator} />
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
                                        onChange={(event) => passwordHandlerAndChekcer(event.target.value)} />
                                </Form.Group>
                                :
                                <Form.Group as={Col} controlId="password">
                                    <Form.Label>Password*</Form.Label>
                                    <Form.Control type="password" placeholder="Password" value={password}
                                        onChange={(event) => passwordHandlerAndChekcer(event.target.value)}
                                        required isInvalid={!passwordValidator} />
                                    <Form.Control.Feedback type="invalid">
                                        Please enter a password with at leat 10 chars (1 lowercase, 1 uppercase and 1 number).
                                    </Form.Control.Feedback>
                                </Form.Group>
                        }
                    </Row>
                    <Row className="mb-3 mt-4 ">
                        {/*NAME FIELD*/}
                        <Form.Group as={Col} controlId="name">
                            <Form.Label>Name*</Form.Label>
                            <Form.Control type="text" placeholder="Enter name" value={name}
                                onChange={(event) => nameHandlerAndChecker(event.target.value)} />
                        </Form.Group>
                        {/*SURNAME FIELD*/}
                        <Form.Group as={Col} controlId="surname">
                            <Form.Label>Surname*</Form.Label>
                            <Form.Control type="text" placeholder="Enter surname" value={surname}
                                onChange={(event) => surnameHandlerandChecker(event.target.value)} />
                        </Form.Group>
                        {/*ADDRESS FIELD*/}
                        <Form.Group as={Col} controlId="address">
                            <Form.Label>Address*</Form.Label>
                            <Form.Control placeholder="1234 Main St" value={address}
                                onChange={(event) => addressValidatorAndChecker(event.target.value)} />
                        </Form.Group>
                    </Row>
                    {/*SSN FIELD*/}
                    <Row className="mt-4">
                        {
                            ssn.length === 0 ?
                                <Form.Group as={Col} controlId="SSN">
                                    <Form.Label>SSN*</Form.Label>
                                    <Form.Control type="text" placeholder="Enter SSN string" value={ssn}
                                        onChange={(event) => ssnValidatorAndChecker(event.target.value)} />
                                </Form.Group>
                                :
                                <Form.Group as={Col} controlId="SSN">
                                    <Form.Label>SSN*</Form.Label>
                                    <Form.Control type="text" placeholder="Enter SSN string" value={ssn}
                                        onChange={(event) => ssnValidatorAndChecker(event.target.value)}
                                        required isInvalid={!ssnValidator} />
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
                                        } />
                                </Form.Group>
                                :
                                <Form.Group as={Col} controlId="phoneNumber">
                                    <Form.Label>Phone Number</Form.Label>
                                    <Form.Control type="text" placeholder="Enter phone number" value={phoneNumber}
                                        onChange={(event) => phoneNumberValidatorAndChecker(event.target.value)}
                                        required isInvalid={!phoneNumberValidator} />
                                    <Form.Control.Feedback type="invalid">
                                        Please enter a valid phone number.
                                    </Form.Control.Feedback>
                                </Form.Group>
                        }
                    </Row>
                    {/*BUTTONs COMPONENT*/}
                    
                    <Row>
                        <Col className="mt-4">
                            <Link to="/ShopEmployee">
                                <Button variant="secondary" size="lg" className="mt-4">
                                    Back
                                </Button>
                            </Link>
                        </Col>
                        <Col className="mt-4">
                            {
                                !(emailValidator === true && passwordValidator === true &&
                                    nameValidator === true && surnameValidator === true && addressValidator === true
                                    && ssnValidator === true && phoneNumberValidator === true) ?
                                    <Button variant="success" size="lg" className="mt-4" disabled>
                                        Submit
                                    </Button>
                                    :
                                    <Button variant="success" size="lg" className="mt-4"
                                        onClick={() => buttonHandlerSubmission()}>
                                        Submit
                                    </Button>
                            }
                        </Col>
                    </Row>
                </Form>
            </Row>
        </Container>
    )
}

/**
 =======
 import './App.css';
 import Row from "react-bootstrap/Row";
 import Col from "react-bootstrap/Col";
 import Button from "react-bootstrap/Button";
 import {Link} from "react-router-dom";
 import {Formik, Form, Field} from 'formik';
 import * as Yup from 'yup';

 function NewCustomer()
 {
    return(
        <>
        <h1>Welcome!</h1>
        <h2>Please, insert some data:</h2>
        <Formik 
            initialValues={{
                name: '',
                surname: '',
                SSN: '',
                email: '',
                password: '',
                address: '',
                phoneNumber: ''
            }}
            validationSchema={
                Yup.object({
                    name: Yup.string().required('Name is required').max(20, "Name can't be longer than 20 characters"),
                    surname: Yup.string().required('Surname is required').max(20, "Surname can't be longer than 20 characters"),
                    SSN: Yup.string().required("SSN is required").min(16).max(16),
                    email: Yup.string().email().required("Email is required"),
                    password: Yup.string().required("Password is required").matches(/^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$/, "Password must be at least 8 characters and have at least one letter and one number"),
                    address: Yup.string().required("Address is required"),
                    phoneNumber: Yup.string()
                })
            }
            onSubmit={async (values) => {
                console.log("SUBMITTED: "+values.email);
            }}
            validateOnChange={false}
            validateOnBlur={false}
        >
            {({values, errors, touched})=>
            <div id="container" className="pagecontent">
                <Form>
                    <Row>
                        <Col>Name: <Field type="text" name="name" label="Name"/></Col>
                        <Col>Surname: <Field type="text" name="surname" label="Surname"/></Col>
                    </Row>
                    <Row>SSN: <Field type="text" name="SSN" label="SSN"/></Row>
                    <Row>Email: <Field type="text" name="email" label="Email"/></Row>
                    <Row>New Password: <Field type="text" name="password" label="New Password"/></Row>
                    <Row>Address: <Field type="text" name="address" label="Address"/></Row>
                    <Row>Phone Number: <Field type="text" name="phoneNumber" label="Phone Number"/></Row>
                    <Row style={{padding : "20px" }}>
                        <Col xs={6}><Button type="submit" variant="success">Submit</Button></Col>
                        <Col xs={6}><Link to="/ShopEmployee"><Button variant="secondary">Back</Button></Link></Col>
                    </Row>
                    {errors.name && touched.name ? errors.name : null}
                    {errors.surname && touched.surname ? errors.surname : null}
                    {errors.SSN && touched.SSN ? errors.SSN : null}
                    {errors.email && touched.email ? errors.email : null}
                    {errors.password && touched.password ? errors.password : null}
                    {errors.address && touched.address ? errors.address : null}
                    {errors.phoneNumber && touched.phoneNumber ? errors.phoneNumber : null}
                    </Form>
                    </div>
                    ... HAS BEEN TRUNCATED, RECOVER FROM PAST COMMITS **/

export { NewCustomer }