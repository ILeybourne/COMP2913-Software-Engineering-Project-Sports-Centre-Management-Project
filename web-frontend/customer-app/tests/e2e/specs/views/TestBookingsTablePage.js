describe("TestBookingTablePage", () => {
  it("Navigate to booking table page", function() {
    cy.visit("/bookingtable");
  });
  it("Check header buttons work", function() {
    cy.get("i").click({ force: true });
  });
});
