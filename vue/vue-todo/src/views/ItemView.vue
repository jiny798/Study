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

<style scoped>
.user-container {
    display: flex; /*내부 div 가로로*/
    align-items: center; /* 요소 하나씩 수직 중앙 정렬 */
    padding: 0.5rem;
}
.fa-user {
    font-size: 2.5rem;
}
.user-description {
    padding-left: 8px;
}
.time {
    font-size: 0.7rem;
}
</style>
