/// <reference types = "Cypress" />


/**
 * usecase 2
 * on Friday Giovanni, another client, 
 * registers himself with the web site
 */
context('customer workflow', () => {
    
    //no seeds are needed
    
    it('Giovanni Sign up', () =>{
        cy.visit(Cypress.env('baseUrl'));
        cy.signUp(
            'giovanni@gmail.com',
            'Malnati30L',
            'Giovanni',
            'Malnati',
            'Corso Duca degli Abruzzi, 24 - 10129 Torino (TO)',
            'MLNGNN80A01L219Q'
            );
    })
    
    it('Giovanni Login', () =>{
        cy.visit(Cypress.env('baseUrl'));
        cy.logout();
        cy.login('giovanni@gmail.com','Malnati30L');
    })
    

    
    it('time machine example', () => {
        cy.visit(Cypress.env('baseUrl'));
        cy.get('#browse-product-button').click();
        cy.timeMachine(0,10);
        
    });

    //TODO: use 02_teardown.sql revert the changes of this usecase
})