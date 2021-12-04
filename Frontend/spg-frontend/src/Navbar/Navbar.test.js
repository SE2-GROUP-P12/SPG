import * as React from "react";
import {render, fireEvent, waitFor, getByText } from '@testing-library/react';
import {BrowserRouter as Router} from "react-router-dom";

import {Navbar} from "./Navbar";
import {API} from "./../API/API";

const mockAlert = (API.getWalletWarning = jest.fn());

test("Alert Balance insufficient", async () => 
{
    localStorage.setItem("username", "mario.rossi@gmail.com");
    mockAlert.mockResolvedValueOnce({
        "exist": "true",
        "message": "Balance insufficient, remember to top up!"
    });

    const {getByText, getByAltText} = render(
        <Router>
            <Navbar isLoggedFlag = {true}
                    loggedUser={"mario.rossi@gmail.com"}
                    loggedUserRole={"CUSTOMER"}
            />
        </Router>
        );

    await waitFor(()=>{
        getByAltText("warning");
    })

    expect(mockAlert).toBeCalledTimes(1);
    expect(mockAlert).toBeCalledWith("mario.rossi@gmail.com");
})