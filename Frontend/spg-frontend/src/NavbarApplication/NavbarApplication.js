import 'bootstrap/dist/css/bootstrap.min.css';
import './../App.css';
import logo from "./../resources/logo.png";
import {Redirect, useLocation} from 'react-router-dom';
import {useState, useEffect} from "react";
import "./NavbarApplication.css"
import {Navbar, Container, Nav, Button, OverlayTrigger, Tooltip} from 'react-bootstrap'

function NavbarApplication(props) {
    const [runRedirect, setRunRedirect] = useState(false);

    const location = useLocation()

    const doLogOut = (event) => {
        event.preventDefault();

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
                localStorage.removeItem("missedPickUp");
            } else {
                console.log("unsuccesfull logout");
            }
        });
        props.setLoggedFlag(false);
        props.setLoggedUser("");
        props.setAccessToken("");
        props.setLoggedUserRole("");
        props.setTopUpWarning({"exist": "false"})
        console.log("successful logout");
        setRunRedirect(true);
    }

    useEffect(() => {
        setRunRedirect(false);
    }, [runRedirect]);

    if (runRedirect === true) {
        return (<Redirect to="/"/>);
    }

    function conditionalSignUp ()
    {
        if(location.pathname==="/NewCustomer")
            return "";
        return (<Button id='signup' className="btn btn-outline-light navbar-button"
                        variant="success"
                        href='/NewCustomer'> Sign up </Button>);
    }

    function conditionalLogIn ()
    {
        if(location.pathname === "/LoginComponent")
            return "";
        return(<Button id='login' className="btn btn-outline-light navbar-button"
                    variant="success"
                    href='/LoginComponent'> Log in </Button>);
    }

    return (
        <Navbar collapseOnSelect expand="lg" className="bg-success">
            <Container fluid>
                <Navbar.Brand href="/"><img src={logo} alt="logo" className="logo"/></Navbar.Brand>
                <Navbar.Toggle aria-controls="responsive-navbar-nav"/>
                <Navbar.Collapse id="responsive-navbar-nav">
                    <Nav className="me-auto"/>
                    <Nav>
                        {props.isLoggedFlag && props.topUpWarning.exist === "true" && props.loggedUserRole === 'CUSTOMER' ?
                            <OverlayTrigger
                                delay={{show: 250, hide: 400}}
                                overlay={<Tooltip id="button-tooltip">{props.topUpWarning.message}</Tooltip>}
                                placement="auto"
                                >
                                <svg xmlns="http://www.w3.org/2000/svg" width="40" height="40" fill="white"
                                     className="bi bi-exclamation-triangle-fill mt-2 mx-2 rounded bg-danger"
                                     viewBox="0 0 16 16"
                                     data-testid='topUpWarning'>
                                    <path
                                        d="M8.982 1.566a1.13 1.13 0 0 0-1.96 0L.165 13.233c-.457.778.091 1.767.98 1.767h13.713c.889 0 1.438-.99.98-1.767L8.982 1.566zM8 5c.535 0 .954.462.9.995l-.35 3.507a.552.552 0 0 1-1.1 0L7.1 5.995A.905.905 0 0 1 8 5zm.002 6a1 1 0 1 1 0 2 1 1 0 0 1 0-2z"/>
                                </svg>
                            </OverlayTrigger>
                            : <></>}
                        {
                            props.isLoggedFlag && localStorage.getItem("missedPickUp") !== "0" && props.loggedUserRole === 'CUSTOMER' ?
                                <OverlayTrigger
                                    align = 'right'
                                    delay={{show: 250, hide: 400}}
                                    overlay={<Tooltip id="button-tooltip">There
                                        are {localStorage.getItem("missedPickUp")} order(s) waiting for your pick up,
                                        please pick it up as soon as possible</Tooltip>}
                                    placement="auto">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="40" height="40" fill="white"
                                         className="bi bi-exclamation-triangle-fill mt-2 mx-2 rounded bg-danger"
                                         viewBox="0 0 16 16"
                                         data-testid='missedPickupWarning'>
                                        <path
                                            d="M8.982 1.566a1.13 1.13 0 0 0-1.96 0L.165 13.233c-.457.778.091 1.767.98 1.767h13.713c.889 0 1.438-.99.98-1.767L8.982 1.566zM8 5c.535 0 .954.462.9.995l-.35 3.507a.552.552 0 0 1-1.1 0L7.1 5.995A.905.905 0 0 1 8 5zm.002 6a1 1 0 1 1 0 2 1 1 0 0 1 0-2z"/>
                                    </svg>
                                </OverlayTrigger>
                                : <></>
                        }
                        {location.pathname != "/BrowseProducts" && location.pathname != "/Dashboard" && (props.loggedUserRole === 'CUSTOMER' ||
                            props.loggedUserRole === '') ?
                            <Button id='browse-product-button' className="btn btn-outline-light navbar-button"
                                    variant="success"
                                    href='/BrowseProducts'> Browse Products </Button> : <></>}
                        {props.isLoggedFlag && location.pathname != "/Dashboard" ?
                            <Button id='dashboard' className="btn btn-outline-light navbar-button" variant="success"
                                    href='/Dashboard'> Dashboard </Button> : <></>}
                        {(location.pathname == "/Dashboard" || (!props.isLoggedFlag && location.pathname == "/BrowseProducts")) ? <></> :
                            <span className="button-separator">|</span>
                        }
                        {
                            props.isLoggedFlag ? <Button id='logout' className="btn btn-danger navbar-button"
                                                         onClick={event => doLogOut(event)}>Log Out</Button> : <>
                                {conditionalSignUp()}
                                {conditionalLogIn()}
                            </>
                        }
                    </Nav>
                </Navbar.Collapse>
            </Container>
        </Navbar>
    );
}

export
{
    NavbarApplication
}
