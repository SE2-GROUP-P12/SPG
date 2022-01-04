import * as React from "react";
import {render, fireEvent, waitFor } from '@testing-library/react';
import {BrowserRouter as Router} from "react-router-dom";

import {TopUp} from "./TopUp";
import {API} from "./../API/API";

const mockGetWallet = (API.getWallet = jest.fn());

test("double digits decimals", async() =>{
    localStorage.setItem("role", "EMPLOYEE");
    localStorage.setItem("username", "francesco.conte@gmail.com");
    mockGetWallet.mockResolvedValueOnce(97.402109999999);
    const {getByText, getByLabelText} = render(
        <Router>
            <TopUp/>
        </Router>
    );

    getByText("Top Up!");
    fireEvent.change(getByLabelText("Email:"), { target: { value: 'mario.rossi@gmail.com' } });
    fireEvent.click(getByText("Submit"));
    waitFor(()=>{
        getByText("User correctly found");
        getByText("97.40");
    })
});

test("perform top up - ok", async() => {
    localStorage.setItem("role", "EMPLOYEE");
    localStorage.setItem("username", "francesco.conte@gmail.com");
    mockGetWallet.mockResolvedValueOnce(97.402109999999);
    const {getByText, getByLabelText, getAllByText} = render(
        <Router>
            <TopUp/>
        </Router>
    );

    //TODO you were here 
    getByText("Top Up!");
    fireEvent.change(getByLabelText("Email:"), { target: { value: 'mario.rossi@gmail.com' } });
    fireEvent.click(getByText("Submit"));
    waitFor(()=>{
        getByText("User correctly found");
        getByText("97.40");
    })
    fireEvent.click(getByText("Cash"));
    waitFor(()=>{
        getAllByText("Top Up");
        getByText("Amount: ");
        getByText("€")
    })
})