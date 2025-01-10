import React from 'react'

import { listContainer } from '../../controls/listContainer'

import SearchBar from './SearchBar'
import { type SearchBarContainerProps } from './types'

function SearchBarContainer({
    options = [],
    trigger,
    onSearch = () => {},
    button = null,
    icon = 'fa fa-search',
    directionIconsInPopUp = 'left',
    descrFieldId,
    iconFieldId,
    labelFieldId,
    urlFieldId,
    fetchData,
}: SearchBarContainerProps) {
    return (
        <SearchBar
            descriptionFieldId={descrFieldId}
            iconFieldId={iconFieldId}
            labelFieldId={labelFieldId}
            urlFieldId={urlFieldId}
            menu={options}
            trigger={trigger}
            onSearch={onSearch}
            onFocus={() => fetchData()}
            button={button}
            icon={icon}
            directionIconsInPopUp={directionIconsInPopUp}
        />
    )
}

export default listContainer<SearchBarContainerProps>(SearchBarContainer)
