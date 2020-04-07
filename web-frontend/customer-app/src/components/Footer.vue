<template>
  <footer class="container">
    <div class="row">
      <div class="col-4 text-left">
        <h6 class="text-uppercase">About Us</h6>
        <div class="row">
          <div class="col">
            <a class="text-muted">Brief about us paragraph.</a><br />
            <a class="text-muted">Learn more </a
            ><a href="/about" title="About">here</a>
          </div>
        </div>
      </div>
      <div class="col-3">
        <h6 class="text-uppercase">Contact</h6>
        <div class="row">
          <div class="col-3 text-left">
            <p><a>Email</a></p>
            <p><a>Email</a></p>
            <p><a>Phone</a></p>
          </div>
          <div class="col-9 text-left">
            <p><a class="text-muted">email</a></p>
            <p><a class="text-muted">email</a></p>
            <p><a class="text-muted">phone number</a></p>
          </div>
        </div>
      </div>
      <div class="col-2 text-left">
        <h6 class="text-uppercase">Useful Links</h6>
        <div class="row">
          <div class="col">
            <p><a href="/timetable" title="Timetable">Timetable</a></p>
            <p><a href="/facilities" title="Facilities">Facilities</a></p>
            <p><a href="/profile" title="Profile">Profile</a></p>
          </div>
        </div>
      </div>
      <div class="col-2 text-left">
        <div class="line">
          <a href="/home" title="Home">
            <div class="text-center">
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
  justify-content: space-between;
  padding: 30px;
}
</style>

<script>
export default {
  name: "Footer"
};
</script>
