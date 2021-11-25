import * as React from "react";
import * as ReactDOM from "react-dom";
import {getQueriesForElement} from '@testing-library/dom';

import {Homepage} from "./Homepage";

const render = (comp) => {
    const root = document.createElement("div");
    ReactDOM.render(comp, root);
    return getQueriesForElement(root);
}

test ("renders correctly", () => {

    const {getByText, getByAltText} = render(<Homepage/>);

    getByAltText("farmer");
    getByAltText("italymap");
    getByText("Come and find us!")
    getByText("We are in:");
    getByText("Roma");
    getByText("Vicenza");
    getByText("Biella");
    getByText("Caserta");
    getByText("Torino");
    getByText("Catania");
});