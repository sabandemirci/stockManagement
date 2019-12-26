import Vue from 'vue'
import VueCookies from 'vue-cookies'
Vue.use(VueCookies)
import App from './App'
import router from './router'
import CoreuiVue from '@coreui/vue'
import { iconsSet as icons } from './assets/icons/icons.js'
import axios from "axios";
Vue.prototype.$http = axios;
Vue.config.performance = true
Vue.use(CoreuiVue)

new Vue({
  el: '#app',
  router,
  icons,
  template: '<App/>',
  components: {
    App
  },
})
