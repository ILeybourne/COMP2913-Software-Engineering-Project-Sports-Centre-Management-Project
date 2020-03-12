import axios from "@/services/axios.service";
import { getInstance as auth0 } from "@/services/auth.service";

/*TODO: Need to inject the authenticated HTTP Client, will require generating a singleton for Auth0 so
 *  we can use in services*/
export class TimetableService {
  async create() {}

  async read() {
    const auth = await auth0();
    console.log(auth);
    await auth.created();
    const token = await auth.getTokenSilently();

    const { data } = await axios.get("/timetable", {
      headers: {
        Authorization: `Bearer ${token}`
      }
    });

    console.log(data);
    this.message = data;
    return data;
  }

  update() {}

  delete() {}
}

const timetableService = new TimetableService();

export default timetableService;
