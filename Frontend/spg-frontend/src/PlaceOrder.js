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

function PlaceOrder(props)
{
    const [user, setUser]=useState(null);
    const [products, setProducts] = useState([0,1,2,4]);

    /*useEffect(()=>{
        let getCartInfo = async () => {
            API.getCart().then(prod => setCart(prod));
        };
        getCartInfo();}, [] );*/

        const dropCart = () => setProducts(null);

    /*TIME MACHINE MANAGEMENT*/  
    const [itsTime, setItsTime] = useState(false)  
    useEffect(()=>{
        let checkTime = (time, date) =>
        {
            console.log("CHECKTIME PLACEORDER: "+time+" "+date)
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
            {products===null ? 
            <div id="container" className="pagecontent">
                <h2>The cart is empty </h2>
            </div>
            : <>
            <div id="container" className="pagecontent">
                <ul className="list-group">{printOrder(products)}</ul> 
            </div>
            <div id="container" className="pagecontent">
            <h2>Whose order is this?</h2>    
            <Formik
                initialValues={{email:''}}
                validationSchema={Yup.object({
                    email: Yup.string().email().required()
                })}
                onSubmit={async(values)=>{
                    setUser(values.email);
                }}
                validateOnChange={false}
                validateOnBlur={false}
            >
                {({values, errors, touched})=>
                        <Form>
                            Email:<Field style={{margin: '20px'}} name="email" type="text"/>
                            <Button style={{margin: '20px'}} type="submit" variant="success">Submit customer</Button>
                            {errors.email && touched.email ? errors.email : null}
                            
                        </Form>
                }
            </Formik>
            </div>
            </>}
            <Row>
                <Col xs={4}><Button disabled={(!itsTime&&(products===null||user===null)) ? true : false} variant='success'>Send order</Button></Col>
                <Col xs={4}><Button disabled={products===null? true : false} variant='danger' onClick={dropCart}>Delete order</Button></Col>
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
        p={"productId":"1",
        "name": "Apples",
        "producer" : "Tonio Cartonio s.p.a.",
        "unit":"kg",
        "unitPrice" : "1.99",
        "amount" : "1"}

        output.push(<OrderEntry product={p}/>);
        total+=p.unitPrice*p.amount;
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
            {props.product.name} by {props.product.producer} : {props.product.amount}{props.product.unit}<br/> 
            SUBTOTAL: {props.product.unitPrice*props.product.amount}€
        </li>
        );
}


export {PlaceOrder, printOrder}