export default [
  {
    _name: 'CSidebarNav',
    _children: [
      {
        _name: 'CSidebarNavItem',
        name: 'Dashboard',
        to: '/dashboard',
        icon: 'cil-speedometer',
      },
      {
        _name: 'CSidebarNavItem',
        name: 'Malzeme Tanımları',
        to: '/materials',
        icon: 'cil-user'
      },
      {
        _name: 'CSidebarNavItem',
        name: 'Ambar Tanımları',
        to: '/warehouses',
        icon: 'cil-location-pin'
      },
      {
        _name: 'CSidebarNavItem',
        name: 'Ambar Hareketleri',
        to: '/warehouseActions',
        icon: 'cil-bell'
      }
    ]
  }
]