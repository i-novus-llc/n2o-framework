import React from 'react';
import { storiesOf } from '@storybook/react';
import { withKnobs, boolean, text } from '@storybook/addon-knobs/react';
import withTests from 'N2oStorybook/withTests';
import { set, pullAt } from 'lodash';

import PanelRegion from './PanelRegion';
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

const stories = storiesOf('Регионы/Панель', module);

stories.addDecorator(withKnobs);
stories.addDecorator(withTests('PanelRegion'));

const PanelRegionJson = set(
  cloneObject(SecurePanelRegion),
  'panels',
  pullAt(cloneObject(SecurePanelRegion).panels, 0)
);
const defaultProps = { panels: PanelRegionJson.panels.slice(0, 1) };
const defaultTwoPanels = {
  panels: [
    {
      icon: 'fa fa-plus',
      label: 'Первый таб',
      id: 'panel1',
      opened: true,
      widgetId: 'Page_Html',
      isVisible: false,
      dependency: {
        visible: [
          {
            bindLink: 'models.resolve',
            condition: false,
          },
        ],
      },
    },
    {
      icon: 'fa fa-plus',
      label: 'Второй таб',
      id: 'panel2',
      opened: false,
      widgetId: 'Page_Html',
      isVisible: false,
      dependency: {
        visible: [
          {
            bindLink: 'models.resolve',
            condition: false,
          },
        ],
      },
    },
  ],
};
const { store } = makeStore();

