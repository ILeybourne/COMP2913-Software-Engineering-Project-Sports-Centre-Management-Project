<template>
    <div class="home">
        <HelloWorld></HelloWorld>
        <Facility></Facility>
        <Facility></Facility>
        <BookingInformation></BookingInformation>
        <Timetable></Timetable>
    </div>
</template>

<script>
    // @ is an alias to /src
    //import HelloWorld from "@/components/HelloWorld.vue";
    import Timetable from "@/components/Timetable.vue";
    import BookingInformation from "@/components/BookingInformation.vue"
    import Facility from "@/components/Facility.vue"
    import HelloWorld from "@/components/HelloWorld.vue"


    export default {
        name: "Home",
        components: {
            HelloWorld,
            Timetable,
            BookingInformation,
            Facility
        }
    };
</script>
