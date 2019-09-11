import React from 'react';
import PropTypes from 'prop-types';
import BaseCollapse from 'rc-collapse';
import cx from 'classnames';
import Panel from './Panel';
import Icon from '../Icon/Icon';

import { map } from 'lodash';

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

const Collapse = ({ className, children, dataKey, ...rest }) => {
  const renderPanels = ({ text, ...panelProps }) => (
    <Panel {...panelProps}>{text}</Panel>
  );

  return (
    <BaseCollapse
      className={cx('n2o-collapse', className)}
      expandIcon={expandIcon}
      {...rest}
    >
      {children || map(rest[dataKey], renderPanels)}
    </BaseCollapse>
  );
};

Collapse.propTypes = {
  /**
   * Массив ключей открытых панелей
   */
  activeKey: PropTypes.oneOfType([PropTypes.string, PropTypes.array]),
  /**
   * Массив ключей открытых по дефолту панелей
   */
  defaultActiveKey: PropTypes.oneOfType([PropTypes.string, PropTypes.array]),
  destroyInactivePanel: PropTypes.bool,
  /**
   * Флаг включения режима 'Аккордион' (При открытии панели захлопнуть предыдущую панель)
   */
  accordion: PropTypes.bool,
  children: PropTypes.node,
  className: PropTypes.string,
  /**
   * Callback на открытие/закрытие панелей
   */
  onChange: PropTypes.func,
  /**
   * Ключ для рендера панелей
   */
  dataKey: PropTypes.string,
};

Collapse.defaultProps = {
  destroyInactivePanel: false,
  accordion: false,
  dataKey: 'items',
};

export { Panel };
export default Collapse;
