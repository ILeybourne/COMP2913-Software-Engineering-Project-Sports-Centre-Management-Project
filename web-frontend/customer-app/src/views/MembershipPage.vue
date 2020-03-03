<template>
    <div class="booking-container">
        <div class="padding-div">
            <button type="button" class="btn btn-outline-secondary" style="padding:15px;">Back</button>
        </div>
        <div class="heading-div">
            <h1>Membership Options</h1>
        </div>
        <div>
            <MembershipOptions></MembershipOptions>
        </div>
    </div>
</template>

<style scoped>
    .padding-div{
        padding:15px;
    }

    .heading-div{
        margin: auto; width: 50%;
    }
</style>

<script>
    import MembershipOptions from "@/components/MembershipOptions.vue";

    // @ is an alias to /src
    export default {
        name: "MembershipPage",
        components: {
            MembershipOptions
        }
    };
</script>