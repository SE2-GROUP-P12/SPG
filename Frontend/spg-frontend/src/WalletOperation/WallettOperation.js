import Container from "react-bootstrap/Container";
import Row from "react-bootstrap/Row";
import {API} from "../API/API";
import DataGrid from '@mui/material/Grid';
import {useEffect, useState} from "react";
import {Spinner, Table, Button} from "react-bootstrap";
import {Link} from "react-router-dom";


function WalletOperation() {
    const [loading, setLoadingSpinner] = useState(true);
    const [wallet, setWallet] = useState(0.00)
    const [customerWalletOperation, setCustomerWalletOperation] = useState([]);

    async function _getWallet() {
        const data = await API.getWalletOperation(localStorage.getItem("username"))
        setWallet(data.walletValue);
        setCustomerWalletOperation(data.operations);
    }

    useEffect(() => {
        _getWallet();
        setLoadingSpinner(false);
    }, []);

    function OperationTableRow(values) {
        let tmp;

        function convertTime(timestamp) {
            const date = new Date(timestamp * 1000);
            tmp = date.toDateString() + ", " + date.toTimeString().split('GMT')[0];
            return tmp;
        }

        return (
            <tr>
                <td>{values.id}</td>
                <td>{values.value.operationType}</td>
                <td>{convertTime(values.value.time)}</td>
                {
                    values.value.operationType === "TOP-UP" ?
                        <td style={{color: 'green'}}>+ {values.value.amount}</td>
                        :
                        <td style={{color: 'red'}}>- {values.value.amount}</td>
                }
                <td>actions placeholder</td>
            </tr>
        );
    }


    function ReviewOperation() {
        return (
            <Table responsive="sm" className="mt-5">
                <thead>
                <tr>
                    <th>#</th>
                    <th>OPERATION TYPE</th>
                    <th>TIMEDATE</th>
                    <th>AMOUNT</th>
                    <th>ACTIONS</th>
                </tr>
                </thead>
                <tbody>
                {customerWalletOperation.map((value, index) => <OperationTableRow id={index} value={value}/>)}
                </tbody>
            </Table>
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
                        <Link to="/Dashboard">
                            <Button variant="secondary" className="mt-5">
                                BACK
                            </Button>
                        </Link>

                    </Container>
            }
        </>
    );
}

export {WalletOperation};