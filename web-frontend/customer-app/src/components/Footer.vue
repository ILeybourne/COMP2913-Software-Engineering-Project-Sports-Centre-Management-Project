<template>
  <footer class="container-fluid">
    <div class="row">
      <div class="col-md-3 text-left">
        <div class="line">
          <a href="/home" title="Home">
            <img src="../assets/logo.png" style="height: 48px; width: 48px" />
          </a>
        </div>
        <div class="line">
          <em>
            "We're better than you, and we know it!"
          </em>
        </div>
        <div class="line">
          <a>
            Dedicated to your sporting experience.
          </a>
        </div>
        <div class="line">
          <a>Learn more</a>
          <a href="/about" title="About">
            about us
          </a>
        </div>
      </div>
      <div class="col-md-6 text-center">
        <h6>Find Us</h6>
      </div>
      <div class="col-md-3 text-right">
        <h6>Contacts:</h6>
        <ul class="list-unstyled text-small">
          <li>
            <a class="text-muted">street address</a>
          </li>
          <li>
            <a class="text-muted">email address</a>
          </li>
          <li>
            <a class="text-muted">website</a>
          </li>
          <li>
            <a class="text-muted">phone number</a>
          </li>
        </ul>
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
