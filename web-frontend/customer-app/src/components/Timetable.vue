<script>
import FullCalendar from "@fullcalendar/vue";
import dayGridPlugin from "@fullcalendar/daygrid";
import resourceTimelinePlugin from "@fullcalendar/resource-timeline";
import interactionPlugin from "@fullcalendar/interaction";

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
      calendarPlugins: [
        dayGridPlugin,
        resourceTimelinePlugin,
        interactionPlugin
      ],
      header: {
        left: "today prev,next",
        center: "title",
        right: "resourceTimelineDay,resourceTimelineWeek"
      }
    };
  },
  computed: {
    resources() {
      return [
        { id: "a", title: "Auditorium A" },
        { id: "b", title: "Auditorium B", eventColor: "green" },
        { id: "c", title: "Auditorium C", eventColor: "orange" },
        {
          id: "d",
          title: "Auditorium D",
          children: [
            { id: "d1", title: "Room D1" },
            { id: "d2", title: "Room D2" }
          ]
        },
        { id: "e", title: "Auditorium E" },
        { id: "f", title: "Auditorium F", eventColor: "red" },
        { id: "g", title: "Auditorium G" },
        { id: "h", title: "Auditorium H" },
        { id: "i", title: "Auditorium I" },
        { id: "j", title: "Auditorium J" },
        { id: "k", title: "Auditorium K" },
        { id: "l", title: "Auditorium L" },
        { id: "m", title: "Auditorium M" },
        { id: "n", title: "Auditorium N" },
        { id: "o", title: "Auditorium O" },
        { id: "p", title: "Auditorium P" },
        { id: "q", title: "Auditorium Q" },
        { id: "r", title: "Auditorium R" },
        { id: "s", title: "Auditorium S" },
        { id: "t", title: "Auditorium T" },
        { id: "u", title: "Auditorium U" },
        { id: "v", title: "Auditorium V" },
        { id: "w", title: "Auditorium W" },
        { id: "x", title: "Auditorium X" },
        { id: "y", title: "Auditorium Y" },
        { id: "z", title: "Auditorium Z" }
      ];
    },
    events() {
      return [
        {
          resourceId: "d",
          title: "event 1",
          start: "2020-03-07",
          end: "2020-03-09"
        },
        {
          resourceId: "c",
          title: "event 3",
          start: "2020-03-08T12:00:00+00:00",
          end: "2020-03-09T06:00:00+00:00"
        },
        {
          resourceId: "f",
          title: "event 4",
          start: "2020-03-08T07:30:00+00:00",
          end: "2020-03-08T09:30:00+00:00"
        },
        {
          resourceId: "b",
          title: "event 5",
          start: "2020-03-08T10:00:00+00:00",
          end: "2020-03-08T15:00:00+00:00"
        },
        {
          resourceId: "e",
          title: "event 2",
          start: "2020-03-08T09:00:00+00:00",
          end: "2020-03-08T14:00:00+00:00"
        }
      ];
    }
  },
  methods: {
    onUnselectEvent(a) {
      console.log(a);
    },
    onUpdateSelectedEvent(a) {
      console.log(a);
    },
    onEventTimeChange(a) {
      console.log(a);
    },
    onSelect(a) {
      console.log(a);
    }
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
      schedulerLicenseKey="GPL-My-Project-Is-Open-Source"
      defaultView="resourceTimelineDay"
      aspectRatio="1"
      minTime="06:00:00"
      maxTime="23:00:00"
      @dateClick="onUnselectEvent($event)"
      @eventClick="onUpdateSelectedEvent($event)"
      @eventDrop="onEventTimeChange($event)"
      @eventResize="onEventTimeChange($event)"
      @select="onSelect()"
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
