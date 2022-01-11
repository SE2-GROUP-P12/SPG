import * as React from "react";
import {render, fireEvent, waitFor } from '@testing-library/react';
import {BrowserRouter as Router} from "react-router-dom";

import {TopUp} from "./TopUp";
import {API} from "./../API/API";

const mockGetWallet = (API.getWallet = jest.fn());
const mockTopUp = (API.topUp = jest.fn());

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
    await waitFor(()=>{
        getByText("User correctly found");
        getByText(/97.40/i);
    })
});

test("user not found", async() =>{
    localStorage.setItem("role", "EMPLOYEE");
    localStorage.setItem("username", "francesco.conte@gmail.com");
    mockGetWallet.mockResolvedValueOnce(undefined);
    const {getByText, getByLabelText} = render(
        <Router>
            <TopUp/>
        </Router>
    );

    getByText("Top Up!");
    fireEvent.change(getByLabelText("Email:"), { target: { value: 'mock@gmail.com' } });
    fireEvent.click(getByText("Submit"));
    await waitFor(()=>{
        getByText("User not found");
        getByText(/0.00/i);
    })
});


test("perform top up - ok", async() => {
    localStorage.setItem("role", "EMPLOYEE");
    localStorage.setItem("username", "francesco.conte@gmail.com");
    mockGetWallet.mockResolvedValueOnce(97.402109999999);
    mockTopUp.mockResolvedValueOnce(true);
    const {getByText, getByLabelText, getAllByText, getByTestId} = render(
        <Router>
            <TopUp/>
        </Router>
    );

    getByText("Top Up!");
    fireEvent.change(getByLabelText("Email:"), { target: { value: 'mario.rossi@gmail.com' } });
    fireEvent.click(getByText("Submit"));
    await waitFor(()=>{
        getByText("User correctly found");
        getByText(/97.40/i);
    })
    fireEvent.click(getByText("Cash"));
    await waitFor(()=>{
        getAllByText("Top Up");
        getByText(/Amount:/i);
    })
    fireEvent.change(getByTestId("amount"), { target: { value: "10" } })
    fireEvent.click(getAllByText("Top Up")[1]);
    await waitFor(()=>{
        getByText("Top up correctly performed");
    })
    await waitFor(()=>{
        getByText(/107.40/i);
    })
})

test("perform top up - fail", async() => {
    localStorage.setItem("role", "EMPLOYEE");
    localStorage.setItem("username", "francesco.conte@gmail.com");
    mockGetWallet.mockResolvedValueOnce(97.402109999999);
    mockTopUp.mockResolvedValueOnce(false);
    const {getByText, getByLabelText, getAllByText, getByTestId} = render(
        <Router>
            <TopUp/>
        </Router>
    );

    getByText("Top Up!");
    fireEvent.change(getByLabelText("Email:"), { target: { value: 'mario.rossi@gmail.com' } });
    fireEvent.click(getByText("Submit"));
    await waitFor(()=>{
        getByText("User correctly found");
        getByText(/97.40/i);
    })
    fireEvent.click(getByText("Cash"));
    await waitFor(()=>{
        getAllByText("Top Up");
        getByText(/Amount:/i);
    })
    fireEvent.change(getByTestId("amount"), { target: { value: "10" } })
    fireEvent.click(getAllByText("Top Up")[1]);
    await waitFor(()=>{
        getByText("Something went wrong during the top up");
    })
    await waitFor(()=>{
        getByText(/97.40/i);
    })
})