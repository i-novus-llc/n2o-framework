import React from 'react';
import PropTypes from 'prop-types';
import cx from 'classnames';

import Icon from '../Icon/Icon';

/**
 * Компонент заголовка для {@link Panel}
 * @reactProps (string) icon - класс для иконки
 * @reactProps {boolean} collapsible - флаг возможности скрывать тело панели
 * @reactProps {function} onToggle - событие клика по тайтлу
 * @reactProps {node} children - элемент вставляемый в PanelTitle
 */

class PanelTitle extends React.Component {
  constructor(props) {
    super(props);

    this._handleToggle = this._handleToggle.bind(this);
  }

  /**
   * Рендер
   */

  _handleToggle() {
    if (this.props.collapsible) {
      this.props.onToggle();
    }
  }

  render() {
    const { icon, children, collapsible } = this.props;

    return (
      <a onClick={this._handleToggle} className={cx({ collapsible: collapsible })}>
        {icon && <Icon name={icon} style={{ padding: '0 5px 0 0' }} />}
        {children}
      </a>
    );
  }
}

PanelTitle.propTypes = {
  icon: PropTypes.string,
  collapsible: PropTypes.bool,
  onToggle: PropTypes.func,
  children: PropTypes.node
};

PanelTitle.defaultProps = {
  collapsible: false
};

export default PanelTitle;
