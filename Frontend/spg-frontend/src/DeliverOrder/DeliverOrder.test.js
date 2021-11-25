import * as React from "react";
import * as ReactDOM from "react-dom";
import {getByLabelText, getQueriesForElement} from '@testing-library/dom';
import {render, fireEvent, waitFor } from '@testing-library/react';
import {DeliverOrder} from "./DeliverOrder";
import { BrowserRouter as Router} from "react-router-dom";
import {API} from "./../API/API";


test ("Renders Correctly", () => {

    const {getByText, getByAltText} = render(
        <Router>
            <DeliverOrder/>
        </Router>
    );

    getByText("Deliver Order");
    getByText("Email:");
    getByText("Submit customer");
    
});
/* 
const mockGetAllOrders = (API.getAllOrders = jest.fn());
const mockCustomerExistsByMail = (API.customerExistsByMail = jest.fn());
const mockGetOrdersByEmail = (API.getOrdersByEmail = jest.fn());
const mockDeliverOrder = (API.deliverOrder = jest.fn());

test ("Submit customer, customer found", async () => {
    
    mockGetAllOrders.mockResolvedValueOnce([
        {
            "orderId":60,
            "email":"mario.rossi@gmail.com",
            "productList": [
                {
                    "productId":"10",
                    "name":"Bananas",
                    "producer":"default producer",
                    "unit":"Kg",
                    "unit price":"6.25",
                    "amount":"5.0"
                }
            ]
        },
        {
            "orderId":50,
            "email":"paolo.bianchi@gmail.com",
            "productList": [
                {
                    "productId":"10",
                    "name":"Bananas",
                    "producer":"default producer",
                    "unit":"Kg",
                    "unit price":"6.25",
                    "amount":"3.0"
                }
            ]
        },
    ]);
    mockCustomerExistsByMail.mockResolvedValueOnce(true);
    mockGetOrdersByEmail.mockResolvedValueOnce([
        {
            "orderId":60,
            "email":"mario.rossi@gmail.com",
            "productList": [
                {
                    "productId":"10",
                    "name":"Bananas",
                    "producer":"default producer",
                    "unit":"Kg",
                    "unit price":"6.25",
                    "amount":"5.0"
                }
            ]
        }
    ]);
    mockDeliverOrder.mockResolvedValueOnce(true);
    
    const {getByText, getByLabelText, getAllByText} = render(
        <Router>
            <DeliverOrder/>
        </Router>
    );

    getByText("Deliver Order");
    getByText("Submit customer");
    getByText("Back");
    
    const input = getByLabelText("Email:"); 
    fireEvent.change(input, { target: { value: "mario.rossi@gmail.com" } });
    fireEvent.click(getByText("Submit customer"));
    

    await waitFor(async () => {
        getAllByText("Deliver");
    });

    expect(mockGetAllOrders).toBeCalledTimes(1);
    expect(mockGetAllOrders).toBeCalledWith();
    expect(mockCustomerExistsByMail).toBeCalledTimes(1);
    expect(mockCustomerExistsByMail).toBeCalledWith("mario.rossi@gmail.com");
    expect(mockGetOrdersByEmail).toBeCalledTimes(1);
    expect(mockGetOrdersByEmail).toBeCalledWith("mario.rossi@gmail.com");
    expect(mockDeliverOrder).toBeCalledTimes(1);
    expect(mockDeliverOrder).toBeCalledWith(60); 
}); 
 */