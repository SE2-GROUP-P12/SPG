import 'bootstrap/dist/css/bootstrap.min.css';
import '../App.css';
import Button from "react-bootstrap/Button";
import {Link, Redirect} from "react-router-dom";
import {Formik, Form, Field} from 'formik';
import {buildLoginBody, getSalt} from '../Utilities';
import Alert from "react-bootstrap/Alert";
import * as Yup from 'yup';
import {useState} from "react";
import Grid from '@mui/material/Grid';
import {API} from "../API/API";


const pbkdf2 = require('pbkdf2');

function Login(props) {
    const [redirectRun, setRedirectRun] = useState(false);
    const [alertShow, setAlertShow] = useState(false);
    const [alertMessage, setAlertMessage] = useState("");

    //console.log(pbkdf2.pbkdf2Sync('password', getSalt(), 1, 32, 'sha512').toString('hex'));

    async function onClickSubmissionHandler(username, password) {
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
                response.json().then(async body => {
                        props.setAccessToken(body['accessToken']);
                        sessionStorage.setItem('accessToken', body['accessToken']);
                        sessionStorage.setItem('refreshToken', body['refreshToken']);
                        localStorage.setItem('username', body['email']);
                        localStorage.setItem('role', body['roles']);
                        localStorage.setItem('missedPickUp', body['MissedPickUp'])
                        props.setLoggedFlag(true);
                        props.setLoggedUser(localStorage.getItem('username'));
                        props.setLoggedUserRole(body['roles']);
                        let warning = await API.getWalletWarning(localStorage.getItem("username"));
                        props.setTopUpWarning(warning);
                        setRedirectRun(true);
                    }
                )
            } else if (response.status === 401) {
                response.json().then(body => setAlertMessage(body.errorMessage))
                setAlertShow(true);
            }
        });
        return true;
    }

    if (redirectRun) {
        return (
            <Redirect to="/Dashboard"></Redirect>
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
                onSubmit={(values) => {
                    setAlertShow(false);
                    onClickSubmissionHandler(values.email, values.password);
                }
                }
                validateOnChange={false}
                validateOnBlur={false}>
                {({values, errors, touched}) =>
                    <Form>
                        <Grid container spacing={2}>
                            <Grid item xs={3}/>
                            <Grid item xs={2} align="left"><label htmlFor="email">Email:</label></Grid>
                            <Grid item xs={4} align="center"><Field id="email" name="email" label="Email"
                                                                    style={{width: '100%'}}/></Grid>
                            <Grid item xs={3}/>
                            <Grid item xs={3}/>
                            <Grid item xs={2} align="left"><label htmlFor="password">Password:</label></Grid>
                            <Grid item xs={4} align="center"><Field id="password" name="password" label="Password"
                                                                    type="password" style={{width: '100%'}}/></Grid>
                            <Grid item xs={3}/>
                            <Grid item xs={3}/>
                            <Grid item xs={3}><Link to="/"><Button variant="secondary">Back</Button></Link></Grid>
                            <Grid item xs={3} align="center"><Button type="submit"
                                                                     variant="success">Login</Button></Grid>
                            <Grid item xs={3}/>
                            <Grid item xs={1}/>
                            <Grid item xs={10}>{errors.email && touched.email ?
                                <Alert variant='warning'>{errors.email}</Alert> : null}</Grid>
                            <Grid item xs={1}/>
                            <Grid item xs={1}/>
                            <Grid item xs={10}>{errors.password && touched.password ?
                                <Alert variant='warning'>{errors.password}</Alert> : null}</Grid>
                            <Grid item xs={1}/>
                            <Grid item xs={1}/>
                            <Grid item xs={10}>{alertShow === true ?
                                <Alert variant="danger">Error: {alertMessage}</Alert> : ""}</Grid>
                            <Grid item xs={1}/>
                        </Grid>
                    </Form>
                }
            </Formik>
        </>
    )
}

export {Login}