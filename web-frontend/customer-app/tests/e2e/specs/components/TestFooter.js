describe("TestFooter", () => {
  it("Navigate to home page", function() {
    cy.visit("/");
  });

  it("Check column 1 elements", function() {
    cy.get("[data-cy=footerColumn1Header]").contains("About Us");
    cy.get("[data-cy=footerColumn1Body]")
      .as("column1Body")
      .contains("Brief about us paragraph.");
    cy.get("@column1Body").contains("Learn more");
    cy.get("@column1Body").contains("here");
  });

  it("Check column 2 elements", function() {
    cy.get("[data-cy=footerColumn2Header]").contains("Contact");
    cy.get("[data-cy=footerColumn2Body]")
      .as("column2Body")
      .contains("Icon"); //TODO: add icons and test them properly
    cy.get("@column2Body").contains("street+number, postcode");
    cy.get("@column2Body").contains("email");
    cy.get("@column2Body").contains("phone number");
  });

  it("Check column 3 elements", function() {
    cy.get("[data-cy=footerColumn3Header]").contains("Useful Links");
    cy.get("[data-cy=footerColumn3Body]")
      .as("column3Body")
      .contains("Timetable");
    cy.get("@column3Body").contains("Facilities");
    cy.get("@column3Body").contains("Profile");
  });

  it("Check column 4 elements", function() {
    cy.get("[data-cy=footerColumn4]")
      .as("column4")
      .contains("Awesome slogan");
    //logo is visible
    cy.get("@column4")
      .find("img")
      .should("be.visible");
  });

  it("Test links", function() {
    //test link to about page
    cy.get("[data-cy=footerColumn1Body")
      .as("links")
      .contains('a[href="/about" title="About" data-cy="footerAboutLink"]')
      .click();
    //test links in link column
    cy.get("[data-cy=footerColumn3Body")
      .as("links")
      .contains('a[href="/timetable" title="Timetable"]')
      .click();
    cy.url().should("be", "http://localhost:8080/timetable");
    cy.get("@links")
      .contains('a[href="/facilities" title="Facilities"]')
      .click();
    cy.url().should("be", "http://localhost:8080/facilities");
    cy.get("@links")
      .contains('a[href="/profile" title="Profile"]')
      .click();
    cy.url().should("be", "http://localhost:8080/profile");
    //test icon link to home page
    cy.get("[data-cy=footerColumn4")
      .contains('a[href="/" title="Home"]')
      .click();
    cy.url().should("be", "http://localhost:8080");
    //return to original page and test successful navigation
    cy.go("back");
    cy.go("back");
    cy.go("back");
    cy.go("back");
    cy.url().should("be", "http://localhost:8080");
  });
});
