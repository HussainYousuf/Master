import Vue from "vue";
import Router from "vue-router";
import Bill from "./views/bill.vue";
import Supplier from "./views/supplier.vue";
import Inventory from "./views/inventory.vue";
import Index from "./views/index.vue";
import Dashboard from "./views/dashboard.vue";
import Supplier_details from "./views/supplier_details.vue";
import View_Orders from "./views/view_orders.vue";
import purchases from "./views/purchases.vue";




Vue.use(Router);

export default new Router({
  mode: "history",
  routes: [
    {
      path: "/",
      name: "home",
      component: Index
    },
    {
      path: "/bill/:id",
      name: "bill",
      component: Bill,
      props: true
    },
    {
      path: "/supplier",
      name: "supplier",
      component: Supplier
    },
    {
      path: "/supplier_details",
      name: "supplier_details",
      component: Supplier_details
    },
    {
      path: "/inventory",
      name: "inventory",
      component: Inventory
    },
    {
      path: "/view_orders",
      name: "view_orders",
      component: View_Orders
    },
    {
      path: "/purchases",
      name: "purchases",
      component: purchases
    }
  ]
});
