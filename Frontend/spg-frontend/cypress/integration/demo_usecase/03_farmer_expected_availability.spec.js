/// <reference types = "Cypress" />
/**
 * usecase 3  
 * on Saturday morning Calvin, the farmer, wakes up and after a 
 * quick check enter the expected availability for next week 
 * (carrots, apples, cauliflower, broccoli, pumpkin)
*/

context('farmer forecasting', () => {

    it('farmer enter expected availability', () => {
        cy.visit(Cypress.env('baseUrl'));

        cy.get('[alt=logo]').should('exist');
        cy.get('[id=login]').should('exist');
        cy.get('[id=signup]').should('exist');
            
        cy.checkHomepage();  

        cy.login('calvin@gmail.com','password');

        cy.checkFarmer();

        cy.get('[id=button-ForecastProducts]').click();

        cy.checkProductsForecast();

        cy.contains('Pumpkin').should('have.length', 1);
        cy.contains('Broccoli').should('have.length', 1);
        cy.contains('Carrots').should('have.length', 1);
        cy.contains('Cauliflower').should('have.length', 1);
        cy.contains('Apple').should('have.length', 1);

        cy.timeMachine(6,10);
        cy.modifyForecast();
        cy.logout();
    })

})