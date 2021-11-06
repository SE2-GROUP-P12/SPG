import 'bootstrap/dist/css/bootstrap.min.css';
import './App.css';
import Container from 'react-bootstrap/Container';
import {Navbar} from "./Navbar";
import {Homepage} from "./Homepage";


function App() {
  return (
      <div className="App">
        <Container fluid className="header">
           <Navbar/>
            <Homepage/>
        </Container>
      </div>

  );
}

export default App;
