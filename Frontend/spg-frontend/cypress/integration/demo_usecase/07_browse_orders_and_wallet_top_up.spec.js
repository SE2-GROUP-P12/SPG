/// <reference types = "Cypress" />
/**
 * usecase 7
 * At noon on Monday Mario checks the orders pending cancellation
 * and make a phone call to Giovanni 
 * to tell him he has to top-up the wallet. 
 * He receives a Satispay payment of 20€ and tops up his wallet.
 */
context('customer workflow', () => {

    it('seed Giovanni order', () => {
        //TODO: seed giovanni order 
    })

    it('Mario login', () => {
        cy.visit(Cypress.env('baseUrl'));
        cy.login('mario@gmail.com','password');
        cy.get('#button-HandleOrders').should('be.visible');
        cy.get('#button-HandleOrders').click();
        cy.get('#button-back').should('be.visible');
        cy.wait(2000);
        cy.get('#button-back').click();
        cy.get('#button-TopUp').should('be.visible');
    });


    it('Mario top-up Giovanni\'s wallet', () => {
        cy.get('#button-TopUp').should('be.visible');
        cy.get('#button-TopUp').click();
        cy.contains('Top Up').should('be.visible');
        cy.topUp('giovanni@gmail.com','20','GiftCard');


    });

})