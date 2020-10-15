import React from 'react';
import PropTypes from 'prop-types';
import ButtonGroup from 'reactstrap/lib/ButtonGroup';
import Button from 'reactstrap/lib/Button';

function Buttons({ visible, searchLabel, resetLabel, onSearch, onReset }) {
  return visible ? (
    <ButtonGroup>
      <Button color="primary" onClick={onSearch}>
        {searchLabel}
      </Button>
      <Button color="secondary" onClick={onReset}>
        {resetLabel}
      </Button>
    </ButtonGroup>
  ) : null;
}

Buttons.propTypes = {
  onSearch: PropTypes.func,
  onReset: PropTypes.func,
  searchLabel: PropTypes.string,
  resetLabel: PropTypes.string,
  visible: PropTypes.bool,
};

Buttons.defaultProps = {
  onSearch: () => {},
  onReset: () => {},
  visible: true,
};

export default Buttons;
