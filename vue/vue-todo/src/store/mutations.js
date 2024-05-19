export default {
    SET_NEWS(state, news) {
        // mutations 는 state와 data를 받을 수 있음
        state.news = news;
    },
    SET_JOBS(state, jobs) {
        // mutations 는 state와 data를 받을 수 있음
        state.jobs = jobs;
    },
    SET_ASK(state, ask) {
        // mutations 는 state와 data를 받을 수 있음
        state.ask = ask;
    },
    SET_USER(state, user) {
        state.user = user;
    },
    SET_ITEM(state, item) {
        state.item = item;
    },
};
