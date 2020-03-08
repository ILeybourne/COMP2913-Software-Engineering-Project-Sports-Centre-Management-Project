import axios from "axios";
export default class ResourceAPI {
  async create() {
    const token = await this.$auth.getTokenSilently();

    const { data } = await axios.get("http://localhost:8000/resources", {
      headers: {
        Authorization: `Bearer ${token}`
      }
    });
    console.log(data);
    this.message = data;
  }
  read() {}
  update() {}
  delete() {}
}
