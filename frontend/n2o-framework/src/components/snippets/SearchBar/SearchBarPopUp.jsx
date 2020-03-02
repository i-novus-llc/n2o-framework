import React from 'react';
import { Dropdown, DropdownToggle } from 'reactstrap';
import SearchBarPopUpList from './SearchBarPopUpList';

function SearchBarPopUp(props) {
  const { dropdownOpen } = props;
  return (
    <Dropdown
      isOpen={dropdownOpen}
      className="n2o-search-bar__popup"
      toggle={() => {}}
    >
      <DropdownToggle
        tag="div"
        data-toggle="dropdown"
        aria-expanded={dropdownOpen}
      />
      <SearchBarPopUpList {...props} />
    </Dropdown>
  );
}

export default SearchBarPopUp;
