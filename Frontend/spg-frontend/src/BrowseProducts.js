import 'bootstrap/dist/css/bootstrap.min.css';
import './App.css';
import Container from "react-bootstrap/Container";
import {useState, useEffect} from "react";
import {API} from "./API";
import fruit from "./resources/fruits.png";
import Button from "react-bootstrap/Button";
import Col from "react-bootstrap/Col";
import Row from "react-bootstrap/Row";
import {Modal} from "react-bootstrap";
import {Link} from "react-router-dom";
import {Formik, Form, Field} from 'formik';
import * as Yup from 'yup';

function BrowseProducts()
{
    const [products, setProducts] = useState([0]);

    /*useEffect(()=>{
        let getProductsInfo = async () => {
            API.browseProducts().then(prod => setProducts(prod));
        };
        getProductsInfo();}, [] );*/

    return(
        <>
        <h1>Products List</h1>
        <div id="container" className="pagecontent">
            {products===null ? <h2>Loading, please wait </h2> : <ul className="list-group">{printProducts(products)}</ul> }
            <Link to='/ShopEmployee'><Button style={{position: "absolute", right: "50px", bottom: "50px"}} variant='success'>Home</Button></Link>
        </div></>
    );
}

function printProducts(prod)
{
    let output=[];
    for(let p of prod)
    {
        p={"productId":"1",
        "name": "Apples",
        "producer" : "Tonio Cartonio s.p.a.",
        "unit":"kg",
        "unitPrice" : "1.99",
        "amount" : "100"}

        output.push(<ProductEntry product={p}/>)
    }
    return output;
}

function ProductEntry(props)
{
    const [show, setShow] = useState(false);
    const handleClose = () => setShow(false);
    const handleShow = () => setShow(true);

    //TODO: addToCart on submit

    return (
        <>
        <Modal show={show} onHide={handleClose}>
            <Modal.Header closeButton>
                <Modal.Title>Add product to cart</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <div id="container" className="pagecontent">
                <img src={fruit} alt="fruit" style={{width :'150px', height : '150px'}}/>
                <Row> {props.product.name} by {props.product.producer} : {props.product.amount}{props.product.unit} available, {props.product.unitPrice}€/{props.product.unit}</Row>
                <Row>
                    <Formik
                        initialValues={{amount: 0}}
                        validationSchema={Yup.object({amount: Yup.number().min(0).max(props.product.amount).required('Amount required!')})}
                        onSubmit={async (values) => {
                            console.log("SUBMITTED: "+values.amount);
                        }}
                        validateOnChange={false}
                        validateOnBlur={false}
                    >
                        {({values, errors, touched})=>
                            <Form>
                                Amount: <Field type="number" id="amount" name="amount"/> {props.product.unit} <br/>
                                <Button style={{margin : '20px'}} type="submit" variant="success">Add to cart</Button>
                                {errors.amount && touched.amount ? errors.amount : null}
                            </Form>
                        }
                    </Formik>
                </Row>
                </div>
            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={handleClose}>
                    Close
                </Button>
            </Modal.Footer>
        </Modal>
        <li className="list-group-item">
            <Row>
            <Col xs={2}> <img src={fruit} alt="fruit" style={{width :'50px', heigth : '50px'}}/> </Col>
            <Col xs={8}>{props.product.name} by {props.product.producer} : {props.product.amount}{props.product.unit} available, {props.product.unitPrice}€/{props.product.unit}</Col>
            <Col xs={2}><Button variant="success" onClick={handleShow}> Add to cart </Button> </Col>
            </Row>
        </li>
        </>
        );
}



export {BrowseProducts}