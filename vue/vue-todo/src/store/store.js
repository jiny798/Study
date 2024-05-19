import Vue from 'vue';
import Vuex from 'vuex';
// import * as getters from './getters';
// import * as mutations from './mutations';
import todoApp from './modules/todoApp';
import mutations from './mutations';
import actions from './actions';

Vue.use(Vuex); // Vuex 라이브러리 초기화

export const store = new Vuex.Store({
    state: {
        news: [],
        jobs: [],
        ask: [],
        user: {},
        item: [],
    },
    getters: {
        fetchedAsk(state) {
            return state.ask;
        },
        fetchedItem(state) {
            return state.item;
        },
    },
    mutations: mutations,
    actions,

    modules: {
        todoApp,
    },
});
