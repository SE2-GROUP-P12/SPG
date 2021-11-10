import 'bootstrap/dist/css/bootstrap.min.css';
import './App.css';
import Button from 'react-bootstrap/Button';
import Alert from 'react-bootstrap/Alert';
import { useState, useEffect } from 'react';
import {Formik, Form, Field} from 'formik';
import { printOrder} from './PlaceOrder';
import * as Yup from 'yup';
import {Link} from 'react-router-dom';

function DeliverOrder(props)
{
    const [user, setUser]=useState(null);
    const [orders, setOrders] = useState([1,2,3]);

    /*useEffect(()=>{
        let getOrdersInfo = async () => {
            API.browseOrders().then(ord => setOrders(ord));
        };
        getOrdersInfo();}, [user] );*/

    /*TIME MACHINE MANAGEMENT*/  
    const [itsTime, setItsTime] = useState(false)  
    useEffect(()=>{
        let checkTime = (time, date) =>
        {
            console.log("CHECKTIME DELIVERORDER: "+time+" "+date);
            if((date==='Wed' && time>='09:00')||(date==='Fri' && time<'23:00')||(date==='Thu'))
                setItsTime(true);
            else
                setItsTime(false);
        }
        checkTime(props.time, props.date);
    }, [props.date, props.time])

    return(
        <>
        <h1>Deliver Order</h1>
        {itsTime ? null : <Alert variant='warning'> It's possible to deliver orders only from Wednesday at 9am to Friday at 11pm</Alert>}
        <div id="container" className="pagecontent">
        <Formik
            initialValues={{
                email:''
            }}
            validationSchema={Yup.object({
                email: Yup.string().email().required()
            })}
            onSubmit={(values)=>{setUser(values.email)}}
            validateOnChange={false}
            validateOnBlur={false}
        >
        {({values, errors, touched})=>
            <Form>
            Email:<Field style={{margin: '20px'}} name="email" type="text"/>
            <Button style={{margin: '20px'}} type="submit" variant="success" disabled={itsTime ? false : true} >Submit customer</Button>
            {errors.email && touched.email ? errors.email : null}                
            </Form>}
        </Formik>
        {user===null ? null : <Orders itsTime={itsTime} orderList={orders}/>}
        </div>
        <Link to='/ShopEmployee'><Button style={{margin: '20px'}} variant='secondary'>Back</Button></Link>
        </>
    );
}

function Orders(props)
{
    let output=[];
    for(let o of props.orderList)
    {
        o={"orderId":1,
            "email":"customer@gmail.com",
            "productList" : 
              [
                {
                  "productId":"1",
                  "name": "Apples",
                  "producer" : "Tonio Cartonio s.p.a.",
                  "unit":"kg",
                  "unit price" : "1.99",
                  "amount" : "10" //ordered amount 
                },
                {
                  "productId":"2",
                  "name": "Eggs",
                  "producer" : "KFC farms", 
                  "unit":"unit",
                  "unit price" : "0.10",
                  "amount" : "6" 
                }
              ] 
          };
        output.push(
        <li className="list-group-item">  
            {printOrder(o.productList)}
            User: {o.email}<Button style={{margin: '20px'}} type='submit' variant='success' disabled={props.itsTime ? false : true} >Deliver</Button> 
        </li>
        )
    }
    return output;
}

export {DeliverOrder}