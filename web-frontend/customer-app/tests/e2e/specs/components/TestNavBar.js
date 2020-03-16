describe("TestNavBar", () => {
    it("Navigate to homepage", function () {
        // Navigate to homepage
        cy.visit('/');
    });

    it('Checking elements exist in the nav bar', function () {
        // Check nav bar elements
        cy.get('nav');
        cy.contains('Home');
        cy.contains('Timetable');
        cy.contains('Facilities');
        cy.contains('Bookings');
        cy.contains('Membership');
    });

    it("Checking elements exist in the footer", function () {
        // Check footer elements
        cy.get('div[class="footer-box"]');
        cy.contains('Logo');
        cy.contains('Slogan');
        cy.contains('Brief About Us');
        cy.contains('Find Us');
        cy.contains('Contact Details');
    });

    it("Check navigation is functional", function () {
        // Click on booking navigation button
        cy.get('a[href="/bookings"]').click();

        // Check navigation is successful
        cy.url().should('eq', 'http://localhost:8081/bookings');

        // Take a screenshot
        cy.screenshot();

        // Return to home page
        cy.go('back');

        // Check navigation is successful
        cy.url().should('eq', 'http://localhost:8081/');
    });
});