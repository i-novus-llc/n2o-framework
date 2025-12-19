import React, {
    useState,
    useContext,
    createContext,
    Context,
} from 'react'
import { Dropdown as ReactstrapDropdown, DropdownToggle, DropdownMenu } from 'reactstrap'

import { type DropdownProps, Trigger, Position } from './types'
import { mapWithClassName } from './helpers'

export const LevelContext: Context<number> = createContext(0)

export function Dropdown({
    label,
    className,
    style,
    children,
    trigger = Trigger.CLICK,
    position = Position.RIGHT,
    disabled = false,
}: DropdownProps) {
    const [isOpen, setIsOpen] = useState(false)
    const currentLevel = useContext(LevelContext)

    const handleToggle = () => {
        if (trigger !== Trigger.HOVER) {
            setIsOpen(prev => !prev)
        }
    }

    const handleMouseEnter = () => {
        if (trigger === Trigger.HOVER && !disabled) {
            setIsOpen(true)
        }
    }

    const handleMouseLeave = () => {
        if (trigger === Trigger.HOVER && !disabled) {
            setIsOpen(false)
        }
    }

    return (
        <ReactstrapDropdown
            isOpen={isOpen}
            toggle={handleToggle}
            onMouseEnter={handleMouseEnter}
            onMouseLeave={handleMouseLeave}
            className={className}
            style={style}
            disabled={disabled}
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
                    cursor: disabled ? 'default' : 'pointer',
                    opacity: disabled ? 0.5 : 1,
                }}
                disabled={disabled}
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
                    {mapWithClassName(children)}
                </LevelContext.Provider>
            </DropdownMenu>
        </ReactstrapDropdown>
    )
}
