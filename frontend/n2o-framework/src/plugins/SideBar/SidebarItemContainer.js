import React from 'react';
import PropTypes from 'prop-types';
import cn from 'classnames';
import map from 'lodash/map';
import SidebarDropdown from './SidebarDropdown';
import { NavLink } from 'react-router-dom';

const ItemType = {
  DROPDOWN: 'dropdown',
  LINK: 'link',
};

const OUTER_LINK_TYPE = 'outer';

/**
 * Рендер иконки
 * @param icon - иконка
 * @param label - текст итема
 * @param type - тип итема
 * @param sidebarOpen - флаг сжатия сайдбара
 * @param subItems
 * @returns {*}
 */
export const renderIcon = (icon, label, type, sidebarOpen, subItems) => {
  let component = <i className={cn(icon)} />;
  if (!sidebarOpen && type === ItemType.DROPDOWN && !subItems) {
    return label;
  } else if (!sidebarOpen && !icon) {
    component = label.substring(0, 1);
  }
  return <span className="n2o-sidebar__item-content-icon">{component}</span>;
};

/**
 * Sidebar Item
 * @param item - объект итема
 * @param activeId - активный элемент
 * @param sidebarOpen - флаг сжатия сайдбара
 * @returns {*}
 * @constructor
 */
function SidebarItemContainer({ item, activeId, sidebarOpen }) {
  const { type, linkType, subItems } = item;

  const renderItem = type => (
    <React.Fragment>
      {type === ItemType.LINK && renderLink(item)}
      {type === ItemType.DROPDOWN && renderDropdown()}
    </React.Fragment>
  );
  const renderLink = item =>
    linkType === OUTER_LINK_TYPE
      ? renderOuterLink(item)
      : renderInnerLink(item);
  const renderOuterLink = ({ href, label, iconClass }) => (
    <a className="n2o-sidebar__item" href={href}>
      {renderIcon(iconClass, label, type, sidebarOpen)}
      {label}
    </a>
  );
  const renderInnerLink = ({ href, label, iconClass }) => (
    <NavLink
      exact
      to={href}
      className="n2o-sidebar__item"
      activeClassName="active"
    >
      {renderIcon(iconClass, label, type, sidebarOpen)}
      {sidebarOpen && <span>{label}</span>}
    </NavLink>
  );

  const renderDropdown = () => (
    <SidebarDropdown {...item} sidebarOpen={sidebarOpen}>
      {map(subItems, subItem => (
        <div className="n2o-sidebar__sub-item">{renderLink(subItem)}</div>
      ))}
    </SidebarDropdown>
  );

  return (
    <div
      className={cn({
        'n2o-sidebar__item--dropdown': type === ItemType.DROPDOWN,
      })}
    >
      {renderItem(type)}
    </div>
  );
}
SidebarItemContainer.propTypes = {
  item: PropTypes.object,
  activeId: PropTypes.string,
  sidebarOpen: PropTypes.bool,
};

export default SidebarItemContainer;
