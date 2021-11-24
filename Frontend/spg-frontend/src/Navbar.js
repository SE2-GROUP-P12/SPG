import 'bootstrap/dist/css/bootstrap.min.css';
import './App.css';
import logo from "./resources/logo.png";
import Button from "react-bootstrap/Button";
import Nav from "react-bootstrap/Nav";
import { Link } from 'react-router-dom';
import { useState } from "react";

import { Switch, Route, BrowserRouter as Router } from "react-router-dom";

function Navbar(props) {
    const [location, setLocation] = useState(window.location);

    function doLogOut() {
        window.location.href = "http://localhost:8080/logout";
    }

    function doLogin() {
        window.location.href = "http://localhost:8080/login";
    }

    /*Login presence button
    // PARTE DI RICK
    if (location == "http://localhost:3000/" || location == "http://localhost:3000/Unauthorized")
        return (
            <Nav className="navbar bg-success navbar-dark">
                <img src={logo} alt="logo" className="logo"/>
                <Link style={{color: 'white'}} to='/NewCustomer'>Sign in</Link>
                <Button className="btn btn-outline-light" variant="success" onClick={() => doLogin()}> Log
                    in </Button>
            </Nav>
        );

    else if (location == "http://localhost:3000/NewCustomer")
        return (
            <Nav className="navbar bg-success navbar-dark">
                <img src={logo} alt="logo" className="logo"/>
            </Nav>
        );

    else
        return (
            <Nav className="navbar bg-success navbar-dark">
                <img src={logo} alt="logo" className="logo"/>
                <Button variant="outline-light" onClick={() => doLogOut()}>Log out</Button>
            </Nav>
        );
        */

    //PARTE DI MARTI
    return (
        <Switch>
            <Route exact path="/">
                <Nav className="navbar bg-success navbar-dark">
                    <img src={logo} alt="logo" className="logo" />
                    <div style={{ paddingBlock: "5px" }}>
                        <Button className="btn btn-outline-light" variant="success" href='/NewCustomer'> Sign up </Button>
                        <Button className="btn btn-outline-light" variant="success" onClick={() => doLogin()}> Log in </Button>
                    </div>
                </Nav>
            </Route>
            <Route exact path="/Unauthorized">
                <Nav className="navbar bg-success navbar-dark">
                    <img src={logo} alt="logo" className="logo" />
                    <div style={{ paddingBlock: "5px" }}>
                        <Button className="btn btn-outline-light" variant="success" href='/NewCustomer'> Sign up </Button>
                        <Button className="btn btn-outline-light" variant="success" onClick={() => doLogin()}> Log in </Button>
                    </div>
                </Nav>
            </Route>
            <Route exact path="/NewCustomer">
                <Nav className="navbar bg-success navbar-dark">
                    <img src={logo} alt="logo" className="logo" />
                </Nav>
            </Route>
            <Route>
                <Nav className="navbar bg-success navbar-dark">
                    <img src={logo} alt="logo" className="logo" />
                    <div style={{ paddingBlock: "5px" }}>
                        <Button variant="outline-light" onClick={() => doLogOut()}>Log out</Button>
                    </div>
                </Nav>
            </Route>
        </Switch>
    );

    /*
    return (
        <Nav className="navbar bg-success navbar-dark">
            <img src={logo} alt="logo" className="logo" />
            <div style={{ paddingBlock: "5px" }}>
            { props.isLogged ? 
                <Button variant="outline-light" onClick={() => doLogOut()}>Log out</Button>
                :
                <div>
                <Button className="btn btn-outline-light" variant="success" href='/NewCustomer'> Sign up </Button>
                <Button className="btn btn-outline-light" variant="success" onClick={() => doLogin()}> Log in </Button>
                }
                </div>
            </div>
        </Nav>
        )
    
    */

}

export { Navbar }