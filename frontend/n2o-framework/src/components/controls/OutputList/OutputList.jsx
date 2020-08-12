import React from 'react';

import map from 'lodash/map';
import get from 'lodash/get';
import isUndefined from 'lodash/isUndefined';

const hasLink = (item, linkFieldId) => !isUndefined(get(item, linkFieldId));
const lastItem = (data, index) => index === data.length - 1;

const getHref = (item, linkFieldId) => get(item, linkFieldId);
const getLabel = (item, labelFieldId) => get(item, labelFieldId);

function LabelWithSeparator({ label, lastItem, separator, data, index }) {
  return `${label} ${!lastItem(data, index) && separator}`;
}

function TextRow({ data, labelFieldId, linkFieldId, separator }) {
  return (
    <div>
      {map(data, (item, index) => {
        const href = getHref(item, linkFieldId);
        const label = getLabel(item, labelFieldId);

        return hasLink(item, linkFieldId) ? (
          <a href={href}>
            {
              <LabelWithSeparator
                label={label}
                lastItem={lastItem}
                separator={separator}
                data={data}
                index={index}
              />
            }
          </a>
        ) : (
          <LabelWithSeparator
            label={label}
            lastItem={lastItem}
            separator={separator}
            data={data}
            index={index}
          />
        );
      })}
    </div>
  );
}

function TextColumn({ data, labelFieldId, separator, linkFieldId }) {
  return (
    <ul>
      {map(data, item => (
        <li>{item.labelFieldId}</li>
      ))}
    </ul>
  );
}

function OutputList({
  data,
  className,
  labelFieldId,
  linkFieldId,
  target,
  direction = 'column',
  separator = ' ',
}) {
  const columnType = direction === 'column';
  const rowType = direction === 'row';

  return <>{columnType ? <TextColumn /> : <TextRow />}</>;
}

export default OutputList;
