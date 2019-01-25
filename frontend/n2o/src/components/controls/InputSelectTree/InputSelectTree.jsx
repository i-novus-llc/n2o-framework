import React from 'react';
import TreeSelect, { SHOW_ALL, SHOW_CHILD, SHOW_PARENT } from 'rc-tree-select';
import {
  difference,
  filter as filterF,
  find,
  isArray,
  isEmpty,
  keys,
  map,
  some,
  uniq,
  uniqBy,
  unionWith,
  isEqual
} from 'lodash';
import Icon from '../../snippets/Icon/Icon';
import InlineSpinner from '../../snippets/Spinner/InlineSpinner';
import CheckboxN2O from '../Checkbox/CheckboxN2O';
import { defaultProps, propTypes } from './allProps';
import { compose, withState } from 'recompose';
import propsResolver from '../../../utils/propsResolver';
import { visiblePartPopup } from './until';
import TreeNode from './TreeSelectNode';
import { injectIntl } from 'react-intl';
import cx from 'classnames';

/**
 * @param onOpen - callback функция вызываемая при открытии popup
 * @param {function} onFocus
 * @param value - выбранное значение
 * @reactProps {function} onBlur
 * @reactProps {any} searchPlaceholder
 * @reactProps {string} placeholder
 * @reactProps {function} setTreeExpandedKeys
 * @reactProps {array} treeExpandedKeys
 * @reactProps {function} closePopupOnSelect
 * @reactProps {node} children
 * @reactProps {function} closePopupOnSelect
 * @param loading -  флаг анимации загрузки
 * @param parentFieldId - значение ключа parent в данных
 * @param valueFieldId - значение ключа value в данных
 * @param labelFieldId - значение ключа label в данных
 * @param iconFieldId - значение ключа icon в данных
 * @param badgeFieldId - значение ключа badge в данных
 * @param badgeColorFieldId - значение ключа badgeColor в данных
 * @param hasChildrenFieldId - значение ключа hasChildren в данных
 * @param format - формат
 * @param data - данные для построения дерева
 * @param onSearch - callback функция вызываемая поиске
 * @param onSelect - callback функция вызываемая выборе элемента дерева
 * @param onChange - callback функция вызываемая изменении элемента дерева
 * @param hasCheckboxes - флаг для показа чекбоксов в элементах дерева. Переводит InputSelectTree в мульти режим
 * @param filter - варианты фильтрации
 * @param multiSelect - флаг для перевода InputSelectTree в мульти режим
 * @param onClose - callback функция вызываемая при закрытии popup
 * @param onToggle - callback функция вызываемая при открытии/закрытии popup
 * @param disabled - флаг неактивности
 * @param _handleItemOpen - callback функция вызываемая ajax true. Передает value открывающего элемента дерева
 * @param ajax - флаг динамичексой подгрузки данных. В данных обязательно указывать параметр hasChildrens
 * @param notFoundContent - текст если поиск не выдал результатов
 * @returns {*}
 * @constructor
 */

