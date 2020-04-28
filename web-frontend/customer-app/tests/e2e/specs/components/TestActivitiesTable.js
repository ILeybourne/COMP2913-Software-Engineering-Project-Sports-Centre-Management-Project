describe("TestActivitiesTable", () => {
  it("Navigate to booking table page", function() {
    cy.visit("/activitiestable");
  });
  it("Check headers exist", function() {
    cy.get("table").contains("th", "Activity");
    cy.get("table").contains("th", "Capacity");
    cy.get("table").contains("th", "Facility");
    cy.get("table").contains("th", "Cost");
    cy.get("table").contains("th", "Actions");
  });
});
