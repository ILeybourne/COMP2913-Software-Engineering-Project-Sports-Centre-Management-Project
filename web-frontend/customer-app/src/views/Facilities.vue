<template>
    <div class="facility-container">
        <div class="outer-padding" ></div>
        <div class="inline-div">
            <div class="inner-div-5"></div>
            <Facility class="facilities"></Facility>
            <div class="inner-div-10"></div>
            <Facility class="facilities"></Facility>
        </div>
        <div class="outer-padding"></div>
        <div class="inline-div">
            <div class="inner-div-5"></div>
            <Facility class="facilities"></Facility>
            <div class="inner-div-10"></div>
            <Facility class="facilities"></Facility>

        </div>
    </div>
</template>

<style scoped>
    .outer-padding{
       padding: 10px;
    }

    .inline-div{
        display: flex;
        width: 100%;
    }

    .facilities{
        width: 40%;
    }

    .inner-div-5{
        width: 5%;
    }

    .inner-div-10{
        width: 10%;
    }
</style>

<script>
    import Facility from "@/components/Facility.vue";

    // @ is an alias to /src
    export default {
        name: "Facilities",
        components: {
            Facility
        }
    };
</script>