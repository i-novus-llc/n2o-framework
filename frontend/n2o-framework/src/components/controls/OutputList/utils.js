import isUndefined from 'lodash/isUndefined';
import get from 'lodash/get';

export const hasLink = (item, linkFieldId) =>
  !isUndefined(get(item, linkFieldId));
export const lastItem = (data, index) => index === data.length - 1;

export const getHref = (item, linkFieldId) => get(item, linkFieldId);
export const getLabel = (item, labelFieldId) => get(item, labelFieldId);
