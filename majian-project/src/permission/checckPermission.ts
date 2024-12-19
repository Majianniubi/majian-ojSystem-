import PermissionEnum from "@/permission/permissionEnum";
import permissionEnum from "@/permission/permissionEnum";

const checkPermission = (
  loginUser: any,
  needPermission = PermissionEnum.NOT_LOGIN
) => {
  const userPermission = loginUser?.userRole ?? PermissionEnum.NOT_LOGIN;
  if (needPermission === PermissionEnum.NOT_LOGIN) {
    return true;
  }
  //需要登陆权限才能访问
  if (needPermission === PermissionEnum.NORMAL_USER) {
    if (userPermission !== PermissionEnum.NORMAL_USER) {
      if (userPermission !== permissionEnum.ADMIN) {
        return false;
      } else {
        return true;
      }
    }
  }
  if (needPermission === PermissionEnum.ADMIN) {
    if (userPermission !== PermissionEnum.ADMIN) {
      return false;
    }
  }
  return true;
};
export default checkPermission;
