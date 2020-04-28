describe("TestBookingTable", () => {
  it("Navigate to booking table page", function() {
    cy.visit("/bookingtable");
  });
  it("Check headers exist", function() {
    cy.get("table").contains("th", "Booking Reference");
    cy.get("table").contains("th", "Booking Time");
    cy.get("table").contains("th", "Booking");
    cy.get("table").contains("th", " Facility");
    cy.get("table").contains("th", "Actions");
  });
});
