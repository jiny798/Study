<template>
    <section>
        <transition-group name="list" tag="ul">
            <li v-for="(todoItem, index) in storedTodoItems" class="shadow" v-bind:key="todoItem.item">
                <font-awesome-icon
                    :icon="['fas', 'check']"
                    v-on:click="toggleComplete({ todoItem, index })"
                    class="checkBtn"
                    v-bind:class="{ checkBtnCompleted: todoItem.completed }"
                />
                <span v-bind:class="{ textCompleted: todoItem.completed }">{{ todoItem.item }}</span>
                <span class="removeBtn" v-on:click="removeTodo({ todoItem, index })">
                    <i>X</i>
                </span>
            </li>
        </transition-group>
    </section>
</template>

<script>
// toggleComplete({todoItem, index})
import { mapGetters, mapMutations } from 'vuex';
export default {
    methods: {
        ...mapMutations({
            removeTodo: 'removeOneItem', // 현재 컴포넌트의 메서드 : 호출할 mutation
            toggleComplete: 'toggleOneItem',
        }),
    },
    computed: {
        ...mapGetters(['storedTodoItems']),
    },
};
</script>

<style scoped>
ul {
    list-style-type: none;
    padding-left: 0px;
    margin-top: 0;
    text-align: left;
}
li {
    align-items: center; /*수직 정렬*/
    display: flex;
    min-height: 50px;
    height: 50px;
    line-height: 50px;
    margin: 0.5rem 0;
    padding: 0 0.9rem;
    background: white;
    border-radius: 5px;
}
.checkBtn {
    line-height: 45px;
    /* color: black; */
    color: #62acde;
    margin-right: 5px;
}
.checkBtnCompleted {
    /* color: #62acde; */
    color: black;
}
.textCompleted {
    text-decoration: line-through;
}
.removeBtn {
    margin-left: auto;
    color: #de4343;
}

/* transition css */
.list-enter-active,
.list-leave-active {
    transition: all 1s;
}
.list-enter, .list-leave-to /* .list-leave-active below version 2.1.8 */ {
    opacity: 0;
    transform: translateY(30px);
}
</style>
