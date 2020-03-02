import React from 'react';
import { DropdownMenu, DropdownItem } from 'reactstrap';
import NavLink from 'reactstrap/lib/NavLink';
import { NavLink as RouterLink } from 'react-router-dom';
import isString from 'lodash/isString';
import cn from 'classnames';

function SearchBarPopUpList(props) {
  const { menu, iconDirection = 'left' } = props;

  const renderIcon = icon => {
    return isString(icon) ? (
      <i
        className={cn(icon, {
          'n2o-search-bar__popup_icon-left': iconDirection === 'left',
          'n2o-search-bar__popup_icon-right': iconDirection === 'right',
        })}
      />
    ) : (
      icon
    );
  };

  const RenderLink = props => {
    const { linkType, href, description, icon, label, iconDirection } = props;
    return linkType === 'outer' ? (
      <NavLink href={href} title={description}>
        {renderIcon(icon)}
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
        {renderIcon(icon)}
        {label}
      </RouterLink>
    );
  };

  return (
    <DropdownMenu className="n2o-search-bar__popup_list">
      {menu.map(linkProps => {
        const { id, description } = linkProps;
        return (
          <React.Fragment key={id}>
            <DropdownItem
              className={cn({
                'n2o-search-bar__popup_item-right': iconDirection === 'right',
                'n2o-search-bar__popup_item-left': iconDirection === 'left',
              })}
            >
              <RenderLink {...linkProps} />
            </DropdownItem>
            {description && <DropdownItem header>{description}</DropdownItem>}
          </React.Fragment>
        );
      })}
    </DropdownMenu>
  );
}

export default SearchBarPopUpList;
