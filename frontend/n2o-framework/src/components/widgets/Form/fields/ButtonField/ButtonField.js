import React from 'react';
import cn from 'classnames';

import StandardButton from '../../../../buttons/StandardButton/StandardButton';

function ButtonField({ className, ...rest }) {
  return (
    <div className={cn('n2o-button-field', className)}>
      <StandardButton {...rest} />
    </div>
  );
}

export default ButtonField;
