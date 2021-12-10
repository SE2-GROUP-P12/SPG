import * as React from "react";
import {render, fireEvent, waitFor, getByText } from '@testing-library/react';
import {BrowserRouter as Router} from "react-router-dom";
import "./NavbarApplication.css"

import {NavbarApplication} from "./NavbarApplication";

test("Alert Balance insufficient", async() =>
{

    const {getByText, getByAltText} = render(
        <Router>
            <NavbarApplication isLoggedFlag = {true}
                               loggedUser={"mario.rossi@gmail.com"}
                               loggedUserRole={"CUSTOMER"}
                               topUpWarning={
                        {"exist": "true",
                        "message": "Balance insufficient, remember to top up!"}}
            />
        </Router>
        );

        let warning = getByAltText("warning");
        getByText("LOG OUT");
        fireEvent.mouseOver(warning);
        await waitFor (() => {
            getByText("Balance insufficient, remember to top up!")
        });
})

test("No balance insufficient", async() =>
{

    const {getByText, getByAltText} = render(
        <Router>
            <NavbarApplication isLoggedFlag = {true}
                               loggedUser={"mario.rossi@gmail.com"}
                               loggedUserRole={"CUSTOMER"}
                               topUpWarning={{"exist": "false"}}
            />
        </Router>
    );
    getByText("LOG OUT");
    expect(() => getByAltText('warning')).toThrow();
})

test ("No user logged in", async () =>{
    const {getByText, getByAltText} = render(
        <Router>
            <NavbarApplication isLoggedFlag = {false}
                               loggedUser={""}
                               loggedUserRole={""}
            />
        </Router>
    );
    getByText("Log in");
    getByText("Sign up");
    expect(() => getByAltText('warning')).toThrow();
})