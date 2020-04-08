describe("TestFacilities", () => {
    it("Navigate to homepage", function() {
        // Navigate to homepage
        cy.navigateToHome();

    });
    it("Navigate to facility page", function() {
        cy.visit("/facilities");
    });

    it("Navigate to facility page", function() {
        cy.get("#facilityComponent");
        //click button
        cy.get("#facilityComponent").get("#facilityButton").click();
    });

});
