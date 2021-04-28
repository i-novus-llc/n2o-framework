import React from 'react'
import Dropdown from 'reactstrap/lib/Dropdown'
import DropdownToggle from 'reactstrap/lib/DropdownToggle'

import SearchBarPopUpList from './SearchBarPopUpList'

function SearchBarPopUp({
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
