<template>
  <div id="profile-container">
    <h1>My Account</h1>
    <div class="row">
      <div id="left-column" class="col-sm-7">
        <div id="profile-card" class="card">
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
              <ul class="list-unstyled">
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
      <div id="right-column" class="col-sm-5 align-self-center">
        <div id="membership-card" class="card">
          <h2>Membership</h2><p>{{ userMemberships() }}</p>
        </div>
        <div class="text-center">
          <button
            id="cancel-membership-btn"
            type="button"
            class="btn btn-outline-primary  "
          >
            <router-link to="/membership">Cancel Membership</router-link>
          </button>
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
    userMemberships() {
      return this.userActiveMemberships(
        this.userAccountIds(this.userCustomerId())
      );
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
  margin: 5% 10%;
}
#left-column {
  min-width: 300px;
}
#profile-card {
  padding: 24px;
}
#profile-picture {
  width: 200px;
}
#right-column {
  padding-top: 24px;
}
#membership-card {
  padding: 24px;
}
#cancel-membership-btn {
  margin-top: 24px;
}
</style>
