import Vue from "vue";
import "./plugins/vuetify";
import App from "./App.vue";
import router from "./router";

Vue.config.productionTip = false;

Vue.mixin({
  data: function() {
    return {
      server: "http://localhost:40005/"
    };
  }
});

new Vue({
  router,
  render: function(h) {
    return h(App);
  }
}).$mount("#app");
