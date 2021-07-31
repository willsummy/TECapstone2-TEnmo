<template>
  <div>
    <form v-on:submit.prevent="login">
      <div>
        <label for="username">Username</label>
        <input id="username" type="text" v-model="credentials.username" />
      </div>
      <div>
        <label for="password">Password</label>
        <input type="text" id="password" v-model="credentials.password" />
      </div>
      <button type="sumbit">Sign In</button>
    </form>
  </div>
</template>

<script>
import AuthenticationService from '../services/AuthenticationService.js'
export default {

  data() {
    return {
      credentials: {
        username: '',
        password: ''
      }
    }
  },
  methods: {
    login() {
      AuthenticationService.login(this.credentials)
        .then( response => {
          this.$store.commit("SET_USER", response.data)
        })
    }
  }

}
</script>

<style>

</style>
