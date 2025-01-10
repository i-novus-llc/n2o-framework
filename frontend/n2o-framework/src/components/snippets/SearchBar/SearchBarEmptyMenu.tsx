import React from 'react'

import { SearchBarPopUp } from './SearchBarPopUp'
import { type SearchBarEmptyMenuProps, EMPTY_MENU } from './types'

export function SearchBarEmptyMenu({ dropdownOpen, urlFieldId }: SearchBarEmptyMenuProps) {
    if (!urlFieldId) { return null }

    return <SearchBarPopUp menu={EMPTY_MENU} dropdownOpen={dropdownOpen} />
}

export default SearchBarEmptyMenu
