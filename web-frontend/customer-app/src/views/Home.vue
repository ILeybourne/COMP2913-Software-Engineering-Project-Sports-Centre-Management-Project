<template>
    <div class="home">
        <Facility></Facility>
        <Facility></Facility>
        <BookingInformation></BookingInformation>
        <Test></Test>
    </div>
</template>

<script>
    // @ is an alias to /src
    //import HelloWorld from "@/components/HelloWorld.vue";
    import Test from "@/components/Test.vue";
    import BookingInformation from "@/components/BookingInformation.vue"
    import Facility from "@/components/Facility.vue"


    export default {
        name: "Home",
        components: {
//    HelloWorld,
            Test,
            BookingInformation,
            Facility
        }
    };
</script>
