<template>
    <div>
        <list-item></list-item>
        <!-- <ul class="news-list">
            <li v-for="item in fetchedAsk" class="post">
                <div class="points">
                    {{ item.points }}
                </div>

  
                <div>
                    <p class="news-title">
                        <router-link :to="`item/${item.id}`">
                            {{ item.title }}
                        </router-link>
                    </p>
                    <small class="link-text">
                        {{ item.time_ago }} by
                        <router-link :to="`/user/${item.user}`" class="link-text">{{ item.user }}</router-link>
                    </small>
                </div>
            </li>
        </ul> -->
    </div>
</template>

<script>
// import { mapState, mapGetters } from 'vuex';
import ListItem from '../components/ListItem.vue';

export default {
    components: {
        ListItem,
    },
    created() {},
    // computed: {
    // ...mapGetters(['fetchedAsk']),
    // ...mapGetters({
    //     fetchedAsk: 'fetchedAsk',
    // }),
    // ...mapState({
    //     fetchedAsk: (state) => state.ask,
    // }),
    // ask() {
    //     return this.$store.state.ask;
    // },
    // },
    // created() {
    //     this.$store.dispatch('FETCH_ASK');
    // },
    // beforeMount() {},
    // mounted() {},
};
</script>

<style></style>
