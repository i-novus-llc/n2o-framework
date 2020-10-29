import React from 'react';
import { storiesOf } from '@storybook/react';
import set from 'lodash/set';
import pullAt from 'lodash/pullAt';

import PanelRegion, {
  PanelRegion as PanelRegionComponent,
} from './PanelRegion';

import PanelRegionMeta from './PanelRegion.meta.json';

import PanelShortHand from '../../snippets/Panel/PanelShortHand';
import Wireframe from '../../snippets/Wireframe/Wireframe';

import { metadataSuccess } from '../../../actions/pages';
import ListMetadata from '../List/ListMetadata.meta';
import SecurePanelRegion from './PanelRegion.meta';
import AuthButtonContainer from '../../../core/auth/AuthLogin';
import { makeStore } from '../../../../.storybook/decorators/utils';
import cloneObject from '../../../utils/cloneObject';
import panelStyles from '../../snippets/Panel/panelStyles';
import { dataSuccessWidget, hideWidget } from '../../../actions/widgets';
import Factory from '../../../core/factory/Factory';

const stories = storiesOf('Регионы/Панель', module);

stories.addParameters({
  info: {
    propTables: [PanelRegionComponent],
    propTablesExclude: [Factory, PanelRegion, AuthButtonContainer],
  },
});

const { store } = makeStore();

