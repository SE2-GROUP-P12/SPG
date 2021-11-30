import * as React from "react";
import * as ReactDOM from "react-dom";
import {getQueriesForElement} from '@testing-library/dom';

import {Farmer} from "./Farmer";
import { BrowserRouter as Router} from "react-router-dom";

function render(comp) {
    const root = document.createElement("div");
    ReactDOM.render(comp, root);
    return getQueriesForElement(root);
}

test ("renders correctly", () => {

    localStorage.setItem("role", "FARMER");
    const {getByText} = render(
        <Router>
            <Farmer/>
        </Router>
    );

    getByText("Farmer");
    getByText("Communicate Products Prevision");
});