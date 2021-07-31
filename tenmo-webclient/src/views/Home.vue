<template>
    <div>Hello TEnmo
        <router-link id="signInBtn" v-bind:to="{ name: 'SignIn'}" >Sign In</router-link>
    </div>
</template>

<script>

export default {

}
</script>

<style>

#signInBtn {
    display: block;
}

</style>
