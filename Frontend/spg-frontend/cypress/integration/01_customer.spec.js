/// <reference types = "Cypress" />

import { matchPath } from "react-router";
import { BrowserRouter } from "react-router-dom";

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

        cy.contains('Browse Products').click();
        cy.checkBrowseProducts();
    })
})