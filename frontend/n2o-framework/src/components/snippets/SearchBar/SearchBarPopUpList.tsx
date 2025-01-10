import React from 'react'
import get from 'lodash/get'
import classNames from 'classnames'
import { DropdownMenu, DropdownItem } from 'reactstrap'

import { RenderLink, itemInSearchBarClassName } from './utils'
import { type SearchBarPopUpListProps } from './types'

export function SearchBarPopUpList({
    labelFieldId,
    descriptionFieldId,
    iconFieldId,
    urlFieldId,
    menu,
    directionIconsInPopUp,
    onItemClick,
}: SearchBarPopUpListProps) {
    return (
        <DropdownMenu className="n2o-search-bar__popup_list">
            {menu?.map((item) => {
                const { id, disabled = false, linkType, separateLink } = item

                const description = get(item, descriptionFieldId || '')
                const label = get(item, labelFieldId || '')
                const icon = get(item, iconFieldId || '')
                const href = get(item, urlFieldId || '')

                return (
                    <div
                        className={classNames('n2o-search-bar__popup_list__item-container', { disabled })}
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
                        {separateLink && <DropdownItem divider /> }
                    </div>
                )
            })}
        </DropdownMenu>
    )
}

export default SearchBarPopUpList
