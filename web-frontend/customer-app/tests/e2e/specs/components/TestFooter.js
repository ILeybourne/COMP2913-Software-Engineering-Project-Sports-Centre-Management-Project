describe("TestFooter", () => {
    it("Navigate to home page", function () {
        cy.visit('/');
    });

    it("Check left hand side", () => {
        cy.get('div[class="footer-container"]').get('div[class="footer-box"]').contains("Logo");
        cy.get('div[class="footer-container"]').get('div[class="footer-box"]').contains("Slogan");
        cy.get('div[class="footer-container"]').get('div[class="footer-box"]').contains("Brief About Us");
    });

    it("Check center", () => {
        cy.get('div[class="footer-box"]').contains("Find Us");
    });

    it("Check right hand side", () => {
        cy.get('div[class="footer-box"]').contains("Contact Details");
    });
});