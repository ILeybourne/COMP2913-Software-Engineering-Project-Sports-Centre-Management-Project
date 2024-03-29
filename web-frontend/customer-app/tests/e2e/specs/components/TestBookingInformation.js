describe("TestBookingInformation", () => {
  it("Navigate to home page", function() {
    cy.visit("/bookings");
  });

  it("Check labels exist", function() {
    cy.get('label[for="facility"]').contains("Facility");
    cy.get('label[for="activity"]').contains("Activity");
    cy.get('label[for="selectedDate"]').contains("Date");
    cy.get('label[for="timeOptions"]').contains("Time");
    cy.get('label[for="price"]').contains("Price");
  });

  it("Check input boxes exist", function() {
    cy.get('select[id="facility"]');
    cy.get('select[id="activity"]');
    cy.get('input[id="selectedDate"]');
    cy.get('select[id="timeOptions"]');
    cy.get('input[id="price"]');
  });

  it("Check button text", function() {
    cy.get('button[class="btn btn-outline-secondary"').contains(
      "Checkout As Guest"
    );
    cy.get('button[class="btn btn-outline-primary"').contains(
      "Checkout With Account"
    );
  });
});
