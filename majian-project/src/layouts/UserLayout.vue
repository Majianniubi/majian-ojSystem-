<template>
  <div id="userLayout">
    <h2>{{ route.name }}</h2>
    <a-layout style="height: 100vh">
      <a-layout-content class="content">
        <router-view />
      </a-layout-content>
      <a-layout-footer class="footer">马建的ojSystem项目系统</a-layout-footer>
    </a-layout>
  </div>
</template>
<script setup lang="ts">
import { useRoute } from "vue-router";

const route = useRoute();
</script>
<style>
#userLayout {
  text-align: center;
}

#userLayout .content {
  margin-bottom: 10px;
  padding: 20px;
}

#userLayout .footer {
  background-color: whitesmoke;
  padding: 16px;
  position: sticky;
  bottom: 0;
  left: 0;
  right: 0;
  text-align: center;
  font-size: 20px;
}
</style>
<script></script>
