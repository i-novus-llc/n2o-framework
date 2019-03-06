import React from 'react';
import { Button } from 'reactstrap';
import InlineSpinner from '../../snippets/Spinner/InlineSpinner';
import PropTypes from 'prop-types';

ListMoreButton.propTypes = {};

function ListMoreButton({ onClick, isLoading }) {
  return (
    <div className="n2o-widget-list-more-button">
      <Button disabled={isLoading} onClick={onClick}>
        {isLoading ? <InlineSpinner /> : 'Загрузить еще'}
      </Button>
    </div>
  );
}

export default ListMoreButton;
