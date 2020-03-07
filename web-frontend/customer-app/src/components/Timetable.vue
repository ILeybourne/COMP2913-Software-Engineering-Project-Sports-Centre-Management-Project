<script>
import FullCalendar from "@fullcalendar/vue";
import dayGridPlugin from "@fullcalendar/daygrid";
import resourceTimelinePlugin from "@fullcalendar/resource-timeline";

//TODO pull from db 1 facility

//TODO write functions in form of adapter (https://www.dofactory.com/javascript/adapter-design-pattern)

export default {
  name: "Timetable",
  components: {
    FullCalendar
  },
  props: {
    eventData: {
      type: Array
    }
  },
  data() {
    return {
      calendarPlugins: [dayGridPlugin, resourceTimelinePlugin],
      header: {
        left: "today prev,next",
        center: "title",
        right: "resourceTimelineDay,resourceTimelineWeek"
      },
      demoData2: [
        {
          id: 1,
          name: "Squash Court",
          activities: [
            {
              id: 1,
              name: "Morning Squash",
              total_capacity: 15,
              current_capacity: 10,
              booking: null,
              start_time: "2020-02-27T08:00:00.000+0000",
              end_time: "2020-02-27T10:00:00.000+0000"
            }
          ]
        },
        {
          id: 2,
          name: "Tennis Court",
          activities: []
        }
      ]
    };
  },
  computed: {
    //functions for new data format
    resources() {
      const resourcesArray = [];

      for (const resource in this.demoData2.content) {
        // console.log(demoData2.content[resource])
        const resourceObject = {
          // id: (String.fromCharCode(96 + demoData2.content[resource].id)),
          id: this.demoData2.content[resource].id,
          title: this.demoData2.content[resource].name
        };

        resourcesArray.push(resourceObject);
      }
      return resourcesArray;
    },

    events() {
      const eventArray = [];

      for (const resource in this.demoData2.content) {
        for (const act in this.demoData2.content[resource].activities) {
          const eventObj = {
            id: this.demoData2.content[resource].activities[act].id,
            resourceId: this.demoData2.content[resource].id,
            title: this.demoData2.content[resource].activities[act].name,
            start: this.demoData2.content[resource].activities[act].start_time,
            end: this.demoData2.content[resource].activities[act].end_time
          };
          eventArray.push(eventObj);
        }
      }
      return eventArray;
    }
  }
};
</script>

<template>
  <div id="calendar">
    <FullCalendar
      schedulerLicenseKey="GPL-My-Project-Is-Open-Source"
      defaultView="resourceTimelineDay"
      aspectRatio="1"
      :events="events"
      :plugins="calendarPlugins"
      :header="header"
      minTime="06:00:00"
      maxTime="23:00:00"
      :resources="resources"
    />
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
