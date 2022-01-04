import * as React from "react";
import {render, fireEvent, waitFor } from '@testing-library/react';
import {BrowserRouter as Router} from "react-router-dom";

import {AddProduct} from "./AddProduct";
import {API} from "./../API/API";

test("renders correctly", async() => {
    localStorage.setItem("role", "FARMER");
    const {getByText, getByLabelText} = render(
        <Router>
            <AddProduct/>
        </Router>
    );
    getByText("Add New Product");
    getByLabelText("Product Name*");
    getByLabelText("Price*");
    getByLabelText("Unit Of Measurement*");
    getByLabelText("Image Url");
    getByText("Back");
    getByText("Submit");
});

test("empty fields", async()=>{
    localStorage.setItem("role", "FARMER");
    const {getByText} = render(
        <Router>
            <AddProduct/>
        </Router>
    );
    fireEvent.click(getByText("Submit"));
    await waitFor(()=>{
        getByText("Product Name is required");
        getByText("Price is required");
        getByText("A Unit of measurement is required");
    })
})

const mockAddProduct = (API.addProduct=jest.fn());

test("successful add", async () => {
    localStorage.setItem("role", "FARMER");
    localStorage.setItem("username", "thomas.jefferson@gmail.com");
    mockAddProduct.mockResolvedValueOnce(true);
    const {getByText, getByLabelText} = render(
        <Router>
            <AddProduct/>
        </Router>
    );
    let name =getByLabelText("Product Name*");
    let price =getByLabelText("Price*");
    let unit= getByLabelText("Unit Of Measurement*");
    fireEvent.change(name, { target: { value: "A cool product" } });
    fireEvent.change(price, { target: { value: "1.00" } });
    fireEvent.change(unit, { target: { value: "units" } });
    fireEvent.click(getByText("Submit"));
    await waitFor(() => {
        getByText("Product successfully added")
    });

    expect(mockAddProduct).toBeCalledTimes(1);
    expect(mockAddProduct).toBeCalledWith({"email": "thomas.jefferson@gmail.com",
        "imageUrl": "",
        "price": 1,
        "productName": "A cool product",
        "unitOfMeasurement": "units"});
})

test("unsuccessful add", async () => {
    localStorage.setItem("role", "FARMER");
    mockAddProduct.mockResolvedValueOnce(false);
    const {getByText, getByLabelText} = render(
        <Router>
            <AddProduct/>
        </Router>
    );
    let name =getByLabelText("Product Name*");
    let price =getByLabelText("Price*");
    let unit= getByLabelText("Unit Of Measurement*");
    fireEvent.change(name, { target: { value: "A cool product" } });
    fireEvent.change(price, { target: { value: "1.00" } });
    fireEvent.change(unit, { target: { value: "units" } });
    fireEvent.click(getByText("Submit"));
    await waitFor(() => {
        getByText("Something went wrong")
    });
    expect(mockAddProduct).toBeCalledTimes(1);
    expect(mockAddProduct).toBeCalledWith({"email": "thomas.jefferson@gmail.com",
        "imageUrl": "",
        "price": 1,
        "productName": "A cool product",
        "unitOfMeasurement": "units"});
})

