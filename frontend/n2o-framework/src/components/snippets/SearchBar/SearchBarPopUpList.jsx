import React from 'react'
import PropTypes from 'prop-types'
import get from 'lodash/get'
import classNames from 'classnames'
import DropdownMenu from 'reactstrap/lib/DropdownMenu'
import DropdownItem from 'reactstrap/lib/DropdownItem'

import { RenderLink, renderDivider, itemInSearchBarClassName } from './utils'

function SearchBarPopUpList({
    labelFieldId,
    descriptionFieldId,
    iconFieldId,
    urlFieldId,
    ...props
}) {
    const { menu, directionIconsInPopUp } = props

    return (
        <DropdownMenu className="n2o-search-bar__popup_list">
            {menu.map((linkProps) => {
                const { id, disabled = false, linkType, separateLink } = linkProps

                const description = get(linkProps, descriptionFieldId)
                const label = get(linkProps, labelFieldId)
                const icon = get(linkProps, iconFieldId)
                const href = get(linkProps, urlFieldId)

                return (
                    <div
                        className={classNames(
                            'n2o-search-bar__popup_list__item-container',
                            {
                                disabled,
                            },
                        )}
                        key={id}
                    >
                        <DropdownItem
                            className={itemInSearchBarClassName(directionIconsInPopUp)}
                            disabled={disabled}
                            onClick={props.onItemClick}
                        >
                            <RenderLink
                                description={description}
                                label={label}
                                icon={icon}
                                href={href}
                                directionIconsInPopUp={directionIconsInPopUp}
                                linkType={linkType}
                                disabled={disabled}
                            />
                        </DropdownItem>
                        {renderDivider(linkProps)}
                    </div>
                )
            })}
        </DropdownMenu>
    )
}

SearchBarPopUpList.propTypes = {
    /**
   * Данные для PopUp
   */
    menu: PropTypes.array,
    /**
   * направление иконок и items в popUp: left(default), right
   */
    directionIconsInPopUp: PropTypes.string,
    /**
   * Описание item
   */
    description: PropTypes.string,
    /**
   * резолв отключения item в popUp
   */
    disabled: PropTypes.bool,
    /**
   * резолв divider в item
   */
    separateLink: PropTypes.bool,
}

export default SearchBarPopUpList
