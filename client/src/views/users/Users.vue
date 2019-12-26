<template>
  <CRow>
    <CCol col="12" xl="8">
      <transition name="slide">
        <CCard>
          <CCardHeader>
            Kullanıcılar
            <CButton color="success" @click="newItem" style="float:right">Yeni</CButton>
          </CCardHeader>
          <CCardBody>
            <CDataTable
              hover
              striped
              :items="items"
              :fields="fields"
              :items-per-page="perPage"
              :pagination="$options.paginationProps"
              index-column
              clickable-rows
            >
              <template #show_details="{item, index}">
                <td class="py-2">
                  <CButton
                    color="primary"
                    variant="outline"
                    square
                    size="sm"
                    @click="rowClicked(item)"
                  >Detay</CButton>
                </td>
              </template>
            </CDataTable>
          </CCardBody>
        </CCard>
      </transition>
    </CCol>
    <CModal title="Kullanıcı Bilgileri" color="warning" :show.sync="dialogVisibility">
      <CRow>
        <CCol col="12">
          <CCard>
            <CCardBody>
              <CRow>
                <CCol sm="12">
                  <CInput label="ID" placeholder="id" v-model="selectedItem.id" :disabled="true" />
                </CCol>
              </CRow>
              <CRow>
                <CCol sm="12">
                  <CInput
                    label="Kullanıcı Adı"
                    placeholder="kullanıcı adı"
                    v-model="selectedItem.userName"
                    :disabled="!isnew ? true: false"
                  />
                </CCol>
              </CRow>
              <CRow>
                <CCol sm="12">
                  <CInput label="Ad" placeholder="ad" v-model="selectedItem.firstName" />
                </CCol>
              </CRow>
              <CRow>
                <CCol sm="12">
                  <CInput label="Soyad" placeholder="soyad" v-model="selectedItem.lastName" />
                </CCol>
              </CRow>
              <CRow>
                <CCol sm="12">
                  <CInput label="Email" placeholder="email" v-model="selectedItem.email" />
                </CCol>
              </CRow>
              <CRow>
                <CCol sm="12">
                  <CSelect
                    label="Role"
                    :options="['ROLE_ADMIN','ROLE_USER', 'ROLE_MODERATOR']"
                    v-model="selectedItem.role"
                    @update:value="onSelectRole"
                  />
                </CCol>
              </CRow>
            </CCardBody>
          </CCard>
        </CCol>
      </CRow>
      <slot slot="footer-wrapper">
        <button type="button" class="btn btn-secondary" @click="dialogVisibility=false">Kapat</button>
        <button type="button" class="btn btn-warning" @click="save">Tamam</button>
      </slot>
    </CModal>
  </CRow>
</template>

<script>
export default {
  name: "Users",
  data: () => {
    return {
      items: [],
      fields: [
        { key: "id", label: "ID" },
        { key: "userName", label: "Kullanıcı Adı" },
        { key: "firstName", label: "Ad" },
        { key: "lastName", label: "Soyad" },
        { key: "email", label: "Email" },
        { key: "role", label: "Rol" },
        { key: "show_details", label: "" }
      ],
      details: [],
      dialogVisibility: false,
      selectedItem: {},
      isnew: false,
      perPage: 5
    };
  },
  beforeMount() {
    this.fetchUsers();
  },
  paginationProps: {
    align: "center",
    doubleArrows: false,
    previousButtonHtml: "prev",
    nextButtonHtml: "next"
  },
  methods: {
    fetchUsers() {
      this.$http
        .request({
          url: "/api/user/v1/queryAll",
          method: "get",
          headers: {
            Authorization: "bearer " + this.$cookies.get("accessToken")
          }
        })
        .then(response => {
          this.items = response.data;
        });
    },
    userLink(id) {
      return `users/${id.toString()}`;
    },
    rowClicked(item) {
      this.isnew = false;
      this.selectedItem = Object.assign({}, item);
      this.dialogVisibility = true;
    },
    newItem() {
      this.isnew = true;
      this.selectedItem = Object.assign({}, {});
      this.dialogVisibility = true;
    },
    onSelectRole(role) {
      this.selectedItem.role = role;
    },
    save() {
      this.$http
        .request({
          url: "/api/user/v1/save",
          method: "post",
          data: this.selectedItem,
          headers: {
            Authorization: "bearer " + this.$cookies.get("accessToken")
          }
        })
        .then(response => {
          this.dialogVisibility = false;
          this.fetchUsers();
        })
        .catch(error => {});
    }
  }
};
</script>
