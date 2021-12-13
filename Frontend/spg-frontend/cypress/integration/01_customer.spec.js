/// <reference types = "Cypress" />



context('customer workflow', () => {

    it('customer browses products', () => {

        cy.visit(Cypress.env('baseUrl'));
        
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
    
    it('customer add to cart', () => {
        
        cy.login('mario.rossi@gmail.com','password');
        
        cy.visit(Cypress.env('baseUrl')+'/BrowseProducts');
        
        //TODO: Gestire meglio la wait
        cy.wait(1000);
        
        cy.get(':nth-child(1) > .MuiPaper-root > .MuiCardActions-root > .MuiGrid-container > .MuiGrid-root > .btn').click();
        cy.get('[id=amount]').clear().type('1');
        cy.get('[id=button-addtocart]').click();
        cy.contains('Close').click();

        //TODO: test checkout and basket pop-up
        // cy.contains('Check out').click();

        cy.get('#dashboard').click();
        cy.wait(2000);
        cy.get('#button-ShowCart').click({force:true});
        cy.contains('Apple').should('exist');
        cy.get('.btn').contains('Delete order').click();

        cy.wait(2000);
        cy.logout();
    })


})