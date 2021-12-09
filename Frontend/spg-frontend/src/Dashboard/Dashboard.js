import 'bootstrap/dist/css/bootstrap.min.css';
import '../App.css';
import Container from 'react-bootstrap/Container';
import Button from 'react-bootstrap/Button';
import Card from '@mui/material/Card';
import CardActions from '@mui/material/CardActions';
import CardContent from '@mui/material/CardContent';
import CardMedia from '@mui/material/CardMedia';
import Typography from '@mui/material/Typography';
import Grid from '@mui/material/Grid';
import {getAllServices} from '../Utilities';
import {Link} from "react-router-dom";


function Dashboard(props) {
    const customerServices = getAllServices();


    //Title
    function TitleComponent() {
        return (
            <>
                <h1>WELCOME BACK {props.loggedUser.split('@')[0]}</h1>
                <h2>Here you are your services</h2>
            </>
        );
    }

    //Card component
    function CustomerCardComponent(props) {
        return (
            <Card sx={{maxWidth: 345}} className="mx-3 mt-5">
                <CardMedia
                    component="img"
                    height="200"
                    image={props.service.imageUrl}
                    alt=""
                />
                <CardContent>
                    <Typography gutterBottom variant="h5" component="div">
                        {props.service.type}
                    </Typography>
                    <Typography variant="body">
                        {props.service.description}
                    </Typography>
                </CardContent>
                <CardActions>
                    <Grid container>
                        <Grid item xs={12}>
                            <Link to={props.service.linkUrl}>
                                <Button variant="success" size="lg"> {props.service.buttonLabel} </Button>
                            </Link>
                        </Grid>
                    </Grid>
                </CardActions>
            </Card>

        );
    }


    //Render component return based on role
    return (
        <Container fluid>
            <TitleComponent/>
            {/*Mapping all the services for rendering*/}
            <Grid container spacing={2} align="center">
                {props.services.filter((service) => service.rolesPermitted.includes(localStorage.getItem("role"))).map((value, index) =>
                    <CustomerCardComponent service={value}/>
                )}
            </Grid>
        </Container>
    );

}

//export component
export
{
    Dashboard
}