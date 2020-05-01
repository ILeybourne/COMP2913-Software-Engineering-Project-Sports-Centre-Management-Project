<template>
  <div class="container-flex">
    <h2>Unauthorised</h2>
    <div class="row">
      <div class="col">
        User {{ user.nickname }} does not have sufficient permissions to view
        this page.<br />If this is a mistake, contact us directly using the
        details listed below.
      </div>
    </div>
  </div>
</template>

<script>
import { mapGetters } from "vuex";

export default {
  name: "PageNotFound",
  computed: {
    ...mapGetters("auth", ["user"])
  }
};
</script>

<style scoped></style>
