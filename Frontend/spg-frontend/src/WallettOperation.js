import Container from "react-bootstrap/Container";
import Row from "react-bootstrap/Row";
import {API} from "./API/API";
import DataGrid from '@mui/material/Grid';
import {useEffect, useState} from "react";
import {Spinner} from "react-bootstrap";


function WalletOperation() {
    const [loading, setLoadingSpinner] = useState(true);
    const [wallet, setWallet] = useState(0.00)
    const [customerWalletOperation, setCustomerWalletOperation] = useState([]);

    useEffect(async () => {
        setWallet(await API.getWallet(localStorage.getItem("username")));
        setCustomerWalletOperation(await API.getWalletOperation(localStorage.getItem("username")));
        setLoadingSpinner(false);
    }, []);


    function ReviewOperation() {
        return (
            <DataGrid
                rows={customerWalletOperation}
                columns={"12345"}
                pageSize={5}
                rowsPerPageOptions={[5]}
                checkboxSelection
            />
        );
    }


    return (
        <>
            {
                loading === true ?
                    <Spinner animation="border" role="status">
                        <span className="visually-hidden">Loading...</span>
                    </Spinner>
                    :
                    <Container>
                        <h1>WALLET REVIEW</h1>
                        <Row>
                            <h3>Your current wallet amount is:<br/> {wallet} Euro</h3>

                        </Row>
                        <Row>
                            <ReviewOperation/>
                        </Row>
                    </Container>
            }
        </>
    );
}

export {WalletOperation};