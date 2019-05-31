import React from 'react';
import StandardField from '../StandardField/StandardField';

function RangeField({ beginControl, endControl, ...rest }) {
  return (
    <div className="n2o-range-field">
      <div className="n2o-range-field-start">
        <StandardField control={beginControl} {...rest} />
      </div>
      <div className="n2o-range-field-end">
        <StandardField control={endControl} {...rest} />
      </div>
    </div>
  );
}

export default RangeField;
