<template>
    <div>
        <p v-for="item in this.$store.state.news">
            <a :href="item.url">{{ item.title }}</a>
            <small>{{ item.time_ago }} by {{ item.user }}</small>
        </p>
    </div>
</template>

<script>
// import { fetchNewsList } from '../api/index.js';

export default {
    created() {
        this.$store.dispatch('FETCH_NEWS'); // action 수행
    },
};
</script>

<style></style>
