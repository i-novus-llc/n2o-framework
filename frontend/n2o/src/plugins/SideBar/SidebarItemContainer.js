import React from 'react';
import cn from 'classnames';
import { map } from 'lodash';
import SidebarDropdown from './SidebarDropdown';
import { NavLink } from 'react-router-dom';

const ItemType = {
  DROPDOWN: 'dropdown',
  LINK: 'link',
};

const OUTER_LINK_TYPE = 'outer';

function SidebarItemContainer({ item, activeId, sidebarOpen }) {
  const { type, linkType, subItems } = item;

  const renderLink = item =>
    linkType === OUTER_LINK_TYPE
      ? renderOuterLink(item)
      : renderInnerLink(item);
  const renderOuterLink = ({ href, label }) => (
    <a className="n2o-sidebar__item" href={href}>
      {label}
    </a>
  );
  const renderInnerLink = ({ href, label }) => (
    <NavLink
      exact
      to={href}
      className="n2o-sidebar__item"
      activeClassName="active"
    >
      {label}
    </NavLink>
  );

  const renderDropdown = () => (
    <SidebarDropdown {...item}>
      {map(subItems, item => (
        <div className="n2o-sidebar__sub-item">{renderLink(item)}</div>
      ))}
    </SidebarDropdown>
  );

  return (
    <div
      className={cn({
        'n2o-sidebar__item--dropdown': type === ItemType.DROPDOWN,
      })}
    >
      {type === ItemType.LINK && renderLink(item)}
      {type === ItemType.DROPDOWN && renderDropdown()}
    </div>
  );
}

export default SidebarItemContainer;
