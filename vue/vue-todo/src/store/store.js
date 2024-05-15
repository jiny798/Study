import Vue from 'vue'
import Vuex from 'vuex';

Vue.use(Vuex); // Vuex 라이브러리 초기화 

const storage = {
    fetch() {     
        const arr = [];
        if(localStorage.length > 0){
            for(let i = 0 ; i < localStorage.length ; i++){
                arr.push(JSON.parse(localStorage.getItem(localStorage.key(i))));
            }
        }
        return arr;
    }
}

export const store = new Vuex.Store({
    state: {
        headerText: 'Todo It !',
        todoItems: storage.fetch()
    },
    getters: {
        storedTodoItems(state){
            return state.todoItems;
        }
    }
    ,
    mutations:{
        // mutations에서 state를 인자로 접근할 수 있다
        addOneItem(state, todoItem){
            const obj = { completed: false, item: todoItem};
            localStorage.setItem(todoItem, JSON.stringify(obj));
            // this.todoItems.push(obj);
            state.todoItems.push(obj);
        },
         removeOneItem(state, payload){
            console.log(state);
            localStorage.removeItem(payload.todoItem.item);
            // this.todoItems.splice(index,1);
            state.todoItems.splice(payload.index, 1);
        },
        toggleOneItem(state, payload){
            console.log(payload.index);
            state.todoItems[payload.index].completed = !state.todoItems[payload.index].completed; 
            localStorage.removeItem(payload.todoItem.item);
            localStorage.setItem(payload.todoItem.item, JSON.stringify(payload.todoItem));
        },
        clearAllItems(state){
            localStorage.clear();
            state.todoItems = [];
        }
    }
})