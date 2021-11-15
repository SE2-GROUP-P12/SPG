import 'bootstrap/dist/css/bootstrap.min.css';
import './App.css';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import {useState} from 'react';
import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import {Formik, Form, Field} from "formik";
import * as Yup from "yup";
import {Link} from 'react-router-dom';
import {API} from "./API";
import Alert from 'react-bootstrap/Alert';

function TopUp()
{
    const [user, setUser]=useState(null);
    const [wallet, setWallet]=useState(0.00);
    const [show, setShow] = useState(false);
    const [error, setError] = useState(null);
    const [success, setSuccess] = useState(null);
    const [modalError, setModalError] = useState(null);
    const [modalSuccess, setModalSuccess] = useState(null);
    const handleClose = () => setShow(false);
    const handleShow = () => setShow(true);

    return (
        <>
        <h1>Top Up!</h1>
        <Formik
            initialValues={{
                email:''
            }}
            validationSchema={
                Yup.object({
                    email: Yup.string().email().required()
                })}
            onSubmit={async(values) => {
                setWallet(0);
                setSuccess(false);
                setError(false);
                setModalError(false);
                setModalSuccess(false);
                setUser(null);
                let outcome = await API.getWallet(values.email);
                if (outcome===undefined)
                    setError(true);
                else
                {   
                    setWallet(outcome);
                    setUser(values.email);
                    setSuccess(true);
                }
            }}
            validateOnChange={false}
            validateOnBlur={false}
        >
            {({values, errors, touched})=>
            <div id="container" className="pagecontent">
                <Form>
                    <Row>Email: <Field name="email" type="text"/></Row>
                    <Button style={{margin : '20px'}} type='submit' variant='success'>Submit</Button>
                    
                <Row>{errors.email && touched.email ? errors.email : null}</Row>
                {error ? <Alert variant='danger'>User not found</Alert> : null}
                {success ? <Alert variant='success'>User correctly found</Alert> : null}
                {modalError ? <Alert variant='danger'>Something went wrong during the top up</Alert> : null}
                {modalSuccess ? <Alert variant='success'>Top us correctly performed</Alert> : null}
                </Form>
            </div>
            }
        </Formik>
        <h2>User's wallet: {wallet}€</h2>
        <div id="container" className="dashboard">
            { user===null ? 
            <Row>
            <Col ><Button disabled onClick={handleShow} className="dashButton" variant="outline-success"> <h1>Cash</h1> </Button></Col>
            <Col ><Button disabled onClick={handleShow} className="dashButton" variant="outline-success"> <h1>Bancomat</h1> </Button></Col>
            <Col ><Button disabled onClick={handleShow} className="dashButton" variant="outline-success"> <h1>Gift Card</h1> </Button></Col>
            </Row> :
            <Row>
            <Col ><Button onClick={handleShow} className="dashButton" variant="outline-success"> <h1>Cash</h1> </Button></Col>
            <Col ><Button onClick={handleShow} className="dashButton" variant="outline-success"> <h1>Bancomat</h1> </Button></Col>
            <Col ><Button onClick={handleShow} className="dashButton" variant="outline-success"> <h1>Gift Card</h1> </Button></Col>
            </Row>}
    </div>
    <Link to='/ShopEmployee'><Button style={{margin: '20px'}} variant='secondary'>Back</Button></Link>
    <Modal show={show} onHide={handleClose}>
            <Modal.Header closeButton>
                <Modal.Title>Top Up</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <Formik
                    initialValues={{
                        amount:''
                    }}
                    validationSchema={
                        Yup.object({
                            amount: Yup.number().required()
                    })}
                    onSubmit={async(values)=>{
                        setSuccess(false);
                        setError(false);
                        setModalError(false);
                        setModalSuccess(false);
                        let outcome = await API.topUp(
                            {"email": user,
                            "value" : values.amount
                        });
                        if(outcome === true)
                        {
                            setWallet(wallet+values.amount);
                            setModalSuccess(true);
                        }
                        else
                            setModalError(true);
                        handleClose();
                    }}
                    validateOnChange={false}
                    validateOnBlur={false}
                >
                    {({values, errors, touched})=>
                    <div id="container">
                        <Form>
                            {errors.amount && touched.amount ? errors.amount : null} <br/>
                            Amount: <Field name="amount" type="number"/> €
                            <Button style={{margin : '20px'}} type='submit' variant='success'>Top Up</Button>
                        </Form>                
                    </div>
                    }
                </Formik>
            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={handleClose}>Close</Button>
            </Modal.Footer>
        </Modal>
        </>
    );
}

export {TopUp}