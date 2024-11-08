import React from 'react'

import listContainer from '../../controls/listContainer'

import SearchBar from './SearchBar'

function SearchBarContainer({
    options,
    trigger,
    onSearch,
    button,
    icon,
    directionIconsInPopUp,
    descrFieldId,
    iconFieldId,
    labelFieldId,
    urlFieldId,
    fetchData,
}) {
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

SearchBarContainer.defaultProps = {
    button: false,
    icon: 'fa fa-search',
    directionIconsInPopUp: 'left',
    onSearch: () => {},
    options: [],
}

export default listContainer(SearchBarContainer)
