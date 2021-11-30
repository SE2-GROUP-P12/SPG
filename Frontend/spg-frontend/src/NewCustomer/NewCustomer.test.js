import * as React from "react";
import {render, fireEvent, waitFor } from '@testing-library/react';
import {BrowserRouter as Router} from "react-router-dom";

import {NewCustomer} from "./NewCustomer";
import {API} from "./../API/API";

var pbkdf2 = require('pbkdf2');
const crypto = require('crypto');

Object.defineProperty(global.self, 'crypto', {
    value: {
      getRandomValues: arr => crypto.randomBytes(arr.length)
    }
  });

test("renders correctly", async() => {
    const {getByText, getByLabelText} = render(
        <Router>
            <NewCustomer/>
        </Router>
        );
    getByText("Register");
    getByLabelText("Email*");
    getByLabelText("Password*");
    getByLabelText("Name*");
    getByLabelText("Surname*");
    getByLabelText("Address*");
    getByLabelText("SSN*");
    getByLabelText("Phone Number");
    getByText("Back");
    getByText("Submit");
});

test("empty fields", async() => {
    const {getByText, getByLabelText} = render(
        <Router>
            <NewCustomer/>
        </Router>
    );
    fireEvent.click(getByText("Submit"));
    await waitFor(()=>{
        getByText("Email is required");
        getByText("Password is required");
        getByText("Name is required");
        getByText("Surname is required");
        getByText("Address is required");
        getByText("SSN is required");
    })
});

test ("insecure password", async() => {
    const {getByText, getByLabelText} = render(
        <Router>
            <NewCustomer/>
        </Router>
    );
    const input = getByLabelText("Password*");
    fireEvent.change(input, { target: { value: "short" } });
    fireEvent.click(getByText("Submit"));
    await waitFor(()=>{
        getByText("Password must be at least 8 characters and have at least one letter and one number");
    });
});

test("create existing user", async() => {

    const mockCustomerExists = (API.customerExists = jest.fn());
    mockCustomerExists.mockResolvedValueOnce(true);
    const {getByText, getByLabelText} = render(
        <Router>
            <NewCustomer/>
        </Router>
    );
    fireEvent.change(getByLabelText("Email*"), { target: { value: "mario.rossi@gmail.com" } });
    fireEvent.change(getByLabelText("Password*"), { target: { value: "Pa55word" } });
    fireEvent.change(getByLabelText("Name*"), { target: { value: "Mario" } });
    fireEvent.change(getByLabelText("Surname*"), { target: { value: "Rossi" } });
    fireEvent.change(getByLabelText("Address*"), { target: { value: "Via Roma 15, Torino" } });
    fireEvent.change(getByLabelText("SSN*"), { target: { value: "RSSMRA00D12N376V" } });
    fireEvent.click(getByText("Submit"));
    await waitFor(()=>{
        getByText("The inserted email is already present in the system, use another one.");
    })

    expect(mockCustomerExists).toBeCalledTimes(1);
    expect(mockCustomerExists).toBeCalledWith({"email": 'mario.rossi@gmail.com', "ssn": 'RSSMRA00D12N376V'});
})

test("create new user", async() => {

    const mockCustomerExists = (API.customerExists = jest.fn());
    const mockAddCustomer = (API.addCustomer =jest.fn());

    mockCustomerExists.mockResolvedValueOnce(false);
    mockAddCustomer.mockResolvedValueOnce(true);
    
    const {getByText, getByLabelText} = render(
        <Router>
            <NewCustomer
            setLoggedUser={ (x)=>x } setLoggedFlag={ (x)=>x }
            setAccessToken={ (x)=>x } accessToken={ (x)=>x }
            setLoggedUserRole={ (x)=>x }
            />
        </Router>
    );
    fireEvent.change(getByLabelText("Email*"), { target: { value: "customer@gmail.com" } });
    fireEvent.change(getByLabelText("Password*"), { target: { value: "Pa55word" } });
    fireEvent.change(getByLabelText("Name*"), { target: { value: "Mario" } });
    fireEvent.change(getByLabelText("Surname*"), { target: { value: "Mario" } });
    fireEvent.change(getByLabelText("Address*"), { target: { value: "Via Roma 15, Torino" } });
    fireEvent.change(getByLabelText("SSN*"), { target: { value: "MRIMRA00D12N376V" } });
    fireEvent.change(getByLabelText("Phone Number"), {target: {value: "1234567890" }});
    fireEvent.click(getByText("Submit"));
    await waitFor(()=>{
        getByText("Everything goes well, new client created.");
    })

    expect(mockCustomerExists).toBeCalledTimes(1);
    expect(mockCustomerExists).toBeCalledWith({"email": 'customer@gmail.com', "ssn": 'MRIMRA00D12N376V'});
    expect(mockAddCustomer).toBeCalledTimes(1);
    expect(mockAddCustomer).toBeCalledWith({
        'address': "Via Roma 15, Torino",
        'email': "customer@gmail.com",
        'name': "Mario",
        'password': expect.anything(),
        'phoneNumber': "1234567890",
        'role': "CUSTOMER",
        'ssn': "MRIMRA00D12N376V",
        'surname': "Mario" } );
})

test("server error", async() => {

    const mockCustomerExists = (API.customerExists = jest.fn());
    mockCustomerExists.mockResolvedValueOnce(undefined);

    const {getByText, getByLabelText} = render(
        <Router>
            <NewCustomer
            setLoggedUser={ (x)=>x } setLoggedFlag={ (x)=>x }
            setAccessToken={ (x)=>x } accessToken={ (x)=>x }
            setLoggedUserRole={ (x)=>x }
            />
        </Router>
    );
    fireEvent.change(getByLabelText("Email*"), { target: { value: "mario.rossi@gmail.com" } });
    fireEvent.change(getByLabelText("Password*"), { target: { value: "Pa55word" } });
    fireEvent.change(getByLabelText("Name*"), { target: { value: "Mario" } });
    fireEvent.change(getByLabelText("Surname*"), { target: { value: "Rossi" } });
    fireEvent.change(getByLabelText("Address*"), { target: { value: "Via Roma 15, Torino" } });
    fireEvent.change(getByLabelText("SSN*"), { target: { value: "RSSMRA00D12N376V" } });
    fireEvent.click(getByText("Submit"));
    await waitFor(()=>{
        getByText("Something went wrong during the server processment, please retry. (500 Internal Server Error)");
    })

    expect(mockCustomerExists).toBeCalledTimes(1);
    expect(mockCustomerExists).toBeCalledWith({"email": 'mario.rossi@gmail.com', "ssn": 'RSSMRA00D12N376V'});
})