<script>

    import FullCalendar from '@fullcalendar/vue'
    import dayGridPlugin from '@fullcalendar/daygrid'
    import resourceTimelinePlugin from '@fullcalendar/resource-timeline';

    var demoData = {
        "resource_1": {
            name: "Astro Turf",
            activities: [
                {
                    activity_id: "3",
                    booking_id: "1312312",
                    start_time: "",
                    end_time: ""
                },
                {
                    activity_id: "5",
                    booking_id: "78654342",
                    start_time: "",
                    end_time: ""
                },
                {
                    booking_id: "7564323",
                    start_time: "",
                    end_time: ""
                },
                {
                    booking_id: "253445",
                    start_time: "",
                    end_time: ""
                },
            ]
        },
        "resource_2": {
            name: "Squash Court",
            activities: [
                {
                    activity_id: "3",
                    booking_id: "1312312",
                    start_time: "",
                    end_time: ""
                },
                {
                    activity_id: "5",
                    booking_id: "78654342",
                    start_time: "",
                    end_time: ""
                },
                {
                    booking_id: "7564323",
                    start_time: "",
                    end_time: ""
                },
                {
                    booking_id: "253445",
                    start_time: "",
                    end_time: ""
                },
            ]
        }
    };


    function f() {
        // console.log(demoData);
        var resourcesOutput =[];


        // var resourceBuilding = Object.keys(demoData);

        var resourcesArray = [];
        const resources = Object.entries(demoData);
        // console.log(resources)
        // console.log(resources[0][1])
        // for (var res in resources){
        //     console.log(res)
        // }

        Object.keys(resources).forEach((resource) => {
            // console.log(resources[resource]);
            // console.log(resource)
            resourcesArray.push(resources[resource])
            if (typeof resources[resource] === 'object') {
                // console.log("hello")
                // console.log(resources[resource])
            }
            // Object.keys(resource[1]).forEach((el) => {
            //     // console.log(el)
            // })
        });

        // console.log(resourcesArray)

        for (var res in resourcesArray) {
            for (var attr in resourcesArray[res]) {


                if (typeof resourcesArray[res][attr] === 'object') {
                    // console.log("hello")
                    // console.log(resources[res][attr]);
                    // console.log(resources[res][attr].name);
                    var objRes = {id: (String.fromCharCode(97 + n)),
                        res, title: resources[res][attr].name};
                    resourcesOutput.push(objRes);
                }
            }
        }
        console.log(resourcesOutput)
        return resourcesOutput;
        // for (var resource in resources){
        //     console.log(resource);
        //
        //     if (typeof resource === 'object') {
        //         console.log(resource);
        //         console.log("hello")
        //     }
        // }
        // for (var resource in demoData) {
        //     console.log(resource);
        //     //Resource for locations' headers?
        //
        //     for (var el in resource){
        //         console.log(el);
        //     }
        // }
    };

    var newResources = f();

    export default {
        name: "Timetable",
        components: {
            FullCalendar // make the <FullCalendar> tag available
        },
        data() {
            return {
                calendarPlugins: [dayGridPlugin, resourceTimelinePlugin],
                resources: newResources,
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
                    left: 'today prev,next',
                    center: 'title',
                    right: 'resourceTimelineDay,resourceTimelineWeek'
                },
            }
        }
    }

</script>


<!--Add groupings-->
<!--resourceGroupField="building"-->
<template>
    <FullCalendar schedulerLicenseKey="GPL-My-Project-Is-Open-Source"
                  defaultView="resourceTimelineDay"
                  aspectRatio="1"
                  events="https://fullcalendar.io/demo-events.json?single-day&for-resource-timeline"
                  :plugins="calendarPlugins"
                  :header="header"
                  :resources="resources"/>
</template>

<style lang='scss'>

    @import '~@fullcalendar/core/main.css';
    @import '~@fullcalendar/daygrid/main.css';
    @import '~@fullcalendar/resource-timeline/main.css';

</style>