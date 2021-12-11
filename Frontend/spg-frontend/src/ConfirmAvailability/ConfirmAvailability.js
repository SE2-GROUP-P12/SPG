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
import "../App.css"
import {forEach} from "react-bootstrap/ElementChildren";

function ConfirmAvailability(props) {

    const [products, setProducts] = useState([]);
    const [loadCompleted, setLoadCompleted] = useState(false);
    const [triggerError, setTriggerError] = useState(false);
    const email = localStorage.getItem("username")
    const [availabilities, setAvailabilities] = useState([]);

    const handleConfirm = (productId, quantityConfirmed) => {
        //Parameters must be present
        if (productId === undefined || quantityConfirmed === undefined) {
            console.log("error")
            return
        }

        if (availabilities.filter(x => x.id == productId).length === 0) {
            setAvailabilities(x => [...x, {
                'id': productId,
                'quantityAvailable': quantityConfirmed
            }])
        } else {
            let newAvailabilities = [...availabilities];
            let index = availabilities.findIndex(x => x.id === productId)
            newAvailabilities[index].quantityAvailable = quantityConfirmed;
            setAvailabilities(x => newAvailabilities);
        }
    }

    async function _browseProductsByFarmer() {
        const data = await API.browseProductsByFarmer({'email': email, 'forecasted': true}, props.setErrorMessage);
        //console.log(data);
        if (data !== null) {
            setProducts(data['data']);
            setLoadCompleted(true);
        } else {
            console.log(data);
            setTriggerError(true);
        }
    }

    useEffect(async () => {
        await _browseProductsByFarmer();
    }, []);

    function ProductEntry(props) {
        const [show, setShow] = useState(false);
        const handleClose = () => setShow(false);
        const handleShow = () => setShow(true);
        const [showSuccess, setShowSuccess] = useState(null);
        const [showError, setShowError] = useState(null)

        let currentlyConfirmed = availabilities.filter(x => x.id === props.product.productId).length > 0 ?
            availabilities.filter(x => x.id === props.product.productId)[0].quantityAvailable : 0

        /*
        const handleConfirm = (quantity) => {
            if(quantity===undefined)
                quantity = props.product.quantityForecast
            if (availabilities.filter(x => x.id == props.product.productId).length === 0) {
                setAvailabilities(x => [...x, {
                    'id': props.product.productId,
                    'quantityAvailable': quantity
                }])
            } else {
                let newAvailabilities = [...availabilities];
                let index = availabilities.findIndex(x => x.id == props.product.productId)
                newAvailabilities[index].quantityAvailable = quantity;
                setAvailabilities(x => newAvailabilities);
            }
        }
        */

        return (
            <>
                <Card sx={{maxWidth: 345}}>
                    <CardMedia
                        component="img"
                        height="140"
                        image={props.product.imageUrl == null ? fruit : props.product.imageUrl}
                        alt="fruit"
                    />
                    <CardContent>
                        <Typography gutterBottom variant="h5" component="div">
                            {props.product.name}
                        </Typography>
                        <Typography variant="body">
                            {props.product.quantityForecast} {props.product.unitOfMeasurement} currently
                            forecasted <br/>
                            {currentlyConfirmed} {props.product.unitOfMeasurement} confirmed <br/>
                        </Typography>
                    </CardContent>
                    <CardActions>
                        <Grid container>
                            <Grid item xs={12}> <Button className="card-button" variant="success"
                                                        onClick={() => handleConfirm(props.product.productId, props.product.quantityForecast)}> Confirm
                                Forecasted
                                Availability </Button></Grid>
                            <Grid item xs={12}> <Button className="card-button" variant="success"
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
                            <img src={props.product.imageUrl == null ? fruit : props.product.imageUrl} alt="fruit"
                                 style={{width: '150px', height: '150px'}}/>
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
                                            <Button style={{margin: '20px'}} type="submit" variant="success">Set Availability</Button>
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
                            //TODO: Check correctness
                            await _browseProductsByFarmer();
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
            <h1>Confirm availability for the next week</h1>
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
            <Link to='/Dashboard'><Button style={{position: 'fixed', bottom: '10px', right: '10px'}}
                                          variant='secondary'>Back</Button></Link>
            <pre>
            {
                JSON.stringify(availabilities, null, 2)
            }</pre>
        </Container>
    )
}

export {ConfirmAvailability}

