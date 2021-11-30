import 'bootstrap/dist/css/bootstrap.min.css';
import './../App.css';
import {useState, useEffect} from 'react';
import {Link} from 'react-router-dom';
import Row from 'react-bootstrap/Row';
import Spinner from 'react-bootstrap/Spinner';
import Col from 'react-bootstrap/Col';
import Button from 'react-bootstrap/Button';
import Alert from 'react-bootstrap/Alert';
import {Formik, Form, Field} from 'formik';
import * as Yup from 'yup';
import {API} from './../API/API';
import {Redirect} from "react-router-dom";
import { Collapse } from '@mui/material';

function PlaceOrder(props) {
    const [customer, setCustomer] = useState("");
    const [order, setOrder] = useState(null);
    const [error, setError] = useState(false);
    const [customerError, setCustomerError] = useState(false);
    const [customerSuccess, setCustomerSuccess] = useState(false);
    const [deleteError, setDeleteError] = useState(false);
    const [sendError, setSendError] = useState(false);
    const [sendSuccess, setSendSuccess] = useState(false);
    const [triggerError, setTriggerError] = useState(false);
    const [loading, setLoading] = useState(true);

    /***************************************************************************/
    //let email = 'mario.rossi@gmail.com' //dovrebbe essere recuperata dalla sessione
    //const username = localStorage.getItem("username");
    //const username = props.loggedUser; //requires hooks system from App.js (where loggedUser state is located)
    /***************************************************************************/

    async function _getCart() {
        const prod = await API.getCart({'email': localStorage.getItem("username")}, props.setErrorMessage);
        if (prod === null) {
            setTriggerError(true);
            setLoading(false)
            return;
        }
        if (prod === undefined) {
            setError(true);
            setOrder([]);
        } else
            setOrder(prod);
    }


    useEffect(() => {
        _getCart();
    }, []);


    const dropOrder = async () => {
        let outcome = await API.dropOrder({'email': localStorage.getItem("username")});
        if (outcome)
            setOrder(null);
        else
            setDeleteError(true);
    }

    const placeOrder = async () => {
        setSendError(false);
        setSendSuccess(false);
        let outcome;
        if (props.loggedUserRole === "EMPLOYEE")
            outcome = await API.placeOrder({'email': localStorage.getItem("username"), 'customer': customer});
        else
            outcome = await API.placeOrder({'email': "", 'customer': localStorage.getItem("username")});
        if (outcome) {
            setOrder(null);
            setSendSuccess(true);
        } else
            setSendError(true);
    }

    /*TIME MACHINE MANAGEMENT*/
    const [itsTime, setItsTime] = useState(false)
    useEffect(() => {
        let checkTime = (time, date) => {
            if ((date === 'Sat' && time >= '09:00') || (date === 'Sun' && time < '23:00'))
                setItsTime(true);
            else
                setItsTime(false);
        }
        checkTime(props.time, props.date);
    }, [props.date, props.time])

    if (triggerError === true) {
        return (
            <Redirect to="/ErrorHandler"></Redirect>
        );
    }

    if (loading === true) {
        <Spinner animation="border" role="status">
            <span className="visually-hidden">Loading...</span>
        </Spinner>
    }

    return (
        <>
            <h1>Place Order</h1>
            {itsTime ? null :
                <Alert variant='warning'> It's possible to place orders only from Saturday at 9am to Sunday at
                    11pm</Alert>}
            {error ? <Alert variant='danger'>Something went wrong, couldn't retrieve order</Alert> : null}
            <div id="container" className="pagecontent">
                <ul className="list-group">{printOrder(order)}</ul>
            </div>
            <div id="container" className="pagecontent">
                {props.loggedUserRole === "EMPLOYEE" ? <>
                        <h2>Whose order is this?</h2>
                        <Formik
                            initialValues={{email: ''}}
                            validationSchema={Yup.object({
                                email: Yup.string().email().required()
                            })}
                            onSubmit={async (values) => {
                                setCustomerSuccess(false);
                                setCustomerError(false);
                                setCustomer(null);
                                let presentEmail = await API.customerExistsByMail(values.email)
                                setCustomerError(!presentEmail);
                                if (presentEmail) {
                                    console.log("CHECKPOINT, EMAIL:" + values.email + " ORDER:" + JSON.stringify(order) + " itsTime:" + itsTime);
                                    setCustomer(values.email);
                                    setCustomerSuccess(true);
                                }
                            }}
                            validateOnChange={false}
                            validateOnBlur={false}
                        >
                            {({values, errors, touched}) =>
                                <Form>
                                    Email:<Field style={{margin: '20px'}} name="email" type="text"/>
                                    <Button style={{margin: '20px'}} type="submit" variant="success">Submit
                                        customer</Button>
                                    {errors.email && touched.email ? errors.email : null}
                                    {customerError ? <Alert variant='danger'> User not found </Alert> : null}
                                    {customerSuccess ?
                                        <Alert variant='success'> User found, you can now place their order </Alert> : null}
                                    {deleteError ?
                                        <Alert variant='danger'> Something went wrong emptying your cart </Alert> : null}
                                    {sendError ?
                                        <Alert variant='danger'> Something went wrong sending your order </Alert> : null}
                                    {sendSuccess ? <Alert variant='success'> Order sent successfully </Alert> : null}
                                </Form>
                            }
                        </Formik>
                    </>
                    :
                    ""}
            </div>
            <Row>
                <Col xs={4}><Link to={'/'+localStorage.getItem("role")}><Button variant='secondary'>Back</Button></Link></Col>
                <Col xs={4}><Button disabled={order === null ? true : false} variant='danger' onClick={dropOrder}>Delete
                    order</Button></Col>
                <Col xs={4}><Button disabled={(!itsTime || order === null || customer === null) ? true : false}
                                    variant='success' onClick={placeOrder}>Send order</Button></Col>
            </Row>
        </>
    );
}

function printOrder(prod) {
    let output = [];
    let total = 0;
    if (prod === null)
        return (<h2>The cart is empty </h2>);
    for (let p of prod) {
        output.push(<OrderEntry product={p}/>);
        total += p.price * p.quantityAvailable;
    }
    output.push(
        <li className='list-group-item'>
            <h2>Total: {total} €</h2>
        </li>
    )
    return output;
}

function OrderEntry(props) {
    return (
        <li className="list-group-item">
            {props.product.name} : {props.product.quantityAvailable} {props.product.unitOfMeasurement}<br/>
            SUBTOTAL: {props.product.price * props.product.quantityAvailable}€
        </li>
    );
}


export {PlaceOrder, printOrder}