import * as React from "react";
import * as ReactDOM from "react-dom";
import {getQueriesForElement} from '@testing-library/dom';

import {ShopEmployee} from "./ShopEmployee";
import { BrowserRouter as Router} from "react-router-dom";

function render(comp) {
    const root = document.createElement("div");
    ReactDOM.render(comp, root);
    return getQueriesForElement(root);
}

test ("renders correctly", () => {

    localStorage.setItem("role", "EMPLOYEE");
    const {getByText, getByAltText} = render(
        <Router>
            <ShopEmployee/>
        </Router>
    );

    getByText("Shop Employee");
    getByText("New Customer");
    getByText("Top Up");
    getByText("Place Order");
    getByText("Deliver Order");
    

});