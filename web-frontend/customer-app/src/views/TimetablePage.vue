<template>
    <div class="timetable">
        <h1>Timetable</h1>
        <Timetable></Timetable>
    </div>
</template>

<script>
    import Timetable from "../components/Timetable";
    // import index.js from "../router/index.js";

    // @ is an alias to /src
    export default {
        name: "TimetablePage",
        components: {
            Timetable,
        },

    };
</script>
