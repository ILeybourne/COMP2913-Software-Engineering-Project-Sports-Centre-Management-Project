<template>
  <v-container class="item-details"
    ><v-row
      ><v-col
        ><h3>Sale details</h3></v-col
      ></v-row
    >
    <v-row
      ><v-col>Item: </v-col>
      <v-col>{{ membershipSaleDetails.name }} Membership</v-col></v-row
    >
    <v-row
      ><v-col>Starts: </v-col>
      <v-col>{{ membershipSaleDetails.startDate }}</v-col></v-row
    >
    <v-row
      ><v-col>Expires: </v-col>
      <v-col>{{ membershipSaleDetails.endDate }}</v-col></v-row
    >
    <v-row
      ><v-col>Cost: </v-col> <v-col>{{ membershipSaleDetails.cost }}</v-col></v-row
    >
    <v-row
      ><v-col>Automatic Renewal? </v-col>
      <v-col>{{ membershipSaleDetails.repeatingPayment }}</v-col></v-row
    >
  </v-container>
</template>

<style scoped>
.item-details {
  margin: 20px;
  text-align: center;
  width: 20%;
  min-height: 400px;
  background: #f6f9fa;
  color: #242424;
  display: flex;
  flex-direction: column;
  justify-content: center;
  min-width: 150px;
  max-width: 400px;
  flex-basis: auto; /* default value */
  flex-grow: 1;
}

.item-details h3 {
  color: #242424;
}

.item-details:hover h3 {
  background: #fcff18;
}
</style>

<script>
export default {
  name: "Checkout",
  components: {},
  data: function() {
    return {
      membershipSaleDetails: {
        name: null,
        startDate: null,
        endDate: null,
        cost: null,
        repeatingPayment: null
      },
      bookingDetails: {
        facility: null,
        activity: null,
        date: null,
        time: null,
        price: null
      }
    };
  },
  computed: {},
  methods: {
    setBookingDetails() {
      this.bookingDetails.facility = this.$route.params.bookingDetails.facility;
      this.bookingDetails.activity = this.$route.params.bookingDetails.activity;
      this.bookingDetails.date = this.$route.params.bookingDetails.date;
      this.bookingDetails.time = this.$route.params.bookingDetails.time;
      this.bookingDetails.price = this.$route.params.bookingDetails.price;
    },

    setMembershipDetails() {
      this.membershipSaleDetails.name = this.$route.params.membershipDetails.name;
      this.membershipSaleDetails.startDate = this.$route.params.membershipDetails.startDate;
      this.membershipSaleDetails.endDate = this.$route.params.membershipDetails.endDate;
      this.membershipSaleDetails.cost = this.$route.params.membershipDetails.amount;
      if (this.$route.params.membershipDetails.repeatingPayment !== null) {
        this.membershipSaleDetails.repeatingPayment = this.$route.params.membershipDetails.repeatingPayment;
      } else {
        this.membershipSaleDetails.repeatingPayment = false;
      }
    }
  },
  async mounted() {
    if (this.$route.params.bookingDetails !== null) {
      this.setMembershipDetails();
    }

    if (this.$route.params.membershipDetails !== null) {
      this.setMembershipDetails();
    }
  }
};
</script>
