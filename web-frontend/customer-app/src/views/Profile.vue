<template>
  <div id="profile-container">
    <div class="row">
      <div id="left-column" class="col-sm-7">
        <h1>My <span style="background: #fcff18">Account</span></h1>
        <div id="profile-info-container" class="container">
          <div class="row">
            <div class="col-xl-5 text-center align-self-center">
              <img
                :src="user.picture"
                id="profile-picture"
                class="rounded rounded-circle border-bottom border-dark"
              />
            </div>
            <div class="col-xl-7">
              <h2>{{ user.nickname }}</h2>
              <ul id="user-info" class="container list-unstyled">
                <li>{{ $auth.user.name }}</li>
                <li>{{ $auth.user.email }}</li>
                <li>
                  <a href="/bookingtable" title="BookingsTable">My Bookings</a>
                </li>
              </ul>
            </div>
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
      let customers = this.customers;
      let accounts = this.accounts;
      let memberships = this.memberships;
      if (customers && accounts && memberships) {
        let activeMemberships = this.userActiveMemberships(
          memberships,
          this.userAccountIds(accounts, this.userCustomerId(customers))
        );
        activeMemberships = activeMemberships.map(membership => {
          return {
            ...membership,
            formattedStartDate: this.formatDate(membership.startDate),
            formattedEndDate: this.formatDate(membership.endDate)
          };
        });
        return activeMemberships;
      } else return null;
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
    },
    formatDate(rawDate) {
      let date = new Date(rawDate);
      return (
        date.getFullYear() + "/" + (date.getMonth() + 1) + "/" + date.getDate()
      );
    },
    userCustomerId(customers) {
      const emailFilter = customer =>
        customer.emailAddress === this.$auth.user.email;
      customers = this.customers.filter(emailFilter);
      if (customers[0].id < 0) {
        return null;
      } else {
        return customers[0].id;
      }
    },
    userAccountIds(accounts, customerId) {
      const accountFilter = account => account.customerId === customerId;
      accounts = accounts.filter(accountFilter);
      accounts = accounts.map(account => account.id);
      return accounts;
    },
    userActiveMemberships(memberships, accountIds) {
      const dateFilter = membership => {
        if (membership !== null) {
          let d = new Date();
          let endDate = new Date(membership.endDate);
          return endDate.getTime() > d.getTime();
        }
      };
      const membershipFilter = membership => {
        if (membership !== null) {
          return accountIds.includes(membership.accountId);
        } else return false;
      };
      memberships = memberships.filter(membershipFilter);
      return memberships.filter(dateFilter);
    }
  },
  mounted() {
    this.getAccounts();
    this.getAllCustomers();
    this.getMemberships();
  }
};
</script>

<style scoped>
#profile-container {
  padding: 5% 8%;
  background: #f6f9fa;
}
#left-column {
  min-width: 300px;
}
#profile-info-container {
  padding: 40px 20px;
}
#profile-picture {
  width: 200px;
}
#user-info {
  padding: 20px 0;
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
</style>
