import * as React from "react";
import * as ReactDOM from "react-dom";
import {render, fireEvent, waitFor} from '@testing-library/react';

import {Dashboard} from "./Dashboard";
import {BrowserRouter as Router} from "react-router-dom";


const allServices = [
    /*PLACE ORDER*/
    {
        "type": "Your Cart",
        "description": "Get your current cart: modifiy, add, remove and handle your items.",
        "buttonLabel": "Show Cart",
        "linkUrl": "/PlaceOrder",
        "rolesPermitted": ['CUSTOMER', 'ADMIN', 'EMPLOYEE']
    },
    /*CUSTOMER ORDERS*/
    {
        "type": "Your Orders",
        "description": "Handle your orders and check tehir status in a simple way",
        "buttonLabel": "show orders",
        "linkUrl": "/Customer/Orders",
        "rolesPermitted": ['CUSTOMER', 'ADMIN']
    },
    /*BROWSE PRODUCT*/
    {
        "type": "Browse Products",
        "description": "Browse a huge line of item in out online store!",
        "buttonLabel": "Browse Products",
        "linkUrl": "/BrowseProducts",
        "rolesPermitted": ['CUSTOMER', 'ADMIN', 'EMPLOYEE']
    },
    /*CUSTOMER WALLET*/
    {
        "type": "Your Wallet",
        "description": "Handle you online wallet: recharge and place the orders as you want",
        "buttonLabel": "show wallet operation",
        "linkUrl": "/Customer/WalletOperations",
        "rolesPermitted": ['CUSTOMER', 'ADMIN']
    },
    /*NEW CUSTOMER*/
    {
        "type": "New Customer",
        "description": "Register new customer and then make people join us",
        "buttonLabel": "Resgister Customer",
        "linkUrl": "/NewCustomer",
        "rolesPermitted": ["ADMIN", "EMPLOYEE"]
    },
    /*TOP UP*/
    {
        "type": "Top Up",
        "description": "Top up customer wallet, choose among several payments methods",
        "buttonLabel": "Top Up",
        "linkUrl": "/TopUp",
        "rolesPermitted": ['ADMIN', 'EMPLOYEE']
    },
    /*DELIVER ORDER*/
    {
        "type": "Handle Order",
        "description": "Deliver Order to a customer and hanlde pending orders",
        "buttonLabel": "Handle Orders",
        "linkUrl": "/DeliverOrder",
        "rolesPermitted": ['ADMIN', 'EMPLOYEE']
    },
    /*FORECAST PRODUCTS*/
    {
        "type": "Forecast Product",
        "description": "Update your current estimation for the product you want to sell",
        "buttonLabel": "Forecast Products",
        "linkUrl": "/ProductsForecast",
        "rolesPermitted": ['ADMIN', 'FARMER']
    },
    /*ADD PRODUCT*/
    {
        "type": "Add Product",
        "description": "Add new products in our inventory: propose something new to your customers",
        "buttonLabel": "Add Product",
        "linkUrl": "/AddProduct",
        "rolesPermitted": ['FARMER', 'ADMIN']
    },
    /*USER SETTINGS*/
    {
        "type": "Settings",
        "description": "Handle you account: change your data, reset password and many more in one place",
        "buttonLabel": "Settings",
        "linkUrl": "/Settings",
        "rolesPermitted": ['CUSTOMER', 'ADMIN', 'FARMER', 'EMPLOYEE']
    },
];

test("CUSTOMER - renders correctly", () => {

    localStorage.setItem("role", "CUSTOMER")
    const loggedUser = "mario.rossi@gmail.com";
    const {getByText, getAllByText} = render(
        <Router>
            <Dashboard loggedUser={loggedUser} services={allServices}/>
        </Router>
    );

    getByText(/mario.rossi/);
    getByText("Your Cart");
    getByText("Your Orders");
    getAllByText("Browse Products");
    getByText("Your Wallet");
    getAllByText("Settings");

});

test("FARMER - renders correctly", () => {

    localStorage.setItem("role", "FARMER")
    const loggedUser = "thomas.jefferson@gmail.com";
    const {getByText, getAllByText} = render(
        <Router>
            <Dashboard loggedUser={loggedUser} services={allServices}/>
        </Router>
    );

    getByText(/thomas.jefferson/);
    getByText("Forecast Product");
    getAllByText("Add Product");
    getAllByText("Settings");
});

test("EMPLOYEE - renders correctly", () => {

    localStorage.setItem("role", "EMPLOYEE")
    const loggedUser = "francesco.conte@gmail.com";
    const {getByText, getAllByText} = render(
        <Router>
            <Dashboard loggedUser={loggedUser} services={allServices}/>
        </Router>
    );

    getByText(/francesco.conte/);
    getByText("Your Cart");
    getAllByText("Browse Products");
    getByText("New Customer");
    getAllByText("Top Up");
    getByText("Handle Order");
    getAllByText("Settings");

});