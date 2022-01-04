import * as React from "react";
import {render, fireEvent, waitFor, getByText, getByTestId} from '@testing-library/react';
import {BrowserRouter as Router} from "react-router-dom";
import "./NavbarApplication.css"

import {NavbarApplication} from "./NavbarApplication";

test("Alert Balance insufficient", async () => {

    const {getByText, getByTestId} = render(
        <Router>
            <NavbarApplication isLoggedFlag={true}
                               loggedUser={"mario.rossi@gmail.com"}
                               loggedUserRole={"CUSTOMER"}
                               topUpWarning={
                                   {
                                       "exist": "true",
                                       "message": "Balance insufficient, remember to top up!"
                                   }}
            />
        </Router>
    );

    let warning = getByTestId("topUpWarning");
    getByText("Log Out");
    fireEvent.mouseOver(warning);
    await waitFor(() => {
        getByText("Balance insufficient, remember to top up!")
    });
})

test("Alert missed pickups", async () => {

    localStorage.setItem("missedPickUp", '4');
    const {getByText, getByTestId} = render(
        <Router>
            <NavbarApplication isLoggedFlag={true}
                               loggedUser={"mario.rossi@gmail.com"}
                               loggedUserRole={"CUSTOMER"}
                               topUpWarning={
                                   {
                                       "exist": "true",
                                       "message": "Balance insufficient, remember to top up!"
                                   }}
            />
        </Router>
    );
    let warning = getByTestId("missedPickupWarning");
    getByText("Log Out");
    fireEvent.mouseOver(warning);
    await waitFor(() => {
        getByText(/There are/i);
        getByText(/4/i);
        getByText(/order\(s\) waiting for your pick up, please pick it up as soon as possible/i);
    });
})

test("No balance insufficient", async () => {

    const {getByText, getByAltText} = render(
        <Router>
            <NavbarApplication isLoggedFlag={true}
                               loggedUser={"mario.rossi@gmail.com"}
                               loggedUserRole={"CUSTOMER"}
                               topUpWarning={{"exist": "false"}}
            />
        </Router>
    );
    getByText("Log Out");
    expect(() => getByAltText('warning')).toThrow();
})

test("No user logged in", async () => {
    const {getByText, getByAltText} = render(
        <Router>
            <NavbarApplication isLoggedFlag={false}
                               loggedUser={""}
                               loggedUserRole={""}
            />
        </Router>
    );
    getByText("Browse Products")
    getByText("Log in");
    getByText("Sign up");
    expect(() => getByAltText('warning')).toThrow();
    expect(() => getByAltText('Dashboard')).toThrow();
})

test("No user logged in Browse Product", async () => {
    delete window.location
    window.location = new URL("http://localhost:3000/BrowseProducts")

    const {getByText, getByAltText} = render(
        <Router>
            <NavbarApplication isLoggedFlag={false}
                               loggedUser={""}
                               loggedUserRole={""}
            />
        </Router>
    );
    getByText("Log in");
    getByText("Sign up");
    expect(() => getByAltText('warning')).toThrow();
    expect(() => getByAltText('Dashboard')).toThrow();
    expect(() => getByAltText('Browse Products')).toThrow();
})

test("Logged user in Browse Product", async () => {
    delete window.location
    window.location = new URL("http://localhost:3000/BrowseProducts")

    const {getByText, getByAltText} = render(
        <Router>
            <NavbarApplication isLoggedFlag={true}
                               loggedUser={"mario.rossi@gmail.com"}
                               loggedUserRole={"CUSTOMER"}
                               topUpWarning={{"exist": "false"}}
            />
        </Router>
    );
    getByText("Log Out");
    getByText("Dashboard")
    expect(() => getByAltText('warning')).toThrow();
    expect(() => getByAltText('Browse Products')).toThrow();
    expect(() => getByAltText('Sign up')).toThrow();
    expect(() => getByAltText('Log In')).toThrow();
})