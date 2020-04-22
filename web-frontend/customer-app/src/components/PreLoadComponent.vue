<template>
  <div class="container h-100">
    <div class="row h-100 justify-content-center align-items-center">
      <div class="spinner-border" role="status">
        <span class="sr-only">Loading...</span>
      </div>
    </div>
  </div>
</template>
<style scoped>
* {
  padding: 0;
  margin: 0;
}
html,
body {
  min-height: 100% !important;
  height: 100%;
}
</style>
<script>
export default {
  name: "PreLoadSpinner"
};
</script>
