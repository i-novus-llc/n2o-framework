import React from 'react';
import TreeSelect from 'rc-tree-select';
import {
  difference,
  filter as filterF,
  every,
  find,
  isArray,
  isEmpty,
  keys,
  forEach,
  map,
  memoize,
  some,
} from 'lodash';
import Icon from '../../snippets/Icon/Icon';
import InlineSpinner from '../../snippets/Spinner/InlineSpinner';
import CheckboxN2O from '../Checkbox/CheckboxN2O';
import { defaultProps, propTypes } from './allProps';
import { compose, withState, setDisplayName } from 'recompose';
import propsResolver from '../../../utils/propsResolver';
import { visiblePartPopup, getCheckedStrategy } from './until';
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
//TODO переделать в класс
function InputSelectTree({
  onOpen,
  onFocus,
  value,
  onBlur,
  searchPlaceholder,
  dropdownExpanded,
  setDropdownExpanded,
  placeholder,
  setTreeExpandedKeys,
  notFoundContent,
  treeExpandedKeys,
  closePopupOnSelect,
  loading,
  isLoading,
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
  showCheckedStrategy,
  ...rest
}) {
  const popupProps = {
    prefixCls: 'n2o-select-tree',
    iconFieldId,
    imageFieldId,
    labelFieldId,
    badgeFieldId,
    badgeColorFieldId,
  };

  /**
   * Функуия для создания дерева.
   * Преобразует коллекцию вида [..., { ... }] в [ ..., {..., children: [...]}]
   * Вложение происходит при совпадении valueFieldId и parentFieldId.
   * @param items
   * @returns {*}
   */
  const createTree = memoize(items => {
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
          children: [],
        },
      }),
      {}
    );

    keys(itemsByID).forEach(key => {
      if (
        itemsByID[key][parentFieldId] &&
        itemsByID[itemsByID[key][parentFieldId]] &&
        itemsByID[itemsByID[key][parentFieldId]].children
      ) {
        itemsByID[itemsByID[key][parentFieldId]].children.push({
          ...itemsByID[key],
        });
      }
    });

    return keys(itemsByID)
      .filter(key => {
        if (!itemsByID[key][parentFieldId]) {
          return key;
        }
        if (
          itemsByID[key][parentFieldId] &&
          !itemsByID.hasOwnProperty(itemsByID[key][parentFieldId])
        ) {
          return key;
        }
      })
      .reduce((acc, key) => [...acc, { ...itemsByID[key] }], []);
  });

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
   * Взять данные по ids.
   * ['id', 'id'] => [{ id: 'id', ... },{ id: 'id', ... }]
   * @param ids
   */
  const getDataByIds = ids =>
    filterF(data, node => some(ids, v => v === node[valueFieldId]));

  /**
   * Берет всех потомков у родителей
   * @param ids
   * @param arrData
   */
  const getChildWithParenId = (ids, arrData) => {
    let buff = getDataByIds(ids);

    // рекурсивно спускаемся вниз ко всем потомкам
    // и добавляем потомков в буфер
    const recursionFn = ids =>
      forEach(ids, id => {
        const childs = filterF(arrData, [parentFieldId, id]);
        buff = buff.concat(childs);
        recursionFn(map(childs, valueFieldId));
      });

    recursionFn(ids, arrData);
    return buff;
  };

  /**
   * Берет всех родителей у потомков
   * если все потомки выделены
   * @param ids
   * @param arrData
   */
  const getParentsWithChildId = (ids, arrData) => {
    let buff = getDataByIds(ids);

    // Берем только те id потомков у которых выделены
    // родители
    const recursionFn = ids => {
      let parentBuff = [];

      forEach(ids, id => {
        const node = find(arrData, { id });
        const parentIdNode = node[parentFieldId];
        if (!parentIdNode) {
          return false;
        }

        const allParendChilds = filterF(arrData, [parentFieldId, parentIdNode]);
        const hasParentAllChildsCheck = every(allParendChilds, child =>
          ids.includes(child[valueFieldId])
        );

        if (hasParentAllChildsCheck) {
          parentBuff.push(parentIdNode);
          const buffHasParent = find(buff, { id: parentIdNode });
          if (!buffHasParent) {
            buff.push(find(arrData, { id: parentIdNode }));
          }
        }
      });

      if (!isEmpty(parentBuff)) {
        recursionFn(parentBuff);
      }
    };

    recursionFn(ids, arrData);
    return buff;
  };

  const getSingleValue = value => find(data, [valueFieldId, value]);
  const getMultiValue = value => {
    // if (isArray(value) && eq(showCheckedStrategy, SHOW_PARENT)) {
    //   return getChildWithParenId(value, data);
    // } else if (isArray(value) && eq(showCheckedStrategy, SHOW_CHILD)) {
    //   return getParentsWithChildId(value, data);
    // } else {
    // стратегия SHOW_ALL
    return getDataByIds(value);
  };
  /**
   * Функция преобразования value rcTreeSelect в формат n2o
   * Производит поиск по родителям и потомкам.
   * rcTreeSelect не дает информации о выделенных потомках при моде 'SHOW_PARENT'
   * и о выделенных родителях при 'SHOW_CHILD'
   * ['id', 'id'] => [{ id: 'id', ... },{ id: 'id', ... }]
   * @param value
   * @returns {*}
   */
  const getItemByValue = value => {
    if (!value) return null;
    if (!multiSelect) {
      return getSingleValue(value);
    }
    return getMultiValue(value);
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
    if (visible) {
      onFocus();
    } else {
      onBlur();
    }
    onToggle(visible);
    setDropdownExpanded(visible);
    visible ? onOpen() : onClose();
    if (ajax) setTreeExpandedKeys([]);
    return false;
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

  const renderSwitcherIcon = ({ isLeaf }) =>
    isLeaf ? null : <Icon name="fa fa-chevron-right" />;

  const clearIcon = <Icon name="fa fa-times" />;

  const inputIcon = loading ? (
    <InlineSpinner />
  ) : (
    <Icon name="fa fa-chevron-down" />
  );

  const getPopupContainer = container => container;

  const open = !loading ? dropdownExpanded : false;

  return (
    <TreeSelect
      tabIndex={1}
      {...value && { value: setValue(value) }}
      open={open}
      onDropdownVisibleChange={handleDropdownVisibleChange}
      className={cx('n2o form-control', 'n2o-input-select-tree', className, {
        loading,
      })}
      switcherIcon={renderSwitcherIcon}
      inputIcon={inputIcon}
      multiple={multiSelect}
      treeCheckable={hasCheckboxes && <CheckboxN2O inline />}
      treeData={createTree(data)}
      filterTreeNode={handlerFilter}
      treeNodeFilterProp={labelFieldId}
      treeNodeLabelProp={labelFieldId}
      maxTagTextLength="10"
      removeIcon={clearIcon}
      clearIcon={clearIcon}
      onChange={handleChange}
      onSelect={handleSelect}
      onSearch={handleSearch}
      treeExpandedKeys={treeExpandedKeys}
      onTreeExpand={onTreeExpand}
      dropdownPopupAlign={dropdownPopupAlign}
      prefixCls="n2o-select-tree"
      showCheckedStrategy={getCheckedStrategy(showCheckedStrategy)}
      getPopupContainer={getPopupContainer}
      notFoundContent={intl.formatMessage({
        id: 'inputSelectTree.notFoundContent',
        defaultMessage: notFoundContent || ' ',
      })}
      placeholder={intl.formatMessage({
        id: 'inputSelectTree.placeholder',
        defaultMessage: placeholder || ' ',
      })}
      searchPlaceholder={intl.formatMessage({
        id: 'inputSelectTree.searchPlaceholder',
        defaultMessage: searchPlaceholder || ' ',
      })}
      {...rest}
    >
      {children}
    </TreeSelect>
  );
}

InputSelectTree.defaultProps = defaultProps;
InputSelectTree.propTypes = propTypes;

export { TreeNode, InputSelectTree };

export default compose(
  setDisplayName('InputSelectTree'),
  withState('treeExpandedKeys', 'setTreeExpandedKeys', []),
  withState('dropdownExpanded', 'setDropdownExpanded', false),
  injectIntl
)(InputSelectTree);
