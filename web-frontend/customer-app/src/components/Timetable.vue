<script>
import FullCalendar from "@fullcalendar/vue";
import dayGridPlugin from "@fullcalendar/daygrid";
import resourceTimelinePlugin from "@fullcalendar/resource-timeline";
import interactionPlugin from "@fullcalendar/interaction";
import { BPopover } from "bootstrap-vue";
import { mapActions, mapGetters } from "vuex";
import SessionInfo from "@/components/SessionInfo.vue";
import SessionCreate from "@/components/SessionCreate.vue";
import { sessionToEvent } from "@/util/session.helpers";

export default {
  name: "Timetable",
  components: {
    SessionCreate,
    FullCalendar,
    SessionInfo
  },
  data() {
    return {
      error: null,
      calendarPlugins: [
        dayGridPlugin,
        resourceTimelinePlugin,
        interactionPlugin
      ],
      header: {
        left: "today prev,next",
        center: "title",
        right: "resourceTimelineDay,resourceTimelineWeek"
      },
      buttonText: {
        today: "Today"
      },
      previewSession: {},
      selectedSession: {
        startTime: null,
        endTime: null,
        facilityId: null
      }
    };
  },
  computed: {
    ...mapGetters("facilities", ["facilities"]),
    ...mapGetters("timetable", ["sessions"]),
    ...mapGetters("auth", ["isEmployeeOrManager"]),
    resources() {
      return this.facilities.map(r => {
        return {
          id: r.id,
          title: r.name
        };
      });
    },
    events() {
      // Transform the server format into full calendar format
      return this.sessions.map(sessionToEvent);
    }
  },
  methods: {
    ...mapActions("facilities", ["getFacilities"]),
    ...mapActions("timetable", ["getAllSessions"]),
    drawEvent(eventInfo) {
      // TODO: change colour if you can't book
      const { event } = eventInfo;
      const { extendedProps: options } = event;
      const totalCapacity = options.totalCapacity || 0;
      const currentCapacity = options.currentCapacity || 0;
      const content = `Current Capacity: ${currentCapacity} Total Capacity: ${totalCapacity}`;
      // Popover for preview
      const popover = new BPopover({
        propsData: {
          title: event.title,
          content,
          placement: "auto",
          boundary: "scrollParent",
          boundaryPadding: 5,
          delay: 500,
          offset: 0,
          triggers: "hover",
          html: true,
          target: eventInfo.el
        }
      });
      popover.$mount();

      let capacity = "";
      if (options.totalCapacity !== null) {
        capacity = `- ${currentCapacity}/${totalCapacity}`;
      }
      eventInfo.el.innerText = `${event.title} ${capacity}`;
    },
    drawResource(e) {
      // Add a link to the timetable for that facility
      e.el.addEventListener("click", () => {
        const facility = e.resource._resource;
        this.$router.push({
          name: "FacilityTimetable",
          params: {
            id: facility.id
          }
        });
      });
      return e;
    },
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
    },
    onSelect(event) {
      if (!this.isEmployeeOrManager) {
        return;
      }
      const s = event.start.toISOString();
      const e = event.end.toISOString();

      this.selectedSession.startTime = s.substring(0, s.length - 1);
      this.selectedSession.endTime = e.substring(0, e.length - 1);
      this.selectedSession.resourceId = Number(event.resource.id);

      this.$bvModal.show("create-activity-modal");
    },
    onSessionDelete() {
      this.$bvModal.hide("preview-activity-modal");
    },
    onSessionCreate({ createBooking: redirectToBooking, ...event }) {
      if (!this.isEmployeeOrManager) {
        return;
      }
      if (redirectToBooking) {
        this.$router.push({
          name: "BookingPage",
          params: {
            facility: String,
            activity: String
          },
          query: {
            facilityId: event.facilityId,
            activityId: event.activityId,
            sessionId: event.sessionId
          }
        });
      } else {
        this.$bvModal.hide("create-activity-modal");
      }
    }
  },
  mounted() {
    this.getFacilities();
    this.getAllSessions();
  }
};
</script>

<template>
  <div class="container">
    <div class="alert alert-warning" v-if="error">{{ error }}</div>
    <div id="calendar">
      <FullCalendar
        :resources="resources"
        :events="events"
        :plugins="calendarPlugins"
        :header="header"
        :selectable="true"
        :selectOverlap="false"
        :selectMirror="true"
        :eventRender="drawEvent"
        :resourceRender="drawResource"
        :resize="false"
        :eventOverlap="false"
        schedulerLicenseKey="GPL-My-Project-Is-Open-Source"
        defaultView="resourceTimelineDay"
        aspectRatio="1"
        minTime="06:00:00"
        maxTime="23:00:00"
        @eventClick="activityClick($event)"
        @select="onSelect($event)"
        :timeZone="'UTC'"
        event-color="yellow"
      />
      <b-modal
        id="create-activity-modal"
        title="Create a new Session"
        hide-footer
      >
        <SessionCreate
          v-if="isEmployeeOrManager"
          @post="onSessionCreate($event)"
          :startTime="selectedSession.startTime"
          :endTime="selectedSession.endTime"
          :facilityId="selectedSession.resourceId"
        ></SessionCreate>
        <b-button
          type="reset"
          variant="danger"
          @click="$bvModal.hide('create-activity-modal')"
          >Cancel
        </b-button>
      </b-modal>
      <b-modal id="preview-activity-modal" title="Session Details">
        <SessionInfo
          v-if="this.previewSession"
          :session="this.previewSession"
          @sessionDeleted="onSessionDelete($event)"
        ></SessionInfo>
      </b-modal>
    </div>
  </div>
</template>

<style scoped lang="scss">
@import "~@fullcalendar/core/main.min.css";
@import "~@fullcalendar/daygrid/main.min.css";
@import "~@fullcalendar/resource-timeline/main.min.css";
@import "~@fullcalendar/timeline/main.min.css";

#calendar {
  max-width: 100%;
  max-height: 100%;
}
</style>
