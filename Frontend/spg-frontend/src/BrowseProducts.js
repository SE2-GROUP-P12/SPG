import 'bootstrap/dist/css/bootstrap.min.css';
import './App.css';
import Container from "react-bootstrap/Container";
import { useState, useEffect } from "react";
import { API } from "./API";
import fruit from "./resources/fruits.png" //Recheck the original filename
import Button from "react-bootstrap/Button";
import Col from "react-bootstrap/Col";
import Row from "react-bootstrap/Row";
import Spinner from "react-bootstrap/Spinner";
import Alert from "react-bootstrap/Alert";
import { Modal } from "react-bootstrap";
import { Link } from "react-router-dom";
import { Formik, Form, Field } from 'formik';
import * as Yup from 'yup';
import Card from '@mui/material/Card';
import CardActions from '@mui/material/CardActions';
import CardContent from '@mui/material/CardContent';
import CardMedia from '@mui/material/CardMedia';
import Typography from '@mui/material/Typography';
import Grid from '@mui/material/Grid';

function BrowseProducts() {
    const [products, setProducts] = useState([]);
    const [loadCompleted, setLoadCompleted] = useState(false);
    const [productsErr, setProductsErr] = useState(false);

    async function _browseProducts() {
        setProductsErr(false);
        const data = await API.browseProducts();
        if(data===undefined)
            setProductsErr(true)
        else
            setProducts(data);
        setLoadCompleted(true);
        return;
    }

    useEffect(async () => {
        await _browseProducts();
    }, []);



    function ProductEntry(props) {
        const [show, setShow] = useState(false);
        const handleClose = () => setShow(false);
        const handleShow = () => setShow(true);
        const [showSuccess, setShowSuccess] = useState(null);
        const [showError, setShowError] = useState(null)

        return (
            <>
                <Card sx={{ maxWidth: 345 }}>
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
                        {props.product.quantityAvailable}{props.product.unitOfMeasurement} available <br/>
                        {props.product.price}€/{props.product.unitOfMeasurement}
                        </Typography>
                    </CardContent>
                    <CardActions>
                        <Grid container>
                            <Grid item xs={12}> <Button variant="success" onClick={handleShow}> Add to cart </Button> </Grid>
                        </Grid>
                    </CardActions>
                </Card>

                <Modal show={show} onHide={() => { handleClose(); setShowError(null); setShowSuccess(null); }}>
                    <Modal.Header closeButton>
                        <Modal.Title>Add product to cart</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        <div id="container" className="pagecontent" align='center'>
                            <img src={fruit} alt="fruit" style={{ width: '150px', height: '150px' }} />
                            <Row>
                                <Col xs={12}>
                                    {props.product.name} : {props.product.quantityAvailable}{props.product.unitOfMeasurement} available, {props.product.price}€/{props.product.unitOfMeasurement}
                                </Col>
                            </Row>
                            <Row>
                                <Formik
                                    initialValues={{ amount: 0 }}
                                    validationSchema={Yup.object({ amount: Yup.number().min(0).max(props.product.quantityAvailable).required('Amount required!') })}
                                    onSubmit={async (values) => {
                                        let outcome = await API.addToCart({
                                            "productId": props.product.productId,
                                            "email": 'mario.rossi@gmail.com',
                                            "quantity": values.amount
                                        });
                                        if (outcome === true)
                                            setShowSuccess("Product added successfully");
                                        else
                                            setShowError("Something went wrong");
                                    }}
                                    validateOnChange={false}
                                    validateOnBlur={false}
                                >
                                    {({ values, errors, touched }) =>
                                        <Form>
                                            Amount: <Field type="number" id="amount" name="amount" max={props.product.quantityAvailable} min={0} /> {props.product.unitOfMeasurement}
                                            <br />
                                            <Button style={{ margin: '20px' }} type="submit" variant="success">Add to
                                                cart</Button>
                                            {errors.amount && touched.amount ? errors.amount : null}
                                            {showSuccess !== null ? <Alert variant='success'>{showSuccess}</Alert> : null}
                                            {showError !== null ? <Alert variant='danger'>{showError}</Alert> : null}
                                        </Form>
                                    }
                                </Formik>
                            </Row>
                        </div>
                    </Modal.Body>
                    <Modal.Footer>
                        <Button variant="secondary" onClick={() => { handleClose(); setShowError(null); setShowSuccess(null); window.location.reload(false); }}>
                            Close
                        </Button>
                    </Modal.Footer>
                </Modal>
            </>
        );
    }

    return (
        <Container fluid>
            <h1>Products List</h1>
            <Grid container spacing={2} >
                {productsErr ? <Grid xs={12} item align="center"><Alert variant='danger'> There has been an error contacting the server</Alert></Grid> : 
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
            <Link to='/ShopEmployee'><Button style={{ margin: '20px' }} variant='secondary'>Back</Button></Link>
        </Container>
    );
}

function printProducts(prod) {
    let output = [];
    for (let p of prod) {
        output.push(<ProductEntry product={p} />)
    }
    return output;
}




export { BrowseProducts }