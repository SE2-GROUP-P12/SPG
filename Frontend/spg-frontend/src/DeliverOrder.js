import 'bootstrap/dist/css/bootstrap.min.css';
import './App.css';
import Button from 'react-bootstrap/Button';
import Alert from 'react-bootstrap/Alert';
import { useState, useEffect } from 'react';
import { Formik, Form, Field } from 'formik';
import { printOrder } from './PlaceOrder';
import * as Yup from 'yup';
import { Link } from 'react-router-dom';
import { API } from './API.js'

function DeliverOrder(props) {
    const [customer, setCustomer] = useState(null);
    const [orders, setOrders] = useState(null);

/*     useEffect(()=>{
        let getOrdersInfo = async () => {
            API.browseOrders().then(ord => setOrders(ord));
        };
        getOrdersInfo();}, [customer] ); */

    /*TIME MACHINE MANAGEMENT*/
    const [itsTime, setItsTime] = useState(false)
    useEffect(() => {
        let checkTime = (time, date) => {
            console.log("CHECKTIME DELIVERORDER: " + time + " " + date);
            if ((date === 'Wed' && time >= '09:00') || (date === 'Fri' && time < '23:00') || (date === 'Thu'))
                setItsTime(true);
            else
                setItsTime(false);
        }
        checkTime(props.time, props.date);
    }, [props.date, props.time])

    async function customerExistsByMail(email) {
        const data = await API.customerExistsByMail(email);
        if (data) {
            setCustomer(email);
            return true;
        }
        else {
            setCustomer(null);
            return false;
        }
    }

    {/* TODO: introduce a method to get the order list of an user by its email*/ }
    async function getOrdersByEmail(email) {
        const data = await API.getOrdersByEmail(email);
        return data;
    }

    async function handleSubmit(email) {
        const okay = await customerExistsByMail(email);
        if (okay) {
            setOrders(getOrdersByEmail(email));
        }
        else {
            setOrders(null);
        }
    }

    return (
        <>
            <h1>Deliver Order</h1>
            {itsTime ? null : <Alert variant='warning'> It's possible to deliver orders only from Wednesday at 9am to Friday at 11pm</Alert>}
            <div id="container" className="pagecontent">
                <Formik
                    initialValues={{
                        email: ''
                    }}
                    validationSchema={Yup.object({
                        email: Yup.string().email().required()
                    })}
                    onSubmit={(values) => { handleSubmit(values.email) }}
                    validateOnChange={false}
                    validateOnBlur={false}
                >
                    {({ values, errors, touched }) =>
                        <Form>
                            Email:<Field style={{ margin: '20px' }} name="email" type="text" />
                            <Button style={{ margin: '20px' }} type="submit" variant="success" disabled={itsTime ? false : true} >Submit customer</Button>
                            {errors.email && touched.email ? errors.email : null}
                        </Form>}
                </Formik>
                {customer === null ? null : <Orders itsTime={itsTime} orderList={orders} />}
            </div>
            <Link to='/ShopEmployee'><Button style={{ margin: '20px' }} variant='secondary'>Back</Button></Link>
        </>
    );
}

function Orders(props) {
    const [showAlert, setShowAlert] = useState(false);
    let output = [];

    async function deliverOrder(orderId) {
        const data = await API.deliverOrder(orderId);
        return data;
    }

    async function handleDelivery(e, orderId) {
        e.preventDefault();
        const data = await deliverOrder(orderId);
        if (data !== null) {

        }
        else {

        }
    }
    if (props.orderList !== null) {
        for (let o in props.orderList) {
           
            output.push(
                <li className="list-group-item">
                    {printOrder(o.productList)}
                    Customer: {o.email}<Button style={{ margin: '20px' }} onClick={(e) => handleDelivery(e, o.orderId)} variant='success' disabled={props.itsTime ? false : true} >Deliver</Button>
                    {showAlert ? <Alert variant="danger">Something went wrong</Alert> : ""}
                </li>
            )
        }
    } else {
        output.push(
            <Alert variant="warning">You still have no order</Alert>
        )
    }
    return output;
}

export { DeliverOrder }