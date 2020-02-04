import React from 'react';
import PropTypes from 'prop-types';
import get from 'lodash/get';
import cx from 'classnames';

/**
 * Ячейка таблицы типа статус
 * @reactProps {object} model - модель данных
 * @reactProps {string} fieldKey - ключ модели для этой ячейки
 * @reactProps {string} color - цветовая схема бейджа(["primary", "secondary", "success", "danger", "warning", "info", "light", "dark", "white"])
 * @example
 * <StatusCell model={model} filedKey={'name'} color="info"/>
 */

function StatusCell(props) {
  const { color, model, fieldKey } = props;
  const statusText = get(model, fieldKey);
  return (
    !!color && (
      <>
        <span className={cx('n2o-status-cell', `bg-${color}`)} />
        {statusText}
      </>
    )
  );
}

StatusCell.propTypes = {
  /**
   * Ключ значения в данных
   */
  fieldKey: PropTypes.string,
  /**
   * Модель данных
   */
  model: PropTypes.object,
  /**
   * Цвет стаутуса
   */
  color: PropTypes.oneOf([
    'primary',
    'secondary',
    'success',
    'danger',
    'warning',
    'info',
    'light',
    'dark',
    'white',
  ]),
};

StatusCell.defaultProps = {
  model: {},
  color: '',
};

export default StatusCell;
