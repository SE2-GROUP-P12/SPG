import 'bootstrap/dist/css/bootstrap.min.css';
import './App.css';
import logo from "./resources/logo.png";
import Button from "react-bootstrap/Button";
import Nav from "react-bootstrap/Nav";
import {useState} from "react";

function Navbar(props) {
    const [location, setLocation] = useState(window.location);

    function doLogOut() {
        window.location.href = "http://localhost:8081/logout";
    }

    function doLogin() {
        window.location.href = "http://localhost:8081/login";
    }

    //Login presence button
    if (location == "http://localhost:3000/")
        return (
            <Nav className="navbar bg-success navbar-dark">
                <img src={logo} alt="logo" className="logo"/>
                <Button className="btn btn-outline-light" variant="success" onClick={() => doLogin()}> Log
                    in </Button>
            </Nav>
        );

    else if (location == "http://localhost:3000/Unauthorized")
        return (
            <Nav className="navbar bg-success navbar-dark">
                <img src={logo} alt="logo" className="logo"/>
                <Button className="btn btn-outline-light" variant="success" onClick={() => doLogin()}> Log
                    in </Button>
            </Nav>
        );

    else
        return (
            <Nav className="navbar bg-success navbar-dark">
                <img src={logo} alt="logo" className="logo"/>
                <Button variant="outline-light" onClick={() => doLogOut()}>Log out</Button>
            </Nav>
        );
}

export {Navbar}