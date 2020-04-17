<template>
  <footer class="footer-container container-fluid">
    <div class="row">
      <div class="column-1 col-sm-5 text-left">
        <h6 class="text-uppercase" data-cy="footerColumn1Header">About Us</h6>
        <div class="row">
          <div id="about-us-paragraph" data-cy="footerColumn1Body" class="col">
            <a class="text-muted"
              >We exist to make sporting services and spaces better for
              everyone. That means investing back into providing quality leisure
              and health facilities for the good of the communities where we
              operate. We are always looking to improve the services and
              facilities we offer and for ways in which it can contribute
              positively to the local community.</a
            ><br />
            <a class="text-muted">Learn more </a
            ><a href="/about" title="About" data-cy="footerAboutLink">here</a>
          </div>
        </div>
      </div>
      <div class="column-2 col-sm-3">
        <h6 class="text-uppercase" data-cy="footerColumn2Header">Contact</h6>
        <div class="row" data-cy="footerColumn2Body">
          <div class="link-icons col-2 text-left">
            <!-- add proper icons -->
            <p><a>Icon</a></p>
            <p><a>Icon</a></p>
            <p><a>Icon</a></p>
          </div>
          <div class="col-10 text-left">
            <p><a class="text-muted">city, postcode, door number</a></p>
            <p><a class="text-muted">email@address.dom</a></p>
            <p><a class="text-muted">phone number</a></p>
          </div>
        </div>
      </div>
      <div class="column-3 col-sm-2 text-left">
        <h6 class="text-uppercase" data-cy="footerColumn3Header">
          Useful Links
        </h6>
        <div class="row">
          <div class="col">
            <div data-cy="footerColumn3Body">
              <p><a href="/timetable" title="Timetable">Timetable</a></p>
              <p><a href="/facilities" title="Facilities">Facilities</a></p>
              <p><a href="/profile" title="Profile">Profile</a></p>
            </div>
          </div>
        </div>
      </div>
      <div class="column-4 col-sm-2 text-left" data-cy="footerColumn4">
        <div class="line">
          <a href="/" title="Home">
            <div class="image text-center">
              <img src="../assets/logo.png" style="height: 64px; width: 64px" />
            </div>
          </a>
        </div>
        <div class="text-center text-muted">
          <em>Awesome slogan</em>
        </div>
      </div>
    </div>
  </footer>
</template>

<style scoped>
.footer-container {
  display: flex;
  padding: 5%;
}
.column-1 {
  padding-right: 5%;
  min-width: 350px;
}
#about-us-paragraph {
}
.column-2 {
  min-width: 350px;
  padding-right: 3.5%;
}
.column-3 {
  min-width: 200px;
}
.column-4 {
  min-width: 120px;
}
.link-icons {
}
</style>

<script>
export default {
  name: "Footer"
};
</script>
