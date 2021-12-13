import * as React from "react";
import {render, fireEvent, waitFor } from '@testing-library/react';
import {BrowserRouter as Router} from "react-router-dom";

import {PlaceOrder} from "./PlaceOrder";
import {API} from "./../API/API";

const mockGetCart = (API.getCart = jest.fn());

test("double digits decimals", async() =>{
    localStorage.setItem("role", "CUSTOMER");
    localStorage.setItem("username", "mario.rossi@gmail.com");
    mockGetCart.mockResolvedValueOnce(
        [
            {   "productId":7,
                "name":"Apples",
                "unitOfMeasurement":"Kg",
                "totalQuantity":50.0,
                "quantityAvailable":2.0,
                "price":2.5,
            }]);
    const {getByText} = render(
        <Router>
            <PlaceOrder time={"10:00"}
                        date={"Sab"}
                        loggedUser={"mario.rossi@gmail.com"}
                        loggedUserRole={"CUSTOMER"}
            />
        </Router>
    );

    getByText("Place Order");
    waitFor(()=>{
        getByText("5.00");
    })
});

test("Skip for now", async()=>{
    localStorage.setItem("role", "CUSTOMER");
    localStorage.setItem("username", "mario.rossi@gmail.com");
    mockGetCart.mockResolvedValueOnce(
        [
            {   "productId":7,
                "name":"Apples",
                "unitOfMeasurement":"Kg",
                "totalQuantity":50.0,
                "quantityAvailable":2.0,
                "price":2.5,
            }]);
    const {getByText, getByLabelText} = render(
        <Router>
            <PlaceOrder time={"10:00"}
                        date={"Sab"}
                        loggedUser={"mario.rossi@gmail.com"}
                        loggedUserRole={"CUSTOMER"}
            />
        </Router>
    );
    fireEvent.click(getByText("Send order"));
    waitFor(()=>{
        getByText("Review and comfirme order");
        getByText("mario.rossi@gmail.com");
        fireEvent.click(getByLabelText("SKIP FOR NOW"));
        getByLabelText("SET SHIPPING INFO");
        getByText("Don't forget to set up your shipping info!")
    })
})

test("Schedule pickup - ok", async()=>{
    localStorage.setItem("role", "CUSTOMER");
    localStorage.setItem("username", "mario.rossi@gmail.com");
    mockGetCart.mockResolvedValueOnce(
        [
            {   "productId":7,
                "name":"Apples",
                "unitOfMeasurement":"Kg",
                "totalQuantity":50.0,
                "quantityAvailable":2.0,
                "price":2.5,
            }]);
    const mockPlaceOrder = (API.placeOrder=jest.fn());
    mockPlaceOrder.mockResolvedValueOnce(true);
    const {getByText, getByLabelText} = render(
        <Router>
            <PlaceOrder time={"10:00"}
                        date={"Sab"}
                        loggedUser={"mario.rossi@gmail.com"}
                        loggedUserRole={"CUSTOMER"}
            />
        </Router>
    );
    fireEvent.click(getByText("Send order"));
    waitFor(()=>{
        getByText("NOTICE: Delivery and Pick up are available Wednedasy, Thursday and Friday from 9:00 to 18:00.")
        fireEvent.change(getByLabelText("date"), {target:{value:"12/22/2021"}});
        fireEvent.change(getByLabelText("time"), {target:{value:"10:00"}});
        fireEvent.click(getByText("Place Order"));
        getByText("Order Completed");
        getByText("Order placed correctly!");

    })
})

