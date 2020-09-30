import React from 'react';
import { getContext } from 'recompose';
import PropTypes from 'prop-types';

/**
 * Компонент отображения отсутствия данных
 * @returns {*}
 * @constructor
 */
function AdvancedTableEmptyText({ t }) {
  return (
    <span className="d-flex justify-content-center text-muted">
      {t('noData')}
    </span>
  );
}

export default getContext({ t: PropTypes.func })(AdvancedTableEmptyText);
