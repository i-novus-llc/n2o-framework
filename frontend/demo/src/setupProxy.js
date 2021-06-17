const proxy = require("http-proxy-middleware");

module.exports = function(app) {
  // app.post('/n2o/locale', (req, res) => {
  //   res.status(200);
  //   res.send();
  // });

  //   заготовка нового mock config

    // app.get("/n2o/config", (req, res) => {
    //     res.send({
    //         menu: {
    //             layout: {
    //                 fullSizeHeader: true,
    //                 fixed: false,
    //             },
    //             header: {
    //                 src: "string",
    //                 class: "string",
    //                 style: "string",
    //                 logo: {
    //                     title: "string",
    //                     class: "string",
    //                     style: "string",
    //                     href: "string",
    //                     src: "string",
    //                 },
    //                 menu: {
    //                     src: "string",
    //                     items: [{
    //                         id: "string",
    //                         type: 'link',
    //                         href: "string",
    //                         target: '_blank',
    //                         items:[],
    //                         icon: "string",
    //                         security: {},
    //                         title: "string",
    //                     }]
    //                 },
    //                 extraMenu: {
    //                     src: "string",
    //                     items: [{
    //                         id: "string",
    //                         type: 'link',
    //                         href: "string",
    //                         target: '_blank',
    //                         items:[],
    //                         icon: "string",
    //                         security: {},
    //                         title: "string",
    //                     }]
    //                 },
    //                 search: {},
    //                 sidebarSwitcher: {
    //                     defaultIcon: "string",
    //                     toggledIcon: "string",
    //                 },
    //             },
    //             sidebar: {
    //                 src: "string",
    //                 class: "string",
    //                 style: "string",
    //                 logo: {
    //                     title: "string",
    //                     class: "string",
    //                     style: "string",
    //                     href: "string",
    //                     src: "string",
    //                 },
    //                 menu: {
    //                     src: "string",
    //                     items: [{
    //                         id: "string",
    //                         type: 'link',
    //                         href: "string",
    //                         target: '_blank',
    //                         items:[],
    //                         icon: "string",
    //                         security: {},
    //                         title: "string",
    //                     }]
    //                 },
    //                 extraMenu: {
    //                     src: "string",
    //                     items: [{
    //                         id: "string",
    //                         type: 'link',
    //                         href: "string",
    //                         target: '_blank',
    //                         items:[],
    //                         icon: "string",
    //                         security: { },
    //                         title: "string",
    //                     }]
    //                 },
    //                 side: 'right',
    //                 defaultState: 'none',
    //                 toggledState: 'none',
    //                 toggleOnHover: true,
    //                 overlay: false,
    //             },
    //             footer: {
    //                 textRight: "string",
    //                 textLeft: "string",
    //                 src: "string",
    //                 class: "string",
    //                 style: "string",
    //             },
    //         },
    //         user: {
    //             username: null,
    //             testProperty: "testProperty"
    //         },
    //         messages: {},
    //         modules: {}
    //     })
    // })

  app.use(
    proxy("/n2o", {
      target: "https://n2o.i-novus.ru/next/demo/",
      changeOrigin: true
    })
  );
};
