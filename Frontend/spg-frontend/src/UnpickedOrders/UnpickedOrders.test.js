import * as React from "react";
import {waitFor, fireEvent, render} from "@testing-library/react";
import {BrowserRouter as Router} from "react-router-dom";
import {UnpickedOrders} from "./UnpickedOrders";
import {API} from "../API/API";

const mockGetUnretrievedOrders = (API.getUnretrievedOrders = jest.fn());
const dateTime = 1641303893772; //moment 04/01/2022 altrimenti dovremmo cambiare i test ogni giorno

 test ("Renders Correctly", async () => {

        localStorage.setItem("role", "ADMIN");
        mockGetUnretrievedOrders.mockResolvedValueOnce([]);
        const {getByText} = render(
            <Router>
                <UnpickedOrders dateTime={dateTime}/>
            </Router>
        );

        await waitFor(() => {
            getByText("Unpicked orders");
            getByText("Show Last Month");
            getByText("No unpicked orders in this time period! 🎉")
            getByText("Back");
        });
});

test ("Toggle Month/Week", async () => {

    localStorage.setItem("role", "ADMIN");
    mockGetUnretrievedOrders.mockResolvedValueOnce([
        {   creationDate: "2022-01-04T12:10:05.194",
            currentStatusDate: "2022-01-04T12:19:54.465192",
            email: "mario.rossi@gmail.com",
            orderId: 63,
            productList: [
                {amount: "7.0", name: "Apples", producer: "Thomas Jefferson", productId: "7", unit: "Kg", "unit price": "2.5"},
                {amount: "5.0", name: "Oranges", producer: "Alexander Hamilton", productId: "10", unit: "Kg", "unit price": "2.1"}
            ],
            status: "NOT_RETRIEVED",
            value: 28},
        {
            creationDate: "2022-01-04T13:52:48.558",
            currentStatusDate: "2021-12-24T13:52:48.597",
            email: "customer@gmail.com",
            orderId: 86,
            productList: [
                {amount: "6.0", name: "Oranges", producer: "Alexander Hamilton", productId: "10", unit: "Kg", "unit price": "2.1"},
                {amount: "8.0", name: "Apples", producer: "Thomas Jefferson", productId: "7", unit: "Kg", "unit price": "2.5"}
            ],
            status: "NOT_RETRIEVED",
            value: 32.6
        }
    ]);
    const {getByText} = render(
        <Router>
            <UnpickedOrders dateTime={dateTime}/>
        </Router>
    );

    await waitFor(() => {
        getByText("Unpicked orders");
        getByText("Show Last Month");
        getByText(/Apples : 7.0 Kg/i);
        getByText(/SUBTOTAL: 17.50€/i);
        getByText(/Total: 28.00 €/i);
        getByText(/Customer: mario.rossi@gmail.com/i);
        getByText(/Since: 04\/01\/2022/i);
        expect(()=>getByText(/Since: 24\/12\/2021/i)).toThrow();
        getByText("Back");
    });
    fireEvent.click(getByText("Show Last Month"));
    await waitFor(()=>{
        getByText("Show Last Week");
        getByText(/Apples : 7.0 Kg/i);
        getByText(/SUBTOTAL: 17.50€/i);
        getByText(/Total: 28.00 €/i);
        getByText(/Customer: mario.rossi@gmail.com/i);
        getByText(/Since: 04\/01\/2022/i);
        getByText(/Apples : 8.0 Kg/i);
        getByText(/SUBTOTAL: 20.00€/i);
        getByText(/Total: 32.60 €/i);
        getByText(/Customer: customer@gmail.com/i);
        getByText(/Since: 24\/12\/2021/i);
    });
});

test ("Empty week full month", async () => {

    localStorage.setItem("role", "ADMIN");
    mockGetUnretrievedOrders.mockResolvedValueOnce([
        {
            creationDate: "2021-12-24T13:52:48.558",
            currentStatusDate: "2021-12-24T13:52:48.597",
            email: "customer@gmail.com",
            orderId: 86,
            productList: [
                {amount: "6.0", name: "Oranges", producer: "Alexander Hamilton", productId: "10", unit: "Kg", "unit price": "2.1"},
                {amount: "8.0", name: "Apples", producer: "Thomas Jefferson", productId: "7", unit: "Kg", "unit price": "2.5"}
            ],
            status: "NOT_RETRIEVED",
            value: 32.6
        }
    ]);
    const {getByText} = render(
        <Router>
            <UnpickedOrders dateTime={dateTime}/>
        </Router>
    );

    await waitFor(() => {
        getByText("Unpicked orders");
        getByText("Show Last Month");
        getByText("No unpicked orders in this time period! 🎉");
        expect(()=>getByText(/Since: 24\/12\/2021/i)).toThrow();
        getByText("Back");
    });
    fireEvent.click(getByText("Show Last Month"));
    await waitFor(()=>{
        getByText("Show Last Week");
        getByText(/Apples : 8.0 Kg/i);
        getByText(/SUBTOTAL: 20.00€/i);
        getByText(/Total: 32.60 €/i);
        getByText(/Customer: customer@gmail.com/i);
        getByText(/Since: 24\/12\/2021/i);
    });
})

test ("Empty week empty month", async () => {

    localStorage.setItem("role", "ADMIN");
    mockGetUnretrievedOrders.mockResolvedValueOnce([]);
    const {getByText} = render(
        <Router>
            <UnpickedOrders dateTime={dateTime}/>
        </Router>
    );

    await waitFor(() => {
        getByText("Unpicked orders");
        getByText("Show Last Month");
        getByText("No unpicked orders in this time period! 🎉");
        getByText("Back");
    });
    fireEvent.click(getByText("Show Last Month"));
    await waitFor(()=>{
        getByText("Show Last Week");
        getByText("No unpicked orders in this time period! 🎉");
    });
})

