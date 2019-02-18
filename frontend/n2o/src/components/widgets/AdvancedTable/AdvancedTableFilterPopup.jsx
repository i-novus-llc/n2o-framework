import React from 'react';
import PropTypes from 'prop-types';
import InputText from '../../controls/InputText/InputText';
import { Button } from 'reactstrap';

/**
 * Компонент overlay для фильтра
 * @param value - значение фильтра
 * @param onChange - callback на изменение
 * @param onResetFilter - callback на сброс фильтра
 * @param onSetFilter - callback на поиск
 * @returns {*}
 * @constructor
 */
function AdvancedTableFilterPopup({ value, onChange, onResetFilter, onSetFilter }) {
  return (
    <React.Fragment>
      <InputText value={value} onChange={onChange} />
      <Button color={'primary'} size={'sm'} onClick={onSetFilter}>
        Искать
      </Button>
      <Button size={'sm'} onClick={onResetFilter}>
        Сбросить
      </Button>
    </React.Fragment>
  );
}

AdvancedTableFilterPopup.propTypes = {
  value: PropTypes.string,
  onChange: PropTypes.func,
  onResetFilter: PropTypes.func,
  onSetFilter: PropTypes.func
};

AdvancedTableFilterPopup.defaultProps = {
  onChange: () => {},
  onResetFilter: () => {},
  onSetFilter: () => {}
};

export default AdvancedTableFilterPopup;
