import React, { useContext, useRef } from 'react'
import classNames from 'classnames'

import { CollapseContext } from '../Collapse/Collapse'

import { usePanelHeight } from './usePanelHeight'
import { Header } from './Header'
import { Content } from './Content'
import { type PanelProps } from './types'

export const Panel = ({
    header,
    children,
    className,
    headerClass,
    hasSeparator,
    collapsible: panelCollapsible,
    forceRender = false,
    showArrow = true,
    disabled = false,
    style,
    extra,
    openAnimation = { duration: 300, easing: 'ease' },
    key,
    panelKey,
    destroyInactivePanel: panelDestroyInactivePanel,
}: PanelProps) => {
    const {
        activeKeys,
        togglePanel,
        collapsible: globalCollapsible,
        destroyInactivePanel: globalDestroyInactivePanel,
        expandIcon,
    } = useContext(CollapseContext)

    const contentRef = useRef<HTMLDivElement>(null)
    const keyValue = panelKey || key || ''
    const collapsible = typeof panelCollapsible === 'boolean' ? panelCollapsible : globalCollapsible
    const isActive = activeKeys.includes(keyValue) || !collapsible
    const isDisabled = disabled || !collapsible

    usePanelHeight({ isActive, ref: contentRef })

    const onClick = () => {
        if (!isDisabled) { togglePanel(keyValue) }
    }

    const destroyInactivePanel = panelDestroyInactivePanel ?? globalDestroyInactivePanel

    return (
        <div
            className={classNames('collapse-panel', className, {
                'collapse-panel-active': isActive,
                'collapse-panel-disabled': isDisabled,
            })}
            style={style}
        >
            <Header
                header={header}
                headerClass={headerClass}
                showArrow={showArrow}
                expandIcon={expandIcon}
                extra={extra}
                hasSeparator={hasSeparator}
                isActive={isActive}
                isDisabled={isDisabled}
                onClick={onClick}
                collapsible={collapsible}
            />
            <Content
                contentRef={contentRef}
                isActive={isActive}
                openAnimation={openAnimation}
                forceRender={forceRender}
                destroyInactivePanel={destroyInactivePanel}
            >
                {children}
            </Content>
        </div>
    )
}

Panel.displayName = '@n2o-components/display/Panel'

export default Panel
