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
    const [orders, setOrders] = useState([]);
    const [dirty, setDirty] = useState(true);

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
            setCustomer(undefined);
            return false;
        }
    }

    {/* TODO: introduce a method to get the order list of an user by its email*/ }
    async function _getOrdersByEmail(email) {
        const data = await API.getOrdersByEmail(email);
        console.log(data);
        return data;
    }

    async function handleSubmit(email) {
        const okay = await customerExistsByMail(email);
        if (okay) {
            const data = await API.getOrdersByEmail(email);
            setOrders(old => data);
            console.log(orders);
        }
        else {
            setOrders([]);
        }
        setDirty(true);
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
                {customer === undefined ? <Alert variant='danger'>Customer not found</Alert> : null}
                {orders != undefined && orders.length === 0 ? <h2>No orders to display yet</h2> : <Orders itsTime={itsTime} orderList={orders} />}
            </div>
            <Link to='/ShopEmployee'><Button style={{ margin: '20px' }} variant='secondary'>Back</Button></Link>
        </>
    );
}

function Orders(props) {
    const [showAlert, setShowAlert] = useState(false);
    const [orders, setOrders] = useState(props.orderList);

    let output = [];
    async function deliverOrder(orderId) 
    {
        const data = await API.deliverOrder(orderId);
        return data;
    }
    function printOrder(prod) {
        let output = [];
        let total = 0;
        if (prod === null)
            return (<h2>The order is empty </h2>);
        for (let p of prod) {
            output.push(<OrderEntry product={p} />);
            total += p["unit price"] * p.amount;
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
                {props.product.name} : {props.product.amount}{props.product.unit}<br />
                SUBTOTAL: {props.product["unit price"] * props.product.amount}€
            </li>
        );
    }
    
    async function handleDelivery(e, orderId) {
        e.preventDefault();
        const data = await deliverOrder(orderId);
        if (data !== null) {
            window.location.reload(false);
        }
        else {
            return (<Alert variant='danger'>Some error occourred</Alert>);
        }
    }
    //console.log(props.orderList);
    if (props.orderList !== []) {
        for (let o of props.orderList) {
            //console.log(o);
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