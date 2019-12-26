import Vue from 'vue'
import Router from 'vue-router'

// Containers
const TheContainer = () => import('@/containers/TheContainer')

// Views
const Dashboard = () => import('@/views/Dashboard')

const Colors = () => import('@/views/theme/Colors')
const Typography = () => import('@/views/theme/Typography')

const Charts = () => import('@/views/charts/Charts')
const Widgets = () => import('@/views/widgets/Widgets')

// Views - Components
const Cards = () => import('@/views/base/Cards')
const Forms = () => import('@/views/base/Forms')
const Switches = () => import('@/views/base/Switches')
const Tables = () => import('@/views/base/Tables')
const Tabs = () => import('@/views/base/Tabs')
const Breadcrumbs = () => import('@/views/base/Breadcrumbs')
const Carousels = () => import('@/views/base/Carousels')
const Collapses = () => import('@/views/base/Collapses')
const Jumbotrons = () => import('@/views/base/Jumbotrons')
const ListGroups = () => import('@/views/base/ListGroups')
const Navs = () => import('@/views/base/Navs')
const Navbars = () => import('@/views/base/Navbars')
const Paginations = () => import('@/views/base/Paginations')
const Popovers = () => import('@/views/base/Popovers')
const ProgressBars = () => import('@/views/base/ProgressBars')
const Tooltips = () => import('@/views/base/Tooltips')

// Views - Buttons
const StandardButtons = () => import('@/views/buttons/StandardButtons')
const ButtonGroups = () => import('@/views/buttons/ButtonGroups')
const Dropdowns = () => import('@/views/buttons/Dropdowns')
const BrandButtons = () => import('@/views/buttons/BrandButtons')

// Views - Icons
const CoreUIIcons = () => import('@/views/icons/CoreUIIcons')
const Brands = () => import('@/views/icons/Brands')
const Flags = () => import('@/views/icons/Flags')

// Views - Notifications
const Alerts = () => import('@/views/notifications/Alerts')
const Badges = () => import('@/views/notifications/Badges')
const Modals = () => import('@/views/notifications/Modals')

// Views - Pages
const Page404 = () => import('@/views/pages/Page404')
const Page500 = () => import('@/views/pages/Page500')
const Login = () => import('@/views/pages/Login')
const Register = () => import('@/views/pages/Register')

// Users
const Users = () => import('@/views/users/Users')

//Materials
const Materials = () => import('@/views/materials/Materials')

//Warehouses
const Warehouses = () => import('@/views/warehouses/Warehouses')
const WarehouseActions = () => import('@/views/warehouseAction/WarehouseActions')


Vue.use(Router)

const router = new Router({
  mode: 'hash', // https://router.vuejs.org/api/#mode
  linkActiveClass: 'active',
  scrollBehavior: () => ({ y: 0 }),
  routes: configRoutes()
})

function configRoutes() {
  return [
    {
      path: '/',
      redirect: '/dashboard',
      name: 'Ana Sayfa',
      component: TheContainer,
      children: [
        {
          path: 'dashboard',
          name: 'Dashboard',
          component: Dashboard,
          meta: {
            auth: true,
            role: ['ROLE_ADMIN']
          }
        },
        {
          path: 'users',
          meta: { label: 'Kullanıcılar' },
          component: Users,
          meta: {
            auth: true,
          }
        },
        {
          path: 'materials',
          meta: { label: 'Malzemeler' },
          component: Materials,
          meta: {
            auth: true,
          }
        },
        {
          path: 'warehouses',
          meta: { label: 'Ambarlar' },
          component: Warehouses,
          meta: {
            auth: true,
          }
        },
        {
          path: 'warehouseActions',
          meta: { label: 'Ambar Hareketleri' },
          component: WarehouseActions,
          meta: {
            auth: true,
          }
        }]
    },
    {
      path: '/pages',
      redirect: '/pages/404',
      name: 'Pages',
      component: {
        render(c) { return c('router-view') }
      },
      children: [
        {
          path: '404',
          name: 'Page404',
          component: Page404
        },
        {
          path: '500',
          name: 'Page500',
          component: Page500
        },
        {
          path: 'login',
          name: 'Login',
          component: Login
        },
        {
          path: 'register',
          name: 'Register',
          component: Register
        }
      ]
    }
  ]
}
const auth = {
  loggedIn() {
    return Vue.$cookies.get('accessToken');
  },
  logout() {
    Vue.$cookies.remove('accessToken')
  }
};
router.beforeEach((to, from, next) => {
  let authrequired = false;
  if (to && to.meta && to.meta.auth) authrequired = true;

  //console.log('authrequired', authrequired, to.name)
  if (authrequired) {
    if (auth.loggedIn()) {
      if (to.name === "Login") {
        next({
          name: "Login"
        });
        return false;
      } else {
        next();
      }
    } else {
      if (to.name !== "Login") {
        next({
          name: "Login",
          params: { redirectPage: to.fullPath }
        });
        return;
      }
      next();
    }
  } else {
    if (auth.loggedIn() && to.name === "Login") {
      next("/pages/login");
      return;
    } else {
      next();
    }
  }
})

export default router;
