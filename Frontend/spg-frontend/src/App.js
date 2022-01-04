import 'bootstrap/dist/css/bootstrap.min.css';
import './App.css';
import {API} from "./API/API";
import Container from 'react-bootstrap/Container';
import {NavbarApplication} from "./NavbarApplication/NavbarApplication";
import {Homepage} from "./Homepage/Homepage";
import {Login} from "./Login/Login";
import {Dashboard} from "./Dashboard/Dashboard";
import {ShopEmployee} from "./ShopEmployee/ShopEmployee";
import {BrowseProducts} from "./BrowseProducts/BrowseProducts";
import {NewCustomer} from './NewCustomer/NewCustomer';
import {TopUp} from "./TopUp/TopUp";
import {PlaceOrder} from './PlaceOrder/PlaceOrder';
import {Customer} from './Customer/Customer';
import {Farmer} from './Farmer/Farmer';
import {DeliverOrder} from './DeliverOrder/DeliverOrder';
import {UnauthorizedComponent} from './UnauthorizedComponent/UnauthorizedComponent';
import {Switch, Route, BrowserRouter as Router} from "react-router-dom";
import {useState, useEffect} from "react";
import Button from 'react-bootstrap/Button';
import {ProductsForecast} from "./ProductsForecast/ProductsForecast";
import {AddProduct} from "./AddProduct/AddProduct";
import {getAllServices} from './Utilities';
import {WalletOperation} from "./WalletOperation/WallettOperation";
import {ConfirmAvailability} from "./ConfirmAvailability/ConfirmAvailability";
import {MuiPickersUtilsProvider, DateTimePicker} from "@material-ui/pickers";
import MomentUtils from '@date-io/moment';
import {createTheme, MuiThemeProvider} from "@material-ui/core/styles";
import {UnpickedOrders} from './UnpickedOrders/UnpickedOrders';

const DEBUG = true;
const moment = require('moment')

