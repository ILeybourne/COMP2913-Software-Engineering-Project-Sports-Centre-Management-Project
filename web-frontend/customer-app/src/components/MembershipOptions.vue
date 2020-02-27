<template>
    <div class="membership-options">
        <div class="membership-container">
            <div class="info-container">
                <div class="spacer"></div>
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
            </div>
            <div class="form-container">
                <form class="form-inline">

                    <div class="spacer"></div>
                    <div class="btn-container">
                        <input type="radio" id="annual" name="membership-type" value="annual">
                    </div>

                    <div class="spacer"></div>
                    <div class="btn-container">
                        <input type="radio" id="monthly" name="membership-type" value="monthly">

                    </div>
                    <div class="spacer"></div>
                    <div class="submit-container">
                        <button type="submit" value="submit"
                                class="btn btn-default">Buy Membership And Create Account</button>
                    </div>
                </form>
            </div>
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
        /*margin: auto;*/
        /*width: 50%;*/
        border: 3px solid #3183E5;
        border-radius: 10px;

        padding: 10px;
        min-height: 50%;
    }

    .info-container {
        display: flex;
        width: 100%;
        height: 500px;

    }

    .btn-container {
        margin: 0 auto

    }

    /*form {*/
    /*    diplay: flex;*/
    /*}*/

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
        width: 4%;
        height: 1%;
    }

    .form-container {
        display: flex;
    }

    .btn-container {
        margin: 0 auto;
        width: 100%;
    }

    .form-inline{
        display: flex;
        flex-flow: row wrap;
        align-items: center
    }
</style>

<script>
    export default {
        name: "BookingInformation",

    };
</script>
