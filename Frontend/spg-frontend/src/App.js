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
import {Switch, Route, BrowserRouter as Router} from "react-router-dom";

function App() {

  return (
      <div className="App">
        <Container fluid className="header">
            <Router>
                <Navbar/>
           <Switch>
           <Route exact path="/PlaceOrder">
                   <PlaceOrder/>
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
               <Route exact path="/Login">
                   <Login/>
               </Route>
               <Route exact path="/">
                   <Homepage/>
               </Route>
           </Switch>
            </Router>
        </Container>
      </div>

  );
}

export default App;