stories.add(
  'Метаданные',
  () => {
    return <PanelRegion {...PanelRegionMeta} pageId="Page" />;
  },
  {
    info: {
      text: `
      Компонент 'Регион панель'
      ~~~js
      import RegionPanel from 'n2o-framework/lib/components/regions/Panel/RegionPanel';
      
      <RegionPanel 
          className="test"
          color="secondary"
          icon="fa fa-exclamation"
          hasTabs="false"
          headerTitle="Заголовок панели"
          footerTitle="Подвал панели"
          open={true}
          collapsible={true}
          fullScreen={false}
          header={true}
          panels={[
            {
              "icon": "fa fa-plus",
              "label": "Первый таб",
              "id": "panel1",
              "opened": true,
              "widgetId": "Page_Html",
              "isVisible": false,
              "dependency": {
                "visible": [
                  {
                    "bindLink": "models.resolve",
                    "condition": false
                  }
                ]
              }
            }
          ]}
      />
      ~~~
      `,
    },
  }
);
// .add('Ограничение доступа', () => {
//   return (
//     <div>
//       <small>
//         Введите <mark>admin</mark>, чтобы увидеть скрытый виджет региона
//       </small>
//       <AuthButtonContainer />
//       <br />
//       <PanelRegion {...SecurePanelRegion} pageId="Page" />
//     </div>
//   );
// })
// .add(
//   'Компоновки',
//   () => {
//     const panelParams = [
//       { ...defaultProps, headerTitle: 'Только заголовок' },
//       {
//         ...defaultProps,
//         headerTitle: 'Заголовок и подвал',
//         footerTitle: 'Подвал',
//       },
//       { ...defaultProps, header: false, footerTitle: 'Без заголовка' },
//     ];
//
//     return (
//       <div className="row">
//         {panelParams.map(item => (
//           <div className="col-md-6">
//             <PanelRegion {...item} pageId="Page" />
//           </div>
//         ))}
//       </div>
//     );
//   },
//   {
//     info: {
//       text: `
//     Компонент 'Регион панель'
//     ~~~js
//     import RegionPanel from 'n2o-framework/lib/components/regions/Panel/RegionPanel';
//
//     <RegionPanel
//         {...props}
//         headerTitle="Заголовок панели"
//     />
//     <RegionPanel
//         {...props}
//         headerTitle="Заголовок панели"
//         footerTitle="Подвал"
//     />
//     <RegionPanel
//         {...props}
//         header={null}
//     />
//     ~~~
//     `,
//     },
//   }
// )
// .add(
//   'Сворачивание',
//   () => {
//     const commonProps = {
//       footerTitle: 'Подвал',
//       collapsible: true,
//       fullScreen: true,
//     };
//
//     const panelParams = [
//       { ...defaultProps, ...commonProps, headerTitle: 'Открыто' },
//       {
//         ...defaultProps,
//         ...commonProps,
//         headerTitle: 'Закрыто',
//         open: false,
//       },
//     ];
//
//     return (
//       <div className="row">
//         <div className="col-md-6">
//           <PanelShortHand {...panelParams[0]}>
//             <div style={{ height: 75, position: 'relative' }}>
//               <Wireframe title="Контент №1" />
//             </div>
//           </PanelShortHand>
//         </div>
//         <div className="col-md-6">
//           <PanelShortHand {...panelParams[1]}>
//             <div style={{ height: 75, position: 'relative' }}>
//               <Wireframe title="Контент №2" />
//             </div>
//           </PanelShortHand>
//         </div>
//       </div>
//     );
//   },
//   {
//     info: {
//       text: `
//     Компонент 'Регион панель'
//     ~~~js
//     import RegionPanel from 'n2o-framework/lib/components/regions/Panel/RegionPanel';
//
//     <RegionPanel
//         {...props}
//         headerTitle="Открыто"
//     />
//     <RegionPanel
//         {...props}
//         headerTitle="Закрыто"
//         open={false}
//     />
//     ~~~
//     `,
//     },
//   }
// )
// .add(
//   'Цвета',
//   () => {
//     /**
//      *  Создаёт массив пропсов для создания множества панелей
//      *  @param {object} styles - доступные стили
//      */
//     const createPanelParams = styles =>
//       Object.values(styles).map(color => ({
//         ...defaultProps,
//         color,
//         headerTitle: color,
//       }));
//
//     const panelParams = createPanelParams(panelStyles);
//
//     return (
//       <div className="row">
//         {panelParams.map(item => (
//           <div className="col-md-6 mb-2">
//             <PanelRegion {...item} pageId="Page" />
//           </div>
//         ))}
//       </div>
//     );
//   },
//   {
//     info: {
//       text: `
//     Компонент 'Регион панель'
//     ~~~js
//     import RegionPanel from 'n2o-framework/lib/components/regions/Panel/RegionPanel';
//
//     <RegionPanel
//         {...props}
//         headerTitle="secondary"
//         color="secondary"
//     />
//     <RegionPanel
//         {...props}
//         headerTitle="primary"
//         color="primary"
//     />
//     <RegionPanel
//         {...props}
//         headerTitle="success"
//         color="success"
//     />
//     <RegionPanel
//         {...props}
//         headerTitle="info"
//         color="info"
//     />
//     <RegionPanel
//         {...props}
//         headerTitle="warning"
//         color="warning"
//     />
//     <RegionPanel
//         {...props}
//         headerTitle="danger"
//         color="danger"
//     />
//     ~~~
//     `,
//     },
//   }
// )
// .add(
//   'На полный экран',
//   () => {
//     const panelParams = [
//       {
//         ...defaultProps,
//         headerTitle: 'С кнопкой переключения',
//         fullScreen: true,
//       },
//       {
//         ...defaultProps,
//         headerTitle: 'Без кнопки переключения',
//         fullScreen: false,
//       },
//     ];
//
//     return (
//       <div className="row">
//         {panelParams.map(item => (
//           <div className="col-md-6">
//             <PanelRegion {...item} pageId="Page" />
//           </div>
//         ))}
//       </div>
//     );
//   },
//   {
//     info: {
//       text: `
//     Компонент 'Регион панель'
//     ~~~js
//     import RegionPanel from 'n2o-framework/lib/components/regions/Panel/RegionPanel';
//
//     <RegionPanel
//         {...props}
//         headerTitle="С кнопкой переключения"
//         fullScreen={true}
//     />
//     <RegionPanel
//         {...props}
//         headerTitle="Без кнопки переключения"
//         fullScreen={false}
//     />
//     ~~~
//     `,
//     },
//   }
// )
// .add(
//   'Вкладки',
//   () => {
//     const panelParams = [
//       { ...defaultTwoPanels, headerTitle: 'С вкладками', hasTabs: true },
//       { ...defaultTwoPanels, headerTitle: 'Без вкладок', hasTabs: false },
//     ];
//
//     return (
//       <div className="row">
//         {panelParams.map(item => (
//           <div className="col-md-12">
//             <PanelRegion {...item} pageId="Page" />
//           </div>
//         ))}
//       </div>
//     );
//   },
//   {
//     info: {
//       text: `
//     Компонент 'Регион панель'
//     ~~~js
//     import RegionPanel from 'n2o-framework/lib/components/regions/Panel/RegionPanel';
//
//     <RegionPanel
//         {...props}
//         headerTitle="С вкладками"
//         hasTabs={true}
//     />
//     <RegionPanel
//         {...props}
//         headerTitle="Без вкладок"
//         hasTabs={false}
//     />
//     ~~~
//     `,
//     },
//   }
// )
// .add(
//   'Вкладки с экшенами',
//   () => {
//     const panelParams = [
//       {
//         ...defaultTwoPanels,
//         headerTitle: 'С вкладками',
//         hasTabs: true,
//         fullScreen: true,
//         collapsible: true,
//       },
//     ];
//
//     return (
//       <div className="row">
//         {panelParams.map(item => (
//           <div className="col-md-12">
//             <PanelRegion {...item} pageId="Page" />
//           </div>
//         ))}
//       </div>
//     );
//   },
//   {
//     info: {
//       text: `
//     Компонент 'Регион панель'
//     ~~~js
//     import RegionPanel from 'n2o-framework/lib/components/regions/Panel/RegionPanel';
//
//     <RegionPanel
//         {...props}
//         headerTitle="С вкладками"
//         hasTabs={true}
//         fullScreen={true}
//         collapsible={true}
//     />
//     ~~~
//     `,
//     },
//   }
// )
// .add(
//   'Тулбар',
//   () => {
//     const panelParams = [
//       {
//         ...defaultTwoPanels,
//         headerTitle: 'Тулбар в заголовке',
//         hasTabs: false,
//         toolbar: [
//           {
//             id: 1,
//             disabled: false,
//             onClick: () => alert('Hello World!'),
//             header: 'Экшен',
//           },
//         ],
//       },
//     ];
//
//     return (
//       <div className="row">
//         {panelParams.map(item => (
//           <div className="col-md-12">
//             <PanelRegion {...item} pageId="Page" />
//           </div>
//         ))}
//       </div>
//     );
//   },
//   {
//     info: {
//       text: `
//     Компонент 'Регион панель'
//     ~~~js
//     import RegionPanel from 'n2o-framework/lib/components/regions/Panel/RegionPanel';
//
//     <RegionPanel
//         {...props}
//         headerTitle="Тулбар в заголовке"
//         hasTabs={false}
//         toolbar={[
//           {
//             id: 1,
//             disabled: false,
//             onClick,
//             header: 'Экшен'
//           }
//         ]}
//     />
//     ~~~
//     `,
//     },
//   }
// )
// .add(
//   'Экшены с тулбаром',
//   () => {
//     const panelParams = [
//       {
//         ...defaultTwoPanels,
//         headerTitle: 'Тулбар в заголовке',
//         hasTabs: false,
//         fullScreen: true,
//         collapsible: true,
//         toolbar: [
//           {
//             id: 1,
//             disabled: false,
//             onClick: () => alert('Hello World!'),
//             header: 'Экшен',
//           },
//         ],
//       },
//     ];
//
//     return (
//       <div className="row">
//         {panelParams.map(item => (
//           <div className="col-md-12">
//             <PanelRegion {...item} pageId="Page" />
//           </div>
//         ))}
//       </div>
//     );
//   },
//   {
//     info: {
//       text: `
//     Компонент 'Регион панель'
//     ~~~js
//     import RegionPanel from 'n2o-framework/lib/components/regions/Panel/RegionPanel';
//
//     <RegionPanel
//         {...props}
//         headerTitle="Тулбар в заголовке"
//         hasTabs={true}
//         fullScreen={true}
//         collapsible={true}
//         toolbar={[
//           {
//             id: 1,
//             disabled: false,
//             onClick,
//             header: 'Экшен'
//           }
//         ]}
//     />
//     ~~~
//     `,
//     },
//   }
// )
// .add('Скрытие панели при visible = false всех виджетов в ней', () => {
//   const props = {
//     className: PanelRegionJson.className,
//     color: PanelRegionJson.color,
//     icon: PanelRegionJson.icon,
//     hasTabs: PanelRegionJson.hasTabs,
//     headerTitle: PanelRegionJson.headerTitle,
//     footerTitle: PanelRegionJson.footerTitle,
//     open: PanelRegionJson.open,
//     collapsible: PanelRegionJson.collapsible,
//     fullScreen: PanelRegionJson.fullScreen,
//     header: PanelRegionJson.header,
//     panels: PanelRegionJson.panels,
//   };
//   store.dispatch(
//     metadataSuccess('Page', {
//       widgets: {
//         'visible-test': {
//           src: 'HtmlWidget',
//           html: {
//             url: null,
//             html: '<h1>Hello</h1>',
//             fetchOnInit: false,
//           },
//         },
//       },
//     })
//   );
//   store.dispatch(
//     dataSuccessWidget('visible-test', {
//       widgets: {
//         'visible-test': {
//           src: 'HtmlWidget',
//           html: {
//             url: null,
//             html: '<h1>Hello</h1>',
//             fetchOnInit: false,
//           },
//         },
//       },
//     })
//   );
//   store.dispatch(hideWidget('visible-test'));
//   const panels = [
//     {
//       icon: 'fa fa-plus',
//       label: 'Первый таб',
//       id: 'visible-test',
//       opened: true,
//       widgetId: 'visible-test',
//       isVisible: false,
//       dependency: {
//         visible: [
//           {
//             bindLink: 'models.resolve',
//             condition: false,
//           },
//         ],
//       },
//     },
//   ];
//   return (
//     <React.Fragment>
//       <div>Панель полностью скрыта</div>
//       <PanelRegion {...props} panels={panels} pageId="Page" />
//     </React.Fragment>
//   );
// });
