<template>
  <div id="userRegister">
    <a-form
      :model="form"
      :style="{ width: '600px' }"
      @submit="handleSubmit"
      label-align="left"
      auto-label-width
    >
      <a-form-item field="userName" label="姓名">
        <a-input v-model="form.userName" placeholder="请输入姓名" />
      </a-form-item>
      <a-form-item field="userName" label="账号">
        <a-input v-model="form.userAccount" placeholder="请输入账号" />
      </a-form-item>
      <a-form-item tooltip="密码不少于八位" field="post" label="密码">
        <a-input-password
          v-model="form.userPassword"
          placeholder="请输入密码"
        />
      </a-form-item>
      <a-form-item tooltip="确认一次" field="post" label="密码">
        <a-input-password
          v-model="form.checkPassword"
          placeholder="请输入密码"
        />
      </a-form-item>
      >
      <a-form-item>
        <a-button html-type="submit">Submit</a-button>
      </a-form-item>
    </a-form>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from "vue";
import { UserControllerService, UserRegisterRequest } from "../../../generated";
import { useRouter } from "vue-router";
import message from "@arco-design/web-vue/es/message";

const router = useRouter();
const form = reactive({
  userAccount: "",
  userPassword: "",
  checkPassword: "",
  userName: "",
} as UserRegisterRequest);
const handleSubmit = async () => {
  if (form.userPassword !== form.checkPassword) {
    message.error({
      content: "两次密码不一致",
      duration: 1000,
    });
  } else {
    const res = await UserControllerService.userRegisterUsingPost(form);
    if (res.code === 0) {
      message.success({
        content: "注册成功",
        duration: 1000,
      });
      router.push("/user/login");
    } else {
      message.error({
        content: "注册失败" + res.message,
        duration: 1000,
      });
    }
  }
};
</script>

<style scoped>
#userRegister {
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>
