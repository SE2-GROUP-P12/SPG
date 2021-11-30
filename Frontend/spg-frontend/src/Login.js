import 'bootstrap/dist/css/bootstrap.min.css';
import './App.css';
import Button from "react-bootstrap/Button";
import { Link, Redirect } from "react-router-dom";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import { Formik, Form, Field } from 'formik';
import { buildLoginBody, getSalt } from './Utilities';
import Alert from "react-bootstrap/Alert";
import * as Yup from 'yup';
import { useState, useEffect } from "react";

const pbkdf2 = require('pbkdf2');


function Login(props) {
    const [redirectPage, setRedirectPage] = useState("/");
    const [redirectRun, setRedirectRun] = useState(false);
    const [alertShow, setAlertShow] = useState(false);
    console.log(pbkdf2.pbkdf2Sync('password', getSalt(), 1, 32, 'sha512').toString('hex'));
    function onClickSubmissionHandler(username, password) {

        //event.preventDefault();
        fetch("/api/login", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: buildLoginBody({
                'username': username,
                'password': pbkdf2.pbkdf2Sync(password, getSalt(), 1, 32, 'sha512').toString('hex')
            })
        }).then(response => {
            if (response.ok) {
                response.json().then(body => {
                    props.setAccessToken(body['accessToken']);
                    sessionStorage.setItem('accessToken', body['accessToken']);
                    sessionStorage.setItem('refreshToken', body['refreshToken']);
                    localStorage.setItem('username', body['email']);
                    localStorage.setItem('role', body['roles']);
                    props.setLoggedFlag(true);
                    props.setLoggedUser(localStorage.getItem('username'));
                    props.setLoggedUserRole(body['roles']);
                    setRedirectRun(true);
                }
                )
            } else if (response.status === 401) {
                setAlertShow(true);
            }
        });
        return true;
    }

    function ErrorCredentialAlert() {
        return (
            <Row className="justify-content-md-center">
                <Alert xs variant="danger" onClose={() => setAlertShow(false)} dismissible>
                    <Alert.Heading>Wrong Credentials</Alert.Heading>
                    <p>
                        Please recheck your credentials.
                    </p>
                    <hr />
                    <div className="d-flex justify-content-end">
                        <Row>
                            <Button variant="outline-danger">
                                RESET
                                PASSWORD
                            </Button>
                        </Row>
                    </div>
                </Alert>
            </Row>
        );
    }

    if (redirectRun == true) {
        return (
            <Redirect to={`/${localStorage.getItem('role')}`}></Redirect>
        );
    }

    return (
        <>
            <h1>Login</h1>
            <Row>
                <Col>
                    {alertShow === true ? <ErrorCredentialAlert /> : ""}
                </Col>
            </Row>
            <Formik
                initialValues={{
                    email: "",
                    password: "",
                }}
                validationSchema={Yup.object({
                    email: Yup.string().email('Invalid email address').required('Email is required'),
                    password: Yup.string().required('Password is required').min(6, "Password is too short")
                })}
                onSubmit={async (values) => {
                    let r = await onClickSubmissionHandler(values.email, values.password);
                }}
                validateOnChange={false}
                validateOnBlur={false}>
                {({ values, errors, touched }) =>
                    <div id="container" className="pagecontent">
                        <Form>
                            <Row>
                                Email: <Field name="email" label="Email" />
                            </Row>
                            <Row className="mt-5">
                                Password: <Field name="password" label="Password" type="password" />
                            </Row>
                            <Row className="mt-5" style={{ padding: "20px" }}>
                                <Col xs={6}><Link to="/"><Button variant="secondary">
                                    Back</Button></Link></Col>
                                <Col xs={6}>
                                    <Button type="submit" variant="success"
                                    >Login</Button>
                                </Col>
                            </Row>
                            <Row>
                                {errors.email && touched.email ? (
                                    <div>{errors.email}</div>) : null}
                                {errors.password && touched.password ? (
                                    <div>{errors.password}</div>) : null}
                            </Row>
                        </Form>
                    </div>
                }
            </Formik>
        </>
    )
}

export { Login }