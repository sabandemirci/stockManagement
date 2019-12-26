<template>
  <CRow>
    <CCol xl="12" lg="12" sm="12" xs="12">
      <CRow>
        <CCol col="12">
          <CCard>
            <CCardHeader>Detay</CCardHeader>
            <CCardBody>
              <CRow>
                <CCol sm="2">
                  <CSelect
                    label="Ambar"
                    :options="warehouseList"
                    @update:value="onSelectWarehouse"
                  />
                </CCol>
                <CCol sm="2">
                  <CSelect
                    label="Giriş/Çıkış"
                    :options="['INPUT','OUTPUT']"
                    v-model="action"
                    @update:value="onSelectAction"
                  />
                </CCol>
                <CCol sm="2">
                  <CInput label="Tedarikçi" placeholder v-model="supplier" />
                </CCol>
                <CCol sm="2">
                  <CInput label="Miktar" placeholder v-model="amount" />
                </CCol>
                <CCol sm="2">
                  <div role="group" class="form-group">
                    <label class>Malzeme</label>
                    <CButton
                      color="success"
                      label="Malzeme"
                      size
                      class="form-control"
                      @click="showMaterialDialog"
                    >{{selectedMaterial.name ? selectedMaterial.name : 'Seçiniz'}}</CButton>
                  </div>
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
            <CCardHeader>Ambar hareketleri</CCardHeader>
            <CCardBody>
              <CDataTable
                hover
                striped
                :items="warehouseActionList"
                :fields="warehouseActionFields"
                :items-per-page="5"
                :pagination="$options.paginationProps"
                index-column
                clickable-rows
              ></CDataTable>
            </CCardBody>
          </CCard>
        </CCol>
      </CRow>
    </CCol>
    <CModal title="Malzeme Seç" color="warning" :show.sync="dialogVisibility">
      <CCard>
        <CCardBody>
          <CDataTable
            hover
            striped
            :items="materialList"
            :fields="materialFields"
            :items-per-page="5"
            :pagination="$options.paginationProps"
            index-column
            clickable-rows
            column-filter
            sorter
            @row-clicked="onMaterialSelect"
          ></CDataTable>
        </CCardBody>
      </CCard>
      <slot slot="footer-wrapper">
        <div></div>
      </slot>
    </CModal>
  </CRow>
</template>

<script>
export default {
  name: "Warehouses",
  paginationProps: {
    align: "center",
    doubleArrows: false,
    previousButtonHtml: "prev",
    nextButtonHtml: "next"
  },
  data: () => {
    return {
      items: [],
      warehouseList: [],
      materialList: [],
      action: "INPUT",
      amount: 0,
      supplier: "",
      selectedWareHouse: {},
      dialogVisibility: false,
      selectedMaterial: {},
      materialFields: [
        { key: "id", label: "ID" },
        { key: "code", label: "Kod" },
        { key: "name", label: "Malzeme Adı" }
      ],
      warehouseActionList: [],
      warehouseActionFields: [
        { key: "warehouse", label: "Ambar" },
        { key: "material", label: "Malzeme" },
        { key: "action", label: "Giriş/Çıkış" },
        { key: "amount", label: "Miktar" },
        { key: "supplier", label: "Tedarikçi" },
        { key: "createdTimestamp", label: "İşlem Tarihi" }
      ]
    };
  },
  beforeMount() {
    this.fetchWarehousesByUser();
  },

  methods: {
    fetchWarehousesByUser() {
      this.$http
        .request({
          url: "/api/warehouse/v1/fetchWarehousesByUser",
          method: "get",
          headers: {
            Authorization: "bearer " + this.$cookies.get("accessToken")
          }
        })
        .then(response => {
          response.data.forEach(item => {
            this.warehouseList.push({
              label: item.name,
              value: item
            });
            this.selectedWareHouse = response.data[0];
            this.fetchWarehouseActionsByWarehouse();
          });
        });
    },
    fetchWarehouseActionsByWarehouse() {
      this.$http
        .request({
          url: `/api/warehouseAction/v1/fetchWarehouseActionsByWarehouse/${this.selectedWareHouse.id}`,
          method: "get",
          headers: {
            Authorization: "bearer " + this.$cookies.get("accessToken")
          }
        })
        .then(response => {
          debugger;
          this.warehouseActionList = response.data;
        });
    },
    save() {
      this.$http
        .request({
          url: "/api/warehouseAction/v1/save",
          method: "post",
          data: {
            warehouseId: this.selectedWareHouse.id,
            action: this.action,
            supplier: this.supplier,
            amount: this.amount,
            material: this.selectedMaterial.id
          },
          headers: {
            Authorization: "bearer " + this.$cookies.get("accessToken")
          }
        })
        .then(response => {
          this.action = "INPUT";
          this.supplier = "";
          this.amount = 0;
          this.selectedMaterial = {};
          this.fetchWarehouseActionsByWarehouse();
        })
        .catch(error => {});
    },
    onSelectWarehouse(item) {
      this.selectedWareHouse = item;
    },
    onSelectAction(item) {
      this.action = item;
    },
    onMaterialSelect(item, index) {
      this.selectedMaterial = item;
      this.dialogVisibility = false;
    },
    showMaterialDialog() {
      this.$http
        .request({
          url: "/api/material/v1/queryAll",
          method: "get",
          headers: {
            Authorization: "bearer " + this.$cookies.get("accessToken")
          }
        })
        .then(response => {
          this.materialList = response.data;
          this.dialogVisibility = true;
        });
    }
  }
};
</script>
