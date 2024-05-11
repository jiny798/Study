<template>
  <div>
    <ul>
      <li v-for="(todoItem, index) in todoItems" v-bind:key="todoItem.item" class="shadow">
        <font-awesome-icon  :icon="['fas','check']" v-on:click="toggleComplete(todoItem, index)"  class="checkBtn" 
          v-bind:class="{checkBtnCompleted: todoItem.completed}"/>
        
        <span v-bind:class="{textCompleted: todoItem.completed}">{{ todoItem.item }}</span>
        <span class="removeBtn" v-on:click="removeTodo(todoItem, index)">
          <i>X</i>
        </span>
      </li>
      
    </ul>
  </div>
</template>

<script>
export default {
  data:function(){
    return{
      todoItems: []
    }
  },
methods:{
  removeTodo: function(todoItem, index){    
    localStorage.removeItem(todoItem);
    this.todoItems.splice(index,1);
  },
  toggleComplete: function(todoItem, index){
    todoItem.completed = !todoItem.completed;
    console.log(index);
    localStorage.removeItem(todoItem.item);
    localStorage.setItem(todoItem.item, JSON.stringify(todoItem));
  }
}
  ,
  created:function () {
    if(localStorage.length > 0){
      for(var i = 0 ; i < localStorage.length ; i++){
        this.todoItems.push(JSON.parse(localStorage.getItem(localStorage.key(i))));
      }
    }
  }
}
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
</style>