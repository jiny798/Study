<template>
    <div>
        <section>
            <!--사용자 정보-->
            <user-profile :info="fetchedItem">
                <div slot="username">{{ fetchedItem.user }}</div>
                <template slot="time">{{ fetchedItem.time_ago }}</template>
            </user-profile>
        </section>
        <section>
            <h2>{{ fetchedItem.title }}</h2>
        </section>
        <section>
            <!--댓글-->
            <!-- <div v-html="fetchedItem.content"></div> -->
            <div v-html="fetchedItem.content"></div>
        </section>
        <!-- <p>{{ fetchedItem.title }}</p>
        <div>
            {{ fetchedItem.content }}
        </div> -->
    </div>
</template>

<script>
import { mapGetters } from 'vuex';
import UserProfile from '../components/UserProfile.vue';

export default {
    components: {
        UserProfile,
    },
    computed: {
        ...mapGetters(['fetchedItem']),
    },
    created() {
        const itemId = this.$route.params.id;
        this.$store.dispatch('FETCH_ITEM', itemId);
    },
};
</script>

<style scoped></style>
