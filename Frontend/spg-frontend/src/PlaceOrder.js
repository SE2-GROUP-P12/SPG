import 'bootstrap/dist/css/bootstrap.min.css';
import './App.css';
import {useState, useEffect} from 'react';
import {Link} from 'react-router-dom';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import Button from 'react-bootstrap/Button';
import Alert from 'react-bootstrap/Alert';
import {Formik, Form, Field} from 'formik';
import * as Yup from 'yup';
import {API} from './API';

function PlaceOrder(props)
{
    const [customer, setCustomer]=useState(null);
    const [order, setOrder] = useState([]);
    const [error, setError] = useState(false);
    const [customerError, setCustomerError] = useState(false);

    useEffect(()=>{
        let email = 'mario.rossi@gmail.com' //dovrebbe essere recuperata dalla sessione
        let getCartInfo = async (email) => {
            let prod = await API.getCart({'email': email});
            if(prod===undefined)
            {    
                setError(true);
                setOrder([]);
            }
            else    
                setOrder(prod);
        };
        getCartInfo(email);}, []);

        const droporder = () =>{
            setOrder([]);
        }

    /*TIME MACHINE MANAGEMENT*/  
    const [itsTime, setItsTime] = useState(false)  
    useEffect(()=>{
        let checkTime = (time, date) =>
        {
            if((date==='Sat' && time>='09:00')||(date==='Sun' && time<'23:00'))
                setItsTime(true);
            else
                setItsTime(false);
        }
        checkTime(props.time, props.date);
    }, [props.date, props.time])

    return(
        <>
        <h1>Place Order</h1>
            {itsTime ? null : <Alert variant='warning'> It's possible to place orders only from Saturday at 9am to Sunday at 11pm</Alert>}
            {error ? <Alert variant='danger'>Something went wrong, couldn't retrieve order</Alert> : null}
            {order===[] && !error ? 
            <div id="container" className="pagecontent">
                <h2>The cart is empty </h2>
            </div>
            : <>
            <div id="container" className="pagecontent">
                <ul className="list-group">{printOrder(order)}</ul> 
            </div>
            <div id="container" className="pagecontent">
            <h2>Whose order is this?</h2>    
            <Formik
                initialValues={{email:''}}
                validationSchema={Yup.object({
                    email: Yup.string().email().required()
                })}
                onSubmit={async(values)=>{
                    setCustomerError(false);
                    setCustomer(null);
                    let presentEmail = await API.customerExistsByMail(values.email).then(setCustomerError(!presentEmail));
                    console.log("CHECKPOINT, EMAIL:"+values.email+" ORDER:"+JSON.stringify(order)+" itsTime:"+itsTime);
                    if(presentEmail)
                    {
                        console.log("CHECKPOINT, EMAIL:"+values.email+" ORDER:"+JSON.stringify(order)+" itsTime:"+itsTime);
                        setCustomer(values.email);
                    }
                }}
                validateOnChange={false}
                validateOnBlur={false}
            >
                {({values, errors, touched})=>
                        <Form>
                            Email:<Field style={{margin: '20px'}} name="email" type="text"/>
                            <Button style={{margin: '20px'}} type="submit" variant="success">Submit customer</Button>
                            {errors.email && touched.email ? errors.email : null}
                            {customerError ? <Alert variant='danger'> User not found </Alert> : null}
                        </Form>
                }
            </Formik>
            </div>
            </>}
            <Row>
                <Col xs={4}><Button disabled={(!itsTime||order===[]||order===undefined||customer===null) ? true : false} variant='success'>Send order</Button></Col>
                <Col xs={4}><Button disabled={order===[]||order===undefined ? true : false} variant='danger' onClick={droporder}>Delete order</Button></Col>
                <Col xs={4}><Link to='/ShopEmployee'><Button variant='secondary'>Back</Button></Link></Col>
            </Row>
        </>
    );
}

function printOrder(prod)
{
    let output=[];
    let total=0;
    for(let p of prod)
    {
        output.push(<OrderEntry product={p}/>);
        total+=p.price*p.quantity;
    }
    output.push(
        <li className='list-group-item'>
            <h2>Total: {total} €</h2>
        </li>
    )
    return output;
}

function OrderEntry(props)
{
    return (
        <li className="list-group-item">
            {props.product.name} : {props.product.quantity}{props.product.unitOfMeasurement}<br/> 
            SUBTOTAL: {props.product.price*props.product.quantity}€
        </li>
        );
}


export {PlaceOrder, printOrder}