/// <reference types = "Cypress" />

context('customer workflow', () => {

    it('customer browses products', () => {

        cy.visit(Cypress.env('baseUrl'))
        cy.clearLocalStorage();

        cy.get('[alt=logo]').should('exist');
        cy.get('[id=login]').should('exist');
        cy.get('[id=signup]').should('exist');
        
        cy.contains('Come and find us!').should('exist');
        cy.contains('We are in:').should('exist');
        cy.contains('Roma').should('exist');
        cy.contains('Vicenza').should('exist');
        cy.contains('Biella').should('exist');
        cy.contains('Caserta').should('exist');
        cy.contains('Torino').should('exist');
        cy.contains('Catania').should('exist');
        cy.get('[alt=italymap]').should('exist');
        
        //cy.get('[id=logout]').click();
        cy.get('[id=login]').click();
        
        cy.contains('Login').should('exist');
        cy.contains('Email:').should('exist');
        cy.contains('Password:').should('exist');
        cy.contains('Back').should('exist');
        cy.get('[type=submit]').should('exist');
        
        cy.get('[id=email]').type('mario.rossi@gmail.com');
        cy.get('[id=password]').type('password');
        cy.get('[type=submit]').click();
        
        cy.contains('LOG OUT').should('exist');

        cy.contains('Customer')
        
    })
})