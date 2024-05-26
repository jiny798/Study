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
import bus from '../utils/bus.js';
import ListMixin from '../mixins/ListMixin.js';
export default {
    components: {
        ListItem,
    },
    mixins: [ListMixin],
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
    created() {
        // this.$store.dispatch('FETCH_ASK');
        // bus.$emit('start:spinner');
        // this.$store
        //     .dispatch('FETCH_ASK')
        //     .then(() => {
        //         console.log('fetched');
        //         bus.$emit('end:spinner');
        //     })
        //     .catch((error) => {
        //         console.log(error);
        //     });
        // bus.$emit('end:spinner');
    },
    // beforeMount() {},
    // mounted() {
    //     bus.$emit('end:spinner');
    // },
};
</script>

<style></style>
