import 'bootstrap/dist/css/bootstrap.min.css';
import '../App.css';
import {Button, Form as reactForm, Modal, Spinner} from 'react-bootstrap';
import Alert from 'react-bootstrap/Alert';
import {useEffect, useState} from 'react';
import {Field, Form, Formik} from 'formik';
import * as Yup from 'yup';
import {Link} from 'react-router-dom';
import {API} from '../API/API.js';
import {MailServerAPI} from '../MailServerAPI/MailServerAPI';
import {printOrder} from "../Utilities";

/*LOADING ALL ORDERS WHEN NO MAIL IS SET*/
let statusByIndex = [];

function DeliverOrder(props) {
    const [customer, setCustomer] = useState(null);
    const [orders, setOrders] = useState([]);
    const [errUser, setErrUser] = useState(false);
    const [show, setShow] = useState(false);
    const handleClose = () => {
        setShow(false);
        setMailLoadCompleted(false)
    };
    const [mailLoadCompleted, setMailLoadCompleted] = useState(false);
    const [showSentMailSpinner, setShowSentMailSpinner] = useState(false);
    const [mailSentAlert, setMailSentAlert] = useState(false);


    const handleShow = () => {
        setShow(true)
    };


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

    useEffect( () => {
        loadAllOrders();
    }, [])

    async function loadAllOrders()
    {
        const data = await _getAllOrders();
        setOrders(data);
    }

    async function _getAllOrders() {
        const data = await API.getAllOrders();
        return data;
    }

    async function customerExistsByMail(email) {
        const data = await API.customerExistsByMail(email);
        if (data) {
            setCustomer(email);
            return true;
        } else {
            setCustomer(undefined);
            return false;
        }
    }


    async function handleSubmit(email) {
        setErrUser(false);
        const okay = await customerExistsByMail(email);
        if (okay) {
            const data = await API.getOrdersByEmail(email);
            setOrders(() => data);
            console.log(orders);
        } else {
            setErrUser(true);
        }
    }


    const getPendingOrdersMail = async () => {
        const data = await MailServerAPI.getPendingOrdersMail();
        let parsedData = [];
        const ret = await Object.keys(data).forEach((email) => parsedData.push({"email": email, "status": "LOADED"}));
        //console.log(...mailList, parsedData);
        //Static array creation
        statusByIndex = [...parsedData];
        setMailLoadCompleted(true);

        console.log(statusByIndex);
    }

    function PendingOrdersMailAction(props) {
        const [checkBox, setCheckBox] = useState(true);

        const setOnChangeCheckBoxStatus = () => {
            const res = !checkBox;
            setCheckBox(res);
            if (res)
                statusByIndex[props.index].status = "LOADED";
            else
                statusByIndex[props.index].status = "DISABLED";

        }


        switch (props.value.status) {
            case 'SENT':
                return (
                    <ul className="border-bottom">
                        <reactForm.Check variant="danger" aria-label="option 1" label={statusByIndex[props.index].email}
                                         checked={checkBox} type='switch' isValid={true}
                                         onChange={() => //LASTMINUTE setOnChangeCheckBoxStatus(props.index)
                                             setOnChangeCheckBoxStatus()}>
                        </reactForm.Check>
                    </ul>
                );

            case 'DISABLED':
                return (
                    <ul className="border-bottom">
                        <reactForm.Check variant="danger" aria-label="option 1" label={statusByIndex[props.index].email}
                                         checked={checkBox}
                                         type='switch' isValid={false}
                                         onChange={() => //LASTMINUTE setOnChangeCheckBoxStatus(props.index)
                                             setOnChangeCheckBoxStatus() }>
                        </reactForm.Check>
                    </ul>);
            default:
                return (
                    <ul className="border-bottom">
                        <reactForm.Check variant="danger" aria-label="option 1" label={statusByIndex[props.index].email}
                                         checked={checkBox}
                                         type='switch'
                                         onChange={() => //LASTMINUTE setOnChangeCheckBoxStatus(props.value)
                                             setOnChangeCheckBoxStatus() }>
                        </reactForm.Check>
                    </ul>
                );
        }
    }


    function SendAllMailModal() {

        function MailSentCounter(props1) {
            return (<div className="mx-5">
                <Spinner animation="border" role="status">
                    <span className="visually-hidden">Loading...</span>
                </Spinner>
            </div>)
        }


        function CustomModalFooter() {
            const [disabledButton, setDisabledButton] = useState(false);

            async function sendAllMailRoutine(event) {
                setDisabledButton(true);

                async function sendMail(index) {
                    const data = await MailServerAPI.solicitCustomerTopUp(localStorage.getItem("username"), statusByIndex[index].email);
                    if (data) {
                        statusByIndex[index].status = "SENT";
                    } else {
                        statusByIndex[index].status = "ERROR_SENDING";
                    }
                }

                setShowSentMailSpinner(true);
                event.preventDefault();
                for (let i = 0; i < statusByIndex.length; i++) {
                    if (statusByIndex[i].status === 'LOADED')
                        await sendMail(i);
                }
                setShowSentMailSpinner(false);
                setMailSentAlert(true);
            }


            return (
                <Modal.Footer>
                    {
                        showSentMailSpinner === true ?
                            <MailSentCounter/>
                            :
                            ""
                    }
                    <Button variant="secondary" onClick={handleClose}>
                        Close
                    </Button>
                    {
                        mailSentAlert === true ?
                            <Alert variant="success">All mail was sent</Alert>
                            :
                            <Button variant="success" onClick={(event) => {
                                sendAllMailRoutine(event)
                            }}>
                                Send All Mail
                            </Button>
                    }

                </Modal.Footer>
            );
        }

        return (
            <Modal show={show} onHide={handleClose}>
                <Modal.Header>
                    <Modal.Title>Send all pending orders reminder mail</Modal.Title>
                </Modal.Header>
                <Modal.Body className="scroll-down-modal">
                    {
                        mailLoadCompleted === true ?
                            statusByIndex.map((mailInfo, index) =>
                                <PendingOrdersMailAction key={index} index={index} value={mailInfo}/>
                            )
                            :
                            <Spinner animation="border" role="status">
                                <span className="visually-hidden">Loading...</span>
                            </Spinner>
                    }
                </Modal.Body>
                <CustomModalFooter/>
            </Modal>
        );
    }


    return (
        <>
            <h1>Deliver Order</h1>
            <SendAllMailModal/>
            {itsTime ? null :
                <Alert variant='warning'> It's possible to deliver orders only from Wednesday at 9am to Friday at
                    11pm</Alert>}
            <div className="mt-5">
                <h4>Send all pending orders reminder mail</h4>
                <Button style={{padding: '1rem 10rem', fontSize: '1.5rem'}} size='lg' className="mt-3" variant="outline-success" onClick={() => /*LASTMINUTE handleShow(getPendingOrdersMail())*/
                {getPendingOrdersMail(); handleShow();}}>
                    Send All Mail</Button>
            </div>
            <div id="container" className="pagecontent">
                <Formik
                    initialValues={{
                        email: ''
                    }}
                    validationSchema={Yup.object({
                        email: Yup.string().email()
                    })}
                    onSubmit={(values) => {
                        if (values.email !== "")
                            handleSubmit(values.email)
                        else
                            loadAllOrders();
                    }}
                    validateOnChange={false}
                    validateOnBlur={false}
                >
                    {({values, errors, touched}) =>
                        <Form>
                            <label htmlFor='email'>Email:</label><Field id='email' style={{margin: '20px'}} name="email"
                                                                        type="text"/>
                            <Button style={{margin: '20px'}} type="submit" variant="success"
                                    disabled={!itsTime ? false : true}>Submit customer</Button>
                            {errors.email && touched.email ? errors.email : null}
                        </Form>}
                </Formik>
                {errUser ? <Alert variant='danger'>Customer not found</Alert> : null}
                {orders != null && orders.length === 0 ? <h2>No orders to display yet</h2> :
                    <Orders itsTime={itsTime} orderList={orders}/>}
            </div>
            <Link to='/Dashboard'><Button style={{margin: '20px'}} variant='secondary'>Back</Button></Link>
        </>
    );
}

