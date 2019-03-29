import React from 'react';
import PropTypes from 'prop-types';

const Html = ({ value, visible, disabled }) => {
  const disabledStyle = {
    pointerEvents: 'none',
    opacity: '0.4'
  };
  const style = disabled ? disabledStyle : undefined;
  return <div style={style}>{visible && <div dangerouslySetInnerHTML={{ __html: value }} />}</div>;
};

Html.propTypes = {
  value: PropTypes.string,
  visible: PropTypes.bool,
  disabled: PropTypes.bool
};

Html.defaultProps = {
  visible: true,
  disabled: false
};

export default Html;
