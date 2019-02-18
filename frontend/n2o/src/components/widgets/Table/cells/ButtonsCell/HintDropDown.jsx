import React from 'react';
import PropTypes from 'prop-types';
import { map, defaultTo, pick, isEmpty } from 'lodash';
import {
  UncontrolledButtonDropdown,
  UncontrolledTooltip,
  DropdownToggle,
  DropdownMenu,
  DropdownItem
} from 'reactstrap';
import Icon from '../../../../snippets/Icon/Icon';
import { MODIFIERS, initUid } from './until';
import SecurityCheck from '../../../../../core/auth/SecurityCheck';

/**
 * @param label - Название компонента dropdown
 * @param hint - Тултип компонента dropdown
 * @param visible - Видимость компонента dropdown
 * @param uId - уникальный идентификатор
 * @param icon - Иконка компонента dropdown
 * @param menu - Элементы списка
 * @param onClick - функция обработки клика
 * @param security - объект настройки прав
 * @param rest - остальные props
 * @returns {*}
 * @constructor
 */
function HintDropDown({ uId, title, hint, visible, menu, icon, onClick, security, ...rest }) {
  const otherToltipProps = pick(rest, ['delay', 'placement', 'hideArrow', 'offset']);
  const dropdownProps = pick(rest, ['disabled', 'direction', 'active', 'color', 'size']);

  const createDropDownMenu = ({ title, visible, icon, action, security, ...itemProps }) => {
    const handleClick = action => e => onClick(e, action);

    const renderItem = () =>
      defaultTo(visible, true) ? (
        <DropdownItem {...itemProps} onClick={handleClick(action)}>
          {icon && <Icon name={icon} />}
          {title}
        </DropdownItem>
      ) : null;

    return isEmpty(security) ? (
      renderItem()
    ) : (
      <SecurityCheck
        config={security}
        render={({ permissions }) => {
          return permissions ? renderItem() : null;
        }}
      />
    );
  };

  const renderDropdown = () =>
    visible ? (
      <UncontrolledButtonDropdown direction="down">
        {hint && (
          <UncontrolledTooltip target={uId} modifiers={MODIFIERS} {...otherToltipProps}>
            {hint}
          </UncontrolledTooltip>
        )}
        <DropdownToggle {...dropdownProps} id={uId}>
          {icon && <Icon name={icon} />}
          {title}
          <Icon className="n2o-dropdown-icon" name="fa fa-angle-down" />
        </DropdownToggle>
        <DropdownMenu positionFixed={true} modifiers={MODIFIERS}>
          {map(menu, createDropDownMenu)}
        </DropdownMenu>
      </UncontrolledButtonDropdown>
    ) : null;

  return isEmpty(security) ? (
    renderDropdown()
  ) : (
    <SecurityCheck
      config={security}
      render={({ permissions }) => {
        return permissions ? renderDropdown() : null;
      }}
    />
  );
}

HintDropDown.propTypes = {
  title: PropTypes.string,
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
