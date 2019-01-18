import React from 'react';
import ProptTypes from 'prop-types';
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
  text: ProptTypes.string,
  visible: ProptTypes.bool
};

export default TextField;
