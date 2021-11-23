import 'bootstrap/dist/css/bootstrap.min.css';
import './App.css';
import logo from "./resources/logo.png";
import Button from "react-bootstrap/Button";
import Nav from "react-bootstrap/Nav";
import {Link} from 'react-router-dom';
import {useState} from "react";

import {Switch, Route, BrowserRouter as Router} from "react-router-dom";

function Navbar(props) {
    const [redirectLocation, setRedirectLocation] = useState("/");

    function doLogOut() {
        //TODO: insert this fecth into the API file
        fetch("/api/logout", {
            method: 'GET',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                'Authorization': "Bearer " + sessionStorage.getItem("accessToken")
            }
        }).then(response => {
            //Logout succesful, clear local/session storage lato FE
            if (response.ok) {
                sessionStorage.removeItem("accessToken");
                sessionStorage.removeItem("refreshToken");
                localStorage.removeItem("role");
                localStorage.removeItem("username");
                window.location.href = "/";
            } else {
                //setRedirectLocation("/Unauthorized");
                //Problems occour
                window.location.href = "http://localhost:3000/Unauthorized";
            }
        })

    }

    return (
        <Switch>
            <Route exact path="/">
                <Nav className="navbar bg-success navbar-dark">
                    <img src={logo} alt="logo" className="logo"/>
                    <Link style={{color: 'white'}} to='/NewCustomer'>Sign in</Link>
                    <Link to="/LoginComponent">
                        <Button className="btn btn-outline-light" variant="success"> Log in </Button>
                    </Link>
                </Nav>
            </Route>
            <Route exact path="/Unauthorized">
                <Nav className="navbar bg-success navbar-dark">
                    <img src={logo} alt="logo" className="logo"/>
                    <Link style={{color: 'white'}} to='/NewCustomer'>Sign in</Link>
                    <Link to="/LoginComponent">
                        <Button className="btn btn-outline-light" variant="success"> Log in </Button>
                    </Link> </Nav>
            </Route>
            <Route exact path="/NewCustomer">
                <Nav className="navbar bg-success navbar-dark">
                    <img src={logo} alt="logo" className="logo"/>
                </Nav>
            </Route>
            <Route>
                <Nav className="navbar bg-success navbar-dark">
                    <img src={logo} alt="logo" className="logo"/>
                    <Button variant="outline-light" onClick={() => doLogOut()}>Log out</Button>
                </Nav>
            </Route>
        </Switch>
    );
}

export {Navbar}