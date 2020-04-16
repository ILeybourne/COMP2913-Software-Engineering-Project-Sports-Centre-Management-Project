<template>
  <div class="cash-info">
    <div
      class="cash-container"
      v-bind:style="{ width: this.componentWidth + '%' }"
    >
      <h2>Cash Payment</h2>
      <form>
        <div class="form-row">
          <label for="PriceDiv">Price:</label>
          <div id="PriceDiv" name="PriceDiv" class="input-container">
            <b-input-group size="lg" prepend="£">
              <b-form-input
                type="number"
                step="0.01"
                id="cashPrice"
                name="cashPrice"
                v-model="$attrs.activityPrice"
                class="form-control"
                readonly
              ></b-form-input>
            </b-input-group>
          </div>
        </div>
        <div class="form-row">
          <label for="cashGivenDiv">Cash Given:</label>
          <div id="cashGivenDiv" name="cashGiven" class="input-container">
            <b-input-group size="lg" prepend="£">
              <b-form-input
                type="number"
                step="0.01"
                id="cashGiven"
                name="cashGiven"
                class="form-control"
                v-model="cashGiven"
                required
                @keyup="getChange"
                pattern="^\d*(\.\d{0,2})?$"
              ></b-form-input>
            </b-input-group>
          </div>
        </div>
        <div class="form-row">
          <label for="changeDueDiv">Change Due:</label>
          <div id="changeDueDiv" name="cashGiven" class="input-container">
            <b-input-group size="lg" prepend="£">
              <b-form-input
                id="changeDue"
                name="changeDue"
                class="form-control"
                v-model="change"
                readonly
                type="number"
                step="0.01"
                @keyup="[getChange, setTwoNumberDecimal($event)]"
              ></b-form-input>
            </b-input-group>
          </div>
        </div>
        <div class="button-container">
          <button
            id="cashSubmitBtn"
            type="button"
            class="btn btn-outline-secondary"
            name="details"
            @click="submitCashPayment"
            v-bind:class="{
              disable: changeValid
            }"
            @load="getChange"
          >
            Submit
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<style scoped>
.input-container {
  min-width: 90%;
}

.form-row {
  padding: 5px;
}

.cash-container {
  margin: auto;
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
h2 {
  text-align: center;
}
</style>

<script>
export default {
  name: "CashInformation",
  data() {
    return {
      changeVal: 0,
      cashGiven: 0,
      changeValid: false,
      componentWidth: 90
    };
  },
  computed: {
    change: {
      get() {
        return Number(this.cashGiven) - this.$attrs.activityPrice;
      },
      set() {
        this.changeVal = Number(this.cashGiven) - this.$attrs.activityPrice;
      }
    }
  },
  methods: {
    setTwoNumberDecimal(event) {
      console.log("event");
      console.log(event);
      this.cashGiven = parseFloat(event).toFixed(2);
      console.log("this.cashGiven");
      console.log(this.cashGiven);
    },

    getChange() {
      this.changeVal = this.cashGiven - this.$attrs.activityPrice;
      if (this.changeVal >= 0) {
        this.changeValid = true;
      } else {
        this.changeValid = false;
      }
      // console.log(this.change)
    },
    submitCashPayment(e) {
      this.$emit("submitCashPayment", this.$data);
      console.log(e);
      this.componentWidth = 60;
    }
  }
};
</script>
