import Vue from "vue";
import VueRouter from "vue-router";
import Home from "../views/Home.vue";
import TimetablePage from "../views/TimetablePage";
import TimetableSinglePage from "../views/TimetableSinglePage";
import Facility from "../views/Facilities"
import BookingInformation from "../views/BookingPage"
import MembershipPage from "../views/MembershipPage";

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
    path: "/bookings",
    name: "BookingPage",
    component: BookingInformation

  },
  {
    path: "/membership",
    name: "MembershipPage",
    component: MembershipPage

  }
];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes
});

export default router;
