import 'bootstrap/dist/css/bootstrap.min.css';
import Modal from "react-bootstrap/Modal";
import Row from "react-bootstrap/Row";
import Button from "react-bootstrap/Button"
import Container from "react-bootstrap/Container";
import { useState } from "react";
import { Link } from "react-router-dom";
import {API} from './API.js'
import {Formik, Form, Field} from 'formik';
import * as Yup from 'yup';
import Grid from '@mui/material/Grid';
import Alert from 'react-bootstrap/Alert';
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
    const [modalShow, setModalShow] = useState(false);
    const [modalMessage, setModalMessage] = useState({});

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
                            <Button variant="secondary" onClick={() => {setModalShow(false);}}>
                                {modalMessage.messageFooterButton}
                            </Button>
                            :
                            <Link to="/ShopEmployee">
                            <Button variant="success">
                                {modalMessage.messageFooterButton}
                            </Button>
                            </Link>
                    }
                </Modal.Footer>
            </Modal>
        );
    }

    return (
        <>
            <h1>Register</h1>
            <Formik
                initialValues={{
                    email: '',
                    password: '',
                    name: '',
                    surname: '',
                    address: '',
                    SSN: '',
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
                    let requestBodyObject = {
                        name: values.name,
                        surname: values.surname,
                        address: values.address,
                        ssn: values.SSN,
                        phoneNumber: values.phoneNumber,
                        role: "CUSTOMER",
                        email: values.email,
                        password: pbkdf2.pbkdf2Sync(values.password, crypto.getRandomValues(new Uint32Array(10)), 1, 32, 'sha512').toString('hex')
                    };
                    let exists = await API.customerExists({"email": values.email, "ssn": values.SSN});
                    if(exists === undefined)
                        setModalMessage(errorModalMessage);
                    else if (exists === true)
                        setModalMessage(conflictModalMessage);
                    else
                    {
                        let added = await API.addCustomer(requestBodyObject);
                        if(added === undefined || added === false)
                            setModalMessage(errorModalMessage);
                        else
                            setModalMessage(okModalMessage);
                    }    
                    setModalShow(true);
                }}
                validateOnBlur={false}
                validateOnChange={false}
            >
                {({values, errors, touched})=>
                <Form>
                {errors.email && touched.email ? <Alert variant='danger'>{errors.email}</Alert> : null}
                {errors.password && touched.password ? <Alert variant='danger'>{errors.password}</Alert> : null}
                {errors.name && touched.name ? <Alert variant='danger'>{errors.name}</Alert> : null}
                {errors.surname && touched.surname ? <Alert variant='danger'>{errors.surname}</Alert> : null}
                {errors.address && touched.address ? <Alert variant='danger'>{errors.address}</Alert> : null}
                {errors.SSN && touched.SSN ? <Alert variant='danger'>{errors.SSN}</Alert> : null}
                {errors.phoneNumber && touched.phoneNumber ? <Alert variant='danger'>{errors.phoneNumber}</Alert> : null}
                <ModalComponent/>
                    <Grid container align='center'>
                        {/*EMAIL FIELD*/}
                        <Grid item xs={1} sm={2}/>
                        <Grid item xs={4} sm={2} align='left'>Email*</Grid>
                        <Grid item xs={6}><Field type="email" name="email" label="Email" style={{width: '100%'}}/></Grid>
                        <Grid item xs={1} sm={2}/>
                        {/*PASSWORD FIELD*/}
                        <Grid item xs={1} sm={2}/>
                        <Grid item xs={4} sm={2} align='left'>Password*</Grid>
                        <Grid item xs={6}><Field type="password" name="password" label="Password" style={{width: '100%'}}/></Grid>
                        <Grid item xs={1} sm={2}/>
                        {/*NAME FIELD*/}
                        <Grid item xs={1} sm={2}/>
                        <Grid item xs={4} sm={2} align='left'>Name*</Grid> 
                        <Grid item xs={6}><Field type="text" name="name" label="Name" style={{width: '100%'}}/></Grid>
                        <Grid item xs={1} sm={2}/>
                        {/*SURNAME FIELD*/}
                        <Grid item xs={1} sm={2}/>
                        <Grid item xs={4} sm={2} align='left'>Surname*</Grid>
                        <Grid item xs={6}><Field type="text" name="surname" label="Surname" style={{width: '100%'}}/></Grid>
                        <Grid item xs={1} sm={2}/>
                        {/*ADDRESS FIELD*/}
                        <Grid item xs={1} sm={2}/>
                        <Grid item xs={4} sm={2} align='left'>Address*</Grid>
                        <Grid item xs={6}><Field type="text" name="address" label="Address" style={{width: '100%'}}/></Grid>
                        <Grid item xs={1} sm={2}/>
                        {/*SSN FIELD*/}
                        <Grid item xs={1} sm={2}/>
                        <Grid item xs={4} sm={2} align='left'>SSN*</Grid>
                        <Grid item xs={6}><Field type="text" name="SSN" label="SSN" style={{width: '100%'}}/></Grid>
                        <Grid item xs={1} sm={2}/>
                        {/*PHONE NUMBER FIELD*/}
                        <Grid item xs={1} sm={2}/>
                        <Grid item xs={4} sm={2} align='left'>Phone Number</Grid>
                        <Grid item xs={6}><Field type="text" name="phoneNumber" label="PhoneNumber" style={{width: '100%'}}/></Grid>
                        <Grid item xs={1} sm={2}/>
                    {/*BUTTONs COMPONENT*/}
                        <Grid item xs={6}>
                            <Link to="/ShopEmployee"><Button variant="secondary" size="lg" className="mt-4">Back</Button></Link>
                        </Grid>
                        <Grid item xs={6}>
                            <Button type='Submit' variant="success" size="lg" className="mt-4">Submit</Button>
                        </Grid>
                    </Grid>
                    </Form>
            }
            </Formik>
        </>
    );
}

export { NewCustomer }