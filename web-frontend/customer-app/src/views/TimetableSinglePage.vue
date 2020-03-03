<template>
    <div class="timetable">
        <h1>Timetable</h1>
        {{facilityName}}
        <TimetableSingle></TimetableSingle>
<!--        <router-link :to="{ name: 'Resource Timetable', params: {facility: detail.name } }">more..</router-link>-->
<!--        {{}}-->
    </div>
</template>

<script>
    import TimetableSingle from "../components/TimetableSingle";
    // import index.js from "../router/index.js";

    // @ is an alias to /src
    export default {
        name: "TimetableSinglePage",
        data(){
            return{
                facilityName: this.$route.params.facilityName
            }
        } ,
        components: {
            TimetableSingle,
        },

    };
</script>
