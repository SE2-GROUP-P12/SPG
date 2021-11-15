import 'bootstrap/dist/css/bootstrap.min.css';
import './App.css';
import {Link} from "react-router-dom";
import logo from "./resources/logo.png";
import Button from "react-bootstrap/Button";
import Nav from "react-bootstrap/Nav";

function Navbar(props) {
    function doLogOut() {
        window.location.href = "http://localhost:8080/logout";
    }

    function doLogin() {
        window.location.href = "http://localhost:8080/login";
    }


    return (
        <Nav className="navbar bg-success navbar-dark">
            <img src={logo} alt="logo" className="logo"/>
            {window.location != "http://localhost:3000/" ?
                    <Button variant="outline-light" onClick={() => doLogOut()}>Log out</Button>
                :
                <Button className="btn btn-outline-light" variant="success" onClick={() => doLogin()}> Log in </Button>
                //<Link to='/LoginComponent' ><Button className="btn btn-outline-light" variant="success"> Log in </Button></Link>
            }
        </Nav>
    );
}

export {Navbar}