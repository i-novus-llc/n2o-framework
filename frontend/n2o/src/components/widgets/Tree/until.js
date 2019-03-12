import React from 'react';
import { forEach, keys, map, filter, eq, omit, isFunction, get } from 'lodash';
import { KEY_CODES } from './component/constants';
import { findDOMNode } from 'react-dom';
import cssAnimation from 'css-animation';

/**
 * Создаем коллекцию из дерева tree -> [{ id: ..., parentId: ... }, ...]
 * @param tree
 * @param parentFieldId
 * @param valueFieldId
 */
export const treeToCollection = (tree, { parentFieldId, valueFieldId, childrenFieldId }) => {
  let buf = [...tree];

  buf.forEach(el => {
    if (el[childrenFieldId]) {
      const elems = el[childrenFieldId].map(v => ({ ...v, [parentFieldId]: el[valueFieldId] }));
      buf.push(...elems);
    } else {
      buf.push(...el);
    }
  });

  return buf.map(v => omit(v, [childrenFieldId]));
};
/**
 * Превращаем коллекцию в обьект с ключами id и value Element
 * [{ id: 1, ...}, { id: 2, ... }] => { 1: {...}, 2: {...} }
 */
export const collectionToComponentObject = (Component, props) => {
  let buf = {};
  const valueFieldId = get(props, 'valueFieldId');
  const datasource = get(props, 'datasource');

  if (valueFieldId && datasource) {
    datasource.forEach(data => {
      buf[data[valueFieldId]] = {
        ...data,
        key: data[valueFieldId],
        title: React.createElement(Component, { data, ...props }),
        children: []
      };
    });
  }
  return buf;
};

export const createTreeFn = Component => props => {
  const itemsByID = collectionToComponentObject(Component, props);

  const parentFieldId = get(props, 'parentFieldId');

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

export const takeKeysWhenSearching = props => {
  const filter = get(props, 'filter');
  const value = get(props, 'value');
  const datasource = get(props, 'datasource', []);
  const valueFieldId = get(props, 'valueFieldId');
  const labelFieldId = get(props, 'labelFieldId');

  if (filter && FILTER_MODE.includes(filter) && value) {
    const filterFunc = item => String.prototype[filter].call(item, value);
    const expandedKeys = datasource
      .filter(item => filterFunc(item[labelFieldId]))
      .map(v => v[valueFieldId]);
    return expandedKeys;
  }
  return [];
};

/**
 * Вспомогогательная функция для клавиатуры
 * Определяет путь по которому будет двигаться клавиатура
 * возвращает массив из id
 * @param data
 * @param expandedKeys - открытые ключи
 * @param parentFieldId
 * @param valueFieldId
 * @returns {Array}
 */
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
  parentFieldId,
  hasCheckboxes
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

export const splitSearchText = (text, searchText) => {
  const html = text.replace(searchText, `<span class='search-text'>${searchText}</span>`);
  return <span dangerouslySetInnerHTML={{ __html: html }} />;
};

const animate = (node, show, done) => {
  let height = node.offsetHeight;
  return cssAnimation(node, 'collapse', {
    start() {
      if (!show) {
        node.style.height = `${node.offsetHeight}px`;
      } else {
        height = node.offsetHeight;
        node.style.height = 0;
      }
    },
    active() {
      node.style.height = `${show ? height : 0}px`;
    },
    end() {
      node.style.height = '';
      done();
    }
  });
};

export const animationTree = {
  enter(node, done) {
    return animate(node, true, done);
  },
  leave(node, done) {
    return animate(node, false, done);
  }
};
