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
              and health facilities for the good of the communities in which we
              operate. We are always looking to improve our services, and to
              give everything we can back to the community.</a
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
            <p><font-awesome-icon icon="home" /></p>
            <p><font-awesome-icon icon="at" /></p>
            <p><font-awesome-icon icon="phone-alt" /></p>
          </div>
          <div class="col-10 text-left">
            <p><a class="text-muted">Headingley Lane, Leeds LS6 2BT </a></p>
            <p><a class="text-muted">customerservice@zenergy.com</a></p>
            <p><a class="text-muted">0113 343 2401</a></p>
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
        <div id="slogan" class="text-center text-muted">
          <em>Respire, perspire,<br />ingest, retire</em>
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
#slogan {
}
.link-icons {
}
</style>

<script>
import { library } from "@fortawesome/fontawesome-svg-core";
import { faHome } from "@fortawesome/free-solid-svg-icons";
import { faAt } from "@fortawesome/free-solid-svg-icons";
import { faPhoneAlt } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";
export default {
  name: "Footer",
  components: {
    "font-awesome-icon": FontAwesomeIcon
  },
  created: function() {
    library.add(faHome);
    library.add(faAt);
    library.add(faPhoneAlt);
  }
};
</script>
