import React from 'react';
import { FormattedMessage } from 'react-intl';

/**
 * Компонент отображения отсутствия данных
 * @returns {*}
 * @constructor
 */
function AdvancedTableEmptyText() {
  return (
    <span className="d-flex justify-content-center text-muted">
      <FormattedMessage id="table.notFound" defaultMessage="Нет данных для отображения" />
    </span>
  );
}

export default AdvancedTableEmptyText;
