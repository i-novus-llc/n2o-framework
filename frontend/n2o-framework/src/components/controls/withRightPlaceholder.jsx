import React from 'react';
import PropTypes from 'prop-types';

const withRightPlaceholder = WrappedComponent => {
  return function(props) {
    const { rightPlaceholder } = props;

    return (
      <div className="n2o-control-container">
        <WrappedComponent {...props} />
        {!!rightPlaceholder ? (
          <div className="n2o-control-container-placeholder">
            {rightPlaceholder}
          </div>
        ) : null}
      </div>
    );
  };
};

withRightPlaceholder.propTypes = {
  rightPlaceholder: PropTypes.string,
};

withRightPlaceholder.defaultProps = {
  rightPlaceholder: '',
};

export default withRightPlaceholder;
