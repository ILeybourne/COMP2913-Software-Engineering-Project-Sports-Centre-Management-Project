describe("TestTimetable", () => {
    it("Navigate to home page", function () {
        cy.visit('/');
    });

    it("Check button labels are correct", () => {
        cy.get('button[class="fc-today-button fc-button fc-button-primary"]').contains("today");
        cy.get('button[class="fc-resourceTimelineDay-button fc-button fc-button-primary fc-button-active"]').contains("day");
        cy.get('button[class="fc-resourceTimelineWeek-button fc-button fc-button-primary"]').contains("week");
    });

    it("Check arrow buttons exist", () => {
        cy.get('button[class="fc-prev-button fc-button fc-button-primary"]');
        cy.get('button[class="fc-next-button fc-button fc-button-primary"]');
    });

    it("Check resource and time labels exist", () => {
        cy.get('span[class="fc-cell-text"]').contains("Resources");
        cy.get('th[class="fc-widget-header"]').get('div').get('span').contains("6am");
        cy.get('th[class="fc-widget-header"]').get('div').get('span').contains("7am");
        cy.get('th[class="fc-widget-header"]').get('div').get('span').contains("8am");
        cy.get('th[class="fc-widget-header"]').get('div').get('span').contains("9am");
        cy.get('th[class="fc-widget-header"]').get('div').get('span').contains("10am");
        cy.get('th[class="fc-widget-header"]').get('div').get('span').contains("11am");
        cy.get('th[class="fc-widget-header"]').get('div').get('span').contains("12pm");
        cy.get('th[class="fc-widget-header"]').get('div').get('span').contains("1pm");
        cy.get('th[class="fc-widget-header"]').get('div').get('span').contains("2pm");
        cy.get('th[class="fc-widget-header"]').get('div').get('span').contains("3pm");
        cy.get('th[class="fc-widget-header"]').get('div').get('span').contains("4pm");
        cy.get('th[class="fc-widget-header"]').get('div').get('span').contains("5pm");
        cy.get('th[class="fc-widget-header"]').get('div').get('span').contains("6pm");
        cy.get('th[class="fc-widget-header"]').get('div').get('span').contains("7pm");
        cy.get('th[class="fc-widget-header"]').get('div').get('span').contains("8pm");
        cy.get('th[class="fc-widget-header"]').get('div').get('span').contains("9pm");
        cy.get('th[class="fc-widget-header"]').get('div').get('span').contains("10pm");
    });

    it("Check invalid labels do not exist", () => {
        cy.get('th[class="fc-widget-header"]').get('div').get('span').contains("5am").should('not.exist');
        cy.get('th[class="fc-widget-header"]').get('div').get('span').contains("11pm").should('not.exist');
    });

    it("Check current date is correct", () => {
        const currentDate = Cypress.moment().format('MMMM DD, YYYY');
        cy.get('div[class="fc-center"]').get('h2').contains(currentDate);
    });
});