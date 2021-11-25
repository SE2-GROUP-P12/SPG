var docapi = require('./API.js');

test ('Browse product should return 50 items, including Apples',
async() => {
    docapi.API.browseProducts().then(
        data => {
            //assume that we retrieved 50 items
            expect(data).toHaveLength(50);
            //assume that we received data as an array and that inside of it there is an item with name 'Apples'
            expect(data).arrayContaining([
                expect.objectContaining({name: 'Apples'})
            ]);
        }
    );
});

test ("Add to mario.rossi@gmail.com's cart 1kg of apples to cart and retrieve it afterwards",
async () => {
    docapi.API.addToCart({productId: 5, email: "mario.rossi@gmail.com", quantity: 1}).then(
        async (data) => {
            //assume that the process of adding the item inside the cart was successful
            expect(data).toBeTruthy();
            //retrieve mario's cart
            docapi.API.getCart({'email': 'mario.rossi@gmail.com'}).then(
                data => {
                    //expect the cart to contain only one item...
                    expect(data).toHaveLength(1);
                    // ... and that item being 1kg of apples
                    expect(data).arrayContaining([
                        expect.objectContaining({name: 'Apples', quantityAvailable: 1})
                    ]);
                }
            );
        }
    );
});

test ("Add 10 euros to mario.rossi@gmail.com's wallet", 
    async () => {
        //assume that mario.rossi@gmail.com exists
        docapi.API.customerExistsByMail('mario.rossi@gmail.com').then(
            data => {
                expect(data).toBeTruthy();
                //get mario.rossi@gmail.com's wallet
                docapi.API.getWallet('mario.rossi@gmail.com').then(
                    wallet => {
                        expect(wallet).toBe(0);
                        //add 10 euros to the wallet
                        docapi.API.topUp({email: "mario.rossi@gmail.com", value: 10}).then(
                            newWallet => {
                                //confirm the modification
                                expect(newWallet).toBe(10);
                            }
                        );
                    }
                );
            }
        );
    }
);

test ("Place an order from a cart", 
    async () => {
        //add a product to mario's cart (if you wanted more products you'd need to make more calls to this very same api, one for each product)
        docapi.API.addToCart({productId: 5, email: "mario.rossi@gmail.com", quantity: 1}).then(
            data => {
                expect(data).toBeTruthy();
                //place the order for mario.rossi@gmail.com's cart
                docapi.API.placeOrder({email: "mario.rossi@gmail.com", customer: "mario.rossi@gmail.com"}).then(
                    answer => {
                        //expect it to be successful
                        expect(answer).toBeTruthy();
                        //look for the order in the orders list
                        docapi.API.getAllOrders().then(
                            orders =>{
                            expect(orders).toHaveLength(1);
                            expect(orders).arrayContaining([expect.objectContaining({email: 'mario.rossi@gmail.com'})]);
                        });
                    });
            });
    }
);

//Marti: Si regà, è brutto impestato ma se ci metto l'await manda tutto in vacca per nessuna apparente ragione

test ("Drop cart", 
    async() => {
        //add products to a cart
        docapi.API.addToCart({productId: 5, email: "mario.rossi@gmail.com", quantity: 1}).then( () => {
            docapi.API.addToCart({productId: 6, email: "mario.rossi@gmail.com", quantity: 2}).then( () => {
                docapi.API.addToCart({productId: 7, email: "mario.rossi@gmail.com", quantity: 3}).then(
                    () => {
                    //retrieve the cart
                    docapi.API.getCart({'email': 'mario.rossi@gmail.com'}).then(
                        cart => {
                            //expect the cart to contain 3 items
                            expect(cart).toHaveLength(3);
                            //then drop its content
                            docapi.API.dropOrder({'email': 'mario.rossi@gmail.com'}).then(
                                deleted => {
                                    //expect the drop to be successful
                                    expect(deleted).toBeTruthy();
                                    docapi.API.getCart({'email': 'mario.rossi@gmail.com'}).then(
                                        //expect the cart to be now empty
                                        newCart => expect(newCart).toHaveLength(0)
                                    );
                                });
                        })
                    });
            });
        }
    );}
);

