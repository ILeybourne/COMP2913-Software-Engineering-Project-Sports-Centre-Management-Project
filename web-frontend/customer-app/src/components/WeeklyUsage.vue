<template>
  <v-app class="usage-container" align="center">
    <v-row class="inner-container" align="center">
      <v-card xs="12" align="center" justify="center" class="usage-contents">
        <v-container class="date">
          <v-dialog ref="dialog" v-model="modal" persistent width="290px" dark>
            <template v-slot:activator="{ on }">
              <v-subheader class="yellow--text title font-weight-bold"
                >WEEK</v-subheader
              >
              <v-text-field
                dark
                class="yellow--text title"
                title="week"
                v-model="calculateDateRange"
                readonly
                v-on="on"
                color="yellow"
              ></v-text-field>
            </template>
            <v-date-picker
              dark
              v-model="startDate"
              type="date"
              no-title
              scrollable
              color="yellow"
              event-color="black"
              show-week
              @select="fillData()"
              @input="fillData()"
            >
              <v-spacer></v-spacer>
              <v-btn class="yellow--text" @click="modal = false">Close</v-btn>
            </v-date-picker>
          </v-dialog>
        </v-container>
        <v-container class="data-table-container">
          <v-data-table
            class="white--text"
            dark
            :headers="headers"
            :items="dataWithFacilities"
            item-key="id"
            group-by="facility.name"
          >
            <template
              class="white"
              v-slot:group.header="{ items, isOpen, toggle }"
            >
              <th colspan="2" bgcolor="#404040" class="white--text">
                <v-icon class="yellow--text" @click="toggle"
                  >{{ isOpen ? "mdi-minus" : "mdi-plus" }}
                </v-icon>
                {{ items[0].facility.name }}
              </th>

              <th colspan="1" bgcolor="#404040" class="yellow--text">
                {{ items[0].facility.income }}
              </th>
            </template>
          </v-data-table>
        </v-container>
      </v-card>
    </v-row>
  </v-app>
</template>
<style scoped>
.v-subheader {
  padding-bottom: 0;
  margin-bottom: 0;
}
.v-input {
  padding: 0;
  margin: 0;
}
.usage-container {
  min-height: 50%;
  height: auto;
  display: flex;
  width: 100%;
}
.inner-container {
  /*margin: auto;*/
  /*width: 50%;*/
  min-height: 50%;
  height: min-content;
  margin: 20px;
  width: 75%;
  display: flex;
  justify-content: flex-start;
  flex-direction: column;
}
.data-table-container {
  justify-content: center;
  text-align: center;
  display: table;
  flex-direction: column;
  flex-basis: auto; /* default value */
  flex-grow: 5;
  height: auto;
  width: 100%;
}
.data-table-container th {
  text-transform: uppercase;
}
.v-text-field .v-label {
  font-size: 20em;
}
.usage-contents {
  padding: 15px 0 15px 0;
  margin: 0;
  width: 90%;
  background-color: #1e1e1e;
  min-width: 50%;
}
.usage-contents span {
  background: #fcff18;
}
.usage-contents h3 {
  padding-bottom: 15px;
}
</style>

<script>
import { mapActions, mapGetters } from "vuex";
import { formatCurrency } from "../util/format.helpers";

