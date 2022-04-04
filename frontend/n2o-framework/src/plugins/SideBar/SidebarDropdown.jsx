import React, { useState } from 'react'
import PropTypes from 'prop-types'
import classNames from 'classnames'

import { NavItemImage } from '../../components/snippets/NavItemImage/NavItemImage'
import { Tooltip } from '../../components/snippets/Tooltip/Tooltip'

import { Icon } from './utils'

/**
 * Sidebar Dropdown Item
 * @param sidebarOpen - флаг сжатия сайдбара
 * @param title - текст дропдауна
 * @param children - subItems
 * @param isOpen - флаг видимости subItems
 * @param type - тип
 * @param id - id
 * @returns {*}
 * @constructor
 */
function SidebarDropdown({
    sidebarOpen,
    title,
    children,
    icon,
    type,
    showContent,
    isMiniView,
    id,
    imageSrc,
    imageShape,
}) {
    const [isOpen, setOpen] = useState(false)
    const toggle = () => setOpen(!isOpen)

    const itemDropdownClass = classNames(
        'n2o-sidebar__item-dropdown-label',
        {
            'pl-3': !isMiniView,
            mini: isMiniView,
        },
    )

    const subItemsClass = classNames(
        'n2o-sidebar__subitems',
        {
            visible: showContent,
        },
    )

    return (
        <>
            <Tooltip
                label={(
                    <div className="n2o-sidebar__item-dropdown">
                        <div
                            onClick={toggle}
                            className={itemDropdownClass}
                            id={id}
                        >
                            <Icon icon={icon} title={title} type={type} sidebarOpen={sidebarOpen} hasSubItems />
                            <NavItemImage imageSrc={imageSrc} title={title} imageShape={imageShape} />
                            <span className={classNames(
                                'n2o-sidebar__item-title',
                                {
                                    mini: isMiniView,
                                    visible: showContent,
                                },
                            )
                            }
                            >
                                {title}
                            </span>
                            <i className={classNames(
                                'align-self-center w-100 d-flex justify-content-end',
                                'n2o-sidebar__item-dropdown-toggle',
                                {
                                    'fa fa-angle-up': isOpen,
                                    'fa fa-angle-down': !isOpen,
                                    mini: isMiniView,
                                },
                            )}
                            />
                        </div>
                    </div>
                )}
                hint={title}
                placement="right"
            />
            {isOpen && (<div className={subItemsClass}>{children}</div>)}
        </>
    )
}

SidebarDropdown.propTypes = {
    sidebarOpen: PropTypes.bool,
    title: PropTypes.string,
    children: PropTypes.node,
    showContent: PropTypes.bool,
    isMiniView: PropTypes.bool,
    icon: PropTypes.string,
    type: PropTypes.string,
    id: PropTypes.string,
    imageSrc: PropTypes.string,
    imageShape: PropTypes.string,
}

export default SidebarDropdown
