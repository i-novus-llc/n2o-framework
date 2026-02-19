import { useState, useCallback, useMemo } from 'react'

import { NOOP_FUNCTION } from '../../utils/emptyTypes'

import { type useCollapseProps } from './types'
import { normalizeToArray, getNewActiveKeys } from './helpers'

export const useCollapse = ({
    activeKey,
    defaultActiveKey,
    accordion,
    collapsible,
    onChange = NOOP_FUNCTION,
}: useCollapseProps) => {
    const [uncontrolledActiveKeys, setUncontrolledActiveKeys] = useState<string[]>(() => normalizeToArray(defaultActiveKey))

    const isControlled = activeKey !== undefined

    const activeKeys = useMemo(
        () => (isControlled ? normalizeToArray(activeKey) : uncontrolledActiveKeys),
        [isControlled, activeKey, uncontrolledActiveKeys],
    )

    const togglePanel = useCallback((key: string) => {
        if (!collapsible) { return }

        const newActiveKeys = getNewActiveKeys(activeKeys, key, accordion)

        if (!isControlled) { setUncontrolledActiveKeys(newActiveKeys) }

        onChange(accordion ? newActiveKeys[0] || '' : newActiveKeys)
    }, [activeKeys, accordion, collapsible, isControlled, onChange])

    return { activeKeys, togglePanel }
}
