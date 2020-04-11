<template>
  <div class="guest-info">
    <div
      class="guest-container"
      v-bind:style="{ width: this.componentWidth + '%' }"
    >
      <form @submit="submitCustomerDetails($event)">
        <div class="form-row">
          <label for="Price">Price:</label>
          <input
            type="text"
            id="cashPrice"
            name="cashPrice"
            v-model="$attrs.activityPrice"
            class="form-control"
            readonly
          />
        </div>
        <div class="form-row">
          <label for="cashGiven">Cash Given:</label>
          <input
            type="text"
            id="cashGiven"
            name="cashGiven"
            class="form-control"
            v-model="cashGiven"

            required
            @keyup="getChange"
          />
        </div>
        <div class="form-row">
          <label for="changeDue">Change Due:</label>
          <input
            type="text"
            id="changeDue"
            name="changeDue"
            class="form-control"
            v-model="change"
            readonly
          />
        </div>
        <div class="button-container">
          <button
            id="guestSubmitBtn"
            type="submit"
            class="btn btn-outline-secondary"
            name="details"
          >
            Submit
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<style scoped>
.form-row {
  padding: 5px;
}

/*.guest-info {*/
/*  padding-top: 5%;*/
/*  padding-bottom: 5%;*/
/*}*/

.guest-container {
  margin: auto;
  /*width: 50%;*/
  border: 3px solid #3183e5;
  padding: 10px;
  border-radius: 10px;
}

.button-container {
  display: flex;
  justify-content: space-between;
  padding-left: 20%;
  padding-right: 20%;
  padding-top: 10px;
}

input {
  width: 90%;
}

select {
  width: 90%;
}

label {
  width: 10%;
}

button {
  margin: auto;

  width: 50%;
}
</style>

<script>
export default {
  name: "CardInformation",
  data() {
    return {
      cashGiven: 0,
      change:0,
      componentWidth: 90,
    };
  },
  computed: {},
  methods: {
    getChange(){
      this.change = this.$attrs.activityPrice - this.cashGiven
      console.log(this.change)
    },
    submitCustomerDetails(e) {
      e.preventDefault();
      if (
        this.$data.firstName !== "" &&
        this.$data.surname !== "" &&
        this.$data.email !== "" &&
        this.$data.phone !== ""
      ) {
        this.$emit("submitCustomerDetails", this.$data);
        this.componentWidth = 60;
      }
    },

  },
  mounted() {
  }
};
</script>
