<template>
  <footer class="footer-container">
    <div class="row">
      <div class="column-1 col-5 text-left">
        <h6 class="text-uppercase" data-cy="footerColumn1Header">About Us</h6>
        <div class="row">
          <div class="col" data-cy="footerColumn1Body">
            <a class="text-muted"
              >Brief about us paragraph. #### #### #### #### #### #### #### ####
              #### #### #### #### #### #### #### #### #### #### #### #### ####
              #### #### #### #### #### ####</a
            ><br />
            <a class="text-muted">Learn more </a
            ><a href="/about" title="About" data-cy="footerAboutLink">here</a>
          </div>
        </div>
      </div>
      <div class="column-2 col-3">
        <h6 class="text-uppercase" data-cy="footerColumn2Header">Contact</h6>
        <div class="row" data-cy="footerColumn2Body">
          <div class="link-icons col-2 text-center">
            <!-- add proper icons -->
            <p><a>Icon</a></p>
            <p><a>Icon</a></p>
            <p><a>Icon</a></p>
          </div>
          <div class="col-10 text-left">
            <p><a class="text-muted">street+number, postcode</a></p>
            <p><a class="text-muted">email</a></p>
            <p><a class="text-muted">phone number</a></p>
          </div>
        </div>
      </div>
      <div class="column-3 col-2 text-left" style="padding-left: 24px">
        <h6 class="text-uppercase" data-cy="footerColumn3Header">
          Useful Links
        </h6>
        <div class="row" data-cy="footerColumn3Body">
          <div class="col">
            <p><a href="/timetable" title="Timetable">Timetable</a></p>
            <p><a href="/facilities" title="Facilities">Facilities</a></p>
            <p><a href="/profile" title="Profile">Profile</a></p>
          </div>
        </div>
      </div>
      <div
        class="column-4 col-2 text-left"
        style="padding-top: 31px"
        data-cy="footerColumn4"
      >
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
  padding: 48px;
}
.column-1 {
  padding-right: 128px;
}
.column-2 {
}
.column-3 {
}
.column-4 {
}
.link-icons {
  padding-left: 0px;
  padding-right: 0px;
}
</style>

<script>
export default {
  name: "Footer"
};
</script>
