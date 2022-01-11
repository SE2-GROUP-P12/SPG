import * as React from "react";
import {render, fireEvent, waitFor} from '@testing-library/react';

import {BrowserRouter as Router} from "react-router-dom";
import {UnauthorizedComponent} from "./UnauthorizedComponent";

test("500 renders correctly", async() => {

    const {getByText, getByAltText} = render(
        <Router>
            <UnauthorizedComponent
            errorMessage={{ status: 500,
                            errorMessage: 'Something',
                            mitigation: 'Some action'}}/>
        </Router>
    );

     getByText("SERVER IS OFFLINE");
     getByAltText("err");
     getByText("MORE INFO");
     fireEvent.click(getByText("MORE INFO"));
     await waitFor(()=>{
         getByText(/STATUS:/i);
         getByText(/500/i);
         getByText(/ERROR MESSAGE:/i);
         getByText(/Something/i);
         getByText(/POSSIBLE MITIGATION:/i);
         getByText(/Some action/i);
         getByText("hide review");
     })
    fireEvent.click(getByText("hide review"));
    await waitFor(()=>{
        expect(()=>getByText(/STATUS:/i)).toThrow();
        expect(()=>getByText(/ERROR MESSAGE:/i)).toThrow();
        expect(()=>getByText(/POSSIBLE MITIGATION:/i)).toThrow();
    })
});


