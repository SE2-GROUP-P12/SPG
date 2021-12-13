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
import {getAllShippingMode} from '../Utilities';


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
    const [modalShow, setModalShow] = useState(false);
    const [showEndRoutineModal, setShowEndRoutineModal] = useState(false);
    const [itsTime, setItsTime] = useState(false)


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
            setOrder([]);
        else
            setDeleteError(true);
    }

    const placeOrder = async (pickUpDate, pickUpAddress, time) => {
        setSendError(false);
        setSendSuccess(false);
        let outcome;
        if (props.loggedUserRole === "EMPLOYEE")
            outcome = await API.placeOrder({
                'email': localStorage.getItem("username"),
                'customer': customer,
                'deliveryDate': (pickUpDate + ((time.split(':')[0] * 3600 + time.split(':')[1] * 60) + 0)).toString(),
                'deliveryAddress': pickUpAddress,
            });
        else
            outcome = await API.placeOrder({
                'email': "",
                'customer': localStorage.getItem("username"),
                'deliveryDate': (pickUpDate + ((time.split(':')[0] * 3600 + time.split(':')[1] * 60) + 0)).toString(),
                'deliveryAddress': pickUpAddress,
            });
        if (outcome) {
            setOrder([]);
            setSendSuccess(true);
            /* NEW */
            let warning = await API.getWalletWarning(localStorage.getItem("username"));
            props.setTopUpWarning(warning);
            /* NEW */
        } else
            setSendError(true);
        //setModalShow(false);
    }

    const showModalHanlder = () => {
        setModalShow(true);
    }

    const handleClose = () => {
        setModalShow(false);
    }


    /*TIME MACHINE MANAGEMENT*/
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
        const [pickUpDate, setPickUpDate] = useState(-1);
        const [pickUpTime, setPickUpTime] = useState("");
        const [showShippingInfo, setShowShppingInfo] = useState("SKIP FOR NOW");
        const [errorDate, setErrorDate] = useState(false);
        const [showAddressForm, setShowAddressForm] = useState(false);
        const [checkBoxStatus, setChechBoxStatus] = useState([true, false]);
        const [customAddress, setCustomAddress] = useState("");
        const [enableButton, setEnableButton] = useState(false);

        const enableButtonHandler = (date, time, address) => {
            //Pick up Selected
            const tmp = new Date(date * 1000);
            if (checkBoxStatus[0] === true) {
                if (date < (Date.now() / 1000)) {
                    setEnableButton(false);
                    return;
                }
                //check if day is from Wednsday to Friday (add check on time...)
                if (![3, 4, 5].includes(tmp.getDay()) || (0 + time.split(':')[0]) > 17 || (0 + time.split(':')[0]) < 9 || time === "") {
                    setEnableButton(false);
                    return;
                }
                setEnableButton(true);
                return;
            }
            //Delivery Selected
            else if (checkBoxStatus[1] === true) {
                if (date < (Date.now() / 1000)) {
                    setEnableButton(false);
                    return;
                }
                //check if day is from Wednsday to Friday (add check on time...)
                if (![3, 4, 5].includes(tmp.getDay()) || (0 + time.split(':')[0]) > 17 || (0 + time.split(':')[0]) < 9 || time === "") {
                    setEnableButton(false);
                    return;
                }
                if (address === "") {
                    setEnableButton(false);
                    return;
                }
                setEnableButton(true);
            }
        }

        const onChangeDateHandler = (date) => {
            let dateObj = new Date(date.replaceAll('-', '.'));
            if ([3, 4, 5].includes(dateObj.getDay())) {
                let tmp = dateObj.getTime() / 1000;
                setPickUpDate(tmp);
                setErrorDate(false);
            } else
                setErrorDate(true);
        }

        const onChangeTimeHandler = (time) => {
            setPickUpTime(time.toString());
        }


        const selectAlertType = () => {


            const getAlertBasedPicker = (variant) => {
                const shippingMode = getAllShippingMode();

                function shippingModeCheckHandler(index) {
                    let tmp = checkBoxStatus;
                    let i;
                    for (i = 0; i < tmp.length; i++)
                        if (i === index)
                            tmp[i] = true;
                        else
                            tmp[i] = false;
                    setCustomAddress("");
                    tmp[index] = true;
                    setChechBoxStatus([...tmp]);
                }

                return (
                    <div className="mt-3">
                        <h6>When deliver your order:</h6>
                        <Alert className="mt-2" variant={variant}>
                            <Alert> <b>NOTICE</b>: Delivery and Pick up are available Wednedasy, Thursday and Friday
                                from 9:00
                                to 18:00.
                            </Alert>
                            <reactForm.Control className="mt-3" type="date" name="deliveryDate"
                                               placeholder="delivery date"
                                               onChange={async (event) => {
                                                   await onChangeDateHandler(event.target.value);
                                                   await enableButtonHandler(new Date(event.target.value), pickUpTime, customAddress);
                                               }}
                            />
                            <reactForm.Control className="mt-3" type="time" name="deliveryTime" className="mt-3"
                                               onChange={(event) => {
                                                   onChangeTimeHandler(event.target.value);
                                                   enableButtonHandler(pickUpDate, event.target.value, customAddress);
                                               }}/>
                        </Alert>
                        <h6>Where deliver your order:</h6>
                        <Alert className="mt-3" variant={variant}>
                            {shippingMode.map((value, index) => <reactForm.Check key={index} label={value.name}
                                                                                 onChange={() => {
                                                                                     shippingModeCheckHandler(index);
                                                                                     enableButtonHandler(pickUpDate, pickUpTime, customAddress)
                                                                                 }}
                                                                                 checked={checkBoxStatus[index]}/>)}
                        </Alert>
                        <reactForm.Control type="text" name="deliveryDate" placeholder="delivery address"
                                           onChange={(event) => {
                                               setCustomAddress(event.target.value);
                                               enableButtonHandler(pickUpDate, pickUpTime, event.target.value);
                                           }}
                                           value={customAddress}
                                           disabled={!checkBoxStatus[1] === true}
                        />
                    </div>
                )
            }


            if (!errorDate) {
                if (pickUpDate === "")
                    return getAlertBasedPicker("")
                else
                    return getAlertBasedPicker("")
            } else
                return getAlertBasedPicker("")
        }

        //console.log(order)

        const getOrderTotalAmount = () => {
            let total = 0.00;
            console.log('order ' + order);
            if (order === null)
                return 0.00;
            for (let p of order) {
                total += p.price * p.quantityAvailable;
            }
            return total.toFixed(2);
        }


        const changeCheckBoxHanlder = () => {
            if (!showDate) {
                setShowShppingInfo("SKIP FOR NOW");
                //reset status
                setErrorDate(false);
                setPickUpDate(-1);
            } else
                setShowShppingInfo("SET SHIPPING INFO");
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
                                <reactForm.Check type="switch" label={showShippingInfo} claaName="success"
                                                 onChange={() => changeCheckBoxHanlder()}
                                                 checked={showDate}/>
                            </Col>
                        </Row>
                    </div>
                    {showDate === true ? selectAlertType() :
                        <Alert variant="warning">Don't forget to set up your shipping info!</Alert>}

                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={handleClose}>
                        Close
                    </Button>
                    {showShippingInfo === "SKIP FOR NOW" ? <Button variant="success" disabled={!enableButton}
                                                                   onClick={() => {
                                                                       placeOrder(pickUpDate, customAddress, pickUpTime);
                                                                       setModalShow(false);
                                                                       setShowEndRoutineModal(true);
                                                                   }}>
                            Place Order</Button>
                        :
                        <Button variant="success"
                                onClick={() => {
                                    placeOrder(-1, "", "");
                                    setModalShow(false);
                                    setShowEndRoutineModal(true)
                                }}>
                            Place Order</Button>}

                </Modal.Footer>
            </Modal>
        );
    }

    return (
        <>
            <h1>Place Order</h1>
            <Modal show={showEndRoutineModal}
                   backdrop="static"
                   keyboard={false}>
                <Modal.Header>
                    <h4>Order Completed</h4>
                </Modal.Header>
                <Modal.Body>
                    {sendError === true ?
                        <Alert variant="warning">Error in placed order, please retry!</Alert> :
                        <Alert variant="success">Congratulations, order placed correctly!</Alert>}
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="success" onClick={() => {
                        setError(false);
                        setShowEndRoutineModal(false);
                    }}>
                        Close
                    </Button>
                </Modal.Footer>
            </Modal>
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
                                    onClick={dropOrder}>Delete order</Button></Col>
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
    if (prod.length === 0)
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