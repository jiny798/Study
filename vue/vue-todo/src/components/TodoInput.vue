<template>
  <div>
    <input type="text" v-model="newTodoItem"> <!--v-model = data 값과 동기화 가능  -->
    <button v-on:click="addTodo">add</button>
  </div>
</template>

<script>
export default {
  data: function(){
    return{
      newTodoItem: ""
    }
  },
  methods:{
    addTodo:function(){
      console.log(this.newTodoItem); // this -> TodoInput 컴포넌트
      localStorage.setItem(this.newTodoItem, this.newTodoItem);
      this.newTodoItem = '';
    }
  }
}
</script>

<style>

</style>