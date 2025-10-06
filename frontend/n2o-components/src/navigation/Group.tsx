import React, {
    KeyboardEvent,
    useState,
    useContext,
    createContext,
    useEffect,
} from 'react'
import classNames from 'classnames'

import { type GroupProps, Position, GroupView } from './types'
import { CHILD_ROOT_CLASS_NAME, GroupChildren, ROOT_CLASS_NAME_PARAM } from './helpers'

const GroupLevelContext = createContext(0)

export function Group({
    label,
    icon,
    className,
    style,
    children,
    collapsible = true,
    defaultState = GroupView.EXPANDED,
    iconPosition = Position.LEFT,
    [ROOT_CLASS_NAME_PARAM]: rootClassName,
    visible = true,
    enabled = true,
}: GroupProps) {
    const [isExpanded, setIsExpanded] = useState(
        collapsible ? defaultState === GroupView.EXPANDED : true,
    )
    const level = useContext(GroupLevelContext)

    useEffect(() => {
        if (collapsible) {
            setIsExpanded(defaultState === GroupView.EXPANDED)
        } else {
            setIsExpanded(true)
        }
    }, [defaultState, collapsible])

    if (!visible) { return null }

    const handleToggle = () => {
        if (!enabled || !collapsible) { return }
        setIsExpanded(!isExpanded)
    }

    const onKeyDown = (event: KeyboardEvent) => {
        if (event.key === 'Enter' || event.key === ' ') { handleToggle() }
    }

    const iconElement = icon ? <i className={icon} /> : null

    return (
        <section
            className={classNames('group', className, rootClassName, {
                'group--disabled': !enabled,
                'group--collapsed': !isExpanded,
            })}
            style={style}
        >
            <div
                className={classNames('group-header', { collapsible, expanded: isExpanded })}
                onClick={handleToggle}
                style={{ cursor: collapsible ? 'pointer' : 'default' }}
                role="button"
                tabIndex={enabled ? 0 : -1}
                aria-expanded={isExpanded}
                aria-disabled={!enabled}
                onKeyDown={onKeyDown}
            >
                {iconPosition === Position.LEFT && iconElement}
                <span className="group-label">{label}</span>
                {iconPosition === Position.RIGHT && iconElement}
            </div>
            {isExpanded && (
                <GroupLevelContext.Provider value={level + 1}>
                    <div
                        className={classNames('group-children', `level-${level + 1}`)}
                        style={{ paddingLeft: `${(level + 1) * 20}px` }}
                    >
                        <GroupChildren rootClassName={CHILD_ROOT_CLASS_NAME}>{children}</GroupChildren>
                    </div>
                </GroupLevelContext.Provider>
            )}
        </section>
    )
}
