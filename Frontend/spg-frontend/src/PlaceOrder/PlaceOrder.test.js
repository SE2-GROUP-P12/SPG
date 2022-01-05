import * as React from "react";
import {render, fireEvent, waitFor, getByTestId, getAllByText} from '@testing-library/react';
import {BrowserRouter as Router} from "react-router-dom";

import {PlaceOrder} from "./PlaceOrder";
import {API} from "./../API/API";

const mockGetCart = (API.getCart = jest.fn());

test("double digits decimals", async() =>{
    localStorage.setItem("role", "CUSTOMER");
    localStorage.setItem("username", "mario.rossi@gmail.com");
    mockGetCart.mockResolvedValueOnce(
        [
            {   "productId":7,
                "name":"Apples",
                "unitOfMeasurement":"Kg",
                "totalQuantity":50.0,
                "quantityAvailable":2.0,
                "price":2.5,
            }]);
    const {getByText} = render(
        <Router>
            <PlaceOrder time={"10:00"}
                        date={"Sat"}
                        loggedUser={"mario.rossi@gmail.com"}
                        loggedUserRole={"CUSTOMER"}
            />
        </Router>
    );

    getByText("Place Order");
    waitFor(()=>{
        getByText("5.00");
    })
});

test("Schedule pickup - ok", async()=>{
    localStorage.setItem("role", "CUSTOMER");
    localStorage.setItem("username", "mario.rossi@gmail.com");
    mockGetCart.mockResolvedValueOnce(
        [
            {   "productId":7,
                "name":"Apples",
                "unitOfMeasurement":"Kg",
                "totalQuantity":50.0,
                "quantityAvailable":2.0,
                "price":2.5,
            }]);
    const mockPlaceOrder = (API.placeOrder=jest.fn());
    mockPlaceOrder.mockResolvedValueOnce(true);
    const {getByText, getByLabelText} = render(
        <Router>
            <PlaceOrder time={"10:00"}
                        date={"Sat"}
                        loggedUser={"mario.rossi@gmail.com"}
                        loggedUserRole={"CUSTOMER"}
            />
        </Router>
    );
    fireEvent.click(getByText("Send order"));
    waitFor(()=>{
        getByText("NOTICE: Delivery and Pick up are available Wednedasy, Thursday and Friday from 9:00 to 18:00.")
        fireEvent.change(getByLabelText("date"), {target:{value:"12/22/2021"}});
        fireEvent.change(getByLabelText("time"), {target:{value:"10:00"}});
        fireEvent.click(getByText("Place Order"));
        getByText("Order Completed");
        getByText("Order placed correctly!");

    })
})

test("Schedule pickup - fail", async()=>{
    localStorage.setItem("role", "CUSTOMER");
    localStorage.setItem("username", "mario.rossi@gmail.com");
    mockGetCart.mockResolvedValueOnce(
        [
            {   "productId":7,
                "name":"Apples",
                "unitOfMeasurement":"Kg",
                "totalQuantity":50.0,
                "quantityAvailable":2.0,
                "price":2.5,
            }]);
    const mockPlaceOrder = (API.placeOrder=jest.fn());
    mockPlaceOrder.mockResolvedValueOnce(false);
    const {getByText, getByLabelText} = render(
        <Router>
            <PlaceOrder time={"10:00"}
                        date={"Sat"}
                        loggedUser={"mario.rossi@gmail.com"}
                        loggedUserRole={"CUSTOMER"}
            />
        </Router>
    );
    fireEvent.click(getByText("Send order"));
    waitFor(()=>{
        getByText("NOTICE: Delivery and Pick up are available Wednedasy, Thursday and Friday from 9:00 to 18:00.")
        fireEvent.change(getByLabelText("date"), {target:{value:"12/22/2021"}});
        fireEvent.change(getByLabelText("time"), {target:{value:"10:00"}});
        fireEvent.click(getByText("Place Order"));
        getByText("Order Completed");
        getByText("Error in placed order, retry!");

    })
})

