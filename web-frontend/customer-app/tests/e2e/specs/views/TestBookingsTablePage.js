describe("TestBookingsTablePage", () => {
  it("Navigate to homepage", function() {
    // Navigate to homepage
    cy.navigateToHome();
  });

  it("select item drop down", function(){
    //select item dropdown
    cy.get('.items-per-page-dropdown').click();
  });
});
