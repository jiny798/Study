<template>
    <div>
        <p v-for="job in this.$store.state.jobs">
            <a :href="job.url">{{ job.title }}</a>
            <small>{{ job.time_ago }},{{ job.domain }}</small>
        </p>
    </div>
</template>

<script>
export default {
    created() {
        // 컴포넌트가 생성되자마자 실행되는 라이프 사이클 훅
        this.$store.dispatch('FETCH_JOBS');
    },
    beforeMount() {},
    mounted() {},
};
</script>

<style></style>
