/// <reference types = "Cypress" />


/**
 *  usecase 1:
 *  on Thursday Miriam, a client, visits the SPG and asks Mario, the shop employee,
 *  to be registered for the system and charges the wallet with 50€
 */
context('customer workflow', () => {
    
    it('Mario login', () => {
        cy.visit(Cypress.env('baseUrl'));
        cy.login('mario@gmail.com','password');
        cy.get('#button-ResgisterCustomer').should('be.visible');
    });
    it('Miriam registration', () => {
        cy.get('#button-ResgisterCustomer').click();
        cy.contains('Register').should('exist');
        cy.signUp(
            'miriam@gmail.com',
            'miriam1945',
            'Miriam',
            'Sciandra',
            'Corso Unione Sovietica, 110 10134 Torino TO',
            'MRMSND45A47G224W'
        );
    });
    it('Miriam logout',() => {
        cy.logout();
    })
    
    it('Mario login', () => {
        cy.visit(Cypress.env('baseUrl'));
        cy.login('mario@gmail.com','password');
        cy.get('#button-TopUp').should('be.visible');
    });

    it('Miriam wallet top up', () => {
        cy.get('#button-TopUp').click();
        cy.contains('Top Up').should('be.visible');
        cy.topUp('miriam@gmail.com','50');
    })

    
    it('Mario logout',() => {
        cy.logout();
    })

    it('teardown Miriam user', () => {
        const dbName = 'spg';
        var query = `DELETE FROM spg.customer WHERE address like 'Corso Unione Sovietica, 110 10134 Torino TO';`;
        cy.task('queryDatabase', { dbName, query });
        query = "DELETE FROM spg.user WHERE email like 'miriam@gmail.com';";
        cy.task('queryDatabase', { dbName, query });
        //wallet operations will be automatically dropped from the db when the user is deleted
    });

})