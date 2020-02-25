<template>
    <div class="booking-container">
        <div style="padding:15px;">
            <button type="button" class="btn btn-outline-secondary" style="padding:15px;">Back</button>
        </div>
        <div style="   margin: auto; width: 50%;">
            <h1>Membership Options</h1>
        </div>
        <div>
            <MembershipOptions></MembershipOptions>
        </div>
    </div>
</template>

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