import React from 'react';
import { Button } from 'reactstrap';
import PropTypes from 'prop-types';

ListMoreButton.propTypes = {
  onClick: PropTypes.func
};

ListMoreButton.defaultProps = {
  onClick: () => {}
};

/**
 * Кнопка "Загрузить еще"
 * @param {function} onClick - callback на клик по кнопке
 * @returns {*}
 * @constructor
 */
function ListMoreButton({ onClick }) {
  return (
    <div className="n2o-widget-list-more-button">
      <Button onClick={onClick}>Загрузить еще</Button>
    </div>
  );
}

export default ListMoreButton;
