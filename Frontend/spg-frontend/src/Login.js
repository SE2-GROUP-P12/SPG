import 'bootstrap/dist/css/bootstrap.min.css';
import './App.css';
import Button from "react-bootstrap/Button";
import {Link} from "react-router-dom";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import {Formik, Form, Field} from 'formik';
import Container from 'react-bootstrap/Container';
import * as Yup from 'yup';

function Login() {
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
                    alert(JSON.stringify(values, null, 2));
                }}
                validateOnChange={false}
                validateOnBlur={false}>
                {({values, errors, touched}) =>
                    <div id="container" className="pagecontent">
                    <Form>
                                <Row>
                                    Email: <Field name="email" label="Email"/>
                                </Row>
                                <Row>
                                    Password: <Field name="password" label="Password" type="password"/>
                                </Row>
                                <Row style={{padding : "20px" }}>
                        <Col xs={6}><Button type="submit" variant="success">Login</Button></Col>
                        <Col xs={6}><Link to="/"><Button variant="secondary">Back</Button></Link></Col>
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

export {Login}