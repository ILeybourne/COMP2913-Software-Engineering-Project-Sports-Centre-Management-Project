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
      <MembershipOptions
        @submitCustomerDetails="receiveSubscriptionInfo"
      ></MembershipOptions>
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
  },
  data: function() {
    return {
      subscriptionType: null
    };
  },

  methods: {
    receiveSubscriptionInfo(value) {
      this.subscriptionType = value.pickedSubscription;
      this.firstName = value.firstName;
      this.surname = value.surname;
      this.email = value.email;
      this.phone = value.phone;
    }
  }
};
</script>
