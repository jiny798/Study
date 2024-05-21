<template>
    <div class="user-container">
        <div>
            <font-awesome-icon class="fa-user" :icon="['fas', 'user']" />
        </div>
        <div class="user-description">
            <slot name="username"></slot>

            <div class="time">
                <!-- {{ info.created }} -->
                <slot name="time"></slot>
            </div>
            <div>
                <slot name="karma"></slot>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    props: {
        info: Object,
    },
    // computed: {
    //     userInfo() {
    //         return this.$store.state.user;
    //     },
    // },
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
