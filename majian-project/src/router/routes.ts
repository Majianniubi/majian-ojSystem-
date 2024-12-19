import { RouteRecordRaw } from "vue-router";
import NoAdminView from "@/components/NoAdminView.vue";
import UserLayout from "@/layouts/UserLayout.vue";
import permissionEnum from "@/permission/permissionEnum";
import LoginViews from "@/components/user/LoginViews.vue";
import RegisterView from "@/components/user/RegisterView.vue";
import AddQuestionView from "@/components/question/AddQuestionView.vue";
import ManageQuestionView from "@/components/question/ManageQuestionView.vue";
import updateQuestionView from "@/components/question/updateQuestionView.vue";
import doQuestionView from "@/components/question/doQuestionView.vue";
import ThroughQuestionSubmit from "@/components/question/ThroughQuestionSubmit.vue";

export const routes: Array<RouteRecordRaw> = [
  {
    path: "/userLayout",
    name: "用户",
    component: UserLayout,
    children: [
      {
        path: "/user/login",
        name: "用户登陆",
        component: LoginViews,
      },
      {
        path: "/user/register",
        name: "用户注册",
        component: RegisterView,
      },
    ],
    meta: {
      hideOnMenu: true,
    },
  },
  {
    path: "/add/question",
    name: "创建题目",
    component: AddQuestionView,
    meta: {
      access: permissionEnum.NORMAL_USER,
    },
  },
  {
    path: "/manage/question",
    name: "管理题目",
    component: ManageQuestionView,
    meta: {
      access: permissionEnum.ADMIN,
    },
  },
  {
    path: "/do/question/:id",
    name: "做题",
    component: doQuestionView,
    props: true,
    meta: {
      hideOnMenu: true,
      access: permissionEnum.NORMAL_USER,
    },
  },
  {
    path: "/update/question",
    name: "更新页面",
    component: updateQuestionView,
    meta: {
      hideOnMenu: true,
      access: permissionEnum.NORMAL_USER,
    },
  },
  {
    path: "/",
    name: "浏览题目",
    component: () => import("@/components/question/ThroughQuestion.vue"),
  },
  {
    path: "/submitQuestion",
    name: "浏览提交题目",
    component: ThroughQuestionSubmit,
  },
  {
    path: "/index",
    name: "主页",
    component: NoAdminView,
  },
  // {
  //   path: "/index",
  //   name: "关于我的",
  //   component: () =>
  //     import(/* webpackChunkName: "about" */ "../views/AboutView.vue"),
  // },
];
