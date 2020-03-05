<script>
import FullCalendar from "@fullcalendar/vue";
import dayGridPlugin from "@fullcalendar/daygrid";
import resourceTimelinePlugin from "@fullcalendar/resource-timeline";

//TODO pull from db 1 facility

var demoData2 = {
  content: [
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

//TODO write functions in form of adapter (https://www.dofactory.com/javascript/adapter-design-pattern)
//functions for new data format
function dataToResourceFormat() {
  var resourcesArray = [];

  for (var resource in demoData2.content) {
    // console.log(demoData2.content[resource])
    var resourceObject = {
      // id: (String.fromCharCode(96 + demoData2.content[resource].id)),
      id: demoData2.content[resource].id,
      title: demoData2.content[resource].name
    };

    resourcesArray.push(resourceObject);
  }
  return resourcesArray;
}

function dataToEventFormat() {
  var eventArray = [];

  for (var resource in demoData2.content) {
    for (var act in demoData2.content[resource].activities) {
      var eventObj = {
        id: demoData2.content[resource].activities[act].id,
        resourceId: demoData2.content[resource].id,
        title: demoData2.content[resource].activities[act].name,
        start: demoData2.content[resource].activities[act].start_time,
        end: demoData2.content[resource].activities[act].end_time
      };
      eventArray.push(eventObj);
    }
  }
  return eventArray;
}

var newResources = dataToResourceFormat();
var newEvents = dataToEventFormat();

console.log(newEvents);
console.log(newResources);

export default {
  name: "Timetable",
  components: {
    FullCalendar // make the <FullCalendar> tag available
  },
  props: {
    eventData: {
      type: Array
    },
    facilityName: {
      type: String
    }
  },
  data() {
    return {
      calendarPlugins: [dayGridPlugin, resourceTimelinePlugin],
      resources: newResources,
      events: newEvents,
      //     [
      //     {id: 'a', building: '460 Bryant', title: 'Auditorium A'},
      //     {id: 'b', building: '460 Bryant', title: 'Auditorium B'},
      //     {id: 'c', building: '460 Bryant', title: 'Auditorium C'},
      //     {id: 'd', building: '460 Bryant', title: 'Auditorium D'},
      //     {id: 'e', building: '460 Bryant', title: 'Auditorium E'},
      //     {id: 'f', building: '460 Bryant', title: 'Auditorium F'},
      //     {id: 'g', building: '564 Pacific', title: 'Auditorium G'},
      //     {id: 'h', building: '564 Pacific', title: 'Auditorium H'},
      //     {id: 'i', building: '564 Pacific', title: 'Auditorium I'},
      //     {id: 'j', building: '564 Pacific', title: 'Auditorium J'},
      //     {id: 'k', building: '564 Pacific', title: 'Auditorium K'},
      //     {id: 'l', building: '564 Pacific', title: 'Auditorium L'},
      //     {id: 'm', building: '564 Pacific', title: 'Auditorium M'},
      //     {id: 'n', building: '564 Pacific', title: 'Auditorium N'},
      //     {id: 'o', building: '101 Main St', title: 'Auditorium O'},
      //     {id: 'p', building: '101 Main St', title: 'Auditorium P'},
      //     {id: 'q', building: '101 Main St', title: 'Auditorium Q'},
      //     {id: 'r', building: '101 Main St', title: 'Auditorium R'},
      //     {id: 's', building: '101 Main St', title: 'Auditorium S'},
      //     {id: 't', building: '101 Main St', title: 'Auditorium T'},
      //     {id: 'u', building: '101 Main St', title: 'Auditorium U'},
      //     {id: 'v', building: '101 Main St', title: 'Auditorium V'},
      //     {id: 'w', building: '101 Main St', title: 'Auditorium W'},
      //     {id: 'x', building: '101 Main St', title: 'Auditorium X'},
      //     {id: 'y', building: '101 Main St', title: 'Auditorium Y'},
      //     {id: 'z', building: '101 Main St', title: 'Auditorium Z'}
      // ],
      header: {
        left: "today prev,next",
        center: "title",
        right: "resourceTimelineDay,resourceTimelineWeek"
      }
    };
  }
};
</script>

<!--Add groupings-->
<!--resourceGroupField="building"-->
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
