/// <reference types = "Cypress" />

context('customer workflow', () => {
    //TODO: test this use case
    it('time machine example', () => {
        cy.visit(Cypress.env('baseUrl'));
        cy.get('#browse-product-button').click();
        cy.timeMachine(0,10);
        
    });
})