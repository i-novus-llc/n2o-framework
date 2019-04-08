import React from 'react';
import { storiesOf } from '@storybook/react';
import { withKnobs, boolean, text } from '@storybook/addon-knobs/react';
import withTests from 'N2oStorybook/withTests';
import PanelsWithDependency from 'N2oStorybook/json/PanelsWithDependency';
import { set, pullAt } from 'lodash';

import PanelRegion from './PanelRegion';
import PanelShortHand from '../../snippets/Panel/PanelShortHand';
import Wireframe from '../../snippets/Wireframe/Wireframe';

import { metadataSuccess } from '../../../actions/pages';
import HtmlWidgetJson from '../../widgets/Html/HtmlWidget.meta';
import ListMetadata from '../List/ListMetadata.meta';
import SecurePanelRegion from './PanelRegion.meta';
import AuthButtonContainer from '../../../core/auth/AuthLogin';
import { makeStore } from '../../../../.storybook/decorators/utils';
import cloneObject from '../../../utils/cloneObject';
import panelStyles from '../../snippets/Panel/panelStyles';
import CheckboxN2O from '../../controls/Checkbox/CheckboxN2O';

const stories = storiesOf('Регионы/Панель', module);

stories.addDecorator(withKnobs);
stories.addDecorator(withTests('PanelRegion'));

const PanelRegionJson = set(
  cloneObject(SecurePanelRegion),
  'panels',
  pullAt(cloneObject(SecurePanelRegion).panels, 0)
);
const defaultProps = { panels: PanelRegionJson.panels.slice(0, 1) };
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
      { ...defaultProps, headerTitle: 'С вкладками', hasTabs: true },
      { ...defaultProps, headerTitle: 'Без вкладок', hasTabs: false },
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
  .add('Зависимости в панелях', () => {
    class PanelStory extends React.Component {
      constructor(props) {
        super(props);
        this.state = { show: true };
        this.onChange = this.onChange.bind(this);
      }
      onChange() {
        this.setState({ show: !this.state.show });
      }
      render() {
        const { show } = this.state;
        const dependency = {
          visible: [
            {
              on: "models.resolve['Page_First']",
              condition: 'true',
            },
            {
              on: "models.resolve['Page_First']",
              condition: show ? 'true' : 'false',
            },
          ],
        };
        return (
          <div className="row">
            <div className="col-md-6">
              <CheckboxN2O
                checked={show}
                onChange={this.onChange}
                inline={true}
                label={'Показать/Скрыть панель по зависимости'}
              />
              <PanelRegion
                {...PanelsWithDependency}
                dependency={dependency}
                pageId="Page"
              />
            </div>
          </div>
        );
      }
    }

    return <PanelStory />;
  });
