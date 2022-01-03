/// <reference types = "Cypress" />

context('customer workflow', () => {

    it('customer browses products', () => {

        cy.visit(Cypress.env('baseUrl'))
        cy.clearLocalStorage();

        cy.get('[alt=logo]').should('exist');
        cy.get('[id=login]').should('exist');
        cy.get('[id=signup]').should('exist');
         
        cy.checkHomepage();  
        cy.login('mario.rossi@gmail.com','password');

        cy.checkCustomer();

        cy.get('[id=button-BrowseProducts]').click();
        cy.checkBrowseProducts();

        

        cy.logout();
    })

})