test("Home delivery - ok", async()=>{
    localStorage.setItem("role", "CUSTOMER");
    localStorage.setItem("username", "mario.rossi@gmail.com");
    mockGetCart.mockResolvedValueOnce(
        [
            {   "productId":7,
                "name":"Apples",
                "unitOfMeasurement":"Kg",
                "totalQuantity":50.0,
                "quantityAvailable":2.0,
                "price":2.5,
            }]);
    const mockPlaceOrder = (API.placeOrder=jest.fn());
    mockPlaceOrder.mockResolvedValueOnce(true);
    const {getByText, getByLabelText} = render(
        <Router>
            <PlaceOrder time={"10:00"}
                        date={"Sat"}
                        loggedUser={"mario.rossi@gmail.com"}
                        loggedUserRole={"CUSTOMER"}
            />
        </Router>
    );
    fireEvent.click(getByText("Send order"));
    waitFor(()=>{
        getByText("NOTICE: Delivery and Pick up are available Wednedasy, Thursday and Friday from 9:00 to 18:00.")
        fireEvent.change(getByLabelText("date"), {target:{value:"12/22/2021"}});
        fireEvent.change(getByLabelText("time"), {target:{value:"10:00"}});
        fireEvent.click(getByLabelText("Home delivery"));
        fireEvent.change(getByLabelText("address"), {target:{value:"Via Roma 10, Torino"}});
        getByText("Order Completed");
        getByText("Order placed correctly!");

    })
})

test("Home delivery - fail", async()=>{
    localStorage.setItem("role", "CUSTOMER");
    localStorage.setItem("username", "mario.rossi@gmail.com");
    mockGetCart.mockResolvedValueOnce(
        [
            {   "productId":7,
                "name":"Apples",
                "unitOfMeasurement":"Kg",
                "totalQuantity":50.0,
                "quantityAvailable":2.0,
                "price":2.5,
            }]);
    const mockPlaceOrder = (API.placeOrder=jest.fn());
    mockPlaceOrder.mockResolvedValueOnce(false);
    const {getByText, getByLabelText} = render(
        <Router>
            <PlaceOrder time={"10:00"}
                        date={"Sat"}
                        loggedUser={"mario.rossi@gmail.com"}
                        loggedUserRole={"CUSTOMER"}
            />
        </Router>
    );
    fireEvent.click(getByText("Send order"));
    waitFor(()=>{
        getByText("NOTICE: Delivery and Pick up are available Wednedasy, Thursday and Friday from 9:00 to 18:00.")
        fireEvent.change(getByLabelText("date"), {target:{value:"12/22/2021"}});
        fireEvent.change(getByLabelText("time"), {target:{value:"10:00"}});
        fireEvent.click(getByLabelText("Home delivery"));
        fireEvent.change(getByLabelText("address"), {target:{value:"Via Roma 10, Torino"}});
        getByText("Order Completed");
        getByText("Error in placed order, retry!");
    })
})

test("remove SKIP FOR NOW", async() => {
    localStorage.setItem("role", "CUSTOMER");
    localStorage.setItem("username", "mario.rossi@gmail.com");
    mockGetCart.mockResolvedValueOnce(
        [
            {   "productId":7,
                "name":"Apples",
                "unitOfMeasurement":"Kg",
                "totalQuantity":50.0,
                "quantityAvailable":2.0,
                "price":2.5,
            }]);
    const {getByText, getByLabelText} = render(
        <Router>
            <PlaceOrder time={"10:00"}
                        date={"Sat"}
                        loggedUser={"mario.rossi@gmail.com"}
                        loggedUserRole={"CUSTOMER"}
            />
        </Router>
    );
    fireEvent.click(getByText("Send order"));
    waitFor(()=>{
        expect(getByLabelText("SKIP FOR NOW")).toThrow();
    })
})

test("getCart fail", async() =>{
    localStorage.setItem("role", "CUSTOMER");
    localStorage.setItem("username", "mario.rossi@gmail.com");
    mockGetCart.mockResolvedValueOnce(undefined);
    const {getByText} = render(
        <Router>
            <PlaceOrder time={"10:00"}
                        date={"Sat"}
                        loggedUser={"mario.rossi@gmail.com"}
                        loggedUserRole={"CUSTOMER"}
            />
        </Router>
    );

    getByText("Place Order");
    waitFor(()=>{
        getByText("Something went wrong, couldn't retrieve order");
    })
});

test ("getCart null", async()=>{
    localStorage.setItem("role", "CUSTOMER");
    localStorage.setItem("username", "mario.rossi@gmail.com");
    mockGetCart.mockResolvedValueOnce(null);
    const {getByText} = render(
        <Router>
            <PlaceOrder time={"10:00"}
                        date={"Sat"}
                        loggedUser={"mario.rossi@gmail.com"}
                        loggedUserRole={"CUSTOMER"}
            />
        </Router>
    );

    getByText("Place Order");
    await waitFor(()=>{
        expect(global.window.location.pathname).toEqual('/ErrorHandler');
    });
})

