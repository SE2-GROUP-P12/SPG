// ***********************************************
// This example commands.js shows you how to
// create various custom commands and overwrite
// existing commands.
//
// For more comprehensive examples of custom
// commands please read more here:
// https://on.cypress.io/custom-commands
// ***********************************************
//
//
// -- This is a parent command --
// Cypress.Commands.add('login', (email, password) => { ... })
//
//
// -- This is a child command --
// Cypress.Commands.add('drag', { prevSubject: 'element'}, (subject, options) => { ... })
//
//
// -- This is a dual command --
// Cypress.Commands.add('dismiss', { prevSubject: 'optional'}, (subject, options) => { ... })
//
//
// -- This will overwrite an existing command --
// Cypress.Commands.overwrite('visit', (originalFn, url, options) => { ... })
Cypress.Commands.add('checkUrl', (url) => {
    const escapeRegExp = (string) => {
        return string.replace(/[.*+?^${}()|[\]\\]/g, '\\$&');
    }
    const regex = new RegExp(escapeRegExp(Cypress.env('baseUrl')+url), 'i');
    cy.url().should('match', regex)
})

Cypress.Commands.add('checkHomepage', () => {
    cy.checkUrl('/');
    cy.contains('Come and find us!').should('exist');
    cy.contains('We are in:').should('exist');
    cy.contains('Roma').should('exist');
    cy.contains('Vicenza').should('exist');
    cy.contains('Biella').should('exist');
    cy.contains('Caserta').should('exist');
    cy.contains('Torino').should('exist');
    cy.contains('Catania').should('exist');
    cy.get('[alt=italymap]').should('exist');
});


/**
 * Perform a signUp of an user (he/she will remain logged in)
 * (the page must be the registration page)
 * @Param email
 * @Param password
 * @Param name
 * @Param surname
 * @Param address
 * @Param ssn
 * @Param phoneNumber (optional)
 */
 Cypress.Commands.add('signUp', (
     email,
     password,
     name,
     surname,
     address,
     ssn,
     phoneNumber) => {

    cy.get('#email').type(email);
    cy.get('#password').type(password);
    cy.get('#name').type(name);
    cy.get('#surname').type(surname);
    cy.get('#address').type(address);
    cy.get('#ssn').type(ssn);
    if (phoneNumber) {
        cy.get('#phoneNumber').type(phoneNumber);
    }
    cy.get('#submit').click();
    cy.wait(2000);
    cy.contains('Creation successful').should('exist');
    cy.get('#home').click();
    
})
/**
 * perform a login of an user
 * @Param email
 * @Param password
 */
Cypress.Commands.add('login', (email, password) => {
    cy.get('[id=login]').should('exist');
    cy.get('[id=login]').click();
    cy.checkUrl('/LoginComponent');
    cy.contains('Login').should('exist');
    cy.contains('Email:').should('exist');
    cy.contains('Password:').should('exist');
    cy.contains('Back').should('exist');
    cy.get('[type=submit]').should('exist');
    
    cy.get('[id=email]').type(email);
    cy.get('[id=password]').type(password);
    cy.get('[type=submit]').click();

    cy.get('[id=logout]').should('exist');
})

/**
 * perform a logout of an logged in user
 */
Cypress.Commands.add('logout', ()=> {
    cy.get('[id=logout').should('exist');
    cy.get('[id=logout').click();

    cy.get('[id=login').should('exist');
    cy.checkUrl("/");

})
/**
 * perform a logout of an logged in user
 */
Cypress.Commands.add('topUp', (email,money)=> {
    cy.get('#email').type(email);
    cy.get('#button-Submit').click();
    cy.contains('User correctly found').should('exist');
    cy.get('#button-Cash').should('be.visible');
    cy.get('#button-Cash').click();
    cy.get('#button-TopUp').should('be.visible');
    cy.get('#amount').type(money);
    cy.get('#button-TopUp').click();
    cy.contains('Top up correctly performed').should('be.visible');

})


Cypress.Commands.add('checkCustomer', () => {
    cy.checkUrl('/Dashboard');
    cy.contains('WELCOME BACK').should('exist');
    cy.contains('Here you are your services').should('exist');
    cy.contains('Your Cart').should('exist');
    cy.contains('Your Orders').should('exist');
    cy.contains('Browse Products').should('exist');
    cy.contains('Your Wallet').should('exist');
    cy.contains('Settings').should('exist');
})

