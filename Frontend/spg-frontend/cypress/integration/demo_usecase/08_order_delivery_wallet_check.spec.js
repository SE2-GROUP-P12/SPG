/// <reference types = "Cypress" />

/**
 * usecase 8
 * On Wednesday Mario first delivers the order to Giovanni,
 * and lated delivers the order to Miriam 
 * and checks her wallet balance so 
 * she's sure she can place another order for next week.
 */
context('customer workflow', () => {


    it('seed', () => {
        //TODO: seed
    });
    //TODO: test this use case

    it('Mario deliver orders', () => {
        cy.visit(Cypress.env('baseUrl'));
        cy.login('mario@gmail.com','password');
        cy.get('#button-HandleOrders').should('be.visible').click();
        cy.get('#button-deliver-miriam').should('be.be.visible').click();
        cy.wait(1000);
        cy.get('#button-deliver-giovanni').should('be.be.visible').click();
        cy.wait(1000);

        cy.get('#button-back').should('be.visible').click();
        cy.get('#button-TopUp').should('be.visible').click();
    })

    it('teardown', () => {
        //TODO: teardown
    })

})