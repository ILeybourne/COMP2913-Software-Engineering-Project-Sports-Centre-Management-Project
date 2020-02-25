<template>
    <div class="membership-options">
        <div class="membership-container">
            <div class="spacer"></div>
            <form>
                <div class="annual-membership-details">
                    Annual Membership Details
                </div>
                <div class="spacer"></div>

                <div class="monthly-membership-details">
                    Monthly Membership Details
                </div>
                <div class="spacer"></div>

                <div class="account-creation-form">
                    Account Creation Form
                </div>
            </form>
        </div>
    </div>
</template>

<style scoped>
    .membership-options {
        padding-top: 5%;
        padding-bottom: 5%;
        min-height: 50%;
        max-height: 50%;
    }

    .membership-container {
        display: flex;
        /*margin: auto;*/
        /*width: 50%;*/
        border: 3px solid #3183E5;
        border-radius: 10px;

        padding: 10px;
        min-height: 50%;
        height: 500px;
    }


    .annual-membership-details {
        width: 20%;
        border: 3px solid #3183E5;
        border-radius: 10px;
        min-height: 50%;
    }

    .monthly-membership-details {
        width: 20%;
        border: 3px solid #3183E5;
        border-radius: 10px;
        min-height: 50%;

    }

    .account-creation-form {
        width: 40%;
        border: 3px solid #3183E5;
        border-radius: 10px;
        min-height: 50%;

    }

    .spacer {
        width: 5%;
    }
</style>

<script>
    export default {
        name: "BookingInformation",

    };
</script>
