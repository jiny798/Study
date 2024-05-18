<template>
    <div>
        <div v-for="item in fetchedAsk">{{ item.title }}</div>
    </div>
</template>

<script>
import { mapState, mapGetters } from 'vuex';
export default {
    computed: {
        // ...mapGetters(['fetchedAsk']),
        // ...mapGetters({
        //     fetchedAsk: 'fetchedAsk',
        // }),
        ...mapState({
            fetchedAsk: (state) => state.ask,
        }),
        // ask() {
        //     return this.$store.state.ask;
        // },
    },
    created() {
        this.$store.dispatch('FETCH_ASK');
    },
    beforeMount() {},
    mounted() {},
};
</script>

<style></style>
