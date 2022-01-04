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
 * dayOfweek = 0 sunday, 1 Monday, .... 6 saturday
 * Hour of the day
 */
 Cypress.Commands.add('signUp', (email,password) => {

    cy.get('#signup').click();
    cy.get('#email').should('exist');
    cy.get('#email').type('giovanni@gmail.com');
    cy.get('#password').should('exist');
    cy.get('#password').type('Malnati30L');
    cy.get('#name').should('exist');
    cy.get('#name').type('Giovanni');
    cy.get('#surname').should('exist');
    cy.get('#surname').type('Malnati');
    cy.get('#address').should('exist');
    cy.get('#address').type('Corso Duca degli Abruzzi, 24 - 10129 Torino (TO)');
    cy.get('#ssn').should('exist');
    cy.get('#ssn').type('MLNGNN80A01L219Q');
    cy.get('#phoneNumber').should('exist');
    cy.get('#submit').click();
    cy.wait(2000);
    cy.contains('Creation successful').should('exist');
    cy.get('#home').should('exist');
    cy.get('#home').click();
    
})

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

Cypress.Commands.add('logout', ()=> {
    cy.get('[id=logout').should('exist');
    cy.get('[id=logout').click();

    cy.get('[id=login').should('exist');
    cy.checkUrl("/");

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
    cy.get('[id=basket]').should('exist');
    cy.contains('Back').should('exist');

    cy.get('[alt=fruit]').should('exist')
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

