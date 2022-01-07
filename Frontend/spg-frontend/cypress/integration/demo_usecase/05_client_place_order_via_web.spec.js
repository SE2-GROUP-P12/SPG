/// <reference types = "Cypress" />


/**
 * usecase 5
 * Sunday morning Giovanni places, via the web interface,
 *  an order for: apple 2kg, and cauliflower 1kg,
 *  the order is accepted even if he has no money in the wallet. 
 * He plans to pick up his order on Wednesday morning.
 */
context('Giovanni place order', () => {

    it('seed user Giovanni', () => {
        //TODO: seed -> inserisci giovanni
        const dbName = 'spg';
        var query = `INSERT INTO spg.user (role, user_id, active, email, name, password, phone_number, ssn, surname) VALUES ('CUSTOMER', 84, false, 'giovanni@gmail.com', 'Giovanni', '52ee8459cfe61a1b86d2a5a7c9b5926b1b25ada31b76640782bf6a7493bef2cc', '', 'MLNGNN80A01L219Q', 'Malnati');`;
        cy.task('queryDatabase', { dbName, query });
        query = `INSERT INTO spg.customer (address, missed_pick_up, wallet, user_id) VALUES ('Corso Duca degli Abruzzi, 24 - 10129 Torino (TO)', 0, 0, 84);`;
        cy.task('queryDatabase', { dbName, query });
        cy.wait(1000);
    })
    //TODO: seed -> inserisci mele, cavolfiori ...

    //TODO: test this use case
    



    it('Giovanni place order', () => {
        cy.visit(Cypress.env('baseUrl'));
        cy.timeMachine(0, 10);
        cy.get('[id=login]').should('be.visible');
        cy.login('giovanni@gmail.com','Malnati30L');
        

        cy.get('#button-BrowseProducts').should('be.visible')
            .click();


        cy.get('#button-add-Cauliflowers')
            .should('be.visible')
            .click();
        cy.get('#amount')
            .clear()
            .type('1');

        cy.get('#button-add-to-cart').should('be.visible')
            .click();

        cy.contains('Product added').should('be.visible');

        cy.get('#button-close').click();

        cy.wait(1000);
        cy.get('#button-add-Apples')
            .should('be.visible')
            .click();
        cy.get('#amount')
            .clear()
            .type('2');
        cy.get('#button-add-to-cart')
            .click();
        cy.get('#button-close')
            .click();

        cy.wait(2000);
        cy.get('#button-basket').should('be.visible').click();
        cy.get('#button-checkout').should('be.visible').click();
        cy.wait(2000);
        cy.get('#button-send-order').should('be.visible').click();
        cy.wait(2000);
        var date = new Date();
        while (date.getDay() != 3){
            date.setDate(date.getDate()+1);    
        };
        cy.get('[type=date]').type(date.toISOString().split('T')[0]);
        cy.get('[type=time]').type('10:00');
        cy.get('#button-place-order').should('be.visible')
            .click();
        cy.contains('Congratulations, order placed correctly!').should('be.visible');
        cy.get('#button-closeOrderCompleted').click();
        
    });


    it('Giovanni logout', () => {
        cy.visit(Cypress.env('baseUrl'));
        cy.logout();
        cy.wait(1000);
    })

    it('teardown basket Giovanni', () => {
        // const dbName = 'spg';
        // var query = `delete from basket_prods where true`;
        // cy.task('queryDatabase', { dbName, query });
        // cy.wait(2000);
        // query = `delete from basket where true`;
        // cy.task('queryDatabase', { dbName, query });
        // query = `delete from order_prods where true`;
        // cy.task('queryDatabase', { dbName, query });
        // query = `delete from order_tab where true`;
        // cy.task('queryDatabase', { dbName, query });
        // query = `DELETE FROM spg.customer WHERE address like 'Corso Duca degli Abruzzi, 24 - 10129 Torino (TO)';`;
        // cy.task('queryDatabase', { dbName, query });
        // var query = "DELETE FROM spg.user WHERE email like 'giovanni@gmail.com';";
        // cy.task('queryDatabase', { dbName, query });
    })
})