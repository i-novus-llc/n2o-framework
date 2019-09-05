import React from 'react';
import PropTypes from 'prop-types';

function AutoCompleteItem({ value, isHighlighted }) {
  return (
    <div style={{ background: isHighlighted ? 'lightgray' : 'white' }}>
      {value}
    </div>
  );
}

AutoCompleteItem.propTypes = {
  value: PropTypes.string,
  isHighlighted: PropTypes.bool,
};

export default AutoCompleteItem;
