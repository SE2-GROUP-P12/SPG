import 'bootstrap/dist/css/bootstrap.min.css';
import '../App.css';
import {Link, Redirect} from 'react-router-dom';
import {useEffect, useState} from 'react';
import {Spinner, Button} from 'react-bootstrap';
import {API} from '../API/API.js';
import {printOrder} from "../Utilities";

function UnpickedOrders(props) {

    const [orders, setOrders] = useState([]);
    const [loading, setLoading] = useState(true);
    const [weekly, setWeekly] = useState (true); //if false display monthly

    useEffect(async ()=>{
        let unretrieved = await API.getUnretrievedOrders();
        setOrders(unretrieved);
        setLoading(false);
    }, [])

    function listOrders(weekly) {
        let output = [];
        if(orders === null || orders.length===0)
            return (<h2>No unpicked orders in this time period! 🎉</h2>);
        for(let o of orders)
        {
            if(weekly && inThisWeek(o.currentStatusDate, props.dateTime))
                continue;
            output.push(<li className="list-group-item">
                {printOrder(o.productList)}
                Customer: {o.email}<br/>
                Since: {o.currentStatusDate.substring(8,10)+"/"+o.currentStatusDate.substring(5,7)+"/"+o.currentStatusDate.substring(0,4)}
            </li>);
        }
        if(output.length===0)
            output.push(<h2>No unpicked orders in this time period! 🎉</h2>);
        return output;
    }

    function inThisWeek (a,b)
    {
        b = new Date(b); //from milliseconds to datetime
        b = new Date(b.getFullYear(), b.getMonth(), b.getDate()); //from datetime to date
        let aYear = a.substring(0,4);
        let aMonth = a.substring (5,7);
        let aDay = a.substring(8,10);
        a = new Date(aYear+"-"+aMonth+"-"+aDay);
        const diffTime = Math.abs(a - b);
        const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
        return diffDays>7;
    }

    function buttonText()
    {
        if(weekly)
            return ("Show Last Month");
        return ("Show Last Week");
    }

    if(localStorage.getItem("role")!=='ADMIN')
        return (<Redirect to="/ErrorHandler" />);
    return (
        <>
            <h1>Unpicked orders</h1>
            {loading ?
                <Spinner animation="border" role="status">
                    <span className="visually-hidden">Loading...</span>
                </Spinner>
                :
                <div id="container" className="pagecontent">
                    <Button style={{margin:'2%'}} variant='outline-success' onClick={()=>{setWeekly(!weekly)}} >{buttonText()}</Button>
                    {listOrders(weekly)}
                </div>
            }
            <Link to='/Dashboard'><Button style={{margin: '20px'}} variant='secondary'>Back</Button></Link>
        </>
    );
}

export { UnpickedOrders }