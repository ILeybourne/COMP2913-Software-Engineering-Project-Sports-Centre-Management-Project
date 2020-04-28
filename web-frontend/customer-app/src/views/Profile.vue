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
              <h2>{{ $auth.user.nickname }}</h2>
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
          <h2>Membership</h2>
          {{ memberships }}
        </div>
        <div class="text-center">
          <button
            id="cancel-membership-btn"
            type="button"
            class="btn btn-outline-primary  "
          >
            Cancel Membership
          </button>
        </div>
        <!--<pre>{{ JSON.stringify(user, null, 2) }}</pre>-->
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
      accountId: null
    };
  },
  computed: {
    ...mapGetters("auth", ["user"]),
    ...mapGetters("accounts", ["accounts"]),
    ...mapGetters("customers", ["customers"]),
    ...mapGetters("memberships", ["memberships", "accountMemberships"])
  },
  methods: {
    ...mapActions("accounts", ["getAccounts"]),
    ...mapActions("customers", ["getAllCustomers"]),
    ...mapActions("memberships", ["getMemberships", "getAccountMemberships"]),
    activeMemberships() {
      let customers = this.customers;
      if (customers == null) {
        return null;
      } else {
        const emailFilter = customer =>
          customer.emailAddress === this.$auth.user.email;
        customers = this.customers.filter(emailFilter);
        let customerId = customers[0].id;
        let accounts = this.accounts;
        if (accounts == null) {
          return null;
        } else {
          const accountFilter = account =>
            Number(
              account._links["Customer Details"].href.split("/").slice(-1)[0]
            ) === Number(customerId);
          accounts = this.accounts.filter(accountFilter);
          accounts = accounts.map(account =>
            Number(
              account._links["Customer Details"].href.split("/").slice(-1)[0]
            )
          );
          /*return accounts.map(accountId =>
            this.getAccountMemberships(accountId)
          );*/
          return null;
        }
      }
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
