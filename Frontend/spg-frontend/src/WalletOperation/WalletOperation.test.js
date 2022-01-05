import * as React from "react";
import {render, fireEvent, waitFor, getAllByText} from '@testing-library/react';
import {BrowserRouter as Router} from "react-router-dom";

import {WalletOperation} from "./WallettOperation";
import {API} from "./../API/API";

const mockGetWalletOperation = (API.getWalletOperation = jest.fn());

test("renders correctly", async() =>{
    localStorage.setItem("role", "customer");
    localStorage.setItem("username", "mario.rossi@gmail.com");

    mockGetWalletOperation.mockResolvedValueOnce({operations: [],
    walletValue: 95})
    const {getByText, getByLabelText} = render(
        <Router>
            <WalletOperation />
        </Router>
    );
    await waitFor(()=>{
        getByText("WALLET REVIEW");
        getByText(/Your current wallet amount is: 95/i);
        getByText("#");
        getByText("OPERATION TYPE");
        getByText("TIMEDATE");
        getByText("AMOUNT");
        getByText("ACTIONS");
        getByText("BACK");
    });
});

test("operation list", async() =>{
    localStorage.setItem("role", "customer");
    localStorage.setItem("username", "mario.rossi@gmail.com");

    mockGetWalletOperation.mockResolvedValueOnce({operations: [{
            amount: 10,
            cust: null,
            operationType: "TOP-UP",
            time: 1641303893,
            walletOperationId: null
        },
            {
                amount: 15,
                cust: null,
                operationType: "SALE",
                time: 1644009035,
                walletOperationId: null
            }],
        walletValue: 95})
    const {getByText, getAllByText} = render(
        <Router>
            <WalletOperation />
        </Router>
    );
    await waitFor(()=>{
        getByText("0");
        getByText(/\+ 10/i);
        //getByText(/Tue Jan 04 2022, 14:44:53/i);
        getByText("1");
        getByText(/\- 15/i);
        //getByText(/Fri Feb 04 2022, 22:10:35/i);
        getAllByText("actions placeholder")
    });
});