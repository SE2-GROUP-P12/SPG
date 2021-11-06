import 'bootstrap/dist/css/bootstrap.min.css';
import './App.css';
import {Formik, Form, Field} from 'formik';
import {Row, Col, Container} from 'react-bootstrap'
import * as Yup from 'yup';

function Login() {
    return (
        <div className="justify-content-center">
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
                    <Form>
                            <Container>
                                <Row>
                                    <Field name="email" label="Email"/>
                                </Row>
                                <Row>
                                    <Field name="password" label="Password" type="password"/>
                                </Row>
                                <Row>
                                    <button type="submit">Login</button>
                                </Row>
                                <Row>
                                    {errors.email && touched.email ? (
                                        <div>{errors.email}</div>) : null}
                                    {errors.password && touched.password ? (
                                        <div>{errors.password}</div>) : null}
                                </Row>
                            </Container>
                    </Form>
                }
            </Formik>
        </div>
    )
}

export {Login}