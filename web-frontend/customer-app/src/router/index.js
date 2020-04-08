import Vue from "vue";
import VueRouter from "vue-router";
import store from "@/store";
import Home from "@/views/Home.vue";
import TimetablePage from "@/views/TimetablePage";
import TimetableSinglePage from "@/views/TimetableSinglePage";
import Facility from "@/views/Facilities";
import BookingInformation from "@/views/BookingPage";
import MembershipPage from "@/views/MembershipPage";
import Profile from "@/views/Profile";
import { authGuard } from "@/auth/helpers/auth.guard";
import BookingsTablePage from "@/views/BookingsTablePage";
import Test from "@/views/Test";
import ActivitiesTablePage from "@/views/ActivitiesTablePage";
import WeeklyUsagePage from "@/views/WeeklyUsagePage";

Vue.use(VueRouter);

const routes = [
  {
    path: "/",
    name: "Home",
    component: Home
  },
  {
    path: "/about",
    name: "About",
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () =>
      import(/* webpackChunkName: "about" */ "../views/About.vue")
  },
  {
    path: "/profile",
    name: "Profile",
    component: Profile,
    beforeEnter: authGuard
  },
  {
    path: "/timetable",
    name: "Resource Timetable",
    component: TimetablePage
  },
  {
    path: "/timetable/:facilityName",
    name: "Resource Timetable",
    component: TimetableSinglePage,
    props: true
  },
  {
    path: "/facilities",
    name: "Facilities",
    component: Facility
  },
  {
    path: "/test",
    name: "Test",
    component: Test
  },
  // {
  //   path: "/bookings?activityId=:",
  //   name: "BookingPageByActivityId",
  //   component: BookingInformation,
  //   beforeEnter: authGuard
  // },
  {
    path: "/bookings",
    name: "BookingPage",
    component: BookingInformation
  },
  {
    path: "/membership",
    name: "MembershipPage",
    component: MembershipPage
  },
  {
    path: "/bookingtable",
    name: "BookingTable",
    component: BookingsTablePage
  },
  {
    path: "/activitiestable",
    name: "ActivitiesTable",
    component: ActivitiesTablePage,
    beforeEnter: authGuard
  },
  {
    path: "/weeklyusage",
    name: "WeeklyUsageGraph",
    component: WeeklyUsagePage,
    beforeEnter: authGuard
  }
];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes
});

router.beforeEach((to, from, next) => {
  store.dispatch("validation/clearValidationErrors");

  next();
});

export default router;
