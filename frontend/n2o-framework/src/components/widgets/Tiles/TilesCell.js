import React from 'react';
import PropTypes from 'prop-types';
import omit from 'lodash/omit';
import get from 'lodash/get';
import Factory from '../../../core/factory/Factory';
import { CELLS } from '../../../core/factory/factoryLevels';
/**
 * Строка карточки
 * @reactProps {string} className - имя css класса
 * @reactProps {object} style - css стиль
 * @reactProps {object} model - Модель
 */
function TilesCell(props) {
  const { component, model, id } = props;
  const data = get(model, id);

  const getPassProps = () => {
    return omit(props, ['component', 'model']);
  };

  return (
    <Factory
      src={get(component, 'src')}
      level={CELLS}
      model={data}
      {...getPassProps()}
    />
  );
}

TilesCell.propTypes = {
  /* Default props */
  className: PropTypes.string,
  style: PropTypes.string,
  /* Specific props */
  model: PropTypes.object,
};

export default TilesCell;
