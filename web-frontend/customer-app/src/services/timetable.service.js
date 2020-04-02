import authHttp from "@/services/auth-http.service";

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
