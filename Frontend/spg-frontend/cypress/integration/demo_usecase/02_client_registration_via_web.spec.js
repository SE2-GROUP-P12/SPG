/// <reference types = "Cypress" />



/**
 * usecase 2
 * on Friday Giovanni, another client, 
 * registers himself with the web site
 */
context('02 Giovanni signup', () => {

    //no seeds are needed

    it('Giovanni Sign up', () => {
        cy.visit(Cypress.env('baseUrl'));
        cy.get('#signup').click();
        cy.signUp(
            'giovanni@gmail.com',
            'Malnati30L',
            'Giovanni',
            'Malnati',
            'Corso Duca degli Abruzzi, 24 - 10129 Torino (TO)',
            'MLNGNN80A01L219Q'
        );
    })

    it('Giovanni Login', () => {
        cy.visit(Cypress.env('baseUrl'));
        cy.logout();
        cy.login('giovanni@gmail.com', 'Malnati30L');
        cy.wait(1000);
        cy.logout();
    })


    it('teardown user Giovanni', () => {
        const dbName = 'spg';
        var query = `DELETE FROM spg.customer WHERE address like 'Corso Duca degli Abruzzi, 24 - 10129 Torino (TO)';`;
        cy.task('queryDatabase', { dbName, query });
        query = "DELETE FROM spg.user WHERE email like 'giovanni@gmail.com';";
        cy.task('queryDatabase', { dbName, query });
    })

})
