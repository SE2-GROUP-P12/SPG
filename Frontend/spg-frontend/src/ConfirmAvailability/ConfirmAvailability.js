import {API} from "../API/API";
import {useEffect, useState} from "react";
import Card from "@mui/material/Card";
import CardMedia from "@mui/material/CardMedia";
import fruit from "../resources/fruits.png";
import CardContent from "@mui/material/CardContent";
import Typography from "@mui/material/Typography";
import CardActions from "@mui/material/CardActions";
import Grid from "@mui/material/Grid";
import Button from "react-bootstrap/Button";
import {Modal} from "react-bootstrap";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import {Field, Form, Formik} from "formik";
import * as Yup from "yup";
import Alert from "react-bootstrap/Alert";
import {Link, Redirect} from "react-router-dom";
import Container from "react-bootstrap/Container";
import Spinner from "react-bootstrap/Spinner";
import "../App.css";
import "./ConfirmAvailability.css";
import {CheckCircleFill} from "react-bootstrap-icons";

function ConfirmAvailability(props) {

    const [products, setProducts] = useState([]);
    const [loadCompleted, setLoadCompleted] = useState(false);
    const [triggerError, setTriggerError] = useState(false);
    const email = localStorage.getItem("username")
    const [availabilities, setAvailabilities] = useState([]);
    const [showSuccessModal, setShowSuccessModal] = useState(false);

    /*TIME MACHINE MANAGEMENT*/
    const [itsTime, setItsTime] = useState(false)
    useEffect(() => {
        let checkTime = (time, date) => {
            console.log("CHECKTIME CONFIRM AVAILABILITY: " + time + " " + date);
            if ((date === 'Sat' && time >= '09:00') || (date === 'Mon' && time < '09:00') || (date === 'Sun'))
                setItsTime(true);
            else
                setItsTime(false);
        }
        checkTime(props.time, props.date);
    }, [props.date, props.time])


    const handleConfirm = async (productId, quantityConfirmed) => {
        //Parameters must be present
        if (productId === undefined || quantityConfirmed === undefined) {
            console.log("error")
            return
        }

        if (availabilities.filter(x => x.productId == productId).length === 0) {
            setAvailabilities(x => [...x, {
                'productId': productId,
                'quantityConfirmed': quantityConfirmed
            }])
        } else {
            let newAvailabilities = [...availabilities];
            let index = availabilities.findIndex(x => x.productId === productId)
            newAvailabilities[index].quantityConfirmed = quantityConfirmed;
            setAvailabilities(newAvailabilities);
        }
    }

    async function handleSubmit() {
        if (await API.submitConfirmed(availabilities) !== undefined) {
            setShowSuccessModal(true);
        } else {
            setTriggerError(true)
        }
    }

    async function _browseProductsByFarmer() {
        const data = await API.browseProductsByFarmer({'email': email, 'forecasted': true}, props.setErrorMessage);
        if (data !== null) {
            setProducts(data['data']);
            setLoadCompleted(true);
        } else {
            setTriggerError(true);
        }
    }

    useEffect(async () => {
        await _browseProductsByFarmer();
    }, []);

    useEffect(async () => {
        products.filter(x => x.quantityConfirmed > 0).forEach(y => handleConfirm(y.productId, y.quantityConfirmed));
    }, [products])

    function ProductEntry(props) {
        const [show, setShow] = useState(false);
        const handleClose = () => setShow(false);
        const handleShow = () => setShow(true);
        const [showSuccess, setShowSuccess] = useState(null);
        const [showError, setShowError] = useState(null)

        let currentlyConfirmed = availabilities.filter(x => x.productId === props.product.productId).length > 0 ?
            availabilities.filter(x => x.productId === props.product.productId)[0].quantityConfirmed : 0

        let encodedImg = props.product.base64Image;

        return (
            <>
                <Card className={currentlyConfirmed > 0 ? "confirmed-product-card" : ""} sx={{maxWidth: 345}}>
                    <CardMedia
                        component="img"
                        height="140"
                        image={props.product.imageUrl == null ? fruit : `data:image/png;base64, ${encodedImg}`}
                        alt="fruit"
                    />
                    <CardContent>
                        <Typography gutterBottom variant="h5" component="div">
                            {currentlyConfirmed > 0 ? <CheckCircleFill color="#5cb85c"/> : <></>} {props.product.name}
                        </Typography>
                        <Typography variant="body">
                            {props.product.quantityForecast} {props.product.unitOfMeasurement} currently
                            forecasted <br/>
                            {currentlyConfirmed} {props.product.unitOfMeasurement} confirmed <br/>
                        </Typography>
                    </CardContent>
                    <CardActions>
                        <Grid container>
                            <Grid item xs={12}> <Button disabled={!itsTime} className="card-button" variant="success"
                                                        onClick={() => handleConfirm(props.product.productId, props.product.quantityForecast)}> Confirm
                                Forecasted
                                Availability </Button></Grid>
                            <Grid item xs={12}> <Button disabled={!itsTime} className="card-button" variant="success"
                                                        onClick={handleShow}> Set Availability </Button>
                            </Grid>
                        </Grid>
                    </CardActions>
                </Card>

                <Modal show={show} onHide={() => {
                    handleClose();
                    setShowError(null);
                    setShowSuccess(null);
                }}>
                    <Modal.Header>
                        <Modal.Title>Set Availability for {props.product.name}</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        <div id="container" className="pagecontent" align='center'>
                            <img src={props.product.imageUrl == null ? fruit : `data:image/png;base64, ${encodedImg}`}
                                 alt="fruit"
                                 style={{height: '150px'}}/>
                            <Row>
                                <Col xs={12}>
                                    {props.product.quantityForecast} {props.product.unitOfMeasurement} currently
                                    forecasted
                                </Col>
                            </Row>
                            <Row>
                                <Col xs={12}>
                                    {currentlyConfirmed} {props.product.unitOfMeasurement} currently
                                    confirmed
                                </Col>
                            </Row>
                            <Row>
                                <Formik
                                    initialValues={{amount: 0}}
                                    validationSchema={Yup.object({amount: Yup.number().min(0).required('Amount required!')})}
                                    onSubmit={async (values) => {
                                        handleConfirm(props.product.productId, values.amount)
                                    }}
                                    validateOnChange={false}
                                    validateOnBlur={false}
                                >
                                    {({values, errors, touched}) =>
                                        <Form>
                                            <label htmlFor='amount'>Amount:</label>
                                            <Field type="number" id="amount" name="amount"
                                                   min={0}/> {props.product.unitOfMeasurement}
                                            <br/>
                                            <Button style={{margin: '20px'}} type="submit" variant="success">Set
                                                Availability</Button>
                                            {errors.amount && touched.amount ? errors.amount : null}
                                            {showSuccess !== null ?
                                                <Alert variant='success'>{showSuccess}</Alert> : null}
                                            {showError !== null ? <Alert variant='danger'>{showError}</Alert> : null}
                                        </Form>
                                    }
                                </Formik>
                            </Row>
                        </div>
                    </Modal.Body>
                    <Modal.Footer>
                        <Button variant="secondary" onClick={async () => {
                            handleClose();
                            setShowError(null);
                            setShowSuccess(null);
                        }}>
                            Close
                        </Button>
                    </Modal.Footer>
                </Modal>
            </>
        );
    }

    if (triggerError === true) {
        return (<Redirect to="/ErrorHandler"></Redirect>);
    }

    return (
        <Container fluid>
            <h1>Confirm availabilities for the next week</h1>
            <br/>
            {itsTime ? null :
                <Alert variant='warning'> It's possible to confirm availabilities only from Saturday at 9am to Monday at
                    9 am</Alert>}
            <br/>
            <Row>
                <Col>
                    <Button disabled={!itsTime} variant="success" onClick={() =>
                        products.forEach(x => {
                            handleConfirm(x.productId, x.quantityForecast)
                        })
                    }>Confirm all products</Button>
                </Col>
            </Row>
            <br/>
            <br/>
            <Grid container spacing={2}>
                {
                    loadCompleted === true ?
                        products.map((prod, index) =>
                            <Grid item xs={12} sm={6} md={4} align="center">
                                <ProductEntry key={index} product={prod}></ProductEntry>
                            </Grid>)
                        :
                        <Grid xs={12} item align="center">
                            <Spinner animation="border" role="status">
                                <span className="visually-hidden">Loading...</span>
                            </Spinner>
                        </Grid>
                }
            </Grid>
            <Link to='/Dashboard'><Button style={{position: 'fixed', bottom: '10px', left: '10px'}}
                                          variant='secondary'>Back</Button></Link>
            <Button onClick={handleSubmit} disabled={!itsTime}
                    style={{position: 'fixed', bottom: '10px', right: '10px'}}
                    variant="success">Submit</Button>
            {/*Successful submit modal*/}
            <Modal show={showSuccessModal} onHide={() => setShowSuccessModal(false)}>
                <Modal.Body>
                    Your availabilities have been communicated with success
                </Modal.Body>
                <Modal.Footer>
                    <Link to="/Dashboard">
                        <Button variant="success">
                            Return to the Dashboard
                        </Button>
                    </Link>
                </Modal.Footer>
            </Modal>
        </Container>
    )
}

export {ConfirmAvailability}

