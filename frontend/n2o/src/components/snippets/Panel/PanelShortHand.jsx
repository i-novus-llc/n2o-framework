import React from 'react';
import PropTypes from 'prop-types';
import { isEmpty } from 'lodash';
import cn from 'classnames';

import Panel from './Panel';
import panelStyles from './panelStyles';

/**
 * Shorthand для создания панели
 * @reactProps {array} tabs - массив с табами
 * @reactProps {array} toolbar - массив для тулбара
 * @reactProps {string} className - имя класса для родительского элемента
 * @reactProps {object} style - стили для родительского элемента
 * @reactProps {string} color - стиль для панели
 * @reactProps {string} icon - класс для иконки
 * @reactProps {string} headerTitle - заголовок для шапки
 * @reactProps {string} footerTitle - заголовок для футера
 * @reactProps {boolean} collapsible - флаг возможности скрывать содержимое панели
 * @reactProps {boolean} open - флаг открытости панели
 * @reactProps {boolean} hasTabs - флаг наличия табов
 * @reactProps {boolean} fullScreen - флаг возможности открывать на полный экран
 * @reactProps {node} children - элемент вставляемый в PanelContainer
 * @reactProps {boolean} - флаг показа заголовка
 * @example <caption>Структура tabs</caption>
 * {
 *  id - id таба
 *  header - содержимое нава
 *  content - содержимое таба
 * }
 */

class PanelContainer extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      isFullScreen: false,
      activeTab: this.props.tabs.length > 0 ? this.props.tabs[0].id : null,
      open: this.props.open,
    };

    this.handleFullScreen = this.handleFullScreen.bind(this);
    this.changeActiveTab = this.changeActiveTab.bind(this);
    this.toggleCollapse = this.toggleCollapse.bind(this);
    this.handleKeyPress = this.handleKeyPress.bind(this);
  }

  /**
   * Обработка нажатия на кнопку полного экрана
   */

  handleFullScreen() {
    this.setState(prevState => ({
      isFullScreen: !prevState.isFullScreen,
    }));
  }

  /**
   * Смена активного таба
   * @param id {string|number} - key для таба
   */

  changeActiveTab(id) {
    this.setState({
      activeTab: id,
    });
  }

  /**
   * Обработка переключения состояние коллапса
   */

  toggleCollapse() {
    this.setState({
      open: !this.state.open,
    });
  }

  /**
   * Обработка нажатия на клавишу для выхода из режима полного экрана
   * @param event
   */

  handleKeyPress(event) {
    if (event.key === 'Escape') {
      this.setState({
        isFullScreen: false,
      });
    }
  }

  componentWillReceiveProps(nextProps) {
    if (nextProps.open !== this.state.open) {
      this.setState({
        open: nextProps.open,
      });
    }
  }

  /**
   * Рендер
   */

  render() {
    const {
      tabs,
      toolbar,
      className,
      style,
      color,
      icon,
      headerTitle,
      footerTitle,
      collapsible,
      hasTabs,
      fullScreen,
      header,
    } = this.props;

    const fullScreenIcon = this.state.isFullScreen ? 'compress' : 'expand';
    return (
      <Panel
        color={color}
        style={style}
        className={cn(className, { 'n2o-panel-region--tabs': hasTabs })}
        open={this.state.open}
        isFullScreen={this.state.isFullScreen}
        onKeyPress={this.handleKeyPress}
      >
        {header && (
          <Panel.Heading>
            <Panel.Title
              collapsible={collapsible}
              icon={icon}
              onToggle={this.toggleCollapse}
            >
              {headerTitle}
            </Panel.Title>
            <Panel.Menu
              fullScreen={fullScreen}
              onFullScreenClick={this.handleFullScreen}
              fullScreenIcon={fullScreenIcon}
            >
              {hasTabs &&
                tabs.map((tab, i) => {
                  const activeTab = this.state.activeTab
                    ? this.state.activeTab === tab.id
                    : i === 0;
                  return (
                    <Panel.NavItem
                      id={tab.id}
                      active={activeTab}
                      disabled={tab.disabled}
                      className={tab.className}
                      style={tab.style}
                      onClick={() => this.changeActiveTab(tab.id)}
                    >
                      {tab.header}
                    </Panel.NavItem>
                  );
                })}
              {toolbar &&
                toolbar.map(item => (
                  <Panel.NavItem
                    id={item.id}
                    disabled={item.disabled}
                    className={item.className}
                    style={item.style}
                    onClick={item.onClick}
                    isToolBar={true}
                  >
                    {item.header}
                  </Panel.NavItem>
                ))}
            </Panel.Menu>
          </Panel.Heading>
        )}
        <Panel.Collapse
          className={cn({ 'd-flex flex-column': this.state.open })}
          isOpen={this.state.open}
        >
          <Panel.Body hasTabs={hasTabs} activeKey={this.state.activeTab}>
            {hasTabs
              ? tabs.map(tab => {
                  return (
                    <Panel.TabBody eventKey={tab.id}>
                      {tab.content}
                    </Panel.TabBody>
                  );
                })
              : this.props.children}
          </Panel.Body>
          {footerTitle && <Panel.Footer>{footerTitle}</Panel.Footer>}
        </Panel.Collapse>
      </Panel>
    );
  }
}

PanelContainer.propTypes = {
  tabs: PropTypes.array,
  toolbar: PropTypes.array,
  className: PropTypes.string,
  style: PropTypes.object,
  color: PropTypes.oneOf([Object.values(panelStyles)]),
  icon: PropTypes.string,
  headerTitle: PropTypes.string,
  footerTitle: PropTypes.string,
  open: PropTypes.bool,
  collapsible: PropTypes.bool,
  hasTabs: PropTypes.bool,
  fullScreen: PropTypes.bool,
  children: PropTypes.node,
  header: PropTypes.bool,
};

PanelContainer.defaultProps = {
  open: true,
  collapsible: false,
  hasTabs: false,
  fullScreen: false,
  tabs: [],
  color: panelStyles.DEFAULT,
  header: true,
};

export default PanelContainer;
