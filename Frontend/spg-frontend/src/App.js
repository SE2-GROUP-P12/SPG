import 'bootstrap/dist/css/bootstrap.min.css';
import './App.css';
import Container from 'react-bootstrap/Container';
import {Navbar} from "./Navbar";
import {Homepage} from "./Homepage";
import {Switch, Route} from "react-router-dom";
import {BrowserRouter as Router } from 'react-router-dom';

function App() {
  return (
      <div className="App">
        <Container fluid className="header">
            <Navbar/>
            <Router>
           <Switch>
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
