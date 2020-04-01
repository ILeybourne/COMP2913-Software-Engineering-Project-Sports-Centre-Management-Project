<template>
  <v-card class="mx-auto text-center" color="green" dark max-width="600">
    <v-card-text>
      <v-sheet color="rgba(0, 0, 0, .12)">
        <v-sparkline
          :value="value"
          color="rgba(255, 255, 255, .7)"
          height="100"
          padding="24"
          stroke-linecap="round"
          smooth
        >
          <template v-slot:label="item"> ${{ item.value }} </template>
        </v-sparkline>
      </v-sheet>
    </v-card-text>
  </v-card></template
>

<style scoped></style>
<script>
export default {
  data: () => ({
    value: [423, 446, 675, 510, 590, 610, 760]
  }),
  computed: {},
  methods: {
    dtEditClick: props => alert("Click props:" + JSON.stringify(props)),
    async getBooking() {
      const token = await this.$auth.getTokenSilently();

      const { data } = await this.$http.get(`/bookings`, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      this.bookings = data.content;
    },
    showCancel() {
      this.$bvModal.show("edit-booking-modal");
    }
  }
};
</script>
