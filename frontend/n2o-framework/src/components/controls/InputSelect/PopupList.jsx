import React from 'react';
import PropTypes from 'prop-types';
import { compose, lifecycle, withHandlers, withState } from 'recompose';
import cx from 'classnames';
import { isEqual, invoke } from 'lodash';

import PopupItems from './PopupItems';
import { isBottom } from './utils';

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
 * @reactProps {any} expandPopUp
 * @reactProps {boolean} needAddFilter
 * @reactProps {node} children - элемент потомок компонента PopupList
 */

function PopupList({
  children,
  isExpanded,
  onScrollEnd,
  expandPopUp,
  filterValue,
  needAddFilter,
  setMenuElement,
  ...rest
}) {
  return (
    <div className={cx('n2o-dropdown-control n2o-pop-up')} ref={setMenuElement}>
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
  expandPopUp: PropTypes.any,
  children: PropTypes.node,
  needAddFilter: PropTypes.bool,
};

const enhance = compose(
  withState('menuElement', 'setMenuElement', null),
  withHandlers({
    onScroll: ({ needAddFilter, filterValue, onScrollEnd }) => e => {
      if (isBottom(e.target)) onScrollEnd(needAddFilter ? filterValue : {});
    },
  }),
  lifecycle({
    componentDidUpdate(prevProps) {
      if (!isEqual(prevProps.options, this.props.options)) {
        invoke(this.props, 'scheduleUpdate');
      }

      if (!prevProps.menuElement && this.props.menuElement) {
        this.props.menuElement.addEventListener('scroll', this.props.onScroll);
      }
    },

    componentWillUnmount() {
      if (this.props.menuElement) {
        this.props.menuElement.removeEventListener(
          'scroll',
          this.props.onScroll
        );
      }
    },
  })
);
export default enhance(PopupList);
