import React from 'react';
import PropTypes from 'prop-types';
import { Badge, DropdownItem } from 'reactstrap';
import scrollIntoView from 'scroll-into-view-if-needed';

import Icon from '../../snippets/Icon/Icon';
import CheckboxN2O from '../Checkbox/CheckboxN2O';
import propsResolver from '../../../utils/propsResolver';
import { find } from 'lodash';
import cx from 'classnames';
import { findDOMNode } from 'react-dom';

import { groupData, inArray, isDisabled } from './utils';

/**
 * Компонент попапа для {@link InputSelect}
 * @reactProps {array} options - массив данных
 * @reactProps {string} activeLabel - активный лейбел
 * @reactProps {function} setActiveLabel - смена активного лейбла
 * @reactProps {string} valueFieldId - значение ключа value в данных
 * @reactProps {string} labelFieldId - значение ключа label в данных
 * @reactProps {string} iconFieldId - поле для иконки
 * @reactProps {string} imageFieldId - поле для картинки
 * @reactProps {string} groupFieldId - поле для группировки
 * @reactProps {string} badgeFieldId - поле для баджей
 * @reactProps {string} badgeColorFieldId - поле для цвета баджа
 * @reactProps {array} disabledValues - неактивные данные
 * @reactProps {function} onSelect - callback при выборе элемента
 * @reactProps {string} format - формат
 * @reactProps {boolean} hasCheckboxes - флаг наличия чекбоксов
 * @reactProps {array} selected - выбранные элементы
 * @reactProps {function} onRemoveItem - callback при удаление элемента
 * @reactProps {function} setActiveValueId
 * @reactProps {string} activeValueId
 * @reactProps {boolean} autocomplete
 */

function PopupItems({
  loading,
  options,
  activeLabel,
  setActiveLabel,
  labelFieldId,
  iconFieldId,
  valueFieldId,
  imageFieldId,
  disabledValues,
  selected,
  groupFieldId,
  hasCheckboxes,
  format,
  badgeFieldId,
  badgeColorFieldId,
  onRemoveItem,
  onSelect,
  setActiveValueId,
  activeValueId,
  autocomplete,
}) {
  const handleRef = item => {
    if (item) {
      const el = findDOMNode(item);
      if (el.classList.contains('active')) {
        scrollIntoView(el, { scrollMode: 'if-needed', block: 'nearest' });
      }
    }
  };

  const handleItemClick = ({ target }, item) => {
    if (target.nodeName === 'LABEL') return false;
    inArray(selected, item) ? onRemoveItem(item) : onSelect(item);
  };

  const displayTitle = item => {
    if (format) return propsResolver({ format }, item).format;
    return item[labelFieldId];
  };

  const renderSingleItem = item => {
    return (
      <DropdownItem
        className={cx('n2o-eclipse-content', {
          active: activeValueId === item[valueFieldId],
        })}
        onMouseOver={() =>
          setActiveValueId && setActiveValueId(item[valueFieldId])
        }
        disabled={
          !hasCheckboxes &&
          isDisabled(
            autocomplete ? item[valueFieldId] : item,
            selected,
            disabledValues
          )
        }
        ref={handleRef}
        key={item.id}
        onClick={e => handleItemClick(e, item)}
        title={displayTitle(item)}
        toggle={false}
      >
        {iconFieldId && renderIcon(item, iconFieldId)}
        {imageFieldId && renderImage(item, imageFieldId)}
        {hasCheckboxes ? renderCheckbox(item, selected) : renderLabel(item)}
        {badgeFieldId && renderBadge(item, badgeFieldId, badgeColorFieldId)}
      </DropdownItem>
    );
  };

  const renderIcon = (item, iconFieldId) =>
    item[iconFieldId] && <Icon name={item[iconFieldId]} />;
  const renderImage = (item, imageFieldId) =>
    item[imageFieldId] && <img src={item[imageFieldId]} />;
  const renderBadge = (item, badgeFieldId, badgeColorFieldId) => (
    <Badge color={item[badgeColorFieldId]}>{item[badgeFieldId]}</Badge>
  );
  const renderCheckbox = (item, selected) => (
    <CheckboxN2O
      value={inArray(selected, item)}
      label={displayTitle(item)}
      inline
    />
  );
  const renderLabel = item => (
    <span className="text-cropped">{displayTitle(item)}</span>
  );

  const renderSingleItems = options =>
    options.map((item, i) => renderSingleItem(item, i));
  const renderGroupedItems = (options, groupFieldId) => {
    const groupedData = groupData(options, groupFieldId);
    return Object.keys(groupedData).map(key =>
      renderGroup(key, groupedData[key])
    );
  };

  const renderGroup = (key, value) => (
    <React.Fragment key={key}>
      {key ? (
        <DropdownItem key={key} header>
          {key}
        </DropdownItem>
      ) : null}
      {renderSingleItems(value)}
      <DropdownItem divider />
    </React.Fragment>
  );

  const renderMenuItems = options =>
    groupFieldId
      ? renderGroupedItems(options, groupFieldId)
      : renderSingleItems(options);

  const renderMenu = options => {
    if (options && options[0] !== null && options.length) {
      return renderMenuItems(options);
    }
    if (!loading && options.length === 0) {
      return <DropdownItem header>Ничего не найдено</DropdownItem>;
    }
  };

  return <React.Fragment>{renderMenu(options)}</React.Fragment>;
}

PopupItems.propTypes = {
  options: PropTypes.array.isRequired,
  activeLabel: PropTypes.string,
  setActiveLabel: PropTypes.func,
  valueFieldId: PropTypes.string.isRequired,
  labelFieldId: PropTypes.string.isRequired,
  iconFieldId: PropTypes.string,
  imageFieldId: PropTypes.string,
  groupFieldId: PropTypes.string,
  badgeFieldId: PropTypes.string,
  badgeColorFieldId: PropTypes.string,
  disabledValues: PropTypes.array,
  onSelect: PropTypes.func,
  onScrollEnd: PropTypes.func,
  selected: PropTypes.array,
  hasCheckboxes: PropTypes.bool,
  onRemoveItem: PropTypes.func,
  format: PropTypes.string,
  setActiveValueId: PropTypes.func,
  activeValueId: PropTypes.string,
  autocomplete: PropTypes.bool,
};

export default PopupItems;
