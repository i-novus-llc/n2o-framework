import React, { useState, useRef } from 'react'
import classNames from 'classnames'

import { NavItemImage } from '@i-novus/n2o-components/lib/display/NavItemImage'

import { Tooltip } from '../../../../../components/snippets/Tooltip/TooltipHOC'
import { Icon } from '../../../utils'

interface DropdownWrapperProps {
    sidebarOpen: boolean
    title?: string
    children?: JSX.Element
    icon?: string
    src: string
    showContent: boolean
    isMiniView: boolean
    id: string
    imageSrc?: string
    imageShape?: string
}

export function DropdownWrapper(props: DropdownWrapperProps) {
    const {
        sidebarOpen,
        title,
        children,
        icon,
        src,
        showContent,
        isMiniView,
        id,
        imageSrc,
        imageShape,
    } = props
    const ref = useRef(null)
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
            <Tooltip hint={title} placement="right">
                <div className="n2o-sidebar__item-dropdown" ref={ref}>
                    <div
                        onClick={toggle}
                        className={itemDropdownClass}
                        id={id}
                    >
                        <Icon icon={icon} title={title || ''} src={src} sidebarOpen={sidebarOpen} hasSubItems />
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
            </Tooltip>
            {isOpen && (<div className={subItemsClass}>{children}</div>)}
        </>
    )
}

export default DropdownWrapper
