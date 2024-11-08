import React from 'react'
import { Dropdown, DropdownToggle } from 'reactstrap'

import { SearchBarPopUpList } from './SearchBarPopUpList'

export function SearchBarPopUp({
    labelFieldId,
    descriptionFieldId,
    iconFieldId,
    urlFieldId,
    ...props
}) {
    const { dropdownOpen } = props

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
            <SearchBarPopUpList
                labelFieldId={labelFieldId}
                descriptionFieldId={descriptionFieldId}
                iconFieldId={iconFieldId}
                urlFieldId={urlFieldId}
                {...props}
            />
        </Dropdown>
    )
}

export default SearchBarPopUp