test("Drop Order - ok", async()=>{
    localStorage.setItem("role", "CUSTOMER");
    localStorage.setItem("username", "mario.rossi@gmail.com");
    mockGetCart.mockResolvedValueOnce(
        [
            {   "productId":7,
                "name":"Apples",
                "unitOfMeasurement":"Kg",
                "totalQuantity":50.0,
                "quantityAvailable":2.0,
                "price":2.5,
            }]);
    const mockDropOrder = (API.dropOrder=jest.fn());
    mockDropOrder.mockResolvedValueOnce(true);
    const {getByText} = render(
        <Router>
            <PlaceOrder time={"10:00"}
                        date={"Sat"}
                        loggedUser={"mario.rossi@gmail.com"}
                        loggedUserRole={"CUSTOMER"}
            />
        </Router>
    );
    await waitFor(()=>{
        getByText(/Apples/i);
    })
    fireEvent.click(getByText("Delete order"));
    await waitFor(()=>{
        expect(()=>getByText(/Apples/i)).toThrow();
    })
})

test("Drop Order - fail", async()=>{
    localStorage.setItem("role", "CUSTOMER");
    localStorage.setItem("username", "mario.rossi@gmail.com");
    mockGetCart.mockResolvedValueOnce(
        [
            {   "productId":7,
                "name":"Apples",
                "unitOfMeasurement":"Kg",
                "totalQuantity":50.0,
                "quantityAvailable":2.0,
                "price":2.5,
            }]);
    const mockDropOrder = (API.dropOrder=jest.fn());
    mockDropOrder.mockResolvedValueOnce(false);
    const {getByText} = render(
        <Router>
            <PlaceOrder time={"10:00"}
                        date={"Sat"}
                        loggedUser={"mario.rossi@gmail.com"}
                        loggedUserRole={"CUSTOMER"}
            />
        </Router>
    );
    await waitFor(()=>{
        getByText(/Apples/i);
    })
    fireEvent.click(getByText("Delete order"));
    await waitFor(()=>{
        expect(()=>getByText(/Apples/i)).not.toThrow();
    })
})

test("Place order employee - home- ok", async()=>{
    localStorage.setItem("role", "EMPLOYEE");
    localStorage.setItem("username", "francesco.conte@gmail.com");
    mockGetCart.mockResolvedValueOnce(
        [
            {   "productId":7,
                "name":"Apples",
                "unitOfMeasurement":"Kg",
                "totalQuantity":50.0,
                "quantityAvailable":2.0,
                "price":2.5,
            }]);
    const mockPlaceOrder = (API.placeOrder=jest.fn());
    mockPlaceOrder.mockResolvedValueOnce(true);
    const mockCustomerExistsByMail = (API.customerExistsByMail = jest.fn());
    mockCustomerExistsByMail.mockResolvedValueOnce(true);
    const {getByText, getByTestId} = render(
        <Router>
            <PlaceOrder time={"10:00"}
                        date={"Sat"}
                        loggedUser={"francesco.conte@gmail.com"}
                        loggedUserRole={"EMPLOYEE"}
                        setTopUpWarning = {x=>x}
            />
        </Router>
    );
    fireEvent.change(getByTestId("email"), {target:{value:"mario.rossi@gmail.com"}})
    fireEvent.click(getByText("Submit customer"));
    await waitFor(()=>{
        getByText("User found, you can now place their order");
    })
    fireEvent.click(getByText("Send order"));
    await waitFor(()=>{
        getByText("Review and confirm order");
        getByTestId("date");
        getByTestId("time");
        getByTestId("Home delivery");
        getByTestId("address");
        getByTestId("placeorder");
    });
    fireEvent.change(getByTestId("date"), {target:{value:'2022-02-23'}});
    fireEvent.change(getByTestId("time"), {target:{value:"10:00"}});
    fireEvent.click(getByTestId("Home delivery"));
    fireEvent.change(getByTestId("address"), {target:{value:"Via Roma 10, Torino"}});
    fireEvent.click(getByTestId("placeorder"));
    expect(mockPlaceOrder).toBeCalledTimes(1);
    await waitFor(()=>{
        getByText("Order Completed");
        getByText("Order sent successfully");
        expect(()=>getByText(/Apples/i)).toThrow();
    })
})

