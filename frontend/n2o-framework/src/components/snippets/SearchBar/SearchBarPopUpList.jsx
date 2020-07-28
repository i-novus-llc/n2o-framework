import React from 'react';
import DropdownMenu from 'reactstrap/lib/DropdownMenu';
import DropdownItem from 'reactstrap/lib/DropdownItem';
import {
  RenderLink,
  renderDescription,
  renderDivider,
  itemInSearchBarClassName,
} from './utils';
import PropTypes from 'prop-types';

function SearchBarPopUpList(props) {
  const { menu, directionIconsInPopUp } = props;

  return (
    <DropdownMenu className="n2o-search-bar__popup_list">
      {menu.map(linkProps => {
        const { id, description, disabled, separateLink } = linkProps;
        return (
          <React.Fragment key={id}>
            <DropdownItem
              className={itemInSearchBarClassName(directionIconsInPopUp)}
              disabled={disabled}
            >
              <RenderLink
                {...linkProps}
                directionIconsInPopUp={directionIconsInPopUp}
              />
            </DropdownItem>
            {renderDescription(linkProps)}
            {renderDivider(linkProps)}
          </React.Fragment>
        );
      })}
    </DropdownMenu>
  );
}

SearchBarPopUpList.propTypes = {
  /**
   * Данные для PopUp
   */
  menu: PropTypes.array,
  /**
   * направление иконок и items в popUp: left(default), right
   */
  directionIconsInPopUp: PropTypes.string,
  /**
   * Описание item
   */
  description: PropTypes.string,
  /**
   * резолв отключения item в popUp
   */
  disabled: PropTypes.bool,
  /**
   * резолв divider в item
   */
  separateLink: PropTypes.bool,
};

export default SearchBarPopUpList;