Cypress.Commands.add('checkFarmer', () => {
    cy.checkUrl('/Dashboard');
    cy.contains('WELCOME BACK').should('exist');
    cy.contains('Here you are your services').should('exist');
    cy.contains('Forecast Product').should('exist');
    cy.contains('Add Product').should('exist');
    cy.contains('Confirm Availability').should('exist');
    cy.contains('Settings').should('exist');
})

Cypress.Commands.add('checkBrowseProducts', () => {
    cy.checkUrl('/BrowseProducts')
    cy.contains('Products List').should('exist');
    
    //Spinner
    cy.get('[role=status]').should('exist');
    cy.get('[id=button-basket]').should('exist');
    cy.contains('Back').should('exist');

    cy.get('[alt=fruit]').should('exist')
})

Cypress.Commands.add('checkPlaceOrder', () => {
    cy.checkUrl('/PlaceOrder')
    cy.contains('Place Order').should('exist');

    cy.contains('Back').should('exist');
    cy.contains('Delete order').should('exist');
    cy.contains('Send order').should('exist');
})

Cypress.Commands.add('checkProductsForecast', () => {
    cy.checkUrl('/ProductsForecast')
    cy.contains('Products List').should('exist');
    
    //Spinner
    cy.get('[role=status]').should('exist');
    cy.contains('Back').should('exist');

    cy.get('[alt=fruit]').should('exist');
})

Cypress.Commands.add('checkConfirmAvailability', () => {
    cy.checkUrl('/ConfirmAvailability')
    cy.contains('Confirm availabilities for the next week').should('exist');
    
    //Spinner
    cy.get('[role=status]').should('exist');
    cy.contains('Back').should('exist');

    cy.get('[alt=fruit]').should('exist');
})

Cypress.Commands.add('modifyForecast', () => {
    cy.get('#button-forecast-Carrots').click({force:true});
    cy.get('[id=amount]').clear({force:true}).type('4');
    cy.get('[id=button-modifyForecast]').click({force:true});
    cy.get('.modal-footer > .btn').click({force:true});

    cy.wait(2000);

    cy.get('#button-forecast-Apples').click({force:true});
    cy.get('[id=amount]').clear({force:true}).type('4');
    cy.get('[id=button-modifyForecast]').click({force:true});
    cy.get('.modal-footer > .btn').click({force:true});

    cy.wait(2000);

    cy.get('#button-forecast-Cauliflowers').click({force:true});
    cy.get('[id=amount]').clear({force:true}).type('4');
    cy.get('[id=button-modifyForecast]').click({force:true});
    cy.get('.modal-footer > .btn').click({force:true});
    
    cy.wait(2000);
    
    cy.get('#button-forecast-Broccoli').click({force:true});
    cy.get('[id=amount]').clear({force:true}).type('4');
    cy.get('[id=button-modifyForecast]').click({force:true});
    cy.get('.modal-footer > .btn').click({force:true});
    
    cy.wait(2000);

    cy.get('#button-forecast-Pumpkin').click({force:true});
    cy.get('[id=amount]').clear({force:true}).type('4');
    cy.get('[id=button-modifyForecast]').click({force:true});
    cy.get('.modal-footer > .btn').click({force:true});
})

/**
 * dayOfweek = 0 sunday, 1 Monday, .... 6 saturday
 * Hour of the day
 */
 Cypress.Commands.add('timeMachine', (dayOfWeek,hour) => {

    var date = new Date();
    date.setHours(hour);
    cy.clock(date);
    cy.contains('TIME MACHINE').should('exist');
    cy.contains('TIME MACHINE').click();
    
    cy.get(':nth-child(2) > :nth-child('+(dayOfWeek+1)+') > .MuiButtonBase-root > .MuiIconButton-label > .MuiTypography-root')
        .click();
    
    cy.contains('TIME TRAVEL!').click();
    date.setDate(dayOfWeek);
    date.setDate(date.getDate() + (dayOfWeek - date.getDay()));
    cy.clock(date);
    cy.reload();
})

