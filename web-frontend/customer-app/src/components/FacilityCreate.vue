<template>
  <div v-if="!edit" class="facility-create">
    <div class="outer-container">
      <v-card dark class="info-container">
        <h3>Create a <span>new Facility</span></h3>
        <b-form @submit.prevent="submit">
          <b-form-group label="Facility Name: " label-for="name">
            <b-form-input id="name" v-model="form.name" type="text" />
          </b-form-group>
          <b-form-group label="Description: " label-for="description">
            <b-form-textarea
              id="description"
              v-model="form.description"
            ></b-form-textarea>
          </b-form-group>
          <b-form-group class="file-form">
            <b-form-file
              v-model="form.file"
              :state="Boolean(form.file)"
              placeholder="Choose a file or drop it here..."
              drop-placeholder="Drop file here..."
            ></b-form-file>
          </b-form-group>
          <v-container class="submit-button-container">
            <b-button
              title="Submit Facility Data"
              id="button"
              class="site-btn"
              block
              type="submit"
              variant="outline-light"
              >Create Facility</b-button
            >
          </v-container>
        </b-form>
      </v-card>
    </div>
  </div>
  <div v-else class="facility-edit">
    <h3>Editing the component</h3>
  </div>
</template>
<style scoped>
.facility-create {
  padding: 59px 0px 59px 0px;
  min-height: 50%;
  height: 100%;
}
.outer-container {
  /*margin: auto;*/
  /*width: 50%;*/
  padding: 10px;
  min-height: 50%;
  display: flex;
  justify-content: center;
}

.info-container {
  color: white !important;
  display: flex;
  justify-content: center;
  flex-direction: column;
  width: auto;
  min-width: 75%;
  height: max-content;
  padding: 40px;
  box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);
}
.info-container h3 {
  padding-bottom: 20px;
  text-align: center;
  color: white;
}
.submit-button-container {
  display: flex;
  justify-content: center;
  text-align: center;
}
#button {
  width: max-content !important;
}
span {
  color: #1f1f1f;
  background: #fcff18;
  padding: 10px;
}
.form-control:focus {
  border-color: yellow;
  box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.075), 0 0 8px rgba(255, 255, 0);
}
.form-control {
  border: 2px solid white;
}
</style>
<script>
import { mapActions } from "vuex";

export default {
  name: "CreateFacility",
  props: {
    edit: {
      type: Boolean,
      required: false,
      default: false
    }
  },
  data() {
    return {
      form: {
        name: null,
        description: null,
        file: null
        // Image
      }
    };
  },
  methods: {
    async submit() {
      // Validate
      console.log(this.form);
      await this.createFacility(this.form);
      this.$router.push({
        name: "FacilityPage"
      });
    },
    ...mapActions("facilities", ["createFacility"])
  }
};
</script>
