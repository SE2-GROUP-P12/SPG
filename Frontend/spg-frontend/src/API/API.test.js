import * as React from "react";
import * as ReactDOM from "react-dom";
import {API} from "./API";

global.fetch = jest.fn();

beforeEach(()=>fetch.mockClear() );

test("browseProducts ok", async() => {

    fetch.mockImplementationOnce(()=>
        Promise.resolve({
            ok: true,
            status: 200,
            json: () => [{name: 'Apples'}]
        })
    )
    const resp = await API.browseProducts((x)=>x);
    expect(resp).toStrictEqual(expect.objectContaining({"status": 200}));
    expect(resp).toStrictEqual(expect.objectContaining({"data": [{"name": "Apples"}]}));
})

test ("browseProducts catch", async() => {
    fetch.mockImplementationOnce(()=>
        Promise.reject()
    )
    const resp = await API.browseProducts((x)=>x);
    expect(resp).toBe(null);
})

test ("browseProducts err", async() => {
    const mockSetErrorMessage = jest.fn();
    fetch.mockImplementationOnce(()=>
        Promise.resolve({
            status: 500
        })
    )
    const resp = await API.browseProducts(mockSetErrorMessage);
    expect(resp).toBe(null);
    expect(mockSetErrorMessage).toBeCalledTimes(1);
})

test("placeOrder ok", async() => {

    fetch.mockImplementationOnce(()=>
        Promise.resolve({
            ok: true,
            status: 200
        })
    )
    const resp = await API.placeOrder({'email': 'mario.rossi@gmail.com', 'customer': 'mario.rossi@gmail.com'});
    expect(resp).toBe(true);
})

test ("placeOrder catch", async() => {
    fetch.mockImplementationOnce(()=>
        Promise.reject()
    )
    const resp = await API.placeOrder({'email': 'mario.rossi@gmail.com', 'customer': 'mario.rossi@gmail.com'});
    expect(resp).toBe(undefined);
})

test ("placeOrder err", async() => {
    fetch.mockImplementationOnce(()=>
        Promise.resolve({
            ok: false ,
            status: 500
        })
    )
    const resp = await API.placeOrder({'email': 'nobody@gmail.com', 'customer': 'nobody@gmail.com'});
    expect(resp).toBe(false);
})

test("addToCart ok", async() => {

    fetch.mockImplementationOnce(()=>
        Promise.resolve({
            ok: true,
            status: 200
        })
    )
    const resp = await API.addToCart();
    expect(resp).toBe(true);
})

test ("addToCart catch", async() => {
    fetch.mockImplementationOnce(()=>
        Promise.reject()
    )
    const resp = await API.addToCart();
    expect(resp).toBe(undefined);
})

test ("addToCart err", async() => {
    fetch.mockImplementationOnce(()=>
        Promise.resolve({
            ok: false ,
            status: 500
        })
    )
    const resp = await API.addToCart();
    expect(resp).toBe(false);
})

test("getWallet ok", async() => {

    fetch.mockImplementationOnce(()=>
        Promise.resolve({
            ok: true,
            status: 200,
            json: () => 10
        })
    )
    const resp = await API.getWallet('mario.rossi@gmail.com');
    expect(resp).toBe(10);
})

test ("getWallet catch", async() => {
    fetch.mockImplementationOnce(()=>
        Promise.reject()
    )
    const resp = await API.getWallet('mario.rossi@gmail.com');
    expect(resp).toBe(undefined);
})

test ("addToCart err", async() => {
    fetch.mockImplementationOnce(()=>
        Promise.resolve({
            ok: false ,
            status: 500
        })
    )
    const resp = await API.getWallet('mario.rossi@gmail.com');
    expect(resp).toBe(undefined);
})

test("topUp ok", async() => {

    fetch.mockImplementationOnce(()=>
        Promise.resolve({
            ok: true,
            status: 200,
        })
    )
    const resp = await API.topUp();
    expect(resp).toBe(true);
})

