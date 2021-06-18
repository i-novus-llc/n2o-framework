import React from 'react'
import PropTypes from 'prop-types'

import { SearchBarPopUp } from './SearchBarPopUp'

export function SearchBarEmptyMenu({ dropdownOpen, urlFieldId }) {
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

SearchBarEmptyMenu.propTypes = {
    dropdownOpen: PropTypes.bool,
    urlFieldId: PropTypes.string,
}

export default SearchBarEmptyMenu
