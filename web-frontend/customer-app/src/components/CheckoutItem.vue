<template>
  <v-container class="item-details"
    ><div v-if="isMembership || isBooking">
      <v-row
        ><v-col align="center"><h3>Sale details</h3></v-col></v-row
      >
      <hr v-if="isBooking" />
      <v-row v-if="isBooking"
        ><v-col>Facility: </v-col>
        <v-col>{{ bookingDetails.facility }} </v-col></v-row
      >
      <hr v-if="isBooking || isMembership" />
      <v-row
        ><v-col v-if="isMembership">Item: </v-col>
        <v-col v-if="isBooking">Activity: </v-col>
        <v-col
          >{{ membershipSaleDetails.name || bookingDetails.activity }}
          <p v-if="isMembership">Membership</p></v-col
        ></v-row
      >
      <hr v-if="isBooking || isMembership" />

      <v-row
        ><v-col>Starts: </v-col>
        <v-col>{{
          membershipSaleDetails.startDate ||
            bookingDetails.time + " " + bookingDetails.date
        }}</v-col></v-row
      >
      <hr v-if="isBooking || isMembership" />

      <v-row v-if="isMembership"
        ><v-col>Expires: </v-col>
        <v-col>{{ membershipSaleDetails.endDate }}</v-col></v-row
      >
      <hr v-if="isMembership" />

      <v-row
        ><v-col>Cost: </v-col>
        <v-col
          >£{{ membershipSaleDetails.cost || bookingDetails.price }}</v-col
        ></v-row
      >
      <hr v-if="isBooking || isMembership" />

      <v-row
        ><v-col v-if="isMembership">Automatic Renewal? </v-col>
        <v-col v-if="isBooking">Participants: </v-col>
        <v-col>{{
          membershipSaleDetails.repeatingPayment || bookingDetails.participants
        }}</v-col></v-row
      >
      <hr v-if="isMembership || isBooking" />
    </div>
    <div v-if="isBooking == false && isMembership == false">
      <v-row
        ><v-col align="center" class="heading"
          ><h3>Sale details</h3></v-col
        ></v-row
      >
      <div>
        <v-row>
          <v-col class="emptyCart">
            <h4>Your cart is empty.</h4>
          </v-col>
        </v-row>
      </div>
      <div class="btnDiv">
        <b-row>
          <b-col class="emptyCartButtons">
            <div>
              <button
                type="button"
                class="btn btn-outline-primary"
                @click="routerPushBookingCreate"
              >
                Create New Booking
              </button>
            </div> </b-col
          ><b-col class="emptyCartButtons">
            <div>
              <button
                type="button"
                class="btn btn-outline-primary"
                @click="routerPushMembershipPage"
              >
                Purchase Membership
              </button>
            </div>
          </b-col>
        </b-row>
      </div>
    </div>
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
  width: 80%;
}

.item-details:hover h3 {
  background: #fcff18;
}

.heading {
  margin-bottom: 150px;
}

.emptyCart {
  margin-bottom: 160px;
}
.emptyCartButtons {
  padding: 0px;
}
button {
  width: 70%;
}

.btnDiv {
  /*margin-top: 200px;*/
}
</style>

<script>
export default {
  name: "CheckoutItem",
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
        price: null,
        participants: null
      },
      isBooking: false,
      isMembership: false
    };
  },
  computed: {},
  methods: {
    routerPushBookingCreate() {
      this.$router.push({
        name: "BookingPage"
      });
    },
    routerPushMembershipPage() {
      this.$router.push({
        name: "MembershipPage"
      });
    },
    setBookingDetails() {
      this.bookingDetails.facility = this.$route.params.bookingDetails.facility;
      this.bookingDetails.activity = this.$route.params.bookingDetails.activity;
      this.bookingDetails.date = this.$route.params.bookingDetails.date;
      this.bookingDetails.time = this.$route.params.bookingDetails.time;
      this.bookingDetails.price = this.$route.params.bookingDetails.price;
      this.bookingDetails.participants = this.$route.params.bookingDetails.participants;
      this.isBooking = true;
      this.isMembership = false;
    },

    setMembershipDetails() {
      this.membershipSaleDetails.name = this.$route.params.membershipDetails.name;
      this.membershipSaleDetails.startDate = this.$route.params.membershipDetails.startDate;
      this.membershipSaleDetails.endDate = this.$route.params.membershipDetails.endDate;
      this.membershipSaleDetails.cost = this.$route.params.membershipDetails.amount;
      this.isBooking = false;
      this.isMembership = true;
      if (this.$route.params.membershipDetails.repeatingPayment !== null) {
        this.membershipSaleDetails.repeatingPayment = this.$route.params.membershipDetails.repeatingPayment;
      } else {
        this.membershipSaleDetails.repeatingPayment = false;
      }
    }
  },
  async mounted() {
    if (this.$route.params.bookingDetails != null) {
      this.setBookingDetails();
    }

    if (this.$route.params.membershipDetails != null) {
      this.setMembershipDetails();
    }
  }
};
</script>