test ("topUp catch", async() => {
    fetch.mockImplementationOnce(()=>
        Promise.reject()
    )
    const resp = await API.topUp();
    expect(resp).toBe(undefined);
})

test ("topUp err", async() => {
    fetch.mockImplementationOnce(()=>
        Promise.resolve({
            ok: false ,
            status: 500
        })
    )
    const resp = await API.topUp();
    expect(resp).toBe(false);
})


test("getCart ok", async() => {
    fetch.mockImplementationOnce(()=>
        Promise.resolve({
            ok: true,
            status: 200,
            json: () => [{name: 'Apples'}]
        })
    )
    const resp = await API.getCart('some@mail.com', x=>x);
    expect(resp).toStrictEqual([{name: 'Apples'}]);
})

test ("getCart catch", async() => {
    fetch.mockImplementationOnce(()=>
        Promise.reject()
    )
    const resp = await API.getCart('some@mail.com', x=>x);
    expect(resp).toBe(null);
})

test ("getCart err404", async() => {
    fetch.mockImplementationOnce(()=>
        Promise.resolve({
            ok: false ,
            status: 404
        })
    )
    const resp = await API.getCart('some@mail.com', x=>x)
    expect(resp).toBe(undefined);
})

test ("getCart err400", async() => {
    fetch.mockImplementationOnce(()=>
        Promise.resolve({
            ok: false ,
            status: 400
        })
    )
    const resp = await API.getCart('some@mail.com', x=>x)
    expect(resp).toBe(null);
})

test("customerExists ok", async() => {

    fetch.mockImplementationOnce(()=>
        Promise.resolve({
            ok: true,
            status: 200,
            json: () => {return ({'exist':true});}
        })
    )
    const resp = await API.customerExists({'email':'mario.rossi@gmail.com','ssn':'idksomepersonaldata'});
    expect(resp).toBe(true);
})

test ("customerExists catch", async() => {
    fetch.mockImplementationOnce(()=>
        Promise.reject()
    )
    const resp = await API.customerExists({'email':'some@mail.com','ssn':'idksomepersonaldata'});
    expect(resp).toBe(undefined);
})

test ("customerExists err", async() => {

    fetch.mockImplementationOnce(()=>
        Promise.resolve({
            ok: false,
            status: 500,
            json: () => {return ({'exist':false});}
        })
    )
    const resp = await API.customerExists({'email':'some@mail.com','ssn':'idksomepersonaldata'});
    expect(resp).toBe(undefined);
})

test("customerExistsByMail ok", async() => {

    fetch.mockImplementationOnce(()=>
        Promise.resolve({
            ok: true,
            status: 200,
            json: () => true
        })
    )
    const resp = await API.customerExistsByMail('mario.rossi@gmail.com');
    expect(resp).toBe(true);
})

test ("customerExistsByMail catch", async() => {
    fetch.mockImplementationOnce(()=>
        Promise.reject()
    )
    const resp = await API.customerExistsByMail('some@mail.com');
    expect(resp).toBe(undefined);
})

test ("customerExistsByMail err", async() => {

    fetch.mockImplementationOnce(()=>
        Promise.resolve({
            ok: false,
            status: 500,
            json: () => false
        })
    )
    const resp = await API.customerExistsByMail('some@mail.com');
    expect(resp).toBe(undefined);
})

test("dropOrder ok", async() => {

    fetch.mockImplementationOnce(()=>
        Promise.resolve({
            ok: true,
            status: 200
        })
    )
    const resp = await API.dropOrder('mario.rossi@gmail.com');
    expect(resp).toBe(true);
})

test ("dropOrder catch", async() => {
    fetch.mockImplementationOnce(()=>
        Promise.reject()
    )
    const resp = await API.dropOrder();
    expect(resp).toBe(undefined);
})

test ("dropOrder err", async() => {

    fetch.mockImplementationOnce(()=>
        Promise.resolve({
            ok: false
        })
    )
    const resp = await API.dropOrder('mario.rossi@gmail.com');
    expect(resp).toBe(false);
})

