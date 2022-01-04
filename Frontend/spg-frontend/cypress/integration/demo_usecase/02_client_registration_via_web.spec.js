/// <reference types = "Cypress" />


/**
 * usecase 2
 * on Friday Giovanni, another client, 
 * registers himself with the web site
 */
context('customer workflow', () => {
    //TODO: test this use case
    
    it('Giovanni Sign up', () =>{
        cy.visit(Cypress.env('baseUrl'));
        cy.signUp('giovanni@gmail.com','password');

    })
    
    it('time machine example', () => {
        cy.visit(Cypress.env('baseUrl'));
        cy.get('#browse-product-button').click();
        cy.timeMachine(0,10);
        
    });
    //TODO: teardown -> elimina giovanni
})