stories
  .addDecorator(storyFn => {
    store.dispatch(metadataSuccess('Page', ListMetadata));
    return storyFn();
  })
  .add('Метаданные', () => {
    const props = {
      className: text('className', PanelRegionJson.className),
      color: text('color', PanelRegionJson.color),
      icon: text('icon', PanelRegionJson.icon),
      hasTabs: boolean('hasTabs', PanelRegionJson.hasTabs),
      headerTitle: text('headerTitle', PanelRegionJson.headerTitle),
      footerTitle: text('footerTitle', PanelRegionJson.footerTitle),
      open: boolean('open', PanelRegionJson.open),
      collapsible: boolean('collapsible', PanelRegionJson.collapsible),
      fullScreen: boolean('fullScreen', PanelRegionJson.fullScreen),
      header: boolean('header', PanelRegionJson.header),
      panels: PanelRegionJson.panels,
    };

    return <PanelRegion {...props} pageId="Page" />;
  })
  .add('Ограничение доступа', () => {
    return (
      <div>
        <small>
          Введите <mark>admin</mark>, чтобы увидеть скрытый виджет региона
        </small>
        <AuthButtonContainer />
        <br />
        <PanelRegion {...SecurePanelRegion} pageId="Page" />
      </div>
    );
  })
  .add('Компоновки', () => {
    const panelParams = [
      { ...defaultProps, headerTitle: 'Только заголовок' },
      {
        ...defaultProps,
        headerTitle: 'Заголовок и подвал',
        footerTitle: 'Подвал',
      },
      { ...defaultProps, header: false, footerTitle: 'Без заголовка' },
    ];

    return (
      <div className="row">
        {panelParams.map(item => (
          <div className="col-md-6">
            <PanelRegion {...item} pageId="Page" />
          </div>
        ))}
      </div>
    );
  })
  .add('Сворачивание', () => {
    const commonProps = {
      footerTitle: 'Подвал',
      collapsible: true,
      fullScreen: true,
    };

    const panelParams = [
      { ...defaultProps, ...commonProps, headerTitle: 'Открыто' },
      { ...defaultProps, ...commonProps, headerTitle: 'Закрыто', open: false },
    ];

    return (
      <div className="row">
        <div className="col-md-6">
          <PanelShortHand {...panelParams[0]}>
            <div style={{ height: 75, position: 'relative' }}>
              <Wireframe title="Контент №1" />
            </div>
          </PanelShortHand>
        </div>
        <div className="col-md-6">
          <PanelShortHand {...panelParams[1]}>
            <div style={{ height: 75, position: 'relative' }}>
              <Wireframe title="Контент №2" />
            </div>
          </PanelShortHand>
        </div>
      </div>
    );
  })
  .add('Цвета', () => {
    /**
     *  Создаёт массив пропсов для создания множества панелей
     *  @param {object} styles - доступные стили
     */
    const createPanelParams = styles =>
      Object.values(styles).map(color => ({
        ...defaultProps,
        color,
        headerTitle: color,
      }));

    const panelParams = createPanelParams(panelStyles);

    return (
      <div className="row">
        {panelParams.map(item => (
          <div className="col-md-6 mb-2">
            <PanelRegion {...item} pageId="Page" />
          </div>
        ))}
      </div>
    );
  })
  .add('На полный экран', () => {
    const panelParams = [
      {
        ...defaultProps,
        headerTitle: 'С кнопкой переключения',
        fullScreen: true,
      },
      {
        ...defaultProps,
        headerTitle: 'Без кнопки переключения',
        fullScreen: false,
      },
    ];

    return (
      <div className="row">
        {panelParams.map(item => (
          <div className="col-md-6">
            <PanelRegion {...item} pageId="Page" />
          </div>
        ))}
      </div>
    );
  })
  .add('Вкладки', () => {
    const panelParams = [
      { ...defaultTwoPanels, headerTitle: 'С вкладками', hasTabs: true },
      { ...defaultTwoPanels, headerTitle: 'Без вкладок', hasTabs: false },
    ];

    return (
      <div className="row">
        {panelParams.map(item => (
          <div className="col-md-12">
            <PanelRegion {...item} pageId="Page" />
          </div>
        ))}
      </div>
    );
  })
  .add('Тулбар', () => {
    const panelParams = [
      {
        ...defaultTwoPanels,
        headerTitle: 'С вкладками',
        hasTabs: false,
        toolbar: [
          {
            id: 1,
            disabled: false,
            onClick: () => {},
            header: 'test',
          },
        ],
      },
    ];

    return (
      <div className="row">
        {panelParams.map(item => (
          <div className="col-md-12">
            <PanelRegion {...item} pageId="Page" />
          </div>
        ))}
      </div>
    );
  })
  .add('Скрытие панели при visible = false всех виджетов в ней', () => {
    const props = {
      className: text('className', PanelRegionJson.className),
      color: text('color', PanelRegionJson.color),
      icon: text('icon', PanelRegionJson.icon),
      hasTabs: boolean('hasTabs', PanelRegionJson.hasTabs),
      headerTitle: text('headerTitle', PanelRegionJson.headerTitle),
      footerTitle: text('footerTitle', PanelRegionJson.footerTitle),
      open: boolean('open', PanelRegionJson.open),
      collapsible: boolean('collapsible', PanelRegionJson.collapsible),
      fullScreen: boolean('fullScreen', PanelRegionJson.fullScreen),
      header: boolean('header', PanelRegionJson.header),
      panels: PanelRegionJson.panels,
    };
    store.dispatch(
      metadataSuccess('Page', {
        widgets: {
          'visible-test': {
            src: 'HtmlWidget',
            html: {
              url: null,
              html: '<h1>Hello</h1>',
              fetchOnInit: false,
            },
          },
        },
      })
    );
    store.dispatch(
      dataSuccessWidget('visible-test', {
        widgets: {
          'visible-test': {
            src: 'HtmlWidget',
            html: {
              url: null,
              html: '<h1>Hello</h1>',
              fetchOnInit: false,
            },
          },
        },
      })
    );
    store.dispatch(hideWidget('visible-test'));
    const panels = [
      {
        icon: 'fa fa-plus',
        label: 'Первый таб',
        id: 'visible-test',
        opened: true,
        widgetId: 'visible-test',
        isVisible: false,
        dependency: {
          visible: [
            {
              bindLink: 'models.resolve',
              condition: false,
            },
          ],
        },
      },
    ];
    return (
      <React.Fragment>
        <div>Панель полностью скрыта</div>
        <PanelRegion {...props} panels={panels} pageId="Page" />
      </React.Fragment>
    );
  });
