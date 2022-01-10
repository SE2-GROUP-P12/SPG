/// <reference types = "Cypress" />


/**
 * usecase 4
 * Saturday evening Miriam passes by the SPG and asks Mario 
 * to place an order for her: carrots 1kg, pumpkin 2kg, and broccoli 1kg.
 * She will pick up her order next Wednesday.
 */
context('04 Shop employee places an order', () => {

    it('seed user Miriam', () => {
        const dbName = 'spg';
        var query = `INSERT INTO spg.user (role, user_id, active, email, name, password, phone_number, ssn, surname) VALUES ('CUSTOMER', 790, false, 'miriam@gmail.com', 'Miriam', '8ceedd0bfd945a09ae833dcaef716af1a60d5cdf0223e8de5ce859d0bed90513', '', 'MRMSND45A47G224W', 'Sciandra');`;
        cy.task('queryDatabase', { dbName, query });
        query = `INSERT INTO spg.customer (address, missed_pick_up, wallet, user_id) VALUES ('Corso Unione Sovietica, 110 10134 Torino TO', 0, 50, 790);`;
        cy.task('queryDatabase', { dbName, query });
        cy.wait(1000);
    })
    
    it('place an order routine', () => {
        cy.visit(Cypress.env('baseUrl'));

        cy.get('[alt=logo]').should('exist');
        cy.get('[id=login]').should('exist');
        cy.get('[id=signup]').should('exist');
        
        cy.checkHomepage();  
        cy.timeMachine(6,18);

        cy.login('mario@gmail.com','password');

        cy.checkShopEmployee();

        cy.get('[id=button-BrowseProducts]').click({force:true});
        cy.checkBrowseProducts();

        cy.get('#button-add-Carrots').should('be.visible').click({force:true});
        cy.waitUntil(() =>
            cy.get('#amount')
                .as('amount')
                .wait(10) // for some reason this is needed, otherwise next line returns `true` even if click() fails due to detached element in the next step
                .then($el => Cypress.dom.isAttached($el)),
            { timeout: 1000, interval: 10 }
        )
            .get('@amount')
            .clear({force:true})
            .type('1');
        cy.get('[id=button-add-to-cart]').should('be.visible').click({force:true});
        cy.wait(500);
        cy.get('[id=button-close]').should('be.visible').click({force:true});
        
        cy.wait(2000);
        
        cy.get('#button-add-Pumpkin').should('be.visible').click({force:true});
        cy.waitUntil(() =>
            cy.get('#amount')
                .as('amount')
                .wait(10) // for some reason this is needed, otherwise next line returns `true` even if click() fails due to detached element in the next step
                .then($el => Cypress.dom.isAttached($el)),
            { timeout: 1000, interval: 10 }
        )
            .get('@amount')
            .clear({force:true})
            .type('1');
        cy.get('[id=button-add-to-cart]').should('be.visible').click({force:true});
        cy.wait(500);
        cy.get('[id=button-close]').should('be.visible').click({force:true});
        
        cy.wait(2000);
        
        cy.get('#button-add-Broccoli').should('be.visible').click({force:true});
        cy.get('[id=amount]').clear({force:true}).type('1');
        cy.get('[id=button-add-to-cart]').should('be.visible').click({force:true});
        cy.get('[id=button-close]').should('be.visible').click({force:true});
        
        cy.wait(2000);
        
        cy.get('#button-basket').should('be.visible').click({force:true});
        cy.get('#button-checkout').should('be.visible').click({force:true});

        cy.checkPlaceOrder();

        cy.contains('Whose order is this?');
        cy.get('#field-email').clear().type('miriam@gmail.com');
        cy.get('#button-submit')
            .wait(100)
            .should('be.visible')
            .click({force:true});
        cy.contains('User found, you can now place their order').should('be.visible');

        cy.get('#button-send-order')
            .should('be.visible')
            .wait(100)
            .click({force:true});
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
        cy.get('#button-closeOrderCompleted').click({force:true});

    })

    it('logout',()=>{
        cy.logout();
    })

    /* it('teardown Miriam user', () => {
        const dbName = 'spg';
        var query = `DELETE FROM spg.customer WHERE address like 'Corso Unione Sovietica, 110 10134 Torino TO';`;
        cy.task('queryDatabase', { dbName, query });
        query = "DELETE FROM spg.user WHERE email like 'miriam@gmail.com';";
        cy.task('queryDatabase', { dbName, query });
        //wallet operations will be automatically dropped from the db when the user is deleted
    }); */
})