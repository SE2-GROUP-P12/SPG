import * as React from "react";
import * as ReactDOM from "react-dom";
import {getQueriesForElement} from '@testing-library/dom';

import {Customer} from "./Customer";
import { BrowserRouter as Router} from "react-router-dom";

function render(comp) {
    const root = document.createElement("div");
    ReactDOM.render(comp, root);
    return getQueriesForElement(root);
}

test ("renders correctly", () => {

    localStorage.setItem("role", "CUSTOMER");
    const {getByText, getByAltText} = render(
        <Router>
            <Customer/>
        </Router>
    );

    getByText("Customer");
    getByText("Browse Products");
    getByText("Place Order");
});