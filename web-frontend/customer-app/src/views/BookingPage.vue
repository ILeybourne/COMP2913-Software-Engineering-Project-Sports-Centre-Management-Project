<template>
  <div class="booking-container" onload="callApi">
    <div>
      {{ facility }}
    </div>
<!--    <button @click="callApi">Call</button>-->
    <pre>{{ JSON.stringify(message) }}</pre>
    <div class="padding-div">
      <button
        type="button"
        class="btn btn-outline-secondary"
        style="padding:15px;"
      >
        Back
      </button>
    </div>
    <div class="heading-div">
      <h1>Bookings</h1>
    </div>
    <div>
      <BookingInformation :facilities="facility" :content="contents"></BookingInformation>
    </div>
  </div>
</template>

<style scoped>
.padding-div {
  padding: 15px;
}

.heading-div {
  margin: auto;
  width: 50%;
}
</style>

<script>
import BookingInformation from "@/components/BookingInformation.vue";
// import axios from "axios";

// @ is an alias to /src
export default {
  name: "BookingPage",
  components: {
    BookingInformation
  },
  // data() {
  //   return {
  //     message: "",
  //     contents: "",
  //     facility: ""
  //   };
  // },
  // methods: {
  //   async callApi() {
  //     const token = await this.$auth.getTokenSilently();
  //
  //     const { data } = await axios.get("http://localhost:8000/resources", {
  //       headers: {
  //         Authorization: `Bearer ${token}`
  //       }
  //     });
  //     console.log(data);
  //
  //     this.message = data;
  //
  //     const content = data.content;
  //     const facils = [];
  //
  //     for (const facil in content){
  //       console.log(content[facil].name);
  //       facils.push(content[facil].name);
  //
  //     }
  //
  //     console.log(content);
  //     this.facilities = facils;
  //     this.contents = content;
  //     console.log(facils);
  //   }
  //
  // }
};


</script>
