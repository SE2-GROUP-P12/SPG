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

global.fetch = jest.fn();
beforeEach(()=>fetch.mockClear() );

test("Wrong credentials", async () => {

    fetch.mockImplementationOnce(()=>
        Promise.resolve({
            ok: false,
            status: 401,
            json: () => Promise.resolve({'errorMessage': 'Bad credentials'})
        })
    )
    const {getByText, getByLabelText, getAllByText} = render(
        <Router>
            <Login/>
        </Router>
    );
    fireEvent.change(getByLabelText("Email:"), { target: { value: "I@dont.exist" } });
    fireEvent.change(getByLabelText("Password:"), { target: { value: "somethingtotallYr4ndom" } });
    fireEvent.click(getAllByText("Login")[1]);
    await waitFor(()=>{
        getByText("Error: Bad credentials");
    });
})

test("Banned user", async () => {

    fetch.mockImplementationOnce(()=>
        Promise.resolve({
            ok: false,
            status: 401,
            json: () => Promise.resolve({'errorMessage': 'User is disabled'})
        })
    )
    const {getByText, getByLabelText, getAllByText} = render(
        <Router>
            <Login/>
        </Router>
    );
    fireEvent.change(getByLabelText("Email:"), { target: { value: "I@dont.exist" } });
    fireEvent.change(getByLabelText("Password:"), { target: { value: "somethingtotallYr4ndom" } });
    fireEvent.click(getAllByText("Login")[1]);
    await waitFor(()=>{
        getByText("Error: User is disabled");
    });
})

test("Right credentials", async() => {
    fetch.mockImplementationOnce(()=>
        Promise.resolve({
            ok: true,
            status: 200,
            json: () => Promise.resolve({'accessToken': '',
                                                refreshToken: '',
                                                username: '',
                                                role: 'CUSTOMER',
                                                missedPickUp: 0})
        })
    )
    const {getByLabelText, getAllByText} = render(
        <Router>
            <Login setLoggedUser={x=>x} setLoggedFlag={x=>x}
                   setAccessToken={x=>x} accessToken={x=>x}
                   setLoggedUserRole={x=>x}
                   setTopUpWarning={x=>x}/>
        </Router>
    );
    fireEvent.change(getByLabelText("Email:"), { target: { value: "I@dont.exist" } });
    fireEvent.change(getByLabelText("Password:"), { target: { value: "somethingtotallYr4ndom" } });
    fireEvent.click(getAllByText("Login")[1]);
    await waitFor(()=>{
        expect(global.window.location.pathname).toEqual('/Dashboard');
    });
})
