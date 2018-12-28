import React from 'react';
import PropTypes from 'prop-types';
import { map, defaultTo, pick } from 'lodash';
import {
  UncontrolledButtonDropdown,
  UncontrolledTooltip,
  DropdownToggle,
  DropdownMenu,
  DropdownItem
} from 'reactstrap';
import Icon from '../../../../snippets/Icon/Icon';
import { MODIFIERS, initUid } from './until';

/**
 * @param label - Название компонента dropdown
 * @param hint - Тултип компонента dropdown
 * @param visible - Видимость компонента dropdown
 * @param disabled - Активность компонента dropdown
 * @param id - уникальный идентификатор
 * @param icon - Иконка компонента dropdown
 * @param menu - Элементы списка
 * @param placement - Расположение тултипа, по дефолту bottom
 * @param delay - Задержка перед показом тултипа
 * @param hideArrow - Показать скрыть стрелку тултипа
 * @param offset - Смещение тултипа относительно положения заданной placement
 * @returns {*}
 * @constructor
 */
function HintDropDown({ uId, label, hint, visible, id, menu, icon, onClick, ...rest }) {
  const otherToltipProps = pick(rest, ['delay', 'placement', 'hideArrow', 'offset']);
  const dropdownProps = pick(rest, ['disabled', 'direction', 'active', 'color', 'size']);

  const createDropDownMenu = ({ label, visible, icon, action, ...itemProps }) => {
    const handlerClick = action => () => onClick(action);

    return (
      defaultTo(visible, true) && (
        <DropdownItem {...itemProps} onClick={handlerClick(action)}>
          {icon && <Icon name={icon} />}
          {label}
        </DropdownItem>
      )
    );
  };

  return (
    visible && (
      <UncontrolledButtonDropdown direction="down">
        {hint && (
          <UncontrolledTooltip target={uId} modifiers={MODIFIERS} {...otherToltipProps}>
            {hint}
          </UncontrolledTooltip>
        )}
        <DropdownToggle {...dropdownProps} id={uId}>
          {icon && <Icon name={icon} />}
          {label}
          <Icon className="n2o-dropdown-icon" name="fa fa-angle-down" />
        </DropdownToggle>
        <DropdownMenu positionFixed={true} modifiers={MODIFIERS}>
          {map(menu, createDropDownMenu)}
        </DropdownMenu>
      </UncontrolledButtonDropdown>
    )
  );
}

HintDropDown.propTypes = {
  label: PropTypes.string,
  hint: PropTypes.string,
  visible: PropTypes.bool,
  disabled: PropTypes.bool,
  id: PropTypes.string,
  size: PropTypes.string,
  color: PropTypes.string,
  menu: PropTypes.object,
  placement: PropTypes.oneOf([
    'auto',
    'auto-start',
    'auto-end',
    'top',
    'top-start',
    'top-end',
    'right',
    'right-start',
    'right-end',
    'bottom',
    'bottom-start',
    'bottom-end',
    'left',
    'left-start',
    'left-end'
  ]),
  delay: PropTypes.oneOfType([
    PropTypes.shape({ show: PropTypes.number, hide: PropTypes.number }),
    PropTypes.number
  ]),
  hideArrow: PropTypes.bool,
  offset: PropTypes.oneOfType([PropTypes.string, PropTypes.number])
};

HintDropDown.defaultProps = {
  visible: true,
  disabled: false,
  placement: 'top',
  size: 'md',
  color: 'link',
  menu: [],
  delay: 100,
  hideArrow: false,
  offset: 0
};

export default initUid(HintDropDown);
