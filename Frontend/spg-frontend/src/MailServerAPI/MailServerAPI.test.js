import * as React from "react";
import * as ReactDOM from "react-dom";
import {MailServerAPI} from "./MailServerAPI";

global.fetch = jest.fn();

beforeEach(()=>fetch.mockClear() );

test("solicitCustomerTopUp ok", async() => {

    fetch.mockImplementationOnce(()=>
        Promise.resolve({
            ok: true,
            status: 200,
        })
    )
    const resp = await MailServerAPI.solicitCustomerTopUp('a@gmail.com', 'b@gmail.com');
    expect(resp).toBe(true);
})

test("solicitCustomerTopUp fail", async() => {

    fetch.mockImplementationOnce(()=>
        Promise.resolve({
            ok: false,
            status: 500,
        })
    )
    const resp = await MailServerAPI.solicitCustomerTopUp('a@gmail.com', 'b@gmail.com');
    expect(resp).toBe(false);
})

test("solicitCustomerTopUp ok", async() => {

    fetch.mockImplementationOnce(()=>
        Promise.reject()
    )
    const resp = await MailServerAPI.solicitCustomerTopUp('a@gmail.com', 'b@gmail.com');
    expect(resp).toBe(false);
})

test("getPendingOrdersMail ok", async() => {

    fetch.mockImplementationOnce(()=>
        Promise.resolve({
            ok: true,
            status: 200,
            json: () => {return {"paolo.bianchi@gmail.com": [71], "mario.rossi@gmail.com": [51, 69]}}
        })
    )
    const resp = await MailServerAPI.getPendingOrdersMail();
    expect(resp).toStrictEqual({"paolo.bianchi@gmail.com": [71], "mario.rossi@gmail.com": [51, 69]});
})

test("getPendingOrdersMail fail", async() => {

    fetch.mockImplementationOnce(()=>
        Promise.resolve({
            ok: false,
            status: 500,
        })
    )
    const resp = await MailServerAPI.getPendingOrdersMail();
    expect(resp).toBe(undefined);
})

test("solicitCustomerTopUp ok", async() => {

    fetch.mockImplementationOnce(()=>
        Promise.reject()
    )
    const resp = await MailServerAPI.getPendingOrdersMail();
    expect(resp).toBe(undefined);
})