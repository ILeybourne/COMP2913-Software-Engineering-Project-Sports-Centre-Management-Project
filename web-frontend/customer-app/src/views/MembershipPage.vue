<template>
  <div class="payment-container">
    <div class="heading-div">
      <h1>Membership Options</h1>
    </div>
    <div>
      <MembershipOptions></MembershipOptions>
    </div>
  </div>
</template>

<style scoped>

.heading-div {
  margin: auto;
  width: 50%;
}
</style>

<script>
import MembershipOptions from "@/components/MembershipOptions.vue";

// @ is an alias to /src
export default {
  name: "MembershipPage",
  components: {
    MembershipOptions
  }
};
</script>
