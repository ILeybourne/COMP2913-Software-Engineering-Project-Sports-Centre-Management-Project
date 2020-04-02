<template>
  <div class="about">
    <p>{{ $auth.loading }} {{ loading }}</p>
    <p>{{ $auth.user }} {{ user }}</p>
    <p>{{ $auth.isAuthenticated }} {{ isAuthenticated }}</p>
    <pre>
    {{ JSON.stringify($auth.auth0Client) }}
    </pre>
  </div>
</template>
<script>
import { mapGetters } from "vuex";

export default {
  name: "Test",
  computed: {
    ...mapGetters({
      loading: "loading.loading",
      user: "auth.user",
      isAuthenticated: "auth.isAuthenticated"
    })
  }
};
</script>
