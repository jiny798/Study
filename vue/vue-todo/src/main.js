import Vue from 'vue';
import App from './App.vue';
import { router } from './router/index.js';

import { library } from '@fortawesome/fontawesome-svg-core';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faCheck } from '@fortawesome/free-solid-svg-icons';
import { store } from './store/store';

library.add(faCheck);
Vue.component('font-awesome-icon', FontAwesomeIcon);
Vue.config.productionTip = false;

new Vue({
    store,
    // router: router, -> router 하나로 축약 가능 ES 문법
    router,
    render: (h) => h(App),
}).$mount('#app');
