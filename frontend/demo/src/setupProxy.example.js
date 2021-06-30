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
    //                 src: "SimpleHeader",
    //                 className: "customSimpleHeaderclassName",
    //                 style: {},
    //                 logo: {
    //                     title: "N2O logo",
    //                     className: "customSimpleHeaderLogoClassName",
    //                     style: {},
    //                     href: "/",
    //                     src: "https://avatars0.githubusercontent.com/u/25926683?s=200&v=4",
    //                 },
    //                 menu: {
    //                     src: "string",
    //                     items: [{
    //                         id: "menuItem0",
    //                         type: 'dropdown',
    //                         href: "/",
    //                         target: '_blank',
    //                         items:[
    //                             {
    //                                 id: "subMenuItem0",
    //                                 type: 'link',
    //                                 href: "/",
    //                                 target: '_blank',
    //                                 items:[],
    //                                 icon: "fa fa square",
    //                                 security: {},
    //                                 title: "subMenuItem0",
    //                                 badge: "badge",
    //                                 badgeColor: "warning"
    //                             }
    //                         ],
    //                         icon: "fa fa square",
    //                         security: {},
    //                         title: "menuItem0",
    //                     }]
    //                 },
    //                 extraMenu: {
    //                     src: "string",
    //                     items: [{
    //                         id: "menuItem0",
    //                         type: 'dropdown',
    //                         href: "/",
    //                         target: '_blank',
    //                         items:[
    //                             {
    //                                 id: "subMenuItem0",
    //                                 type: 'link',
    //                                 href: "/",
    //                                 target: '_blank',
    //                                 items:[],
    //                                 icon: "fa fa square",
    //                                 security: {},
    //                                 title: "subMenuItem0",
    //                             }
    //                         ],
    //                         icon: "fa fa square",
    //                         security: {},
    //                         title: "menuItem0",
    //                     }]
    //                 },
    //                 search: {},
    //                 sidebarSwitcher: {
    //                     defaultIcon: "fa fa-times",
    //                     toggledIcon: "fa fa-bars",
    //                 },
    //             },
    //             sidebar: {
    //                 src: "string",
    //                 className: "string",
    //                 style: "string",
    //                 logo: {
    //                     title: "string",
    //                     className: "string",
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
    //                 className: "string",
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
      // target: "https://n2oapp.net/sandbox/view/NL2hn" //прокси на сендбокс
      target: "https://n2o.i-novus.ru/next/demo/",
      changeOrigin: true
    })
  );
};
