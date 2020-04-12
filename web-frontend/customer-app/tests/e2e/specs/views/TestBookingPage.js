describe("TestBookingPage", () => {
  it("Navigate to homepage", function() {
    // Navigate to homepage
    cy.navigateToHome();
  });

  it("should navigate to booking page", function() {
    // Navigate to homepage
    cy.visit("/bookings");
  });

  it("should enter information into booking information component", function() {
    // Enter information

    cy.get('select[id="facility"] > option')
      .eq(2)
      .then(element => cy.get('select[id="facility"]').select(element.val()));

    // const activities = await cy.request("/activitytypes").its("body")
    // cy.log(activities)

    cy.get('select[id="activity"] > option')
      .eq(1)
      .then(element => cy.get('select[id="activity"]').select(element.val()));

    cy.get('input[id="date"]')
      .type("2020-04-05")
      .trigger("change");
    //type("01011970").type('{enter}');

    // cy.get('select[id="time"]').type("13:00");
    cy.get('select[id="time"] > option')
      .eq(1)
      .then(element => cy.get('select[id="time"]').select(element.val()));

    // cy.get('input[id="price"]').type("£10.00");
  });

  it("should submit booking information and fill guest", function() {
    // Click checkout with guest
    cy.contains("Checkout With Account").click();
      cy.get("#firstName").type("Izzy");
      cy.get("#surname").type("Leybourne");
      cy.get("#email").type("Izzy@email.com");
      cy.get("#phone").type("07912345678");
      cy.get("#health").type("N/A");

  });

  it("should submit guest information and fill card", function() {
    // Click checkout with guest
    cy.get("#guestSubmitBtn").click();
    cy.getWithinIframe('[name="cardnumber"]').type('4242424242424242');
    cy.getWithinIframe('[name="exp-date"]').type('1232');
    cy.getWithinIframe('[name="cvc"]').type('987');
    cy.getWithinIframe('[name="postal"]').type('12345');

  });

  it('should submit payment', function () {
    cy.get('#paymentButton').click()
  });


});
