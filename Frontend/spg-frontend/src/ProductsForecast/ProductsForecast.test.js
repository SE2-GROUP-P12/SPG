import * as React from "react";
import {render, fireEvent, waitFor } from '@testing-library/react';
import {BrowserRouter as Router} from "react-router-dom";
import {ProductsForecast} from "./ProductsForecast";
import {API} from "./../API/API";

const mockBrowseProductsByFarmer = (API.browseProductsByFarmer = jest.fn());

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

/*test("double digits decimals", async() =>{
    localStorage.setItem("role", "FARMER");
    localStorage.setItem("username", "thomas.jefferson@gmail.com");
    mockBrowseProductsByFarmer.mockResolvedValueOnce([
        {
            endAvailability: null,
            farmer: 5,
            imageUrl: "https://images.unsplash.com/photo-1560806887-1e4cd0b6cbd6",
            name: "Apples",
            price: 2,
            productId: 7,
            quantityAvailable: 47,
            quantityBaskets: 3,
            quantityDelivered: 0,
            quantityForecast: 0,
            quantityOrdered: 0,
            startAvailability: null,
            totalQuantity: 50,
            unitOfMeasurement: "Kg"
        }
    ])

    const {getByText} = render(
        <Router>
            <ProductsForecast/>
        </Router>
    )

    //getByText("Products List");
    /*await waitFor(()=>{
        getByText("Apples");
        getByText("2.00");
    })

})*/
