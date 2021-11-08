import 'bootstrap/dist/css/bootstrap.min.css';
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
            }
        </Formik>
        </>
    );
}

export {NewCustomer}