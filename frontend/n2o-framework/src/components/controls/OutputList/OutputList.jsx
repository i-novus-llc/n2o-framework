import React from 'react';

import cn from 'classnames';

import withFetchData from '../withFetchData';

import { TextRow, TextColumn } from './utils';

function OutputList({
  data = [],
  className,
  labelFieldId = 'name',
  linkFieldId = 'href',
  target = '_blank',
  direction = 'column',
  separator = ' ',
  ...props
}) {
  const columnType = direction === 'column';
  const rowType = direction === 'row';

  return (
    <div className={cn('n2o-output-list', { [className]: className })}>
      {columnType ? (
        <TextColumn
          data={data}
          labelFieldId={labelFieldId}
          linkFieldId={linkFieldId}
          separator={separator}
          direction={direction}
          target={target}
          {...props}
        />
      ) : (
        <TextRow
          data={data}
          labelFieldId={labelFieldId}
          linkFieldId={linkFieldId}
          separator={separator}
          direction={direction}
          target={target}
          {...props}
        />
      )}
    </div>
  );
}

export default withFetchData(OutputList);