test("Schedule pickup - fail", async()=>{
    localStorage.setItem("role", "CUSTOMER");
    localStorage.setItem("username", "mario.rossi@gmail.com");
    mockGetCart.mockResolvedValueOnce(
        [
            {   "productId":7,
                "name":"Apples",
                "unitOfMeasurement":"Kg",
                "totalQuantity":50.0,
                "quantityAvailable":2.0,
                "price":2.5,
            }]);
    const mockPlaceOrder = (API.placeOrder=jest.fn());
    mockPlaceOrder.mockResolvedValueOnce(false);
    const {getByText, getByLabelText} = render(
        <Router>
            <PlaceOrder time={"10:00"}
                        date={"Sab"}
                        loggedUser={"mario.rossi@gmail.com"}
                        loggedUserRole={"CUSTOMER"}
            />
        </Router>
    );
    fireEvent.click(getByText("Send order"));
    waitFor(()=>{
        getByText("NOTICE: Delivery and Pick up are available Wednedasy, Thursday and Friday from 9:00 to 18:00.")
        fireEvent.change(getByLabelText("date"), {target:{value:"12/22/2021"}});
        fireEvent.change(getByLabelText("time"), {target:{value:"10:00"}});
        fireEvent.click(getByText("Place Order"));
        getByText("Order Completed");
        getByText("Error in placed order, retry!");

    })
})

test("Home delivery - ok", async()=>{
    localStorage.setItem("role", "CUSTOMER");
    localStorage.setItem("username", "mario.rossi@gmail.com");
    mockGetCart.mockResolvedValueOnce(
        [
            {   "productId":7,
                "name":"Apples",
                "unitOfMeasurement":"Kg",
                "totalQuantity":50.0,
                "quantityAvailable":2.0,
                "price":2.5,
            }]);
    const mockPlaceOrder = (API.placeOrder=jest.fn());
    mockPlaceOrder.mockResolvedValueOnce(true);
    const {getByText, getByLabelText} = render(
        <Router>
            <PlaceOrder time={"10:00"}
                        date={"Sab"}
                        loggedUser={"mario.rossi@gmail.com"}
                        loggedUserRole={"CUSTOMER"}
            />
        </Router>
    );
    fireEvent.click(getByText("Send order"));
    waitFor(()=>{
        getByText("NOTICE: Delivery and Pick up are available Wednedasy, Thursday and Friday from 9:00 to 18:00.")
        fireEvent.change(getByLabelText("date"), {target:{value:"12/22/2021"}});
        fireEvent.change(getByLabelText("time"), {target:{value:"10:00"}});
        fireEvent.click(getByLabelText("Home delivery"));
        fireEvent.change(getByLabelText("address"), {target:{value:"Via Roma 10, Torino"}});
        getByText("Order Completed");
        getByText("Order placed correctly!");

    })
})

test("Home delivery - fail", async()=>{
    localStorage.setItem("role", "CUSTOMER");
    localStorage.setItem("username", "mario.rossi@gmail.com");
    mockGetCart.mockResolvedValueOnce(
        [
            {   "productId":7,
                "name":"Apples",
                "unitOfMeasurement":"Kg",
                "totalQuantity":50.0,
                "quantityAvailable":2.0,
                "price":2.5,
            }]);
    const mockPlaceOrder = (API.placeOrder=jest.fn());
    mockPlaceOrder.mockResolvedValueOnce(false);
    const {getByText, getByLabelText} = render(
        <Router>
            <PlaceOrder time={"10:00"}
                        date={"Sab"}
                        loggedUser={"mario.rossi@gmail.com"}
                        loggedUserRole={"CUSTOMER"}
            />
        </Router>
    );
    fireEvent.click(getByText("Send order"));
    waitFor(()=>{
        getByText("NOTICE: Delivery and Pick up are available Wednedasy, Thursday and Friday from 9:00 to 18:00.")
        fireEvent.change(getByLabelText("date"), {target:{value:"12/22/2021"}});
        fireEvent.change(getByLabelText("time"), {target:{value:"10:00"}});
        fireEvent.click(getByLabelText("Home delivery"));
        fireEvent.change(getByLabelText("address"), {target:{value:"Via Roma 10, Torino"}});
        getByText("Order Completed");
        getByText("Error in placed order, retry!");
    })
})