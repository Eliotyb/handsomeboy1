<template>
  <div id="app-root">
    <router-view />
    <AiAssistant />
  </div>
</template>

<script>
import AiAssistant from './components/AiAssistant.vue'

export default {
  name: 'App',
  components: {
    AiAssistant
  }
}
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
}

#app, #app-root {
  min-height: 100vh;
}
</style>
