import React from 'react';
import { connect } from 'react-redux';
import { storiesOf } from '@storybook/react';

import set from 'lodash/set';
import omit from 'lodash/omit';
import pullAt from 'lodash/pullAt';
import pick from 'lodash/pick';

import ListRegion, { ListRegion as ListRegionComponent } from './ListRegion';
import ListRegionJson from './ListRegion.meta';
import { metadataSuccess } from '../../../actions/pages';
import HtmlWidgetJson from '../../widgets/Html/HtmlWidget.meta';
import ListMetadata from './ListMetadata.meta';
import AuthButtonContainer from '../../../core/auth/AuthLogin';
import { makeStore } from '../../../../.storybook/decorators/utils';
import cloneObject from '../../../utils/cloneObject';
import { InitWidgetsList } from 'N2oStorybook/json';
import ListWithDependency from 'N2oStorybook/json/ListWithDependency';
import fetchMock from 'fetch-mock';
import { getStubData } from 'N2oStorybook/fetchMock';
import CheckboxN2O from '../../controls/Checkbox/CheckboxN2O';
import Factory from '../../../core/factory/Factory';

const stories = storiesOf('Регионы/Лист', module);

stories.addParameters({
  info: {
    propTables: [ListRegionComponent],
    propTablesExclude: [ListRegion, Factory, AuthButtonContainer],
  },
});
// const ListRegionJson = set(
//   cloneObject(SecureListRegionJson),
//   'items',
//   pullAt(cloneObject(SecureListRegionJson).items, 0)
// );
const { store } = makeStore();

stories.add(
  'Метаданные',
  () => {
    return <ListRegion {...ListRegionJson} pageId="Page" />;
  },
  {
    info: {
      text: `
      Компонент 'Регион Список'
      ~~~js
      import ListRegion from 'n2o-framework/lib/components/regions/List/ListRegion';
      
      <ListRegion 
          pageId="Page"
          content={[
            {
            "widgetId": "Page_Html",
            "label": "HTML",
            "opened": true
            }
          ]}
       />
      ~~~
      `,
    },
  }
);
// .add(
//   'Ограничение доступа',
//   () => {
//     store.dispatch(metadataSuccess('Page', ListMetadata));
//     return (
//       <div>
//         <small>
//           Введите <mark>admin</mark>, чтобы увидеть скрытый виджет региона
//         </small>
//         <AuthButtonContainer />
//         <br />
//         <ListRegion {...SecureListRegionJson} pageId="Page" />
//       </div>
//     );
//   },
//   {
//     info: {
//       text: `
//     Компонент 'Регион Список'
//     ~~~js
//     import ListRegion from 'n2o-framework/lib/components/regions/List/ListRegion';
//
//     <ListRegion
//         pageId="Page"
//         items={[
//           {
//             "widgetId": "Page_Html",
//             "label": "HTML",
//             "opened": true
//           },
//           {
//             "widgetId": "Page.SecureHtml",
//             "label": "HTML (secure)",
//             "opened": true,
//             "security": {
//               "roles": ["admin"]
//             }
//           }
//         ]}
//      />
//     ~~~
//     `,
//     },
//   }
// )
//
// .add('Инициализация виджетов', () => {
//   fetchMock
//     .restore()
//     .get('begin:n2o/data/test', getStubData)
//     .get('begin:n2o/data2/test', async url => {
//       await new Promise(r =>
//         setTimeout(() => {
//           r();
//         }, 2000)
//       );
//       return getStubData(url);
//     });
//
//   store.dispatch(
//     metadataSuccess('Page', { ...pick(InitWidgetsList, 'widgets') })
//   );
//
//   return <ListRegion {...omit(InitWidgetsList, 'widgets')} pageId="Page" />;
// })
// .add('Зависимость региона от виджета', () => {
//   fetchMock
//     .restore()
//     .get('begin:n2o/data/test', getStubData)
//     .get('begin:n2o/data2/test', async url => {
//       await new Promise(r =>
//         setTimeout(() => {
//           r();
//         }, 2000)
//       );
//       return getStubData(url);
//     });
//
//   store.dispatch(
//     metadataSuccess('Page', { ...pick(ListWithDependency, 'widgets') })
//   );
//
//   return (
//     <React.Fragment>
//       <div>Первый collapse скрыт по зависимости виджета</div>
//       <ListRegion {...ListWithDependency} pageId="Page" />
//     </React.Fragment>
//   );
// });
