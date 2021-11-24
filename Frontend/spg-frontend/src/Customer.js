import 'bootstrap/dist/css/bootstrap.min.css';
import './App.css';
import Button from "react-bootstrap/Button"
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import {Link} from "react-router-dom";

function Customer(props) {
    return (
        <>
            <h1>Customer</h1>
            <div id="container" className="dashboard">
                <Row>
                    <Col xs={4}><Link to="/BrowseProducts"><Button className="dashButton" variant="outline-success"> <h1>Browse Products</h1> </Button></Link></Col>
                    <Col xs={4}><Link to='/PlaceOrder'><Button className="dashButton" variant="outline-success"> <h1>Place Order</h1> </Button></Link></Col>
                    <Col xs={4}><Link to='/DeliverOrder'><Button className="dashButton" variant="outline-success"> <h1>Deliver Order</h1> </Button></Link></Col>
                </Row>
            </div>
        </>);
}

export { Customer }