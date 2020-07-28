import React from 'react';
import SearchBarPopUp from './SearchBarPopUp';

function SearchBarEmptyMenu({ dropdownOpen }) {
  const emptyMenu = [
    {
      id: 'Ничего не найдено',
      label: 'Ничего не найдено',
      href: '/',
      disabled: true,
    },
  ];
  return <SearchBarPopUp menu={emptyMenu} dropdownOpen={dropdownOpen} />;
}

export default SearchBarEmptyMenu;
