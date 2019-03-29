import React from 'react';
import PropTypes from 'prop-types';
import { Card, Collapse } from 'reactstrap';
import cx from 'classnames';

import panelStyles from './panelStyles';
import PanelHeading from './PanelHeading';
import PanelTitle from './PanelTitle';
import PanelMenu from './PanelMenu';
import PanelNavItem from './PanelNavItem';
import PanelFooter from './PanelFooter';
import PanelBody from './PanelBody';
import PanelTabBody from './PanelTabBody';

/**
 * Компонент панели
 * @reactProps {string} className - имя класса для родительского элемента
 * @reactProps {object} style - стили для родительского элемента
 * @reactProps {string} color - стиль для панели
 * @reactProps {boolean} isFullScreen - флаг режима на весь экран
 * @reactProps {function} onToggle - callback для скрытия/раскрытия панели
 * @reactProps {boolean} - open
 * @reactProps {function} onKeyPress - callback при нажатии на кнопку
 * @reactProps {node} children - вставляемый в панель элемент
 */

class Panel extends React.Component {
  /**
   * Рендер
   */

  render() {
    const {
      className,
      isFullScreen,
      style,
      onToggle,
      open,
      color,
      onKeyPress,
      children
    } = this.props;

    // const fontBg = [panelStyles.DEFAULT].includes(color) ? 'text-dark' : 'text-white';
    const panelClass = cx('n2o-panel-region', className, 'text-dark', {
      'panel-fullscreen': isFullScreen
    });

    return (
      <Card
        className={panelClass}
        style={style}
        onToggle={onToggle}
        expanded={open}
        color={color}
        outline
        onKeyDown={e => onKeyPress(e)}
        tabIndex="-1"
      >
        {children}
        <div className="panel-fullscreen-help">
          <span>ESC - выход из полноэкранного режима</span>
        </div>
      </Card>
    );
  }
}

Panel.propTypes = {
  className: PropTypes.string,
  style: PropTypes.object,
  color: PropTypes.string,
  isFullScreen: PropTypes.bool,
  open: PropTypes.bool,
  onToggle: PropTypes.func,
  onKeyPress: PropTypes.func,
  children: PropTypes.node
};

Panel.defaultProps = {
  isFullScreen: false,
  color: panelStyles.DEFAULT
};

Object.assign(Panel, {
  Heading: PanelHeading,
  Title: PanelTitle,
  Menu: PanelMenu,
  NavItem: PanelNavItem,
  Footer: PanelFooter,
  Body: PanelBody,
  TabBody: PanelTabBody,
  Collapse: Collapse
});

export default Panel;
