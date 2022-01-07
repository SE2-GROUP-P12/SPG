import 'bootstrap/dist/css/bootstrap.min.css';
import '../App.css';
import Row from 'react-bootstrap/Row';
import {useState} from 'react';
import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import { Formik, Form, Field, FastField } from "formik";
import * as Yup from "yup";
import { Link } from 'react-router-dom';
import { API } from "../API/API";
import Alert from 'react-bootstrap/Alert';
import Grid from '@mui/material/Grid';

function TopUp() {
    const [user, setUser] = useState(null);
    const [wallet, setWallet] = useState(0.00);
    const [show, setShow] = useState(false);
    const [error, setError] = useState(null);
    const [success, setSuccess] = useState(null);
    const [modalError, setModalError] = useState(null);
    const [modalSuccess, setModalSuccess] = useState(null);
    const handleClose = () => setShow(false);
    const handleShow = () => setShow(true);

    return (
        <>
            <h1>Top Up!</h1>
            <Formik
                initialValues={{
                    email: ''
                }}
                validationSchema={
                    Yup.object({
                        email: Yup.string().email().required()
                    })}
                onSubmit={async (values) => {
                    setWallet(0);
                    setSuccess(false);
                    setError(false);
                    setModalError(false);
                    setModalSuccess(false);
                    setUser(null);
                    let outcome = await API.getWallet(values.email);
                    if (outcome === undefined)
                        setError(true);
                    else {
                        setWallet(outcome);
                        setUser(values.email);
                        setSuccess(true);
                    }
                }}
                validateOnChange={false}
                validateOnBlur={false}
            >
                {({ values, errors, touched }) =>
                    <div id="container" className="pagecontent">
                        <Form>
                            <Row><label htmlFor="email">Email:</label> <Field id="email" name="email" type="text" /></Row>
                            <Button id='button-Submit' style={{ margin: '20px' }} type='submit' variant='success'>Submit</Button>

                            <Row>{errors.email && touched.email ? errors.email : null}</Row>
                            {error ? <Alert variant='danger'>User not found</Alert> : null}
                            {success ? <Alert variant='success'>User correctly found</Alert> : null}
                            {modalError ? <Alert variant='danger'>Something went wrong during the top up</Alert> : null}
                            {modalSuccess ? <Alert variant='success'>Top up correctly performed</Alert> : null}
                        </Form>
                    </div>
                }
            </Formik>
            <h2>User's wallet: {wallet.toFixed(2)}€</h2>
            {user === null ?
                <Grid container spacing={2}>
                    <Grid item xs={6} sm={4}><Button disabled id='button-Cash' onClick={handleShow} size='huge' variant="outline-success"> <h1>Cash</h1> </Button></Grid>
                    <Grid item xs={6} sm={4}><Button disabled id='button-CreditCard' onClick={handleShow} size='huge' variant="outline-success"> <h1>Credit Card</h1> </Button></Grid>
                    <Grid item xs={6} sm={4}><Button disabled id='button-GiftCard' onClick={handleShow} size='huge' variant="outline-success"> <h1>Gift Card</h1> </Button></Grid>
                </Grid>
                :
                <Grid container spacing={2}>
                    <Grid item xs={6} sm={4}><Button id='button-Cash' onClick={handleShow} size='huge' variant="outline-success"> <h1>Cash</h1> </Button></Grid>
                    <Grid item xs={6} sm={4}><Button id='button-CreditCard' onClick={handleShow} size='huge' variant="outline-success"> <h1>Credit Card</h1> </Button></Grid>
                    <Grid item xs={6} sm={4}><Button id='button-GiftCard' onClick={handleShow} size='huge' variant="outline-success"> <h1>Gift Card</h1> </Button></Grid>
                </Grid>
            }
        <Row>
            <Link to='/Dashboard'><Button style={{margin: '20px'}} variant='secondary'>Back</Button></Link>
        </Row>
    <Modal show={show} onHide={handleClose}>
            <Modal.Header closeButton>
                <Modal.Title>Top Up</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <Formik
                    initialValues={{
                        amount:''
                    }}
                    validationSchema={
                        Yup.object({
                            amount: Yup.number().required()
                    })}
                    onSubmit={async(values)=>{
                        setSuccess(false);
                        setError(false);
                        setModalError(false);
                        setModalSuccess(false);
                        let outcome = await API.topUp(
                            {"email": user,
                            "value" : values.amount
                        });
                        if(outcome === true)
                        {
                            setWallet(wallet+values.amount);
                            setModalSuccess(true);
                        }
                        else
                            setModalError(true);
                        handleClose();
                    }}
                    validateOnChange={false}
                    validateOnBlur={false}
                >
                    {({values, errors, touched})=>
                    <div id="container">
                        <Form>
                            {errors.amount && touched.amount ? errors.amount : null} <br/>
                            Amount: <FastField data-testid="amount" id='amount' type="number" name="amount" label="amount" min="0"
                                               step="0.01"/> €
                            <Button id='button-TopUp' style={{margin : '20px'}} type='submit' variant='success'>Top Up</Button>
                        </Form>                
                    </div>
                    }
                </Formik>
            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={handleClose}>Close</Button>
            </Modal.Footer>
        </Modal>
        </>
    );
}

export { TopUp }