import 'bootstrap/dist/css/bootstrap.min.css';
import './App.css';
import farmer from "./resources/farmer.jpg";
import italymap from "./resources/map.PNG";
import Grid from '@mui/material/Grid';

function Homepage() {

return (
    <Grid container spacing={2}>
        <Grid item xs={12}>
            <img src={farmer} className="farmer" alt="farmer"/>
        </Grid>
        <Grid item align='center' xs={12} md={6}>
            <h1>Come and find us!</h1>
            <h3>We are in:</h3>
            <p>Roma</p>
                <p>Vicenza</p>
                <p>Biella</p>
                <p>Caserta</p>
                <p>Torino</p>
                <p>Catania</p>
        </Grid>
        <Grid item align='center' xs={12} md={6}>
            <img src={italymap} className="italymap" alt="italymap" />
        </Grid>
    </Grid>
);
}

export {Homepage}