test("getAllOrders ok", async() => {

    fetch.mockImplementationOnce(()=>
        Promise.resolve({
            ok: true,
            status: 200,
            json: () => [{name: 'Apples'}]
        })
    )
    const resp = await API.getAllOrders();
    expect(resp).toStrictEqual([{"name": "Apples"}]);
})

test("getAllOrders empty", async() => {

    fetch.mockImplementationOnce(()=>
        Promise.resolve({
            ok: true,
            status: 200,
            json: () => []
        })
    )
    const resp = await API.getAllOrders();
    expect(resp).toStrictEqual(expect.objectContaining([]));
})

test ("getAllOrders catch", async() => {
    fetch.mockImplementationOnce(()=>
        Promise.reject()
    )
    const resp = await API.getAllOrders();
    expect(resp).toBe(undefined);
})

test ("getAllOrders err", async() => {
    fetch.mockImplementationOnce(()=>
        Promise.resolve({
            ok: false,
            json: () => null
        })
    )
    const resp = await API.getAllOrders()
    expect(resp).toBe(null);
})

test("getOrdersByEmail ok", async() => {

    fetch.mockImplementationOnce(()=>
        Promise.resolve({
            ok: true,
            status: 200,
            json: () => [{name: 'Apples'}]
        })
    )
    const resp = await API.getOrdersByEmail('mario.rossi@gmail.com');
    expect(resp).toStrictEqual([{"name": "Apples"}]);
})

test("getAllOrders empty", async() => {

    fetch.mockImplementationOnce(()=>
        Promise.resolve({
            ok: true,
            status: 200,
            json: () => []
        })
    )
    const resp = await API.getOrdersByEmail('mario.rossi@gmail.com');
    expect(resp).toStrictEqual(expect.objectContaining([]));
})

test ("getAllOrders catch", async() => {
    fetch.mockImplementationOnce(()=>
        Promise.reject()
    )
    const resp = await API.getOrdersByEmail('mario.rossi@gmail.com');
    expect(resp).toBe(undefined);
})

test ("getAllOrders err", async() => {
    fetch.mockImplementationOnce(()=>
        Promise.resolve({
            ok: false,
            json: () => null
        })
    )
    const resp = await API.getOrdersByEmail('mario.rossi@gmail.com');
    expect(resp).toBe(null);
})

test("deliverOrder ok", async() => {
    fetch.mockImplementationOnce(()=>
        Promise.resolve({
            ok: true,
            status: 200,
            json: () => true
        })
    )
    const resp = await API.deliverOrder();
    expect(resp).toBe(true);
})

test ("deliverOrder catch", async() => {
    fetch.mockImplementationOnce(()=>
        Promise.reject()
    )
    const resp = await API.deliverOrder();
    expect(resp).toBe(undefined);
})

test ("deliverOrder err", async() => {
    fetch.mockImplementationOnce(()=>
        Promise.resolve({
            ok: false,
            status: 500,
            json: () => false
        })
    )
    const resp = await API.deliverOrder();
    expect(resp).toBe(false);
})

test("addCustomer ok", async() => {
    fetch.mockImplementationOnce(()=>
        Promise.resolve({
            ok: true,
            status: 201,
            json: () => true
        })
    )
    const resp = await API.addCustomer();
    expect(resp).toBe(true);
})

test ("addCustomer catch", async() => {
    fetch.mockImplementationOnce(()=>
        Promise.reject()
    )
    const resp = await API.addCustomer();
    expect(resp).toBe(undefined);
})

test ("addCustomer err", async() => {
    fetch.mockImplementationOnce(()=>
        Promise.resolve({
            ok: false,
            status: 500,
            json: () => false
        })
    )
    const resp = await API.addCustomer();
    expect(resp).toBe(undefined);
})

test("sessionReloader ok", async() => {
    fetch.mockImplementationOnce(()=>
        Promise.resolve({
            ok: true,
            status: 201,
            json: () => 'some token'
        })
    )
    const resp = await API.sessionReloader();
    expect(resp).toBe('some token');
})

