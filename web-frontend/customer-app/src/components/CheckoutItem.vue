<template>
  <v-container class="item-details"
    ><v-row
      ><v-col
        ><h3>{{ membershipDetails.type }} details</h3></v-col
      ></v-row
    >
    <v-row
      ><v-col>sale: </v-col> <v-col>{{ membershipDetails.name }}</v-col></v-row
    >
    <v-row
      ><v-col>Starts: </v-col>
      <v-col>{{ membershipDetails.startDate }}</v-col></v-row
    >
    <v-row
      ><v-col>Expires: </v-col>
      <v-col>{{ membershipDetails.endDate }}</v-col></v-row
    >
    <v-row
      ><v-col>Cost: </v-col> <v-col>{{ membershipDetails.cost }}</v-col></v-row
    >
    <v-row
      ><v-col>Automatic Renewal? </v-col>
      <v-col>{{ membershipDetails.repeatingPayment }}</v-col></v-row
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
      membershipDetails: {
        type: null,
        id: null,
        name: null,
        startDate: null,
        endDate: null,
        cost: null,
        repeatingPayment: null
      }
    };
  },
  computed: {},
  methods: {
    setMembershipDetails() {
      this.membershipDetails.type = this.$route.params.newMembership.type;
      this.membershipDetails.id = this.$route.params.newMembership.id;
      this.membershipDetails.name = this.$route.params.newMembership.name;
      this.membershipDetails.startDate = this.$route.params.newMembership.startDate;
      this.membershipDetails.endDate = this.$route.params.newMembership.endDate;
      this.membershipDetails.cost = this.$route.params.newMembership.amount;
      this.membershipDetails.repeatingPayment = this.$route.params.newMembership.repeatingPayment;
    }
  },
  async mounted() {
    if (this.$route.params.newMembership !== null) {
      this.setMembershipDetails();
    }
  }
};
</script>
