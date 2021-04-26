import React from 'react'
import PropTypes from 'prop-types'
import isUndefined from 'lodash/isUndefined'
import { compose, lifecycle } from 'recompose'

import listContainer from '../../controls/listContainer'

import SearchBar from './SearchBar'

function SearchBarContainer(props) {
    const {
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
    } = props

    return (
        <SearchBar
            descriptionFieldId={descrFieldId}
            iconFieldId={iconFieldId}
            labelFieldId={labelFieldId}
            urlFieldId={urlFieldId}
            menu={data}
            trigger={trigger}
            onSearch={onSearch}
            onFocus={() => props._fetchData()}
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
}

export default compose(listContainer)(SearchBarContainer)
