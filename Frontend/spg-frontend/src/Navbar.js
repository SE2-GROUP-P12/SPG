import 'bootstrap/dist/css/bootstrap.min.css';
import './App.css';
import logo from "./resources/logo.png";
import Button from "react-bootstrap/Button";
import DropdownButton from "react-bootstrap/DropdownButton";
import Dropdown from "react-bootstrap/Dropdown";
import Nav from "react-bootstrap/Nav";
import Modal from "react-bootstrap/Modal";
import Form from "react-bootstrap/Form";
import {buildLoginBody} from './Utilities';
import {Link, Redirect} from 'react-router-dom';
import {useState, useEffect} from "react";

import {Switch, Route, BrowserRouter as Router} from "react-router-dom";

function Navbar(props) {
    const [showModalLogin, setShowModalLogin] = useState(false);
    const [enableSubmit, setEnableSubmit] = useState(false);
    const [runRedirect, setRunRedirect] = useState(false);

    useEffect(() => {setRunRedirect(false)}, );


    const onClickSubmissionHandler = (event, username, password) => {
        event.preventDefault();
        fetch("/api/login", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: buildLoginBody({
                'username': username,
                'password': password
            })
        }).then(response => {
            if (response.ok) {
                response.json().then(body => {
                        sessionStorage.setItem('accessToken', body['accessToken']);
                        sessionStorage.setItem('refreshToken', body['refreshToken']);
                        localStorage.setItem('username', body['email']);
                        localStorage.setItem('role', body['roles']);
                    }
                )
                props.setLoggedFlag(true);
                props.setLoggedUser(username);
            }
        });
        console.log("loginOK");
    }

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

    function LoginForm(props) {

        const usernameHandler = (event) => {
            props.setUsername(event.target.value);
        }
        const passwordHandler = (event) => {
            props.setPassword(event.target.value);
        }


        return (
            <Form>
                <Form.Group className="mb-3" controlId="formBasicEmail">
                    <Form.Label>Email address</Form.Label>
                    <Form.Control type="email" placeholder="Enter email" onChange={(event) => usernameHandler(event)}
                                  value={props.username}/>
                    <Form.Text className="text-muted">
                        We'll never share your email with anyone else.
                    </Form.Text>
                </Form.Group>
                <Form.Group className="mb-3" controlId="formBasicPassword">
                    <Form.Label>Password</Form.Label>
                    <Form.Control type="password" placeholder="Password" onChange={(event) => passwordHandler(event)}
                                  value={props.password}/>
                </Form.Group>
            </Form>);
    }

    function ModalComponent() {
        const [username, setUsername] = useState("");
        const [password, setPassword] = useState("");

        return (

            <Modal
                show={showModalLogin}
                onHide={() => setShowModalLogin(false)}
                backdrop="static"
                keyboard={false}
            >
                <Modal.Header closeButton>
                    <Modal.Title>Please, log in into SPG</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <LoginForm username={username} setUsername={setUsername}
                               password={password} setPassword={setPassword}/>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="success" onClick={event => {
                        onClickSubmissionHandler(event, username, password);
                        setShowModalLogin(false);
                        //setRedirect(true);
                    }}>
                        LOG IN
                    </Button>
                </Modal.Footer>
            </Modal>
        );
    }

    function DropDownLoggedUser() {
        return (
            <DropdownButton variant="outline-dark" id="dropdown-item-button" title={props.loggedUser.split("@")[0]}>
                <Link to="/ShopEmployee"><Dropdown.Item as="button">HOME</Dropdown.Item></Link>
                <Dropdown.Item as="button" onClkick={(event) => doLogOut(event, props)}>LOG
                    OUT</Dropdown.Item>
            </DropdownButton>
        );
    }
    if(runRedirect === true){
        return (<Redirect to = "/"></Redirect>);
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
                                <Button className = "btn btn-danger" onClick={event => doLogOut(event)}>LOG OUT</Button>
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
                            <Button className="btn btn-outline-light" variant="success"> Log in </Button>
                        </Link>
                    </Nav>
                </Route>
            </Switch>
        </>

    );
}

export {Navbar}