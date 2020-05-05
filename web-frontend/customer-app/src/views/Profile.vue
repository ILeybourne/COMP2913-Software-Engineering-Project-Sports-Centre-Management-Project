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
            <div v-if="userMemberships().length > 0">
              <ul
                id="membership-info"
                class="list-unstyled"
                v-for="membership in userMemberships()"
                :key="membership.id"
              >
                <li class="text-capitalize">
                  <h4>{{ membership.name }}</h4>
                </li>
                <li>Began: {{ membership.formattedStartDate }}</li>
                <li>Ends: {{ membership.formattedEndDate }}</li>
                <li>Auto-renewal: {{ membership.autoRenewal }}</li>
              </ul>
              <div class="text-center membership-btn-box">
                <router-link
                  class="membership-btn font-weight-bolder"
                  to="/membership"
                  >Cancel Membership</router-link
                >
              </div>
            </div>
            <div v-else>
              <p style="margin: 0">You do not have a membership!</p>
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
    return {};
  },
  computed: {
    ...mapGetters("auth", ["user"]),
    ...mapGetters("accounts", ["accounts"]),
    ...mapGetters("customers", ["customers"]),
    ...mapGetters("membership", ["memberships", "accountMemberships"])
  },
  methods: {
    ...mapActions("accounts", ["getAccounts"]),
    ...mapActions("customers", ["getAllCustomers"]),
    ...mapActions("membership", ["getMemberships", "getAccountMemberships"]),
    userMemberships() {
      let memberships = this.userActiveMemberships(
        this.userAccountIds(this.userCustomerId())
      );
      memberships = memberships.map(membership => {
        return {
          ...membership,
          formattedStartDate: this.formatDate(membership.startDate),
          formattedEndDate: this.formatDate(membership.endDate),
          autoRenewal: this.formatRenewal(membership.repeatingPayment)
        };
      });
      return memberships;
    },
    formatDate(rawDate) {
      let date = new Date(rawDate);
      return (
        date.getFullYear() + "/" + (date.getMonth() + 1) + "/" + date.getDate()
      );
    },
    formatRenewal(repeating) {
      if (repeating) {
        return "enabled";
      } else {
        return "disabled";
      }
    },
    userCustomerId() {
      let customers = this.customers;
      if (customers == null) {
        return null;
      } else {
        const emailFilter = customer =>
          customer.emailAddress === this.$auth.user.email;
        customers = this.customers.filter(emailFilter);
        return customers[0].id;
      }
    },
    userAccountIds(customerId) {
      let accounts = this.accounts;
      if (accounts == null || customerId == null) {
        return null;
      } else {
        const accountFilter = account => account.customerId === customerId;
        accounts = accounts.filter(accountFilter);
        accounts = accounts.map(account => account.id);
        return accounts;
      }
    },
    userActiveMemberships(accountIds) {
      let memberships = this.memberships;
      if (memberships == null) {
        return null;
      } else {
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
        memberships = memberships.filter(dateFilter);
        return memberships.filter(membershipFilter);
      }
    },
    setUserMembershipDetails() {
      this.userMembershipDetails = this.userActiveMemberships(
        this.userAccountIds(this.userCustomerId())
      );
    }
  },
  mounted() {
    this.getAccounts();
    this.getAllCustomers();
    this.getMemberships();
    this.setUserMembershipDetails;
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
}
</style>
