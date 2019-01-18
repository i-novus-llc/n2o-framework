import React from 'react';
import PropTypes from 'prop-types';
import parseFormatter from '../../../../../utils/parseFormatter';
import cn from 'classnames';

function TextField({ text, visible, format, className, style }) {
  return (
    visible && (
      <div className={cn('n2o-text-field', { [className]: className })} style={style}>
        {format ? parseFormatter(text, format) : text}
      </div>
    )
  );
}

TextField.propTypes = {
  text: PropTypes.string,
  visible: PropTypes.bool,
  format: PropTypes.string,
  className: PropTypes.string,
  style: PropTypes.object
};

TextField.defaultProps = {
  visible: true
};

export default TextField;
