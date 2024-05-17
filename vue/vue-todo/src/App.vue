<template>
    <div id="app">
        <tool-bar></tool-bar>
        <router-view></router-view>

        <TodoHeader></TodoHeader>
        <TodoInput></TodoInput>
        <TodoList></TodoList>
        <TodoFooter></TodoFooter>
    </div>
</template>

<script>
import TodoHeader from './components/TodoHeader.vue';
import TodoInput from './components/TodoInput.vue';
import TodoList from './components/TodoList.vue';
import TodoFooter from './components/TodoFooter.vue';
import ToolBar from './components/ToolBar.vue';

export default {
    data() {
        return {
            todoItems: [],
        };
    },
    methods: {},

    components: {
        TodoHeader,
        TodoInput,
        TodoList,
        TodoFooter,
        ToolBar,
    },
};
</script>

<style>
body {
    padding: 0;
    margin: 0;
    text-align: center;
    background-color: #f6f6f6;
}

input {
    border-style: groove;
    width: 200px;
}
.shadow {
    box-shadow: 5px 10px 10px rgba(0, 0, 0, 0.03);
}

button {
    border-style: groove;
}
</style>
