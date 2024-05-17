<template>
    <div>
        <div v-for="job in jobs">{{ job.title }}</div>
    </div>
</template>

<script>
import { fetchJobsList } from '../api/index.js';
export default {
    data() {
        return {
            jobs: [],
        };
    },
    created() {
        // 컴포넌트가 생성되자마자 실행되는 라이프 사이클 훅
        fetchJobsList()
            .then((response) => (this.jobs = response.data))
            .catch((error) => console.log(error));
    },
    beforeMount() {},
    mounted() {},
};
</script>

<style></style>
