import Container from "react-bootstrap/Container";
import Button from "react-bootstrap/Button";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import Image from "react-bootstrap/Image";
import Alert from "react-bootstrap/Alert";
import gandalfGif from "../resources/gandal.gif";
import serverFailure from "../resources/serverFailure.gif";
import {useState} from "react";
import {Redirect} from "react-router-dom";


function UnauthorizedComponent(props) {
    const [show, setShow] = useState(false);
    if (props.errorMessage === undefined) {
        return (<Redirect to ="/"/>);
    }
    return (
        <Container fluid>
            <h1 className="mt-4">
                <b>{props.errorMessage['status'] !== 500 ? "YOU SHALL NOT PASS!" : "SERVER IS OFFLINE"}</b></h1>
            <Row className="align-center mt-4">
                <Col>
                    <Image alt='err' src={props.errorMessage['status'] === 500 ? serverFailure : gandalfGif} rounded fluid/>
                </Col>
            </Row>
            <Row className="mt-4 row-md">
                <Col className="col-md">
                    <Alert show={show} variant="dark">
                        <Alert.Heading>
                            <h2>BACKEND RESPONSE REVIEW</h2>
                        </Alert.Heading>
                        <Container className="mt-4 mb-4">
                            <Row className="justify-content-md-center border-bottom border-dark mt-5">
                                <Col>
                                    <h4><b>STATUS: </b></h4>
                                </Col>
                                <Col>
                                    <h4>
                                        {props.errorMessage['status']}
                                    </h4>
                                </Col>
                            </Row>
                            <Row className="justify-content-md-center border-bottom border-dark mt-5">
                                <Col>
                                    <h4><b>ERROR MESSAGE: </b></h4>
                                </Col>
                                <Col>
                                    <h4>
                                        {props.errorMessage['errorMessage']}
                                    </h4>
                                </Col>
                            </Row>
                            <Row className="justify-content-md-center border-bottom border-dark mt-5">
                                <Col>
                                    <h4><b>POSSIBLE MITIGATION: </b></h4>
                                </Col>
                                <Col>
                                    <h4>
                                        {props.errorMessage['mitigation']}

                                    </h4>
                                </Col>
                            </Row>
                        </Container>
                        <hr/>
                        <div className="d-flex justify-content-end">
                            <Button onClick={() => setShow(false)} variant="outline-dark">
                                hide review
                            </Button>
                        </div>
                    </Alert>
                    {!show && <Button variant="dark" onClick={() => setShow(true)}>MORE INFO</Button>}
                </Col>
            </Row>
        </Container>

    )
}

export {UnauthorizedComponent};
