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

    mockBrowseProducts.mockResolvedValueOnce([
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
    ]);
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

    getByText("Products List");
    getByText("Loading...");
    getByText("🛒 0 item(s)");
    waitFor(() => {
        getByText("Apples");
        getByAltText("fruit");
        getByText(buttonText);
    })

    //expect(mockBrowseProducts).toBeCalledTimes(1);
    //expect(mockBrowseProducts).toBeCalledWith(null);
    //expect(mockGetCart).toBeCalledTimes(1);
    //expect(mockGetCart).toBeCalledWith({"email":"mario.rossi@gmail.com"}, null);
});

test("Add to cart", async () => {

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
            "price": 5,
            "imageUrl": ""
        }
    ]);
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
    getByText("Loading...");
    waitFor(async () => {
        getByText("Apples");
        getByText(buttonText);
        getByAltText("fruit");
        fireEvent.click(getByText(buttonText));
    });
    waitFor(async () => {
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
        fireEvent.click(getByText("Close"));
    })

    //expect(mockBrowseProducts).toBeCalledTimes(1);
    //expect(mockBrowseProducts).toBeCalledWith(null);
    //expect(mockAddToCard).toBeCalledTimes(1);
    //expect(mockAddToCard).toBeCalledWith({"productId":5 ,"email": 'mario.rossi@gmail.com',"quantity": 1});
    //expect(mockGetCart).toBeCalledTimes(2);
    //expect(mockGetCart).toBeCalledWith({"email":"mario.rossi@gmail.com"}, null);
});

test("Bad browse product", async () => {
    mockBrowseProducts.mockResolvedValueOnce(null);
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

test("Unregistered user Browse Products", async () => {
    mockBrowseProducts.mockResolvedValueOnce([
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
    ]);


    localStorage.setItem("role", "");
    localStorage.setItem("username", "");

    const {getByText} = render(
        <Router>
            <BrowseProducts
                setErrorMessage={null}
                errorMessage={null}
                isLogged={() => false}
                loggedUser={() => ""}
            />
        </Router>
    );

    waitFor( () => {
        getByText("Apples");
        getByAltText("fruit");
        expect(() => getByText("Add to cart")).toThrow()
    })
})