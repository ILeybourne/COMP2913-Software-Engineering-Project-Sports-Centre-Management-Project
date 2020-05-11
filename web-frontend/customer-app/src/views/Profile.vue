<template>
  <div>
    <div class="heading-div">
      <h1>My <span>Account</span></h1>
    </div>
    <div id="profile-container" class="row">
      <div id="left-column" class="col-sm-7">
        <div id="profile-info-container" class="row">
          <div id="picture-col" class="col-xl-5 text-center">
            <img
              :src="user.picture"
              id="profile-picture"
              class="rounded rounded-circle border-bottom border-dark"
              alt="Profile Picture"
            />
          </div>
          <div id="info-col" class="col-xl-7">
            <h2>{{ user.nickname }}</h2>
            <ul id="user-info" class="container list-unstyled">
              <li>{{ $auth.user.name }}</li>
              <li>{{ $auth.user.email }}</li>
            </ul>
            <router-link id="bookings-button" to="/bookingtable"
              >My Bookings</router-link
            >
          </div>
        </div>
      </div>
      <div id="right-column" class="col-sm-5 text-center">
        <div id="membership-container">
          <div class="align-self-center">
            <h2 style="background: #fcff18">Membership</h2>
          </div>
          <div id="membership-info-container" class="container">
            <div v-if="userMemberships !== null">
              <ul
                id="membership-info"
                class="list-unstyled"
                v-for="membership in userMemberships"
                :key="membership.id"
              >
                <li class="text-capitalize">
                  <h4>{{ membership.name }}</h4>
                </li>
                <li>Began: {{ membership.formattedStartDate }}</li>
                <li>Ends: {{ membership.formattedEndDate }}</li>
                <li v-if="membership.repeatingPayment">
                  Membership renews automatically.
                </li>
                <li v-else>
                  Set up a repeating plan on your next membership
                  <router-link to="/membership">here</router-link>!
                </li>
                <div
                  v-if="membership.repeatingPayment"
                  class="text-center membership-btn-box"
                >
                  <button
                    v-if="membership.repeatingPayment && !cancelModal"
                    v-on:click="cancelModal = true"
                    class="membership-btn"
                  >
                    Cancel Membership
                  </button>
                </div>
                <div
                  v-if="cancelModal"
                  class="container cancel-modal align-self-center"
                >
                  <div class="row no-gutters align-center">
                    <div class="col">Cancel automatic membership renewal?</div>
                  </div>
                  <div class="row">
                    <div class="col-6">
                      <button v-on:click="cancelMembership(membership.id)">
                        Yes
                      </button>
                    </div>
                    <div class="col-6">
                      <button v-on:click="cancelModal = false">No</button>
                    </div>
                  </div>
                </div>
              </ul>
            </div>
            <div v-else>
              <p style="margin: 0">You don't have a membership!</p>
              <div class="text-center membership-btn-box">
                <router-link
                  class="membership-btn font-weight-bolder"
                  to="/membership"
                  >Become a Member</router-link
                >
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { mapGetters, mapActions } from "vuex";
import { formatDateYMD } from "@/util/format.helpers";

export default {
  name: "Profile",
  data() {
    return {
      cancelModal: false
    };
  },
  computed: {
    ...mapGetters("auth", ["user"]),
    ...mapGetters("accounts", ["accounts"]),
    ...mapGetters("customers", ["customers"]),
    ...mapGetters("membership", ["memberships", "accountMemberships"]),
    userMemberships() {
      return this.userActiveMemberships.map(membership => {
        return {
          ...membership,
          formattedStartDate: formatDateYMD(membership.startDate),
          formattedEndDate: formatDateYMD(membership.endDate)
        };
      });
    },
    userCustomerId() {
      const emailFilter = customer =>
        customer.emailAddress === this.$auth.user.email;
      const customers = this.customers.filter(emailFilter);
      if (customers[0] && customers[0].id) {
        return customers[0].id;
      } else {
        return null;
      }
    },
    userAccountIds() {
      const accountFilter = account =>
        account.customerId === this.userCustomerId;
      return this.accounts.filter(accountFilter).map(account => account.id);
    },
    userActiveMemberships() {
      const dateFilter = membership => {
        if (membership !== null) {
          let d = new Date();
          let endDate = new Date(membership.endDate);
          return endDate.getTime() > d.getTime();
        }
      };
      const membershipFilter = membership => {
        if (membership !== null) {
          return this.userAccountIds.includes(membership.accountId);
        } else return false;
      };
      const memberships = this.memberships.filter(membershipFilter);
      return memberships.filter(dateFilter);
    }
  },
  methods: {
    ...mapActions("accounts", ["getAccounts"]),
    ...mapActions("customers", ["getAllCustomers"]),
    ...mapActions("membership", ["getMemberships", "getAccountMemberships"]),
    async cancelMembership(membership_id) {
      await this.$http
        .put(`/membership/members/${membership_id}/stop`)
        .then(response => console.log(response));
      this.cancelModal = false;
    }
  },
  watch: {
    memberships: {
      handler() {
        this.getMemberships();
      },
      deep: true
    }
  },
  async mounted() {
    await this.getAccounts();
    await this.getAllCustomers();
    await this.getMemberships();
  }
};
</script>

<style scoped>
#profile-container {
  margin-top: 80px;
  padding: 5% 3%;
  background: #f6f9fa;
}
#left-column {
  min-width: 300px;
}
#profile-info-container {
  padding: 40px 0;
}
#profile-picture {
  width: 200px;
}
#user-info {
  padding: 20px 0;
  font-size: larger;
}
#bookings-button {
  font-weight: bolder;
  color: #353535;
  background: #fcff18;
  padding: 5px 10px;
}
#right-column {
  align-self: center;
}
#membership-container {
  margin: 0 20px;
  text-align: center;
  background: #ffffff;
  display: flex;
  flex-direction: column;
  justify-content: center;
  flex-basis: auto; /* default value */
  flex-grow: 1;
  box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);
  padding: 30px 0;
}
#membership-info-container {
  padding: 20px;
}
.membership-btn-box {
  padding-top: 20px;
}
.membership-btn {
  display: inline-block;
  color: #353535;
  background: #fcff18;
  padding: 15px 30px;
  font-weight: bolder;
}
.cancel-modal {
  max-width: 80%;
  text-align: center;
  font-weight: bolder;
  background: #fcff18;
  color: #353535;
}
@media screen and (max-width: 600px) {
  .heading-div h1 {
    font-size: 10vw;
  }
}
.heading-div {
  margin-bottom: 20px;
}
.heading-div h1 {
  width: 60%;
  margin: auto;
}
.heading-div p {
  width: 100%;
  padding: 10px;
}
.heading-div span {
  background: #fcff18;
}
</style>
