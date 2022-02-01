import React from 'react'
import PropTypes from 'prop-types'
import get from 'lodash/get'
import classNames from 'classnames'
import { DropdownMenu, DropdownItem } from 'reactstrap'

import { RenderLink, renderDivider, itemInSearchBarClassName } from './utils'

export function SearchBarPopUpList({
    labelFieldId,
    descriptionFieldId,
    iconFieldId,
    urlFieldId,
    menu,
    directionIconsInPopUp,
    onItemClick,
}) {
    return (
        <DropdownMenu className="n2o-search-bar__popup_list">
            {menu.map((linkProps) => {
                const { id, disabled = false, linkType } = linkProps

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
                            onClick={onItemClick}
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
    onItemClick: PropTypes.func,
    labelFieldId: PropTypes.string,
    descriptionFieldId: PropTypes.string,
    iconFieldId: PropTypes.string,
    urlFieldId: PropTypes.string,
}

export default SearchBarPopUpList
