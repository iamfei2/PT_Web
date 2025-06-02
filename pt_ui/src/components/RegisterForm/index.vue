<template>
  <el-form ref="registerForm" :rules="registerFormRules" :model="registerForm">
    <el-form-item prop="username">
      <el-input v-model="registerForm.username" type="text" auto-complete="off" placeholder="账号">
        <svg-icon slot="prefix" icon-class="user" class="el-input__icon input-icon" />
      </el-input>
    </el-form-item>
    <el-form-item prop="email">
      <el-input v-model="registerForm.email" type="text" auto-complete="off" placeholder="邮箱地址">
        <svg-icon slot="prefix" icon-class="email" class="el-input__icon input-icon" />
      </el-input>
    </el-form-item>
    <el-form-item prop="password">
      <el-input
        v-model="registerForm.password"
        type="password"
        auto-complete="off"
        placeholder="密码"
        @keyup.enter.native="handleRegister"
      >
        <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon" />
      </el-input>
    </el-form-item>
    <el-form-item prop="passwordConfirm">
      <el-input
        v-model="registerForm.passwordConfirm"
        type="password"
        auto-complete="off"
        placeholder="确认密码"
        @keyup.enter.native="handleRegister"
      >
        <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon" />
      </el-input>
    </el-form-item>
    <el-form-item prop="code">
      <el-input
        v-model="registerForm.code"
        auto-complete="off"
        placeholder="验证码"
        style="width: 63%"
        @keyup.enter.native="handleLogin"
      >
        <svg-icon slot="prefix" icon-class="validCode" class="el-input__icon input-icon" />
      </el-input>
      <div class="login-code">
        <img :src="codeUrl" @click="getCode" class="login-code-img"/>
      </div>
    </el-form-item>
    <el-form-item prop="inviteCode">
      <el-input v-model="registerForm.inviteCode" type="text" auto-complete="off" placeholder="请输入邀请码">
        <svg-icon slot="prefix" icon-class="online" class="el-input__icon input-icon" />
      </el-input>
    </el-form-item>
    <el-form-item>
      <el-button
        :loading="loading"
        size="medium"
        style="width:100%;"
        @click.native.prevent="handleTest"
      >
        <span>答题获取邀请码</span>
      </el-button>
    </el-form-item>
    <el-form-item style="width:100%;">
      <el-button
        :loading="loading"
        size="medium"
        type="primary"
        style="width:100%;"
        @click.native.prevent="handleRegister"
      >
        <span v-if="!loading">注 册</span>
        <span v-else>登 录 中...</span>
      </el-button>
    </el-form-item>
  </el-form>
</template>

<script>
  import { getCodeImg } from "@/api/login";
  import { register } from "@/api/register"

  export default {
    name: "Login",
    data() {
      var checkEmail = (rule, value, callback) => {
        const regEmail = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
        if (regEmail.test(value)) {
          return callback();
        }
        callback(new Error("请输入合法的验证码"));
      };
      return {
        registerForm: {},
        codeUrl: "",
        registerFormRules: {
          username: [
            { required: true, trigger: "blur", message: "用户名不能为空" }
          ],
          password: [
            { required: true, trigger: "blur", message: "密码不能为空" }
          ],
          passwordConfirm: [
            { required: true, trigger: "blur", message: "确认密码不能为空" }
          ],
          email: [
            { required: true, trigger: "blur", message: "邮箱地址有误", type: "email" },
            { validator: checkEmail, trigger: "blur", message: "邮箱地址有误" }
          ],
          code: [{ required: true, trigger: "change", message: "验证码不能为空" }]
        },
        loading: false,
        redirect: undefined
      };
    },
    watch: {
      $route: {
        handler: function(route) {
          this.redirect = route.query && route.query.redirect;
        },
        immediate: true
      }
    },
    created() {
      this.getCode();
    },
    methods: {
      handleTest () {
        window.open('https://ks.wjx.top/vj/ek27lfn.aspx')
      },
      handleRegister () {
        this.$refs.registerForm.validate(valid => {
          if (valid) {
            register(this.registerForm).then( res => {
              this.$emit('finished', res)
              if (res.code === 200) {
                this.msgSuccess("注册成功!")
                this.registerForm = {}
              } else {
                this.msgError("注册失败, " + res.msg)
              }
            })
          }
        })
      },
      getCode() {
        getCodeImg().then(res => {
          this.codeUrl = "data:image/gif;base64," + res.img;
          this.registerForm.uuid = res.uuid;
        });
      }
    }
  };
</script>

<style rel="stylesheet/scss" lang="scss">
  .login-tip {
    font-size: 13px;
    text-align: center;
    color: #bfbfbf;
  }
  .login-code {
    width: 33%;
    height: 38px;
    float: right;
    img {
      cursor: pointer;
      vertical-align: middle;
    }
  }
  .el-login-footer {
    height: 40px;
    line-height: 40px;
    position: fixed;
    bottom: 0;
    width: 100%;
    text-align: center;
    color: #fff;
    font-family: Arial;
    font-size: 12px;
    letter-spacing: 1px;
  }
  .login-code-img {
    height: 38px;
  }
</style>
