import { fetchNewsList, fetchJobsList, fetchAskList, fetchUserInfo, fetchCommentItem } from '@/api/index.js';

export default {
    FETCH_NEWS(context) {
        // mutations 를 호출하기 위한 context 제공
        fetchNewsList()
            .then((response) => {
                context.commit('SET_NEWS', response.data);
                return response;
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
    FETCH_USER({ commit }, username) {
        fetchUserInfo(username)
            .then(({ data }) => {
                commit('SET_USER', data);
            })
            .catch((error) => {
                console.log(error);
            });
    },

    FETCH_ITEM({ commit }, id) {
        fetchCommentItem(id)
            .then(({ data }) => {
                commit('SET_ITEM', data);
            })
            .catch((error) => {
                console.log(error);
            });
    },
};
