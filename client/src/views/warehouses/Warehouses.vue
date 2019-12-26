<template>
  <CRow>
    <CCol xl="6" lg="6" sm="12" xs="12">
      <transition name="slide">
        <CCard>
          <CCardHeader>
            Ambarlar
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
    <CCol xl="6" lg="6" sm="12" xs="12">
      <CRow>
        <CCol col="12">
          <CCard>
            <CCardHeader>Detay</CCardHeader>
            <CCardBody>
              <CRow>
                <CCol sm="12">
                  <CInput label="ID" v-model="selectedItem.id" :disabled="true" />
                </CCol>
              </CRow>
              <CRow>
                <CCol sm="12">
                  <CInput label="Ambar Kodu" v-model="selectedItem.code" />
                </CCol>
              </CRow>
              <CRow>
                <CCol sm="12">
                  <CInput label="Ambar Adı" v-model="selectedItem.name" />
                </CCol>
              </CRow>
              <CRow>
                <CCol sm="12">
                  <CInput label="Adres" v-model="selectedItem.address" />
                </CCol>
              </CRow>
            </CCardBody>
            <CCardFooter>
              <button type="button" class="btn btn-warning" @click="save">Kaydet</button>
            </CCardFooter>
          </CCard>
        </CCol>
      </CRow>
      <CRow>
        <CCol col="12">
          <CCard>
            <CCardHeader>
              Yetkili Kullanıcılar
              <CButton color="success" @click="showDialog" style="float:right">Kullanıcı Ekle</CButton>
            </CCardHeader>
            <CCardBody>
              <CDataTable
                hover
                striped
                :items="selectedUserItems"
                :fields="selectedUserFields"
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
                      @click="removeUser(index)"
                    >Sil</CButton>
                  </td>
                </template>
              </CDataTable>
            </CCardBody>
            <CCardFooter>
              <button type="button" class="btn btn-warning" @click="saveUser">Kaydet</button>
            </CCardFooter>

            <CModal title="Kullanıcı Seç" color="warning" :show.sync="dialogVisibility">
              <CCard>
                <CCardBody>
                  <CDataTable
                    hover
                    striped
                    :items="userItems"
                    :fields="userFields"
                    :items-per-page="perPage"
                    :pagination="$options.paginationProps"
                    index-column
                    clickable-rows
                    column-filter
                    sorter
                    @row-clicked="onUserSelect"
                  ></CDataTable>
                </CCardBody>
              </CCard>
              <slot slot="footer-wrapper">
                <div></div>
              </slot>
            </CModal>
          </CCard>
        </CCol>
      </CRow>
    </CCol>
  </CRow>
</template>

<script>
export default {
  name: "Warehouses",
  data: () => {
    return {
      items: [],
      fields: [
        { key: "id", label: "ID" },
        { key: "code", label: "Ambar Kodu" },
        { key: "name", label: "Ambar Adı" },
        { key: "address", label: "Adres" },
        { key: "show_details", label: "" }
      ],
      selectedUserItems: [],
      userItems: [],
      userFields: [
        { key: "id", label: "ID" },
        { key: "firstName", label: "Ad" },
        { key: "lastName", label: "Soyad" },
        { key: "email", label: "email" }
      ],
      selectedUserFields: [
        { key: "id", label: "ID" },
        { key: "firstName", label: "Ad" },
        { key: "lastName", label: "Soyad" },
        { key: "email", label: "email" },
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
    this.fetchItems();
  },
  paginationProps: {
    align: "center",
    doubleArrows: false,
    previousButtonHtml: "prev",
    nextButtonHtml: "next"
  },
  methods: {
    fetchItems() {
      this.$http
        .request({
          url: "/api/warehouse/v1/queryAll",
          method: "get",
          headers: {
            Authorization: "bearer " + this.$cookies.get("accessToken")
          }
        })
        .then(response => {
          this.items = response.data;
        });
    },
    onUserSelect(item, index) {
      if (this.selectedUserItems.indexOf(item) === -1) {
        this.selectedUserItems.push(item);
        this.dialogVisibility = false;
      }
    },
    showDialog() {
      this.$http
        .request({
          url: "/api/user/v1/queryAll",
          method: "get",
          headers: {
            Authorization: "bearer " + this.$cookies.get("accessToken")
          }
        })
        .then(response => {
          this.userItems = response.data;
          this.dialogVisibility = true;
        });
    },
    removeUser(index) {
      this.selectedUserItems.splice(index, 1);
    },
    rowClicked(item) {
      this.isnew = false;
      this.$http
        .request({
          url: `/api/warehouse/v1/warehouse/${item.id}`,
          method: "get",
          headers: {
            Authorization: "bearer " + this.$cookies.get("accessToken")
          }
        })
        .then(response => {
          this.selectedItem = response.data;
          this.selectedUserItems = response.data.wareHouseUserList;
        });
    },
    newItem() {
      this.isnew = true;
      this.selectedItem = Object.assign({}, {});
    },
    onSelectRole(role) {
      this.selectedItem.role = role;
    },
    save() {
      this.$http
        .request({
          url: "/api/warehouse/v1/save",
          method: "post",
          data: this.selectedItem,
          headers: {
            Authorization: "bearer " + this.$cookies.get("accessToken")
          }
        })
        .then(response => {
          this.fetchItems();
        })
        .catch(error => {});
    },
    saveUser() {
      const data = {};
      data.id = this.selectedItem.id;
      data.selectedUserList = [];
      this.selectedUserItems.forEach(item => {
        data.selectedUserList.push(item.id);
      });
      this.$http
        .request({
          url: "/api/warehouse/v1/saveUsers",
          method: "post",
          data: data,
          headers: {
            Authorization: "bearer " + this.$cookies.get("accessToken")
          }
        })
        .then(response => {})
        .catch(error => {});
    }
  }
};
</script>