function App() {
    /*SERVICES*/
    const [allServices] = useState(getAllServices());
    /*BACK END ERROR HANDLER*/
    const [errorMessage, setErrorMessage] = useState(undefined);
    /*LOGGGED USER SESSION*/
    const [isLogged, setIsLogged] = useState(false);
    const [loggedUser, setLoggedUser] = useState("");
    const [loggedUserRole, setLoggedUserRole] = useState("");
    const [accessToken, setAccessToken] = useState("");
    /*TIME MACHINE MANAGEMENT*/
    const [show, setShow] = useState(false);
    const handleShow = () => setShow(true);
    const handleDateTimeChange = async (d) => {
        setDateTime(() => d)
        setDate(() => d.format('ddd'))
        setTime(() => d.format('HH') + ":" + d.format('mm'))
        console.log('new unix time: ' + d.format('X'))
        await API.timeTravel({'epoch time': d.format('X')})
    }
    let [date, setDate] = useState(moment().format('ddd'));
    let [time, setTime] = useState(() => {
        let d = moment();
        return d.format('HH') + ":" + d.format('mm');
    });
    let [dateTime, setDateTime] = useState(moment());

    /*TOP UP WARNING MANAGEMENT*/
    const [topUpWarning, setTopUpWarning] = useState({'exist': "false"});

    /*SESSION RELOAD ROUTINE*/
    async function _reloadSession() {
        const data = await API.sessionReloader();
        if (data !== null) {//session refreshed
            setIsLogged(true);
            setAccessToken(data['accessToken']);
            setLoggedUser(data["email"]);
            setLoggedUserRole(data["roles"]);
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

    useEffect(async () => {
        await _reloadSession();
    }, []);

    return (
        <div className="App">
            <Container fluid className="header">
                <Router>
                    <NavbarApplication setLoggedFlag={setIsLogged} isLoggedFlag={isLogged}
                                       setLoggedUser={setLoggedUser} loggedUser={loggedUser}
                                       setAccessToken={setAccessToken}
                                       setLoggedUserRole={setLoggedUserRole} loggedUserRole={loggedUserRole}
                                       topUpWarning={topUpWarning} setTopUpWarning={setTopUpWarning}/>
                    <Switch>
                        <Route exact path="/DeliverOrder">
                            <DeliverOrder time={time}
                                          date={date}/>
                        </Route>
                        <Route exact path="/PlaceOrder">
                            <PlaceOrder time={time}
                                        date={date}
                                        setErrorMessage={setErrorMessage}
                                        loggedUser={loggedUser}
                                        loggedUserRole={loggedUserRole}
                                        setTopUpWarning={setTopUpWarning}
                            />
                        </Route>
                        <Route exact path="/TopUp">
                            <TopUp/>
                        </Route>
                        <Route exact path="/NewCustomer">
                            <NewCustomer setLoggedUser={setLoggedUser} setLoggedFlag={setIsLogged}
                                         setAccessToken={setAccessToken} accessToken={accessToken}
                                         setLoggedUserRole={setLoggedUserRole}/>
                        </Route>
                        <Route exact path="/BrowseProducts">
                            <BrowseProducts setErrorMessage={setErrorMessage}
                                            errorMessage={errorMessage}
                                            isLogged={isLogged}
                                            loggedUser={loggedUser}>
                            </BrowseProducts>
                        </Route>
                        <Route exact path="/ConfirmAvailability">
                            <ConfirmAvailability time={time}
                                                 date={date}
                                                 setErrorMessage={setErrorMessage}
                                                 errorMessage={errorMessage}
                                                 isLogged={isLogged}
                                                 loggedUser={loggedUser}>
                            </ConfirmAvailability>
                        </Route>
                        <Route exact path="/Employee">
                            <ShopEmployee isLogged={isLogged}
                                          loggedUser={loggedUser}
                                          loggedUserRole={loggedUserRole}/>
                        </Route>
                        <Route exact path="/ShopEmployee">
                            <ShopEmployee isLogged={isLogged}
                                          loggedUser={loggedUser}
                                          loggedUserRole={loggedUserRole}/>
                        </Route>
                        <Route exact path="/Customer">
                            <Customer loggedUser={loggedUser}
                                      loggedUserRole={loggedUserRole}/>
                        </Route>
                        <Route exact path="/Farmer">
                            <Farmer/>
                        </Route>
                        <Route exact path="/ProductsForecast">
                            <ProductsForecast
                                setErrorMessage={setErrorMessage}/>
                        </Route>
                        <Route exact path="/AddProduct">
                            <AddProduct/>
                        </Route>
                        <Route exact path="/Admin">
                            <Customer loggedUser={loggedUser}
                                      loggedUserRole={loggedUserRole}/>
                        </Route>
                        <Route exact path="/unpickedOrders">
                            <UnpickedOrders dateTime={dateTime}/>
                        </Route>
                        <Route exact path="/LoginComponent">
                            <Login setLoggedUser={setLoggedUser} setLoggedFlag={setIsLogged}
                                   setAccessToken={setAccessToken} accessToken={accessToken}
                                   setLoggedUserRole={setLoggedUserRole}
                                   setTopUpWarning={setTopUpWarning}/>
                        </Route>
                        <Route exact path="/Customer/WalletOperations">
                            <WalletOperation/>
                        </Route>
                        <Route exact path="/">
                            <Homepage/>
                        </Route>
                        <Route exact path="/ErrorHandler">
                            <UnauthorizedComponent errorMessage={errorMessage}/>
                        </Route>
                        <Route exact path="/Dashboard">
                            <Dashboard loggedUser={loggedUser} services={allServices}/>
                        </Route>
                    </Switch>
                </Router>

            </Container>
            {DEBUG ?
                <>
                    {
                        <MuiPickersUtilsProvider utils={MomentUtils}>
                            <MuiThemeProvider theme={createTheme({
                                palette: {
                                    primary: {
                                        main: '#5cb85c'
                                    }
                                }
                            })}>
                                <DateTimePicker open={show}
                                                onOpen={() => setShow(true)}
                                                onClose={() => setShow(false)}
                                                value={dateTime}
                                                onChange={handleDateTimeChange}
                                                okLabel="TIME TRAVEL!"
                                                TextFieldComponent={() => null}
                                                ampm={false}/>
                            </MuiThemeProvider>
                        </MuiPickersUtilsProvider>
                    }

                    <Button style={{margin: '100px'}} variant="danger" onClick={handleShow}>TIME MACHINE (for
                        debug
                        only)<br/>{date}, {time}</Button>
                </>
                : null}
        </div>

    );
}

export default App;
