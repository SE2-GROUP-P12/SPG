import 'bootstrap/dist/css/bootstrap.min.css';
import '../App.css';
import Container from "react-bootstrap/Container";
import {useState, useEffect} from "react";
import {API} from "../API/API";
import fruit from "../resources/fruits.png" //Recheck the original filename
import Button from "react-bootstrap/Button";
import Col from "react-bootstrap/Col";
import Row from "react-bootstrap/Row";
import Spinner from "react-bootstrap/Spinner";
import Alert from "react-bootstrap/Alert";
import {Modal} from "react-bootstrap";
import {Link, Redirect} from "react-router-dom";
import {Formik, Form, Field} from 'formik';
import * as Yup from 'yup';
import Card from '@mui/material/Card';
import CardActions from '@mui/material/CardActions';
import CardContent from '@mui/material/CardContent';
import CardMedia from '@mui/material/CardMedia';
import Typography from '@mui/material/Typography';
import Grid from '@mui/material/Grid';


function ProductsForecast(props) {
    const [products, setProducts] = useState([]);
    const [loadCompleted, setLoadCompleted] = useState(false);
    const [triggerError, setTriggerError] = useState(false);
    const email = localStorage.getItem("username");
    const [forecastTime, setForecastTime] = useState(true);

    async function _browseProductsByFarmer() {
        const data = await API.browseProductsByFarmer({'email': email}, props.setErrorMessage)
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
        let checkTime = (time, date) => {
            console.log("CHECKTIME FORECAST TIME: " + time + " " + date);
            if ((date === 'Sat' && time >= '09:00') ||  (date === 'Sun') || (date === 'Mon' && time < '09:00'))
                setForecastTime(false);
            else
                setForecastTime(true);
        }
        checkTime(props.time, props.date);
    }, [props.date, props.time]);

    function ProductEntry(PEprops) {
        const [show, setShow] = useState(false);
        const handleClose = () => setShow(false);
        const handleShow = () => setShow(true);
        const [showSuccess, setShowSuccess] = useState(null);
        const [showError, setShowError] = useState(null)

        let encodedImg = PEprops.product.base64Image

        return (
            <>
                <Card sx={{maxWidth: 345}}>
                    <CardMedia
                        component="img"
                        height="140"
                        image={PEprops.product.imageUrl == null ? fruit : `data:image/png;base64, ${encodedImg}`}
                        alt="fruit"
                    />
                    <CardContent>
                        <Typography gutterBottom variant="h5" component="div">
                            {PEprops.product.name}
                        </Typography>
                        <Typography variant="body">
                            {PEprops.product.quantityForecast} {PEprops.product.unitOfMeasurement} currently
                            forecasted <br/>
                            {PEprops.product.price.toFixed(2)}€/{PEprops.product.unitOfMeasurement}
                        </Typography>
                    </CardContent>
                    <CardActions>
                        <Grid container>
                            <Grid item xs={12}> <Button disabled={!forecastTime} variant="success" onClick={handleShow}> Modify
                                Forecast </Button>
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
                        <Modal.Title>Modify Forecast for {PEprops.product.name}</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        <div id="container" className="pagecontent" align='center'>
                            <img src={PEprops.product.imageUrl == null ? fruit : `data:image/png;base64, ${encodedImg}`}
                                 alt="fruit"
                                 style={{height: '150px'}}/>
                            <Row>
                                <Col xs={12}>
                                    {PEprops.product.quantityForecast} {PEprops.product.unitOfMeasurement} currently
                                    forecasted
                                </Col>
                            </Row>
                            <Row>
                                <Formik
                                    initialValues={{amount: 0}}
                                    validationSchema={Yup.object({amount: Yup.number().min(0).required('Amount required!')})}
                                    onSubmit={async (values) => {
                                        let data = {
                                            "productId": PEprops.product.productId,
                                            "quantity": values.amount
                                        }
                                        let outcome = await API.modifyForecast(data);
                                        if (outcome === true)
                                            setShowSuccess("Forecast successfully modified");
                                        else
                                            setShowError("Something went wrong");
                                    }}
                                    validateOnChange={false}
                                    validateOnBlur={false}
                                >
                                    {({values, errors, touched}) =>
                                        <Form>
                                            <label htmlFor='amount'>Amount:</label>
                                            <Field type="number" id="amount" name="amount" data-testid='amount'
                                                   min={0}/> {PEprops.product.unitOfMeasurement}
                                            <br/>
                                            <Button disabled={!forecastTime} style={{margin: '20px'}} type="submit" variant="success">Modify
                                                Forecast</Button>
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
            <h1>Products List</h1>
            {forecastTime ? null :
                <Alert variant='warning'> It's possible to do forecasts for products only from Monday at 9am to Saturday at
                    9am</Alert>}
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
        </Container>
    )
}

export {ProductsForecast}