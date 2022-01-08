/// <reference types = "Cypress" />
/**
 * usecase 6
 * Monday morning Calvin finds out that due to an unexpected frost all his broccoli are damaged and must be disposed. 
 * So he confirms the availability of all products, except for broccoli.
 */
context('06 farmer confirmation', () => {
    //TODO: test this use case
    it('dispose a product', () => {
        
        cy.visit(Cypress.env('baseUrl'));

        cy.get('[alt=logo]').should('exist');
        cy.get('[id=login]').should('exist');
        cy.get('[id=signup]').should('exist');
            
        cy.checkHomepage();

        cy.timeMachine(1,8);
        
        cy.login('calvin@gmail.com','password');

        cy.checkFarmer();

        cy.get('[id=button-ConfirmAvailability]').click();

        cy.checkConfirmAvailability();
        
        cy.get('#button-confirm-all').should('be.visible').click();
        
        cy.get('#button-set-Broccoli').should('be.visible').click();
        cy.get('#amount').clear().type('0');
        cy.get('#button-set-availability').click();
        
        cy.get('#button-submit').should('be.visible').click();
        
    })
    
    it('logout',()=>{
        cy.logout();
    })
})