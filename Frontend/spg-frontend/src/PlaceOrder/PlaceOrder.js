import 'bootstrap/dist/css/bootstrap.min.css';
import './../App.css';
import {useState, useEffect} from 'react';
import {Link, Redirect} from 'react-router-dom';
import Row from 'react-bootstrap/Row';
import Spinner from 'react-bootstrap/Spinner';
import Col from 'react-bootstrap/Col';
import Button from 'react-bootstrap/Button';
import Alert from 'react-bootstrap/Alert';
import {Modal, Form as reactForm} from 'react-bootstrap';
import {Formik, Form, Field} from 'formik';
import * as Yup from 'yup';
import {API} from '../API/API';


function PlaceOrder(props) {
    const [customer, setCustomer] = useState("");
    const [order, setOrder] = useState([]);
    const [error, setError] = useState(false);
    const [customerError, setCustomerError] = useState(false);
    const [customerSuccess, setCustomerSuccess] = useState(false);
    const [deleteError, setDeleteError] = useState(false);
    const [sendError, setSendError] = useState(false);
    const [sendSuccess, setSendSuccess] = useState(false);
    const [triggerError, setTriggerError] = useState(false);
    const [loading, setLoading] = useState(true);
    //Modal confirm placd order
    const [modalShow, setModalShow] = useState(false);

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

    const placeOrder = async (pickUpDate, pickUpAddress) => {
        setSendError(false);
        setSendSuccess(false);
        let outcome;
        if (props.loggedUserRole === "EMPLOYEE")
            outcome = await API.placeOrder({
                'email': localStorage.getItem("username"),
                'customer': customer,
                'deliveryDate': pickUpDate,
                'deliveryAddress': ''
            });
        else
            outcome = await API.placeOrder({
                'email': "",
                'customer': localStorage.getItem("username"),
                'deliveryDate': pickUpDate,
                'deliveryAddress': ''
            });
        if (outcome) {
            setOrder(null);
            setSendSuccess(true);
            /* NEW */
            let warning = await API.getWalletWarning(localStorage.getItem("username"));
            props.setTopUpWarning(warning);
            /* NEW */
        } else
            setSendError(true);
        setOrder([]);
    }

    const showModalHanlder = () => {
        setModalShow(true);
    }

    const handleClose = () => {
        setModalShow(false);
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
            <Redirect to="/ErrorHandler"/>
        );
    }

    if (loading === true) {
        <Spinner animation="border" role="status">
            <span className="visually-hidden">Loading...</span>
        </Spinner>
    }


    //MODAL REVIEW COMPONENT

    const ReviewOrderComponent = (props) => {
        const [showDate, setShowDate] = useState(true);
        const [pickUpDate, setPickUpDate] = useState("");
        const [pickUpTime, setPickUpTime] = useState("");
        const [label, setLabel] = useState("SKIP FOR NOW");
        const [errorDate, setErrorDate] = useState(false);
        const [showAddressForm, setShowAddressForm] = useState(false);
        const [shippingSelector, setShippingSelector] = useState("DELIVERY");


        const onChangeDateHandler = (date) => {
            let dateObj = new Date(date);
            if ([3, 4, 5].includes(dateObj.getDay())) {
                let tmp = "" + dateObj.getDay() + "/" + dateObj.getMonth() + "/" + dateObj.getFullYear();
                setPickUpDate(tmp);
                console.log(tmp);
                setErrorDate(false);
            } else
                setErrorDate(true);
        }

        const onChangeTimeHandler = (time) => {
            console.log(time)
            setPickUpTime(time.toString());
        }


        const selectAlertType = () => {

            const handleDeliverySelectionSwitch = () => {
                console.log(shippingSelector === "DELIVERY")
                if (shippingSelector === "DELIVERY")
                    setShippingSelector("PICK UP");
                setShippingSelector("DELIVERY");
            }


            const getAlertBasedPicker = (variant) => {


                return (
                    <>
                        <h6>When deliver your order:</h6>
                        <Alert className="mt-3" variant={variant}>
                            <reactForm.Control type="date" name="deliveryDate" placeholder="delivery date"
                                               onChange={(event) => onChangeDateHandler(event.target.value)}
                            />
                            <reactForm.Control className="mt-3" type="time" name="deliveryTime" className="mt-3"
                                               onChange={(event) => onChangeTimeHandler(event.target.value)}/>
                        </Alert>
                        <h6>Where deliver your order:</h6>
                        <Alert className="mt-3" variant={variant}>
                            <reactForm.Check type="switch"
                                             label={(shippingSelector === "DELIVERY") ? "Delivery" : "Pick Up"}
                                             onClick={() => handleDeliverySelectionSwitch()}/>
                            {
                                showAddressForm === true ?
                                    <reactForm.Control type="text" name="deliveryDate" placeholder="delivery address"
                                                       onChange={(event) => onChangeDateHandler(event.target.value)}
                                    />
                                    : ""
                            }
                        </Alert>
                    </>
                )
            }


            if (!errorDate) {
                if (pickUpDate === 0)
                    return getAlertBasedPicker("")
                else
                    return getAlertBasedPicker("success")
            } else
                return getAlertBasedPicker("danger")
        }

        //console.log(order)

        const getOrderTotalAmount = () => {
            let total = 0.00;
            for (let p of order) {
                total += p.price * p.quantityAvailable;
            }
            return total.toFixed(2);
        }


        const changeCheckBoxHanlder = () => {
            if (!showDate) {
                setLabel("SKIP FOR NOW");
                //reset status
                setErrorDate(false);
                setPickUpDate(-1);
            } else
                setLabel("SET SHIPPING INFO");
            setShowDate(!showDate);
        }

        return (<Modal
                show={modalShow}
                onHide={handleClose}
                backdrop="static"
                keyboard={false}
            >
                <Modal.Header closeButton>
                    <h3>Review and comfirme order</h3>
                </Modal.Header>
                <Modal.Body>
                    <b>ISSUER: </b> {localStorage.getItem("role") === "EPLOYEE" ? customer : localStorage.getItem("username")}
                    <br/>
                    <br/>
                    <b>AMOUNT: </b> {getOrderTotalAmount()}
                    <br/><br/>
                    <div>
                        <Row>
                            <Col>
                                <h5 variant="success">SHIPPING INFO </h5>
                            </Col>
                            <Col>
                                <reactForm.Check type="switch" label={label} claaName="success"
                                                 onChange={() => changeCheckBoxHanlder()}
                                                 checked={showDate}/>
                            </Col>
                        </Row>
                    </div>
                    {showDate === true ? selectAlertType() : "HANDLE SENDING TO THE CURRENT CUSTOMER ADDRESS"}

                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={handleClose}>
                        Close
                    </Button>
                    <Button variant="success" disabled={pickUpDate === 0}
                            onClick={() => placeOrder(pickUpDate, null)}>Place
                        Order</Button>
                </Modal.Footer>
            </Modal>
        );
    }

    return (
        <>
            <h1>Place Order</h1>
            <ReviewOrderComponent/>
            {itsTime ? null :
                <Alert variant='warning'> It's possible to place orders only from Saturday at 9am to
                    Sunday at
                    11pm</Alert>}
            {error ? <Alert variant='danger'>Something went wrong, couldn't retrieve
                order</Alert> : null}
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
                                    //console.log("CHECKPOINT, EMAIL:" + values.email + " ORDER:" + JSON.stringify(order) + " itsTime:" + itsTime);
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
                                    {customerError ?
                                        <Alert variant='danger'> User not found </Alert> : null}
                                    {customerSuccess ?
                                        <Alert variant='success'> User found, you can now place their
                                            order </Alert> : null}
                                    {deleteError ?
                                        <Alert variant='danger'> Something went wrong emptying your
                                            cart </Alert> : null}
                                    {sendError ?
                                        <Alert variant='danger'> Something went wrong sending your
                                            order </Alert> : null}
                                    {sendSuccess ? <Alert variant='success'> Order sent
                                        successfully </Alert> : null}
                                </Form>
                            }
                        </Formik>
                    </>
                    :
                    ""}
            </div>
            <Row>
                <Col xs={4}><Link to="/Dashboard"><Button
                    variant='secondary'>Back</Button></Link></Col>
                <Col xs={4}><Button disabled={order.length === 0 ? true : false} variant='danger'
                                    onClick={dropOrder}>Delete
                    order</Button></Col>
                <Col xs={4}><Button
                    disabled={(!itsTime || order.length === 0 || customer === null) ? true : false}
                    variant='success' onClick={() => showModalHanlder()}>Send order</Button></Col>
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
        console.log("product-id: " + p.productId)
        output.push(<OrderEntry product={p} key={p.productId.toString()} value={p.productId}/>);
        total += p.price * p.quantityAvailable;
    }
    output.push(
        <li className='list-group-item'>
            <h2>Total: {total.toFixed(2)} €</h2>
        </li>
    )
    return output;
}

function OrderEntry(props) {
    return (
        <li className="list-group-item">
            {props.product.name} : {props.product.quantityAvailable} {props.product.unitOfMeasurement}<br/>
            SUBTOTAL: {(props.product.price * props.product.quantityAvailable).toFixed(2)}€
        </li>
    );
}


export
{
    PlaceOrder, printOrder
}