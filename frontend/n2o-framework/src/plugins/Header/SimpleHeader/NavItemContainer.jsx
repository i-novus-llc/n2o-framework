import React from 'react';
import PropTypes from 'prop-types';
import { Link, NavLink } from 'react-router-dom';
import cx from 'classnames';
import { isEmpty, get } from 'lodash';

import {
  NavItem,
  UncontrolledDropdown,
  DropdownToggle,
  DropdownMenu,
  DropdownItem,
} from 'reactstrap';
import SecurityCheck from '../../../core/auth/SecurityCheck';

/**
 * Контейнер navItem'ов, в зависимости от type, создает внутри линк, дропдаун или текст
 * @param {object} props - пропсы
 * @param {object} props.item  - объект, пропсы которого перейдут в item. Например, для ссыллок {id, label, href,type, link, linkType}
 * @param {string} props.activeId  - id активного item'a
 * @param {string} props.  - id активного item'a
 */
const NavItemContainer = ({
  item,
  activeId,
  type,
  sidebarOpen,
  options,
  direction,
}) => {
  const getInnerLink = (item, className) => (
    <NavLink exact className="nav-link" to={item.href} activeClassName="active">
      {item.icon && <NavItemIcon />}
      {item.label}
    </NavLink>
  );

  const NavItemIcon = () => <i className={cx('mr-1', item.icon)} />;

  const handleLink = (item, className) => {
    if (item.linkType === 'outer') {
      return (
        <NavItem>
          {item.icon && <NavItemIcon />}
          <a className={cx('nav-link', className)} href={item.href}>
            {item.label}
          </a>
        </NavItem>
      );
    } else {
      return (
        <NavItem>
          <NavLink
            exact
            className={cx('nav-link', className)}
            to={item.href}
            activeClassName="active"
          >
            {item.icon && <NavItemIcon />}
            {item.label}
          </NavLink>
        </NavItem>
      );
    }
  };

  const handleLinkDropdown = (item, dropdownItems) => {
    return (
      <UncontrolledDropdown nav inNavbar direction={direction}>
        <DropdownToggle nav caret>
          {item.icon && <NavItemIcon />}
          {item.label}
        </DropdownToggle>
        <DropdownMenu right={get(options, 'right', false)}>
          {dropdownItems}
        </DropdownMenu>
      </UncontrolledDropdown>
    );
  };

  let dropdownItems = [];
  if (item.type === 'dropdown' && !sidebarOpen) {
    dropdownItems = item.subItems.map((child, i) => (
      <DropdownItem>{handleLink(child, 'dropdown-item')}</DropdownItem>
    ));
    if (
      item.type === 'dropdown' &&
      item.subItems.length > 1 &&
      type === 'sidebar'
    ) {
      dropdownItems = [
        <DropdownItem key={-1} onClick={e => e.preventDefault()}>
          {item.icon && <NavItemIcon />}
          <a className="dropdown-item">{item.oldLabel || item.label}</a>
        </DropdownItem>,
        ...dropdownItems,
      ];
    }
  } else if (type === 'sidebar' && item.type === 'dropdown' && sidebarOpen) {
    const defaultLink = item => (
      <Link className="dropdown-item" to={item.href}>
        {item.icon && <NavItemIcon />}
        {item.label}
      </Link>
    );
    const linkItem = item =>
      item.linkType === 'outer'
        ? defaultLink(item)
        : getInnerLink(item, 'dropdown-item');
    dropdownItems = item.subItems.map((item, i) => (
      <DropdownItem> {linkItem(item)} </DropdownItem>
    ));
  }

  return (
    (item.type === 'dropdown' &&
      !sidebarOpen &&
      handleLinkDropdown(item, dropdownItems)) ||
    (item.type === 'link' && handleLink(item)) ||
    (item.type === 'text' && (
      <NavItem>
        {item.icon && <NavItemIcon />}
        <span className="nav-link">{item.label}</span>
      </NavItem>
    )) ||
    null
  );
};

NavItemContainer.propTypes = {
  item: PropTypes.shape({
    label: PropTypes.string,
    href: PropTypes.string,
    icon: PropTypes.string,
    linkType: PropTypes.oneOf(['inner', 'outer']),
    withSubMenu: PropTypes.bool,
    subItems: PropTypes.array,
  }),
  type: PropTypes.oneOf(['header', 'sidebar']),
  open: PropTypes.bool,
  direction: PropTypes.string,
};

NavItemContainer.defaultProps = {
  direction: 'bottom',
};

export default NavItemContainer;
