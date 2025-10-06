import React, {
    useState,
    useContext,
    createContext,
    Context,
} from 'react'
import { Dropdown as ReactstrapDropdown, DropdownToggle, DropdownMenu } from 'reactstrap'
import classNames from 'classnames'

import { type DropdownProps, Trigger, Position } from './types'
import { CHILD_ROOT_CLASS_NAME, GroupChildren, ROOT_CLASS_NAME_PARAM } from './helpers'

export const LevelContext: Context<number> = createContext(0)

export function Dropdown({
    label,
    className,
    style,
    children,
    trigger = Trigger.CLICK,
    position = Position.RIGHT,
    enabled = true,
    visible = true,
    [ROOT_CLASS_NAME_PARAM]: rootClassName,
}: DropdownProps) {
    const [isOpen, setIsOpen] = useState(false)
    const currentLevel = useContext(LevelContext)

    if (!visible) { return null }

    const handleToggle = () => {
        if (trigger !== Trigger.HOVER) {
            setIsOpen(prev => !prev)
        }
    }

    const handleMouseEnter = () => {
        if (trigger === Trigger.HOVER && enabled) {
            setIsOpen(true)
        }
    }

    const handleMouseLeave = () => {
        if (trigger === Trigger.HOVER && enabled) {
            setIsOpen(false)
        }
    }

    return (
        <ReactstrapDropdown
            isOpen={isOpen}
            toggle={handleToggle}
            onMouseEnter={handleMouseEnter}
            onMouseLeave={handleMouseLeave}
            className={classNames(className, rootClassName)}
            style={style}
            disabled={!enabled}
            inNavbar={currentLevel > 0}
            direction={position}
        >
            <DropdownToggle
                tag="div"
                caret
                style={{
                    display: 'flex',
                    flexDirection: position === Position.LEFT ? 'row-reverse' : 'row',
                    alignItems: 'center',
                    gap: '0.5rem',
                    cursor: enabled ? 'pointer' : 'default',
                    opacity: enabled ? 1 : 0.5,
                }}
                disabled={!enabled}
            >
                {label}
            </DropdownToggle>
            <DropdownMenu
                className="group-children"
                style={{
                    paddingLeft: currentLevel > 0 ? `${currentLevel * 16}px` : 0,
                    position: currentLevel > 0 ? 'absolute' : undefined,
                    left: currentLevel > 0 ? '100%' : undefined,
                    top: currentLevel > 0 ? 0 : undefined,
                    marginLeft: 0,
                }}
                modifiers={currentLevel > 0 ? [{ name: 'flip', enabled: currentLevel < 3 }] : undefined}
            >
                <LevelContext.Provider value={currentLevel + 1}>
                    <GroupChildren rootClassName={CHILD_ROOT_CLASS_NAME}>{children}</GroupChildren>
                </LevelContext.Provider>
            </DropdownMenu>
        </ReactstrapDropdown>
    )
}
