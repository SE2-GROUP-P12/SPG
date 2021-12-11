import 'bootstrap/dist/css/bootstrap.min.css';
import './../App.css';
import logo from "./../resources/logo.png";
import warning from "./../resources/warning.png";
import Button from "react-bootstrap/Button";
import Nav from "react-bootstrap/Nav";
import OverlayTrigger from "react-bootstrap/OverlayTrigger";
import Tooltip from 'react-bootstrap/Tooltip';
import {Redirect} from 'react-router-dom';
import {useState, useEffect} from "react";

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
                console.log("unsuccesfull logout");
            }
        });
        props.setLoggedFlag(false);
        props.setLoggedUser("");
        props.setAccessToken("");
        props.setLoggedUserRole("");
        props.setTopUpWarning({"exist":"false"})
        console.log("successful logout");
        setRunRedirect(true);
    }

    useEffect(() => {
        setRunRedirect(false);
    }, [runRedirect]);

    if (runRedirect === true) {
        return (<Redirect to="/"></Redirect>);
    }

    if(props.isLoggedFlag===false)
        return (
            <Nav className="navbar bg-success navbar-dark">
            <img src={logo} alt="logo" className="logo"/>
            <div>
                <Button className="btn btn-outline-light" variant="success" href='/NewCustomer'> Sign up </Button>
                <Button className="btn btn-outline-light" variant="success" href='/LoginComponent'> Log in </Button>
            </div>
        </Nav>);
    else
        return (
            <Nav className="navbar bg-success navbar-dark">
                <img src={logo} alt="logo" className="logo" />
                <div>
                    { props.topUpWarning.exist === "true" && props.loggedUserRole === 'CUSTOMER' ?
                        <OverlayTrigger
                            delay={{ show: 250, hide: 400 }}
                            overlay={<Tooltip id="button-tooltip">{props.topUpWarning.message}</Tooltip>}
                            placement="left">
                            <img src={warning} alt="warning" className="warning" />
                        </OverlayTrigger>
                        : ""}
                    <Button className="btn btn-danger" onClick={event => doLogOut(event)}>LOG OUT</Button>
                </div>
            </Nav>
        );
}

export { Navbar }
