import store from "@/store";
import router from "@/router";
import PermissionEnum from "@/permission/permissionEnum";
import checkPermission from "@/permission/checckPermission";

router.beforeEach(async (to, from, next) => {
  let loginUser = store.state.user.loginUser;
  if (!loginUser || !loginUser.userRole) {
    await store.dispatch("user/getLoginUser");
  }
  loginUser = store.state.user.loginUser;
  const needPermission = to.meta?.access ?? PermissionEnum.NOT_LOGIN;
  if (needPermission !== PermissionEnum.NOT_LOGIN) {
    if (
      !loginUser ||
      !loginUser.userRole ||
      loginUser.userRole === PermissionEnum.NOT_LOGIN
    ) {
      next(`/user/login?redirect=${to.fullPath}`);
      return;
    }
    if (!checkPermission(loginUser, needPermission as string)) {
      next("/noAuth");
    }
  }
  next();
});
