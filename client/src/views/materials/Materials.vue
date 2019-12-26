<template>
  <CRow>
    <CCol col="12" xl="8">
      <transition name="slide">
        <CCard>
          <CCardHeader>
            Malzemeler
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
    <CModal title="Malzeme Bilgileri" color="warning" :show.sync="dialogVisibility">
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
                    label="Malzeme Kodu"
                    placeholder="malzeme kodu"
                    v-model="selectedItem.code"
                  />
                </CCol>
              </CRow>
              <CRow>
                <CCol sm="12">
                  <CInput
                    label="Malzeme Adı"
                    placeholder="malzeme adı"
                    v-model="selectedItem.name"
                  />
                </CCol>
              </CRow>
              <CRow>
                <CCol sm="12">
                  <CInput
                    label="Minimum Miktar"
                    placeholder="minimumMiktar"
                    v-model="selectedItem.minimumAmount"
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
  name: "Materials",
  data: () => {
    return {
      items: [],
      fields: [
        { key: "id", label: "ID" },
        { key: "code", label: "Malzeme Kodu" },
        { key: "name", label: "Malzeme Adı" },
        { key: "minimumAmount", label: "Minimum Miktar" },
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
          url: "/api/material/v1/queryAll",
          method: "get",
          headers: {
            Authorization: "bearer " + this.$cookies.get("accessToken")
          }
        })
        .then(response => {
          this.items = response.data;
        });
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
          url: "/api/material/v1/save",
          method: "post",
          data: this.selectedItem,
          headers: {
            Authorization: "bearer " + this.$cookies.get("accessToken")
          }
        })
        .then(response => {
          this.dialogVisibility = false;
          this.fetchItems();
        })
        .catch(error => {});
    }
  }
};
</script>
