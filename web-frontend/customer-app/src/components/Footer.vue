<template>
  <footer class="container-fluid">
    <div class="row">
      <div class="col-md-3 text-left">
        <svg
          xmlns="http://www.w3.org/2000/svg"
          width="48"
          height="48"
          viewBox="0 0 48 48"
          fill="none"
          stroke="currentColor"
          stroke-width="2"
          stroke-linecap="round"
          stroke-linejoin="round"
          class="d-block mb-2"
        >
          <circle cx="24" cy="24" r="20"></circle>
        </svg>
        <a>
          "We're better than you, and we know it!"
        </a>
        <nav aria-label="Page navigation example">
          <ul class="pagination">
            <li class="page-item"><a class="page-link" href="#">About us</a></li>
          </ul>
        </nav>
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
