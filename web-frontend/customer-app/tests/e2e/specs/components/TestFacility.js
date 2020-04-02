describe("TestFacility", () => {
  it("Navigate to facilities page", function() {
    cy.visit("/facilities");
  });

  it("Check image exists", () => {
    cy.get('div[class="image"]').get("img");
  });

  it("Check information", () => {
    cy.get('div[class="facility-info"]').contains("info");
    cy.get('div[class="facility-info"]')
      .get("div")
      .get('button[class="btn btn-outline-secondary"]')
      .contains("Book Now");
  });
});
