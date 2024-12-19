<template>
  <div id="basicLayout">
    <a-layout style="min-height: 100vh">
      <a-layout-header class="header">
        <GlobalHeader></GlobalHeader>
      </a-layout-header>
      <a-layout-content class="content">
        <router-view />
      </a-layout-content>
      <a-layout-footer class="footer">马建的ojSystem项目系统</a-layout-footer>
    </a-layout>
  </div>
</template>

<style>
#basicLayout {
}

#basicLayout .header {
  margin-bottom: 10px;
}

#basicLayout .content {
  background-color: whitesmoke;
  margin-bottom: 10px;
  padding: 15px;
}

#basicLayout .footer {
  background-color: whitesmoke;
  position: sticky;
  bottom: 0;
  left: 0;
  right: 0;
  text-align: center;
  font-size: 20px;
}
</style>
<script>
import GlobalHeader from "@/components/GlobalHeader";

export default {
  components: { GlobalHeader },
};
</script>
