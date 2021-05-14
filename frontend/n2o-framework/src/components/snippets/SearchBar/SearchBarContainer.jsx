import React from 'react'
import PropTypes from 'prop-types'

import listContainer from '../../controls/listContainer'

// eslint-disable-next-line import/no-named-as-default
import SearchBar from './SearchBar'

function SearchBarContainer({
    data,
    trigger,
    onSearch,
    button,
    icon,
    directionIconsInPopUp,
    descrFieldId,
    iconFieldId,
    labelFieldId,
    urlFieldId,
    _fetchData,
}) {
    return (
        <SearchBar
            descriptionFieldId={descrFieldId}
            iconFieldId={iconFieldId}
            labelFieldId={labelFieldId}
            urlFieldId={urlFieldId}
            menu={data}
            trigger={trigger}
            onSearch={onSearch}
            onFocus={() => _fetchData()}
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
    data: [],
}

SearchBarContainer.propTypes = {
    /**
     * данные в popUp(ссылки), при наличии dataProvider
     */
    data: PropTypes.array,
    /**
     * Направление иконок ссылок в popUp
     */
    directionIconsInPopUp: PropTypes.string,
    icon: PropTypes.string,
    urlFieldId: PropTypes.string,
    labelFieldId: PropTypes.string,
    descrFieldId: PropTypes.string,
    iconFieldId: PropTypes.string,
    _fetchData: PropTypes.func,
    onSearch: PropTypes.func,
    trigger: PropTypes.any,
    button: PropTypes.any,
}

export default listContainer(SearchBarContainer)
