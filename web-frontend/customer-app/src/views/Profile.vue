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
              </ul>
              <ul class="list-unstyled">
                <li>Other stuff</li>
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
          <a>Membership details</a>
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
import { mapGetters } from "vuex";

export default {
  name: "Profile",
  computed: {
    ...mapGetters("auth", ["user"])
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
