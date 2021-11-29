import 'bootstrap/dist/css/bootstrap.min.css';
import './App.css';
import logo from "./resources/logo.png";
import warning from "./resources/warning.png";
import Button from "react-bootstrap/Button";
import Nav from "react-bootstrap/Nav";
import Grid from '@mui/material/Grid';
import { buildLoginBody } from './Utilities';
import { Link, Redirect } from 'react-router-dom';
import { useState, useEffect } from "react";

import { Switch, Route, BrowserRouter as Router } from "react-router-dom";
import { API } from './API/API';

function Navbar(props) {
    const [runRedirect, setRunRedirect] = useState(false);
    const [walletWarning, setWalletWarning] = useState(false);
    
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
                console.log("unsuccesfull logout");
            }
        });
        props.setLoggedFlag(false);
        props.setLoggedUser("");
        props.setAccessToken("");
        props.setLoggedUserRole("");
        console.log("successful logout");
        setRunRedirect(true);
    }

    /* async function _getWalletWarning(){
        const data = await API.getWalletWarning(props.loggedUser);
        setWalletWarning(data);
        return data;
    } */

    useEffect(() => {
        setRunRedirect(false);
    }, [runRedirect]);

   /*  useEffect(() => {
        _getWalletWarning();
    }, []); */

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
                        <img src={logo} alt="logo" className="logo" />
                    </Nav>
                </Route>
                <Route exact path="/NewCustomer">
                    <Nav className="navbar bg-success navbar-dark">
                        <img src={logo} alt="logo" className="logo" />
                        <div>
                            {
                                props.isLoggedFlag !== true ?
                                    <Button className="btn btn-outline-light" variant="success" href="/LoginComponent"> Log in </Button>
                                    :
                                    <>
                                        {(walletWarning && (props.loggedUserRole === 'CUSTOMER')) ? <img src={warning} alt="warning" className="warning" /> : ""}
                                    </>
                                    
                            }
                        </div>
                    </Nav>
                </Route>
                <Route>
                    <Nav className="navbar bg-success navbar-dark">
                        <img src={logo} alt="logo" className="logo" />
                        <div>
                            {
                                props.isLoggedFlag === true ?
                                    <>
                                        {(walletWarning && (props.loggedUserRole === 'CUSTOMER')) ? <img src={warning} alt="warning" className="warning"/> : ""}
                                        <Button className="btn btn-danger" onClick={event => doLogOut(event)}>LOG OUT</Button>
                                    </>
                                    :
                                    <>
                                        <Button className="btn btn-outline-light" variant="success" href='/NewCustomer'> Sign up </Button>
                                        <Button className="btn btn-outline-light" variant="success" href='/LoginComponent'> Log in </Button>
                                    </>
                            }
                        </div>
                    </Nav>
                </Route>
            </Switch>
        </>
    );
}

export { Navbar }
