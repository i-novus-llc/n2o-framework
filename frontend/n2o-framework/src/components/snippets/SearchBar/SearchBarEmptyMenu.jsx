import React from 'react'

import SearchBarPopUp from './SearchBarPopUp'

function SearchBarEmptyMenu({ dropdownOpen, urlFieldId }) {
    const emptyMenu = [
        {
            id: 'Ничего не найдено',
            label: 'Ничего не найдено',
            href: '/',
            disabled: true,
        },
    ]

    if (!urlFieldId) {
        return null
    }

    return <SearchBarPopUp menu={emptyMenu} dropdownOpen={dropdownOpen} />
}

export default SearchBarEmptyMenu
