<template>
    <div>
        <div v-for="user in this.$store.state.news" :key="user.id">{{ user.title }}</div>
    </div>
</template>

<script>
// import { fetchNewsList } from '../api/index.js';

export default {
    // data() {
    //     return {
    //         users: [],
    //     };
    // },
    created() {
        this.$store.dispatch('FETCH_NEWS'); // action 수행
        // var vm = this;
        // fetchNewsList()
        //     .then(function (response) {
        //         console.log(response);
        //         vm.users = response.data;
        //     })
        //     .catch(function (error) {
        //         console.log(error);
        //     });
    },
};
</script>

<style></style>
