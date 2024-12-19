<template>
  <div class="globalHeader">
    <a-row align="center" :wrap="false">
      <a-col flex="auto">
        <div class="menu-demo">
          <a-menu
            mode="horizontal"
            :selected-keys="selectKey"
            @menu-item-click="noMenuClick"
          >
            <a-menu-item
              key="0"
              :style="{ padding: 0, marginRight: '38px' }"
              disabled
            >
              <div class="title-bar">
                <img class="logo" src="@/assets/oj-logo.png" />
              </div>
            </a-menu-item>
            <a-menu-item v-for="data in visibleRoutes" :key="data.path">
              {{ data.name }}
            </a-menu-item>
          </a-menu>
        </div>
      </a-col>
      <a-col flex="100px">
        <div class="user">
          {{ store.state.user?.loginUser?.userName ?? "未登录" }}
        </div>
      </a-col>
    </a-row>
  </div>
</template>

<script setup lang="ts">
import { routes } from "@/router/routes";
import { useRoute, useRouter } from "vue-router";
import { computed, ref } from "vue";
import { useStore } from "vuex";
import checkPermission from "@/permission/checckPermission";
import PermissionEnum from "@/permission/permissionEnum";

const router = useRouter();
const route = useRoute();
const store = useStore();
const visibleRoutes = computed(() => {
  return routes.filter((item) => {
    if (item?.meta?.hideOnMenu === true) {
      return false;
    }
    if (
      !checkPermission(
        store.state.user?.loginUser,
        item?.meta?.access as string
      )
    ) {
      return false;
    }
    return true;
  });
});
const selectKey = ref(["/"]);
router.afterEach((to, from) => {
  selectKey.value = [to.path];
});
const noMenuClick = (key: string) => {
  router.push({
    path: key,
  });
};
</script>

<style scoped>
.title-bar {
  width: 120px;
  height: 60px;
  display: flex;
  align-items: center;
}

.title {
  font-size: 20px;
  color: #444;
}

.logo {
  margin-left: 20px;
  width: 60px;
  height: 60px;
}

.user {
  text-shadow: #eee 1px 1px;
  font-size: 20px;
}
</style>
