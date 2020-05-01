import Vue from "vue";
import VueRouter from "vue-router";
import store from "@/store";
import Home from "@/views/Home.vue";
import TimetablePage from "@/views/TimetablePage";
import BookingInformation from "@/views/BookingPage";
import MembershipPage from "@/views/MembershipPage";
import Profile from "@/views/Profile";
import { authGuard } from "@/auth/helpers/auth.guard";
import BookingsTablePage from "@/views/BookingsTablePage";
import ActivitiesTablePage from "@/views/ActivitiesTablePage";
import WeeklyUsageGraphPage from "@/views/WeeklyUsageGraphPage";
import FacilityPriceListing from "@/views/FacilityPriceListing";
import WeeklyUsagePage from "@/views/WeeklyUsagePage";
import FacilityPage from "@/views/FacilityPage";
import PageNotFound from "@/views/PageNotFound";
import Unauthorised from "@/views/Unauthorised";
import ServerError from "@/views/ServerError";
import FacilityTimetable from "@/components/FacilityTimetable";
import FacilityCreate from "@/components/FacilityCreate";
import Checkout from "@/views/Checkout";

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
    // Optional id
    path: "/facilities/:id?",
    name: "FacilityPage",
    component: FacilityPage,
    props: true,
    meta: {
      title: "Zenergy | Facilities - Sport & Physical Activities"
    },
    children: [
      {
        path: "timetable",
        component: FacilityTimetable,
        name: "FacilityTimetable",
        props: route => ({ facilityId: Number(route.params.id) })
      },
      {
        path: "edit",
        component: FacilityCreate,
        name: "FacilityEdit",
        props: route => ({ facilityId: Number(route.params.id), edit: true })
      }
    ]
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
    meta: {
      title: "View Weekly Usage"
    }
  },
  {
    path: "/unauthorised",
    name: "Unauthorised",
    component: Unauthorised
  },
  {
    path: "*",
    component: PageNotFound
  },
  {
    path: "/checkout",
    name: "Checkout",
    component: Checkout
  },
  {
    path: "/error",
    name: "ServerError",
    component: ServerError
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
