<template>
  <div class="payment-container">
    <div class="heading-div">
      <h1><span>Membership</span> Options</h1>
      <p>
        All our memberships include access to the facilities and activities we
        we offer at a reduced rate, with the option to book onto regular
        sessions at a discounted price.
      </p>
    </div>
    <div>
      <MembershipOptions></MembershipOptions>
    </div>
  </div>
</template>

<style scoped>
.padding-div {
  padding: 15px;
}
.heading-div{
  padding: 10px;
  margin: auto;
}
@media screen and (max-width: 600px) {
  .heading-div h1 {
    font-size: 10vw;
  }
}
.heading-div h1{
  width: 50%;
  margin: auto;
}
.heading-div p{
  width: 100%;
  padding: 10px;
}
.heading-div span{
  background: #fcff18;
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
