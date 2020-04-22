import Vue from "vue";
import VueRouter from "vue-router";
import store from "@/store";
import Home from "@/views/Home.vue";
import TimetablePage from "@/views/TimetablePage";
import Facility from "@/views/Facilities";
import BookingInformation from "@/views/BookingPage";
import MembershipPage from "@/views/MembershipPage";
import Profile from "@/views/Profile";
import { authGuard } from "@/auth/helpers/auth.guard";
import BookingsTablePage from "@/views/BookingsTablePage";
import Test from "@/views/Test";
import ActivitiesTablePage from "@/views/ActivitiesTablePage";
import WeeklyUsageGraphPage from "@/views/WeeklyUsageGraphPage";
import FacilityPriceListing from "@/views/FacilityPriceListing";
import WeeklyUsagePage from "@/views/WeeklyUsagePage";
Vue.use(VueRouter);

const routes = [
  {
    path: "/",
    name: "Home",
    component: Home,
    meta: {
      title: "Zenergy | Home"
    }
  },
  {
    path: "/about",
    name: "About",
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () =>
      import(/* webpackChunkName: "about" */ "../views/About.vue"),
    meta: {
      title: "Zenergy | About"
    }
  },
  {
    path: "/profile",
    name: "Profile",
    component: Profile,
    beforeEnter: authGuard,
    meta: {
      //auth: true,
      title: "Zenergy | My Profile"
    }
  },
  {
    path: "/timetable",
    name: "Resource Timetable",
    component: TimetablePage,
    meta: {
      title: "Zenergy | Timetable - Sport & Physical activities"
    }
  },
  {
    path: "/timetable/:facilityName",
    name: "Resource Timetable",
    component: TimetableSinglePage,
    props: true,
    meta: {
      title: "Zenergy | Facility Timetable - Sport & Physical Activities"
    }
  },
  {
    path: "/facilities",
    name: "Facilities",
    component: Facility,
    meta: {
      title: "Zenergy | Facilities - Sport & Physical Activities"
    }
  },
  {
    path: "/test",
    name: "Test",
    component: Test
  },
  {
    path: "/bookings",
    name: "BookingPage",
    component: BookingInformation,
    meta: {
      title: "Zenergy | Booking"
    }
  },
  {
    path: "/membership",
    name: "MembershipPage",
    component: MembershipPage,
    meta: {
      title: "Zenergy | Membership"
    }
  },
  {
    path: "/bookingtable",
    name: "BookingTable",
    component: BookingsTablePage,
    beforeEnter: authGuard,
    meta: {
      title: "Zenergy | View Bookings"
    }
  },
  {
    path: "/activitiestable",
    name: "ActivitiesTable",
    component: ActivitiesTablePage,
    beforeEnter: authGuard,
    meta: {
      title: "Zenergy | Activity"
    }
  },
  {
    path: "/weeklyusagegraph",
    name: "WeeklyUsageGraph",
    component: WeeklyUsageGraphPage,
    beforeEnter: authGuard,
    meta: {
      title: "Zenergy | View Weekly Usage Graphically"
    }
  },
  {
    path: "/facilitypricelisting",
    name: "FacilityPriceListing",
    component: FacilityPriceListing,
    meta: {
      title: "Zenergy | Prices"
    }
  },
  {
    path: "/weeklyusage",
    name: "WeeklyUsage",
    component: WeeklyUsagePage,
    beforeEnter: authGuard,
    component: WeeklyUsagePage,
    meta: {
      title: "View Weekly Usage"
    }
  }
];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  linkActiveClass: "active",
  routes
});

router.beforeEach((to, from, next) => {
  store.dispatch("validation/clearValidationErrors");
  //Allows home title to be loaded on first visit
  const nearestWithTitle = to.matched
    .slice()
    .reverse()
    .find(r => r.meta && r.meta.title);
  if (nearestWithTitle) document.title = nearestWithTitle.meta.title;
  next();
});

export default router;
