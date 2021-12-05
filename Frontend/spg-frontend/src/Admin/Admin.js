import 'bootstrap/dist/css/bootstrap.min.css';
import '../App.css';
import Button from "react-bootstrap/Button"
import Grid from '@mui/material/Grid';
import { Redirect, Link } from "react-router-dom";

function Admin() {
    return (
        localStorage.getItem('role') === 'ADMIN' ?
            <div>
                <h1 style={{ paddingBlock: "20px" }}>Admin</h1>
                <Grid container spacing={2}>
                    <Grid item xs={12} sm={6} md={4}>
                        <Link to="/ShopEmployee"><Button size='huge' variant="outline-success"> <h1>ShopEmployee</h1> </Button> </Link>
                    </Grid>
                    <Grid item xs={12} sm={6} md={4}>
                        <Link to='/Customer'><Button size='huge' variant="outline-success"> <h1>Customer</h1> </Button></Link>
                    </Grid>
                    <Grid item xs={12} sm={6} md={4}>
                        <Link to='/Farmer'><Button size='huge' variant="outline-success"> <h1>Farmer</h1> </Button></Link>
                    </Grid>
                </Grid>
            </div>
            :
            <Redirect to="/ErrorHandler"/>
    );
}

export { Admin }