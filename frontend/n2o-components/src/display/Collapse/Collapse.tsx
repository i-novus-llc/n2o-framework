import React, { useMemo, createContext } from 'react'
import classNames from 'classnames'

import { type CollapseProps, type CollapseContextType } from './types'
import { useCollapse } from './useCollapse'
import { renderExpandIcon } from './renderExpandIcon'

export const CollapseContext = createContext<CollapseContextType>({
    activeKeys: [],
    togglePanel: () => {},
})

export const Collapse = ({
    className,
    children,
    collapsible = true,
    destroyInactivePanel = false,
    accordion = false,
    isVisible = true,
    onChange,
    defaultActiveKey,
    activeKey,
    expandIcon,
    style,
    ...rest
}: CollapseProps) => {
    const { activeKeys, togglePanel } = useCollapse({
        activeKey,
        defaultActiveKey,
        accordion,
        collapsible,
        onChange,
    })

    const value = useMemo(() => ({
        activeKeys,
        togglePanel,
        collapsible,
        destroyInactivePanel,
        accordion,
        expandIcon: expandIcon || renderExpandIcon,
    }), [activeKeys, togglePanel, collapsible, destroyInactivePanel, accordion, expandIcon])

    if (!isVisible) { return null }

    return (
        <CollapseContext.Provider value={value}>
            <div className={classNames('collapse-wrapper', className)} style={style} {...rest}>
                {children}
            </div>
        </CollapseContext.Provider>
    )
}

Collapse.displayName = '@n2o-components/display/Collapse'

export default Collapse
