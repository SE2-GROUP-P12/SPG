import 'bootstrap/dist/css/bootstrap.min.css';
import './App.css';
import {API} from "./API/API";
import Container from 'react-bootstrap/Container';
import {Navbar} from "./Navbar";
import {Homepage} from "./Homepage/Homepage";
import {Login} from "./Login";
import {ShopEmployee} from "./ShopEmployee/ShopEmployee";
import {BrowseProducts} from "./BrowseProducts/BrowseProducts";
import { NewCustomer } from './NewCustomer/NewCustomer';
import {TopUp} from "./TopUp";
import {PlaceOrder} from './PlaceOrder';
import { DeliverOrder } from './DeliverOrder/DeliverOrder';
import {UnauthorizedComponent} from './UnauthorizedComponent';
//import {NoTime} from './NoTime'; //non sono ancora sicura che serva (-Marti)
import {Switch, Route, BrowserRouter as Router} from "react-router-dom";
import {useState, useEffect} from "react";
import Modal from 'react-bootstrap/Modal';
import {Formik, Form, Field} from 'formik';
import Button from 'react-bootstrap/Button';

const DEBUG = true;

function App() {
    /*BACK END ERROR HANDLER*/
    const [errorMessage, setErrorMessage] = useState(undefined);
    /*LOGGGED USER SESSION*/
    const [isLogged, setIsLogged] = useState(false);
    const [loggedUser, setLoggedUser] = useState("");
    const [loggedUserRole, setLOggedUserRole] = useState("");
    const [accessToken, setAccessToken] = useState("");
    /*TIME MACHINE MANAGEMENT*/
    const [show, setShow] = useState(false);
    const handleClose = () => setShow(false);
    const handleShow = () => setShow(true);
    const days = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];
    let [date, setDate] = useState(days[new Date().getDay()]);
    let [time, setTime] = useState(() => {
        let d = new Date();
        return d.getHours() + ":" + d.getMinutes();
    });
    /*ERROR HANDLER ROUTINE*/
    // useEffect(()=> {
    //     setErrorMessage(undefined);
    // },);

    /*SESSION RELOAD ROUTINE*/
    async function _reloadSession() {
        const data = await API.sessionReloader();
        if (data !== null) {//session refreshed
            setIsLogged(true);
            setAccessToken(data['accessToken']);
            setLoggedUser(data["email"]);
            setLOggedUserRole(data["roles"]);
            sessionStorage.removeItem("accessToken");
            sessionStorage.removeItem("refreshTokem");
            localStorage.removeItem("role");
            localStorage.removeItem("username");
            sessionStorage.setItem("accessToken", data['accessToken']);
            sessionStorage.setItem("refreshToken", data['refreshToken']);
            localStorage.setItem("role", data["roles"]);
            localStorage.setItem("username", data["email"]);
        }
    }

    useEffect(() => {
        _reloadSession();
    }, []);

    /*TIME HANDLER*/
    function printDays() {
        let output = [<option value='' label='Select a day'/>];
        for (let i = 0; i < days.length; i++)
            output.push(<option value={days[i]} label={days[i]}/>);
        return output;
    }


    return (
        <div className="App">
            <Container fluid className="header">
                <Router>
                    <Navbar setLoggedFlag={setIsLogged} isLoggedFlag={isLogged}
                            setLoggedUser={setLoggedUser} loggedUser={loggedUser}
                            setAccessToken={setAccessToken}
                            setLoggedUserRole={setLOggedUserRole} loggedUserRole={loggedUserRole}/>
                    <Switch>
                        <Route exact path="/DeliverOrder">
                            <DeliverOrder time={time}
                                          date={date}/>
                        </Route>
                        <Route exact path="/PlaceOrder">
                            <PlaceOrder time={time}
                                        date={date}
                                        setErrorMessage={setErrorMessage}/>
                        </Route>
                        <Route exact path="/TopUp">
                            <TopUp/>
                        </Route>
                        <Route exact path="/NewCustomer">
                            <NewCustomer/>
                        </Route>
                        <Route exact path="/BrowseProducts">
                            <BrowseProducts setErrorMessage={setErrorMessage}
                                            errorMessage={errorMessage}
                                            isLogged={isLogged}></BrowseProducts>
                        </Route>
                        <Route exact path="/ShopEmployee">
                            <ShopEmployee/>
                        </Route>
                        <Route exact path="/LoginComponent">
                            <Login setLoggedUser={setLoggedUser} setLoggedFlag={setIsLogged}
                                   setAccessToken={setAccessToken} accessToken={accessToken}/>
                        </Route>
                        <Route exact path="/">
                            <Homepage/>
                        </Route>
                        <Route exact path="/ErrorHandler">
                            <UnauthorizedComponent errorMessage={errorMessage}/>
                        </Route>
                    </Switch>
                </Router>
                <Modal show={show} onHide={handleClose}>
                    <Modal.Header closeButton>
                        <Modal.Title>Time machine</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        <Formik
                            initialValues={{
                                date: '',
                                time: ''
                            }}
                            onSubmit={(values) => {
                                console.log("CHECKTIME APP:" + values.date + " " + values.time)
                                setDate(values.date);
                                setTime(values.time);
                                handleClose();
                            }}
                        >
                            {({values, handleChange, handleBlur}) =>
                                <Form>
                                    <select
                                        name="date"
                                        value={values.date}
                                        onChange={handleChange}
                                        onBlur={handleBlur}
                                        style={{display: 'block'}}
                                    >
                                        {printDays()}
                                    </select>
                                    <Field type='time' name='time'/>
                                    <Button type='submit' variant='danger'>Time travel!</Button>
                                </Form>
                            }
                        </Formik>
                    </Modal.Body>
                </Modal>
            </Container>
            {DEBUG ?
                <Button style={{margin: '100px'}} variant="danger" onClick={handleShow}>TIME MACHINE (for
                    debug
                    only)<br/>{date}, {time}</Button>
                : null}
        </div>

    );
}

export default App;
