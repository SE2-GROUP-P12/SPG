import 'bootstrap/dist/css/bootstrap.min.css';
import './App.css';
import Container from 'react-bootstrap/Container';
import {Navbar} from "./Navbar";
import {Homepage} from "./Homepage";
import {Login} from "./Login";
import {ShopEmployee} from "./ShopEmployee";
import {BrowseProducts} from "./BrowseProducts";
import { NewCustomer } from './NewCustomer';
import {TopUp} from "./TopUp";
import {PlaceOrder} from './PlaceOrder';
import { DeliverOrder } from './DeliverOrder';
import {UnauthorizedComponent} from './UnauthorizedComponent';
//import {NoTime} from './NoTime'; //non sono ancora sicura che serva (-Marti)
import {Switch, Route, BrowserRouter as Router} from "react-router-dom";
import {useState} from "react";
import Modal from 'react-bootstrap/Modal';
import {Formik, Form, Field} from 'formik';
import Button from 'react-bootstrap/Button';

const DEBUG = true;

function App() {
    /*LOG IN AND SESSION MANAGEMENT*/
    const [isLogged, setIsLogged] = useState(false);
    const [loggedUserInfo, setLoggedUserInfo] = useState({});

    /*TIME MACHINE MANAGEMENT*/
    const [show, setShow] = useState(false);
    const handleClose = () => setShow(false);
    const handleShow = () => setShow(true);
    const days = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];
    let [date, setDate] = useState(days[new Date().getDay()]);
    let [time, setTime] = useState(()=>{
        let d= new Date();
        return d.getHours()+":"+d.getMinutes();
        return d.getHours()+":"+d.getMinutes();
    });
    function printDays () 
    {
        let output=[<option value='' label='Select a day'/>];
        for(let i=0; i<days.length; i++)
            output.push(<option value={days[i]} label={days[i]}/>);
        return output;
    }

  return (
      <div className="App">
        <Container fluid className="header">
            <Router>
                <Navbar isLogged = {isLogged} loggeduserInfo = {loggedUserInfo} setIsLogged={setIsLogged}/>
           <Switch>
           <Route exact path="/DeliverOrder">
                   <DeliverOrder time={time} date={date}/>
                </Route>
           <Route exact path="/PlaceOrder">
                   <PlaceOrder time={time} date={date}/>
                </Route>
           <Route exact path="/TopUp">
                   <TopUp/>
                </Route>
                <Route exact path="/NewCustomer">
                   <NewCustomer/>
                </Route>
               <Route exact path="/BrowseProducts">
                   <BrowseProducts/>
               </Route>
               <Route exact path="/ShopEmployee">
                   <ShopEmployee/>
               </Route>
               <Route exact path="/LoginComponent">
                   <Login/>
               </Route>
               <Route exact path="/Unauthorized">
                   <UnauthorizedComponent/>
               </Route>
               <Route exact path="/">
                   <Homepage/>
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
                        onSubmit={(values)=>{
                            console.log("CHECKTIME APP:"+values.date+" "+values.time)
                            setDate(values.date);
                            setTime(values.time);
                            handleClose();
                        }}
                    >
                    {({values,handleChange,handleBlur})=>
                        <Form>
                        <select
                        name="date"
                        value={values.date}
                        onChange={handleChange}
                        onBlur={handleBlur}
                        style={{ display: 'block' }}
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
        { DEBUG ? 
        <Button style={{margin: '100px'}} variant="danger" onClick={handleShow}>TIME MACHINE (for debug only)<br/>{date}, {time}</Button>
        : null}
      </div>

  );
}

export default App;
