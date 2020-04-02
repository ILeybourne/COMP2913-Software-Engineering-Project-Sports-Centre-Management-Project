describe("TestBookingPage", () => {
  it("Navigate to homepage", function() {
    // Navigate to homepage
    cy.navigateToHome();
  });

  it("Enter information into text boxes", function() {
    // Enter information
    cy.get('input[id="facility"]').type("Facility Name");
    cy.get('input[id="activity"]').type("Activity Name");
    cy.get('input[id="date"]').type("01/01/1970");
    cy.get('input[id="time"]').type("13:00");
    cy.get('input[id="price"]').type("£10.00");
  });

  it("Submit information", function() {
    // Click checkout with guest
    cy.contains("Checkout With Account").click();
  });
});
