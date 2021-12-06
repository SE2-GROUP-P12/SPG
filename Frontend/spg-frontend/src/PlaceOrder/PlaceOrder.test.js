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