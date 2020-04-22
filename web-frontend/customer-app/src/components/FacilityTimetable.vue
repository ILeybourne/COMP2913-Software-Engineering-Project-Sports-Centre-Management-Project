<template>
  <div id="calendar-container">
    <h3>Timetable for Selected Resource</h3>
    <div id="calendar">
      <FullCalendar
        :events="events"
        :plugins="calendarPlugins"
        :selectable="true"
        :selectMirror="true"
        :resize="false"
        defaultView="timeGridWeek"
        aspectRatio="1"
        minTime="06:00:00"
        maxTime="23:00:00"
      />

      <!--      @eventClick="activityClick($event)"-->
      <!--      @select="onSelect($event)"-->
      <!--      <b-modal-->
      <!--        id="create-activity-modal"-->
      <!--        title="Create a new Session"-->
      <!--        hide-footer-->
      <!--      >-->
      <!--        <SessionCreate-->
      <!--          @post="onSessionCreate($event)"-->
      <!--          :startTime="selectedSession.startTime"-->
      <!--          :endTime="selectedSession.endTime"-->
      <!--          :facilityId="selectedSession.resourceId"-->
      <!--        ></SessionCreate>-->
      <!--        <b-button-->
      <!--          type="reset"-->
      <!--          variant="danger"-->
      <!--          @click="$bvModal.hide('create-activity-modal')"-->
      <!--          >Cancel-->
      <!--        </b-button>-->
      <!--      </b-modal>-->
      <!--      <b-modal id="preview-activity-modal" title="Session Details">-->
      <!--        <SessionInfo-->
      <!--          v-if="this.previewSession"-->
      <!--          :session="this.previewSession"-->
      <!--        ></SessionInfo>-->
      <!--      </b-modal>-->
    </div>
    <div class="row"></div>
  </div>
</template>
<script>
import timeGridPlugin from "@fullcalendar/timegrid";
import FullCalendar from "@fullcalendar/vue";
import store from "@/store";
import { sessionToEvent } from "@/util/session.helpers";
import { mapActions } from "vuex";

export default {
  name: "FacilityTimetable",
  components: {
    FullCalendar
  },
  props: {
    facilityId: {
      type: Number,
      required: true
    }
  },
  data() {
    return {
      calendarPlugins: [timeGridPlugin],
      sessions: []
    };
  },
  computed: {
    events() {
      return this.sessions.map(sessionToEvent);
    }
  },
  methods: {
    ...mapActions("timetable", ["getAllSessions"])
  },
  async mounted() {
    await this.getAllSessions();
    console.log("Mounted the Facility timetable, got data");
    this.sessions = store.getters["timetable/getSessionsForFacility"](
      this.facilityId
    );
  }
};
</script>
<style scoped lang="scss">
@import "~@fullcalendar/core/main.min.css";
@import "~@fullcalendar/timegrid/main.min.css";
</style>
