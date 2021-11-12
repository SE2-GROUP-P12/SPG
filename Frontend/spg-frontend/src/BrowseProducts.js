import 'bootstrap/dist/css/bootstrap.min.css';
import './App.css';
import Container from "react-bootstrap/Container";
import {useState, useEffect} from "react";
import {API} from "./API";
import fruit from "./resources/fruits.png" //Recheck the original filename
import Button from "react-bootstrap/Button";
import Col from "react-bootstrap/Col";
import Row from "react-bootstrap/Row";
import Spinner from "react-bootstrap/Spinner";
import Alert from "react-bootstrap/Alert";
import {Modal} from "react-bootstrap";
import {Link} from "react-router-dom";
import {Formik, Form, Field} from 'formik';
import * as Yup from 'yup';

function BrowseProducts() {
    const [products, setProducts] = useState([]);
    const [loadCompleted, setLoadCompleted] = useState(false);

    const _browseProducts = async () => {
        const data = await API.browseProducts();
        setProducts(data);
    }
    
    useEffect(() => {
        _browseProducts();
    }, []);

    function ProductEntry(props) {
        const [show, setShow] = useState(false);
        const handleClose = () => setShow(false);
        const handleShow = () => setShow(true);
        const [showSuccess, setShowSuccess] = useState(null);
        const [showError, setShowError] = useState(null)

        return (
            <>
                <Modal show={show} onHide={()=>{handleClose(); setShowError(null); setShowSuccess(null);}}>
                    <Modal.Header closeButton>
                        <Modal.Title>Add product to cart</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        <div id="container" className="pagecontent">
                            <img src={fruit} alt="fruit" style={{width: '150px', height: '150px'}}/>
                            <Row> {props.product.name} : {props.product.quantityAvailable}{props.product.unitOfMeasurement} available, {props.product.price}€/{props.product.unitOfMeasurement}</Row>
                            <Row>
                                <Formik
                                    initialValues={{amount: 0}}
                                    validationSchema={Yup.object({amount: Yup.number().min(0).max(props.product.quantityAvailable).required('Amount required!')})}
                                    onSubmit={async (values) => {
                                        let outcome = await API.addToCart({
                                            "productId" : props.product.productId,
                                            "email" : 'mario.rossi@gmail.com',
                                            "quantity" : values.amount
                                        });
                                        if(outcome===true)
                                            setShowSuccess("Product added successfully");
                                        else    
                                            setShowError("Something went wrong");
                                    }}
                                    validateOnChange={false}
                                    validateOnBlur={false}
                                >
                                    {({values, errors, touched}) =>
                                        <Form>
                                            Amount: <Field type="number" id="amount" name="amount" max={props.product.quantityAvailable} min={0}/> {props.product.unitOfMeasurement}
                                            <br/>
                                            <Button style={{margin: '20px'}} type="submit" variant="success">Add to
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
                        <Button variant="secondary" onClick={()=>{ handleClose(); setShowError(null); setShowSuccess(null); }}>
                            Close
                        </Button>
                    </Modal.Footer>
                </Modal>
                <li className="list-group-item">
                    <Row>
                        <Col xs={2}> <img src={fruit} alt="fruit" style={{width: '50px', heigth: '50px'}}/> </Col>
                        <Col
                            xs={8}>{props.product.name} : {props.product.quantityAvailable}{props.product.unitOfMeasurement} available, {props.product.price}€/{props.product.unitOfMeasurement}</Col>
                        <Col xs={2}><Button variant="success" onClick={handleShow}> Add to cart </Button> </Col>
                    </Row>
                </li>
            </>
        );
    }

    return (
        <Container fluid>
            <h1>Products List</h1>
            {
                loadCompleted === true ?
                    products.map((prod, index) => <ProductEntry key={index} product={prod}></ProductEntry>)
                    :
                    <Spinner animation="border" role="status">
                        <span className="visually-hidden">Loading...</span>
                    </Spinner>
            }

            <Link to='/ShopEmployee'><Button style={{margin: '20px'}} variant='secondary'>Back</Button></Link>
        </Container>
    );
}

function printProducts(prod) {
    let output = [];
    for (let p of prod) {
        output.push(<ProductEntry product={p}/>)
    }
    return output;
}




export {BrowseProducts}