<template>
  <div id="userLogin">
    <a-form
      :model="form"
      :style="{ width: '600px' }"
      @submit="handleSubmit"
      label-align="left"
      auto-label-width
    >
      <a-form-item field="userName" label="账号">
        <a-input v-model="form.userAccount" placeholder="请输入账号" />
      </a-form-item>
      <a-form-item tooltip="密码不少于八位" field="post" label="密码">
        <a-input-password
          v-model="form.userPassword"
          placeholder="请输入密码"
        />
      </a-form-item>
      >
      <a-form-item>
        <a-button html-type="submit" style="margin-right: 20px"
          >Submit
        </a-button>
        <a-button @click="handleRegister">Register</a-button>
      </a-form-item>
    </a-form>
  </div>
</template>

<script setup lang="ts">
import { reactive } from "vue";
import { UserControllerService, UserLoginRequest } from "../../../generated";
import { useRouter } from "vue-router";
import message from "@arco-design/web-vue/es/message";
import { useStore } from "vuex";

const stroe = useStore();
const router = useRouter();
const form = reactive({
  userPassword: "",
  userName: "",
} as UserLoginRequest);
const handleSubmit = async () => {
  const res = await UserControllerService.userLoginUsingPost(form);
  if (res.code === 0) {
    stroe.dispatch("user/getLoginUser");
    router.push({
      path: "/",
      replace: true,
    });
  } else {
    message.error("登陆失败" + res.message);
  }
};
const handleRegister = () => {
  router.push("/user/register");
};
</script>

<style scoped>
#userLogin {
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>
