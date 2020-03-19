import React from 'react';
import PropTypes from 'prop-types';
import every from 'lodash/every';
import { compose, setDisplayName } from 'recompose';
import PanelShortHand from '../../snippets/Panel/PanelShortHand';
import { WIDGETS } from '../../../core/factory/factoryLevels';
import Factory from '../../../core/factory/Factory';
import withRegionContainer from '../withRegionContainer';
import withWidgetProps from '../withWidgetProps';
import withSecurity from '../../../core/auth/withSecurity';
import { SECURITY_CHECK } from '../../../core/auth/authTypes';

/**
 * Регион Панель
 * @reactProps containers {array} - массив из объектов, которые описывают виджет {id, name, opened, pageId, fetchOnInit, widget}
 * @reactProps className (string) - имя класса для родительского элементаs
 * @reactProps style (object) - стили для родительского элемента
 * @reactProps color (string) - стиль для панели
 * @reactProps icon (string) - класс для иконки
 * @reactProps headerTitle (string) - заголовок для шапки
 * @reactProps footerTitle (string) - заголовок для футера
 * @reactProps collapsible (boolean) - флаг возможности скрывать содержимое панели
 * @reactProps open (boolean) - флаг открытости панели
 * @reactProps hasTabs (boolean) - флаг наличия табов
 * @reactProps fullScreen (boolean) - флаг возможности открывать на полный экран
 * @reactProps {array} panels - массив панелей вида
 * @reactProps {string} pageId - идентификатор страницы
 * @reactProps {function} getWidget - функция получения виджета
 * @reactProps {object} user - пользователь !!! не используется
 * @reactProps {function} authProvider - провайдер аутентификации !!! не используется
 * @reactProps {function} resolveVisibleDependency - резол видимости региона
 * @reactProps {object} dependency - зависимость видимости панели
 */

class PanelRegion extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      tabs: [],
    };
    this.checkPanel = this.checkPanel.bind(this);
    this.getTab = this.getTab.bind(this);
  }

  componentWillReceiveProps() {
    this.getPanelsWithAccess();
  }

  componentDidMount() {
    this.getPanelsWithAccess();
  }

  getContent(panel) {
    const { getWidget, pageId } = this.props;
    return (
      <Factory
        level={WIDGETS}
        id={panel.widgetId}
        key={panel.widgetId}
        {...getWidget(pageId, panel.widgetId)}
        pageId={pageId}
      />
    );
  }

  getTab(panel) {
    const { getWidget, pageId } = this.props;

    return {
      id: panel.widgetId,
      content: this.getContent(panel),
      header: panel.label,
      ...panel,
      ...getWidget(pageId, panel.widgetId),
    };
  }

  async checkPanel(panel) {
    if (panel.security) {
      const { user, authProvider } = this.props;
      const config = panel.security;
      try {
        const permissions = await authProvider(SECURITY_CHECK, {
          config,
          user,
        });
        this.setState({ tabs: this.state.tabs.concat(this.getTab(panel)) });
      } catch (error) {
        //...
      }
    } else {
      this.setState({ tabs: this.state.tabs.concat(this.getTab(panel)) });
    }
  }

  getPanelsWithAccess() {
    const { authProvider, user, panels } = this.props;
    this.setState({ tabs: [] }, async () => {
      for (const panel of panels) {
        await this.checkPanel(panel);
      }
    });
  }

  /**
   * Рендер
   */
  render() {
    const {
      panels,
      getWidgetProps,
      activeEntity,
      changeActiveEntity,
    } = this.props;
    const isInvisible = every(
      panels,
      item => getWidgetProps(item.widgetId).isVisible === false
    );

    return (
      <PanelShortHand
        tabs={this.state.tabs}
        {...this.props}
        open={activeEntity}
        style={{ display: isInvisible && 'none' }}
        onVisibilityChange={changeActiveEntity}
      >
        {panels.map(container => this.getContent(container))}
      </PanelShortHand>
    );
  }
}

PanelRegion.propTypes = {
  /**
   * Список элементов
   */
  panels: PropTypes.array.isRequired,
  /**
   * ID страницы
   */
  pageId: PropTypes.string.isRequired,
  /**
   * Класс
   */
  className: PropTypes.string,
  /**
   * Стили
   */
  style: PropTypes.object,
  /**
   * Цвет панели
   */
  color: PropTypes.string,
  /***
   * Иконка панели
   */
  icon: PropTypes.string,
  /**
   * Текст заголовка
   */
  headerTitle: PropTypes.string,
  /**
   * Текст футера
   */
  footerTitle: PropTypes.string,
  /**
   * Флаг открытия панели
   */
  open: PropTypes.bool,
  /**
   * Флаг возможности скрывать содержимое панели
   */
  collapsible: PropTypes.bool,
  /**
   * Флаг наличия табов
   */
  hasTabs: PropTypes.bool,
  /**
   * Флаг открытия на весь экран
   */
  fullScreen: PropTypes.bool,
  getWidget: PropTypes.func.isRequired,
  resolveVisibleDependency: PropTypes.func,
  dependency: PropTypes.object,
};

PanelRegion.defaultProps = {
  open: true,
  collapsible: false,
  hasTabs: false,
  fullScreen: false,
};

export { PanelRegion };
export default compose(
  setDisplayName('PanelRegion'),
  withRegionContainer({ listKey: 'panels' }),
  withSecurity,
  withWidgetProps
)(PanelRegion);
