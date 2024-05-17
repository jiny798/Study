<template>
    <div>
        <div v-for="item in ask">{{ item.title }}</div>
    </div>
</template>

<script>
import { fetchAskList } from '../api/index.js';
export default {
    data() {
        return {
            ask: [],
        };
    },
    created() {
        // 컴포넌트가 생성되자마자 실행되는 라이프 사이클 훅
        var vm = this;
        fetchAskList()
            .then(function (response) {
                vm.ask = response.data;
            })
            .catch(function (error) {
                console.log(error);
            });
    },
    beforeMount() {},
    mounted() {},
};
</script>

<style></style>
