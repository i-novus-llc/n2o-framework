import React from 'react';
import { isString } from 'lodash';
import PropTypes from 'prop-types';
import { Panel as BasePanel } from 'rc-collapse';
import cx from 'classnames';

/**
 * Панель Collapse
 * @param {string} header - Заголовок панели
 * @param {string} type - тип отображения панели( 'default', 'line', 'divider' )
 * @param {boolean} showArrow - показать иконку
 * @param {object} openAnimation - обьект для изменения анимации открытия и закрытия панели
 * @param {boolean} disabled - сделать панель неактивным
 * @param {boolean} forceRender - Рендерить неактивные панели
 * @returns {*}
 * @constructor
 */
const Panel = ({ className, headerClass, header, type, children, ...rest }) => (
  <BasePanel
    header={
      <span
        tabIndex={1}
        title={isString(header) && header}
        className="n2o-panel-header-text"
      >
        {header}
      </span>
    }
    className={cx('n2o-collapse-panel', type, className)}
    headerClass={cx('n2o-panel-header', headerClass)}
    {...rest}
  >
    {children}
  </BasePanel>
);

Panel.propTypes = {
  header: PropTypes.oneOfType([PropTypes.string, PropTypes.node]),
  headerClass: PropTypes.string,
  showArrow: PropTypes.bool,
  className: PropTypes.string,
  style: PropTypes.object,
  openAnimation: PropTypes.object,
  disabled: PropTypes.bool,
  forceRender: PropTypes.bool,
  children: PropTypes.node,
  type: PropTypes.oneOf(['default', 'line', 'divider']),
};

Panel.defaultProps = {
  type: 'default',
};

export default Panel;
