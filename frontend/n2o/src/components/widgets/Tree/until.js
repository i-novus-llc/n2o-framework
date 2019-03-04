import React from 'react';
import { forEach, keys, map, filter, eq } from 'lodash';
import { KEY_CODES } from './component/constants';
import { findDOMNode } from 'react-dom';
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
        title: React.createElement(Component, { data, ...rest, valueFieldId }),
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

export const FILTER_MODE = ['includes', 'startsWith', 'endsWith'];

export const takeKeysWhenSearching = ({
  filter,
  value,
  datasource,
  valueFieldId,
  labelFieldId
}) => {
  if (filter && FILTER_MODE.includes(filter) && value) {
    const filterFunc = item => String.prototype[filter].call(item, value);
    const expandedKeys = datasource
      .filter(item => filterFunc(item[labelFieldId]))
      .map(v => v[valueFieldId]);
    return expandedKeys;
  }
  return [];
};

//
export const getTreeLinerRoute = (data, expandedKeys, { parentFieldId, valueFieldId }) => {
  //берем всех родителей
  const parenIds = filter(data, dt => !dt[parentFieldId]).map(dt => dt[valueFieldId]);

  let buff = [];

  // рекурсивно спускаемся вниз ко всем потомкам
  // и если потомки есть в expandedKeys то добавляем в буфер
  const recursionFn = ids =>
    forEach(ids, id => {
      buff.push(id);

      if (expandedKeys.includes(id)) {
        const childs = filter(data, dt => {
          return dt[parentFieldId] === id;
        }).map(dt => dt[valueFieldId]);

        if (childs) {
          recursionFn(childs);
        }
      }
    });

  recursionFn(parenIds);
  return buff;
};

export const keyDownAction = ({
  key,
  treeRef,
  datasource,
  expandedKeys,
  prefixCls,
  valueFieldId,
  parentFieldId
}) => {
  const node = findDOMNode(treeRef.current);

  const route = getTreeLinerRoute(datasource, expandedKeys, { valueFieldId, parentFieldId });

  const focusedElement = document.activeElement;

  if (eq(key, KEY_CODES.KEY_DOWN)) {
    if (eq(focusedElement.className, 'hotkey')) {
      const child = node.querySelector(`.cls-${route[0]}`);
      child.focus();
    }
    if (focusedElement.dataset.id) {
      const id = focusedElement.dataset.id;
      const inx = route.indexOf(id);
      if (route.length > inx + 1) {
        const child = node.querySelector(`.cls-${route[inx + 1]}`);
        child.focus();
      }
    }
  }
  if (eq(key, KEY_CODES.KEY_UP)) {
    if (focusedElement.dataset.id) {
      const id = focusedElement.dataset.id;
      const inx = route.indexOf(id);
      if (inx - 1 >= 0) {
        const child = node.querySelector(`.cls-${route[inx - 1]}`);
        child.focus();
      }
    }
  }
  if (eq(key, KEY_CODES.ENTER)) {
    if (focusedElement.dataset.id) {
      const id = focusedElement.dataset.id;
      const child = node.querySelector(`.cls-${id}`);
      child.click();
    }
  }
  if (eq(key, KEY_CODES.KEY_SPACE)) {
    if (focusedElement.dataset.id) {
      const id = focusedElement.dataset.id;
      const child = node
        .querySelector(`.cls-${id}`)
        .closest('li')
        .querySelector(`.${prefixCls}-switcher`);
      child.click();
    }
  }
  if (eq(key, KEY_CODES.CTRL_ENTER)) {
    if (focusedElement.dataset.id) {
      const id = focusedElement.dataset.id;
      const child = node
        .querySelector(`.cls-${id}`)
        .closest('li')
        .querySelector(`.${prefixCls}-checkbox`);
      child && child.click();
    }
  }
  return false;
};
