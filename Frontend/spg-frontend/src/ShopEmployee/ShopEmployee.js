import 'bootstrap/dist/css/bootstrap.min.css';
import '../App.css';
import Button from "react-bootstrap/Button"
import {Link, Redirect} from "react-router-dom";
import Grid from '@mui/material/Grid';

function ShopEmployee()
{
    return(
        localStorage.getItem('role') === 'EMPLOYEE' || localStorage.getItem('role') === 'ADMIN' ?
        <div>
            <h1 style={{paddingBlock: "20px"}}>Shop Employee</h1>
            <Grid container spacing={2}>
                <Grid item xs={12} sm={6} md={4}>
                    <Link to="/NewCustomer"><Button size='huge' variant="outline-success"> <h1>New Customer</h1> </Button> </Link> 
                </Grid>
                <Grid item xs={12} sm={6} md={4}>
                    <Link to="/BrowseProducts"><Button size='huge' variant="outline-success"> <h1>Browse Products</h1> </Button></Link>
                </Grid>
                <Grid item xs={12} sm={6} md={4}>
                    <Link to="/TopUp"><Button size='huge' variant="outline-success"> <h1>Top Up</h1> </Button></Link> 
                </Grid>
                <Grid item xs={12} sm={6} md={4}>
                    <Link to='/PlaceOrder'><Button size='huge' variant="outline-success"> <h1>Place Order</h1> </Button></Link>
                </Grid>
                <Grid item xs={12} sm={6} md={4}>
                    <Link to='/DeliverOrder'><Button size='huge' variant="outline-success"> <h1>Deliver Order</h1> </Button></Link>
                </Grid>
            </Grid>
        </div>
        :
        <Redirect to="/ErrorHandler"></Redirect>
    )
}

export {ShopEmployee}