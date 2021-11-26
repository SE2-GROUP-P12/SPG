import * as React from "react";
import {render, fireEvent, waitFor } from '@testing-library/react';
import {BrowserRouter as Router} from "react-router-dom";

import {BrowseProducts} from "./BrowseProducts";
import {API} from "./../API/API";

const mockBrowseProducts = (API.browseProducts = jest.fn());
const mockAddToCard = (API.addToCart = jest.fn());
const buttonText = "Add to cart";

test ("Renders correctly", async () => {

    mockBrowseProducts.mockResolvedValueOnce([
        {
          "productId":"1",
          "name": "Apples",
          "producer" : "Tonio Cartonio s.p.a.",
          "unitOfMeasurement":"kg",
          "totalQuantity": 10,
          "quantityAvailable": 10,
          "quantityBaskets": 0,
          "quantityOrdered": 0,
          "quantityDelivered": 0,
          "price": 5,
          "imageUrl" : ""
        }
      ]);

    const {getByText, getByAltText} = render(
    <Router>
        <BrowseProducts/>
    </Router>
    );

    getByText("Products List");
    getByText("Loading...");
    await waitFor(()=>{
        getByText("Apples");
        getByAltText("fruit");
        getByText(buttonText);
    })

    expect(mockBrowseProducts).toBeCalledTimes(1);
    expect(mockBrowseProducts).toBeCalledWith();
});

test("Add to cart", async () => {

    mockBrowseProducts.mockResolvedValueOnce([
        {
          "productId":5,
          "name": "Apples",
          "producer" : "Tonio Cartonio s.p.a.",
          "unitOfMeasurement":"kg",
          "totalQuantity": 10,
          "quantityAvailable": 10,
          "quantityBaskets": 0,
          "quantityOrdered": 0,
          "quantityDelivered": 0,
          "price": 5,
          "imageUrl" : ""
        }
      ]);
    
    mockAddToCard.mockResolvedValueOnce(true);

    const {getByText, getByAltText, getByLabelText, getAllByText} = render(
    <Router>
        <BrowseProducts/>
    </Router>
    );

    getByText("Products List");
    getByText("Loading...");
    await waitFor(async ()=>{
        getByText("Apples");
        getByText(buttonText);
        getByAltText("fruit");
        fireEvent.click(getByText(buttonText));
    });
    await waitFor(async ()=>{
        const input = getByLabelText("Amount:");
        fireEvent.change(input, { target: { value: 1 } });
        fireEvent.click(getAllByText(buttonText)[1]);
    });
    await waitFor(async () => {
        getByText("Product added successfully");
        fireEvent.click(getByText("Close"));
    })
    
    expect(mockBrowseProducts).toBeCalledTimes(1);
    expect(mockBrowseProducts).toBeCalledWith();
    expect(mockAddToCard).toBeCalledTimes(1);
    expect(mockAddToCard).toBeCalledWith({"productId":5 ,"email": 'mario.rossi@gmail.com',"quantity": 1});
});

test ("Failed to load products", async() => {
    mockBrowseProducts.mockResolvedValueOnce(undefined);

    const {getByText, getByAltText} = render(
        <Router>
            <BrowseProducts/>
        </Router>
    );

    getByText("Products List");
    getByText("Loading...");
    await waitFor(()=>{
        getByText("There has been an error contacting the server");
    })

    expect(mockBrowseProducts).toBeCalledTimes(1);
    expect(mockBrowseProducts).toBeCalledWith();
});

