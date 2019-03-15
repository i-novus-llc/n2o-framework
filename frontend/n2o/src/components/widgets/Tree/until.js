import React from 'react';
import {
  forEach,
  keys,
  map,
  filter,
  eq,
  omit,
  isFunction,
  get,
  has,
  uniqueId,
  find,
  some
} from 'lodash';
import { KEY_CODES } from './component/constants';
import { findDOMNode } from 'react-dom';
import cssAnimation from 'css-animation';

import Icon from '../../snippets/Icon/Icon';

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
  const iconFieldId = get(props, 'iconFieldId');

  if (valueFieldId && datasource) {
    datasource.forEach(data => {
      buf[data[valueFieldId]] = {
        ...data,
        icon: has(data, iconFieldId) && (
          <Icon key={uniqueId('tree_icon_')} name={data[iconFieldId]} />
        ),
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

///Key base fns

const down = (focusedElement, route, node) => {
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
};

const up = (focusedElement, route, node) => {
  if (focusedElement.dataset.id) {
    const id = focusedElement.dataset.id;
    const inx = route.indexOf(id);
    if (inx - 1 >= 0) {
      const child = node.querySelector(`.cls-${route[inx - 1]}`);
      child.focus();
    }
  }
};

const select = (focusedElement, node) => {
  if (focusedElement.dataset.id) {
    const id = focusedElement.dataset.id;
    const child = node.querySelector(`.cls-${id}`);
    child.click();
  }
};

const toggle = (focusedElement, node, { prefixCls }) => {
  if (focusedElement.dataset.id) {
    const id = focusedElement.dataset.id;
    const child = node
      .querySelector(`.cls-${id}`)
      .closest('li')
      .querySelector(`.${prefixCls}-switcher`);
    child.click();
  }
};

const checked = (focusedElement, node, { prefixCls }) => {
  if (focusedElement.dataset.id) {
    const id = focusedElement.dataset.id;
    const child = node
      .querySelector(`.cls-${id}`)
      .closest('li')
      .querySelector(`.${prefixCls}-checkbox`);
    child && child.click();
  }
};

/// end base fns

export const customTreeActions = ({
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

  const isParent = id => some(datasource, [parentFieldId, id]);

  const isRootParent = id => {
    const elem = find(datasource, [valueFieldId, id]);
    if (elem && !has(elem, parentFieldId) && !find(datasource, [parentFieldId, id])) {
      return true;
    }
    return false;
  };

  if (eq(key, KEY_CODES.KEY_DOWN)) {
    down(focusedElement, route, node);
  }
  if (eq(key, KEY_CODES.KEY_UP)) {
    up(focusedElement, route, node);
  }
  if (eq(key, KEY_CODES.KEY_SPACE)) {
    select(focusedElement, node);
  }
  if (eq(key, KEY_CODES.CTRL_ENTER)) {
    checked(focusedElement, node, { prefixCls });
  }
  if (eq(key, KEY_CODES.RIGHT)) {
    const id = focusedElement.dataset.id;
    if (!expandedKeys.includes(id) && isParent(id) && !isRootParent(id) && route.includes(id)) {
      toggle(focusedElement, node, { prefixCls });
    } else {
      down(focusedElement, route, node);
    }
  }
  if (eq(key, KEY_CODES.LEFT)) {
    const id = focusedElement.dataset.id;
    if (expandedKeys.includes(id) && isParent(id) && !isRootParent(id) && route.includes(id)) {
      toggle(focusedElement, node, { prefixCls });
    } else {
      up(focusedElement, route, node);
    }
  }
  if (eq(key, 'DB_CLICK')) {
    toggle(focusedElement, node, { prefixCls });
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

export const singleDoubleClickFilter = (singleCallback, doubleCallback, timeout) => {
  let timer = 0;

  return (...args) => {
    timer++;
    if (timer === 1) {
      setTimeout(() => {
        if (timer === 1) {
          singleCallback && singleCallback(...args);
        } else {
          doubleCallback && doubleCallback(...args);
        }
        timer = 0;
      }, timeout || 100);
    }
  };
};
