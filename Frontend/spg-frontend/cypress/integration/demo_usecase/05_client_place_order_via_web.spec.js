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
    }) 
    //TODO: seed -> inserisci mele, cavolfiori ...
    
    //TODO: test this use case
    it('Giovanni place an order', () => {
        cy.visit(Cypress.env('baseUrl'))
        cy.clearLocalStorage();

        cy.get('[alt=logo]').should('exist');
        cy.get('[id=login]').should('exist');
        cy.get('[id=signup]').should('exist');
         
        cy.timeMachine(0,10)

        cy.checkHomepage();  
        cy.login('mario.rossi@gmail.com','password');
        cy.checkCustomer();

        cy.get('[id=button-BrowseProducts]').click();
        cy.checkBrowseProducts();

        cy.get('#button-add-Cauliflowers').click();
        cy.get('[id=amount]').clear({force:true}).type('1');
        cy.get('[id=button-add-to-cart]').click({force:true});
        cy.get('[id=button-close]').click({force:true});
        
        cy.wait(2000);
        
        cy.get('#button-add-Apples').click({force:true});
        cy.get('[id=amount]').clear({force:true}).type('2');
        cy.get('[id=button-add-to-cart]').click({force:true});
        cy.get('[id=button-close]').click({force:true});
        
        cy.get('#button-basket').click({force:true});
        cy.get('#button-checkout').click({force:true});

        cy.checkPlaceOrder();
        cy.get('#button-send-order').should('be.visible').click();

        cy.contains('Review and confirm order');
        cy.contains('ISSUER: mario.rossi@gmail.com')
        
        cy.get('#date-field').type('29/12/2021');
        cy.get('#time-field').type('10:30');

        cy.get('#button-place-order').click();
    })

    it('logout',()=>{
        cy.logout();
    })

    it('teardown basket Giovanni', () => {
        const dbName = 'spg';
        var query = `delete from basket_prods where true`;
        cy.task('queryDatabase', { dbName, query });
        cy.wait(2000);
        query = `delete from basket where true`;
        cy.task('queryDatabase', { dbName, query });
        query = `DELETE FROM spg.customer WHERE address like 'Corso Duca degli Abruzzi, 24 - 10129 Torino (TO)';`;
        cy.task('queryDatabase', { dbName, query });
        var query = "DELETE FROM spg.user WHERE email like 'giovanni@gmail.com';";
        cy.task('queryDatabase', { dbName, query });
    })
    
    //TODO: teardown -> (seed) elimina giovanni
    //TODO: teardown -> (seed) elimina mele, cavolfiori ...
    //TODO: teardown -> elimina ordine creato nel test
})