<template>
  <div class="inputBox shadow">
    <input type="text" v-model="newTodoItem" v-on:keyup.enter="addTodo"> <!--v-model = data 값과 동기화 가능  -->
    <!-- <button v-on:click="addTodo">add</button> -->
    <span class="addContainer" v-on:click="addTodo">
      <i class="">+</i>
    </span>

    <ModalView v-if="showModal" @close="showModal = false">
        <h3 slot="header">경고</h3>
        <div slot="body">아무값도 입력하지 않았습니다. 
          <div><i @click="showModal = false"> 확인 </i></div>
          
          
        </div>

    </ModalView>
  </div>
</template>

<script>
import ModalView from './common/ModalView.vue';

export default {
  data: function(){
    return{
      newTodoItem: "",
      showModal: false,
    }
  },
  methods:{
    // https://v2.vuejs.org/v2/examples/modal.html 모달 사이트 
    addTodo(){
      if(this.newTodoItem !== ''){
        // this.$emit('이벤트 이름', 인자1,인자2);
        // this.$emit('addTodoItem',this.newTodoItem );
        const text = this.newTodoItem.trim();
        this.$store.commit('addOneItem', text);
        this.clearInput();
      }else{
        this.showModal = !this.showModal;
      }
    },
    clearInput(){
      this.newTodoItem = '';
    }
  },
  components:{
    ModalView : ModalView,
  }
}
</script>

<style scoped>
input:focus {
  outline: none;
}

.inputBox{
  background: white;
  height: 50px; 
  line-height: 50px;
  border-radius: 5px;
}

.inputBox input {
  width: inherit;
  border-style: none;
  font-size: 0.9rem;
}

.addContainer{
  float: right;
  background: linear-gradient(to right, #6478FB, #8763FB);
  display: block;
  width: 3rem;
  border-radius: 0 5px 5px 0;
}

.addBtn {
  color: white;
  vertical-align: middle;
}

</style>