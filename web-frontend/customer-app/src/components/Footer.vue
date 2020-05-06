<template>
  <div>
    <footer class="footer-container container-fluid">
      <div class="row">
        <div class="column-1 col-sm-5 text-left">
          <h6 class="text-uppercase" data-cy="footerColumn1Header">About Us</h6>
          <div class="row">
            <div
              id="about-us-paragraph"
              data-cy="footerColumn1Body"
              class="col"
            >
              <p class="text-muted">
                We exist to make sporting services and spaces better for
                everyone. That means investing back into providing quality
                leisure and health facilities for the good of the communities in
                which we operate. We are always looking to improve our services,
                and to give everything we can back to the community.
              </p>
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
                <p><router-link to="/timetable">Timetable</router-link></p>
                <p><router-link to="/facilities">Facilities</router-link></p>
                <p><router-link to="/profile">Profile</router-link></p>
              </div>
            </div>
          </div>
        </div>
        <div class="column-4 col-sm-2 text-left" data-cy="footerColumn4">
          <div class="line">
            <router-link to="/" title="Home">
              <div class="image text-center">
                <img
                  src="../assets/logo.png"
                  style="height: 63.6px; width: 198.6px"
                />
              </div>
            </router-link>
          </div>
          <div id="slogan" class="text-center text-muted">
            <em>Respire, perspire,<br />ingest, retire</em>
          </div>
        </div>
      </div>
    </footer>
    <div class="row">
      <div class="col-md-12 order-2 order-md-1">
        <div class="copyright">
          <!-- Link back to Colorlib can't be removed. Template is licensed under CC BY 3.0. -->
          <p style="text-align: center">
            Copyright &copy; {{ year }} All rights reserved | This template is
            made with <i class="fa fa-heart" aria-hidden="true"></i> by
            <a href="https://colorlib.com" rel="noopener" target="_blank">Colorlib</a>
          </p>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.copyright {
  margin: auto;
}

.footer-container {
  display: flex;
  padding-top: 5%;
  padding-left: 5%;
  padding-right: 5%;
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
  computed: {
    year: function() {
      return new Date().getFullYear();
    }
  },
  created: function() {
    library.add(faHome);
    library.add(faAt);
    library.add(faPhoneAlt);
  }
};
</script>
