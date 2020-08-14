import isUndefined from 'lodash/isUndefined';
import get from 'lodash/get';
import map from 'lodash/map';
import assign from 'lodash/assign';

export const hasLink = (item, linkFieldId) =>
  !isUndefined(get(item, linkFieldId));
export const lastItem = (data, index) => index === data.length - 1;

export const getHref = (item, linkFieldId) => get(item, linkFieldId);
export const getLabel = (item, labelFieldId) => get(item, labelFieldId);

export const valueWithSeparator = (value, labelFieldId, separator) =>
  map(value, (item, index) =>
    !lastItem(value, index)
      ? assign({}, item, {
          [labelFieldId]: item[labelFieldId] + separator,
        })
      : item
  );
