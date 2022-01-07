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
        cy.wait(1000);
        cy.checkCustomer();

        cy.get('[id=button-BrowseProducts]').click();
        cy.wait(1000);
        cy.checkBrowseProducts();
        cy.wait(1000);
    })

    it('logout', () => {
        cy.logout();
    })

})