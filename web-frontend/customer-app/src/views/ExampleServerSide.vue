<template>
  <div class="example">
    <button @click="callApi">Call</button>
    <pre>{{ JSON.stringify(message) }}</pre>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "ExampleServerSide",
  components: {},
  data() {
    return {
      message: ""
    };
  },
  methods: {
    async callApi() {
      const token = await this.$auth.getTokenSilently();

      const { data } = await axios.get("http://localhost:8000/resources", {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      this.message = data;
    }
  }
};
</script>
