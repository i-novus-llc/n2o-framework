import React from 'react';
import { pure } from 'recompose';
import PropTypes from 'prop-types';
import InputText from '../../controls/InputText/InputText';
import Button from 'reactstrap/lib/Button';

/**
 * Компонент overlay для фильтра
 * @param value - значение фильтра
 * @param onChange - callback на изменение
 * @param onResetFilter - callback на сброс фильтра
 * @param onSetFilter - callback на поиск
 * @param component - компонент контрол фильтра
 * @returns {*}
 * @constructor
 */
function AdvancedTableFilterPopup({
  value,
  onChange,
  onResetFilter,
  onSetFilter,
  component,
}) {
  const childProps = {
    value,
    onChange,
  };

  return (
    <React.Fragment>
      <div className="n2o-advanced-table-filter-dropdown-popup">
        {component ? (
          React.createElement(
            component,
            Object.assign({}, childProps, { popupPlacement: 'top-start' })
          )
        ) : (
          <InputText value={value} onChange={onChange} />
        )}
      </div>
      <div className="n2o-advanced-table-filter-dropdown-buttons">
        <Button color={'primary'} size={'sm'} onClick={onSetFilter}>
          Искать
        </Button>
        <Button size={'sm'} onClick={onResetFilter}>
          Сбросить
        </Button>
      </div>
    </React.Fragment>
  );
}

AdvancedTableFilterPopup.propTypes = {
  value: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
  onChange: PropTypes.func,
  onResetFilter: PropTypes.func,
  onSetFilter: PropTypes.func,
};

AdvancedTableFilterPopup.defaultProps = {
  onChange: () => {},
  onResetFilter: () => {},
  onSetFilter: () => {},
};

export { AdvancedTableFilterPopup };
export default pure(AdvancedTableFilterPopup);
