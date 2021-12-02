import 'bootstrap/dist/css/bootstrap.min.css';
import './../App.css';
import Button from "react-bootstrap/Button"
import Grid from '@mui/material/Grid';
import { Redirect, Link } from "react-router-dom";

function Farmer() {
    return (
        localStorage.getItem('role') === 'FARMER' ?
            <div>
                <h1 style={{ paddingBlock: "20px" }}>Farmer</h1>
                <Grid container spacing={2}>
                    <Grid item xs={12} sm={6} md={4}>
                        <Link to="/ProductsForecast"><Button size='huge' variant="outline-success"> <h1>Communicate Products Prevision</h1> </Button> </Link>
                    </Grid>
                    <Grid item xs={12} sm={6} md={4}>
                        <Link to='/AddProduct'><Button size='huge' variant="outline-success"> <h1>Add New Product</h1> </Button></Link>
                    </Grid>
                </Grid>
            </div>
            :
            <Redirect to="/ErrorHandler"></Redirect>
    );
}

export { Farmer }