test("Place order employee - home- fail", async()=>{
    localStorage.setItem("role", "EMPLOYEE");
    localStorage.setItem("username", "francesco.conte@gmail.com");
    mockGetCart.mockResolvedValueOnce(
        [
            {   "productId":7,
                "name":"Apples",
                "unitOfMeasurement":"Kg",
                "totalQuantity":50.0,
                "quantityAvailable":2.0,
                "price":2.5,
            }]);
    const mockPlaceOrder = (API.placeOrder=jest.fn());
    mockPlaceOrder.mockResolvedValueOnce(false);
    const mockCustomerExistsByMail = (API.customerExistsByMail = jest.fn());
    mockCustomerExistsByMail.mockResolvedValueOnce(true);
    const {getByText, getByTestId} = render(
        <Router>
            <PlaceOrder time={"10:00"}
                        date={"Sat"}
                        loggedUser={"francesco.conte@gmail.com"}
                        loggedUserRole={"EMPLOYEE"}
                        setTopUpWarning = {x=>x}
            />
        </Router>
    );
    fireEvent.change(getByTestId("email"), {target:{value:"mario.rossi@gmail.com"}})
    fireEvent.click(getByText("Submit customer"));
    await waitFor(()=>{
        getByText("User found, you can now place their order");
    })
    fireEvent.click(getByText("Send order"));
    await waitFor(()=>{
        getByText("Review and confirm order");
        getByTestId("date");
        getByTestId("time");
        getByTestId("Home delivery");
        getByTestId("address");
        getByTestId("placeorder");
    });
    fireEvent.change(getByTestId("date"), {target:{value:'2022-02-23'}});
    fireEvent.change(getByTestId("time"), {target:{value:"10:00"}});
    fireEvent.click(getByTestId("Home delivery"));
    fireEvent.change(getByTestId("address"), {target:{value:"Via Roma 10, Torino"}});
    fireEvent.click(getByTestId("placeorder"));
    expect(mockPlaceOrder).toBeCalledTimes(1);
    await waitFor(()=>{
        getByText("Order Completed");
        getByText(/Error in placed order, please retry!/i);
        expect(()=>getByText(/Apples/i)).not.toThrow();
    })
})

test("Place order employee - pickup - ok", async()=>{
    localStorage.setItem("role", "EMPLOYEE");
    localStorage.setItem("username", "francesco.conte@gmail.com");
    mockGetCart.mockResolvedValueOnce(
        [
            {   "productId":7,
                "name":"Apples",
                "unitOfMeasurement":"Kg",
                "totalQuantity":50.0,
                "quantityAvailable":2.0,
                "price":2.5,
            }]);
    const mockPlaceOrder = (API.placeOrder=jest.fn());
    mockPlaceOrder.mockResolvedValueOnce(true);
    const mockCustomerExistsByMail = (API.customerExistsByMail = jest.fn());
    mockCustomerExistsByMail.mockResolvedValueOnce(true);
    const {getByText, getByTestId} = render(
        <Router>
            <PlaceOrder time={"10:00"}
                        date={"Sat"}
                        loggedUser={"francesco.conte@gmail.com"}
                        loggedUserRole={"EMPLOYEE"}
                        setTopUpWarning = {x=>x}
            />
        </Router>
    );
    fireEvent.change(getByTestId("email"), {target:{value:"mario.rossi@gmail.com"}})
    fireEvent.click(getByText("Submit customer"));
    await waitFor(()=>{
        getByText("User found, you can now place their order");
    })
    fireEvent.click(getByText("Send order"));
    await waitFor(()=>{
        getByText("Review and confirm order");
        getByTestId("date");
        getByTestId("time");
        getByTestId("Pick up");
        getByTestId("placeorder");
    });
    fireEvent.change(getByTestId("date"), {target:{value:'2022-02-23'}});
    fireEvent.change(getByTestId("time"), {target:{value:"10:00"}});
    fireEvent.click(getByTestId("Pick up"));
    fireEvent.click(getByTestId("placeorder"));
    expect(mockPlaceOrder).toBeCalledTimes(1);
    await waitFor(()=>{
        getByText("Order Completed");
        getByText(/Congratulations, order placed correctly!/i);
        expect(()=>getByText(/Apples/i)).toThrow();
    })
})

