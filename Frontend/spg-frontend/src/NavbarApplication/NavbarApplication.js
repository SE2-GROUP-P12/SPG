import 'bootstrap/dist/css/bootstrap.min.css';
import './../App.css';
import logo from "./../resources/logo.png";
import warning from "./../resources/warning.png";
import {Redirect, useLocation} from 'react-router-dom';
import {useState, useEffect} from "react";
import "./NavbarApplication.css"
import {Navbar, Container, Nav, Button, OverlayTrigger, Tooltip} from 'react-bootstrap'

function NavbarApplication(props) {
    const [runRedirect, setRunRedirect] = useState(false);

    const location = useLocation()

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
        props.setTopUpWarning({"exist": "false"})
        console.log("successful logout");
        setRunRedirect(true);
    }

    useEffect(() => {
        setRunRedirect(false);
    }, [runRedirect]);

    if (runRedirect === true) {
        return (<Redirect to="/"></Redirect>);
    }

    return (
        <Navbar collapseOnSelect expand="lg" className="bg-success">
            <Container fluid>
                <Navbar.Brand href="/"><img src={logo} alt="logo" className="logo"/></Navbar.Brand>
                <Navbar.Toggle aria-controls="responsive-navbar-nav"/>
                <Navbar.Collapse id="responsive-navbar-nav">
                    <Nav className="me-auto"></Nav>
                    <Nav>
                        {props.isLoggedFlag && props.topUpWarning.exist === "true" && props.loggedUserRole === 'CUSTOMER' ?
                            <OverlayTrigger
                                delay={{show: 250, hide: 400}}
                                overlay={<Tooltip id="button-tooltip">{props.topUpWarning.message}</Tooltip>}
                                placement="left">
                                <img src={warning} alt="warning" className="warning"/>
                            </OverlayTrigger>
                            : <></>}
                        {location.pathname != "/BrowseProducts" && location.pathname != "/Dashboard" ?
                            <Button className="btn btn-outline-light navbar-button" variant="success"
                                    href='/BrowseProducts'> Browse Products </Button> : <></>}
                        {props.isLoggedFlag && location.pathname != "/Dashboard" ?
                            <Button className="btn btn-outline-light navbar-button" variant="success"
                                    href='/Dashboard'> Dashboard </Button> : <></>}
                        {(location.pathname == "/Dashboard" || (!props.isLoggedFlag && location.pathname == "/BrowseProducts")) ? <></> :
                            <span className="button-separator">|</span>
                        }
                        {
                            props.isLoggedFlag ? <Button className="btn btn-danger navbar-button"
                                                         onClick={event => doLogOut(event)}>Log Out</Button> : <>
                                {location.pathname == "/NewCustomer" ? <></> :
                                    <Button className="btn btn-outline-light navbar-button" variant="success"
                                            href='/NewCustomer'> Sign
                                        up </Button>}
                                {location.pathname == "/LoginComponent" ? <></> :
                                    <Button className="btn btn-outline-light navbar-button" variant="success"
                                            href='/LoginComponent'> Log in </Button>
                                }
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
