import 'bootstrap/dist/css/bootstrap.min.css';
import './App.css';
import Button from "react-bootstrap/Button";
import { Link, Redirect } from "react-router-dom";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import { Formik, Form, Field } from 'formik';
import { buildLoginBody } from './Utilities';
import * as Yup from 'yup';
import { useState, useEffect } from "react";

let base64 = require('base-64');


function Login(props) {
    const [runRedirect, setRunRedirect] = useState(false);
    const [redirectPage,setRedirectPage] = useState("/");


    async function onClickSubmissionHandler(username, password) {
        //event.preventDefault();
        fetch("/api/login", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: buildLoginBody({
                'username': username,
                'password': password
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
                }
                )
            }
        });
        return true;
    }
    
    useEffect(() => {
        setRunRedirect(false)
    },[runRedirect]);

    if (runRedirect == true) {
        return (
            <Redirect to={`/${localStorage.getItem('role')}`}></Redirect>
        );
}

return (
    <>
        <h1>Login</h1>
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
                setRunRedirect(true);
            }}
            validateOnChange={false}
            validateOnBlur={false}>
            {({ values, errors, touched }) =>
                <div id="container" className="pagecontent">
                    <Form>
                        <Row>
                            Email: <Field name="email" label="Email" />
                        </Row>
                        <Row>
                            Password: <Field name="password" label="Password" type="password" />
                        </Row>
                        <Row style={{ padding: "20px" }}>

                            <Col xs={6}>
                                <Button type="submit" variant="success"
                                >Login</Button>
                            </Col>
                            <Col xs={6}><Link to="/"><Button variant="secondary">
                                Back</Button></Link></Col>
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