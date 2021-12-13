import * as React from "react";
import {render, fireEvent, waitFor} from '@testing-library/react';
import {BrowserRouter as Router} from "react-router-dom";
import {ConfirmAvailability} from "./ConfirmAvailability";
import {API} from "./../API/API";
import {getByLabelText} from "@testing-library/dom";

test('renders correctly', async() => {
    localStorage.setItem("username", "thomas.jefferson@gmail.com");
    const mockGetProducts = (API.browseProductsByFarmer = jest.fn());
    mockGetProducts.mockResolvedValueOnce({data: [{
            endAvailability: null,
            farmer: 5,
            imageUrl: "https://images.unsplash.com/photo-1560806887-1e4cd0b6cbd6",
            name: "Apples",
            price: 2.5,
            productId: 7,
            quantityAvailable: 50,
            quantityBaskets: 0,
            quantityConfirmed: 0,
            quantityDelivered: 0,
            quantityForecast: 50,
            quantityOrdered: 0,
            startAvailability: null,
            totalQuantity: 50,
            unitOfMeasurement: "Kg"
        }]});
    const {getByText, getByAltText} = render(
        <Router>
            <ConfirmAvailability/>
        </Router>);
    await waitFor(()=>{
        getByText("Confirm availabilities for the next week");
        getByText("Confirm all products");
        getByText("Apples");
        getByAltText("fruit");
        getByText("Confirm Forecasted Availability");
        getByText("Set Availability");
        getByText("Back");
        getByText("Submit");
    })
})

test('confirm forecasted', async() => {
    localStorage.setItem("username", "thomas.jefferson@gmail.com");
    const mockGetProducts = (API.browseProductsByFarmer = jest.fn());
    const mockSubmitConfirmed = (API.submitConfirmed = jest.fn());
    mockGetProducts.mockResolvedValueOnce({data: [{
            endAvailability: null,
            farmer: 5,
            imageUrl: "https://images.unsplash.com/photo-1560806887-1e4cd0b6cbd6",
            name: "Apples",
            price: 2.5,
            productId: 7,
            quantityAvailable: 50,
            quantityBaskets: 0,
            quantityConfirmed: 0,
            quantityDelivered: 0,
            quantityForecast: 50,
            quantityOrdered: 0,
            startAvailability: null,
            totalQuantity: 50,
            unitOfMeasurement: "Kg"
        }]});
    mockSubmitConfirmed.mockResolvedValueOnce(true);
    const {getByText} = render(
        <Router>
            <ConfirmAvailability/>
        </Router>);
    await waitFor(()=>{
        getByText(/0 Kg confirmed/)
    })
    fireEvent.click(getByText("Confirm Forecasted Availability"));
    await waitFor(()=>{
        getByText(/50 Kg confirmed/)
    })
    fireEvent.click(getByText("Submit"));
    await waitFor(()=>{
        getByText("Your availabilities have been communicated with success")
    })
})

test('confirm all forecasted', async() => {
    localStorage.setItem("username", "thomas.jefferson@gmail.com");
    const mockGetProducts = (API.browseProductsByFarmer = jest.fn());
    const mockSubmitConfirmed = (API.submitConfirmed = jest.fn());
    mockGetProducts.mockResolvedValueOnce({data: [{
            endAvailability: null,
            farmer: 5,
            imageUrl: "https://images.unsplash.com/photo-1560806887-1e4cd0b6cbd6",
            name: "Apples",
            price: 2.5,
            productId: 7,
            quantityAvailable: 50,
            quantityBaskets: 0,
            quantityConfirmed: 0,
            quantityDelivered: 0,
            quantityForecast: 50,
            quantityOrdered: 0,
            startAvailability: null,
            totalQuantity: 50,
            unitOfMeasurement: "Kg"
        }]});
    mockSubmitConfirmed.mockResolvedValueOnce(true);
    const {getByText} = render(
        <Router>
            <ConfirmAvailability/>
        </Router>);
    await waitFor(()=>{
        getByText(/0 Kg confirmed/)
    })
    fireEvent.click(getByText("Confirm all products"));
    await waitFor(()=>{
        getByText(/50 Kg confirmed/)
    })
    fireEvent.click(getByText("Submit"));
    await waitFor(()=>{
        getByText("Your availabilities have been communicated with success")
    })
})

test ('set forecast', async ()=>{
    localStorage.setItem("username", "thomas.jefferson@gmail.com");
    const mockGetProducts = (API.browseProductsByFarmer = jest.fn());
    const mockSubmitConfirmed = (API.submitConfirmed = jest.fn());
    mockGetProducts.mockResolvedValueOnce({data: [{
            endAvailability: null,
            farmer: 5,
            imageUrl: "https://images.unsplash.com/photo-1560806887-1e4cd0b6cbd6",
            name: "Apples",
            price: 2.5,
            productId: 7,
            quantityAvailable: 50,
            quantityBaskets: 0,
            quantityConfirmed: 0,
            quantityDelivered: 0,
            quantityForecast: 50,
            quantityOrdered: 0,
            startAvailability: null,
            totalQuantity: 50,
            unitOfMeasurement: "Kg"
        }]});
    mockSubmitConfirmed.mockResolvedValueOnce(true);
    const {getByText, getByLabelText, getAllByText} = render(
        <Router>
            <ConfirmAvailability/>
        </Router>);
    await waitFor(()=>{
        getByText(/0 Kg confirmed/)
    })
    fireEvent.click(getByText("Set Availability"));
    fireEvent.change(getByLabelText("Amount:"),{target: {value: 10}});
    fireEvent.click(getAllByText("Set Availability")[1])
    await waitFor(()=>{
        getByText(/10 Kg confirmed/)
    })
    fireEvent.click(getByText("Submit"));
    await waitFor(()=>{
        getByText("Your availabilities have been communicated with success")
    })
})