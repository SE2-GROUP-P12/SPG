import * as React from "react";
import * as ReactDOM from "react-dom";
import {getQueriesForElement} from '@testing-library/dom';

import {Admin} from "./Admin";
import { BrowserRouter as Router} from "react-router-dom";

function render(comp) {
    const root = document.createElement("div");
    ReactDOM.render(comp, root);
    return getQueriesForElement(root);
}

test ("renders correctly", () => {

    localStorage.setItem("role", "ADMIN");
    const {getByText} = render(
        <Router>
            <Admin/>
        </Router>
    );
    getByText("Admin");
    getByText("ShopEmployee");
    getByText("Customer");
    getByText("Farmer");
});