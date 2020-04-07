describe("TestTimetablePage", () => {
  it("Navigate to homepage", function() {
    // Navigate to homepage
    cy.navigateToHome();
  });

  it("should navigate to timetable page", function() {
    // Navigate to homepage
    cy.visit("/timetable");
  });

  it("should book first activity in first facility", function() {
    // Enter information
    cy.get(
      '.fc-time-area > .fc-scroller-clip > .fc-scroller > .fc-scroller-canvas > .fc-content > .fc-rows > table > tbody > [data-resource-id="1"] > .fc-widget-content '
    ).click("bottom");
    cy.get("#activitySelect > option")
      .eq(1)
      .then(element =>
        cy.get('select[id="activitySelect"]').select(element.val())
      );
    cy.get(".btn-primary").click();
  });
});
