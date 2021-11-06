import 'bootstrap/dist/css/bootstrap.min.css';
import './App.css';
import {Link} from "react-router-dom";
import logo from "./resources/logo.png";
import Button from "react-bootstrap/Button";
import Nav from "react-bootstrap/Nav";
//import {useState} from "react";

function doLogOut()
{
    console.log("log out");
}

function Navbar ()
{
    //const [logged, setLogged]=useState(null);
    const logged=null;
    return (
        <Nav className="navbar bg-success navbar-dark">
        <img src={logo} alt="logo" className="logo" />
            { logged!=null ? <Button variant="outline-light" onClick={()=>doLogOut()}>Log out</Button> : <Button className="btn btn-outline-light" variant="success"> Log in </Button> }
        </Nav>
    );
}

export {Navbar}