test("Place order employee - pickup - fail", async()=>{
    localStorage.setItem("role", "EMPLOYEE");
    localStorage.setItem("username", "francesco.conte@gmail.com");
    mockGetCart.mockResolvedValueOnce(
        [
            {   "productId":7,
                "name":"Apples",
                "unitOfMeasurement":"Kg",
                "totalQuantity":50.0,
                "quantityAvailable":2.0,
                "price":2.5,
            }]);
    const mockPlaceOrder = (API.placeOrder=jest.fn());
    mockPlaceOrder.mockResolvedValueOnce(false);
    const mockCustomerExistsByMail = (API.customerExistsByMail = jest.fn());
    mockCustomerExistsByMail.mockResolvedValueOnce(true);
    const {getByText, getByTestId} = render(
        <Router>
            <PlaceOrder time={"10:00"}
                        date={"Sat"}
                        loggedUser={"francesco.conte@gmail.com"}
                        loggedUserRole={"EMPLOYEE"}
                        setTopUpWarning = {x=>x}
            />
        </Router>
    );
    fireEvent.change(getByTestId("email"), {target:{value:"mario.rossi@gmail.com"}})
    fireEvent.click(getByText("Submit customer"));
    await waitFor(()=>{
        getByText("User found, you can now place their order");
    })
    fireEvent.click(getByText("Send order"));
    await waitFor(()=>{
        getByText("Review and confirm order");
        getByTestId("date");
        getByTestId("time");
        getByTestId("Pick up");
        getByTestId("placeorder");
    });
    fireEvent.change(getByTestId("date"), {target:{value:'2022-02-23'}});
    fireEvent.change(getByTestId("time"), {target:{value:"10:00"}});
    fireEvent.click(getByTestId("Pick up"));
    fireEvent.click(getByTestId("placeorder"));
    expect(mockPlaceOrder).toBeCalledTimes(1);
    await waitFor(()=>{
        getByText("Order Completed");
        getByText(/Error in placed order, please retry!/i);
        expect(()=>getByText(/Apples/i)).not.toThrow();
    })
})

test("Place order customer - home- ok", async()=>{
    localStorage.setItem("role", "CUSTOMER");
    localStorage.setItem("username", "mario.rossi@gmail.com");
    mockGetCart.mockResolvedValueOnce(
        [
            {   "productId":7,
                "name":"Apples",
                "unitOfMeasurement":"Kg",
                "totalQuantity":50.0,
                "quantityAvailable":2.0,
                "price":2.5,
            }]);
    const mockPlaceOrder = (API.placeOrder=jest.fn());
    mockPlaceOrder.mockResolvedValueOnce(true);
    const {getByText, getByTestId} = render(
        <Router>
            <PlaceOrder time={"10:00"}
                        date={"Sat"}
                        loggedUser={"mario.rossi@gmail.com"}
                        loggedUserRole={"CUSTOMER"}
                        setTopUpWarning = {x=>x}
            />
        </Router>
    );
    await waitFor(()=>{
        getByText("Send order");
    })
    fireEvent.click(getByText("Send order"));
    await waitFor(()=>{
        getByText("Review and confirm order");
        getByTestId("date");
        getByTestId("time");
        getByTestId("Home delivery");
        getByTestId("address");
        getByTestId("placeorder");
    });
    fireEvent.change(getByTestId("date"), {target:{value:'2022-02-23'}});
    fireEvent.change(getByTestId("time"), {target:{value:"10:00"}});
    fireEvent.click(getByTestId("Home delivery"));
    fireEvent.change(getByTestId("address"), {target:{value:"Via Roma 10, Torino"}});
    fireEvent.click(getByTestId("placeorder"));
    expect(mockPlaceOrder).toBeCalledTimes(1);
    await waitFor(()=>{
        getByText("Order Completed");
        getByText("Congratulations, order placed correctly!");
        expect(()=>getByText(/Apples/i)).toThrow();
    })
})

