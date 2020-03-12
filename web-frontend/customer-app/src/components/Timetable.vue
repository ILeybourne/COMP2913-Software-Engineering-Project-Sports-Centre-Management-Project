<script>
import FullCalendar from "@fullcalendar/vue";
import dayGridPlugin from "@fullcalendar/daygrid";
import resourceTimelinePlugin from "@fullcalendar/resource-timeline";
import interactionPlugin from "@fullcalendar/interaction";
import { BPopover } from "bootstrap-vue";
import ActivityInfo from "@/components/ActivityInfo.vue";

export default {
  name: "Timetable",
  components: {
    FullCalendar,
    ActivityInfo
  },
  props: {
    eventData: {
      type: Array
    }
  },
  data() {
    return {
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
      /* TODO: pull from server */
      activityTypes: [
        {
          name: "Private Squash Match",
          totalCapacity: 4,
          price: 4.0,
          resources: [1]
        },
        {
          name: "Private Squash Coaching",
          totalCapacity: 1,
          price: 35.0,
          resources: [1]
        },
        {
          name: "Private Tennis Match",
          totalCapacity: 4,
          price: 4.0,
          resources: [4]
        },
        {
          name: "Five-a-Side football",
          totalCapacity: 10,
          price: 8,
          resources: [3]
        },
        {
          name: "Squash Match",
          totalCapacity: 4,
          price: 4.0,
          resources: [3]
        },
        {
          name: "1-on-1 Tennis Coaching",
          totalCapacity: 1,
          price: 35.0,
          resources: [4]
        }
      ],
      facilities: [],
      activities: [],
      previewActivity: {},
      selectedActivityForm: {
        startTime: null,
        endTime: null,
        resourceId: null,
        activityType: null,
        name: null
      }
    };
  },
  computed: {
    resources() {
      return this.facilities.map(r => {
        return {
          id: r.id,
          title: r.name
        };
      });
    },
    events() {
      /* TODO: make adapter */
      /*Transform the server format into full calendar format*/
      return this.activities.map(activity => {
        return {
          id: activity.id,
          resourceId: activity.resource.id,
          title: activity.name,
          start: activity.startTime,
          end: activity.endTime,
          totalCapacity: activity.totalCapacity,
          currentCapacity: activity.currentCapacity
        };
      });
    },
    activityTypeOptions() {
      const filter = activity => {
        return activity.resources.includes(
          Number(this.selectedActivityForm.resourceId)
        );
      };
      return this.activityTypes.filter(filter).map(a => a.name);
    }
  },
  methods: {
    drawEvent(eventInfo) {
      const { event } = eventInfo;
      const { extendedProps: options } = event;
      const totalCapacity = options.totalCapacity || 0;
      const currentCapacity = options.currentCapacity || 0;
      const content = `Current Capacity: ${currentCapacity} Total Capacity: ${totalCapacity}`;
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

      console.log(event);
      console.log(eventInfo);
      let capacity = "";
      if (options.totalCapacity !== null) {
        capacity = ` - ${currentCapacity}/${totalCapacity}`;
      }
      eventInfo.el.innerText = `${event.title}${capacity}`;
    },
    activityClick(eventInfo) {
      const { event } = eventInfo;
      const { extendedProps: options } = event;
      console.log(options);
      this.previewActivity = this.activities.find(activity => activity.id === Number(event.id));
      this.$bvModal.show("preview-activity-modal");
    },
    onEventTimeChange(a) {
      console.log(a);
    },
    onSelect(event) {
      const s = event.start.toISOString();
      this.selectedActivityForm.startTime = s.substring(0, s.length - 1);
      const e = event.end.toISOString();
      this.selectedActivityForm.endTime = e.substring(0, e.length - 1);

      this.selectedActivityForm.resourceId = event.resource.id;
      this.$bvModal.show("create-activity-modal");
    },
    async submitNewActivity(event) {
      event.preventDefault();
      let activityType = this.selectedActivityForm.activityType;
      debugger;
      const activity = this.activityTypes.find(a => a.name === activityType);
      console.log(activity);

      try {
        /* TODO: Validate and check server response */
        this.selectedActivityForm.name = activityType;
        const token = await this.$auth.getTokenSilently();
        const body = {
          ...this.selectedActivityForm,
          ...activity,
          currentCapacity: 0
        };
        console.log(body);
        const { data } = await this.$http.post(
          `/resources/${this.selectedActivityForm.resourceId}/activities`,
          body,
          {
            headers: {
              Authorization: `Bearer ${token}`
            }
          }
        );

        console.log(data);

        await this.$router.push({
          name: "BookingPage",
          query: { activityId: data.id }
        });
      } catch (e) {
        console.error(e);
      }
    },
    async getActivities() {
      const token = await this.$auth.getTokenSilently();

      const { data } = await this.$http.get(`/timetable`, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });

      this.activities = data;
    },
    async getResources() {
      const token = await this.$auth.getTokenSilently();

      const { data } = await this.$http.get("/resources", {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });

      this.facilities = data.content;
    }
  },
  async mounted() {
    await this.getActivities();
    await this.getResources();
    // await timetableService.read();
  }
};
</script>

