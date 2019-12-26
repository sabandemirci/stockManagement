<template>
  <CContainer class="d-flex align-items-center min-vh-100">
    <CRow class="justify-content-center">
      <CCol md="8">
        <CCardGroup>
          <CSpinner
            color="success"
            style="width: 4rem;height: 4rem;position: absolute;top: 50%;left: 50%;z-index: 999;"
            v-if="loadingDialog"
          />
          <CCard class="p-4">
            <CCardBody>
              <CForm>
                <h1>Login</h1>
                <p class="text-muted">Sign In to your account</p>
                <CInput placeholder="Username" autocomplete="username email" v-model="username">
                  <template #prepend-content>
                    <CIcon name="cil-user" />
                  </template>
                </CInput>
                <CInput
                  placeholder="Password"
                  type="password"
                  autocomplete="curent-password"
                  v-model="password"
                >
                  <template #prepend-content>
                    <CIcon name="cil-lock-locked" />
                  </template>
                </CInput>
                <CRow>
                  <CCol col="6">
                    <CButton color="primary" class="px-4" @click="login">Login</CButton>
                  </CCol>
                  <CCol col="6" class="text-right">
                    <CButton color="link" class="px-0">Forgot password?</CButton>
                  </CCol>
                </CRow>
              </CForm>
            </CCardBody>
          </CCard>
          <CCard
            color="primary"
            text-color="white"
            class="text-center py-5 d-md-down-none"
            style="width:44%"
            body-wrapper
          >
            <h2>Sign up</h2>
            <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.</p>
            <CButton color="primary" class="active mt-3">Register Now!</CButton>
          </CCard>
        </CCardGroup>
      </CCol>
    </CRow>
  </CContainer>
</template>

<script>
export default {
  name: "Login",
  data() {
    return {
      username: null,
      password: null,
      loadingDialog: false
    };
  },
  methods: {
    login() {
      this.loadingDialog = true;
      this.$http
        .request({
          url: "/api/auth/v1/signin",
          method: "post",
          data: {
            username: this.username,
            password: this.password
          }
        })
        .then(response => {
          this.loadingDialog = false;
          this.$cookies.set("accessToken", response.data.accessToken);
          localStorage.roles = response.data.roles;
          this.$router.push({ path: "/" });
        })
        .catch(error => {
          this.loadingDialog = false;
        })
        .finally(function() {});

      console.log(this.username);
      console.log(this.password);
    }
  }
};
</script>