function Orders(props) {
    const [showAlert, setShowAlert] = useState(false);
    const [show, setShow] = useState(false);
    const [manageOrderCustmer, setManageOrderCustomer] = useState("");


    const handleClose = () => setShow(false);
    const handleShow = (email) => {
        setManageOrderCustomer(email);
        setShow(true);
    };

    let output = [];

    async function deliverOrder(orderId) {
        return await API.deliverOrder(orderId);
    }
    
    async function handleDelivery(e, orderId) {
        e.preventDefault();
        const data = await deliverOrder(orderId);
        if (data === true) {
            window.location.reload(false);
        } else
            return (<Alert variant='danger'>Balance insufficient</Alert>);
    }

    //Modal to handle error in orders
    function ModalManageOrder() {
        const [alertShow, setAlertShow] = useState(false);
        const [data, setData] = useState(undefined);

        //Send mail button handler
        const solicitButtonHandler = async () => {
            const responseData = await MailServerAPI.solicitCustomerTopUp(localStorage.getItem("username"), manageOrderCustmer);
            await setData(responseData);
            //fetch executed, based on data response we'll set all the UI components up
            setAlertShow(true);
        }

        return (
            <Modal show={show} onHide={handleClose}>
                <Modal.Header className="bg-success">
                    <Modal.Title style={{color: 'white'}}>Handle unpaid order</Modal.Title>
                </Modal.Header>
                <Modal.Body>The customer has not paid the order, do you want to solicit the customer via email? <br/>Press
                    Solicit to send an automatically generated email to {manageOrderCustmer}
                    {alertShow === true ?
                        data === true ? <Alert className="mt-3" variant="success">Mail sent correctly</Alert>
                            :
                            <Alert className="mt-3" variant="danger">Can not send the mail</Alert> : ""}

                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={handleClose}>
                        Close
                    </Button>
                    <Button variant="success" disabled={data ? true : false}
                            onClick={(event) => //LASTMINUTE solicitButtonHandler(event)
                                solicitButtonHandler() }>
                        Solicit!
                    </Button>
                </Modal.Footer>
            </Modal>
        );
    }

    //console.log(props.orderList);
    if (props.orderList !== []) {
        for (let o of props.orderList) {
            if (o.status === "CONFIRMED")
                output.push(
                    <Alert variant="success">
                        <li className="list-group-item">
                            {printOrder(o.productList)}
                            Customer: {o.email}
                            <Button style={{margin: '20px'}} onClick={(e) => handleDelivery(e, o.orderId)}
                                    variant='success'
                                    disabled={props.itsTime ? false : true}>Deliver</Button>
                            {showAlert ? <Alert variant="danger">Something went wrong</Alert> : ""}
                        </li>
                    </Alert>
                )
            else if (o.status === "OPEN")
                output.push(
                    <>
                        <ModalManageOrder email={o.email}/>
                        <Alert variant="warning">
                            <li className="list-group-item">
                                {printOrder(o.productList)}
                                Customer: {o.email}
                                <Button style={{margin: '20px'}} onClick={() => handleShow(o.email)}
                                        variant='outline-warning'
                                >Manage Order</Button>
                                <Button style={{margin: '20px'}}
                                        variant='outline-danger'
                                >Cancel Order</Button>
                                {showAlert ? <Alert variant="danger">Something went wrong</Alert> : ""}
                            </li>
                        </Alert>
                    </>
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