<template>
  <div id="calendar">
    <FullCalendar
      :resources="resources"
      :events="events"
      :plugins="calendarPlugins"
      :header="header"
      :selectable="true"
      :selectMirror="true"
      :eventRender="drawEvent"
      schedulerLicenseKey="GPL-My-Project-Is-Open-Source"
      defaultView="resourceTimelineDay"
      aspectRatio="1"
      minTime="06:00:00"
      maxTime="23:00:00"
      @eventClick="activityClick($event)"
      @eventDrop="onEventTimeChange($event)"
      @eventResize="onEventTimeChange($event)"
      @select="onSelect($event)"
    />
    <b-modal id="create-activity-modal" title="Create Activity" hide-footer>
      <!--TODO: use Vuex-->
      <!--TODO: Reoccurring-->
      <!--TODO: extract from this page-->
      <!--TODO: Activity preview component showing capacity etc for selection-->
      <b-form @submit="submitNewActivity($event)">
        <b-form-group
          id="activityType"
          label="Activity Type"
          label-for="activitySelect"
        >
          <b-select
            id="activitySelect"
            v-model="selectedActivityForm.activityType"
            :options="activityTypeOptions"
            required
          ></b-select>
        </b-form-group>
        <b-form-group
          id="startTime"
          label="Start Time:"
          label-for="startTimeInput"
        >
          <b-form-input
            id="startTimeInput"
            v-model="selectedActivityForm.startTime"
            type="datetime-local"
            readonly
            required
          ></b-form-input>
        </b-form-group>

        <b-form-group id="endTime" label="End Time:" label-for="endTimeInput">
          <b-form-input
            id="endTimeInput"
            v-model="selectedActivityForm.endTime"
            type="datetime-local"
            readonly
            required
          ></b-form-input>
        </b-form-group>
        <div class="d-flex justify-content-between">
          <b-button type="submit" variant="primary">Book Activity</b-button>
          <b-button
            type="reset"
            variant="danger"
            @click="$bvModal.hide('create-activity-modal')"
            >Cancel
          </b-button>
        </div>
      </b-form>
    </b-modal>
    <b-modal id="preview-activity-modal" title="Activity">
      <ActivityInfo :activity="this.previewActivity"></ActivityInfo>
    </b-modal>
  </div>
</template>

<style lang="scss">
@import "~@fullcalendar/core/main.css";
@import "~@fullcalendar/daygrid/main.css";
@import "~@fullcalendar/resource-timeline/main.css";
@import "~@fullcalendar/timeline/main.css";

#calendar {
  max-width: 100%;
}
</style>
