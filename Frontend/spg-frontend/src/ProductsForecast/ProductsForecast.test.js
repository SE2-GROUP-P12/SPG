import * as React from "react";
import {render, fireEvent, waitFor, getAllByText} from '@testing-library/react';
import {BrowserRouter as Router} from "react-router-dom";
import {ProductsForecast} from "./ProductsForecast";
import {API} from "./../API/API";

const mockBrowseProductsByFarmer = (API.browseProductsByFarmer = jest.fn());
const mockModifyForecast = (API.modifyForecast = jest.fn());

test ("Renders correctly", async () => {

    mockBrowseProductsByFarmer.mockResolvedValue({'data': [
        {
            "productId": "1",
            "name": "Apples",
            "producer": "Tonio Cartonio s.p.a.",
            "unitOfMeasurement": "kg",
            "totalQuantity": 10,
            "quantityAvailable": 10,
            "quantityBaskets": 0,
            "quantityOrdered": 0,
            "quantityDelivered": 0,
            "price": 2,
            "imageUrl": ""
        }
    ]});

    localStorage.setItem("role", "FARMER");
    localStorage.setItem("username", "thomas.jefferson@gmail.com");

    const {getByText} = render(
        <Router>
            <ProductsForecast
                setErrorMessage={null}
                errorMessage={null}
                isLogged={() => true}
                loggedUser={() => "thomas.jefferson@gmail.com"}
            />
        </Router>
    );

    getByText("Products List");
    getByText("Loading...");
    waitFor(() => {
        getByText("Apples");
        getByText(/2.00/);
    })
})

test("modify forecast ok", async() =>{
    localStorage.setItem("role", "FARMER");
    localStorage.setItem("username", "thomas.jefferson@gmail.com");
    mockBrowseProductsByFarmer.mockResolvedValue({'data': [
            {
                "productId": "1",
                "name": "Apples",
                "producer": "Tonio Cartonio s.p.a.",
                "unitOfMeasurement": "kg",
                "totalQuantity": 10,
                "quantityAvailable": 10,
                "quantityBaskets": 0,
                "quantityOrdered": 0,
                "quantityDelivered": 0,
                "price": 2,
                "imageUrl": ""
            }
        ]});
    mockModifyForecast.mockResolvedValueOnce(true);
    const {getByText, getByTestId, getAllByText} = render(
        <Router>
            <ProductsForecast/>
        </Router>
    );
    getByText("Products List");
    await waitFor(()=>{
        getByText("Apples");
        getByText(/2.00/i);
    })
    fireEvent.click(getByText("Modify Forecast"));
    await waitFor(()=>{
        getByText("Modify Forecast for Apples");
        getByText(/Amount:/i);
        getAllByText(/Kg/i);
    })
    fireEvent.change(getByTestId("amount"), {target:{value: 10}});
    fireEvent.click(getAllByText("Modify Forecast")[1]);
    await waitFor(()=>{
        getByText("Forecast successfully modified");
    })
    fireEvent.click(getByText(/Close/i));
})

test("modify forecast fail", async() =>{
    localStorage.setItem("role", "FARMER");
    localStorage.setItem("username", "thomas.jefferson@gmail.com");
    mockBrowseProductsByFarmer.mockResolvedValue({'data': [
            {
                "productId": "1",
                "name": "Apples",
                "producer": "Tonio Cartonio s.p.a.",
                "unitOfMeasurement": "kg",
                "totalQuantity": 10,
                "quantityAvailable": 10,
                "quantityBaskets": 0,
                "quantityOrdered": 0,
                "quantityDelivered": 0,
                "price": 2,
                "imageUrl": ""
            }
        ]});
    mockModifyForecast.mockResolvedValueOnce(false);
    const {getByText, getByTestId, getAllByText} = render(
        <Router>
            <ProductsForecast/>
        </Router>
    );
    getByText("Products List");
    await waitFor(()=>{
        getByText("Apples");
        getByText(/2.00/i);
    })
    fireEvent.click(getByText("Modify Forecast"));
    await waitFor(()=>{
        getByText("Modify Forecast for Apples");
        getByText(/Amount:/i);
        getAllByText(/Kg/i);
    })
    fireEvent.change(getByTestId("amount"), {target:{value: 10}});
    fireEvent.click(getAllByText("Modify Forecast")[1]);
    await waitFor(()=>{
        getByText("Something went wrong");
    })
    fireEvent.click(getByText(/Close/i));
})

test ("browse product error", async () => {

    mockBrowseProductsByFarmer.mockResolvedValue(null);

    localStorage.setItem("role", "FARMER");
    localStorage.setItem("username", "thomas.jefferson@gmail.com");

    const {} = render(
        <Router>
            <ProductsForecast
                setErrorMessage={null}
                errorMessage={null}
                isLogged={() => true}
                loggedUser={() => "thomas.jefferson@gmail.com"}
            />
        </Router>
    );
    await waitFor(()=>{
        expect(global.window.location.pathname).toEqual('/ErrorHandler');
    });
})