import 'bootstrap/dist/css/bootstrap.min.css';
import './App.css';
import Button from "react-bootstrap/Button"
import Container from "react-bootstrap/Container";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";

function ShopEmployee()
{
    return(
        <Container className="dashboard">
            <h1>Shop Employee</h1>
            <Row>
            <Col xs={4}><Button variant="outline-success"> <h1>New Customer</h1> </Button></Col>
            <Col xs={4}><Button variant="outline-success"> Browse Products </Button></Col>
            <Col xs={4}><Button variant="outline-success"> Top Up</Button></Col>
        </Row>
        <Row>
            <Col xs={6}><Button variant="outline-success"> Place Order </Button></Col>
            <Col xs={6}><Button variant="outline-success"> Deliver Order </Button></Col>
        </Row>
    </Container>);
}

export {ShopEmployee}