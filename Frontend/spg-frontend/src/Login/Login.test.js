import * as React from "react";
import {render, fireEvent, waitFor } from '@testing-library/react';
import {BrowserRouter as Router} from "react-router-dom";

import {Login} from "./Login";
import {API} from "./../API/API";


var pbkdf2 = require('pbkdf2');
const crypto = require('crypto');

Object.defineProperty(global.self, 'crypto', {
    value: {
        getRandomValues: arr => crypto.randomBytes(arr.length)
    }
});

test("Renders properly", ()=>{
    const {getByText, getByLabelText, getAllByText} = render(
        <Router>
            <Login/>
        </Router>
    );
    getAllByText("Login");
    getByLabelText("Email:");
    getByLabelText("Password:");
    getByText("Back");
});

test("Missing credentials", async ()=>{
    const {getByText, getByLabelText, getAllByText} = render(
        <Router>
            <Login/>
        </Router>
    );
    fireEvent.click(getAllByText("Login")[1]);
    await waitFor(()=>{
        getByText("Email is required");
        getByText("Password is required");
    });
})

/*test("Wrong credentials", async () => {

    const mockLogin = (API.login = jest.fn());
    mockLogin.mockResolvedValueOnce(false); //assuming that login API returns false on 401
    const {getByText, getByLabelText, getAllByText} = render(
        <Router>
            <Login/>
        </Router>
    );
    fireEvent.change(getByLabelText("Email:"), { target: { value: "I@dont.exist" } });
    fireEvent.change(getByLabelText("Password:"), { target: { value: "somethingtotallYr4ndom" } });
    fireEvent.click(getAllByText("Login")[1]);
    await waitFor(()=>{
        getByText("Wrong Credentials, please recheck your credentials.");
    });
    expect(mockLogin).toBeCalledTimes(1);
    expect(mockLogin).toBeCalledWith({"email":"I@dont.exist", "password":expect.anything()});
})

test("Right credentials", async() => {
    const mockLogin = (API.login = jest.fn());
    mockLogin.mockResolvedValueOnce(false); //assuming that login API returns false on 401
    const {getByText, getByLabelText, getAllByText} = render(
        <Router>
            <Login/>
        </Router>
    );
    fireEvent.change(getByLabelText("Email:"), { target: { value: "mario.rossi@gmail.com" } });
    fireEvent.change(getByLabelText("Password:"), { target: { value: "password" } });
    fireEvent.click(getAllByText("Login")[1]);
    await waitFor(()=>{
        expect(location.pathname).toEqual('/CUSTOMER'); //or something similar idk
    });
    expect(mockLogin).toBeCalledTimes(1);
    expect(mockLogin).toBeCalledWith({"email":"I@dont.exist", "password":expect.anything()});
})*/
