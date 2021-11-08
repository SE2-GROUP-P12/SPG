import 'bootstrap/dist/css/bootstrap.min.css';
import './App.css';
import Button from "react-bootstrap/Button"
import Container from "react-bootstrap/Container";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import {Link} from "react-router-dom";

function ShopEmployee()
{
    return(
        <>
        <h1>Shop Employee</h1>
        <div id="container" className="dashboard">
            <Row>
            <Col xs={4}><Link to="/NewCustomer"><Button className="dashButton" variant="outline-success"> <h1>New Customer</h1> </Button></Link></Col>
                <Col xs={4}><Link to="/BrowseProducts"><Button className="dashButton" variant="outline-success"> <h1>Browse Products</h1> </Button></Link></Col>
            <Col xs={4}><Link to="/TopUp"><Button className="dashButton" variant="outline-success"> <h1>Top Up</h1> </Button></Link></Col>
        </Row>
        <Row>
            <Col xs={6}><Link to='/PlaceOrder'><Button className="dashButton" variant="outline-success"> <h1>Place Order</h1> </Button></Link></Col>
            <Col xs={6}><Link to='/DeliverOrder'><Button className="dashButton" variant="outline-success"> <h1>Deliver Order</h1> </Button></Link></Col>
        </Row>
    </div></>);
}

export {ShopEmployee}