/// <reference types = "Cypress" />
/* 
 3. on Saturday morning Calvin, the farmer, wakes up and after a 
 quick check enter the expected availability for next week 
 (carrots, apples, cauliflower, broccoli, pumpkin)
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

    })

})