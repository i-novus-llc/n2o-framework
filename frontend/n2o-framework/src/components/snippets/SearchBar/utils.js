import React from 'react';
import cn from 'classnames';
import isString from 'lodash/isString';
import NavLink from 'reactstrap/lib/NavLink';
import { NavLink as RouterLink } from 'react-router-dom';
import DropdownItem from 'reactstrap/lib/DropdownItem';

const iconInPopUpClassName = (icon, directionIconsInPopUp) => {
  return cn(icon, {
    'n2o-search-bar__popup_icon-left': directionIconsInPopUp === 'left',
    'n2o-search-bar__popup_icon-right': directionIconsInPopUp === 'right',
  });
};

export const itemInSearchBarClassName = directionIconsInPopUp => {
  return cn({
    'n2o-search-bar__popup_item-right': directionIconsInPopUp === 'right',
    'n2o-search-bar__popup_item-left': directionIconsInPopUp === 'left',
  });
};

const renderIcon = (icon, directionIconsInPopUp) => {
  return isString(icon) ? (
    <i className={iconInPopUpClassName(icon, directionIconsInPopUp)} />
  ) : (
    icon
  );
};

export const RenderLink = props => {
  const {
    linkType,
    href,
    description,
    label,
    icon,
    directionIconsInPopUp,
  } = props;
  return linkType === 'outer' ? (
    <NavLink href={href} title={description}>
      {renderIcon(icon, directionIconsInPopUp)}
      {label}
    </NavLink>
  ) : (
    <RouterLink
      exact
      className="nav-link"
      to={href}
      title={description}
      activeClassName="active"
    >
      {renderIcon(icon, directionIconsInPopUp)}
      {label}
    </RouterLink>
  );
};

export const renderDescription = props => {
  const { description, disabled } = props;
  return description && disabled ? (
    <DropdownItem header className="n2o-search-bar__popup_desc-disabled">
      {description}
    </DropdownItem>
  ) : (
    description && <DropdownItem header>{description}</DropdownItem>
  );
};

export const renderDivider = props => {
  const { separateLink } = props;
  return separateLink && separateLink === true && <DropdownItem divider />;
};
