import 'bootstrap/dist/css/bootstrap.min.css';
import './App.css';
import logo from "./resources/logo.png";
import Button from "react-bootstrap/Button";
import Nav from "react-bootstrap/Nav";
import Grid from '@mui/material/Grid';
import {buildLoginBody} from './Utilities';
import {Link, Redirect} from 'react-router-dom';
import {useState, useEffect} from "react";

import {Switch, Route, BrowserRouter as Router} from "react-router-dom";

function Navbar(props) {
    const [runRedirect, setRunRedirect] = useState(false);


    const doLogOut = (event) => {
        event.preventDefault();
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
            } else {
                console.log("usuccesfull logout");
            }
        });
        props.setLoggedFlag(false);
        props.setLoggedUser("");
        props.setAccessToken("");
        props.setLoggedUserRole("");
        console.log("successful logout");
        setRunRedirect(true);
    }
    useEffect(() => {setRunRedirect(false)}, );

    function doLogin() {
        window.location.href = "http://localhost:8081/login";
    }


    if (runRedirect === true) {
        return (<Redirect to="/"></Redirect>);
    }

    return (
        <>
            <Switch>
                <Route exact path="/LoginComponent">
                    <Nav className="navbar bg-success navbar-dark">
                        <img src={logo} alt="logo" className="logo"/>
                        <Link style={{color: 'white'}} to='/NewCustomer'>Sign in</Link>
                    </Nav>
                </Route>
                <Route>
                    <Nav className="navbar bg-success navbar-dark">
                        <img src={logo} alt="logo" className="logo"/>
                        {
                            props.isLoggedFlag === true ?
                                <Button className="btn btn-danger" onClick={event => doLogOut(event)}>LOG OUT</Button>
                                :
                                <>
                                    <Link style={{color: 'white'}} to='/NewCustomer'>Sign in</Link>
                                    <Link to="/LoginComponent">
                                        <Button className="btn btn-outline-light" variant="success"> Log in </Button>
                                    </Link>
                                </>
                        }
                    </Nav>
                </Route>
                <Route exact path="/NewCustomer">
                    <Nav className="navbar bg-success navbar-dark">
                        <img src={logo} alt="logo" className="logo"/>
                        <Link to="/LoginComponent">
                            {props.isLoggedFlag !== true ?
                                <Button className="btn btn-outline-light" variant="success"> Log in </Button>
                                :
                                ""
                            }
                        </Link>
                    </Nav>
                </Route>
            </Switch>
        </>

    );
}

export {Navbar}

//Navbar return by MARTI
/*
* return(
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
* */