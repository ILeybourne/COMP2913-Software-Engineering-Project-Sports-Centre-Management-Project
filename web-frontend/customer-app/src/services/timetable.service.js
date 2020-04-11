import authHttp from "@/plugins/axios.plugin";

export class TimetableService {
  async create() {}

  async read() {
    const { data } = await authHttp.get("/timetable");
    return data;
  }
  update() {}
  delete() {}
}

const timetableService = new TimetableService();

export default timetableService;