test ("sessionReloader catch", async() => {
    fetch.mockImplementationOnce(()=>
        Promise.reject()
    )
    const resp = await API.sessionReloader();
    expect(resp).toBe(undefined);
})

test ("sessionReloader err", async() => {
    fetch.mockImplementationOnce(()=>
        Promise.resolve({
            ok: false,
            status: 500,
            json: () => 'bad things happened'
        })
    )
    const resp = await API.sessionReloader();
    expect(resp).toBe(null);
})

test("browseProductsByFarmer ok", async() => {

    fetch.mockImplementationOnce(()=>
        Promise.resolve({
            ok: true,
            status: 200,
            json: () => [{name: 'Apples'}]
        })
    )
    const resp = await API.browseProductsByFarmer({'email':'nothing@important.com'},(x)=>x);
    expect(resp).toStrictEqual(expect.objectContaining({"status": 200}));
    expect(resp).toStrictEqual(expect.objectContaining({"data": [{"name": "Apples"}]}));
})

test ("browseProductsByFarmer catch", async() => {
    fetch.mockImplementationOnce(()=>
        Promise.reject()
    )
    const resp = await API.browseProductsByFarmer({'email':'nothing@important.com'},(x)=>x);
    expect(resp).toBe(null);
})

test ("browseProductsByFarmer err", async() => {
    const mockSetErrorMessage = jest.fn();
    fetch.mockImplementationOnce(()=>
        Promise.resolve({
            status: 500
        })
    )
    const resp = await API.browseProductsByFarmer({'email':'nothing@important.com'},mockSetErrorMessage);
    expect(resp).toBe(null);
    expect(mockSetErrorMessage).toBeCalledTimes(1);
})


test("modifyForecast ok", async() => {
    fetch.mockImplementationOnce(()=>
        Promise.resolve({
            ok: true,
            status: 200,
            json: () => true
        })
    )
    const resp = await API.modifyForecast();
    expect(resp).toBe(true);
})

test ("modifyForecast catch", async() => {
    fetch.mockImplementationOnce(()=>
        Promise.reject()
    )
    const resp = await API.modifyForecast();
    expect(resp).toBe(undefined);
})


test("addProduct ok", async() => {
    fetch.mockImplementationOnce(()=>
        Promise.resolve({
            ok: true,
            status: 201,
            json: () => true
        })
    )
    const resp = await API.addProduct();
    expect(resp).toBe(true);
})

test ("addProduct catch", async() => {
    fetch.mockImplementationOnce(()=>
        Promise.reject()
    )
    const resp = await API.addProduct()
    expect(resp).toBe(undefined);
})

test ("addProduct err", async() => {
    fetch.mockImplementationOnce(()=>
        Promise.resolve({
            ok: false,
            status: 500,
            json: () => false
        })
    )
    const resp = await API.addProduct();
    expect(resp).toBe(false);
})

test("getWalletWarning ok", async() => {
    fetch.mockImplementationOnce(()=>
        Promise.resolve({
            ok: true,
            status: 201,
            json: () => 'warning'
        })
    )
    const resp = await API.getWalletWarning('mario.rossi@gmail.com');
    expect(resp).toBe('warning');
})

test ("getWalletWarning catch", async() => {
    fetch.mockImplementationOnce(()=>
        Promise.reject()
    )
    const resp = await API.getWalletWarning('mario.rossi@gmail.com');
    expect(resp).toBe(undefined);
})

test ("timeTravel ok", async() =>{
    fetch.mockImplementationOnce(()=>
        Promise.resolve({
            ok: true,
            status: 200
        })
    )
    const resp = await API.timeTravel();
    expect(resp).toBe(true);
})


test ("timeTravel fail", async() =>{
    fetch.mockImplementationOnce(()=>
        Promise.resolve({
            ok: false,
            status: 500
        })
    )
    const resp = await API.timeTravel();
    expect(resp).toBe(false);
})


test ("timeTravel catch", async() =>{
    fetch.mockImplementationOnce(()=>
        Promise.reject()
    )
    const resp = await API.timeTravel();
    expect(resp).toBe(undefined);
})