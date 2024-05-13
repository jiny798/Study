<template>
  <div id="app">
    <TodoHeader></TodoHeader>
    <TodoInput v-on:addTodoItem="addOneItem"></TodoInput>
    <todo-list v-bind:propsdata = "todoItems" 
    v-on:removeItem="removeOneItem" 
    v-on:toggleItem="toggleOneItem"></todo-list>
    <todo-footer v-on:clearAll="clearAllItems"></todo-footer>
  </div>
</template>

<script>
import TodoHeader from './components/TodoHeader.vue'
import TodoInput from './components/TodoInput.vue'
import TodoList from './components/TodoList.vue'
import TodoFooter from './components/TodoFooter.vue'

export default {
  data(){
    return{
      todoItems: []
    }
  },
  methods:{
    addOneItem(todoItem){
      const obj = { completed: false, item: todoItem};
      localStorage.setItem(todoItem, JSON.stringify(obj));
      this.todoItems.push(obj);
    },
    removeOneItem(todoItem, index){
      localStorage.removeItem(todoItem.item);
      this.todoItems.splice(index,1);
    },
    toggleOneItem(todoItem, index){
      // todoItem.completed = !todoItem.completed;
      // todoItem을 받아서 바꾸는 것보다, index만 받아서 직접 todoItems 변경 
      this.todoItems[index].completed = !this.todoItems[index].completed; 
      localStorage.removeItem(todoItem.item);
      localStorage.setItem(todoItem.item, JSON.stringify(todoItem));
    },
    clearAllItems(){
      console.log('clearAllItems');
      localStorage.clear();
      this.todoItems = [];
    }
  }
  ,
  created() {
    if(localStorage.length > 0){
      for(let i = 0 ; i < localStorage.length ; i++){
        this.todoItems.push(JSON.parse(localStorage.getItem(localStorage.key(i))));
      }
    }
  },
  components:{
    'TodoHeader' : TodoHeader,
    'TodoInput' : TodoInput,
    'TodoList' : TodoList,
    'TodoFooter' : TodoFooter
  }
}

</script>

<style>

body{
  text-align: center;
  background-color: #f6f6f6;
}

input{
  border-style: groove;
  width: 200px;
}
.shadow{
  box-shadow: 5px 10px 10px rgba(0, 0, 0, 0.03);
}

button{
  border-style: groove;
}

</style>
