import { fetchNewsList, fetchJobsList, fetchAskList } from '@/api/index.js';

export default {
    FETCH_NEWS(context) {
        // mutations 를 호출하기 위한 context 제공
        fetchNewsList()
            .then((response) => {
                console.log(response);
                context.commit('SET_NEWS', response.data);
            })
            .catch((error) => {
                console.log(error);
            });
    },
    FETCH_JOBS({ commit }) {
        // mutations 를 호출하기 위한 context 제공
        fetchJobsList()
            .then(({ data }) => {
                commit('SET_JOBS', data);
            })
            .catch((error) => {
                console.log(error);
            });
    },
    FETCH_ASK({ commit }) {
        // mutations 를 호출하기 위한 context 제공
        fetchAskList()
            .then(({ data }) => {
                commit('SET_ASK', data);
            })
            .catch((error) => {
                console.log(error);
            });
    },
};
