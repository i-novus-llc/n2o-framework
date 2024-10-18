import React, { useState } from 'react'
import { ButtonDropdown, DropdownToggle, DropdownMenu } from 'reactstrap'

interface DropdownProps {
    id: string;
    children: React.ReactNode;
    color: string;
    title: string;
    size?: string;
    disabled?: boolean;
}

const Dropdown: React.FC<DropdownProps> = ({
    id,
    children,
    color,
    title,
    size,
    disabled = false,
}) => {
    const [dropdownOpen, setDropdownOpen] = useState(false)

    const toggle = () => { setDropdownOpen(!dropdownOpen) }

    return (
        <ButtonDropdown isOpen={dropdownOpen} toggle={toggle}>
            <DropdownToggle color={color} size={size} id={id} disabled={disabled} caret>
                {title}
            </DropdownToggle>
            <DropdownMenu>{children}</DropdownMenu>
        </ButtonDropdown>
    )
}

export default Dropdown
