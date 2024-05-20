<template>
    <div>
        <section>
            <!--질문 정보-->
            <div class="user-container">
                <div>
                    <font-awesome-icon class="fa-user" :icon="['fas', 'user']" />
                </div>
                <div class="user-description">
                    <router-link :to="`/user/${fetchedItem.user}`">
                        {{ fetchedItem.user }}
                    </router-link>
                    <div class="time">
                        {{ fetchedItem.time_ago }}
                    </div>
                </div>
            </div>
        </section>

        <section>
            <!--댓글-->
            <!-- <div v-html="fetchedItem.content"></div> -->
            <h2>{{ fetchedItem.title }}</h2>
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

export default {
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
