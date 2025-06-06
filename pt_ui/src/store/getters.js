const getters = {
  sidebar: state => state.app.sidebar,
  size: state => state.app.size,
  device: state => state.app.device,
  visitedViews: state => state.tagsView.visitedViews,
  cachedViews: state => state.tagsView.cachedViews,
  token: state => state.user.token,
  avatar: state => state.user.avatar,
  points: state => state.user.points,
  uploaded: state => state.user.uploaded,
  downloaded: state => state.user.downloaded,
  banExpire: state => state.user.banExpire,
  banReason: state => state.user.banReason,
  name: state => state.user.name,
  introduction: state => state.user.introduction,
  roles: state => state.user.roles,
  permissions: state => state.user.permissions,
  permission_routes: state => state.permission.routes,
  sidebarRouters:state => state.permission.sidebarRouters,
  categories: state => state.galaxybit.categories
}
export default getters
