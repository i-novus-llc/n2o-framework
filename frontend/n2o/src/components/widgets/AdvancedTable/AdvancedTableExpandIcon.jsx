import React from 'react';
import PropTypes from 'prop-types';
import cn from 'classnames';

/**
 * Компонент кнопки открытия подстроки
 * @param record - модель строки
 * @param expanded - флаг открытия
 * @param onExpand - callback на открытие
 * @returns {*}
 * @constructor
 */
function AdvancedTableExpandIcon({ record, expanded, onExpand }) {
  return (
    <span
      className={cn({ 'n2o-advanced-table-expand': record.children || record.expandedContent })}
      onClick={e => onExpand(record, e)}
    >
      {(record.expandedContent || record.children) && (
        <i
          className={cn('fa', 'n2o-advanced-table-expand-icon', {
            'n2o-advanced-table-expand-icon-expanded': expanded,
            'fa-angle-right': !expanded,
            'fa-angle-down': expanded
          })}
        />
      )}
    </span>
  );
}

AdvancedTableExpandIcon.propTypes = {
  record: PropTypes.object,
  expanded: PropTypes.bool,
  onExpand: PropTypes.func
};

AdvancedTableExpandIcon.defaultProps = {
  record: {}
};

export default AdvancedTableExpandIcon;
