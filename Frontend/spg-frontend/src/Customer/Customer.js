import 'bootstrap/dist/css/bootstrap.min.css';
import './../App.css';
import Button from "react-bootstrap/Button"
import Grid from '@mui/material/Grid';
import { Redirect, Link } from "react-router-dom";

function Customer() {
    return (
        localStorage.getItem('role') === 'CUSTOMER' || localStorage.getItem('role') === 'ADMIN' ?
            <div>
                <h1 style={{ paddingBlock: "20px" }}>Customer</h1>
                <Grid container spacing={2}>
                    <Grid item xs={12} sm={6} md={4}>
                        <Link to="/BrowseProducts"><Button size='huge' variant="outline-success"> <h1>Browse Products</h1> </Button> </Link>
                    </Grid>
                    <Grid item xs={12} sm={6} md={4}>
                        <Link to='/PlaceOrder'><Button size='huge' variant="outline-success"> <h1>Place Order</h1> </Button></Link>
                    </Grid>
                </Grid>
            </div>
            :
            <Redirect to="/ErrorHandler"></Redirect>
    );
}

export { Customer }