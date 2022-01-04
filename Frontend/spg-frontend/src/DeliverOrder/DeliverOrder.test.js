import * as React from "react";
import {render, fireEvent, waitFor } from '@testing-library/react';
import {DeliverOrder} from "./DeliverOrder";
import { BrowserRouter as Router} from "react-router-dom";
import {API} from "./../API/API";
import {MailServerAPI} from "../MailServerAPI/MailServerAPI";


test ("Renders Correctly", () => {

    const {getByText, getByAltText} = render(
        <Router>
            <DeliverOrder/>
        </Router>
    );

    getByText("Deliver Order");
    getByText("Email:");
    getByText("Submit customer");
    getByText("Send all pending orders reminder mail");
    getByText("Send All Mail");

});

test("Open Order - mail ok", async() => {
    const mockGetAllOrders = (API.getAllOrders = jest.fn());
    const mockSolicitTopUp = (MailServerAPI.solicitCustomerTopUp = jest.fn());
    mockGetAllOrders.mockResolvedValueOnce(
        [{creationDate: "2021-12-09T16:08:49.113832",
            currentStatusDate: "2021-12-09T16:08:49.113832",
            email: "mario.rossi@gmail.com",
            orderId: 51,
            productList: [],
            status: "OPEN",
            value: 100}]);
    mockSolicitTopUp.mockResolvedValueOnce(true);

    const {getByText} = render(
        <Router>
            <DeliverOrder/>
        </Router>
    );

    await waitFor(()=>{
        getByText("Cancel Order");
    });
    fireEvent.click(getByText("Manage Order"));
    await waitFor(()=>{
        getByText("Handle unpaid order");
        getByText("Close");
    })
    fireEvent.click(getByText("Solicit!"))
    await waitFor(()=>{
        getByText("Mail sent correctly");
    });
})

test("Open Order - mail fail", async() => {
    const mockGetAllOrders = (API.getAllOrders = jest.fn());
    const mockSolicitTopUp = (MailServerAPI.solicitCustomerTopUp = jest.fn());
    mockGetAllOrders.mockResolvedValueOnce(
        [{creationDate: "2021-12-09T16:08:49.113832",
            currentStatusDate: "2021-12-09T16:08:49.113832",
            email: "mario.rossi@gmail.com",
            orderId: 51,
            productList: [],
            status: "OPEN",
            value: 100}]);
    mockSolicitTopUp.mockResolvedValueOnce(false);

    const {getByText} = render(
        <Router>
            <DeliverOrder/>
        </Router>
    );

    await waitFor(()=>{
        getByText("Cancel Order");
    });
    fireEvent.click(getByText("Manage Order"));
    await waitFor(()=>{
        getByText("Handle unpaid order");
        getByText("Close");
    })
    fireEvent.click(getByText("Solicit!"))
    await waitFor(()=>{
        getByText("Can not send the mail");
    });
})

test ("Confirmed order", async()=>{
    const mockGetAllOrders = (API.getAllOrders = jest.fn());
    const mockDeliverOrder = (API.deliverOrder = jest.fn());
    mockGetAllOrders.mockResolvedValueOnce(
        [{creationDate: "2021-12-09T16:08:49.113832",
            currentStatusDate: "2021-12-09T16:08:49.113832",
            email: "mario.rossi@gmail.com",
            orderId: 51,
            productList: [],
            status: "CONFIRMED",
            value: 100}]);
    mockDeliverOrder.mockResolvedValueOnce(true);

    const {getByText} = render(
        <Router>
            <DeliverOrder
                date="Sat"
                time="10:00"/>
        </Router>
    );

    await waitFor(()=>{
        getByText(/mario.rossi@gmail.com/);
    })
    fireEvent.click(getByText("Deliver"));
    //we can't test any further for now because we decide what we return and we can't show that the the order is still there/has been cancelled
})


test("all mails ok", async()=>{
    const mockGetPendingOrdersMails = (MailServerAPI.getPendingOrdersMail = jest.fn());
    const mockSolicitTopUp = (MailServerAPI.solicitCustomerTopUp = jest.fn());
    const mockGetAllOrders = (API.getAllOrders = jest.fn());
    mockGetAllOrders.mockResolvedValueOnce([]);
    mockGetPendingOrdersMails.mockResolvedValueOnce({"paolo.bianchi@gmail.com": [71], "mario.rossi@gmail.com": [51, 69]});
    mockSolicitTopUp.mockResolvedValueOnce(true);

    const {getByText, getAllByText} = render(
        <Router>
            <DeliverOrder/>
        </Router>
    );

    fireEvent.click(getByText("Send All Mail"));
    await waitFor(()=>{
        getByText("paolo.bianchi@gmail.com");
        getByText("mario.rossi@gmail.com");
    })
    fireEvent.click(getAllByText("Send All Mail")[1])
    await waitFor(()=>{
        getByText("All mail was sent");
    })
})

test("all mails fail", async()=>{
    const mockGetPendingOrdersMails = (MailServerAPI.getPendingOrdersMail = jest.fn());
    const mockSolicitTopUp = (MailServerAPI.solicitCustomerTopUp = jest.fn());
    const mockGetAllOrders = (API.getAllOrders = jest.fn());
    mockGetAllOrders.mockResolvedValueOnce([]);
    mockGetPendingOrdersMails.mockResolvedValueOnce({"paolo.bianchi@gmail.com": [71], "mario.rossi@gmail.com": [51, 69]});
    mockSolicitTopUp.mockResolvedValueOnce(false);

    const {getByText, getAllByText} = render(
        <Router>
            <DeliverOrder/>
        </Router>
    );

    fireEvent.click(getByText("Send All Mail"));
    await waitFor(()=>{
        getByText("paolo.bianchi@gmail.com");
        getByText("mario.rossi@gmail.com");
    })
    fireEvent.click(getAllByText("Send All Mail")[1])
    await waitFor(()=>{
        getByText("Send All Mail");
    })
})

test ("double decimal digits", async() => {

    const mockGetOrders = (API.getAllOrders = jest.fn());
    mockGetOrders.mockResolvedValueOnce([
        {   "orderId":68,
            "email":"mario.rossi@gmail.com",
            "value":225.0,
            "productList":
                [
                    {"productId":"9","name":"Eggs","producer":"Thomas Jefferson","unit":"Units","unit price":"6.25","amount":"36.0"}
                ]
        }]);
    localStorage.setItem("role", "EMPLOYEE");
    const {getByText} = render(
        <Router>
            <DeliverOrder/>
        </Router>
    );

    getByText("Deliver Order");
    getByText("Email:");
    getByText("Submit customer");
    waitFor(()=>{
        getByText(/225.00€/);
    })
});