export default {
  name: "BookingTable",
  components: {},
  // ItemsPerPageDropdown
  data: function() {
    return {
      booking: [],
      startDate: null,
      endDate: null,
      menu: false,
      modal: false,
      headers: [
        {
          text: "FACILITY",
          value: "name",
          class: "yellow--text title font-weight-bold",
          sortable: true
        },
        {
          text: "COST",
          value: "formattedCost",
          class: "yellow--text title font-weight-bold",
          sortable: true

        },
        {
          text: "INCOME",
          value: "formattedIncome",
          class: "yellow--text title font-weight-bold",
          sortable: true

        }
      ],
      dataWithFacilities: [],
      bookingWithActivity: [],
      bookingData: [],
      weekData: []
    };
  },
  computed: {
    ...mapGetters("facilities", ["activities"]),
    ...mapGetters("facilities", ["facilities"]),
    ...mapGetters("timetable", ["sessions"]),
    ...mapGetters("timetable", ["bookings"]),
    ...mapGetters("timetable", ["resources"]),
    calculateDateRange() {
      const dateString =
        this.formatDate(this.startDate) + " - " + this.formatDate(this.endDate);
      return dateString;
    }
  },
  methods: {
    ...mapActions("facilities", {
      getFacilities: "getFacilities",
      getActivities: "getActivities"
    }),
    ...mapActions("timetable", {
      getSessions: "getAllSessions",
      getBookings: "getBookings",
      getResources: "getResources"
    }),
    //Gets Data for selected Week and calls the method to calculate each activitys income
    async fillData() {
      await this.getFacilities();
      await this.getActivities();
      console.log("performed fill data");
      const startDate = this.$moment(this.startDate);
      const endDate = startDate.clone().add("days", 6);
      this.endDate = endDate.toJSON();
      const response = await this.getSessions();
      const thisWeek = response
        .map(activity => {
          const startTimestamp = this.$moment(activity.startTime);

          return {
            ...activity,
            startTimestamp,
            dayOfYear: startTimestamp.dayOfYear()
          };
        })
        .filter(activity => {
          return (
            activity.startTimestamp.isAfter(startDate) &&
            activity.startTimestamp.isBefore(endDate)
          );
        });
      this.weekData = thisWeek;
      await this.getRelatedFacility();
    },

    formatDate(stringDate) {
      let date = new Date(stringDate);
      return (
        date.getDate() + "/" + (date.getMonth() + 1) + "/" + date.getFullYear()
      );
    },
    async defaultStartDate() {
      const endDate = this.$moment(new Date());
      const startDate = endDate.clone().subtract("days", 6);
      this.startDate = startDate.toJSON();
    },
    async getRelatedFacility() {
      await this.getFacilities();
      await this.getBookings();
      let facilityArr = [];
      for (const activity of this.activities) {
        const facilityId = activity.facility_id;
        const facilities = this.facilities;
        activity.facility = facilities.find(
          facility => Number(facility.id) === Number(facilityId)
        );
        facilityArr.push(activity);
      }
      for (const activity of facilityArr) {
        let facilityIncome = 0;
        facilityIncome = formatCurrency(
          this.calculateFacilityIncome(activity.facility_id)
        );
        activity.facility.income = facilityIncome;
        activity.formattedIncome = 0;
        let income = 0;
        income = this.calculateActivityTypeIncome(activity.id);
        activity.formattedIncome = formatCurrency(income);
        activity.formattedCost = formatCurrency(activity.cost);
      }
      this.dataWithFacilities = facilityArr;
      return facilityArr;
    },

    calculateFacilityIncome(facility_id) {
      let activityTypeIncome = 0;
      let facilityIncome = 0;
      for (const activity of this.activities) {
        if (activity.facility_id === facility_id) {
          activityTypeIncome = this.calculateActivityTypeIncome(activity.id);
          facilityIncome = facilityIncome + activityTypeIncome;
        }
      }
      return facilityIncome;
    },
    calculateActivityTypeIncome(activity_type_id) {
      let activityTypeIncome = 0;
      let sessionIncome = 0;
      for (const weeklyActivity of this.weekData) {
        if (weeklyActivity.activityTypeId === activity_type_id) {
          sessionIncome = this.calculateSessionIncome(weeklyActivity.id);
          activityTypeIncome = activityTypeIncome + sessionIncome;
        }
      }
      return activityTypeIncome;
    },

    calculateSessionIncome(session_id) {
      let sessionIncome = 0;
      let bookingIncome = 0;
      for (const booking of this.bookings) {
        if (booking.session_id === session_id) {
          bookingIncome = booking.amount * booking.participants;
          sessionIncome = sessionIncome + bookingIncome;
        }
      }
      return sessionIncome;
    }
  },
  async mounted() {
    await this.defaultStartDate();
    await this.fillData();
    await this.getActivities();
    await this.getFacilities();
    await this.getBookings();
  }
};
</script>
