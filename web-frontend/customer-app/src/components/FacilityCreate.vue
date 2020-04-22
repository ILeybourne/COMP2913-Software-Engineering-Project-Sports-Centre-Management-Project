<template>
  <div v-if="!edit" class="facility-create">
    <h3>Create a new Facility</h3>
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
      <b-form-group>
        <b-form-file
          v-model="form.file"
          :state="Boolean(form.file)"
          placeholder="Choose a file or drop it here..."
          drop-placeholder="Drop file here..."
        ></b-form-file>
      </b-form-group>
      <div>
        <b-button block type="submit" variant="success"
          >Create Facility</b-button
        >
      </div>
    </b-form>
  </div>
  <div v-else class="facility-edit">
    <h3>Editing the component</h3>
  </div>
</template>

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

<style scoped></style>
