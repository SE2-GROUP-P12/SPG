import 'bootstrap/dist/css/bootstrap.min.css';
import './../App.css';
import Container from "react-bootstrap/Container";
import {useState, useEffect} from "react";
import {API} from "./../API/API";
import fruit from "./../resources/fruits.png" //Recheck the original filename
import Button from "react-bootstrap/Button";
import Col from "react-bootstrap/Col";
import Row from "react-bootstrap/Row";
import Spinner from "react-bootstrap/Spinner";
import Alert from "react-bootstrap/Alert";
import {Modal, Offcanvas} from "react-bootstrap";
import {Link, Redirect, useHistory} from "react-router-dom";
import {Formik, Form, Field} from 'formik';
import * as Yup from 'yup';
import Card from '@mui/material/Card';
import CardActions from '@mui/material/CardActions';
import CardContent from '@mui/material/CardContent';
import CardMedia from '@mui/material/CardMedia';
import Typography from '@mui/material/Typography';
import Grid from '@mui/material/Grid';


import {printOrder} from "../PlaceOrder/PlaceOrder";

function BrowseProducts(props) {
    const [products, setProducts] = useState([]);
    const [loadCompleted, setLoadCompleted] = useState(false);
    const [triggerError, setTriggerError] = useState(false);
    const [cart, setCart] = useState([]);
    const [error, setError] = useState(false);
    const [sellTime, setSellTime] = useState(true);

    const history = useHistory();

    async function _browseProducts() {
        const data = await API.browseProducts(props.setErrorMessage);
        if (data != null) {
            setProducts(data['data']);
            setLoadCompleted(true);
        } else {
            console.log(data);
            setTriggerError(true);
        }
    }

    useEffect(async () => {
        await _browseProducts();
        let checkTime = (time, date) => {
            console.log("CHECKTIME SELL PRODUCTS: " + time + " " + date);
            if ((date === 'Sun' && time >= '23:00') || (date === 'Mon' && time < '09:00'))
                setSellTime(false);
            else
                setSellTime(true);
        }
        checkTime(props.time, props.date);
    }, [props.date, props.time]);

    async function _getCart() {
        const prod = await API.getCart({'email': localStorage.getItem("username")}, props.setErrorMessage);
        if (prod === null) {
            setError(true);
            setCart([]);
        } else
            setCart(prod);
    }

    useEffect(async () => {
        if (localStorage.getItem("username") != null)
            await _getCart();
    }, []);

    function ProductEntry(pe_props) {
        const [show, setShow] = useState(false);
        const handleClose = () => setShow(false);
        const handleShow = () => setShow(true);
        const [showSuccess, setShowSuccess] = useState(null);
        const [showError, setShowError] = useState(null)

        let encodedImg = pe_props.product.base64Image;
        return (
            <>
                <Card sx={{maxWidth: 345}}>
                    <CardMedia
                        component="img"
                        height="140"
                        image={pe_props.product.imageUrl == null ? fruit : `data:image/png;base64, ${encodedImg}`}
                        alt="fruit"
                    />
                    <CardContent>
                        <Typography gutterBottom variant="h5" component="div">
                            {pe_props.product.name}
                        </Typography>
                        <Typography variant="body">
                            {pe_props.product.quantityAvailable}{pe_props.product.unitOfMeasurement} available <br/>
                            {pe_props.product.price.toFixed(2)}€/{pe_props.product.unitOfMeasurement}
                        </Typography>
                    </CardContent>
                    {props.isLogged ? <CardActions>
                        <Grid container>
                            <Grid item xs={12}> <Button id={`button-add-${pe_props.product.name}`} disabled={!sellTime} variant="success" onClick={handleShow}> Add to cart </Button>
                            </Grid>
                        </Grid>
                    </CardActions> : <></>}
                </Card>

                <Modal show={show} onHide={() => {
                    handleClose();
                    setShowError(null);
                    setShowSuccess(null);
                }}>
                    <Modal.Header>
                        <Modal.Title>Add product to cart</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        <div id="container" className="pagecontent" align='center'>
                            <img
                                src={pe_props.product.imageUrl == null ? fruit : `data:image/png;base64, ${encodedImg}`}
                                alt="fruit"
                                style={{height: '150px'}}/>
                            <Row>
                                <Col xs={12}>
                                    {pe_props.product.name} : {pe_props.product.quantityAvailable}{pe_props.product.unitOfMeasurement} available, {pe_props.product.price.toFixed(2)}€/{pe_props.product.unitOfMeasurement}
                                </Col>
                            </Row>
                            <Row>
                                <Formik
                                    initialValues={{amount: 0}}
                                    validationSchema={Yup.object({amount: Yup.number().min(0).max(pe_props.product.quantityAvailable).required('Amount required!')})}
                                    onSubmit={async (values) => {
                                        let outcome = await API.addToCart({
                                            "productId": pe_props.product.productId,
                                            "email": localStorage.getItem("username"),
                                            "quantity": values.amount
                                        });
                                        if (outcome === true) {
                                            setShowSuccess("Product added successfully");
                                            await _getCart();
                                        } else
                                            setShowError("Something went wrong");
                                    }}
                                    validateOnChange={false}
                                    validateOnBlur={false}
                                >
                                    {({values, errors, touched}) =>
                                        <Form>
                                            <label htmlFor='amount'>Amount:</label>
                                            <Field type="number" id="amount" name="amount"
                                                   max={pe_props.product.quantityAvailable}
                                                   min={0}/> {pe_props.product.unitOfMeasurement}
                                            <br/>
                                            <Button id="button-add-to-cart" disabled={!sellTime} style={{margin: '20px'}} type="submit" variant="success">Add to
                                                cart</Button>
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
                        <Button id="button-close" variant="secondary" onClick={() => {
                            handleClose();
                            setShowError(null);
                            setShowSuccess(null);
                            _browseProducts();
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

    function CartView() {
        const [show, setShow] = useState(false);
        const handleClose = () => setShow(false);
        const handleShow = () => setShow(true);

        return (<>
            {error ?
                <Button id="button-basket" variant="warning" size="lg" //onClick={handleShow}
                        style={{position: 'fixed', bottom: '10px', left: '10px'}}>
                    Error loading cart
                </Button>
                :
                <Button id="button-basket" variant="success" size="lg" onClick={handleShow}
                        style={{position: 'fixed', bottom: '10px', left: '10px'}}>
                    🛒 {cart.length} item(s)
                </Button>}
            <Offcanvas show={show} onHide={handleClose}>
                <Offcanvas.Header><h2>Your Cart</h2></Offcanvas.Header>
                <Offcanvas.Body>
                    {error === true ? <Alert variant='danger'>Something went wrong</Alert> : printOrder(cart)}
                    <Button style={{margin: '20px'}} variant="secondary" onClick={handleClose}>Close</Button>
                    <Link to="/PlaceOrder"><Button id="button-checkout" style={{margin: '20px'}} variant="success">Check out</Button></Link>
                </Offcanvas.Body>
            </Offcanvas>
        </>);
    }

    return (
        <Container fluid>
            <h1>Products List</h1>
            {sellTime ? null :
                <Alert variant='warning'> It's not possible to buy products from Sunday at 23pm to Monday at 9am</Alert>}
            <Grid container spacing={2}>
                {
                    loadCompleted === true && products != undefined ?
                        products.map((prod, index) =>
                            <Grid item xs={12} sm={6} md={4} align="center">
                                <ProductEntry key={index} product={prod} loggedUser={props.loggedUser}></ProductEntry>
                            </Grid>)
                        :
                        <Grid xs={12} item align="center">
                            <Spinner animation="border" role="status">
                                <span className="visually-hidden">Loading...</span>
                            </Spinner>
                        </Grid>
                }
            </Grid>
            {props.isLogged ? <CartView/> : <></>}
            <Button onClick={() => history.goBack()} style={{position: 'fixed', bottom: '10px', right: '10px'}}
                    variant='secondary'>Back</Button>
        </Container>
    );
}


export {BrowseProducts}