import React from 'react';
import PropTypes from 'prop-types';
import BaseCollapse from 'rc-collapse';
import cx from 'classnames';
import Panel from './Panel';
import Icon from '../Icon/Icon';

const expandIcon = ({ isActive }) => (
  <div className="n2o-collapse-icon-wrapper">
    <Icon
      className={cx('n2o-collapse-icon', { isActive })}
      name="fa fa-angle-right"
    />
  </div>
);

/**
 * Компонент Collapse
 * @param {string | array} activeKey - активный ключ панели (При совпадении с ключами Panel происходит открытие последней)
 * @param {string | array} defaultActiveKey - активный ключ по умолчанию
 * @param {boolean} destroyInactivePanel - при закрытии панели удалить внутреннее содержимое.
 * @param {boolean} accordion - включить режим accordion (При открытии панели захлопнуть предыдущую панель)
 * @returns {*}
 * @constructor
 */

const Collapse = ({ className, ...rest }) => (
  <BaseCollapse
    className={cx('n2o-collapse', className)}
    expandIcon={expandIcon}
    {...rest}
  />
);

Collapse.propTypes = {
  activeKey: PropTypes.oneOfType(PropTypes.string, PropTypes.array),
  defaultActiveKey: PropTypes.oneOfType(PropTypes.string, PropTypes.array),
  destroyInactivePanel: PropTypes.bool,
  accordion: PropTypes.bool,
  children: PropTypes.node,
  className: PropTypes.string,
  onChange: PropTypes.func,
};

Collapse.defaultProps = {
  destroyInactivePanel: false,
  accordion: false,
};

export { Panel };
export default Collapse;
