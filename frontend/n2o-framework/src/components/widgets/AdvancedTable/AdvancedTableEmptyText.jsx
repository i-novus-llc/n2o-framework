import React from 'react';

/**
 * Компонент отображения отсутствия данных
 * @returns {*}
 * @constructor
 */
function AdvancedTableEmptyText(t) {
  return (
    <span className="d-flex justify-content-center text-muted">
      {t('noData')}
    </span>
  );
}

export default AdvancedTableEmptyText;
