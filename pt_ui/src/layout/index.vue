<template>
  <div :class="classObj" class="app-wrapper" :style="{'--current-color': theme}">
    <div :class="{hasTagsView:needTagsView}" class="main-container">
      <div :class="{'fixed-header':fixedHeader}">
        <navbar />
               <!-- 1. 在顶部添加水平显示的侧边栏 -->
               <div class="top-menu-container">
                 <sidebar class="sidebar-horizontal" :style="{ backgroundColor: sideTheme === 'theme-dark' ? variables.menuBg : variables.menuLightBg }" />
               </div>
      </div>

      <!-- 2. 将分类栏移动到左侧 -->
           <div class="left-tags-container">
             <tags-view v-if="needTagsView" />
           </div>

      <app-main />
      <right-panel v-if="showSettings">
        <settings />
      </right-panel>
    </div>
  </div>
</template>

<script>
import RightPanel from '@/components/RightPanel'
import { AppMain, Navbar, Settings, Sidebar, TagsView } from './components'
import SidebarLogo from './components/Sidebar/Logo.vue' // 添加Logo组件导入
import ResizeMixin from './mixin/ResizeHandler'
import { mapState } from 'vuex'
import variables from '@/assets/styles/variables.scss'

export default {
  name: 'Layout',
  components: {
      AppMain,
      Navbar,
      RightPanel,
      Settings,
      Sidebar,
      TagsView,
      SidebarLogo // 注册Logo组件
},
mixins: [ResizeMixin],
  computed: {
...mapState({
    theme: state => state.settings.theme,
    sideTheme: state => state.settings.sideTheme,
    sidebar: state => state.app.sidebar,
    device: state => state.app.device,
    showSettings: state => state.settings.showSettings,
    needTagsView: state => state.settings.tagsView,
    fixedHeader: state => state.settings.fixedHeader
  }),
    classObj() {
    return {
      hideSidebar: !this.sidebar.opened,
      openSidebar: this.sidebar.opened,
      withoutAnimation: this.sidebar.withoutAnimation,
      mobile: this.device === 'mobile'
    }
  },
  variables() {
    return variables;
  },
     isCollapse() {
         return !this.sidebar.opened;
     }
},
methods: {
  handleClickOutside() {
    this.$store.dispatch('app/closeSideBar', { withoutAnimation: false })
  }
}
}
</script>

<style lang="scss" scoped>
@import "~@/assets/styles/mixin.scss";
@import "~@/assets/styles/variables.scss";

.app-wrapper {
  @include clearfix;
  position: relative;
  height: 100%;
  width: 100%;

  //&.mobile.openSidebar {
  //  position: fixed;
  //  top: 0;
  //}
}

.drawer-bg {
  background: #000;
  opacity: 0.3;
  width: 100%;
  top: 0;
  height: 100%;
  position: absolute;
  z-index: 999;
}

.fixed-header {
  position: fixed;
  top: 0;
  left: 0;
  z-index: 9;
  width: 100%;
  transition: width 0.28s;
  height: auto; /* 增加高度以容纳顶部菜单 */
  padding:10px 20px;
}

//.hideSidebar .fixed-header {
//  width: calc(100% - 54px)
//}

.mobile .fixed-header {
  width: 100%;
}

 /* 新增样式 */
 .tags-sidebar-container {
   position: fixed;
   top: 0;
   left: 0;
   bottom: 0;
   width: $sideBarWidth;
   z-index: 1001;
   overflow: hidden;
   box-shadow: 2px 0 6px rgba(0, 21, 41, 0.35);
   transition: width 0.28s;
 }

 .top-sidebar-container {
   display: flex;
   align-items: center;
   height: 50px;
   background: #fff;
   border-bottom: 1px solid #d8dce5;
 }

 .top-sidebar {
   flex: 1;
   height: 100% !important;

     ::v-deep .el-scrollbar {
       height: 100%;

         .el-menu {
           display: flex;
           height: 100%;

             .el-menu-item,
             .el-submenu {
               height: 100%;
               line-height: 50px;
             }
         }
     }
 }

 .main-container {
   margin-left: 0;
 }

 .hideSidebar {
     .tags-sidebar-container {
       width: 54px;
     }
     .main-container {
       margin-left: 54px;
     }
 }

 .mobile {
     .tags-sidebar-container {
       width: 0 !important;
     }
     .main-container {
       margin-left: 0;
     }
 }
</style>
