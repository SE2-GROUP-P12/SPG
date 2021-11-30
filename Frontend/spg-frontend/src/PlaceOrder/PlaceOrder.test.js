import * as React from "react";
import {render, fireEvent, waitFor } from '@testing-library/react';
import {BrowserRouter as Router} from "react-router-dom";

import {PlaceOrder} from "./PlaceOrder";
import {API} from "./../API/API";

const mockAddToCard = (API.addToCart = jest.fn());
const mockGetCart = (API.getCart = jest.fn());

test("renders correctly", async() => {

    localStorage.setItem("role", "CUSTOMER");
    localStorage.setItem("username", "mario.rossi@gmail.com");
    mockAddToCard.mockResolvedValueOnce(true);
    mockGetCart.mockResolvedValueOnce([{endAvailability: null, 
        farmer: 5, 
        imageUrl: "https://images.unsplash.com/photo-1560806887-1e4cd0b6cbd6",
        name: "Apples",
        price: 2.5,
        productId: 7,
        quantityAvailable: 1,
        quantityBaskets: 1,
        quantityDelivered: 0,
        quantityForecast: 0,
        quantityOrdered: 0,
        startAvailability: null,
        totalQuantity: 50,
        unitOfMeasurement: "Kg"}]);

    expect(API.addToCart({"productId":7 ,"email": 'mario.rossi@gmail.com',"quantity": 1})).toBeTruthy();
    
    const {getByText} = render(
        <Router>
            <PlaceOrder/>
        </Router>
    );

    //getByText("Apples");
     
    //expect(mockAddToCard).toBeCalledTimes(1);
    //expect(mockAddToCard).toBeCalledWith({"productId":7 ,"email": 'mario.rossi@gmail.com',"quantity": 1});
    expect(mockGetCart).toBeCalledTimes(1);
    expect(mockGetCart).toBeCalledWith({'email': localStorage.getItem("username")}, undefined);
});