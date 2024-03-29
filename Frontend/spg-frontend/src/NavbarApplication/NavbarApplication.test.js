import * as React from "react";
import {render, fireEvent, waitFor} from '@testing-library/react';
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

    const {getByText, getByTestId} = render(
        <Router>
            <NavbarApplication isLoggedFlag={true}
                               loggedUser={"mario.rossi@gmail.com"}
                               loggedUserRole={"CUSTOMER"}
                               topUpWarning={{"exist": "false"}}
            />
        </Router>
    );
    getByText("Log Out");
    expect(() => getByTestId("topUpWarning")).toThrow();
})

test("No user logged in", async () => {
    const {getByText, getByTestId} = render(
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
    expect(() => getByTestId("topUpWarning")).toThrow();
})

test("No user logged in Browse Product", async () => {
    delete window.location
    window.location = new URL("http://localhost:3000/BrowseProducts")

    const {getByText, getByAltText, getByTestId} = render(
        <Router>
            <NavbarApplication isLoggedFlag={false}
                               loggedUser={""}
                               loggedUserRole={""}
            />
        </Router>
    );
    getByText("Log in");
    getByText("Sign up");
    expect(() => getByTestId('topUpWarning')).toThrow();
    expect(() => getByAltText('Dashboard')).toThrow();
    expect(() => getByAltText('Browse Products')).toThrow();
})

test("Logged user in Browse Product", async () => {
    delete window.location
    window.location = new URL("http://localhost:3000/BrowseProducts")

    const {getByText, getByAltText, getByTestId} = render(
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
    expect(() => getByTestId('topUpWarning')).toThrow();
    expect(() => getByAltText('Browse Products')).toThrow();
    expect(() => getByAltText('Sign up')).toThrow();
    expect(() => getByAltText('Log In')).toThrow();
})

global.fetch = jest.fn();
beforeEach(()=>fetch.mockClear() );

test ("Log out - ok", async () => {
    fetch.mockImplementationOnce(()=>
        Promise.resolve({
            ok: true,
            status: 200
        })
    )
    const {getByText} = render(
        <Router>
            <NavbarApplication isLoggedFlag={true}
                               setLoggedFlag={x=>x}
                               setLoggedUser={x=>x}
                               setLoggedUserRole={x=>x}
                               setAccessToken={x=>x}
                               setTopUpWarning={x=>x}
                               loggedUser={"mario.rossi@gmail.com"}
                               loggedUserRole={"CUSTOMER"}
                               topUpWarning={{"exist": "false"}}
            />
        </Router>
    );
    await waitFor(()=>{
        getByText("Log Out");
    })
    fireEvent.click(getByText("Log Out"));
})

test ("Log out - fail", async () => {
    fetch.mockImplementationOnce(()=>
        Promise.resolve({
            ok: false,
            status: 400
        })
    )
    const {getByText} = render(
        <Router>
            <NavbarApplication isLoggedFlag={true}
                               setLoggedFlag={x=>x}
                               setLoggedUser={x=>x}
                               setLoggedUserRole={x=>x}
                               setAccessToken={x=>x}
                               setTopUpWarning={x=>x}
                               loggedUser={"mario.rossi@gmail.com"}
                               loggedUserRole={"CUSTOMER"}
                               topUpWarning={{"exist": "false"}}
            />
        </Router>
    );
    await waitFor(()=>{
        getByText("Log Out");
    })
    fireEvent.click(getByText("Log Out"));
})

test ("No signup in new customer", async() =>{
    delete window.location
    window.location = new URL("http://localhost:3000/NewCustomer")
    const {getByText} = render(
        <Router>
            <NavbarApplication isLoggedFlag={true}
                               loggedUser={"mario.rossi@gmail.com"}
                               loggedUserRole={"CUSTOMER"}
                               topUpWarning={{"exist": "false"}}
            />
        </Router>
    );
    await waitFor(()=>{
        getByText("Log Out");
        expect(()=>getByText("Sign up")).toThrow();
    })

})

test ("No login in login", async() =>{
    delete window.location
    window.location = new URL("http://localhost:3000/LoginComponent")
    const {getByText} = render(
        <Router>
            <NavbarApplication isLoggedFlag={false}
                               loggedUser={""}
                               loggedUserRole={""}
                               topUpWarning={{"exist": "false"}}
            />
        </Router>
    );
    await waitFor(()=>{
        getByText("Sign up");
        expect(()=>getByText("Log in")).toThrow();
    })

})