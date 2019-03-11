import React from 'react';
import { Button } from 'reactstrap';
import PropTypes from 'prop-types';

ListMoreButton.propTypes = {};

function ListMoreButton({ onClick }) {
  return (
    <div className="n2o-widget-list-more-button">
      <Button onClick={onClick}>Загрузить еще</Button>
    </div>
  );
}

export default ListMoreButton;
