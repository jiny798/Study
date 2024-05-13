<template>
  <div>
    <header>
      <h1>{{ this.$store.state.headerText}}</h1>
    </header>
  </div>
</template>

<script>
export default {
  created(){
  //  console.log(this.$store.state.headerText);
  //  this.$store.state.headerText = 'aaa';
  }
}
</script>

<style scoped>
h1{
  color: #2f3b52;
  font-weight: 900;
  margin: 2.5rem 0 1.5rem;
}

</style>