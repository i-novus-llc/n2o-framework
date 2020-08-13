import React from 'react';

import isUndefined from 'lodash/isUndefined';
import get from 'lodash/get';
import map from 'lodash/map';

const hasLink = (item, linkFieldId) => !isUndefined(get(item, linkFieldId));
const lastItem = (data, index) => index === data.length - 1;

const getHref = (item, linkFieldId) => get(item, linkFieldId);
const getLabel = (item, labelFieldId) => get(item, labelFieldId);

function LabelWithSeparator({ label, lastItem, separator, data, index }) {
  return `${label}${!lastItem(data, index) ? separator + ' ' : ''}`;
}

function Link({ href, label, separator, data, index, target }) {
  return (
    <a className="n2o-output-list__link" href={href} target={target}>
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
  );
}

export function TextMapping({
  data,
  linkFieldId,
  labelFieldId,
  separator,
  direction,
  target,
}) {
  const columnType = direction === 'column';
  const rowType = direction === 'row';

  return (
    <>
      {map(data, (item, index) => {
        const href = getHref(item, linkFieldId);
        const label = getLabel(item, labelFieldId);

        const rowAndLink = hasLink(item, linkFieldId) && rowType;
        const columnAndLink = hasLink(item, linkFieldId) && columnType;

        const RenderLink = () => (
          <Link
            href={href}
            label={label}
            separator={separator}
            data={data}
            index={index}
            target={target}
          />
        );

        const RenderSimpleText = () => (
          <LabelWithSeparator
            label={label}
            lastItem={lastItem}
            separator={separator}
            data={data}
            index={index}
          />
        );

        return rowAndLink ? (
          <RenderLink />
        ) : columnAndLink ? (
          <li>
            <RenderLink />
          </li>
        ) : rowType ? (
          <RenderSimpleText />
        ) : columnType ? (
          <li>
            <RenderSimpleText />
          </li>
        ) : null;
      })}
    </>
  );
}

export function TextRow(props) {
  return (
    <div>
      <TextMapping {...props} />
    </div>
  );
}

export function TextColumn(props) {
  return (
    <ul className="n2o-output-list__items-list">
      <TextMapping {...props} />
    </ul>
  );
}
