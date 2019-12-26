module.exports = {
  lintOnSave: false,
  runtimeCompiler: true,
  configureWebpack: {
    //Necessary to run npm link https://webpack.js.org/configuration/resolve/#resolve-symlinks
    resolve: {
       symlinks: false
    }
  },
  runtimeCompiler:true,
  devServer: {
    port: 8081,
    //sockPath: '/vue/sockjs-node',
    proxy: {
      '/api': {
        target: 'http://localhost:8080'
      }
    },
    watchOptions: {
      poll: 1000
    }
  }
}
