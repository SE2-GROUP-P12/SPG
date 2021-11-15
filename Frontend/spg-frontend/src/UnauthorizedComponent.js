import Container from "react-bootstrap/Container";
import Button from "react-bootstrap/Button";

function UnauthorizedComponent(){
    return (
        <Container fluid>
            <h1>401 UNAUTHORIZED</h1>
            <h4>try to login before access the resources!</h4>
        </Container>

    )
}

export {UnauthorizedComponent};