function InputSelectTree({
  onOpen,
  onFocus,
  value,
  onBlur,
  searchPlaceholder,
  placeholder,
  setTreeExpandedKeys,
  notFoundContent,
  treeExpandedKeys,
  closePopupOnSelect,
  loading,
  parentFieldId,
  valueFieldId,
  labelFieldId,
  iconFieldId,
  imageFieldId,
  badgeFieldId,
  badgeColorFieldId,
  hasChildrenFieldId,
  format,
  data,
  onSearch,
  onSelect,
  onChange,
  hasCheckboxes,
  filter,
  multiSelect,
  children,
  onClose,
  onToggle,
  handleItemOpen,
  ajax,
  className,
  intl,
  dropdownPopupAlign,
  ref,
  ...rest
}) {
  const popupProps = {
    prefixCls: 'n2o-select-tree',
    iconFieldId,
    imageFieldId,
    labelFieldId,
    badgeFieldId,
    badgeColorFieldId
  };

  /**
   * Функуия для создания дерева.
   * Преобразует коллекцию вида [..., { ... }] в [ ..., {..., children: [...]}]
   * Вложение происходит при совпадении valueFieldId и parentFieldId.
   * @param items
   * @returns {*}
   */
  const createTree = items => {
    let itemsByID = [...items].reduce(
      (acc, item) => ({
        ...acc,
        [item[valueFieldId]]: {
          ...item,
          key: item[valueFieldId],
          value: item[valueFieldId],
          title: format
            ? propsResolver({ format }, item).format
            : visiblePartPopup(item, popupProps),
          ...(ajax && { isLeaf: !item[hasChildrenFieldId] }),
          children: []
        }
      }),
      {}
    );

    keys(itemsByID).forEach(key => {
      if (itemsByID[key][parentFieldId])
        itemsByID[itemsByID[key][parentFieldId]].children.push({ ...itemsByID[key] });
    });

    return keys(itemsByID)
      .filter(key => !itemsByID[key][parentFieldId])
      .reduce((acc, key) => [...acc, { ...itemsByID[key] }], []);
  };

  /**
   * Если нет data но есть value
   * строим дерево из value иначе будет неправильное отображение
   * @param items
   * @returns {*}
   */
  const setData = items => {
    const newValue = isArray(value) ? value : [value];
    if (isEmpty(items) && !isEmpty(value)) {
      return createTree(newValue);
    }
    return createTree(items);
  };

  /**
   * Функция для поиска.
   * При поиске вызов функции происходит для каждого элемента дерева.
   * @param input
   * @param node
   * @returns {*}
   */
  const handlerFilter = (input, node) => {
    const mode = ['includes', 'startsWith', 'endsWith'];

    if (mode.includes(filter)) {
      return String.prototype[filter].call(node.props[labelFieldId], input);
    }
    return true;
  };

  /**
   * Функция преобразования value rcTreeSelect в формат n2o
   * ['id', 'id'] => [{ id: 'id', ... },{ id: 'id', ... }]
   * @param value
   * @returns {*}
   */
  const getItemByValue = value => {
    if (!value) return null;
    if (isArray(value)) {
      return filterF(data, node => some(value, v => v === node[valueFieldId]));
    }
    return find(data, [valueFieldId, value]);
  };

  /**
   * Функция для обратного преобразования value n2o в формат rcTreeSelect
   * ['id', 'id'] => [{ id: 'id', ... },{ id: 'id', ... }]
   * @param value
   * @returns {*}
   */
  const setValue = value => {
    if (!value) return null;
    if (isArray(value)) {
      return map(value, v => v[valueFieldId]);
    }
    return value[valueFieldId];
  };

  /**
   * Функция для переопределения onChange
   * @param value
   */
  const handleChange = value => {
    onChange(getItemByValue(value));
  };

  /**
   * Функция для переопределения onSelect
   * @param value
   */
  const handleSelect = value => {
    onSelect(getItemByValue(value));
  };

  /**
   * Функция для переопределения onSearch
   * @param value
   */
  const handleSearch = value => {
    onSearch(value);
    return true;
  };

  /**
   * Функция для контроля открытия/закрытия popup
   * @param visible
   * @returns {boolean}
   */
  const handleDropdownVisibleChange = visible => {
    onToggle(visible);
    visible ? onOpen() : onClose();
    if (ajax) setTreeExpandedKeys([]);
    return true;
  };

  /**
   * Функция для контроля открытия/закрытия элемента дерева
   * @param keys
   */
  const onTreeExpand = async keys => {
    const currentKey = difference(keys, treeExpandedKeys);
    if (ajax) {
      await handleItemOpen(currentKey[0]);
    }
    setTreeExpandedKeys(keys);
  };

  const renderSwitcherIcon = ({ isLeaf }) => (isLeaf ? null : <Icon name="fa fa-chevron-right" />);

  const clearIcon = <Icon name="fa fa-times" />;

  const inputIcon = loading ? <InlineSpinner /> : <Icon name="fa fa-chevron-down" />;

  return (
    <TreeSelect
      tabIndex={-1}
      {...value && { value: setValue(value) }}
      onDropdownVisibleChange={handleDropdownVisibleChange}
      className={cx('n2o', className)}
      switcherIcon={renderSwitcherIcon}
      inputIcon={inputIcon}
      multiple={multiSelect}
      treeCheckable={hasCheckboxes && <CheckboxN2O inline />}
      treeData={setData(data)}
      filterTreeNode={handlerFilter}
      treeNodeFilterProp={labelFieldId}
      removeIcon={clearIcon}
      clearIcon={clearIcon}
      onChange={handleChange}
      onSelect={handleSelect}
      onSearch={handleSearch}
      treeExpandedKeys={treeExpandedKeys}
      onTreeExpand={onTreeExpand}
      dropdownPopupAlign={dropdownPopupAlign}
      prefixCls="n2o-select-tree"
      notFoundContent={intl.formatMessage({
        id: 'inputSelectTree.notFoundContent',
        defaultMessage: notFoundContent || ' '
      })}
      placeholder={intl.formatMessage({
        id: 'inputSelectTree.placeholder',
        defaultMessage: placeholder || ' '
      })}
      searchPlaceholder={intl.formatMessage({
        id: 'inputSelectTree.searchPlaceholder',
        defaultMessage: searchPlaceholder || ' '
      })}
      ref={e => {
        if (e) {
          ref && ref(e);
          e.onSelectorBlur = e => {
            onBlur(e);
            return true;
          };
          e.onSelectorFocus = e => {
            onFocus(e);
            return true;
          };
        }
      }}
      {...rest}
    >
      {children}
    </TreeSelect>
  );
}

InputSelectTree.defaultProps = defaultProps;
InputSelectTree.propTypes = propTypes;

export { SHOW_ALL, SHOW_CHILD, SHOW_PARENT, TreeNode };

export default compose(
  withState('treeExpandedKeys', 'setTreeExpandedKeys', []),
  injectIntl
)(InputSelectTree);