test ("Get all orders, then get only the orders for mario",
async () => {
    //create an order (at this point we've seen how it's done)
    docapi.API.addToCart({productId: 5, email: "mario.rossi@gmail.com", quantity: 1}).then(
        () => {
            docapi.API.placeOrder({email: "mario.rossi@gmail.com", customer: "mario.rossi@gmail.com"}).then(
                //let's add another order, this time for paolo.bianchi@gmail.com (the cart is still mario.rossi because he acts in a shopemployee-like way -for now)
                docapi.API.addToCart({productId: 6, email: "mario.rossi@gmail.com", quantity: 5}).then(
                    () => {
                        docapi.API.placeOrder({email: "mario.rossi@gmail.com", customer: "paolo.bianchi@gmail.com"}).then(
                            () => {
                                //retireve all orders and find both customers
                                docapi.API.getAllOrders().then(
                                    orders => {
                                        expect(orders).toHaveLength(2);
                                        expect(orders).arrayContaining(expect.objectContaining({email: 'mario.rossi@gmail.com'}));
                                        expect(orders).arrayContaining(expect.objectContaining({email: 'paolo.bianchi@gmail.com'}));
                                        //retrieve orders by mail and find onli mario's
                                        docapi.API.getOrdersByEmail("mario.rossi@gmail.com").then(
                                            newOrders => {
                                                expect(newOrders).toHaveLength(1);
                                                expect(newOrders).arrayContaining(expect.objectContaining({email: 'mario.rossi@gmail.com'}));
                                                expect(newOrders).not.arrayContaining(expect.objectContaining({email: 'paolo.bianchi@gmail.com'}));
                                            }
                                        );
                                    }
                                );
                            }
                        )
                    }   
                )
            );
        });
    
});

test ("Deliver order", 
    async () => {
        //create an order
        docapi.API.addToCart({productId: 5, email: "mario.rossi@gmail.com", quantity: 1}).then(
            () => {
                docapi.API.placeOrder({email: "mario.rossi@gmail.com", customer: "mario.rossi@gmail.com"}).then(
                    () => {
                        docapi.API.getAllOrders().then(
                            orders=>{
                                //get the list of orders
                                expect(orders).toHaveLength(1).arrayContaining(expect.objectContaining({email: 'mario.rossi@gmail.com'}));
                                let orderId = orders[email = 'mario.rossi@gmail.com'].orderId; 
                                //NOTE: the orderId is actually retrieved from the very element on the page, 
                                //in this only case we assume for simplicity that there are no other orders for mario and so we can retrieve the orderId like this

                                //deliver an order
                                docapi.API.deliverOrder(orderId).then(
                                    answer => {
                                        //assume the delivery was successful
                                        expect(answer).toBeTruthy();
                                        //and that the order is no more in the orders list
                                        docapi.API.getAllOrders.then(
                                            newOrders => expect(newOrders).toHaveLength(0)
                                        );
                                    }
                                );
                            }
                        );
                    }
                );}
        );
});

test ("Add customer", async() => {
    //check existence of customer
    docapi.API.customerExistsByMail("levi.ackerman@gmail.com").then(
        answer => 
        {
            //assume that the customer doesn't exist yet
            expect(answer).toBeFalsey();
            //add the customer
            docapi.API.addCustomer({"name":"Levi",
                                    "surname":"Ackermann",
                                    "address":"Via Eren da Qui 12, Milano",
                                    "ssn":"lvickm80a01l840f",
                                    "phoneNumber":"1234567890",
                                    "role":"CUSTOMER",
                                    "email":"levi.ackerman@gmail.com",
                                    "password":"0e9584fba2dbbe7ddd67f50241cb3b36cc24fc15fc7d06e1d9f3d37f3776a46d"}).then(
                                        answer => {
                                            //assume that the creation was successful
                                            expect(answer).toBeTruthy();
                                            docapi.API.customerExistsByMail("levi.ackerman@gmail.com").then(
                                                //check that the customer exists
                                                answer => expect(answer).toBeTruthy()
                                            );
                                        }
                                    );
        }
    );
})