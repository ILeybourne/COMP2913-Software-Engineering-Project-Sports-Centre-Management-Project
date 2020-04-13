<template>
  <div class="profile-container">
    <div class="row">
      <div class="left-column col-sm-6">
        <div class="profile-picture-container">
          <img :src="user.picture" class="profile-picture rounded" />
        </div>
        <div>
          <h2>{{ $auth.user.name }}</h2>
          <p>{{ $auth.user.email }}</p>
        </div>
      </div>
      <div class="right-column col-sm-6">
        <pre>{{ JSON.stringify(user, null, 2) }}</pre>
      </div>
    </div>
  </div>
</template>

<script>
import { mapGetters } from "vuex";

export default {
  name: "Profile",
  computed: {
    ...mapGetters("auth", ["user"])
  }
};
</script>

<style scoped>
.profile-container {
  margin: 5%;
}
.left-column {
  min-width: 350px;
}
.profile-picture-container {
  padding-bottom: 24px;
}
.profile-picture {
  width: 200px;
}
.right-column {
}
</style>
