import bus from '../utils/bus.js';

export default {
    // 재사용할 컴포넌트 옵션 + 로직
    created() {
        bus.$emit('start:spinner');
        console.log('start:spinner');
        this.$store
            .dispatch('FETCH_LIST', this.$route.name)
            .then(() => {
                console.log('fetched');
                bus.$emit('end:spinner');
                console.log('end:spinner');
            })
            .catch((error) => {
                console.log(error);
            });
    },
};
