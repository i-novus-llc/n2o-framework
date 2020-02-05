import React from "react";
import HtmlWidget from "n2o-framework/lib/components/widgets/Html/HtmlWidget";
import SelectRegion from "../SelectRegion";

export default [
  {
    component: SelectRegion,
    name: "Регион через select",
    props: {
      pageId: "testPage",
      title: "Выберите город",
      items: [
        {
          widgetId: "city_kazan",
          label: "Казань",
          active: true
        },
        {
          widgetId: "city_moscow",
          label: "Москва"
        },
        {
          widgetId: "city_piter",
          label: "Санкт-Петербург"
        }
      ]
    },
    reduxState: {
      pages: {
        testPage: {
          metadata: {
            widgets: {
              city_kazan: {
                src: "HtmlWidget",
                html: { html: "<img style='width: 100%; max-height: 100%; max-width: 100%;' src='http://rasvet-tur.ru/wp-content/uploads/2017/09/kazan-1290x540.jpg' />" }
              },
              city_moscow: {
                src: "HtmlWidget",
                html: { html: "<img style='width: 100%; max-height: 100%; max-width: 100%;' src='https://www.svyaznoy.travel/img/scontent/images/Москва_1.jpg' />" }
              },
              city_piter: {
                src: "HtmlWidget",
                html: { html: "<img style='width: 100%; max-height: 100%; max-width: 100%;' src='http://bolshoy19.ru/upload/iblock/eb7/eb75a3724c9925941dcd3477e20e4498.jpg' />" }
              }
            }
          }
        }
      }
    },
    context: {
      getComponent: () => {
        return HtmlWidget;
      }
    }
  }
];
