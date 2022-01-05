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


    it('teardown user Giovanni', () => {
        const dbName = 'spg';
        var query = `DELETE FROM spg.customer WHERE address like 'Corso Duca degli Abruzzi, 24 - 10129 Torino (TO)';`;
        cy.task('queryDatabase', { dbName, query });
        query = "DELETE FROM spg.user WHERE email like 'giovanni@gmail.com';";
        cy.task('queryDatabase', { dbName, query });
    })
    //TODO: teardown -> (seed) elimina giovanni
    //TODO: teardown -> (seed) elimina mele, cavolfiori ...
    //TODO: teardown -> elimina ordine creato nel test
})