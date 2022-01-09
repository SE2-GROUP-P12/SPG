/// <reference types = "Cypress" />

/**
 * usecase 8
 * On Wednesday Mario first delivers the order to Giovanni,
 * and lated delivers the order to Miriam 
 * and checks her wallet balance so 
 * she's sure she can place another order for next week.
 */
context('08 order delivery and wallet check', () => {


    it('seed', () => {
        //TODO: seed
    });
    //TODO: test this use case

    it('Mario deliver orders', () => {
        cy.visit(Cypress.env('baseUrl'));
        cy.timeMachine(3,10);
        cy.login('mario@gmail.com','password');
        cy.get('#button-HandleOrders').should('be.visible').click();
        cy.get('#button-deliver-miriam').should('be.be.visible').click();
        cy.wait(1000);
        cy.get('#button-deliver-giovanni').should('be.be.visible').click();
        cy.wait(1000);

        cy.get('#button-back').should('be.visible').click();
        cy.wait(1000);
        cy.get('#button-TopUp').should('be.visible').click();
        cy.wait(1000);
        var email = 'miriam@gmail.com';
        cy.get('#email').type(email);
        cy.get('#button-Submit').click();
        cy.contains('User correctly found').should('exist');
        cy.wait(1000);
        var email = 'giovanni@gmail.com';
        cy.get('#email').cear().type(email);
        cy.get('#button-Submit').click();
        cy.contains('User correctly found').should('exist');
        cy.wait(1000);
    })

    it('teardown', () => {
        //TODO: teardown
    })

})