test("Place order customer - home- fail", async()=>{
    localStorage.setItem("role", "CUSTOMER");
    localStorage.setItem("username", "mario.rossi@gmail.com");
    mockGetCart.mockResolvedValueOnce(
        [
            {   "productId":7,
                "name":"Apples",
                "unitOfMeasurement":"Kg",
                "totalQuantity":50.0,
                "quantityAvailable":2.0,
                "price":2.5,
            }]);
    const mockPlaceOrder = (API.placeOrder=jest.fn());
    mockPlaceOrder.mockResolvedValueOnce(false);
    const {getByText, getByTestId} = render(
        <Router>
            <PlaceOrder time={"10:00"}
                        date={"Sat"}
                        loggedUser={"mario.rossi@gmail.com"}
                        loggedUserRole={"CUSTOMER"}
                        setTopUpWarning = {x=>x}
            />
        </Router>
    );
    await waitFor(()=>{
        getByText("Send order");
    })
    fireEvent.click(getByText("Send order"));
    await waitFor(()=>{
        getByText("Review and confirm order");
        getByTestId("date");
        getByTestId("time");
        getByTestId("Home delivery");
        getByTestId("address");
        getByTestId("placeorder");
    });
    fireEvent.change(getByTestId("date"), {target:{value:'2022-02-23'}});
    fireEvent.change(getByTestId("time"), {target:{value:"10:00"}});
    fireEvent.click(getByTestId("Home delivery"));
    fireEvent.change(getByTestId("address"), {target:{value:"Via Roma 10, Torino"}});
    fireEvent.click(getByTestId("placeorder"));
    expect(mockPlaceOrder).toBeCalledTimes(1);
    await waitFor(()=>{
        getByText("Order Completed");
        getByText(/Error in placed order, please retry!/i);
        expect(()=>getByText(/Apples/i)).not.toThrow();
    })
})

test("Place order customer - pickup- ok", async()=>{
    localStorage.setItem("role", "CUSTOMER");
    localStorage.setItem("username", "mario.rossi@gmail.com");
    mockGetCart.mockResolvedValueOnce(
        [
            {   "productId":7,
                "name":"Apples",
                "unitOfMeasurement":"Kg",
                "totalQuantity":50.0,
                "quantityAvailable":2.0,
                "price":2.5,
            }]);
    const mockPlaceOrder = (API.placeOrder=jest.fn());
    mockPlaceOrder.mockResolvedValueOnce(true);
    const {getByText, getByTestId} = render(
        <Router>
            <PlaceOrder time={"10:00"}
                        date={"Sat"}
                        loggedUser={"mario.rossi@gmail.com"}
                        loggedUserRole={"CUSTOMER"}
                        setTopUpWarning = {x=>x}
            />
        </Router>
    );
    await waitFor(()=>{
        getByText("Send order");
    })
    fireEvent.click(getByText("Send order"));
    await waitFor(()=>{
        getByText("Review and confirm order");
        getByTestId("date");
        getByTestId("time");
        getByTestId("Pick up");
        getByTestId("placeorder");
    });
    fireEvent.change(getByTestId("date"), {target:{value:'2022-02-23'}});
    fireEvent.change(getByTestId("time"), {target:{value:"10:00"}});
    fireEvent.click(getByTestId("Pick up"));
    fireEvent.click(getByTestId("placeorder"));
    expect(mockPlaceOrder).toBeCalledTimes(1);
    await waitFor(()=>{
        getByText("Order Completed");
        getByText("Congratulations, order placed correctly!");
        expect(()=>getByText(/Apples/i)).toThrow();
    })
})

test("Place order customer - pickup - fail", async()=>{
    localStorage.setItem("role", "CUSTOMER");
    localStorage.setItem("username", "mario.rossi@gmail.com");
    mockGetCart.mockResolvedValueOnce(
        [
            {   "productId":7,
                "name":"Apples",
                "unitOfMeasurement":"Kg",
                "totalQuantity":50.0,
                "quantityAvailable":2.0,
                "price":2.5,
            }]);
    const mockPlaceOrder = (API.placeOrder=jest.fn());
    mockPlaceOrder.mockResolvedValueOnce(false);
    const {getByText, getByTestId} = render(
        <Router>
            <PlaceOrder time={"10:00"}
                        date={"Sat"}
                        loggedUser={"mario.rossi@gmail.com"}
                        loggedUserRole={"CUSTOMER"}
                        setTopUpWarning = {x=>x}
            />
        </Router>
    );
    await waitFor(()=>{
        getByText("Send order");
    })
    fireEvent.click(getByText("Send order"));
    await waitFor(()=>{
        getByText("Review and confirm order");
        getByTestId("date");
        getByTestId("time");
        getByTestId("Pick up");
        getByTestId("placeorder");
    });
    fireEvent.change(getByTestId("date"), {target:{value:'2022-02-23'}});
    fireEvent.change(getByTestId("time"), {target:{value:"10:00"}});
    fireEvent.click(getByTestId("Pick up"));
    fireEvent.click(getByTestId("placeorder"));
    expect(mockPlaceOrder).toBeCalledTimes(1);
    await waitFor(()=>{
        getByText("Order Completed");
        getByText(/Error in placed order, please retry!/i);
        expect(()=>getByText(/Apples/i)).not.toThrow();
    })
})