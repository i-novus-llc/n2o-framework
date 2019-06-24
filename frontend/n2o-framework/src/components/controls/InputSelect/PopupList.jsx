import React from 'react';
import PropTypes from 'prop-types';
import PopupItems from './PopupItems';
import cx from 'classnames';

/**
 * Компонент попапа для {@link InputSelect}
 * @reactProps {array} options - массив данных
 * @reactProps {string} valueFieldId - значение ключа value в данных
 * @reactProps {string} labelFieldId - значение ключа label в данны
 * @reactProps {string} iconFieldId - поле для иконки
 * @reactProps {string} imageFieldId - поле для картинки
 * @reactProps {string} groupFieldId - поле для группировки
 * @reactProps {string} badgeFieldId - поле для баджей
 * @reactProps {string} badgeColorFieldId - поле для цвета баджа
 * @reactProps {array} disabledValues - неактивные данные
 * @reactProps {string} format - формат
 * @reactProps {boolean} hasCheckboxes - флаг наличия чекбоксов
 * @reactProps {array} selected - выбранные элементы
 * @reactProps {boolean} isExpanded - флаг видимости попапа
 * @reactProps {function} onSelect - callback при выборе элемента
 * @reactProps {function} onScrollEnd - callback при достижения конца прокрутки попапа
 * @reactProps {function} onRemoveItem - callback при удаление элемента
 * @reactProps {any} inputSelect
 * @reactProps {any} expandPopUp
 * @reactProps {boolean} needAddFilter
 * @reactProps {node} children - элемент потомок компонента PopupList
 */

function PopupList({
  children,
  inputSelect,
  isExpanded,
  onScrollEnd,
  expandPopUp,
  filterValue,
  needAddFilter,
  ...rest
}) {
  /**
   * Проверяет достигла ли прокрутка конца
   * @param element - элемент
   * @returns {boolean}
   */

  const isBottom = ({ scrollHeight, scrollTop, clientHeight }) =>
    Math.floor(scrollHeight - scrollTop) === clientHeight;

  /**
   * Обработчик прокрутки попапа
   * @param evt - событие прокрутки
   */

  const trackScrolling = evt => {
    if (isBottom(evt.target)) {
      onScrollEnd(needAddFilter ? filterValue : {});
    }
  };

  let ref = null;

  return (
    <div className={cx('n2o-dropdown-control')} onScroll={trackScrolling}>
      {children}
      <PopupItems {...rest} />
    </div>
  );
}

PopupList.propTypes = {
  isExpanded: PropTypes.bool.isRequired,
  onScrollEnd: PropTypes.func,
  options: PropTypes.array.isRequired,
  valueFieldId: PropTypes.string.isRequired,
  labelFieldId: PropTypes.string.isRequired,
  iconFieldId: PropTypes.string,
  imageFieldId: PropTypes.string,
  groupFieldId: PropTypes.string,
  badgeFieldId: PropTypes.string,
  badgeColorFieldId: PropTypes.string,
  disabledValues: PropTypes.array,
  onSelect: PropTypes.func,
  selected: PropTypes.array,
  hasCheckboxes: PropTypes.bool,
  onRemoveItem: PropTypes.func,
  format: PropTypes.string,
  inputSelect: PropTypes.any,
  expandPopUp: PropTypes.any,
  children: PropTypes.node,
  needAddFilter: PropTypes.bool,
};

export default PopupList;
