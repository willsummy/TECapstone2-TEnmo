import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
    state: {
        currentUser: {
            "token": "",
            "username": ''
        }
    },
    mutations: {
        SET_USER(state, logInUser) {
            state.currentUser.token = logInUser.token;
            state.currentUser.username = logInUser.user.username;
        }
    },
    actions: {},
    modules: {}
})
