<template>
  <div class="container-flex">
    <h1>Payment <span>Successful</span></h1>
    <div class="row">
      <div
        class="col"
        v-bind:class="{ 'd-none': hideBookingDiv, col: !hideBookingDiv }"
        v-if="!hideBookingDiv"
      >
        <v-container class="checkout-container" id="billing-details"
          ><v-row
            ><v-col><h3>Your Booking has been placed</h3> </v-col></v-row
          >
          <hr />
          <v-row
            ><v-col>Facility: </v-col>
            <v-col>{{ bookingDetails.facility || null }}</v-col></v-row
          >
          <hr />
          <v-row
            ><v-col>Activity: </v-col>
            <v-col>{{ bookingDetails.activity || null }} </v-col></v-row
          >
          <hr />
          <v-row
            ><v-col>Date: </v-col>
            <v-col>{{ bookingDetails.date || null }}</v-col></v-row
          >
          <hr />
          <v-row
            ><v-col>Time: </v-col>
            <v-col>{{ bookingDetails.time || null }}</v-col></v-row
          >
          <hr />
          <v-row
            ><v-col>Price: </v-col>
            <v-col>£{{ bookingDetails.price || null }}</v-col></v-row
          >
          <hr />
          <b-row>
            <b-col>
              <div>
                <button
                  type="button"
                  class="btn btn-outline-secondary"
                  @click="routerPushBookingCreate"
                >
                  Create New Booking
                </button>
              </div> </b-col
            ><b-col v-bind:class="{ 'd-none': !isUser }">
              <div>
                <button
                  type="button"
                  class="btn btn-outline-secondary"
                  @click="routerPushBookingManage"
                >
                  View My Bookings
                </button>
              </div>
            </b-col>
          </b-row>
        </v-container>
      </div>
      <div
        v-bind:class="{ 'd-none': hideMembershipDiv, col: !hideMembershipDiv }"
        v-if="!hideMembershipDiv"
      >
        <v-container class="checkout-container" id="billing-details"
          ><v-row
            ><v-col
              ><h3>You are now a member of <span>Zenergy</span></h3>
            </v-col></v-row
          >
          <hr />
          <v-row
            ><v-col>Subscription Type: </v-col>
            <v-col>{{ membershipSaleDetails.name || null }}</v-col></v-row
          >
          <hr />
          <v-row
            ><v-col>Start Date: </v-col>
            <v-col>{{ membershipSaleDetails.startDate || null }} </v-col></v-row
          >
          <hr />
          <v-row
            ><v-col>End Date: </v-col>
            <v-col>{{ membershipSaleDetails.endDate || null }}</v-col></v-row
          >
          <hr />
          <v-row
            ><v-col>Price: </v-col>
            <v-col>£{{ membershipSaleDetails.cost || null }}</v-col></v-row
          >
          <hr />
          <b-row>
            <b-col>
              <div>
                <button
                  type="button"
                  class="btn btn-outline-secondary"
                  @click="routerPushBookingCreate"
                >
                  Create New Booking
                </button>
              </div> </b-col
            ><b-col v-bind:class="{ 'd-none': !isUser }">
              <div>
                <button
                  type="button"
                  class="btn btn-outline-secondary"
                  @click="routerPushBookingManage"
                >
                  View My Bookings
                </button>
              </div>
            </b-col>
          </b-row>
        </v-container>
      </div>
    </div>
  </div>
</template>
<style scoped>
.checkout-container {
  display: flex;
  flex-direction: column;
  height: auto;
  width: auto;
  background: #f6f9fa;
  color: #242424;
  justify-content: center;
  flex-basis: auto; /* default value */
  flex-grow: 1;
  width: 50%;
  margin-right: auto;
  margin-top: 10%;
  -webkit-box-shadow: 10px 10px 24px 5px rgba(0, 0, 0, 0.2);
  -moz-box-shadow: 10px 10px 24px 5px rgba(0, 0, 0, 0.2);
  box-shadow: 10px 10px 24px 5px rgba(0, 0, 0, 0.2);
}

.checkout-container h3 {
  color: #242424;
  text-align: center;
}

.col {
  text-align: center;
}
button {
  width: 80%;
}

h1 {
  text-align: center;
}

span {
  background: #fcff18;
}
</style>
<script>
import { mapGetters } from "vuex";
import { isEmpty } from "../util/session.helpers";

export default {
  name: "PaymentSuccess",
  computed: {
    ...mapGetters("auth", ["user"])
  },
  data() {
    return {
      bookingDetails: null,
      membershipSaleDetails: null,
      paymentResponse: null,
      formData: null,
      postMembershipResponse: null,
      hideBookingDiv: true,
      hideMembershipDiv: true,
      isUser: null
    };
  },
  methods: {
    setData() {
      if (!isEmpty(this.$route.params)) {
        this.bookingDetails = this.$route.params.paymentSuccessData.bookingDetails;
        this.membershipSaleDetails = this.$route.params.paymentSuccessData.membershipSaleDetails;
        this.paymentResponse = this.$route.params.paymentSuccessData.paymentResponse;
        this.formData = this.$route.params.paymentSuccessData.formData;
        this.postMembershipResponse = this.$route.params.paymentSuccessData.postMembershipResponse;
      }
    },
    routerPushBookingCreate() {
      this.$router.push({
        name: "BookingPage"
      });
    },
    routerPushBookingManage() {
      this.$router.push({
        name: "BookingTable"
      });
    }
  },
  mounted() {
    this.setData();
    this.isUser = !isEmpty(this.user);
    if (this.paymentResponse != null) {
      if (this.bookingDetails.activityTypeId != null) {
        this.hideBookingDiv = false;
      }
      if (this.membershipSaleDetails.id != null) {
        this.hideMembershipDiv = false;
      }
    } else {
      this.$router.push({
        name: "Unauthorised"
      });
    }
  }
};
</script>
