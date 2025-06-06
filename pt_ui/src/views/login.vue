<template>
  <div class="login">
    <div class="login-container">
      <div class="login-left">
        <h1 class="title">Welcome to BitBit</h1>
        <p class="subtitle">Explore the Bit of possibilities</p>
      </div>
      <div class="login-right">
        <el-tabs v-model="activeTab" class="custom-tabs">
          <el-tab-pane label="登录" name="login">
            <el-form ref="loginForm" :rules="loginRules" :model="loginForm">
              <el-form-item prop="username">
                <el-input v-model="loginForm.username" type="text" auto-complete="off" placeholder="账号">
                  <svg-icon slot="prefix" icon-class="user" class="el-input__icon input-icon" />
                </el-input>
              </el-form-item>
              <el-form-item prop="password">
                <el-input v-model="loginForm.password" type="password" auto-complete="off" placeholder="密码"
                  @keyup.enter.native="handleLogin">
                  <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon" />
                </el-input>
              </el-form-item>
              <el-form-item prop="code" class="code-input">
                <el-input v-model="loginForm.code" auto-complete="off" placeholder="验证码"
                  @keyup.enter.native="handleLogin">
                  <svg-icon slot="prefix" icon-class="validCode" class="el-input__icon input-icon" />
                </el-input>
                <img :src="codeUrl" @click="getCode" class="login-code-img" />
              </el-form-item>
              <div class="remember-me">
                <el-checkbox v-model="loginForm.rememberMe">记住密码</el-checkbox>
                <a href="#" class="forgot-password">忘记密码？</a>
              </div>
              <el-button :loading="loading" type="primary" class="login-button" @click.native.prevent="handleLogin">
                <span v-if="!loading">登 录</span>
                <span v-else>登 录 中...</span>
              </el-button>
            </el-form>
          </el-tab-pane>
          <el-tab-pane label="注册" name="register">
            <register-form @finished="handleRegisterFinished" />
          </el-tab-pane>
        </el-tabs>
      </div>
    </div>
    <div class="el-login-footer">
      <span>Copyright © 2018-2021 GalaxyBit All Rights Reserved.</span>
      <span style="display: block">QQ群: 248383729</span>
    </div>
  </div>
</template>

<script>
import RegisterForm from "@/components/RegisterForm"
import { getCodeImg } from "@/api/login";
import Cookies from "js-cookie";
import { encrypt, decrypt } from '@/utils/jsencrypt'

export default {
  name: "Login",
  components: { RegisterForm },
  data() {
    return {
      activeTab: "login",
      codeUrl: "",
      loginForm: {
        username: "",
        password: "",
        rememberMe: false,
        code: "",
        uuid: ""
      },
      loginRules: {
        username: [
          { required: true, trigger: "blur", message: "用户名不能为空" }
        ],
        password: [
          { required: true, trigger: "blur", message: "密码不能为空" }
        ],
        code: [{ required: true, trigger: "change", message: "验证码不能为空" }]
      },
      loading: false,
      redirect: undefined
    };
  },
  watch: {
    $route: {
      handler: function (route) {
        this.redirect = route.query && route.query.redirect;
      },
      immediate: true
    }
  },
  created() {
    this.getCode();
    this.getCookie();
  },
  methods: {
    handleRegisterFinished(res) {
      if (res.code === 200) {
        this.activeTab = "login"
      }
    },
    getCode() {
      getCodeImg().then(res => {
        this.codeUrl = "data:image/gif;base64," + res.img;
        this.loginForm.uuid = res.uuid;
      });
    },
    getCookie() {
      const username = Cookies.get("username");
      const password = Cookies.get("password");
      const rememberMe = Cookies.get('rememberMe')
      this.loginForm = {
        username: username === undefined ? this.loginForm.username : username,
        password: password === undefined ? this.loginForm.password : decrypt(password),
        rememberMe: rememberMe === undefined ? false : Boolean(rememberMe)
      };
    },
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true;
          if (this.loginForm.rememberMe) {
            Cookies.set("username", this.loginForm.username, { expires: 30 });
            Cookies.set("password", encrypt(this.loginForm.password), { expires: 30 });
            Cookies.set('rememberMe', this.loginForm.rememberMe, { expires: 30 });
          } else {
            Cookies.remove("username");
            Cookies.remove("password");
            Cookies.remove('rememberMe');
          }
          this.$store.dispatch("Login", this.loginForm).then(() => {
            this.$router.push({ path: this.redirect || "/" }).catch(() => { });
          }).catch(() => {
            this.loading = false;
            this.getCode();
          });
        }
      });
    }
  }
};
</script>

<style lang="scss">
// Keep the styles from the first code
.login {
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #1e3c72 0%, #2a5298 100%);
  position: relative;

  .login-container {
    display: flex;
    width: 900px;
    height: 600px;
    background: rgba(255, 255, 255, 0.95);
    border-radius: 15px;
    overflow: hidden;
    box-shadow: 0 15px 30px rgba(0, 0, 0, 0.2);
  }

  .login-left {
    flex: 1;
    background: linear-gradient(120deg, #3498db 0%, #2c3e50 100%);
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    color: white;
    padding: 40px;

    .title {
      font-size: 36px;
      margin-bottom: 20px;
      text-align: center;
    }

    .subtitle {
      font-size: 18px;
      text-align: center;
    }
  }

  .login-right {
    flex: 1;
    padding: 30px;
    display: flex;
    flex-direction: column;
    justify-content: center;
    overflow-y: auto;
  }

  .custom-tabs {
    .el-tabs__nav-wrap::after {
      display: none;
    }

    .el-tabs__item {
      font-size: 18px;
    }
  }

  .el-input {
    margin-bottom: 15px;

    input {
      height: 40px;
      border-radius: 20px;
    }
  }

  .code-input {
    display: flex;
    gap: 10px;

    .el-input {
      flex: 1;
    }

    .login-code-img {
      height: 40px;
      border-radius: 20px;
      cursor: pointer;
    }
  }

  .remember-me {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
  }

  .forgot-password {
    color: #2a5298;
    text-decoration: none;

    &:hover {
      text-decoration: underline;
    }
  }

  .login-button,
  .register-button {
    width: 100%;
    height: 40px;
    border-radius: 20px;
    font-size: 16px;
    background: #2a5298;
    border: none;

    &:hover {
      background: #1e3c72;
    }
  }

  .register-form {
    .el-form-item {
      margin-bottom: 15px;
    }
  }

  .el-login-footer {
    position: absolute;
    bottom: 20px;
    width: 100%;
    text-align: center;
    color: rgba(255, 255, 255, 0.8);
    font-size: 14px;
  }
}
</style>