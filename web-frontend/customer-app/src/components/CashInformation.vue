<template>
  <div class="checkout-container">
    <div
      class="cash-container"
      v-bind:style="{ width: this.componentWidth + '%' }"
    >
      <h4>Cash Payment</h4>
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
                @keyup="[getChange(), setTwoNumberDecimal()]"
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
                step="0.1"
                @keyup="[getChange]"
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
  padding: 10px;
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

.checkout-container {
  display: flex;
  flex-direction: column;
  padding: 59px 0px 59px 0px;
  min-height: 50%;
  height: auto;
  width: auto;
  margin: 20px;
  background: #f6f9fa;
  color: #242424;
  justify-content: center;
  flex-basis: auto; /* default value */
  flex-grow: 1;
  -webkit-box-shadow: 10px 10px 24px 5px rgba(0, 0, 0, 0.1);
  -moz-box-shadow: 10px 10px 24px 5px rgba(0, 0, 0, 0.1);
  box-shadow: 10px 10px 24px 5px rgba(0, 0, 0, 0.1);
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
        return (Number(this.cashGiven) - this.$attrs.activityPrice).toFixed(2);;
      },
      set() {
        this.changeVal = (Number(this.cashGiven) - this.$attrs.activityPrice).toFixed(2);;
      }
    }
  },
  methods: {
    setTwoNumberDecimal() {
      this.cashGiven = parseFloat(this.cashGiven).toFixed(2);
    },

    getChange() {
      this.changeVal = this.cashGiven - this.$attrs.activityPrice;
      this.changeValid = this.changeVal >= 0;
    },
    submitCashPayment() {
      this.$emit("submitCashPayment", this.$data);
    }
  }
};
</script>
