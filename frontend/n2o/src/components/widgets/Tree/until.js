import React from 'react';
import { keys } from 'lodash';

/**
 * Превращаем коллекцию в обьект с ключами id и value Element
 * [{ id: 1, ...}, { id: 2, ... }] => { 1: {...}, 2: {...} }
 */
export const collectionToComponentObject = (
  Component,
  { valueFieldId, parentFieldId, datasource, ...rest }
) => {
  let buf = {};
  // forEach быстрее reduce и for in
  if (valueFieldId && datasource) {
    datasource.forEach(data => {
      buf[data[valueFieldId]] = {
        ...data,
        key: data[valueFieldId],
        title: React.createElement(Component, { data, ...rest }),
        children: []
      };
    });
  }
  return buf;
};

export const createTreeFn = Component => props => {
  const itemsByID = collectionToComponentObject(Component, props);

  const { parentFieldId } = props;

  keys(itemsByID).forEach(key => {
    const elem = itemsByID[key];
    if (elem[parentFieldId] && itemsByID[elem[parentFieldId]]) {
      itemsByID[elem[parentFieldId]].children.push({ ...elem });
    }
  });

  let buf = [];

  keys(itemsByID).forEach(key => {
    if (!itemsByID[key][parentFieldId]) {
      buf.push(itemsByID[key]);
    }
  });

  return buf;
};

export const takeKeysWhenSearching = ({
  filter,
  value,
  datasource,
  valueFieldId,
  labelFieldId
}) => {
  if (filter && ['includes', 'startsWith', 'endsWith'].includes(filter) && value) {
    const filterFunc = item => String.prototype[filter].call(item, value);
    const expandedKeys = datasource
      .filter(item => filterFunc(item[labelFieldId]))
      .map(v => v[valueFieldId]);
    return expandedKeys;
  }
  return [];
};
