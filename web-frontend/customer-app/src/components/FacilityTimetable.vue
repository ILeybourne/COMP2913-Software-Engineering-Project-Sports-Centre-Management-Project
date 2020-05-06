<template>
  <div id="calendar-container">
    <h3>Timetable for Selected Resource</h3>
    <div id="calendar">
      <FullCalendar
        :events="events"
        :plugins="calendarPlugins"
        :selectable="true"
        :selectOverlap="false"
        :eventOverlap="false"
        :selectMirror="true"
        :resize="false"
        defaultView="timeGridWeek"
        aspectRatio="1"
        minTime="06:00:00"
        maxTime="23:00:00"
        @eventClick="activityClick($event)"
      />
    </div>
    <!--    <b-modal-->
    <!--      id="create-activity-modal"-->
    <!--      title="Create a new Session"-->
    <!--      hide-footer-->
    <!--    >-->
    <!--      <SessionCreate-->
    <!--        @post="onSessionCreate($event)"-->
    <!--        :startTime="selectedSession.startTime"-->
    <!--        :endTime="selectedSession.endTime"-->
    <!--        :facilityId="selectedSession.resourceId"-->
    <!--      ></SessionCreate>-->
    <!--      <b-button-->
    <!--        type="reset"-->
    <!--        variant="danger"-->
    <!--        @click="$bvModal.hide('create-activity-modal')"-->
    <!--      >Cancel-->
    <!--      </b-button>-->
    <!--    </b-modal>-->
    <b-modal id="preview-activity-modal" title="Session Details">
      <SessionInfo
        v-if="this.previewSession"
        :session="this.previewSession"
        @sessionDeleted="onSessionDelete($event)"
      ></SessionInfo>
    </b-modal>
  </div>
</template>
<script>
import timeGridPlugin from "@fullcalendar/timegrid";
import FullCalendar from "@fullcalendar/vue";
import store from "@/store";
import { sessionToEvent } from "@/util/session.helpers";
import { mapActions } from "vuex";
import SessionInfo from "@/components/SessionInfo.vue";

export default {
  name: "FacilityTimetable",
  components: {
    FullCalendar,
    SessionInfo
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
      previewSession: null,
      error: null
    };
  },
  computed: {
    sessions() {
      const key = "timetable/getSessionsForFacility";
      return store.getters[key](this.facilityId);
    },
    events() {
      return this.sessions.map(sessionToEvent);
    }
  },
  methods: {
    ...mapActions("timetable", ["getAllSessions"]),
    activityClick(eventInfo) {
      const { event } = eventInfo;

      this.previewSession = this.sessions.find(
        activity => activity.id === Number(event.id)
      );

      if (this.previewSession) {
        this.$bvModal.show("preview-activity-modal");
      } else {
        this.error = "The session you selected could not be previewed";
      }
    }
  },
  async mounted() {
    await this.getAllSessions();
  }
};
</script>
<style scoped lang="scss">
@import "~@fullcalendar/core/main.min.css";
@import "~@fullcalendar/timegrid/main.min.css";
</style>
