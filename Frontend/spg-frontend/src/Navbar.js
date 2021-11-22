import 'bootstrap/dist/css/bootstrap.min.css';
import './App.css';
import logo from "./resources/logo.png";
import Button from "react-bootstrap/Button";
import Nav from "react-bootstrap/Nav";
import {Link} from 'react-router-dom';
import {useState} from "react";
import Grid from '@mui/material/Grid';

import {Switch, Route, BrowserRouter as Router} from "react-router-dom";

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

    return(
           <Switch>
           <Route exact path="/">
                <Nav className="navbar bg-success navbar-dark" >
                    <Grid container spacing={2}>
                        <Grid item align='left' xs={4}>
                            <img src={logo} alt="logo" className="logo"/>
                        </Grid>
                        <Grid item align='center' xs={4}> 
                            <Link to='/NewCustomer'><Button className="btn btn-outline-light" variant="success"> Sign in </Button></Link>
                        </Grid>
                        <Grid item align='right' xs={4}>
                            <Button className="btn btn-outline-light" variant="success" onClick={() => doLogin()}> Log in </Button>
                        </Grid>
                    </Grid>
                </Nav>
            </Route>
           <Route exact path="/Unauthorized">
        <Nav className="navbar bg-success navbar-dark" >
                    <Grid container spacing={2}>
                        <Grid item align='left' xs={4}>
                            <img src={logo} alt="logo" className="logo"/>
                        </Grid>
                        <Grid item align='center' xs={4}> 
                            <Link to='/NewCustomer'><Button className="btn btn-outline-light" variant="success"> Sign in </Button></Link>
                        </Grid>
                        <Grid item align='right' xs={4}>
                            <Button className="btn btn-outline-light" variant="success" onClick={() => doLogin()}> Log in </Button>
                        </Grid>
                    </Grid>
                </Nav>
            </Route>
            <Route exact path="/NewCustomer">
            <Nav className="navbar bg-success navbar-dark" >
                    <Grid container spacing={2}>
                        <Grid item align='left' xs={4}>
                            <img src={logo} alt="logo" className="logo"/>
                        </Grid>
                        <Grid item align='center' xs={8}/> 
                    </Grid>
                </Nav>
            </Route>
            <Route>
            <Nav className="navbar bg-success navbar-dark" >
                    <Grid container spacing={2}>
                        <Grid item align='left' xs={4}>
                            <img src={logo} alt="logo" className="logo"/>
                        </Grid>
                        <Grid item xs={4}/> 
                        <Grid item align='right' xs={4}>
                            <Button className="btn btn-outline-light" variant="success" onClick={() => doLogin()}> Log in </Button>
                        </Grid>
                    </Grid>
                </Nav>       
            </Route>
           </Switch>
    );
}

export {Navbar}