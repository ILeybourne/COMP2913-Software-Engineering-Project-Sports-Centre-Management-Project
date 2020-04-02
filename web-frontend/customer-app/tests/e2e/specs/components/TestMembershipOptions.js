describe("TestMembershipOptions", () => {
  it("Navigate to membership page", function() {
    cy.visit("/membership");
  });

  it("Check headings exist", () => {
    cy.get('div[class="annual-membership-details"]').contains(
      "Annual Membership Details"
    );
    cy.get('div[class="monthly-membership-details"]').contains(
      "Monthly Membership Details"
    );
    cy.get('div[class="account-creation-form"]').contains(
      "Account Creation Form"
    );
  });
});
