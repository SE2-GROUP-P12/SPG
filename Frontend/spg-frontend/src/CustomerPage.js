import 'bootstrap/dist/css/bootstrap.min.css';
import './App.css';
import Container from 'react-bootstrap/Container';
import Button from 'react-bootstrap/Button';
import Card from '@mui/material/Card';
import CardActions from '@mui/material/CardActions';
import CardContent from '@mui/material/CardContent';
import CardMedia from '@mui/material/CardMedia';
import Typography from '@mui/material/Typography';
import Grid from '@mui/material/Grid';
import {getCustomerServices} from './Utilities';


function CustomerPage(props) {
    const customerServices = getCustomerServices();
     /*BUTTON HANDLERs*/
    //Show cartButton
    const showCartButtonHandler = () => {

    }

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
                        <Grid item xs={12}> <Button variant="success"> {props.service.buttonLabel} </Button>
                        </Grid>
                    </Grid>
                </CardActions>
            </Card>

        );
    }


    //Render component return
    return (
        <Container fluid>
            <TitleComponent/>
            {/*Mapping all the services for rendering*/}
            <Grid container spacing={2} align="center">
                {props.services.map((value, index) =>
                    <CustomerCardComponent service={value}/>
                )}
            </Grid>
        </Container>
    );
}

//export component
export
{
    CustomerPage
}