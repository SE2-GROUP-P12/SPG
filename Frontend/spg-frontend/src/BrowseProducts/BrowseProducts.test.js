import * as React from "react";
import {render, fireEvent, waitFor} from '@testing-library/react';
import {BrowserRouter as Router} from "react-router-dom";
import {BrowseProducts} from "./BrowseProducts";
import {API} from "./../API/API";

const mockBrowseProducts = (API.browseProducts = jest.fn());
const mockAddToCard = (API.addToCart = jest.fn());
const mockGetCart = (API.getCart = jest.fn());
const buttonText = "Add to cart";

test("Renders correctly", async () => {

    mockBrowseProducts.mockResolvedValueOnce({data:[
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
            "price": 5,
            "imageUrl": ""
        }
    ]});
    mockGetCart.mockResolvedValueOnce([]);

    localStorage.setItem("role", "CUSTOMER");
    localStorage.setItem("username", "mario.rossi@gmail.com");

    const {getByText, getByAltText} = render(
        <Router>
            <BrowseProducts
                setErrorMessage={null}
                errorMessage={null}
                isLogged={() => true}
                loggedUser={() => "mario.rossi@gmail.com"}
            />
        </Router>
    );

    await waitFor(() => {
        getByText("Products List");
        getByText("🛒 0 item(s)");
        getByText(/Apples/i);
        getByAltText("fruit");
        getByText(buttonText);
    })
});

test("Add to cart", async () => {

    mockBrowseProducts.mockResolvedValueOnce( {data:[
        {
            "productId": 5,
            "name": "Apples",
            "producer": "Tonio Cartonio s.p.a.",
            "unitOfMeasurement": "kg",
            "totalQuantity": 10,
            "quantityAvailable": 10,
            "quantityBaskets": 0,
            "quantityOrdered": 0,
            "quantityDelivered": 0,
            "price": 5,
            "imageUrl": ""
        }
    ]});
    mockGetCart.mockResolvedValueOnce([]);
    mockGetCart.mockResolvedValueOnce([{
        "endAvailability": null,
        "farmer": 5,
        "imageUrl": "",
        "name": "Apples",
        "price": 2.5,
        "productId": 5,
        "quantityAvailable": 1,
        "quantityBaskets": 1,
        "quantityDelivered": 0,
        "quantityForecast": 0,
        "quantityOrdered": 25,
        "startAvailability": null,
        "totalQuantity": 50,
        "unitOfMeasurement": "Kg"
    }])
    mockAddToCard.mockResolvedValueOnce(true);
    localStorage.setItem("role", "CUSTOMER");
    localStorage.setItem("username", "mario.rossi@gmail.com");

    const {getByText, getByAltText, getByLabelText, getAllByText} = render(
        <Router>
            <BrowseProducts
                setErrorMessage={null}
                errorMessage={null}
                isLogged={() => true}
                loggedUser={() => "mario.rossi@gmail.com"}
            />
        </Router>
    );

    getByText("Products List");
    await waitFor(() => {
        getByText(/Apples/i);
        getByText(buttonText);
        getByAltText("fruit");
    });
    fireEvent.click(getByText(buttonText));
    await waitFor(() => {
        getByLabelText("Amount:");
    });
    fireEvent.change(getByLabelText("Amount:"), {target: {value: 1}});
    fireEvent.click(getAllByText(buttonText)[1]);
    await waitFor(() => {
        getByText("🛒 1 item(s)");
    });
    fireEvent.click(getByText("🛒 1 item(s)"));
    await waitFor(()=> {
        getByText("Your Cart");
        getByText("Apples");
        getAllByText(/1/i);
    });
    fireEvent.click(getByText("Close"));
});

test("Bad browse product", async () => {
    mockBrowseProducts.mockResolvedValueOnce(null);
    mockGetCart.mockResolvedValueOnce(null);
    localStorage.setItem("role", "CUSTOMER");
    localStorage.setItem("username", "mario.rossi@gmail.com");

    const {getByText, getByAltText, getByLabelText, getAllByText} = render(
        <Router>
            <BrowseProducts
                setErrorMessage={null}
                errorMessage={null}
                isLogged={() => true}
                loggedUser={() => "mario.rossi@gmail.com"}
            />
        </Router>
    );

    getByText("Products List");
    await waitFor(async () => {
        expect(global.window.location.pathname).toEqual('/ErrorHandler');
    });

})

test("Double digits decimals", async () => {
    mockBrowseProducts.mockResolvedValueOnce([
        {
            "productId": 5,
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
    ]);
    mockGetCart.mockResolvedValueOnce([]);
    mockGetCart.mockResolvedValueOnce([{
        "endAvailability": null,
        "farmer": 5,
        "imageUrl": "",
        "name": "Apples",
        "price": 2,
        "productId": 5,
        "quantityAvailable": 1,
        "quantityBaskets": 1,
        "quantityDelivered": 0,
        "quantityForecast": 0,
        "quantityOrdered": 25,
        "startAvailability": null,
        "totalQuantity": 50,
        "unitOfMeasurement": "Kg"
    }])
    mockAddToCard.mockResolvedValueOnce(true);
    localStorage.setItem("role", "CUSTOMER");
    localStorage.setItem("username", "mario.rossi@gmail.com");

    const {getByText, getByAltText, getByLabelText, getAllByText} = render(
        <Router>
            <BrowseProducts
                setErrorMessage={null}
                errorMessage={null}
                isLogged={() => true}
                loggedUser={() => "mario.rossi@gmail.com"}
            />
        </Router>
    );

    getByText("Products List");
    getByText("Loading...");
    waitFor(async () => {
        getByText("2.00");
        fireEvent.click(getByText(buttonText));
    });
    waitFor(async () => {
        getByText("2.00");
        const input = getByLabelText("Amount:");
        fireEvent.change(input, {target: {value: 1}});
        fireEvent.click(getAllByText(buttonText)[1]);
    });
    waitFor(async () => {
        getByText("Product added successfully");
        fireEvent.click(getByText("Close"));
    })

    waitFor(async () => {
        fireEvent.click(getByText("🛒 1 item(s)"));
        getByText("Your Cart");
        getByText("Apples");
        getByText("1");
        getByText("2.00");
        fireEvent.click(getByText("Close"));
    })
})