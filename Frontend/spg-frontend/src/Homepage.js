import 'bootstrap/dist/css/bootstrap.min.css';
import './App.css';
import farmer from "./resources/farmer.jpg";
import Col from "react-bootstrap/Col";
import Row from "react-bootstrap/Row";
import italymap from "./resources/map.PNG";

function Homepage() {
    return (
        <div>
            <img src={farmer} className="farmer" alt="farmer"/>
            <Row className="findus">
            <Col xs={6}>
                <img src={italymap} className="italymap" alt="italymap" />
            </Col>
                <Col className="findus">
                    <h1>Come and find us!</h1>
                    <h3>We are in:</h3>
                    <h4><ul>
                        <li>Roma</li>
                        <li>Vicenza</li>
                        <li>Biella</li>
                        <li>Napoli</li>
                        <li>Torino</li>
                        <li>Catania</li>
                    </ul></h4>
                </Col>
            </Row>
        </div>);
}

export {Homepage}